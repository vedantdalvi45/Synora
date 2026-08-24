# Fix AAR Metadata Conflict for androidx.core:core-ktx

The build is failing because `androidx.core:core-ktx:1.19.0` requires Android Gradle Plugin (AGP) 9.1.0 or higher, but the project is currently using AGP 8.13.2.

To resolve this, we can either:
1. **Downgrade `androidx.core:core-ktx`** to a version compatible with AGP 8.x (Recommended for stability).
2. **Upgrade AGP to 9.1.0+**, which also requires upgrading Gradle to 9.3.1+.

This plan focuses on **Option 1** as it is the most targeted and least disruptive fix.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///D:/Coding/Android%20Projects/Synora/gradle/libs.versions.toml)
- Downgrade `coreKtx` version from `1.19.0` to `1.15.0`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:checkDebugAarMetadata` to verify the metadata check passes.
- Run `./gradlew assembleDebug` to ensure the project builds successfully.
