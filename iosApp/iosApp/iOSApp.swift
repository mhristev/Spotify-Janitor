import SwiftUI
import BackgroundTasks
import SpotifyiOS
import Shared

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        InitKoinKt.doInitKoin()
        BGTaskScheduler.shared.register(forTaskWithIdentifier: "com.app.deleteTrack", using: nil) { task in
                    self.handleDeleteTrack(task: task as! BGProcessingTask)
                }
        
        return true
    }
    private func handleDeleteTrack(task: BGProcessingTask) {
            print("Handling background delete task")
           
            guard let trackID = UserDefaults.standard.string(forKey: "backgroundDeleteTrackID") else {
                print("No track ID found for background deletion")
                task.setTaskCompleted(success: false)
                return
            }
            
        let viewModel = KoinDependencies.shared.getFavoriteTracksViewModel()
            
            Task {
                viewModel.onAction(action: FavoriteTracksActionOnRemoveTrackByIdGlobally(trackId: trackID))
                UserDefaults.standard.removeObject(forKey: "backgroundDeleteTrackID")
                task.setTaskCompleted(success: true)
            }
        }
}


@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
