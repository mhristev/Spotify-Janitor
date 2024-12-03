//
//  FavoriteTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI

struct FavoriteTracksView: View {

    let inter: TrackRepository = KoinDependencies.shared.getTrackRepository()
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
                        Image(systemName: "arrow.clockwise")
                            .font(.system(size: 20))
                            .foregroundColor(.white)
                            .padding(.trailing, 16)
                    }
                }
                .padding(.top, 20)
                .background(Color.black)
                
                // Track list
//                List(viewModel.state.tracks, id: \.id) { track in
//                    TrackRow(track: track)
//                        .listRowSeparator(.hidden)
//                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
//                }
//                .listStyle(.plain)
        }
        .background(Color.black)
        .shadow(radius: 5)
        }
}


#Preview {
    FavoriteTracksView()
}
 
