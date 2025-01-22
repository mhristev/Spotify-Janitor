//
//  FavoriteTracksViewModel2.swift
//  iosApp
//
//  Created by Martin Hristev on 17.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Shared
import Combine
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesRxSwift
import SwiftUI

class FavoriteTracksViewModelIOSImpl: Shared.FavoriteTracksViewModel {
    
    @Published var stateIOS: FavoriteTracksState = FavoriteTracksState(cachedTracks: [], errorString: nil, isLoadingSync: false, isLoadingNextTracks: false, isShowingUndoButton: false)


    @Published var isUndoVisible = false
    @Published var isShowingConfirmation = false
    @Published var trackToDelete2: Track? = nil
    @Published var errorMessage: String? = nil


    private var cancellables = Set<AnyCancellable>()
    private var restoreTimer: AnyCancellable? = nil
    
    init() {
        super.init(trackRepository: KoinDependencies.shared.getTrackRepository())

        createPublisher(for: self.stateFlow)
            .assertNoFailure()
            .sink { [weak self] newState in
                self?.stateIOS = newState
            }
            .store(in: &cancellables)
        
        observeUIEvents()
    }
    
    private func observeUIEvents() {
        createObservable(for: self.uiEvents)
            .subscribe(onNext: { [weak self] event in
                switch(event) {
                case is Shared.UIEvent.ShowError:
                    if let errorEvent = event as? Shared.UIEvent.ShowError {
                        self?.showErrorMessage(message: errorEvent.message)
                    }
                default: break
                }
            }, onError: { error in
                print("Received error: \(error)")
            })
    }
    
    private func showErrorMessage(message: String) {
        withAnimation {
            self.errorMessage = message
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            withAnimation {
                self.errorMessage = nil
            }
        }
    }
    
    func confirmDeleteTrack(_ track: Track) {
        self.trackToDelete2 = track
        self.isShowingConfirmation = true
    }
    
    func deleteConfirmedTrack() {
        guard let track = trackToDelete2 else { return }
        // Locally remove track first
        self.onAction(action: FavoriteTracksActionOnRemoveTrackLocally(track: track))
        
        withAnimation {
            self.isUndoVisible = true
        }
        
        // Start a timer for undo action; if not undone, proceed with global removal
        restoreTimer?.cancel()
        restoreTimer = Just(())
            .delay(for: .seconds(2), scheduler: DispatchQueue.main)
            .sink { [weak self] _ in
                guard let self = self else { return }
                if self.isUndoVisible {
                    withAnimation {
                        self.isUndoVisible = false
                    }
                    // Remove globally from server if undo not performed
                    self.onAction(action: FavoriteTracksActionOnRemoveTrackByIdGlobally(trackId: track.id))
                }
            }
    }
    
    func restoreDeletedTrack() {
        self.onAction(action: FavoriteTracksActionOnRestoreLastRemovedTrackLocally())
        withAnimation {
            self.isUndoVisible = false
        }
        restoreTimer?.cancel()
    }
    
    func loadMoreFavorites() {
        self.onAction(action: FavoriteTracksActionOnGetNextFavoriteTracks())
    }
    
    func syncFavorites() {
        self.onAction(action: FavoriteTracksActionOnSyncTracks())
    }
}
