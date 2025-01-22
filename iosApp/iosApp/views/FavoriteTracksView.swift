import SwiftUICore
import SwiftUI
struct FavoriteTracksView: View {
    @StateObject var viewModel = FavoriteTracksViewModelIOSImpl()
    
    var body: some View {
        VStack {
            TopBarView(title: "Favorite Tracks", imageName: "arrow.clockwise", onAction: {
                viewModel.syncFavorites()
            })

            List {
                ForEach(viewModel.stateIOS.cachedTracks, id: \.id) { track in
                    TrackRow(track: track)
                        .listRowSeparator(.hidden)
                        .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                }
                .onDelete { offsets in
                    if let index = offsets.first {
                        let track = viewModel.stateIOS.cachedTracks[index]
                        viewModel.confirmDeleteTrack(track)
                    }
                }

                Button(action: {
                    viewModel.loadMoreFavorites()
                }) {
                    HStack {
                        Spacer()
                        Text("Load More")
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(.white)
                        Spacer()
                    }
                    .padding(.vertical, 10)
                    .background(
                        Capsule()
                            .stroke(Color.white, lineWidth: 1.5)
                    )
                }
                .listRowBackground(Color.clear)
                .frame(maxWidth: .infinity)
            }
            .listStyle(.plain)
            .background(Color(.PRIMARY_DARK).ignoresSafeArea(.all))
            
            if viewModel.isUndoVisible {
                Button("Undo") {
                    viewModel.restoreDeletedTrack()
                }
                .padding()
                .background(Color.green)
                .foregroundStyle(Color(.PRIMARY_TEXT_WHITE))
                .cornerRadius(8)
                .transition(.opacity)
            }
            
            if let message = viewModel.errorMessage {
                KMPUIAction(message: message, color: Color.red)
            }
        }
        .background(Color(.PRIMARY_DARK))
        .shadow(radius: 5)
        .alert("Confirm Remove", isPresented: $viewModel.isShowingConfirmation) {
            Button("Remove", role: .destructive) {
                viewModel.deleteConfirmedTrack()
            }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("Are you sure you want to remove this track?")
        }
    }
}


