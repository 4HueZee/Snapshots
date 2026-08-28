pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Battle Barge"
include(":app")
include(":agnostic")

// --- Composite Build Hooks for Future Standalone Modules ---
// To develop a new module (e.g., a Game) in a separate directory and 
// dock it into the Battle Barge, uncomment the line below:
// includeBuild("../battlebarge-game")
