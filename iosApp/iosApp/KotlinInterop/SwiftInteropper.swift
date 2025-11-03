//
//  SwiftInteropper.swift
//  iosApp
//
//  Created by Marcel Bloemendaal on 30/10/2025.
//

import Foundation
import ComposeApp

public class SwiftInteropProvider: InteropProvider {

    public func provideExampleText() -> String {
        return "Hello World! Sent to you from Swift!"
    }
}
