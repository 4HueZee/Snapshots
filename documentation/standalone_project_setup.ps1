# Battle Barge: Standalone Module Scaffolding Script
# This script prepares a directory to be a Battle Barge-compatible standalone project.

param (
    [Parameter(Mandatory=$true)]
    [string]$ModuleName
)

$TargetDir = "../battlebarge-$ModuleName"
$Namespace = "com.example.battlebarge.$ModuleName"

Write-Host "Creating standalone project scaffold for: $ModuleName" -ForegroundColor Cyan

# 1. Create Directory Structure
New-Item -ItemType Directory -Path "$TargetDir/gradle" -Force
New-Item -ItemType Directory -Path "$TargetDir/$ModuleName/src/main/java/com/example/battlebarge/$ModuleName" -Force

# 2. Sync Version Catalog
$VesselCatalog = "../../Project/MyApplication/gradle/libs.versions.toml"
if (Test-Path $VesselCatalog) {
    Copy-Item $VesselCatalog -Destination "$TargetDir/gradle/libs.versions.toml"
    Write-Host "Linked Version Catalog from Vessel." -ForegroundColor Green
} else {
    Write-Warning "Vessel Version Catalog not found at $VesselCatalog. You will need to copy it manually."
}

# 3. Create settings.gradle.kts
$SettingsContent = @"
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}
rootProject.name = "battlebarge-$ModuleName"
include(":$ModuleName")
"@
Set-Content -Path "$TargetDir/settings.gradle.kts" -Value $SettingsContent

# 4. Create module build.gradle.kts
$BuildContent = @"
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "$Namespace"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
}
"@
Set-Content -Path "$TargetDir/$ModuleName/build.gradle.kts" -Value $BuildContent

Write-Host "Scaffold complete in $TargetDir" -ForegroundColor Cyan
Write-Host "Next steps:"
Write-Host "1. Open the project in Android Studio."
Write-Host "2. Create your @Composable entry point in the $ModuleName folder."
Write-Host "3. Dock it into the Battle Barge using 'includeBuild' in settings.gradle.kts."
