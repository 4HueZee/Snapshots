# Privacy & Security Features

This document tracks the privacy and security capabilities implemented within the Battle Barge vessel.

## Password Management
Users signed in via Email/Password have access to two management flows:

### 1. Direct Password Change
- **Location**: Privacy Settings -> Change Password
- **Mechanism**: In-app dialog allowing the user to set a new password immediately.
- **Security Requirement**: Requires a "recent login." If the user hasn't signed in recently, they will be prompted to re-log in for security.

### 2. Email Password Reset
- **Location**: Privacy Settings -> Reset via Email
- **Mechanism**: Sends a Firebase-generated password recovery link to the user's registered email address.
- **Use Case**: Preferred if the user has forgotten their password or wants to use a standard recovery flow.

## Account Lifecycle
### Account Deletion
- **Location**: Privacy Settings -> Delete Account
- **Mechanism**: Permanently removes the user's account from Firebase Auth.
- **Security Requirement**: Like password changes, this requires a recent login session.

## Verification
- **Email Verification**: The status of the user's email verification is displayed at the top of the Privacy section to encourage account security.
