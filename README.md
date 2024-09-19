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
   https://github.com/SI-Classroom-Batch-017/android-abschluss-LarsNicodemus.git

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
# EvenTually
