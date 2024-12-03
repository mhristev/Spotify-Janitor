import SwiftUI
import SafariServices
import SpotifyiOS
import AuthenticationServices

struct SpotifyLoginView: View {
    @State private var authSession: ASWebAuthenticationSession?
    @State private var accessToken: String?

    private let clientID = "91be3576121a482e9ad00bb97888f3e8"
    private let redirectURI = URL(string: "org.internship.kmp.martin://callback")!

    var body: some View {
        VStack {
            if let token = accessToken {
                Text("Access Token: \(token)")
            } else {
                Button(action: {
                    initiateSpotifyLogin()
                }) {
                    Text("Login with Spotify")
                }
            }
        }
    }

    private func initiateSpotifyLogin() {
        let scopes = "user-read-private user-library-read user-read-email playlist-read-private"
        let authURL = URL(string: "https://accounts.spotify.com/authorize?client_id=\(clientID)&response_type=token&redirect_uri=\(redirectURI.absoluteString)&scope=\(scopes.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)")!
        let presentationContextProvider = PresentationContextProvider()
        authSession = ASWebAuthenticationSession(url: authURL, callbackURLScheme: redirectURI.scheme) { callbackURL, error in
            if let callbackURL = callbackURL, error == nil {
                if let token = extractAccessToken(from: callbackURL) {
                    accessToken = token
                }
            }
        }
        authSession?.presentationContextProvider = presentationContextProvider
        authSession?.start()
    }

    private func extractAccessToken(from url: URL) -> String? {
        guard let fragment = url.fragment else { return nil }
        let params = fragment.split(separator: "&").reduce(into: [String: String]()) { result, param in
            let parts = param.split(separator: "=")
            if parts.count == 2 {
                result[String(parts[0])] = String(parts[1])
            }
        }
        return params["access_token"]
    }
}

//class PresentationContextProvider: NSObject, ASWebAuthenticationPresentationContextProviding {
//    func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
//        return UIApplication.shared.connectedScenes
//            .compactMap { $0 as? UIWindowScene }
//            .flatMap { $0.windows }
//            .first { $0.isKeyWindow }!
//    }
//}
