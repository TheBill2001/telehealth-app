# TeleHealth App

This is the application part of the project.

## Setup

The app need a [server](../server/README.md) to operate. Set the server URL as follow:

- Create a file name `local.properties`.
- Add the server URL to the file:
    ```
    API_URL=<URL>
    ```
- The URL need to end with a forward slash `/`, ie: `API_URL="http://192.168.100.20:3000/api/"`.
- DO NOT use `localhost` :D as the app doesn't run on the same machine as the server, INCLUDING when running in the emulator.
- Build and run from Android Studio.

## Implementation.

The app is written in Kotlin. The UI is contruct using Jetpack Compose with Material 3 components. The client uses Retrofit 2 for API communication. Kotlinx is used for JSON serialization.

## Functionalities

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/df7d0a64-2c44-4109-9ddb-513d2919e1ad" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/01cb6f39-a9c1-4711-8c5e-d4dc93fca9f7" width=400>

### Symptom tracking.

The app allow the user to track their own symptom, with a severity rating ranging from 1 (mild) to 10 (extreme).

- Symptom listing.
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/467d6cc3-3d45-479e-99b2-fa4303ec7539" width=400>

- Add new symptom description. The date and time will automatically set.
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/831e55a7-5e7b-4dc5-a5ca-c1b272067466" width=400>

- Edit an symptom entry.
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/38eccc49-7e87-48dd-8084-b83d1dbde1d3" width=400>

### COVID test result.

The user can view their COVID test results from a testing facility and submit their own self-report test result. The user cannot modified submitted result.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/45079e1c-6617-46a8-ba4c-3dd68a231701" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/8d67d2d5-14d7-45bf-926a-d063110f11f0" width=400>

## Vaccination history.

The user can view their vaccination history. Vaccination history has two types: (normal) vaccine and booster.

_Server ready. Not finish UI, yet_

## Vaccination registration.

The user can register with a facility for vaccination (đăng kí tiêm). Registration has 4 status: Pending, Canceled, Accepted, Finished.

_Server ready. Not finish UI, yet_

## Remote medical guidance.

**CANCLED - NOT IMPLEMENTED - TECHNICAL DIFICULTIES - NOT ENOUGH KNOWLEDGE**

The user can use text and video chat for remote medical advice/guidance.
