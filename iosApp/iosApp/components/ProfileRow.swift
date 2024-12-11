//
//  ProfileRow.swift
//  iosApp
//
//  Created by Martin Hristev on 10.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct ProfileRow: View {
    var title: String
    var value: String
    
    var body: some View {
        HStack {
            Text(title)
                .font(.headline)
                .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                .padding(.leading, 20)
            
            Spacer()
            
            Text(value)
                .font(.body)
                .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                .padding(.trailing, 20)
        }
        .padding(.vertical, 10)
        .overlay(
            Rectangle()
                .frame(height: 1)
                .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                .padding(.top, 10),
            alignment: .bottom
        )
    }
}
#Preview {
    UserProfileView()
}
