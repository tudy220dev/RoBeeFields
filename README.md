<div align="center">

# RoBee Fields

**[RO](#ro---română) | [EN](#en---english) | [FR](#fr---français) | [HU](#hu---magyar) | [DE](#de---deutsch) | [UK](#uk---українська) | [RU](#ru---русский) | [BG](#bg---български) | [PT](#pt---português) | [IT](#it---italiano) | [ES](#es---español)**

</div>

---

# RO - Ghid pentru Apicultori

**Caiet digital de stupină și hartă topografică offline**, construit pentru pastoral, vetre izolate și zone de munte unde semnalul la telefon lipsește cu desăvârșire.

---

## Ce face aplicația în stupină

RoBee Fields înlocuiește carnetul de hârtie care se udă sau se pierde în iarbă, fără să te oblige să depinzi de internet sau de conturi online:

- **Hărți complete fără semnal (Offline):** Vezi vatra, drumurile forestiere și relieful din zonă direct pe ecran, fără să ai nevoie de date mobile.
- **Poziționare GPS pentru fiecare stup:** Înregistrezi coordonatele exacte ale fiecărui stup sau pavilion și le găsești ușor în pastoral, chiar și noaptea sau pe ceață.
- **Fără conturi și fără abonamente:** Aplicația pornește instantaneu. Datele rămân doar pe telefonul tău, fără parole, reclame sau sincronizări ascunse care consumă bateria.
- **Jurnal de intervenții pe loc:** Notezi rapid starea familiei de albine, regina, tratamentele efectuate sau spațiul de cules.
- **Trimite locația prin SMS când nu ai date:** Dacă ești într-o zonă fără internet și ai nevoie de ajutor sau vrei să transmiți coordonatele stupinei către colegi, aplicația comprimă datele într-un SMS scurt care trece chiar și prin cel mai slab semnal 2G.
- **Datele tale îți aparțin:** Poți exporta oricând evidența stupinei (în format tabelar/CSV) direct pe telefon sau pe un card de memorie, ca să nu pierzi istoricul anilor trecuți.

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
- Deschide un tichet la secțiunea [Issues](../../issues) sau trimite-ne un mesaj cu modelul telefonului și ce anume ar trebui îmbunătățit pentru munca directă la stupi.

<details>
<summary><b>Detalii Tehnice & Arhitectură (Pentru Dezvoltatori)</b></summary>

- **Limbaj:** Kotlin (Coroutines, Flow)
- **Interfață:** Jetpack Compose (Material 3 cu contrast ridicat pentru lumină solară)
- **Motor Hărți:** MapsForge (randare offline)
- **Bază de date:** SQLite local prin Android Jetpack Room
- **Pregătit pentru senzori:** Suport arhitectural pentru integrarea viitoare a datelor primite prin radio de la cântare de stup și noduri de microclimat.
</details>

---
---

# EN - Beekeepers Field Guide

**A local-first digital apiary log and offline topographic map**, engineered for migratory beekeepers and out-apiaries operating in deep valleys, forests, and remote terrain without mobile data coverage.

---

## Practical Apiary Utility

RoBee Fields eliminates wet paper hive logs without trapping your data inside proprietary cloud accounts or draining your battery:

- **Complete Maps Without Cellular Service:** Access topographic maps and forest access roads directly on your device without cellular signal.
- **Pinpoint GPS Hive Mapping:** Map out exact coordinates for every hive body, nucleus, or trailer on the yard—making night moves and audits straightforward.
- **Zero Accounts, Zero Subscriptions:** Fires up instantly with no login walls. Your logs stay strictly on your handset without sneaky analytics draining your battery in the field.
- **Quick Yard Inspections:** Record colony strength, queen status, supers added, and medical treatments with minimal taps.
- **Fallback SMS Status Reports:** Need to transmit your yard location or status report from a dead zone? The app condenses critical data into a tight text package that transmits over a weak 2G signal.
- **Data Sovereignty:** Back up and export your entire hive history to open spreadsheet formats (CSV) directly to local storage or an SD card anytime.

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
- File a report on our [Issues](../../issues) tab detailing your device model and notes on how it handled your fieldwork.

<details>
<summary><b>Technical Stack & Architecture (Developer Notes)</b></summary>

- **Core:** Kotlin (Coroutines, StateFlow)
- **UI:** Jetpack Compose (Material 3, optimized for high ambient outdoor light)
- **Cartography:** MapsForge offline vector engine
- **Database:** On-device SQLite via Android Jetpack Room
- **Hardware Expansion:** Data models designed to accept future radio packet inputs from hive scales and microclimate yard sensors.
</details>

---
---

# FR - Guide de Terrain pour Apiculteurs

**Registre de rucher numérique local et carte topographique hors-ligne**, conçu pour la transhumance, les ruchers isolés et les zones de montagne dépourvues de couverture réseau.

---

## Fonctionnalités Pratiques au Rucher

RoBee Fields remplace le carnet papier vulnérable aux intempéries sans enfermer vos données dans des comptes en ligne ni vider votre batterie :

- **Cartographie intégrale hors-ligne :** Visualisez l'emplacement de votre rucher, les pistes forestières et le relief directement sur l'écran, sans aucune connexion de données.
- **Localisation GPS précise de chaque ruche :** Enregistrez les coordonnées exactes de chaque ruche, ruchette ou remorque pour les retrouver facilement de nuit ou par temps de brouillard.
- **Sans compte ni abonnement :** Démarre instantanément sans écran de connexion. Les données restent exclusivement sur votre appareil, sans traqueurs en arrière-plan.
- **Journal d'inspection rapide :** Notez rapidement la force de la colonie, l'état de la reine, la pose de hausses ou les traitements sanitaires.
- **Transmission de secours par SMS :** Vous devez envoyer votre position ou un bilan depuis une zone blanche ? L'application compresse les informations essentielles dans un SMS court transmis même via un faible signal 2G.
- **Souveraineté des données :** Exportez l'ensemble de votre historique au format CSV ouvert directement sur la mémoire interne ou une carte SD.

---

## Utilisation au Rucher

1. **Installation :** Téléchargez le fichier `.apk` depuis la page des [Releases](../../releases) et lancez l'installation sur votre smartphone.
2. **Ajout de la carte :**
   * Téléchargez le fichier de carte vectorielle hors-ligne (`.map` pour votre région ou pays).
   * Enregistrez le fichier sur le téléphone et sélectionnez-le dans l'application — la carte reste accessible en mode Avion.
3. **Enregistrement des ruches :**
   * Activez le GPS haute précision à côté de la ruche.
   * Enregistrez la position, saisissez le numéro de la ruche et validez d'un seul geste.

---

## Retours d'Expérience

Si vous utilisez l'application sur le terrain et constatez un point d'amélioration ou un dysfonctionnement :
- Ouvrez un rapport dans l'onglet [Issues](../../issues) en précisant le modèle de votre smartphone et vos observations d'usage.

<details>
<summary><b>Détails Techniques & Architecture (Développeurs)</b></summary>

- **Langage :** Kotlin (Coroutines, StateFlow)
- **Interface :** Jetpack Compose (Material 3 avec contraste renforcé pour usage en plein soleil)
- **Moteur Cartographique :** MapsForge (rendu vectoriel hors-ligne)
- **Base de données :** SQLite local via Android Jetpack Room
- **Évolutivité matérielle :** Architecture prête à recevoir les trames radio de balances connectées et de capteurs de microclimat.
</details>

---
---

# HU - Terepi Útmutató Méhészeknek

**Helyi digitális méhészeti napló és offline domborzati térkép**, vándorméhészethez, elszigetelt telephelyekhez és térerő nélküli hegyvidéki területekhez tervezve.

---

## Mit nyújt az alkalmazás a méhészetben

A RoBee Fields feleslegessé teszi az elázó vagy fűben elvesző papírfüzeteket, felhőfiókok és adatforgalom kényszere nélkül:

- **Teljes térkép térerő nélkül (Offline):** Kövesd a telephelyet, az erdei utakat és a domborzatot közvetlenül a képernyőn, mobilinternet nélkül.
- **Pontos GPS-pozíció kaptáranként:** Rögzítsd az egyes kaptárak vagy vándorkocsik pontos koordinátáit; találd meg őket könnyedén éjszaka vagy ködben is.
- **Nincsenek fiókok és előfizetések:** Azonnal indul bejelentkezési falak nélkül. Az adatok kizárólag a telefonodon maradnak, akkumulátort merítő háttérfolyamatok nélkül.
- **Gyors beavatkozási napló:** Jegyezd fel a család erősségét, az anya állapotát, a méztéri fiókok bővítését vagy a gyógykezeléseket pár érintéssel.
- **Helyzetmegosztás SMS-ben:** Ha lefedettség nélküli területen kell segítséget hívnod vagy koordinátákat átadnod, az app rövid SMS-be sűríti az adatokat, ami gyenge 2G hálózaton is átmegy.
- **A saját adataid feletti kontroll:** Bármikor exportálhatod a méhészet teljes nyilvántartását nyílt táblázatos formátumba (CSV) belső tárhelyre vagy SD-kártyára.

---

## Használat a telephelyen

1. **Telepítés:** Töltsd le az `.apk` telepítőfájlt a [Releases](../../releases) menüpontból, és nyisd meg a telefonodon.
2. **Térkép betöltése:**
   * Töltsd le a megfelelő ingyenes vektortérképet (`.map` fájl az adott országhoz/régióhoz).
   * Másold a telefon tárhelyére és válaszd ki az alkalmazásban — a térkép Repülőgép üzemmódban is hibátlanul renderelődik.
3. **Kaptárak rögzítése:**
   * Kapcsold be a nagy pontosságú GPS-t a kaptár mellett állva.
   * Rögzítsd a pozíciót, írd be a kaptár számát, és mentsd el egyetlen érintéssel.

---

## Terepi Visszajelzés

Ha teszteled az alkalmazást a méhészetben, és hibát találsz vagy javítanál az ergonómián:
- Nyiss egy hibajegyet az [Issues](../../issues) felületen a telefonod típusával és a gyakorlati tapasztalataiddal.

<details>
<summary><b>Technikai Részletek & Architektúra (Fejlesztőknek)</b></summary>

- **Nyelv:** Kotlin (Coroutines, StateFlow)
- **Felület:** Jetpack Compose (Material 3 magas kontraszttal az erős napfényhez)
- **Térképmotor:** MapsForge (offline vektormotor)
- **Adatbázis:** Helyi SQLite Android Jetpack Room segítségével
- **Hardveres bővíthetőség:** Támogatás kaptármérlegek és mikroklíma-szenzorok jövőbeli rádiós adatcsomagjaihoz.
</details>

---
---

# DE - Feldführer für Imker

**Lokales digitales Stockbuch und topografische Offline-Karte**, konzipiert für Wanderimkerei, abgelegene Standplätze und Bergregionen ohne Mobilfunkempfang.

---

## Nutzen auf dem Bienenstand

RoBee Fields ersetzt das durchnässte Papier-Stockbuch ohne Cloud-Zwang oder unnötigen Akkuverbrauch:

- **Vollständige Offline-Karten:** Überblicken Sie Bienenstand, Waldwege und Geländestruktur direkt auf dem Display – ganz ohne mobile Daten.
- **GPS-Verortung für jedes Volk:** Speichern Sie exakte Koordinaten für jede Beute, jeden Ablegerkasten oder Wanderwagen für sicheres Arbeiten bei Nacht und Nebel.
- **Keine Benutzerkonten, keine Abonnements:** Startet sofort ohne Login. Alle Daten verbleiben lokal auf Ihrem Smartphone ohne ressourcenfressende Hintergrund-Tracker.
- **Schnelles Inspektionsprotokoll:** Notieren Sie Volksstärke, Weiselrichtigkeit, aufgesetzte Zargen und Behandlungen mit minimalen Eingaben.
- **Standortübermittlung per SMS:** Müssen Sie Ihre Position aus einem Funkloch senden? Die App komprimiert wichtige Daten in eine kurze SMS, die auch über schwaches 2G übertragen wird.
- **Volle Datenhoheit:** Exportieren Sie Ihre Bestandsdaten jederzeit im offenen Tabellenformat (CSV) auf den internen Speicher oder eine SD-Karte.

---

## Einsatz in der Praxis

1. **Installation:** Laden Sie die `.apk`-Datei im Bereich [Releases](../../releases) herunter und installieren Sie diese auf Ihrem Smartphone.
2. **Karte laden:**
   * Laden Sie die passende Vektorkartendatei (`.map` für Ihre Region) herunter.
   * Speichern Sie diese auf dem Gerätespeicher und wählen Sie sie in der App aus – die Karte bleibt auch im Flugmodus vollständig verfügbar.
3. **Beuten erfassen:**
   * Aktivieren Sie den präzisen GPS-Modus direkt an der Beute.
   * Standort erfassen antippen, Beutennummer eingeben und mit einem Fingertipp speichern.

---

## Feedback aus der Praxis

Wenn Sie die Anwendung am Bienenstand testen und Verbesserungspotenzial oder Fehler entdecken:
- Erstellen Sie einen Eintrag unter [Issues](../../issues) mit Angabe Ihres Gerätemodells und Ihrer praktischen Anmerkungen.

<details>
<summary><b>Technischer Stack & Architektur (Entwickler)</b></summary>

- **Basis:** Kotlin (Coroutines, StateFlow)
- **UI:** Jetpack Compose (Material 3 mit hohem Kontrast für direktes Sonnenlicht)
- **Karten-Engine:** MapsForge (Offline-Vektorengine)
- **Datenbank:** Lokales SQLite über Android Jetpack Room
- **Hardware-Schnittstellen:** Architektur vorbereitet für Funkdaten von Stockwaagen und Mikroklima-Sensoren.
</details>

---
---

# UK - Польовий Довідник Пасічника

**Локальний цифровий журнал пасіки та офлайн-топографічна карта**, створений для кочівлі, віддалених точків та гірських районів без мобільного зв'язку.

---

## Практична користь на пасіці

RoBee Fields замінює паперові зошити, які мокнуть та губляться, не вимагаючи підключення до хмарних сервісів і не витрачаючи заряд батареї:

- **Повноцінні карти без зв'язку (Офлайн):** Переглядайте розташування пасіки, лісові дороги та рельєф безпосередньо на екрані без мобільного інтернету.
- **Точна GPS-прив'язка кожного вулика:** Фіксуйте точні координати кожного вулика, нуклеуса чи платформи для легкого пошуку вночі або в тумані.
- **Без акаунтів та передплат:** Миттєвий запуск без вікон авторизації. Ваші записи залишаються лише на пристрої без прихованої аналітики.
- **Швидкий журнал огляду:** Записуйте силу сім'ї, стан матки, встановлені корпуси та лікувальні обробки за лічені секунди.
- **Відправка координат через SMS:** Потрібно передати локацію або звіт із «глухої» зони? Додаток стискає дані у коротке SMS, що проходить навіть крізь слабкий сигнал 2G.
- **Контроль над даними:** Експортуйте всю історію пасіки у формат CSV безпосередньо в пам'ять телефону або на карту пам'яті SD.

---

## Як використовувати на практиці

1. **Встановлення:** Завантажте інсталяційний файл `.apk` із розділу [Releases](../../releases) та відкрийте його на пристрої.
2. **Додавання карти регіону:**
   * Завантажте векторний файл карти (`.map` для вашої області чи країни).
   * Збережіть файл у пам'яті телефону та оберіть його в налаштуваннях додатка — карта буде доступна навіть у режимі «У літаку».
3. **Реєстрація вуликів:**
   * Увімкніть високоточний GPS біля вулика.
   * Натисніть кнопку фіксації точки, вкажіть номер вулика та збережіть запис одним дотиком.

---

## Зворотний зв'язок

Якщо під час роботи на пасіці ви помітили незручності в інтерфейсі або технічні збої:
- Створіть запит у розділі [Issues](../../issues), вказавши модель вашого смартфона та зауваження щодо роботи в полі.

<details>
<summary><b>Технічні Деталі та Архітектура (Для розробників)</b></summary>

- **Мова:** Kotlin (Coroutines, StateFlow)
- **Інтерфейс:** Jetpack Compose (Material 3 з підвищеним контрастом для сонячного світла)
- **Картографічний рушій:** MapsForge (офлайн-рендеринг векторів)
- **База даних:** Локальна SQLite через Android Jetpack Room
- **Апаратна інтеграція:** Архітектура адаптована для майбутнього прийому радіопакетів від ваг вулика та датчиків мікроклімату.
</details>

---
---

# RU - Полевой Справочник Пчеловода

**Локальный цифровой журнал пасеки и офлайн топографическая карта**, разработанный для кочевого пчеловодства, удаленных точков и горных местностей без мобильной связи.

---

## Возможности приложения на пасеке

RoBee Fields заменяет бумажные журналы, которые промокают или теряются, не требуя облачных аккаунтов и не разряжая аккумулятор:

- **Полные карты без мобильной сети (Офлайн):** Изучайте местность, лесные дороги и рельеф прямо на экране без мобильного интернета.
- **Точное GPS-позиционирование каждого улья:** Фиксируйте точные координаты каждого улья, нуклеуса или прицепа для удобной работы ночью или в тумане.
- **Без учетных записей и подписок:** Мгновенный запуск без форм входа. Данные хранятся строго на вашем смартфоне без фоновой аналитики.
- **Оперативный журнал осмотров:** Быстро вносите силу пчелосемьи, состояние матки, добавленные корпуса и ветеринарные обработки.
- **Передача координат по SMS:** Нужно отправить геолокацию или отчет из зоны отсутствия связи? Приложение сжимает данные в короткое SMS, передаваемое даже через слабый 2G-сигнал.
- **Полная сохранность данных:** Экспортируйте журнал в открытый табличный формат (CSV) в память телефона или на SD-карту в любой момент.

---

## Как использовать на точке

1. **Установка:** Скачайте установочный файл `.apk` из раздела [Releases](../../releases) и запустите его на смартфоне.
2. **Загрузка карты:**
   * Загрузите файл векторной карты (`.map` для вашего региона).
   * Сохраните файл на устройстве и выберите его в настройках приложения — карта отображается даже в режиме «В самолете».
3. **Регистрация ульев:**
   * Включите точный GPS рядом с ульем.
   * Нажмите фиксацию положения, введите номер улья и сохраните запись одним касанием.

---

## Обратная связь

Если вы используете приложение на пасеке и столкнулись с ошибками или неудобным поведением:
- Создайте обращение во вкладке [Issues](../../issues), указав модель смартфона и описание ситуации.

<details>
<summary><b>Технические Детали и Архитектура (Для разработчиков)</b></summary>

- **Ядро:** Kotlin (Coroutines, StateFlow)
- **Интерфейс:** Jetpack Compose (Material 3 с повышенной контрастностью для яркого солнца)
- **Картографический движок:** MapsForge (офлайн векторный рендеринг)
- **База данных:** Локальная SQLite через Android Jetpack Room
- **Аппаратные расширения:** Структура данных готова к приему радиосигналов от весов и датчиков микроклимата.
</details>

---
---

# BG - Полеви Наръчник за Пчелари

**Локален дигитален дневник за пчелин и офлайн топографска карта**, създаден за подвижно пчеларство, изолирани райони и планински терени без обхват на мобилната мрежа.

---

## Практическа полза на пчелина

RoBee Fields заменя хартиения бележник без зависимост от интернет връзка, профили или излишен разход на батерия:

- **Пълни карти без мобилен сигнал (Офлайн):** Преглеждайте пчелина, горските пътища и релефа директно на екрана без мобилни данни.
- **Точно GPS позициониране за всеки кошер:** Запазвайте точните координати на всеки кошер, отводка или пчеларска платформа за лесна ориентация нощем или при мъгла.
- **Без профили и абонаменти:** Стартира незабавно без входни екрани. Данните остават изцяло на вашия телефон без фоново проследяване.
- **Бърз дневник за прегледи:** Отбелязвайте силата на семейството, майката, поставените корпуси и третиранията за секунди.
- **Изпращане на локация чрез SMS:** Намирате се в зона без обхват и се нуждаете от съдействие? Приложението компресира данните в кратък SMS, който се изпраща дори при слаб 2G сигнал.
- **Пълен контрол над данните:** Експортирайте архива на пчелина в отворен табличен формат (CSV) директно на устройството или на SD карта.

---

## Как се използва на пчелина

1. **Инсталация:** Изтеглете файла `.apk` от секцията [Releases](../../releases) и го инсталирайте на телефона.
2. **Добавяне на карта:**
   * Изтеглете векторния файл с карта (`.map` за вашия регион или държава).
   * Поставете файла в паметта на телефона и го изберете от приложението — картата ще работи напълно и в самолетен режим.
3. **Записване на кошери:**
   * Включете GPS с висока точност до кошера.
   * Натиснете за маркиране на локация, въведете номера на кошера и запазете с едно докосване.

---

## Обратна връзка от полето

Ако изпробвате приложението на пчелина и откриете проблем или имате идея за подобрение:
- Отворете тема в раздел [Issues](../../issues) с модела на телефона и описание на работния процес.

<details>
<summary><b>Технически Стек & Архитектура (За Разработчици)</b></summary>

- **Език:** Kotlin (Coroutines, StateFlow)
- **Интерфейс:** Jetpack Compose (Material 3 с висок контраст за директна слънчева светлина)
- **Картографски модул:** MapsForge (офлайн векторно рендиране)
- **База данни:** Локална SQLite чрез Android Jetpack Room
- **Хардуерна съвместимост:** Архитектурна готовност за получаване на радиопакети от пчелни везни и микроклиматични сензори.
</details>

---
---

# PT - Guia de Campo para Apicultores

**Registo digital de apiário local e mapa topográfico offline**, concebido para apicultura transumante, apiários isolados e zonas de montanha sem cobertura de rede móvel.

---

## Utilidade Prática no Apiário

O RoBee Fields substitui o caderno de campo sem prender os seus dados a serviços na nuvem nem esgotar a bateria do seu telemóvel:

- **Mapas completos sem rede móvel (Offline):** Visualize o apiário, acessos florestais e curvas de nível diretamente no ecrã sem consumir dados móveis.
- **Geolocalização GPS de cada colmeia:** Guarde as coordenadas exatas de cada colmeia, núcleo ou atrelado para fácil localização noturna ou sob nevoeiro.
- **Sem contas nem subscrições:** Arranca instantaneamente sem ecrãs de registo. Os seus dados ficam unicamente no telemóvel sem processos analíticos ocultos.
- **Registo rápido de maneio:** Anote rapidamente a força da colónia, estado da rainha, alças adicionadas e tratamentos sanitários.
- **Envio de localização por SMS:** Precisa de partilhar a localização a partir de uma zona sem cobertura? A aplicação resume os dados essenciais num SMS curto transmissível mesmo com sinal fraco 2G.
- **Soberania sobre os dados:** Exporte o histórico do apiário em formato aberto de folha de cálculo (CSV) para o armazenamento interno ou cartão SD em qualquer momento.

---

## Como Utilizar no Terreno

1. **Instalação:** Descarregue o ficheiro `.apk` a partir da secção de [Releases](../../releases) e abra-o no telemóvel.
2. **Carregar o mapa da região:**
   * Descarregue o ficheiro de mapa vetorial (`.map` da sua região ou país).
   * Guarde-o na memória do dispositivo e selecione-o na aplicação — a cartografia funciona sem restrições em Modo de Voo.
3. **Registar colmeias:**
   * Ative o GPS de alta precisão junto à colmeia.
   * Toque para fixar o ponto, introduza o identificador da colmeia e guarde com um só toque.

---

## Retorno de Experiência

Se estiver a utilizar a aplicação no campo e encontrar falhas ou aspetos a melhorar:
- Abra um reporte no separador [Issues](../../issues) mencionando o modelo do equipamento e observações práticas de utilização.

<details>
<summary><b>Especificações Técnicas & Arquitetura (Para Desenvolvedores)</b></summary>

- **Linguagem:** Kotlin (Coroutines, StateFlow)
- **Interface:** Jetpack Compose (Material 3 com elevado contraste para luz solar intensa)
- **Motor Cartográfico:** MapsForge (renderização vetorial offline)
- **Base de dados:** SQLite local através do Android Jetpack Room
- **Conectividade:** Arquitetura pronta para integração de pacotes rádio provenientes de balanças de colmeia e sensores ambientais.
</details>

---
---

# IT - Guida da Campo per Apicoltori

**Registro digitale di apiario locale e mappa topografica offline**, progettato per nomadismo, postazioni remote e zone montane prive di copertura cellulare.

---

## Utilità Pratica in Apiario

RoBee Fields sostituisce il registro cartaceo che si bagna o si deteriora, senza vincolare i tuoi dati al cloud e senza consumare inutilmente la batteria:

- **Mappe complete senza connessione (Offline):** Visualizza la postazione, le piste forestali e l'orografia direttamente a schermo, senza traffico dati.
- **Localizzazione GPS per ogni arnia:** Registra le coordinate esatte di ogni cassa, nucleo o carrello per ritrovarli facilmente di notte o con nebbia.
- **Nessun account né abbonamento:** Si avvia all'istante senza schermate di login. I dati rimangono esclusivamente sullo smartphone, senza tracciamenti in background.
- **Registro rapido degli interventi:** Annota con pochi tocchi la forza della famiglia, la presenza della regina, la posa dei melari e i trattamenti sanitari.
- **Invio posizione via SMS di emergenza:** Ti trovi in una zona senza rete e devi comunicare le coordinate? L'app compatta i dati in un breve SMS che parte anche con una debole copertura 2G.
- **Controllo totale dei dati:** Esporta in qualunque momento lo storico dell'apiario in formato aperto (CSV) sulla memoria interna o su scheda SD.

---

## Come Usarlo in Postazione

1. **Installazione:** Scarica il file di installazione `.apk` dalla sezione [Releases](../../releases) e installalo sullo smartphone.
2. **Caricamento della mappa:**
   * Scarica il file cartografico vettoriale (`.map` della tua regione o nazione).
   * Salvalo sul telefono e selezionalo nelle impostazioni dell'app — la mappa sarà consultabile anche in Modalità Aereo.
3. **Censimento arnie:**
   * Attiva il GPS ad alta precisione in prossimità dell'arnia.
   * Premi sul pulsante di rilevamento posizione, inserisci il numero dell'arnia e salva con un solo tocco.

---

## Feedback dal Campo

Se provi l'applicazione in apiario e riscontri problemi o hai proposte di miglioramento ergonomico:
- Apri una segnalazione nella sezione [Issues](../../issues) specificando il modello di smartphone e le tue note pratiche.

<details>
<summary><b>Stack Tecnico & Architettura (Per Sviluppatori)</b></summary>

- **Linguaggio:** Kotlin (Coroutines, StateFlow)
- **UI:** Jetpack Compose (Material 3 con contrasto ottimizzato per forte luce solare)
- **Motore Cartografico:** MapsForge (rendering vettoriale offline)
- **Database:** SQLite locale gestito tramite Android Jetpack Room
- **Espansione Hardware:** Predisposizione per la ricezione di pacchetti radio da bilance da apiario e sensori microclimatici.
</details>

---
---

# ES - Guía de Campo para Apicultores

**Cuaderno de campo digital local y mapa topográfico sin conexión**, diseñado para apicultura trashumante, colmenares aislados y zonas de montaña sin cobertura móvil.

---

## Utilidad Práctica en el Colmenar

RoBee Fields sustituye la libreta de papel tradicional sin atar tus datos a cuentas en la nube ni drenar la batería de tu dispositivo:

- **Cartografía completa sin cobertura (Offline):** Consulta el asentamiento, pistas forestales y el relieve directamente en pantalla sin gastar datos móviles.
- **Posicionamiento GPS por colmena:** Registra las coordenadas precisas de cada colmena, núcleo o remolque para localizarlos con facilidad de noche o con niebla.
- **Sin cuentas ni suscripciones:** Inicia de inmediato sin muros de registro. La información reside exclusivamente en tu teléfono, sin analíticas en segundo plano.
- **Registro ágil de inspecciones:** Apunta en segundos el vigor de la colonia, estado de la reina, alzas añadidas y tratamientos sanitarios.
- **Envío de ubicación por SMS:** ¿Te encuentras en una zona blanca y requieres enviar coordenadas o un reporte? La aplicación empaqueta los datos en un SMS corto capaz de transmitirse con señal 2G débil.
- **Soberanía de datos:** Exporta todo el historial del colmenar en formatos tabulares abiertos (CSV) a la memoria interna o tarjeta SD cuando quieras.

---

## Modo de Uso en el Asentamiento

1. **Instalación:** Descarga el instalador `.apk` desde el apartado de [Releases](../../releases) y ejecútalo en tu terminal.
2. **Carga de mapas:**
   * Descarga el archivo de mapa vectorial (`.map` para tu país o región).
   * Guárdalo en la memoria del teléfono y selecciónalo en la app — el mapa funcionará sin problemas incluso en Modo Avión.
3. **Registro de colmenas:**
   * Activa el GPS de alta precisión junto a la colmena.
   * Pulsa marcar posición, introduce el número de identificación y guarda con un solo toque.

---

## Sugerencias e Incidencias

Si pruebas la aplicación durante el trabajo de campo y detectas problemas o mejoras funcionales:
- Abre una incidencia en la pestaña [Issues](../../issues) indicando el modelo del móvil y las observaciones recogidas en el colmenar.

<details>
<summary><b>Detalles Técnicos y Arquitectura (Para Desarrolladores)</b></summary>

- **Lenguaje:** Kotlin (Coroutines, StateFlow)
- **Interfaz:** Jetpack Compose (Material 3 con contraste elevado para luz solar directa)
- **Motor Cartográfico:** MapsForge (renderizado vectorial offline)
- **Base de datos:** SQLite local mediante Android Jetpack Room
- **Integración de Sensores:** Arquitectura preparada para decodificar paquetes de radio provenientes de básculas de colmena y nodos de microclima.
</details>
