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
        let publisher: AnyPublisher<Bool, Never> = createPublisher(for: authRepository.isUserAuthTest)

                cancellable = publisher
                    .receive(on: DispatchQueue.main)
                    .sink { [weak self] value in
                        self?.isUserAuthTest = value
                    }
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
