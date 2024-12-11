//
//  FavoriteTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import BackgroundTasks
import Shared
import KMPObservableViewModelSwiftUI
import AuthenticationServices

struct FavoriteTracksView: View {
    
    @StateViewModel
    var viewModel = KoinDependencies.shared.getFavoriteTracksViewModel()
    
    @State private var isUndoVisible = false  // Flag for showing the undo button
    @State private var isShowingConfirmation = false  // Flag for showing confirmation dialog
    @State private var trackToDelete: Track? = nil  // Track pending deletion
    
    var body: some View {
            VStack {
                TopBarView(title: "Favorite Tracks", imageName: "arrow.clockwise", onAction: {
                    viewModel.onAction(action: FavoriteTracksActionSyncronizeTracks())
                })
                
                List {
                    ForEach(viewModel.state.tracks, id: \.id) { track in
                        TrackRow(track: track)
                            .listRowSeparator(.hidden)
                            .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                    }
                    .onDelete(perform: prepareForDelete)
                    // Load More Button
                    Button(action: {
                        viewModel.onAction(action: FavoriteTracksActionGetNextFavoriteTracks())
                    }) {
                        Text("Load More")
                            .padding()
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .cornerRadius(8)
                    }
                }
                .listStyle(.plain)
                if isUndoVisible {
                    Button("Undo") {
                        restoreDeletedTrack()
                    }
                    .padding()
                    .background(Color.green)
                    .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                    .cornerRadius(8)
                    .transition(.opacity)
                }
        }.background(Color(.PRIMARY_DARK))
            .shadow(radius: 5)
            .alert("Confirm Deletion", isPresented: $isShowingConfirmation) {
                        Button("Delete", role: .destructive) {
                            if let track = trackToDelete {
                                deleteTrack(track)
                            }
                        }
                        Button("Cancel", role: .cancel) {}
                    } message: {
                        Text("Are you sure you want to delete this track?")
                    }
    }
    
    private func prepareForDelete(at offsets: IndexSet) {
            guard let index = offsets.first else { return }
            trackToDelete = viewModel.state.tracks[index]
            isShowingConfirmation = true
        }
    
    private func deleteTrack(_ trackToDelete: Track) {
//            guard let index = offsets.first else { return }
//            let trackToDelete = viewModel.state.tracks[index]
            
            UserDefaults.standard.set(trackToDelete.id, forKey: "backgroundDeleteTrackID")
            
        viewModel.onAction(action: FavoriteTracksActionOnRemoveTrackFromCashedList(track: trackToDelete))
            
            withAnimation {
                isUndoVisible = true
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
                if self.isUndoVisible {
                    withAnimation {
                        self.isUndoVisible = false
                    }
                }
            }
            
            scheduleBackgroundDeleteTask(track: trackToDelete)
        }
        
        // Schedule the background task to delete the track
        private func scheduleBackgroundDeleteTask(track: Track) {
            let request = BGProcessingTaskRequest(identifier: "com.app.deleteTrack")
            request.requiresNetworkConnectivity = true
            request.earliestBeginDate = Date(timeIntervalSinceNow: 10)
            
            do {
                try BGTaskScheduler.shared.submit(request)
                print("Background delete task scheduled for track id \(track.id)")
            } catch {
                print("Failed to submit background task: \(error.localizedDescription)")
            }
        }
        
        private func restoreDeletedTrack() {
            viewModel.onAction(action: FavoriteTracksActionOnRestoreLastRemovedTrackToCashedList())
            
            isUndoVisible = false
            
            // Cancel the scheduled background task
            BGTaskScheduler.shared.cancel(taskRequestWithIdentifier: "com.app.deleteTrack")
            
            // Remove the stored track ID
            UserDefaults.standard.removeObject(forKey: "backgroundDeleteTrackID")
        }
}

