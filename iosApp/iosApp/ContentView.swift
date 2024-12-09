import Shared
import Combine
import SwiftUI
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesRxSwift

struct ContentView: View {
    @State private var isUserAuthenticated: Bool = false
    var authViewModel = KoinDependencies.shared.authViewModel
    
    var body: some View {
        Group {
            if isUserAuthenticated {
                AppRootView()
            } else {
                AuthView()
            }
        }
        .onAppear {
            observeUserAuthentication()
        }
    }
    
    private func observeUserAuthentication() {
        createObservable(for: authViewModel.isUserLoggedInFlow)
            .subscribe(onNext: { isLoggedIn in
                self.isUserAuthenticated = isLoggedIn as! Bool
            }, onError: { error in
                print("Received error: \(error)")
            })
    }
}
#Preview {
    ContentView()
}
