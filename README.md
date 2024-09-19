

![Beispielbild](https://github.com/SI-Classroom-Batch-017/android-abschluss-LarsNicodemus/blob/main/app/src/main/res/drawable/eventually_round1.png) 
## EvenTually

EvenTually ist eine Android-App, die dir hilft, dich an den Jahrestag mit deinem Partner zu erinnern. Die App bietet personalisierte Vorschläge für Karten, Geschenke und Aktivitäten, sodass du sicher sein kannst, dass der Tag perfekt geplant ist und du eine tolle Zeit mit deinem Liebsten verbringen kannst.

## Funktionen

- **Profil-Erstellung:** Lege ein Profil für dich, deinen Partner und den Jahrestag an. 
  - Erfasse Details wie Alter, Geschlecht, Beziehungsbeginn, Hobbies, Likes, Vorlieben beim Essen und bei Aktivitäten.

- **Erinnerung an den Jahrestag:** Die App erinnert dich rechtzeitig an den bevorstehenden Jahrestag.
  - Du kannst einstellen wann du benachrichtigt werden willst, zur Auswahl stehen 7,5,3,1 Tage vorher, zum Jahrestag und auch ein monatlicher Reminder.
  - Zusätzlich kannst du die Zeit festlegen zu der du benachrichtigt werden willst.

- **Personalisierte Vorschläge:** 
  - Karten zum Jahrestag sowie Geschenkideen werden über die eBay API geladen und bereitgestellt.
  - Aktivitäts- und Restaurantvorschläge werden unter Berücksichtigung der Vorlieben über die Google Places API geladen und bereitgestellt.
  - Wetterdaten bis zu 15 Tag vor dem Jahrestag sichtbar via Open Meteo API, um Aktivitäten besser planen zu können.

## Architektur

Die App verwendet das MVVM (Model-View-ViewModel) Architekturpattern mit einem Repository für die Datenverwaltung. Folgende Technologien und Patterns kommen zum Einsatz:

- **Navigation:** NavGraph-basierte Navigation
- **RecyclerView:** Mindestens eine RecyclerView wird genutzt, um Listen darzustellen
- **LiveData:** Zur Beobachtung und Reaktion auf Datenänderungen
- **Room Datenbanken:**
  - Random Facts über Liebe
  **Firebase Firestore**
  - Nutzerprofil
  - Partnerprofil
  - Eventprofil
  - Reminder
  - etc.
    
- **API Calls:**
  - Wetterdaten über (Open Meteo API)
  - Ortsbestimmung über (Google Geocoding API)
  - Restaurants und Aktivitäten (Google Places API)
  - Produktvorschläge wie Karten und Geschenke mit der (eBay Buyer API)

## Mindestanforderungen

Das Projekt erfüllt die folgenden Mindestanforderungen:

- Tägliches Pushen auf GitHub (ab Woche 2)
- Mindestens 5 funktionsfähige Screens
- Navigation via NavGraph
- Mindestens 1 RecyclerView
- LiveData mit MVVM und Repository Pattern
- Mindestens 1 API Call
- Nutzung von Room Datenbanken

## Installation und Nutzung

1. **Klonen des Repositories:**
   ```bash
   https://github.com/LarsNicodemus/EvenTually.git

## Beiträge

Beiträge zu diesem Projekt sind herzlich willkommen! Wenn du Fehler findest, neue Funktionen vorschlagen oder zum Code beitragen möchtest, folge diesen Schritten:

1. **Fork dieses Repositories:** Erstelle einen Fork des Projekts.
2. **Branch erstellen:** Erstelle einen neuen Branch für deine Änderungen:
   ```bash
   git checkout -b feature/dein-feature
   git commit -m "Beschreibung deiner Änderungen"
   git push origin feature/dein-feature
## Kontakt

Wenn du Fragen, Feedback oder Vorschläge hast, kannst du mich über die folgenden Wege erreichen:

- **GitHub:**       [LarsNicodemus](https://github.com/larsnicodemus) – Schau dir meine anderen Projekte an oder erstelle ein Issue, um Fehler zu melden oder Features vorzuschlagen.
- **Email:**        [lars@no-19.net](mailto:lars@no-19.net) – Für direkte Anfragen oder detailliertes Feedback.
- **LinkedIn:**     [Lars Nicodemus](https://www.linkedin.com/in/larsnicodemus/) – Verbinde dich mit mir und lass uns in Kontakt bleiben.

Ich freue mich auf deine Nachricht und versuche, so schnell wie möglich zu antworten.

# EvenTually






![Beispielbild](https://github.com/SI-Classroom-Batch-017/android-abschluss-LarsNicodemus/blob/main/app/src/main/res/drawable/eventually_round1.png) 


EvenTually is an Android app that helps you remember your anniversary with your partner. The app offers personalized suggestions for cards, gifts, and activities, ensuring that the day is perfectly planned and you can spend a great time with your loved one.


## Features

- **Profile Creation:** Create a profile for yourself, your partner, and the anniversary.
  - Capture details such as age, gender, relationship start date, hobbies, likes, food preferences, and activity preferences.
- **Anniversary Reminder:** The app reminds you of the upcoming anniversary in a timely manner.
  - You can set when you want to be notified, with options for 7, 5, 3, 1 days before, on the anniversary, and also a monthly reminder.
  - Additionally, you can set the time at which you want to be notified.
- **Personalized Suggestions:**
  - Anniversary cards and gift ideas are loaded and provided through the eBay API.
  - Activity and restaurant suggestions are loaded and provided through the Google Places API, taking into account preferences.
  - Weather data visible up to 15 days before the anniversary via Open Meteo API

## Architecture

The app uses the MVVM (Model-View-ViewModel) architecture pattern with a repository for data management. The following technologies and patterns are used:

- **Navigation:** NavGraph-based navigation
- **RecyclerView:** At least one RecyclerView is used to display lists
- **LiveData:** For observing and reacting to data changes
- **Room Databases:**
  - Random facts about love
- **Firebase Firestore:**
  - User profile
  - Partner profile
  - Event profile
  - Reminders
  - etc.
- **API Calls:**
  - Weather data via (Open Meteo API)
  - Location determination via (Google Geocoding API)
  - Restaurants and activities (Google Places API)
  - Product suggestions such as cards and gifts with the (eBay Buyer API)
    
## Minimum Requirements

The project meets the following minimum requirements:

- Daily pushes to GitHub (from week 2)
- At least 5 functional screens
- Navigation via NavGraph
- At least 1 RecyclerView
- LiveData with MVVM and Repository Pattern
- At least 1 API call
- Use of Room databases

## Installation and Usage

1. **Clone the repository:**
   ```bash
   https://github.com/LarsNicodemus/EvenTually.git

## Contributions

Contributions to this project are welcome! If you find bugs, want to suggest new features, or contribute to the code, follow these steps:

1. **Fork this repository:** Create a fork of the project.
2. **Create a branch:** Create a new branch for your changes:
   ```bash
   git checkout -b feature/dein-feature
   git commit -m "Beschreibung deiner Änderungen"
   git push origin feature/dein-feature
   
## Contact

If you have questions, feedback, or suggestions, you can reach me through the following channels:

- **GitHub:**       [LarsNicodemus](https://github.com/larsnicodemus) – Check out my other projects or create an issue to report bugs or suggest features.
- **Email:**        [lars@no-19.net](mailto:lars@no-19.net) – For direct inquiries or detailed feedback.
- **LinkedIn:**     [Lars Nicodemus](https://www.linkedin.com/in/larsnicodemus/) – Connect with me and let's stay in touch.

I look forward to your message and will try to respond as quickly as possible.

# EvenTually
