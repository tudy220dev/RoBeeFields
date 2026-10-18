# RoBee Fields


A local-first, low-overhead field log and geospatial mapping tool engineered for mobile apiary operations in zero-connectivity environments.


# Project Description


The mobile application provides a Jetpack Compose-based interface for viewing topographic maps and precise GPS localization of apiaries. The system enables hierarchical data organization, ensuring the individual identification of each hive through names or unique IDs assigned in the field.


# Design Philosophy

* **Zero Cloud Lock-in:** No mandatory accounts, no remote database dependencies, and zero analytics background tracking.
* **Cold-Start Reliability:** Instant boot time with zero network timeouts; runs entirely on local device silicon.
* **Low Power Footprint:** Optimized background loops to prevent battery drain while navigating or logging in remote locations.

# Main Features

* **Offline cartographic visualization:** Rendering of maps and orthophotos directly on the device without depending on an active internet connection.

* **Apiary management:** Recording and associating geographic coordinates for locations, along with the individual identification of hives.
  
* **Alternative reporting system:** Generating textual data packages for sending coordinates and apiary status via SMS in areas lacking data signal.

* **Data persistence:** Local storage optimized to ensure the retention of records made during field sessions.

# Tech Stack


Language: Kotlin

UI Framework: Jetpack Compose (Material 3)

Offline maps database: MapsForge

Storage: Local database (Room / SQLite)

Development Environment: Android Studio
