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
import Combine

import SwiftUI
import Combine

struct BrowseTracksView: View {
    @StateViewModel
    var viewModel = KoinDependencies.shared.browseTracksViewModel
    
    @State private var searchQuery: String = ""
    @State private var timer: Timer? = nil
    
    func performRequest(with query: String) {
        viewModel.onAction(action: BrowseTracksActionOnSearch(query: query))
         }
    var body: some View {
        NavigationStack {
            VStack {
                HStack {
                    Text("Browse")
                        .font(.title3)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding(.leading, 10)
                    
                    Spacer()
                }
          
                .background(Color(.PRIMARY_DARK))
                .padding(.top, 0)
                // Search Bar
                TextField("Search", text: $searchQuery)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: searchQuery) { newValue in
                        timer?.invalidate()
                        timer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false) { _ in
                            performRequest(with: newValue)
                        }
                    }
                List(viewModel.state.tracks, id: \.id) { track in
                    TrackRow(
                        track: track,
                        onAddToFavoritesClick:
                            viewModel.onAction(action: BrowseTracksActionOnTrackAddToFavorites(track: track))
                    )
                        .listRowSeparator(.hidden)
                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .listStyle(.plain)
            }
            .navigationBarHidden(true)
            .navigationTitle("Search")
            .background(Color(.PRIMARY_DARK))
            .shadow(radius: 5)
        }.onAppear(perform: {
            performRequest(with: "")
        })
    }
}


#Preview {
    BrowseTracksView()
}
