# Battle Barge: Module Integration Guide

This guide defines the "Docking Protocol" for standalone projects (Games, Catalogues, Sandboxes) intended to be integrated into the Battle Barge shell.

## 1. Project Structure

Standalone projects should be created as **Android Library** modules (using the `com.android.library` plugin) rather than Application modules if the ultimate goal is integration. This simplifies resource merging and dependency resolution.

### Namespace Convention
To avoid collisions with the "Vessel" (Battle Barge) and other modules, use a hierarchical namespace:
- **Shell**: `com.example.battlebarge`
- **Modules**: `com.example.battlebarge.[module_name]` (e.g., `com.example.battlebarge.game`)

## 2. Dependency Management

To ensure compatibility, your standalone project **MUST** use the same dependency versions as the Battle Barge.

### Version Catalog Integration
Copy the [libs.versions.toml](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/gradle/libs.versions.toml) from the Battle Barge project to your new project's `gradle/` folder.

In your new project's `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}
```

## 3. UI and Theming

The Battle Barge uses a custom `BattleBargeTheme`. To ensure your module's UI matches the vessel's aesthetics:

1. **Do not define a new Theme**: Instead, accept a `@Composable content: @Composable () -> Unit` wrapper or use `MaterialTheme` directly.
2. **Accessing Vessel Styles**: If you need the custom sine-wave geometry or specific colors, your module will eventually depend on a shared `:core:designsystem` module (or you can copy the `SineLeafShape.kt` logic for standalone dev).

## 4. Integration Hook (Entry Point)

Define a single "Public Entry Point" for your module.

```kotlin
// In your module's public API
@Composable
fun ModuleMainEntry(
    onNavigateBack: () -> Unit,
    // Add any required data injections here (e.g., UserID)
) {
    // Your module's internal navigation and UI
}
```

## 5. Development Workflow (Composite Builds)

During development of your standalone project:
1. Keep the project in a separate folder (e.g., `../battlebarge-game`).
2. In the Battle Barge's `settings.gradle.kts`, uncomment:
   `includeBuild("../battlebarge-game")`
3. In the Battle Barge's `app/build.gradle.kts`, add:
   `implementation("com.example.battlebarge:game")` (or the respective group/artifact defined in the game project).

## 6. Proguard/R8
If your module uses reflection (e.g., for JSON parsing), provide a `consumer-rules.pro` file so the shell automatically inherits the necessary keep rules.
