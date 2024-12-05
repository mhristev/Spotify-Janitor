//
//  FavoriteTracksView.swift
//  iosApp
//
//  Created by Martin Hristev on 2.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI

struct FavoriteTracksView: View {
    
    @StateViewModel
    var viewModel = KoinDependencies.shared.favoriteTracksViewModel
    
    var body: some View {
            VStack {
                TopBarView(title: "Favorite Tracks", imageName: "arrow.clockwise", onAction: {
                    viewModel.onAction(action: FavoriteTracksActionSyncronizeTracks())
                })
                
                List(viewModel.state.tracks, id: \.id) { track in
                    TrackRow(track: track)
                        .listRowSeparator(.hidden)
                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .listStyle(.plain)
        }.background(Color(.PRIMARY_DARK))
            .shadow(radius: 5)
    }
}


//#Preview {
//    FavoriteTracksView()
//}
 
