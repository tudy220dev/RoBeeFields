# RoBee Fields

A local-first, low-overhead field log and geospatial mapping tool engineered for mobile apiary operations in zero-connectivity environments.

---

## Project Overview

RoBee Fields provides a streamlined Jetpack Compose interface for viewing offline topographic vector maps and pinning high-precision GPS coordinates for remote apiaries. The system enables structured hierarchical data management, ensuring robust individual tracking of each hive via assigned field IDs and localized notes.

---

## Design Philosophy

* **Zero Cloud Lock-in:** No mandatory user accounts, no remote database dependencies, and zero background analytics tracking.
* **Cold-Start Reliability:** Instant boot time with zero network timeouts; operates entirely on local device hardware.
* **Low Power Footprint:** Stripped of persistent background sync loops to protect battery life during extended off-grid field operations.

---

## Core Capabilities

* **Offline Cartographic Engine:** Direct rendering of vector maps and orthophotos on-device using MapsForge without cellular or satellite data connections.
* **Spatial Apiary Registry:** Fast coordinate acquisition for apiary perimeters and individual hive placements.
* **Fallback SMS Telemetry:** Compresses coordinates, colony status flags, and health indicators into compact text payloads for transmission over 2G/SMS in dead zones.
* **Local Data Persistence:** Immediate SQLite/Room commits for every field inspection, frame count, or treatment log.
* **Data Sovereignty & Local Backups:** One-tap export to structured open formats (CSV / JSON) directly to local storage. Your operational data never leaves the handset.
* **Sensor-Ready Architecture:** Extensible data schemas designed to ingest serial and packet radio inputs from external microclimate nodes and weather telemetry masts.

---

## Field Installation & Setup

1. **Install:** Grab the latest `.apk` binary directly from the repository's [Releases](../../releases) tab.
2. **Offline Maps:**
   * Obtain the required `.map` vector file (OpenStreetMap / MapsForge format).
   * Place the file in the designated application storage path (or locate it via the in-app file picker on initial startup).
3. **Required Permissions:**
   * **Location (Precise / Fine):** Mandatory for recording apiary boundaries and hive positions.
   * **SMS (Optional):** Used strictly for transmitting compressed emergency status packets over cellular networks without data coverage.

---

## Tech Stack

* **Language:** Kotlin (Coroutines, StateFlow)
* **UI Engine:** Jetpack Compose (Material 3, high-contrast sunlight tuning)
* **Cartography:** MapsForge
* **Persistence:** Android Jetpack Room / SQLite
* **Environment:** Android Studio

---

## Field Feedback & Issues

If you are field-testing the software and hit an edge-case bug, map freeze, or database exception:
1. Submit an entry via the [Issues](../../issues) tab.
2. Include your device model, Android OS version, and whether the test was performed under airplane mode.
