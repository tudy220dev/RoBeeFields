# RoBee Fields


A software project developed for university purposes for the digital management of apiaries and bee colonies in field conditions (offline-first). The application is intended for beekeepers operating in isolated areas without stable GSM coverage.


# Project Description


The mobile application provides a Jetpack Compose-based interface for viewing topographic maps and precise GPS localization of apiaries. The system enables hierarchical data organization, ensuring the individual identification of each hive through names or unique IDs assigned in the field.


# Main Features


Offline cartographic visualization: Rendering of maps and orthophotos directly on the device without depending on an active internet connection.

Apiary management: Recording and associating geographic coordinates for locations, along with the individual identification of hives.

Alternative reporting system: Generating textual data packages for sending coordinates and apiary status via SMS in areas lacking data signal.

Data persistence: Local storage optimized to ensure the retention of records made during field sessions.


# Tech Stack


Language: Kotlin

UI Framework: Jetpack Compose (Material 3)

Storage: Local database (Room / SQLite)

Development Environment: Android Studio
