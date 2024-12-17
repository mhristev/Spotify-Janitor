//
//  TrackRow.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared
import SDWebImageSwiftUI

struct TrackRow: View {
    var track: Track
    var onAddToFavoritesClick: ((Track) -> Void)?
//    var checkIfTrackAlreadyInFavorite: (Track) -> Bool
    // https://github.com/SDWebImage/SDWebImageSwiftUI
    var body: some View {
        HStack {
            WebImage(url: URL(string: track.album.imageUrl)) { image in
                    image.resizable()
                    .scaledToFit()
                    .frame(width: 60, height: 60)
                    .cornerRadius(8)
                } placeholder: {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                        .frame(width: 60, height: 60)
                }
                .resizable()
                .transition(.fade(duration: 0.5))
                .scaledToFit()
                .frame(width: 60, height: 60, alignment: .center)
            
            VStack(alignment: .leading) {
                Text(track.name)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                    .lineLimit(1)
                    .padding(.bottom, 2)
                
                Text(track.artists.map { $0.name }.joined(separator: " · "))
                    .font(.system(size: 14))
                    .foregroundStyle(Color(.SECONDARY_TEXT_GREY))
                    .lineLimit(1)
            }
            .padding(.leading, 10)
            
            Spacer()
            
            if let onAddToFavorites = onAddToFavoritesClick {
                Button(action: {
                    onAddToFavorites(track)
                }) {
                    Image(systemName: "plus.circle")
                        .font(.system(size: 20))
                        .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                        .padding(.trailing, 16)
                }
            }

        }
        .padding(.vertical, 10)
        .background(Color(.PRIMARY_DARK))
        
       
    }
}
