//
//  SearchTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import KMPObservableViewModelSwiftUI
import Shared

struct BrowseTracksView: View {
    @StateObject private var viewModel = BrowseTracksViewModelIOSImpl()
    @State private var searchQuery: String = ""
    
    var body: some View {
  
            VStack {
                TopBarView(title: "Browse", imageName: nil, onAction: nil)
                TextField("What do you want to listen to?", text: $searchQuery)
                           .padding(7)
                           .padding(.leading, 30) // Space for the icon
                           .background(Color(.systemGray6))
                           .cornerRadius(8)
                           .overlay(
                               HStack {
                                   Image(systemName: "magnifyingglass")
                                       .foregroundColor(.gray)
                                       .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
                                       .padding(.leading, 8)
                                   Spacer()
                               }
                           )
                           .padding(.horizontal, 10)
                           .onChange(of: searchQuery) { oldvalue, newValue in
                               viewModel.searchTimer(newValue: newValue)
                           }
                List(viewModel.stateIOS.tracks, id: \.id) { track in
                    TrackRow(track: track,
                             onAddToFavoritesClick: { _ in
                        viewModel.onAction(action: BrowseTracksActionOnTrackAddToFavorites(track: track))
                        
                    })
                    .listRowSeparator(.hidden)
                    .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .listStyle(.plain)
                if viewModel.message != nil {
                    KMPUIAction(message: viewModel.message ?? "", color: viewModel.messageColor)
                }
            }
            .navigationBarHidden(true)
            .navigationTitle("Search")
            .background(Color(.PRIMARY_DARK))
            .shadow(radius: 5)
            .onAppear {
                viewModel.observeUIEvents()
            }
    }
}
    

#Preview {
    BrowseTracksView()
}
