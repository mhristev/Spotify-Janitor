//
//  SearchTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct SearchTracksView: View {
    @State private var searchQuery = ""
    let debouncePeriod: Double = 0.5
    var body: some View {
        NavigationView {
            VStack {
                HStack {
                    Text("Search")
                        .font(.title3)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding(.leading, 10)
                    
                    Spacer()
                }
                
                // Search TextField with debouncing
                TextField("What do you want to listen to?", text: $searchQuery)
                    .padding()
                    .background(RoundedRectangle(cornerRadius: 8).fill(Color.white))
//                    .onChange(of: searchQuery) { newValue in
//                        debounceSearchQuery(newValue)
//                    }
                    .padding()

                // Lazy list of tracks
                // Track list
//                List(songs) { song in
//                    TrackRow(trackName: song.name, artist: song.artist, imageName: song.imageName)
//                        .listRowSeparator(.hidden)
//                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
//                }
//                .listStyle(.plain)
            }
            .background(Color.black)
        }
//        .onAppear {
//            viewModel.searchTracks("") // Load initial data if needed
//        }
    }
    
//    // Debounce logic
//    private func debounceSearchQuery(_ newQuery: String) {
//        timer?.invalidate()  // Invalidate the previous timer
//        timer = Timer.scheduledTimer(withTimeInterval: debouncePeriod, repeats: false) { _ in
//            if newQuery != lastQuery {
//                lastQuery = newQuery
//                viewModel.searchTracks(newQuery)
//            }
//        }
//    }
}


#Preview {
    SearchTracksView()
}
