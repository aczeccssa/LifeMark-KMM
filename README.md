## Description

### This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## TODO:

### 20240426 TODOList:
* SQLite Database API support in mutiplatform
  - swift: https://github.com/stephencelis/SQLite.swift
  - kotlin: https://developer.android.google.cn/jetpack/androidx/releases/sqlite?hl=en#kts
* SQLite solution about kotlin to Swift/Objective-C
  [Oficial Documentation](https://kotlinlang.org/docs/native-objc-interop.html#mappings)
