//import SwiftUI
import Shared

//struct ContentView: View {
//    @State private var showContent = false
//    var body: some View {
//        VStack {
//            Button("Click me!") {
//                withAnimation {
//                    showContent = !showContent
//                }
//            }
//
//            if showContent {
//                VStack(spacing: 16) {
//                    Image(systemName: "swift")
//                        .font(.system(size: 200))
//                        .foregroundColor(.accentColor)
//                    Text("SwiftUI:")
//                }
//                .transition(.move(edge: .top).combined(with: .opacity))
//            }
//        }
//        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
//        .padding()
//    }
//}
//
//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}

import SwiftUI

struct ContentView: View {
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
                    Label("First", systemImage: "house.fill")
                }
            
            // Middle View (The one you provided)
            FavoriteTracksView()
                .tabItem {
                    Label("Middle", systemImage: "heart.fill")
                }
            
            // Third View
            UserProfileView()
                .tabItem {
                    Label("Third", systemImage: "gearshape.fill")
                }
        }
        .accentColor(.blue)  // Change the color of the selected tab
        .background(Color.red)
        
    }
}

struct FirstView: View {
    var body: some View {
        VStack {
            Text("First View")
            // Customize this view as needed
        }
    }
}

struct MiddleView: View {
    var body: some View {
        VStack {
            Text("Favorite Tracks")
                .font(.title)
                .bold()
            
            List {
                // Add your track items here
                Text("Track 1")
                Text("Track 2")
                Text("Track 3")
            }
        }
        .padding()
        .background(Color.black) // Adjust background color
        .cornerRadius(10)
        .shadow(radius: 5)
    }
}

struct ThirdView: View {
    var body: some View {
        VStack {
            Text("Third View")
            // Customize this view as needed
        }
    }
}


#Preview {
    ContentView()
}
