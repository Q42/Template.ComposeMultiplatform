//
//  SwiftSampleNativeDependency.swift
//  iosApp
//
//  Created by Marcel Bloemendaal on 30/10/2025.
//

import Foundation
import ComposeApp

public class SwiftSampleNativeDependency: SampleNativeDependency {
    public func executeNativeMethod() {
        print("Hello from Swift!")
    }

    public func executeNativeAsyncMethod() async throws -> String {
        try await Task.sleep(for: .seconds(2.0))
        return "Hello from Swift async!"
    }
}
