//
//  UserProfileView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared
    import KMPObservableViewModelSwiftUI

    struct UserProfileView: View {
        
        @StateViewModel
        var viewModel = KoinDependencies.shared.getUserProfileViewModel()
        
        @State var redirect = false
        
        func logout() {
            viewModel.onAction(action: UserProfileActionOnLogout())
            self.redirect = viewModel.navigateToLogin
        }
        
        var body: some View {
           
            VStack {
                VStack {
                    HStack {
                        Text("Spotify Profile")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(Color(.PRIMARY_TEXT_WHITE))
                            .padding(.leading, 16)
                        
                        Spacer()
                        
                        Button(action: {
                            print("Logging out...")
                            logout()
                        }) {
                            Image(systemName: "rectangle.portrait.and.arrow.right")
                                .font(.system(size: 20))
                                .foregroundColor(.white)
                                .padding(.trailing, 16)
                        }
                    }
                    
                    VStack {
                        
                        AsyncImage(url: URL(string: viewModel.state.user?.imageUrl ?? "")) { phase in
                            switch phase {
                            case .empty:
                                // This is the loading state
                                ProgressView()
                                    .progressViewStyle(CircularProgressViewStyle())
                                    .frame(width: 128, height: 128)
                            case .success(let image):
                                // Successfully loaded image
                                image
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 188, height: 188)                                .clipShape(Circle())
                                    .overlay(Circle().stroke(Color(.PRIMARY_TEXT_WHITE), lineWidth: 1))
                                    .shadow(radius: 10)
                            case .failure:
                                
                                Image(systemName: "exclamationmark.triangle.fill")
                                    .foregroundColor(.red)
                                    .frame(width: 60, height: 60)
                            @unknown default:
                                EmptyView()
                            }
                        }
                        
                        Text(viewModel.state.user?.displayName ?? "")
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .padding(.top, 8)
                            .padding(.bottom, 10)
                        
                    }
                    .frame(maxWidth: .infinity)
                    
                    .padding(.top, 30)
                }.background(Color(.PRIMARY_PURPLE))
                
                
                VStack {
                    ProfileRow(title: "EMAIL", value: viewModel.state.user?.email ?? "")
                    ProfileRow(title: "COUNTRY", value: viewModel.state.user?.country ?? "")
                    ProfileRow(title: "FOLLOWERS", value: String(viewModel.state.user?.followers ?? 0))
                    ProfileRow(title: "PRODUCT", value: viewModel.state.user?.product ?? "")
                }
                .padding(.top, 20)
                
                Spacer()
                .navigationDestination(isPresented: $redirect) {
                    ContentView()
                        .navigationBarBackButtonHidden(true)
                }
            
            }
            .background(Color(.PRIMARY_DARK))
            
        }
    }

struct ProfileRow: View {
    var title: String
    var value: String
    
    var body: some View {
        HStack {
            Text(title)
                .font(.headline)
                .foregroundColor(Color(.PRIMARY_TEXT_WHITE))
                .padding(.leading, 20)
            
            Spacer()
            
            Text(value)
                .font(.body)
                .foregroundColor(Color(.PRIMARY_TEXT_WHITE))
                .padding(.trailing, 20)
        }
        .padding(.vertical, 10)
        .overlay(
            Rectangle()
                .frame(height: 1)  // Set the height of the border
                .foregroundColor(Color(.PRIMARY_TEXT_WHITE))  // Color of the bottom border
                .padding(.top, 10), // Adjust padding to position the border at the bottom
            alignment: .bottom
        )
    }
}
#Preview {
    UserProfileView()
}
