//
//  UIAction.swift
//  iosApp
//
//  Created by Martin Hristev on 17.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct KMPUIAction: View {
    var message: String
    var color: Color
    
    var body: some View {
        
        Text(message)
            .font(.system(size: 14, weight: .medium))
            .multilineTextAlignment(.center)
            .padding()
            .frame(maxWidth: .infinity)
            .background(
                RoundedRectangle(cornerRadius: 0) // No corners for a banner-style
                    .fill(color.opacity(0.6))
            )
            .foregroundColor(.white)
            .padding(.horizontal, 16)
            .transition(.opacity.combined(with: .move(edge: .top)))
            .animation(.easeInOut(duration: 0.3), value: message)
    }
}

