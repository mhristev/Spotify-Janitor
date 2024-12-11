    //
    //  AppView.swift
    //  iosApp
    //
    //  Created by Martin Hristev on 3.12.24.
    //  Copyright © 2024 orgName. All rights reserved.
    //

    import SwiftUI

    struct AppRootView: View {
        @State private var selectedTab: Int = 1
        
        init() {
            let tabBarAppearance = UITabBarAppearance()
            tabBarAppearance.configureWithDefaultBackground()
            tabBarAppearance.backgroundColor = UIColor(resource: .PRIMARY_DARK)
            UITabBar.appearance().standardAppearance = tabBarAppearance
            UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance
        }
        
        var body: some View {
            NavigationView {
                TabView(selection: $selectedTab) {
                    BrowseTracksView()
                        .tabItem {
                            Label("Browse", systemImage: "magnifyingglass")
                        }.tag(0)
                    
                    FavoriteTracksView()
                        .tabItem {
                            Label("Favorites", systemImage: "heart.fill")
                        }.tag(1)
                    
                    UserProfileView()
                        .tabItem {
                            Label("Me", systemImage: "person.crop.circle")
                        }.tag(2)
                }
                .accentColor(Color(.PRIMARY_TEXT_WHITE))
            }
            
        }
        
    }
