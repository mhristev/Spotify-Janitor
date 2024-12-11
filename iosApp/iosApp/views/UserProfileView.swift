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
import SDWebImageSwiftUI

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
                        .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                        .padding(.leading, 16)
                    
                    Spacer()
                    
                    Button(action: {
                        print("Logging out...")
                        logout()
                    }) {
                        Image(systemName: "rectangle.portrait.and.arrow.right")
                            .font(.system(size: 20))
                            .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                            .padding(.trailing, 16)
                    }
                }
                
                VStack {
                    WebImage(url: URL(string: viewModel.state.user?.imageUrl ?? "")) { image in
                            image.resizable()
                            .scaledToFit()
                            .frame(width: 128, height: 128)
                            .clipShape(Circle())
                            .overlay(Circle().stroke(Color(.PRIMARY_TEXT_WHITE), lineWidth: 0.5))
                            .shadow(radius: 10)
                        } placeholder: {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle())
                                .frame(width: 128, height: 128)
                        }
                        .resizable()
                        .transition(.fade(duration: 0.5))
                        .scaledToFit()
                        .frame(width: 128, height: 128, alignment: .center)
                    
//                    AsyncImage(url: URL(string: viewModel.state.user?.imageUrl ?? "")) { phase in
//                        switch phase {
//                        case .empty:
//                            // This is the loading state
//                            ProgressView()
//                                .progressViewStyle(CircularProgressViewStyle())
//                                .frame(width: 128, height: 128)
//                        case .success(let image):
//                            // Successfully loaded image
//                            image
//                                .resizable()
//                                .scaledToFit()
//                                .frame(width: 188, height: 188)                                .clipShape(Circle())
//                                .overlay(Circle().stroke(Color(.PRIMARY_TEXT_WHITE), lineWidth: 1))
//                                .shadow(radius: 10)
//                        case .failure:
//                            
//                            Image(systemName: "exclamationmark.triangle.fill")
//                                .foregroundStyle(.red)
//                                .frame(width: 60, height: 60)
//                        @unknown default:
//                            EmptyView()
//                        }
//                    }
                    
                    Text(viewModel.state.user?.displayName ?? "")
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
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


