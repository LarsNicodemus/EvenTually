

# EvenTually






![Beispielbild](https://github.com/SI-Classroom-Batch-017/android-abschluss-LarsNicodemus/blob/main/app/src/main/res/drawable/eventually_round1.png) 




https://github.com/user-attachments/assets/e1953478-e13a-4bcc-aaa5-fc520a3404fd




### EvenTually is an Android app that helps you remember your anniversary with your partner. The app offers personalized suggestions for cards, gifts, and activities, ensuring that the day is perfectly planned and you can spend a great time with your loved one.


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
