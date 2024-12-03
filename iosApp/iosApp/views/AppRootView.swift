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
        tabBarAppearance.backgroundColor = .black  // Change tab bar background to red
        UITabBar.appearance().standardAppearance = tabBarAppearance
        UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance
    }
    
    var body: some View {
        TabView {
            // First View
            SearchTracksView()
                .tabItem {
                    Label("Search", systemImage: "magnifyingglass")
                }
            
            // Middle View (The one you provided)
            FavoriteTracksView()
                .tabItem {
                    Label("Favorites", systemImage: "heart.fill")
                }
            
            // Third View
            UserProfileView()
                .tabItem {
                    Label("Me", systemImage: "gearshape.fill")
                }
        }
        .accentColor(.blue)  // Change the color of the selected tab
        
    }
    
}
