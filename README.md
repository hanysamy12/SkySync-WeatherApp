# SkySync - Weather Forecast App
**SkySync** is a beautiful and functional weather app built with *Jetpack Compose* and *kotlin* for Android. 
It provides accurate real-time weather information and future forecasts.
The app includes localization, data persistence, offline support, and allows users to save their favorite locations for quick access.

## 🚀Features
- **Current Weather**: Real-time weather updates based on your location or a saved favorite.
- **5-Day Forecast**: Extended forecast with temperature, wind speed, and condition visuals.
- **Favorites** ⭐: Save multiple locations as favorites and quickly view their weather.
- **Language Support** 🌐: Switch between Arabic 🇪🇬  and English 🇬🇧.
- **Units Preference**: Toggle temperature units (°C/°F/°K) and wind units (m/s or mph).
- **Weather Alerts**: Set custom alerts using WorkManager for selected time.
- **Network Awareness**: Auto-detect network changes and show connection status.
- **Settings Screen**: Manage preferences stored via DataStore.
- **Offline Friendly**: Graceful behavior when disconnected from the internet.
- **Modern UI**: Clean and smooth UI using Jetpack Compose.


## ⚙️ Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Asynchronous Programming**: Kotlin Coroutines
- **State Management**: Kotlin Flows,
- **Local Storage**: DataStore (Preferences) , Room DataBase
- **Background Work**: WorkManager for weather alert scheduling
- **Location**: Fused Location Provider
- **Networking**: Retrofit + Gson
- **Permissions Handling**: Compose-friendly permission management

## 🔒 Permissions
- **INTERNET** – To fetch weather data
- **ACCESS_FINE_LOCATION** – For location-based weather
- **POST_NOTIFICATIONS** – To show alerts

  ## 🧠 Author

**SkySync** developed by ***Hany Samy***.
  
