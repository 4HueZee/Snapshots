## 1. Advanced Architecture: Aho-Corasick
The filter now uses an **Aho-Corasick Trie Automaton**, the industry standard for high-performance string matching (used by search engines and enterprise firewalls).

### How it works:
- **Trie Construction**: All blacklisted words are built into a tree structure during app initialization.
- **Failure Links**: If a match fails partially, the algorithm "jumps" to the next possible suffix match instantly, rather than restarting.
- **Constant Time Performance**: Searching for 1 word or 10,000 words takes exactly the same amount of time relative to the input text. This ensures the UI never lags during validation.

## 2. Multi-Layer Normalization
We continue to use a normalization pass before the search to catch workarounds:
1. **Symbol Mapping**: `@` -> `a`, `$` -> `s`, `1` -> `i`, etc.
2. **Space/Punctuation Stripping**: `f_u_c_k` -> `fuck`.
3. **Character De-duplication**: `fuuuuuuuuuck` -> `fuck`.

## 3. Data Integrity
The filter is now exceptionally strict across four main categories:
1. **Explicit Profanity**: Standard cuss words.
2. **NSFW / Sexual**: Suggestive language and anatomical slang.
3. **Potty Talk**: Immature/Toilet humor.
4. **Antisocial / Harassment**: Hate speech, slurs, and threatening language.

## 4. Scalability
This architecture is "Import-Ready." If we decide to use the full **LDNOOBW** dataset (10,000+ words), we simply add them to the `init` block. The memory overhead will remain low (< 500KB), and performance will remain near-instant.
