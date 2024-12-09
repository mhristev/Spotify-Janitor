//
//  TrackRow.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct TrackRow: View {
    var track: Track
    var onAddToFavoritesClick: ((Track) -> Void)?
//    var checkIfTrackAlreadyInFavorite: (Track) -> Bool
    
    var body: some View {
        HStack {
            AsyncImage(url: URL(string: track.album.imageUrl)) { phase in
                switch phase {
                case .empty:
                    // This is the loading state
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                        .frame(width: 60, height: 60)
                case .success(let image):
                    // Successfully loaded image
                    image
                        .resizable()
                        .scaledToFit()
                        .frame(width: 60, height: 60)
                        .cornerRadius(8)
                case .failure:
                    // This is the error state
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundColor(.red)
                        .frame(width: 60, height: 60)
                @unknown default:
                    // Handle unknown future states
                    EmptyView()
                }
            }
            
            VStack(alignment: .leading) {
                Text(track.name)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(.white)
                    .lineLimit(1)
                    .padding(.bottom, 2)
                
                Text(track.artists.map { $0.name }.joined(separator: " · "))
                    .font(.system(size: 14))
                    .foregroundColor(.gray)
                    .lineLimit(1)
            }
            .padding(.leading, 10)
            
            Spacer()
            
//            if !checkIfTrackAlreadyInFavorite(track) {
                if let onAddToFavorites = onAddToFavoritesClick {
                    Button(action: {
                        onAddToFavorites(track)
                    }) {
                        Image(systemName: "plus.circle")
                            .font(.system(size: 20))
                            .foregroundColor(.white)
                            .padding(.trailing, 16)
                    }
                }
//            }
        }
        .padding(.vertical, 10)
        .background(Color(.PRIMARY_DARK))
        
       
    }
}
