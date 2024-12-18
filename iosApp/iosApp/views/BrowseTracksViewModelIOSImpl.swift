//
//  BrowseTracksViewModel2.swift
//  iosApp
//
//  Created by Martin Hristev on 17.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import Shared
import Combine
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesRxSwift
import SwiftUICore
import SwiftUI

// Why cancellable?
// https://github.com/rickclephas/KMP-ObservableViewModel?tab=readme-ov-file#cancellable-viewmodel

class BrowseTracksViewModelIOSImpl: Shared.BrowseTracksViewModel, Cancellable {
    
    @State private var timer: Timer? = nil
    @Published var message: String? = nil
    @Published var messageColor: Color = Color.blue
    @Published var stateIOS: BrowseTracksState = BrowseTracksState(tracks: [], isLoadingTracks: false, isSavingToFavoritesSuccess: false, errorString: nil)
    
    private var cancellables = Set<AnyCancellable>()

    init() {
        super.init(trackRepository: KoinDependencies.shared.getTrackRepository())
       createPublisher(for: self.stateFlow)
           .assertNoFailure()
           .sink { newState in
               self.stateIOS = newState}
           .store(in: &cancellables)
    }
    
    func cancel() {
        cancellables = []
    }
    
    func searchTimer(newValue: String) {
        timer?.invalidate()
        timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false) { _ in
            self.performRequest(with: newValue)
        }
    }
    
    func showMessage(messageReceived:String, color: Color) {
        withAnimation {
            message = messageReceived
            messageColor = color
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
           withAnimation {
               self.message = nil
           }
       }
    }
    
    func observeUIEvents() {
        createObservable(for: self.uiEvents)
               .subscribe(onNext: { event in
                   switch(event) {
                       case is Shared.UIEvent.ShowError:
                           if let errorEvent = event as? Shared.UIEvent.ShowError {
                               self.showMessage(messageReceived: errorEvent.message, color: Color.red)
                           }
                       case is Shared.UIEvent.ShowSuccess:
                           if let successEvent = event as? Shared.UIEvent.ShowSuccess {
                               self.showMessage(messageReceived: successEvent.message, color: Color.green)
                           }
                       default: break
                       
                   }

               }, onError: { error in
                   print("Received error: \(error)")
               })
    }
    
    func performRequest(with query: String) {
        self.onAction(action: BrowseTracksActionOnSearch(query: query))
    }
    
    
}
