<div align="center">

# RoBee Fields

**[RO - Română](#ro---română) | [EN - English](#en---english)**

</div>

---

# RO - Română

Aplicație mobilă local-first pentru cartografiere geospațială și jurnal de teren, optimizată pentru consum redus de energie și utilizare în stupine mobile aflate în zone izolate fără acoperire GSM.

---

## Descrierea Proiectului

RoBee Fields oferă o interfață bazată pe Jetpack Compose pentru vizualizarea hărților topografice vectoriale și localizarea precisă prin GPS a stupinelor și vetrelor pastorale. Aplicația permite organizarea ierarhică a datelor, garantând identificarea individuală a fiecărui stup prin identificatori unici (ID) sau denumiri atribuite direct în teren.

---

## Filozofie de Proiectare

* **Zero Cloud Lock-in:** Fără conturi obligatorii, fără baze de date la distanță și fără module de urmărire sau analiză în fundal.
* **Pornire la Rece Fiabilă (Cold-Start):** Lansare instantanee fără timeout-uri de rețea; procesarea rulează exclusiv pe hardware-ul dispozitivului local.
* **Amprentă Minimă de Consum:** Fără bucle de sincronizare ascunse în fundal, prevenind descărcarea bateriei în timpul sesiunilor prelungite de teren.

---

## Caracteristici Principale

* **Cartografie Offline Integrată:** Randare nativă de hărți vectoriale și ortofotoplanuri direct pe dispozitiv via MapsForge, fără conexiune activă la internet.
* **Gestiune Spațială a Stupinelor:** Înregistrarea și asocierea coordonatelor geografice precise pentru vetre și poziționarea fiecărui stup în parte.
* **Sistem Alternativ de Raportare (SMS Fallback):** Generare de pachete text comprimate cu locația și starea stupinei pentru transmitere prin SMS în zone cu semnal 2G slab sau fără date mobile.
* **Persistență Locală a Datelor:** Toate înregistrările de inspecție și tratamente sunt salvate instantaneu într-o bază de date locală SQLite (Room).
* **Suveranitatea Datelor:** Export structurat (CSV / JSON) și backup direct pe card SD sau stocare internă. Datele nu părăsesc niciodată telefonul tău.
* **Arhitectură Extensibilă pentru Senzori:** Schemă de date pregătită pentru ingestia pachetelor de telemetrie de la stații meteo locale și noduri radio de microclimat.

---

## Ghid de Instalare și Utilizare în Teren

1. **Instalare:** Descarcă cel mai recent pachet `.apk` din secțiunea [Releases](../../releases) a repository-ului.
2. **Hărți Offline:**
   * Descarcă fișierul de hartă vectorială `.map` dorit (MapsForge / OpenStreetMap).
   * Plasează fișierul în memoria internă în folderul dedicat aplicației (sau selectează fișierul direct din interfață la prima pornire).
3. **Permisiuni Necesare:**
   * **Location (Precise / GPS):** Esențială pentru citirea poziției vetrei și marcarea stupilor.
   * **SMS (Opțional):** Folosită strict pentru generarea și transmiterea pachetelor de avarie prin mesaje text scurte.

---

## Tehnologii Utilizate (Tech Stack)

* **Limbaj:** Kotlin (Coroutines, StateFlow)
* **Interfață Utilizator:** Jetpack Compose (Material 3, contrast optimizat pentru lumină solară)
* **Randare Hărți Offline:** MapsForge
* **Bază de Date Locală:** Android Jetpack Room / SQLite
* **Mediu de Dezvoltare:** Android Studio

---

## Feedback & Raportare Probleme

Dacă testezi aplicația în teren și identifici blocaje, erori GPS sau probleme de randare a hărții:
1. Deschide o sesizare nouă în secțiunea [Issues](../../issues).
2. Menționează modelul telefonului, versiunea de Android și dacă datele mobile au fost oprite în momentul testului.

---
---

# EN - English

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
