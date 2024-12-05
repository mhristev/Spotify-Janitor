import SwiftUI
import SpotifyiOS
import Shared

@main
struct iOSApp: App {
    
    init() {
        InitKoinKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
