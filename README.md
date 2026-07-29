# 🎬 Films

An elegant, modern Android application for discovering movies, watching trailers, and finding
streaming providers. Built entirely with **Kotlin** and **Jetpack Compose**, following the
principles of **Clean Architecture**, **State-Driven Navigation**, and an **Offline-First**
approach.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-blue.svg?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?logo=android)
![Adaptive UI](https://img.shields.io/badge/Adaptive_UI-Foldables_%26_Tablets-8A2BE2.svg)
![Koin](https://img.shields.io/badge/DI-Koin-FF4154.svg)
![Ktor](https://img.shields.io/badge/Network-Ktor-087CFA.svg)
![Room](https://img.shields.io/badge/Database-Room-3DDC84.svg)

---

## 📱 Screenshots

<div align="center">
  <img src="app/docs/screenshots/home_screen.png" alt="Home Screen" width="22%" />&nbsp;&nbsp;
  <img src="app/docs/screenshots/favorites_screen.png" alt="Favorites Screen" width="22%" />&nbsp;&nbsp;
  <img src="app/docs/screenshots/watch_later_screen.png" alt="Watch Later Screen" width="22%" />&nbsp;&nbsp;
  <img src="app/docs/screenshots/settings_screen.png" alt="Settings Screen" width="22%" />

<br><br>

<img src="app/docs/screenshots/home_screen_fold.png" alt="Adaptive Home Screen" width="30%" />
&nbsp;&nbsp;&nbsp;
<img src="app/docs/screenshots/details_screen_fold.png" alt="Adaptive Details Screen" width="30%" />
&nbsp;&nbsp;&nbsp;
<img src="app/docs/screenshots/settings_screen_fold.png" alt="Adaptive Settings Screen" width="30%" />
</div>

---

## ✨ Features

* **Adaptive UI (Tablets & Foldables):** Fully optimized for large screens using **Material 3
  Adaptive**. Dynamically switches between single-pane (phones) and dual-pane (
  `ListDetailPaneScaffold`) layouts depending on the window size, providing a desktop-class
  experience.
* **State-Driven Navigation (Navigation 3):** Cutting-edge routing approach using
  `androidx.navigation3`. Complete removal of `NavController` in favor of pure Kotlin State (
  `List<NavKey>`). Seamless tab state restoration using `SavedStateConfiguration` and
  `kotlinx.serialization`.
* **Modern UI/UX:** Built with Jetpack Compose. Immersive Edge-to-Edge design with custom shadows,
  spring animations (`animateBounds`), and translucent glass-morphism effects.
* **Offline-First:** Movies are cached locally using **Room** with automated schema migrations (
  `AutoMigration`). Instant loading from the database with smart background synchronization.
* **Reactive State Management:** Powered by `StateFlow` and `SharedFlow`. Safe handling of UI events
  and automatic cancellation of outdated network requests (`flatMapLatest`).
* **YouTube Trailer Integration:** Watch official movie trailers directly inside the app.
* **Watch Providers:** Discover where to legally stream, rent, or buy movies in your country (
  powered by JustWatch).
* **Dynamic Theme & Localization:** Seamless switching between Light/Dark themes and languages (
  EN/RU/etc.) on-the-fly using `DataStore` and `CompositionLocal` without Activity recreation.

---

## 🛠 Tech Stack

* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Adaptive Layouts**
* **Navigation:** Navigation 3
* **Architecture:** Strict Clean Architecture, Multi-module (UDF / MVI-like state management)
* **Dependency Injection:** [Koin](https://insert-koin.io/)
* **Networking:** [Ktor Client](https://ktor.io/)
* **Local Database:** [Room](https://developer.android.com/training/data-storage/room) (with KSP &
  AutoMigrations)
* **Preferences:
  ** [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
* **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
* **Animations:** [Lottie for Compose](https://airbnb.io/lottie/)
* **Video Player:
  ** [Android YouTube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player)

---

## ⚙️ Project Architecture

The project is strictly modularized by layers, enforcing the Dependency Inversion Principle and
encapsulation (`internal` modifiers):

* `:domain` (Pure Kotlin): The heart of the app. Contains business models, abstract repository
  interfaces, and pure Enums. Has zero Android dependencies.
* `:data` (Android Library): Handles remote (Ktor) and local (Room) data sources. Implements the
  `:domain` interfaces. Network DTOs and Database Entities are kept `internal` to prevent leaking
  into the UI.
* `:ui` (Android Library): Contains Jetpack Compose screens, ViewModels, Adaptive Scaffolds, and
  UI-specific mappers. Depends only on `:domain`.
* `:app` (Android App): The lightweight shell (Entry Point). Connects all modules together,
  initializes Koin, and sets up the root `NavDisplay`.

---

## 🚀 Getting Started

To build and run the project, you need to provide your
own [TMDB API Key](https://developer.themoviedb.org/docs/getting-started).

1. Clone the repository:
   ```bash
   git clone https://github.com/RomanAisly/Films
   ```
