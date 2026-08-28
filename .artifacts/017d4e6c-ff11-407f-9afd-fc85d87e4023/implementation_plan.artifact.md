# Implementation Plan - Agnostic Roster Engine (:agnostic)

Build the core engine for Battle Barge that allows on-demand fetching, validating, and caching of game system rules from GitHub.

## User Review Required

> [!IMPORTANT]
> **Namespace Alignment:** The `build.gradle.kts` currently uses `com.example.battlebarge.agnostic`. The user's outline suggests `com.battlebarge.agnostic`. I will align with the user's preferred package `com.battlebarge.agnostic`.

> [!NOTE]
> **External Libraries:** I will add Room (for caching) and OkHttp/Retrofit (for GitHub API) to the project dependencies.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/gradle/libs.versions.toml)
- Add versions and library definitions for Room, Retrofit, and OkHttp.

#### [MODIFY] [build.gradle.kts (agnostic)](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/build.gradle.kts)
- Add Room and Networking dependencies.
- Apply KSP plugin for Room.

---

### Domain Layer
Defines the pure business models.

#### [NEW] [EngineNode.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/domain/model/EngineNode.kt)
- Core data model representing a unit or rule node.

---

### Data Layer - Local (Room)
Handles persistent caching of validated rules.

#### [NEW] [EngineNodeEntity.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/local/EngineNodeEntity.kt)
- Room entity for storing nodes.
#### [NEW] [NodeTagEntity.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/local/NodeTagEntity.kt)
- Room entity for tags associated with nodes.
#### [NEW] [EngineNodeDao.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/local/EngineNodeDao.kt)
- Data Access Object for roster operations.
#### [NEW] [RosterDatabase.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/local/RosterDatabase.kt)
- Room database definition (`roster_cache.db`).

---

### Data Layer - Remote & Parser
Handles fetching from GitHub and efficient XML processing.

#### [NEW] [GithubApiService.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/remote/GithubApiService.kt)
- Retrofit service for checking file SHAs via GitHub API.
#### [NEW] [RawDownloader.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/remote/RawDownloader.kt)
- OkHttp-based component for streaming raw `.cat` files from GitHub CDN.
#### [NEW] [XmlValidationChecker.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/parser/XmlValidationChecker.kt)
- Lightweight validator to prevent database corruption from malformed XML.
#### [NEW] [BsDataXmlParser.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/data/parser/BsDataXmlParser.kt)
- Streaming `XmlPullParser` implementation for high-performance ingestion.

---

### Repository & Presentation
Wires everything together and exposes it to the UI.

#### [NEW] [RosterRepository.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/domain/repository/RosterRepository.kt)
- Orchestrates the "Hash Check -> Conditional Download -> Validate -> Parse -> Cache" flow.
#### [NEW] [RosterViewModel.kt](file:///C:/Users/Owner/AndroidDev/Project/BattleBarge/agnostic/src/main/java/com/battlebarge/agnostic/presentation/RosterViewModel.kt)
- Exposes `StateFlow` to the UI.

## Verification Plan

### Automated Tests
- **Unit Tests for Parser:** Verify `BsDataXmlParser` correctly handles various XML structures.
- **Unit Tests for Validator:** Ensure `XmlValidationChecker` rejects malformed files.
- **Database Tests:** Verify Room transactions and indices.

### Manual Verification
- Deploy to device and trigger a download of a sample `.cat` file from a public GitHub repository.
- Inspect `Logcat` for SHA comparison logs and transaction status.
