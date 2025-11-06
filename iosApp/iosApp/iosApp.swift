import SwiftUI
import ComposeApp

@main
struct ComposeApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea(.all)
        }
    }
}

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let appConfiguration = ExternalConfigIosAppConfiguration(
            nativeDependencyExample: SwiftNativeDependencyExample(),
            nativeViewFactory: SwiftNativeViewFactory()
        )

        return MainKt.MainViewController(iosAppConfiguration: appConfiguration)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Updates will be handled by Compose
    }
}
