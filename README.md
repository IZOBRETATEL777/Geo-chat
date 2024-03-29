# <img alt="logo.png" height="60" src="docs%2Flogo.png" width="60"/> Geo Chat Mobile Messenger
---
<p align="center">
  <img alt="workflow_1.gif" src="docs%2Fworkflow_1.gif"/>
  <br>
  <i>Connecting despite the distances. Collaborating by common interests.</i>
</p>

---
## Description

Geo Chat is a mobile application with messenger functionalities that connect people of the same geographic area. People of the same city, village or even a street can become friends and chat or just share important information.

Main features:

- Chatting using simple text messages
- Chatrooms are geographical points such as cities or even supermarkets
- Anonymity – you will disclose to others only your nickname. You can easily change it as well!

Some interesting features:

- Check your network connectivity from settings. The app will notify you about network connection changes.
- Do not burn your phone! Check the temperature of the device in the settings window.

## Technology Stack

- Google Android SDK 29
- Java 17 (Eclipse Temurin)
- Kotlin DSL + Gradle 8.0
- Google Firebase
- Google Maps/Places API

## Security Aspects

The application is written according to some best security practices in terms of coding and user interaction:

- General Data Protection Regulation (EU GDPR) policies are enforced in data storage: database server located in EU zone (Belgium): [https://europa.eu/youreurope/business/dealing-with-customers/data-protection/data-protection-gdpr/index\_en.htm](https://europa.eu/youreurope/business/dealing-with-customers/data-protection/data-protection-gdpr/index_en.htm)
- Data encryption is implemented, so that user data is encrypted on the database server.
- An independent Authentication provider is used (Firebase Auth). Data is stored in Android Keychain.
- API-keys storage using gradle-secrets plugin: [https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin](https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin)
- Manual Application Security Testing is performed by MobSF testing framework.

Evaluation criteria characteristics

Main

Activities:

- All activities are interconnected and relevant for application needs.
- 4 activities. See [activity](https://github.com/IZOBRETATEL777/Geo-chat/tree/master/app/src/main/java/az/edu/bhos/mychatapp/activity).
- CurrentLocationActivity sends "selected\_city" value to the MainActivity
- SettingsActivity is working with Shared Preferences and send "location" to the MainAcitvity

Sensors:

- 3 sensors are used: Location (android.location), Network (Broadcast receiver) and Battery Temperature (via Intent.ACTION\_BATTERY\_CHANGED)
- Requested permissions and features:
    - permission.INTERNET
    - permission.ACCESS\_NETWORK\_STATE
    - permission.ACCESS\_FINE\_LOCATION
    - hardware.sensor.accelerometer
    - hardware.sensor.compass
- Smooth and understandable UI/UX from the perspective of application security with the experience in back-end developer 😊

Extra

- Integration of NoSQL Database
- Working with Shared Preferences
- Working with Settings fragments, Toolbar
- Integration of Google Services
- Focus on security and data protection

## Screenshots

<img alt="Screenshot_1.png" height="420" src="docs%2FScreenshot_1.png" width="240"/>
<img alt="Screenshot_2.png" height="420" src="docs%2FScreenshot_2.png" width="240"/>
<img alt="Screenshot_3.png" height="420" src="docs%2FScreenshot_3.png" width="240"/>
<img alt="Screenshot_4.png" height="420" src="docs%2FScreenshot_4.png" width="240"/>
<img alt="Screenshot_5.png" height="420" src="docs%2FScreenshot_5.png" width="240"/>

## Links

- Chatting functionalities: [https://youtu.be/qCjIns0-Xpk](https://youtu.be/qCjIns0-Xpk)
- Geo Chatrooms: [https://youtu.be/OLs-7tqiyaA](https://youtu.be/OLs-7tqiyaA)