import SwiftUI
import SpotifyiOS
import Shared

@main
struct iOSApp: App {
    
    init() {
        InitKoinKt.doInitKoin()
    }
    
    var body: some Scene {
        let myColor = AppConstants.Colors.shared.PRIMARY_PURPLE_HEX
        WindowGroup {
            ContentView()
        }
    }
}
