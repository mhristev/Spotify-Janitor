//
//  ShowMoreButton.swift
//  iosApp
//
//  Created by Martin Hristev on 17.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

// MARK: - Button View
struct LoadMoreButtonView: View {
    var action: () -> Void // Pass the action for the button
    
    var body: some View {
        Button(action: {
            action()
        }) {
            Text("Load More")
                .padding()
                .foregroundColor(.white) // Text color
                .frame(maxWidth: .infinity) // Optional: Expand horizontally
                .background(Color.black) // Background color
                .cornerRadius(8) // Rounded corners for background
                .overlay(
                    RoundedRectangle(cornerRadius: 8) // Rounded border
                        .stroke(Color.white, lineWidth: 2)
                )
        }
        .padding(.horizontal) // Optional horizontal padding
    }
}

#Preview {
    LoadMoreButtonView(action: {print("hero")})
}
