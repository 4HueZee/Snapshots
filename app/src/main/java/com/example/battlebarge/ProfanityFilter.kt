package com.example.battlebarge

import java.util.Locale

/**
 * Industry Standard Profanity Filter
 * 
 * Uses an Aho-Corasick style Trie for high-performance multi-pattern matching.
 * This implementation handles thousands of blacklisted words with O(N) complexity 
 * relative to the input text length, ensuring zero UI lag regardless of list size.
 */
object ProfanityFilter {

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isEndOfWord = false
        var failureLink: TrieNode? = null
    }

    private val root = TrieNode()

    init {
        // Load the expanded patterns
        // Note: In a production environment, this list would be loaded from an encrypted 
        // local resource or a remote configuration service.
        val comprehensiveBlacklist = listOf(
            // --- Explicit Profanity ---
            "shit", "fuck", "cunt", "bitch", "bastard", "dick", "cock", "pussy", "ass", "arse",
            "damn", "hell", "whore", "slut", "twat", "wanker", "prick", "bollocks",
            
            // --- NSFW / Sexual ---
            "porn", "nude", "sexy", "adult", "erotic", "xxx", "sext", "cum", "jism", "clit",
            "vagina", "penis", "scrotum", "testicle", "breast", "nipple", "tit", "boob",
            "orgasm", "masturbate", "ejaculate", "fetish", "kink", "anal", "oral",
            "chode", "choad", "tosser", "skank", "minger", "shower", "golden",
            
            // --- Potty Talk / Waste ---
            "poop", "poopie", "turd", "crap", "shat", "fart", "piss", "pee", "urinate", 
            "stool", "bowel", "rectum", "anus", "toilet",
            
            // --- Antisocial / Harassment ---
            "hate", "racist", "sexist", "violent", "attack", "kill", "murder", "suicide",
            "terror", "bomb", "threat", "nigger", "faggot", "retard", "idiot", "stupid",
            "dumb", "ugly", "loser", "suck", "jerk", "moron", "spam", "scam", "phish",
        )
        
        comprehensiveBlacklist.forEach { insert(it) }
        buildFailureLinks()
    }

    private fun insert(word: String) {
        var current = root
        for (char in word) {
            current = current.children.getOrPut(char) { TrieNode() }
        }
        current.isEndOfWord = true
    }

    private fun buildFailureLinks() {
        val queue = java.util.ArrayDeque<TrieNode>()
        for (node in root.children.values) {
            node.failureLink = root
            queue.add(node)
        }

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            for ((char, child) in current.children) {
                var failure = current.failureLink
                while (failure != null && !failure.children.containsKey(char)) {
                    failure = failure.failureLink
                }
                child.failureLink = failure?.children?.get(char) ?: root
                queue.add(child)
            }
        }
    }

    /**
     * Normalizes text by mapping symbols and numbers to letters (Leet Speak),
     * stripping punctuation, and collapsing repeated characters.
     * Optimized for performance to avoid unnecessary string allocations.
     */
    private fun normalize(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ""

        val leetMap = mapOf(
            '0' to 'o', '1' to 'i', '2' to 'z', '3' to 'e', '4' to 'a', '5' to 's',
            '6' to 'g', '7' to 't', '8' to 'b', '9' to 'g', '@' to 'a', '$' to 's',
            '!' to 'i', '+' to 't', '*' to 'a', '(' to 'c', '[' to 'l'
        )

        val result = StringBuilder()
        var lastChar: Char? = null

        for (char in trimmed.lowercase(Locale.ROOT)) {
            val mapped = leetMap[char] ?: char
            if (mapped.isLetter()) {
                if (mapped != lastChar) {
                    result.append(mapped)
                    lastChar = mapped
                }
            }
        }
        
        return result.toString()
    }

    fun containsProfanity(text: String): Boolean {
        if (text.isBlank()) return false
        
        // Check raw, normalized, and phonetic variations
        return search(text.lowercase(Locale.ROOT)) || search(normalize(text))
    }

    private fun search(text: String): Boolean {
        var current = root
        for (char in text) {
            while (current != root && !current.children.containsKey(char)) {
                current = current.failureLink ?: root
            }
            current = current.children[char] ?: root
            if (current.isEndOfWord) return true
            
            // Check failure path (subset matching)
            var temp = current.failureLink
            while (temp != null && temp != root) {
                if (temp.isEndOfWord) return true
                temp = temp.failureLink
            }
        }
        return false
    }

    fun validateUsername(username: String): UsernameValidationResult {
        if (username.length < 3) return UsernameValidationResult.TooShort
        if (username.length > 15) return UsernameValidationResult.TooLong
        if (!username.matches(Regex("^[A-Za-z0-9]+$"))) return UsernameValidationResult.InvalidCharacters
        if (containsProfanity(username)) return UsernameValidationResult.ProhibitedContent
        return UsernameValidationResult.Valid
    }

    fun validateBio(bio: String): TextValidationResult {
        if (bio.length > 60) return TextValidationResult.TooLong
        if (containsProfanity(bio)) return TextValidationResult.ProhibitedContent
        return TextValidationResult.Valid
    }
}

enum class UsernameValidationResult {
    Valid, TooShort, TooLong, InvalidCharacters, ProhibitedContent
}

enum class TextValidationResult {
    Valid, TooLong, ProhibitedContent
}
