# Spotify Janitor App

Spotify Janitor is a cross-platform mobile application built with **Kotlin Multiplatform (KMP)**, designed to help Spotify users manage their saved tracks effectively. The app allows users to clean up their Spotify library, manage liked tracks, and explore Spotify's catalog with ease.

---

## Features

- **Spotify Authentication**: Secure login using Spotify's OAuth system.
- **Saved Tracks Management**: View, search, add, and remove liked tracks with an undo option.
- **Offline Data Storage**: Cached data to minimize API calls and improve performance.
- **Profile Management**: View user profile details, including name, premium status, and country.
- **Cross-Platform Support**: Available for both Android and iOS with a shared codebase.
- **UI**: Developed using Jetpack Compose (Android) and SwiftUI (iOS).

---

## Architecture

The app is structured using the **MVI (Model-View-Intent)** architecture.

---

### Workflow
- Local database acts as the primary source of truth.
- Synchronization with Spotify API ensures data consistency.

---

## Libraries and Tools

- **Kotlin Multiplatform (KMP)**: Shared codebase for Android and iOS.
- **Koin**: Dependency injection.
- **Ktor**: API requests with platform-specific clients (OkHttp for Android, Darwin for iOS).
- **Room**: Offline data storage.
- **Coil**: Image loading and caching.
- **NativeCoroutines**: Coroutine interoperability for Swift.
- **WorkManager (Android)** and **BGTaskScheduler (iOS)**: Background task scheduling.
- **KMP-ObservableViewModel**: Shared ViewModels between platforms.
- **KVault**: Secure storage for sensitive data.

---

## How It Works

### Authentication
1. Users log in via Spotify's OAuth.
2. Tokens are stored securely.

### Track Management
- Displays saved tracks in batches (50 tracks at a time).
- Users can search the catalog and manage their liked songs.

### Undo Deletions
- Tracks are flagged for removal and scheduled for deletion.
- Users have a brief window to undo the action.

### Add New Tracks
- Users can search for new tracks and add them to their library.
---

## Setup and Installation

### Prerequisites
- **Spotify Developer Account**: Register your app on the [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/).
- **API Keys**: Obtain your `Client ID` and `Client Secret`.

### Clone the Repository
```bash
git clone <repository-url>
cd spotify-janitor-app
```

### Android Setup

1. Open the project in **Android Studio**.
2. Add your Spotify API credentials to the `secrets.properties` file, in the same directory as `build.gradle`:
   ```properties
   spotifyClientId=<your-client-id>
   spotifyClientSecret=<your-client-secret>
   ```
3.	Run the app on an Android emulator or physical device.

### iOS Setup
1.	Open the iosApp project in Xcode.
2.	Add your Spotify API credentials to the appropriate .plist file.
   ```properties
   REDIRECT_URI=<your-client-id>
   CLIENT_ID=<your-client-secret>
   ```
3.	Build and run the app on an iOS simulator or physical device.

---

## Screenshots

### iOS
![iOS Demo](demo/ios_Simulator.gif)

### Android
![Android Demo](demo/android_Simulator.gif)
