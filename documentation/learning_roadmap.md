# Battle Barge: Learning & Optimization Roadmap

This document tracks architectural decisions, technical debt, and future optimization ideas. Revisit these items as your familiarity with Android development grows.

---

## 🟢 Architecture & Navigation

### 1. Unified Navigation (Login vs. Main)
- **Current State:** The `LoginScreen` and `MainScreen` (NavHost) are switched using a hard `if/else` check in `MainActivity.kt`.
- **The Observation:** This is clean for a beginner app, but prevents shared transitions or complex "deep linking" (e.g., clicking a link to go straight to a specific profile if logged in).
- **Future Optimization:** Move the `LoginScreen` into the `NavHost`. Use "Conditional Navigation" to decide the start destination.
- **Why wait?** It requires understanding "Navigation Graphs" and "State Saving" in more detail.

---

## 🟡 UI & Performance

### (Entry Pending...)

---

## 🔴 Data & Security

### 1. Narrative Systems & Procedural Generation
- **Concept:** Using "Invisible Permanence" to generate thematic descriptions without extra database storage.
- **Seeded Randomization:** Use User/World IDs as a mathematical "seed" to ensure a user always sees the same word (e.g., "Conscripted") every time they log in.
- **Encoded/Semantic IDs:** For manual control, IDs can be tagged (e.g., `W_VOLC_7721`) so the app instantly knows to use the "Volcanic" dictionary.
- **Modular Dictionaries:** Keep word lists in separate files (Account, Worlds, Items) so they are only loaded into memory when that specific screen is visible.
- **Goal:** Achieve complex storytelling that is "Permanently Costless" to maintain and sync in a multiplayer environment.
