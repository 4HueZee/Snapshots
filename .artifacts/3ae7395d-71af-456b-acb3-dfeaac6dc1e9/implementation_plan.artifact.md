# Modularization Plan for Battle Barge

This plan outlines the transition from a monolithic `:app` structure to a multi-module architecture. This will allow for parallel development, faster build times for individual modules, and cleaner separation of concerns.

## User Review Required

> [!IMPORTANT]
> **Gradle Sync**: After these changes, a full Gradle sync will be required. I will trigger this automatically if possible, but you may need to manually click "Sync Now" in Android Studio if it prompts you.
>
> **Namespace Changes**: I will be updating namespaces to follow a standard `com.example.battlebarge.[module]` pattern. This will require updating imports across the project.

## Proposed Changes

### [Component] Project Configuration

#### [MODIFY] [settings.gradle.kts](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/settings.gradle.kts)
- Include new modules: `:core:designsystem`, `:core:data`, `:feature:friends`, and `:feature:settings`.

### [Component] Core Design System (`:core:designsystem`)
This module will house the "vessel" aesthetics—colors, typography, and custom shapes—so all other features can remain visually consistent.

#### [NEW] [build.gradle.kts](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/core/designsystem/build.gradle.kts)
- Configure as an Android Library module.
- Add Compose dependencies.

#### [MOVE] [SineLeafShape.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/ui/theme/SineLeafShape.kt) -> `:core:designsystem`
#### [MOVE] [Theme.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/ui/theme/Theme.kt) -> `:core:designsystem`
#### [MOVE] [AppThemeConfig.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/ui/theme/AppThemeConfig.kt) -> `:core:designsystem`

### [Component] Core Data (`:core:data`)
Handles data persistence and external API communication (Firebase).

#### [NEW] [build.gradle.kts](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/core/data/build.gradle.kts)
- Add Firebase and DataStore dependencies.

#### [MOVE] [UserRepository.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/UserRepository.kt) -> `:core:data`
#### [MOVE] [FriendsRepository.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/FriendsRepository.kt) -> `:core:data`

### [Component] Feature Modules (`:feature:friends`, `:feature:settings`)
Independent operational modules.

#### [MOVE] [FriendsModule.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/FriendsModule.kt) -> `:feature:friends`
#### [MOVE] [AccountSettingsMenu.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/AccountSettingsMenu.kt) -> `:feature:settings`

### [Component] Application Vessel (`:app`)
The final container that stitches everything together.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/build.gradle.kts)
- Add implementation dependencies for all new modules.
- Remove redundant dependencies that are now handled by sub-modules.

## Verification Plan

### Automated Tests
- Run `:core:designsystem` build tasks to verify geometry logic.
- Run `gradlew build` to ensure all inter-module dependencies are correctly resolved.

### Manual Verification
- Deploy the app and navigate through the settings and friends views to ensure the UI still renders correctly with the new module structure.
