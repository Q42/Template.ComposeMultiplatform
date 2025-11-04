//
//  NativeButton.swift
//  iosApp
//
//  Created by Marcel Bloemendaal on 04/11/2025.
//

import SwiftUI

struct SwiftUINativeButton: View {
    let label: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(label)
        }
    }
}

#Preview {
    SwiftUINativeButton(label: "Hello", action: {})
}
