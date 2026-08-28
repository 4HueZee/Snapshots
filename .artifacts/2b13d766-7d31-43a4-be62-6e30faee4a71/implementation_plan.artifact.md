# Development Progression & Fortification Plan

This plan outlines the steps to implement a Presence System, enhance the UI with Adaptive Design and Edge-to-Edge support, and fortify the application with Offline-First logic and Automated Testing.

## User Review Required

> [!IMPORTANT]
> The Presence System will add a `lastActive` field to the user's Firestore document. This data will be visible to friends.

## Proposed Changes

### 1. Presence System

Implement real-time presence tracking using Firestore and Lifecycle observers.

#### [NEW] [PresenceRepository.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/PresenceRepository.kt)
- Create a repository to manage `online` status and `lastActive` timestamps.
- Use `ProcessLifecycleOwner` to detect app background/foreground transitions.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/MainActivity.kt)
- Initialize the `PresenceRepository` to track the user's status while the app is active.

#### [MODIFY] [UserProfile.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/UserProfile.kt)
- Add `isOnline` and `lastActive` fields to the data model.

---

### 2. Adaptive Design & Edge-to-Edge

Modernize the UI for a premium, immersive experience across all devices.

#### [MODIFY] [MainScreen.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/MainScreen.kt)
- Refine layout to use `WindowInsets` for full edge-to-edge immersion.
- Add support for adaptive spacing.

#### [MODIFY] [FriendsModule.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/FriendsModule.kt)
- Implement a **List-Detail pattern** for the Social Hub on large screens (tablets/foldables) using the Material 3 Adaptive library.

---

### 3. Engineering Fortification

Harden the app for reliability and long-term maintenance.

#### [MODIFY] [UserRepository.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/UserRepository.kt)
- Explicitly enable and configure Firestore Persistence for offline-first capabilities.
- Ensure all repositories handle data fetches from local cache when the network is unavailable.

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/gradle/libs.versions.toml)
- Add dependencies for:
    - `androidx.compose.material3.adaptive`
    - `androidx.test.ext:junit-ktx`
    - `io.mockk:mockk`

#### [NEW] [UserRepositoryTest.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/test/java/com/example/battlebarge/UserRepositoryTest.kt)
- Implement initial unit tests for profile validation and cache logic.

## Verification Plan

### Automated Tests
- Run `./gradlew test` to verify repository logic.
- Run instrumented tests on an emulator to verify UI scaling on different screen sizes.

### Manual Verification
- Deploy to a physical device/emulator and verify that the UI doesn't overlap with system bars.
- Test the app in Split Screen or on a tablet emulator to verify the adaptive layout transitions.
- Kill network connectivity and verify that the app still allows viewing profile and friends (Offline-First).
