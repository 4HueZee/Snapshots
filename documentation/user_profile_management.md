# User Profile Management

The Battle Barge allows users to customize their identity and preference settings. All user data is persisted in Firestore and handled securely.

## Profile Fields
Users can customize the following fields:

### 1. Display Name
- **Validation**: 3-15 alphanumeric characters.
- **Filtering**: Checked against a strict profanity and anti-social behavior filter before saving. This includes NSFW language, phonetic workarounds, and immature "potty talk" / toilet humor.
- **Uniqueness**: Must be unique across all users.

### 2. Bio
- **Purpose**: A short personal statement or description.
- **Limit**: Max 3 lines.

### 3. Interaction Region
- **Purpose**: Indicates the user's preferred region for online interactions and data routing.
- **Options**: NA, EU, Asia, OCE, SA, Africa.
- **Note**: Actual precise location data is handled via background permissions and is not exposed as profile information.

### 4. Profile Visibility
- **Public**: Profile is visible to others for social interactions.
- **Private**: Profile is hidden from search and discovery.

## Technical Implementation
- **Repository**: `UserRepository.kt` handles all Firestore transactions.
- **UI Component**: `ProfileCustomizationModule.kt` provides the editing interface using standard Compose Material 3 components.
- **Validation**: Profile updates are sent as a single atomic transaction.
