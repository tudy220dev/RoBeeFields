<div align="center">

# RoBee Fields

**[RO - Română](#ro---română) | [EN - English](#en---english)**

</div>

---

# RO - Ghid pentru Apicultori

**Caiet digital de stupină și hartă topografică offline**, construit pentru pastoral, vetre izolate și zone de munte unde semnalul la telefon lipsește cu desăvârșire.

---

## Ce face aplicația în stupină

RoBee Fields înlocuiește carnetul de hârtie care se udă sau se pierde în iarbă, fără să te oblige să depinzi de internet sau de conturi online:

* **Hărți complete fără semnal (Offline):** Vezi vatra, drumurile forestiere și relieful din zonă direct pe ecran, fără să ai nevoie de date mobile.
* **Poziționare GPS pentru fiecare stup:** Înregistrezi coordonatele exacte ale fiecărui stup sau pavilion și le găsești ușor în pastoral, chiar și noaptea sau pe ceață.
* **Fără conturi și fără abonamente:** Aplicația pornește instantaneu. Datele rămân doar pe telefonul tău, fără parole, reclame sau sincronizări ascunse care consumă bateria.
* **Jurnal de intervenții pe loc:** Notezi rapid starea familiei de albine, regina, tratamentele efectuate sau spațiul de cules.
* **Trimite locația prin SMS când nu ai date:** Dacă ești într-o zonă fără internet și ai nevoie de ajutor sau vrei să transmiți coordonatele stupinei către colegi, aplicația comprimă datele într-un SMS scurt care trece chiar și prin cel mai slab semnal 2G.
* **Datele tale îți aparțin:** Poți exporta oricând evidența stupinei (în format tabelar/CSV) direct pe telefon sau pe un card de memorie, ca să nu pierzi istoricul anilor trecuți.

---

## Cum o folosești la stupi

1. **Instalare:** Descarcă fișierul de instalare `.apk` din secțiunea de [Releases](../../releases) și deschide-l pe telefon.
2. **Adăugarea hărții zonei:** 
   * Descarcă fișierul de hartă vectorială gratuită (`.map` pentru România sau regiunea ta).
   * Pune fișierul în telefon și selectează-l din meniul aplicației — harta va fi disponibilă permanent, chiar dacă telefonul este pe modul Avion.
3. **Înregistrarea stupilor:**
   * Activează GPS-ul telefonului când ești lângă stup.
   * Apasă pe marcarea locației, trece numărul stupului și salvează datele cu o singură atingere.

---

## Feedback din Pastoral

Dacă testezi aplicația la stupină și observi ceva care nu este practic sau dacă găsești vreo problemă în utilizare:
* Deschide un tichet la secțiunea [Issues](../../issues) sau trimite-ne un mesaj cu modelul telefonului și ce anume ar trebui îmbunătățit pentru munca directă la stupi.

<details>
<summary><b>Detalii Tehnice & Arhitectură (Pentru Dezvoltatori)</b></summary>

* **Limbaj:** Kotlin (Coroutines, Flow)
* **Interfață:** Jetpack Compose (Material 3 cu contrast ridicat pentru lumină solară)
* **Motor Hărți:** MapsForge (randare offline)
* **Bază de date:** SQLite local prin Android Jetpack Room
* **Pregătit pentru senzori:** Suport arhitectural pentru integrarea viitoare a datelor primite prin radio de la cântare de stup și noduri de microclimat.
</details>

---
---

# EN - Beekeepers Field Guide

**A local-first digital apiary log and offline topographic map**, engineered for migratory beekeepers and out-apiaries operating in deep valleys, forests, and remote terrain without mobile data coverage.

---

## Practical Apiary Utility

RoBee Fields eliminates wet paper hive logs without trapping your data inside proprietary cloud accounts or draining your battery:

* **Complete Maps Without Cellular Service:** Access topographic maps and forest access roads directly on your device without cellular signal.
* **Pinpoint GPS Hive Mapping:** Map out exact coordinates for every hive body, nucleus, or trailer on the yard—making night moves and audits straightforward.
* **Zero Accounts, Zero Subscriptions:** Fires up instantly with no login walls. Your logs stay strictly on your handset without sneaky analytics draining your battery in the field.
* **Quick Yard Inspections:** Record colony strength, queen status, supers added, and medical treatments with minimal taps.
* **Fallback SMS Status Reports:** Need to transmit your yard location or status report from a dead zone? The app condenses critical data into a tight text package that transmits over a weak 2G signal.
* **Data Sovereignty:** Back up and export your entire hive history to open spreadsheet formats (CSV) directly to local storage or an SD card anytime.

---

## How to Deploy in the Field

1. **Install:** Download the `.apk` file directly from the [Releases](../../releases) page and run the install on your device.
2. **Load Your Map:**
   * Download the offline `.map` file for your operational region.
   * Store it on internal memory and select it in the app settings—cartography will render seamlessly even in full Airplane Mode.
3. **Log Hives:**
   * Ensure high-accuracy GPS is enabled on your device.
   * Stand at the hive stand, tap to record position, enter the hive identifier, and save.

---

## Field Feedback

If you run this application out in the yards and encounter interface friction or edge-case bugs:
* File a report on our [Issues](../../issues) tab detailing your device model and notes on how it handled your fieldwork.

<details>
<summary><b>Technical Stack & Architecture (Developer Notes)</b></summary>

* **Core:** Kotlin (Coroutines, StateFlow)
* **UI:** Jetpack Compose (Material 3, optimized for high ambient outdoor light)
* **Cartography:** MapsForge offline vector engine
* **Database:** On-device SQLite via Android Jetpack Room
* **Hardware Expansion:** Data models designed to accept future radio packet inputs from hive scales and microclimate yard sensors.
</details>
