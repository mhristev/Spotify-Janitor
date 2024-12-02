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
    var trackName: String
    var artist: String
    var imageName: String
    
    var body: some View {
        HStack {
            Image(imageName)
                .resizable()
                .scaledToFit()
                .frame(width: 60, height: 60)
                .cornerRadius(8)
            
            VStack(alignment: .leading) {
                Text(trackName)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(.white)
                    .lineLimit(1)
                    .padding(.bottom, 2)
                
                Text(artist)
                    .font(.system(size: 14))
                    .foregroundColor(.gray)
                    .lineLimit(1)
            }
            .padding(.leading, 10)
            
            Spacer()
        }
        .padding(.vertical, 10)
        .background(Color.black)
        
       
    }
}
