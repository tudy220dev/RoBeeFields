# RoBee Fields

Un instrument local-first de cartografiere geospațială și jurnal de teren, optimizat pentru consum redus de energie și utilizare în stupine mobile aflate în zone complet izolate de rețeaua GSM.

---

## Descrierea Proiectului

RoBee Fields oferă o interfață bazată pe Jetpack Compose pentru vizualizarea hărților topografice vectoriale și localizarea precisă prin GPS a stupinelor și vetrelor pastorale. Aplicația permite organizarea ierarhică a datelor, garantând identificarea individuală a fiecărui stup prin identificatori unici (ID) sau denumiri atribuite direct în teren.

---

## Filozofie de Proiectare

* **Zero Cloud Lock-in:** Fără conturi obligatorii, fără baze de date la distanță și fără module de urmărire sau analiză în fundal.
* **Pornire la Rece Fiabilă (Cold-Start):** Lansare instantanee fără timeout-uri de rețea; procesarea rulează exclusiv pe procesorul dispozitivului local.
* **Amprentă Minimă de Consum:** Fără bucle de sincronizare ascunse în fundal, prevenind descărcarea bateriei în timpul navigării prelungite pe teren.

---

## Caracteristici Principale

* **Cartografie Offline Integrată:** Randare nativă de hărți vectoriale și ortofotoplanuri direct pe dispozitiv via MapsForge, fără conexiune la internet.
* **Gestiune Spațială a Stupinelor:** Înregistrarea și asocierea coordonatelor geografice precise pentru vetre și poziționarea individuală a stupilor.
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

* **Limbaj:** Kotlin (Coroutines, Flow)
* **Interfață Utilizator:** Jetpack Compose (Material 3, contrast adaptat pentru lumină solară)
* **Randare Hărți Offline:** MapsForge
* **Bază de Date Locală:** Android Jetpack Room / SQLite
* **Mediu de Dezvoltare:** Android Studio

---

## Feedback & Raportare Probleme

Dacă testezi aplicația în teren și identifici blocaje, erori GPS sau probleme de randare a hărții:
1. Deschide o sesizare nouă în secțiunea [Issues](../../issues).
2. Menționează modelul telefonului, versiunea de Android și dacă datele mobile au fost oprite în momentul testului.
