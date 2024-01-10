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

### Login.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/17fb1c5d-126f-4645-81b8-60bdaae9bdb3" width=400>

### Register.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/a044c932-0025-48c2-898d-a0397288fee1" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/80199234-95b7-40a6-b750-2113e9d1e3fc" width=400>

### Home screen.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/2835633b-9fa0-47c4-aded-b151d6540591" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/01cb6f39-a9c1-4711-8c5e-d4dc93fca9f7" width=400>

### Symptom tracking.

The app allow the user to track their own symptom, with a severity rating ranging from 1 (mild) to 10 (extreme).

- Symptom listing.
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/467d6cc3-3d45-479e-99b2-fa4303ec7539" width=400>

- Add new symptom description. The date and time will automatically set.
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/a3bd75cf-da42-4479-9526-972d0cf07159" width=400>

- Edit an symptom entry.
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/befe3c3b-6f2b-46ac-a1c1-cb581d4efdb4" width=400>

### COVID test result.

The user can view their COVID test results from a testing facility and submit their own self-report test result. The user cannot modified submitted result.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/45079e1c-6617-46a8-ba4c-3dd68a231701" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/8d67d2d5-14d7-45bf-926a-d063110f11f0" width=400>

## Vaccination history.

The user can view their vaccination history. Vaccination history has two types: (normal) vaccine and booster.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/ca99628d-4bb8-4cc2-bd44-635de96752b8" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/c824bbe6-8609-4b2b-9148-684e2d8f1f05" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/b28298d2-39de-4f51-8271-46868d084f2e" width=400>

## Vaccination registration.

The user can register with a facility for vaccination (đăng kí tiêm). Registration has 4 status: Pending, Canceled, Accepted, Finished.

<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/14ebe86a-5352-4089-b0e3-c2a683bffbd0" width=400>
<img src="https://github.com/TheBill2001/telehealth-app/assets/68230358/8bcf01bd-b74b-401a-973d-e14f43cb2409" width=400>
