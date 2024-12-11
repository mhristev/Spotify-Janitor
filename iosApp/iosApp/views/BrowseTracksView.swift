//
//  SearchTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import KMPObservableViewModelSwiftUI
import Shared
import Combine

import SwiftUI
import Combine

struct BrowseTracksView: View {
    @StateViewModel
    var viewModel = KoinDependencies.shared.getBrowseTracksViewModel()
    
    @State private var searchQuery: String = ""
    @State private var timer: Timer? = nil
    @State private var showMessage: Bool = false
    @State private var messageText: String = ""
    
    func performRequest(with query: String) {
        viewModel.onAction(action: BrowseTracksActionOnSearch(query: query))
    }
    
    private func handleAddToFavoritesSuccess(trackName: String) {
            withAnimation {
                messageText = "\(trackName) added to favorites!"
                showMessage = true
            }
            
            // Hide the message after 3 seconds
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                withAnimation {
                    showMessage = false
                }
            }
        }
    
    var body: some View {
  
            VStack {
                TopBarView(title: "Browse", imageName: nil, onAction: nil)
                TextField("What do you want to listen to?", text: $searchQuery)
                           .padding(7)
                           .padding(.leading, 30) // Space for the icon
                           .background(Color(.systemGray6))
                           .cornerRadius(8)
                           .overlay(
                               HStack {
                                   Image(systemName: "magnifyingglass")
                                       .foregroundColor(.gray)
                                       .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
                                       .padding(.leading, 8)
                                   Spacer()
                               }
                           )
                           .padding(.horizontal, 10)
                           .onChange(of: searchQuery) { oldvalue, newValue in
                               timer?.invalidate()
                               timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false) { _ in
                                   performRequest(with: newValue)
                               }
                           }
                List(viewModel.state.tracks, id: \.id) { track in
                    TrackRow(track: track,
                             onAddToFavoritesClick: { _ in
                        viewModel.onAction(action: BrowseTracksActionOnTrackAddToFavorites(track: track))
                        handleAddToFavoritesSuccess(trackName: track.name)
                    })
                    .listRowSeparator(.hidden)
                    .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .listStyle(.plain)
                if showMessage {
                    Text(messageText)
                        .padding()
                        .background(Color.green.opacity(0.8))
                        .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                        .cornerRadius(8)
                        .transition(.opacity)
                }
            }
            .navigationBarHidden(true)
            .navigationTitle("Search")
            .background(Color(.PRIMARY_DARK))
            .shadow(radius: 5)
        
  
        
    }
}
    


#Preview {
    BrowseTracksView()
}
