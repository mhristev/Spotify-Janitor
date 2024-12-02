//
//  FavoriteTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
struct Song: Identifiable {
    let id = UUID()
    let name: String
    let artist: String
    let imageName: String // Image name for the album cover
}
struct FavoriteTracksView: View {
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
            VStack {
                // Top Bar with title and refresh button
                HStack {
                    Text("Favorite Tracks")
                        .font(.title3)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding(.leading, 10)
                    
                    Spacer()

                    Button(action: {
                        // Refresh action logic goes here
                        print("Refreshing...")
                    }) {
                        Image(systemName: "arrow.clockwise.circle.fill")
                            .font(.system(size: 40))
                            .foregroundColor(.white)
                            .padding(.trailing, 16)
                    }
                }
                .padding(.top, 20)
                .background(Color.black)
                
                // Track list
                List(songs) { song in
                    TrackRow(trackName: song.name, artist: song.artist, imageName: song.imageName)
                        .listRowSeparator(.hidden)
                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .listStyle(.plain)
        }
        .background(Color.black)
        .shadow(radius: 5)
        }
}


#Preview {
//    TrackRow(trackName: "SIN MIEDO", artist: "JPEGMAFIA", imageName: "sin_miedo_image")
    FavoriteTracksView()
}
 
