//
//  AppView.swift
//  iosApp
//
//  Created by Martin Hristev on 3.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AppRootView: View {
    init() {
        let tabBarAppearance = UITabBarAppearance()
        tabBarAppearance.configureWithDefaultBackground()
        tabBarAppearance.backgroundColor = UIColor(resource: .PRIMARY_DARK)
        UITabBar.appearance().standardAppearance = tabBarAppearance
        UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance
    }
    
    var body: some View {
        TabView {
            BrowseTracksView()
                .tabItem {
                    Label("Search", systemImage: "magnifyingglass")
                }
            
            FavoriteTracksView()
                .tabItem {
                    Label("Favorites", systemImage: "heart.fill")
                }
            
            UserProfileView()
                .tabItem {
                    Label("Me", systemImage: "gearshape.fill")
                }
        }
        .accentColor(Color(.PRIMARY_TEXT_WHITE))
        
    }
    
}
