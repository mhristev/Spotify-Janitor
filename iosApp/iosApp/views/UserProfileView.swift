//
//  UserProfileView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct UserProfileView: View {
    var body: some View {
        VStack {
            // Top Bar with title and logout button
            VStack {
                HStack {
                    Text("Spotify Profile")
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding(.leading, 16)
                    
                    Spacer() // Pushes the logout button to the right
                    
                    Button(action: {
                        // Logout action goes here
                        print("Logging out...")
                    }) {
                        Text("Logout")
                            .font(.body)
                            .foregroundColor(.white)
                            .padding(.trailing, 16)
                    }
                }
                
                // Profile Header
                VStack {
                    Image("profile_picture") // Ensure this image is included in your assets
                        .resizable()
                        .scaledToFill()
                        .frame(width: 120, height: 120)
                        .clipShape(Circle())
                        .overlay(Circle().stroke(Color.white, lineWidth: 4))
                        .shadow(radius: 10)
                    
                    Text("Martin Hristev")
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding(.top, 8)
                        .padding(.bottom, 10)
                    
                }
                .frame(maxWidth: .infinity)
     
                .padding(.top, 30)
            }.background(Color.purple)
            
            
            VStack {
                ProfileRow(title: "EMAIL", value: "mhristev03@gmail.com")
                ProfileRow(title: "COUNTRY", value: "BG")
                ProfileRow(title: "FOLLOWERS", value: "27")
                ProfileRow(title: "PRODUCT", value: "premium")
            }
            .padding(.top, 20)
            
            Spacer()

        }
        .background(Color.black)
    }
}

struct ProfileRow: View {
    var title: String
    var value: String
    
    var body: some View {
        HStack {
            Text(title)
                .font(.headline)
                .foregroundColor(.white)
                .padding(.leading, 20)
            
            Spacer()
            
            Text(value)
                .font(.body)
                .foregroundColor(.white)
                .padding(.trailing, 20)
        }
        .padding(.vertical, 10)
        .overlay(
            Rectangle()
                .frame(height: 1)  // Set the height of the border
                .foregroundColor(.white)  // Color of the bottom border
                .padding(.top, 10), // Adjust padding to position the border at the bottom
            alignment: .bottom
        )
    }
}
#Preview {
    UserProfileView()
}
