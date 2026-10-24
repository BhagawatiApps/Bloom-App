# Bloom App

Bloom is an Android companion for building healthy daily habits related to PCOS management. The app helps users track routines such as cycle care, meals, movement, water, relaxation, and sleep from one simple dashboard.

> Bloom is for habit tracking and educational support. It is not a substitute for professional medical advice, diagnosis, or treatment.

## Features

- Guided four-step onboarding
- Personalized daily habit selection
- Daily progress tracking and completion status
- Habit categories for meals, movement, water, relaxation, sleep, and cycle care
- Daily tips and reminders
- PCOS-focused FAQ carousel and learning content
- Dashboard, Insights, Learn, and Settings sections
- Local habit persistence with Room
- Splash screen and edge-to-edge Material UI
- Google Mobile Ads integration using Google's official test ad IDs

## Tech stack

- Java
- Android SDK 37
- Minimum Android version: Android 7.0 (API 24)
- Gradle with Android Gradle Plugin
- AndroidX AppCompat, Activity, Navigation, and ConstraintLayout
- Material Components
- Room
- Glide
- ViewPager2
- Google Mobile Ads SDK

## Requirements

- Android Studio with Android SDK 37 installed
- JDK 11
- An Android emulator or a physical Android device running API 24 or newer
- Internet access for the first dependency download and for loading ads

## Getting started

1. Clone the repository:

   ```bash
   git clone https://github.com/BhagawatiApps/Bloom-App.git
   cd Bloom-App
   ```

2. Open the project in Android Studio.

3. Allow Gradle to sync and install any requested Android SDK components.

4. Run the `app` configuration on an emulator or connected device.

The application ID is `com.example.bloomapp`.

## Build from the command line

Build a debug APK:

```bash
./gradlew assembleDebug
```

Install the debug build on a connected device:

```bash
./gradlew installDebug
```

Run unit tests:

```bash
./gradlew test
```

Run instrumentation tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

## Project structure

```text
.
├── app/
│   └── src/main/
│       ├── java/com/example/bloomapp/
│       │   ├── Adapter/       RecyclerView and ViewPager adapters
│       │   ├── Data/          Room database, DAOs, and entities
│       │   ├── Fragment/      Main navigation destinations
│       │   ├── OnBoarding/    Onboarding flow and habit selection
│       │   ├── Screens/       Splash screen
│       │   ├── Utils/          Preferences and habit helpers
│       │   └── ads/            Ad configuration and loading helpers
│       └── res/                Layouts, drawables, navigation, and values
├── gradle/
├── build.gradle
└── settings.gradle
```

## Local data

Habit and daily progress data are stored locally in a Room database named `bloom_habit_db`. First-launch and onboarding preferences are stored with Android shared preferences.

## AdMob configuration

The repository currently uses Google's official test IDs in `app/src/main/res/values/ads.xml`. Replace these values with your own AdMob application and ad-unit IDs before distributing a production build, and keep production credentials out of source control where appropriate.

## License

No license has been added to this repository yet. Until a license is provided, assume that all rights are reserved.
