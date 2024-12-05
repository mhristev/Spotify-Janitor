//
//  ViewTopBar.swift
//  iosApp
//
//  Created by Martin Hristev on 4.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct TopBarView: View {
    
    var title: String
    var imageName: String
    var onAction: () -> Void
    
    var body: some View {
        HStack {
            Text(title)
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(Color(.PRIMARY_TEXT_WHITE))
                .padding(.leading, 10)
            
            Spacer()
            
            Button(action: {
                onAction()
            }) {
                Image(systemName: imageName)
                    .font(.system(size: 20))
                    .foregroundColor(.white)
                    .padding(.trailing, 16)
            }
        }
        .padding(.top, 20)
        .background(Color(.PRIMARY_DARK))
    }
}
struct TopBarView_Previews: PreviewProvider {
    static var previews: some View {
        TopBarView(title: "Favorite Tracks", imageName: "arrow.clockwise", onAction: {
            print("Refreshing...")
        })
    }
}
