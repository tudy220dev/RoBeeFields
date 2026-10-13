# HivePoint Offline-First Expansion Plan

This plan covers the implementation of local Room database storage, offline map integration using osmdroid, and SMS/P2P BroadcastReceiver synchronization for the HivePoint decentralized beekeeping app.

## User Review Required

> [!IMPORTANT]
> - **Dependencies**: We will add Room (for local persistence), KSP (Kotlin Symbol Processing for Room), and osmdroid (for offline maps and GPS marker rendering).
> - **Permissions**: We will request `RECEIVE_SMS`, `READ_SMS`, `SEND_SMS`, `ACCESS_FINE_LOCATION`, and `ACCESS_COARSE_LOCATION` in `AndroidManifest.xml` for offline field telemetry and P2P sync.

## Proposed Changes

### 1. Build & Dependencies Configuration
#### [MODIFY] [libs.versions.toml](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/gradle/libs.versions.toml)
- Add versions and libraries for Room and osmdroid.
- Add KSP plugin alias if needed.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/build.gradle.kts)
- Apply KSP plugin.
- Add Room runtime, compiler (KSP), and coroutines/ktx support.
- Add osmdroid-android library.

---

### 2. Room Database & Local Entities
#### [NEW] [HiveEntity.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/data/HiveEntity.kt)
- Define Room Entities (`ApiaryEntity`, `BeehiveEntity`, `InspectionLogEntity`) matching domain models.
- Add TypeConverters for Enums (`HiveStatus`, `Temperament`).

#### [NEW] [HiveDao.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/data/HiveDao.kt)
- Create DAOs for querying apiaries, beehives, and inspection logs with Flow support.

#### [NEW] [HiveDatabase.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/data/HiveDatabase.kt)
- Room Database instance with Singleton builder.

---

### 3. SMS & P2P Sync Manager
#### [NEW] [SmsReceiver.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/sync/SmsReceiver.kt)
- BroadcastReceiver intercepting incoming SMS messages starting with `HP1|`.
- Uses `HiveSerializer` to parse and upsert beehive data into Room database locally.

#### [NEW] [SmsSender.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/sync/SmsSender.kt)
- Utility to send serialized hive strings via Android `SmsManager`.

---

### 4. Offline Map & UI Integration
#### [NEW] [OfflineMapScreen.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/ui/map/OfflineMapScreen.kt)
- Composable wrapping `org.osmdroid.views.MapView` using `AndroidView`.
- Renders offline pins for apiaries and beehives with status colors.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/radut/AndroidStudioProjects/HivePoint/app/src/main/java/com/example/hivepoint/MainActivity.kt)
- Update main activity to handle runtime permissions (SMS and Location) and display the OfflineMapScreen and database dashboard.

---

## Verification Plan

### Automated Tests
- Run unit tests for `HiveSerializer` and Room DAO queries.
- Build project using `gradle_build` to verify KSP and Room compilation.

### Manual Verification
- Test SMS receiving simulation or deployment on device/emulator.
- Verify map rendering and offline pin plotting.
