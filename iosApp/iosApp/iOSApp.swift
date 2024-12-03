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
            let test = KoinDependencies.shared.myTestInterface()
            Text(test.sayHello())
            let myColor = AppConstants.Colors.shared.PRIMARY_PURPLE_HEX
            Text(myColor)
            let repo = KoinDependencies.shared.getTrackRepository()
//                .foregroundColor(Color(AppConstants.Colors.shared.PRIMARY_PURPLE_HEX))
            let test3 = KoinDependencies.shared.getFavoriteTracksViewModel()
       //     let test2 = KoinDependencies().getTrackRepository
            
           // AuthView()
        }
    }
}
