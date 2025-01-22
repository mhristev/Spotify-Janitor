//
//  AuthView.swift
//  iosApp
//
//  Created by Martin Hristev on 3.12.24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import SpotifyiOS
import KMPObservableViewModelSwiftUI
import Shared
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesRxSwift
import KMPObservableViewModelSwiftUI
import SDWebImageSwiftUI
import AuthenticationServices

struct AuthView: View {
    @State var isLoggedIn = false
    
    @State private var authSession: ASWebAuthenticationSession?
    @State private var accessToken: String?
    
    @StateViewModel
    var viewModel = KoinDependencies.shared.getAuthViewModel()
    
    
    private var clientID: String {
            Bundle.main.value(for: "CLIENT_ID") ?? ""
        }
    
    private var redirectURI: URL {
            if let uriString = Bundle.main.value(for: "REDIRECT_URI") {
                return URL(string: uriString) ?? URL(string: "https://default-fallback-uri.com")!
            }
            return URL(string: "https://default-fallback-uri.com")!
        }

    
    var body: some View {
        NavigationView {
            ZStack {
                Color(.PRIMARY_DARK)
                    .edgesIgnoringSafeArea(.all)
                
                VStack {
//                    AsyncImage(url: URL(string: "https://storage.googleapis.com/pr-newsroom-wp/1/2023/05/Spotify_Primary_Logo_RGB_Green.png")) { image in
//                        image
//                            .resizable()
//                            .scaledToFit()
//                            .frame(width: 120, height: 120)
//                    } placeholder: {
//                        ProgressView()
//                            .progressViewStyle(CircularProgressViewStyle())
//                            .frame(width: 120, height: 120)
//                    }
                    
                    WebImage(url: URL(string: "https://storage.googleapis.com/pr-newsroom-wp/1/2023/05/Spotify_Primary_Logo_RGB_Green.png")) { image in
                            image.resizable()
                            .scaledToFit()
                            .frame(width: 120, height: 120)
                        } placeholder: {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle())
                                .frame(width: 120, height: 120)
                        }
                    
                    Spacer().frame(height: 16)
                    
                    Text("Spotify Janitor")
                        .font(.headline)
                        .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                        .padding(.bottom, 24)
                    
                    Spacer().frame(height: 24)
                    
                    Button(action: {
                        initiateSpotifyLogin()
                    }) {
                        Text("Login with Spotify")
                            .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                            .font(.headline)
                            .padding()
                        
                    }
                    .background(Color(.SPOTIFY_GREEN))
                    .cornerRadius(10) // Add rounded corners
                    .shadow(radius: 5)
    
                    
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.horizontal, 20)
                NavigationLink(value: isLoggedIn) {
                                    
                                }
                .navigationDestination(isPresented: $isLoggedIn) {
                    ContentView()
                                        .navigationBarBackButtonHidden(true) // Hide the back button
                                }
            }
            
        }.onAppear(perform: {
            observeUserAuthentication()
        })
    }
    private func observeUserAuthentication() {
        let observable = createObservable(for: viewModel.isUserLoggedInFlow)
        
        observable
            .subscribe(onNext: { isLoggedIn in
                self.isLoggedIn = isLoggedIn as! Bool
            }, onError: { error in
                print("Received error: \(error)")
            })
    }
    
    private func initiateSpotifyLogin() {
        let scopes = "user-read-private user-library-read user-read-email playlist-read-private user-library-modify"
        let authURL = URL(string: "https://accounts.spotify.com/authorize?client_id=\(clientID)&response_type=token&redirect_uri=\(redirectURI.absoluteString)&scope=\(scopes.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)")!
        let presentationContextProvider = PresentationContextProvider()
        authSession = ASWebAuthenticationSession(url: authURL, callbackURLScheme: redirectURI.scheme) { callbackURL, error in
            if let callbackURL = callbackURL, error == nil {
                let (token, expiresIn) = extractAccessToken(from: callbackURL)
                if let token = token {
                    if let intValue = Int32(expiresIn ?? "") {
                        viewModel.onAction(action: AuthActionOnLogin(accessToken: token, expiresIn: intValue))
                    }
                   
                }
            }
        }
        authSession?.presentationContextProvider = presentationContextProvider
        authSession?.start()
    }
    
    private func extractAccessToken(from url: URL) -> (String?, String?) {
        guard let fragment = url.fragment else { return (nil, nil) }
        let params = fragment.split(separator: "&").reduce(into: [String: String]()) { result, param in
            let parts = param.split(separator: "=")
            if parts.count == 2 {
                result[String(parts[0])] = String(parts[1])
            }
        }
        var expiresIn = params["expires_in"]
        var accessToken = params["access_token"]
        return (accessToken, expiresIn)
    }
}

class PresentationContextProvider: NSObject, ASWebAuthenticationPresentationContextProviding {
    func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
        return UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }!
    }
}

extension Bundle {
    func value(for key: String) -> String? {
        guard let path = self.path(forResource: "Secrets", ofType: "plist"),
              let dictionary = NSDictionary(contentsOfFile: path) else {
            return nil
        }
        return dictionary[key] as? String
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        AuthView()
    }
}

