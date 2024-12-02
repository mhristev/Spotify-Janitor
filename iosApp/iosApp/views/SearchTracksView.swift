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
    let songs = [
            Song(name: "gloria (with sza)", artist: "Kendrick Lamar - SZA", imageName: "gloria_image"),
            Song(name: "Drinking with Cupid", artist: "VOILÀ", imageName: "drinking_with_cupid_image"),
            Song(name: "Someone Like You", artist: "Mac Miller", imageName: "someone_like_you_image"),
            Song(name: "Domo23", artist: "Tyler, The Creator", imageName: "domo23_image"),
            Song(name: "Two Feet", artist: "Young Franco, Pell, Dana Williams", imageName: "two_feet_image"),
            Song(name: "Supposed To Be", artist: "Louis Futon, Duckwrth, Baegod", imageName: "supposed_to_be_image"),
            Song(name: "Lost Lately - pure alternative version", artist: "San Holo", imageName: "lost_lately_image"),
            Song(name: "Reflections", artist: "George Walter", imageName: "reflections_image"),
            Song(name: "The Spot", artist: "Your Smith", imageName: "the_spot_image"),
            Song(name: "Baby Boy Is Drunk", artist: "Powers Pleasant, AG Club & AUDREY NUNA", imageName: "baby_boy_image"),
            Song(name: "SIN MIEDO", artist: "JPEGMAFIA", imageName: "sin_miedo_image"),
            Song(name: "Honey", artist: "Lenny Kravitz", imageName: "honey_image")
        ]
    
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
                List(songs) { song in
                    TrackRow(trackName: song.name, artist: song.artist, imageName: song.imageName)
                        .listRowSeparator(.hidden)
                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .listStyle(.plain)
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
