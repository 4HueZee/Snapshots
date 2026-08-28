# Google Login Integration with Credential Manager

This plan fixes the current Google Login implementation by correcting syntax errors, adding required permissions, and properly handling the Credential Manager response.

## User Review Required

> [!IMPORTANT]
> **Web Client ID**: You must provide a valid Web Client ID from the [Google Cloud Console](https://console.cloud.google.com/). I will add a placeholder in `strings.xml` for you to replace.
>
> **SHA-1 Fingerprint**: Ensure your app's SHA-1 fingerprint is registered in the Google Cloud Console for your Android Client ID.

## Proposed Changes

### [Component] Android Manifest

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/AndroidManifest.xml)
- Add `INTERNET` permission required for Google Sign-In to communicate with authentication servers.

### [Component] Resources

#### [MODIFY] [strings.xml](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/res/values/strings.xml)
- Add a string resource `google_web_client_id` to store the Server Client ID centrally.

### [Component] Login UI Logic

#### [MODIFY] [LoginScreen.kt](file:///C:/Users/Owner/AndroidDev/Project/MyApplication/app/src/main/java/com/example/battlebarge/LoginScreen.kt)
- Correct the `async` keyword to `suspend` (Kotlin idiomatic).
- Use `context.getString(R.string.google_web_client_id)` to load the client ID.
- Add logic to extract the `idToken` from the `GoogleIdTokenCredential` after a successful sign-in.
- Add necessary imports for `CustomCredential` and `GoogleIdTokenCredential`.

## Verification Plan

### Automated Tests
- None at this stage; manual verification is required for OAuth flows.

### Manual Verification
1. Replace the placeholder in `strings.xml` with your real Web Client ID.
2. Build and run the app on a device with Google Play Services.
3. Tap "Sign in with Google" and verify that the Google Account picker appears.
4. Check the Logcat for successful token retrieval (I will add a log statement).
