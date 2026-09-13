package com.battlebarge.agnostic.data.remote

/**
 * A curated manifest of Sci-Fi game systems from the BSData organization.
 * Allows the app to provide a rich discovery library without expensive organization crawling.
 */
object SciFiAtlas {
    val games = listOf(
        DiscoveredGame(
            name = "Warhammer 40,000 10th Edition",
            description = "The latest edition of the grim dark future.",
            defaultBranch = "main",
            owner = "BSData",
            repoName = "wh40k-10th-edition"
        ),
        DiscoveredGame(
            name = "Warhammer 40,000 9th Edition",
            description = "Previous edition rules and catalogues.",
            defaultBranch = "master",
            owner = "BSData",
            repoName = "wh40k-9th-edition"
        ),
        DiscoveredGame(
            name = "Kill Team (2024 Edition)",
            description = "Skirmish combat in the 41st Millennium.",
            defaultBranch = "main",
            owner = "BSData",
            repoName = "kill-team"
        ),
        DiscoveredGame(
            name = "The Horus Heresy",
            description = "Age of Darkness - Galactic Civil War.",
            defaultBranch = "master",
            owner = "BSData",
            repoName = "horus-heresy"
        )
    )
}

/**
 * Updated data class for game systems to include repository specific names.
 */
data class DiscoveredGame(
    val name: String,
    val description: String?,
    val defaultBranch: String,
    val owner: String,
    val repoName: String
)
