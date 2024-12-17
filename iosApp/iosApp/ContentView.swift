import Shared
import Combine
import SwiftUI
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesRxSwift

struct ContentView: View {
    @State private var isUserAuthenticated: Bool = false

    var authViewModel = KoinDependencies.shared.getAuthViewModel()
    var authRepository = KoinDependencies.shared.getAuthRepository()
    
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
        
//        createPublisher(for: authViewModel.isUserLoggedInFlow)
//            .sink { completion in
//            } receiveValue: { newValue in
//                self.isUserAuthenticated = newValue as! Bool
//            }

    }
}
#Preview {
    ContentView()
}
