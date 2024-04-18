import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {

    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            // ⬇️: Compose has own keyboard handler(.keyboard)
            .ignoresSafeArea(.all)
    }
}

struct ContentView_preview: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

