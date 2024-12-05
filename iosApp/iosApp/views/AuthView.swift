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

import AuthenticationServices

struct AuthView: View {
    @State private var isLoggedIn = false
    @State private var authSession: ASWebAuthenticationSession?
    @State private var accessToken: String?
    
    
    @StateViewModel
    var viewModel = KoinDependencies.shared.authViewModel
    
    private let clientID = "91be3576121a482e9ad00bb97888f3e8"
    private let redirectURI = URL(string: "org.internship.kmp.martin://callback")!
    
    
    var body: some View {
        ZStack {
            Color(.PRIMARY_DARK)
                .edgesIgnoringSafeArea(.all)
            
            VStack {
                AsyncImage(url: URL(string: "https://storage.googleapis.com/pr-newsroom-wp/1/2023/05/Spotify_Primary_Logo_RGB_Green.png")) { image in
                    image
                        .resizable()
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
                    .foregroundColor(Color.white)
                    .padding(.bottom, 24)
                
                Spacer().frame(height: 24)
                
                Button(action: {
                    initiateSpotifyLogin()
                }) {
                    Text("Login with Spotify").foregroundColor(Color.white)
                    
                }.background(Color(.SPOTIFY_GREEN))
                
                
                
                
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .padding(.horizontal, 20)
        }.fullScreenCover(isPresented: $isLoggedIn) {
            AppRootView()
        }
        
    }
    
    private func initiateSpotifyLogin() {
        let scopes = "user-read-private user-library-read user-read-email playlist-read-private"
        let authURL = URL(string: "https://accounts.spotify.com/authorize?client_id=\(clientID)&response_type=token&redirect_uri=\(redirectURI.absoluteString)&scope=\(scopes.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)")!
        let presentationContextProvider = PresentationContextProvider()
        authSession = ASWebAuthenticationSession(url: authURL, callbackURLScheme: redirectURI.scheme) { callbackURL, error in
            if let callbackURL = callbackURL, error == nil {
                let (token, expiresIn) = extractAccessToken(from: callbackURL)
                if let token = token {
                    if let intValue = Int32(expiresIn ?? "") {
                        viewModel.onAction(action: AuthActionOnLogin(accessToken: token, expiresIn: intValue))
                        isLoggedIn = true
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


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        AuthView()
    }
}
