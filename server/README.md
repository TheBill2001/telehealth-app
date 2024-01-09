# TeleHealth Server

This is a mock server for the TeleHealth App.

## Setup

The server is written with TypeScript and run on NodeJS. The database uses MongoDB.

1. Install the dependencies.
    ```bash
    npm install
    ```
2. Setting up the [environment variable](#environtment-variable). The most crucial variable that need to be set are `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`
3. Run the server.
    ```bash
    npm start
    ```

## Environtment variable

-   `PORT`: Server port, default to `3000`.
-   `DB_URL`: URL to connect to mongodb server.
-   `DB_USERNAME`: Database username.
-   `DB_PASSWORD`: Database password.
-   `ADMIN_USERNAME`: Server default Admin username, default to `admin`.
-   `ADMIN_PASSWORD`: Server default Admin password, default to `123456`.
-   `JWT_SECRET`: JSON Web Token secret to authenticate user, default to `123456`.

## API documentation

The API tries it best to be RESTful. All data are in JSON format.

### Authentication

All authentication method start at `/api/auth`. The authentication uses Json Web Token (JWT) which will be set as cookie.

#### Login - POST `/api/auth/login`

-   Request:

    ```json
    {
        "username": "<login>",
        "password": "<password>"
    }
    ```

-   Response:
    -   200 - Login successful, a JWT token is set in cookie.
    -   401 - Wrong credentials.

#### Register - POST `/api/auth/register`

-   Request, data will be validated:

    -   `phone` can only be Vietnamese.
    -   `email` will be validated.
    -   `citizenID` is a string of 12 digits.

    ```json
    {
        "username": "<username>",
        "password": "<password>",
        "userInfo": {
            "phone": "<phone>",
            "email": "<email>",
            "name": "<name>",
            "dateOfBirth": "<Date>",
            "citizenID": "<id>"
        }
    }
    ```

-   Response:
    -   200 - Register successful, a JWT token is set in cookie.
    -   409 - Username existed.
    -   422 - Incorrect data.

#### Ping - GET `/api/auth/ping`

Check if token is valid. Send token via cookie header.

-   Response:
    -   200 - Valid
    -   401 - Not valid

### Profile - GET `/api/profile`

Get user profile base on token. The response is a JSON of the form:

```
{
    id: string,
    name: string,
    phone: string,
    email: string,
    dateOfBirth: date,
    citizenID: string
}
```

-   Response:
    -   200 - Valid
    -   401 - Token invalid
    -   404 - Missing profile

### Symptom

Require user token in cookie.

#### Get user's symptom - GET `/api/symptom`

Get user's symptoms from token:

-   Request:

    -   Query:
        -   `from`: Filter from date.
        -   `to`: Filter to date.
        -   `desc`: Descending order.

-   Response:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "userId": "<userId>",
        "symptoms": [
            {
                "_id": "<string>",
                "description": "<string>",
                "severity": "<number>",
                "note": "<string>",
                "createdAt": "<Date>",
                "updatedAt": "<Date>"
            }
        ]
    }
    ```

#### Add user symptom - POST `/api/symptom`

-   Request:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "description": "<string>",
        "severity": "<number>",
        "note": "<string>"
    }
    ```

-   Response:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "_id": "<string>",
        "description": "<string>",
        "severity": "<number>",
        "note": "<string>",
        "createdAt": "<Date>",
        "updatedAt": "<Date>"
    }
    ```

#### Get a user symptom for ID - GET `/api/symptom/:id`

-   Response:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "_id": "<string>",
        "description": "<string>",
        "severity": "<number>",
        "note": "<string>",
        "createdAt": "<Date>",
        "updatedAt": "<Date>"
    }
    ```

#### Update user symptom - PUT `/api/symptom/:id`

-   Request:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "description": "<string>",
        "severity": "<number>",
        "note": "<string>"
    }
    ```

-   Response:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "_id": "<string>",
        "description": "<string>",
        "severity": "<number>",
        "note": "<string>",
        "createdAt": "<Date>",
        "updatedAt": "<Date>"
    }
    ```

#### Delete user symptom - DELETE `/api/symptom/:id`

### COVID Test `/api/covidTest`

Require user token in cookie.

#### Get user's covid test results - GET `/api/covidTest`

-   Request:

    -   Query:
        -   `from`: Filter from date.
        -   `to`: Filter to date.
        -   `desc`: Descending order.

-   Response:

    -   `testingFacility` can be empty, denote the testing facility.
    -   `type`: 0 - self report, 1 - from a testing facility

    ```json
    {
        "userId": "<userId>",
        "symptoms": [
            {
                "_id": "<string>",
                "positive": "<boolean>",
                "testingFacility": "<string>",
                "type": "<integer>",
                "createdAt": "<Date>",
                "updatedAt": "<Date>"
            }
        ]
    }
    ```

#### Add user's covid test result - POST `/api/covidTest`

-   Request:

    -   This API end point is for user only. Any test result added through this end point is automatically marked at self report.

    ```json
    {
        "positive": "<boolean>"
    }
    ```

-   Response:
    -   `testingFacility` can be empty, denote the testing facility.
    -   `type`: 0 - self report, 1 - from a testing facility
    ```json
    {
        "_id": "<string>",
        "positive": "<boolean>",
        "testingFacility": "<string>",
        "type": "<integer>",
        "createdAt": "<Date>",
        "updatedAt": "<Date>"
    }
    ```

#### Get user's covid test result by ID - GET `/api/covidTest/:id`

-   Response:
    -   `testingFacility` can be empty, denote the testing facility.
    -   `type`: 0 - self report, 1 - from a testing facility
    ```json
    {
        "_id": "<string>",
        "positive": "<boolean>",
        "testingFacility": "<string>",
        "type": "<integer>",
        "createdAt": "<Date>",
        "updatedAt": "<Date>"
    }
    ```

#### Update user's covid test result by ID - PUT `/api/covidTest/:id`

-   Request:

    -   This API end point is for user only. Only self report can be update.

    ```json
    {
        "positive": "<boolean>"
    }
    ```

-   Response:
    -   `testingFacility` can be empty, denote the testing facility.
    -   `type`: 0 - self report, 1 - from a testing facility
    ```json
    {
        "_id": "<string>",
        "positive": "<boolean>",
        "testingFacility": "<string>",
        "type": "<integer>",
        "createdAt": "<Date>",
        "updatedAt": "<Date>"
    }
    ```

#### Delete user's covid test result by ID - DELETE `/api/covidTest/:id`

Can only delete self report entries.

### Vaccine `/api/vacine`

Require user token in cookie.

#### Get user's vaccination history - GET `/api/vacine/history`

-   Request:

    -   Query:
        -   `from`: Filter from date.
        -   `to`: Filter to date.
        -   `desc`: Descending order.

-   Response:
    ```json
    {
        "userId": "<string>",
        "history": [{
            "_id": "<string>",
            "name": "<string>",
            "type": "Booster" | "Vaccine",
            "facility": "<string>",
            "date": "<date>",
        }]
    }
    ```

#### Get user's vaccination registration - GET `/api/vacine/registration`

-   Request:

    -   Query:
        -   `from`: Filter from date.
        -   `to`: Filter to date.
        -   `desc`: Descending order.

-   Response:

    ```json
    {
        "userId": "<string>",
        "registration": [{
            "name": "<string>",
            "type": "Booster" | "Vaccine",
            "status": "Pending" | "Canceled" | "Accepted" | "Finished",
            "facility": "<string>",
            "date": "<date>",
            "createdAt": "<date>",
            "updatedAt": "<date>",
        }]
    }
    ```

#### Register vaccination - POST `/api/vacine/registration`

-   Request:

    ```json
    {
        "name": "<string>",
        "type": "Booster" | "Vaccine",
    }
    ```

-   Response:
    ```json
    {
        "name": "<string>",
        "type": "Booster" | "Vaccine",
        "status": "Pending" | "Canceled" | "Accepted" | "Finished",
        "facility": "<string>",
        "date": "<date>",
        "createdAt": "<date>",
        "updatedAt": "<date>",
    }
    ```

#### Get vaccination registration info - GET `/api/vacine/registration/:id`

-   Response:
    ```json
    {
        "name": "<string>",
        "type": "Booster" | "Vaccine",
        "status": "Pending" | "Canceled" | "Accepted" | "Finished",
        "facility": "<string>",
        "date": "<date>",
        "createdAt": "<date>",
        "updatedAt": "<date>",
    }
    ```

#### Cancel vaccination registration - PUT `/api/vacine/registration/:id/cancel`

-   Response:
    ```json
    {
        "name": "<string>",
        "type": "Booster" | "Vaccine",
        "status": "Pending" | "Canceled" | "Accepted" | "Finished",
        "facility": "<string>",
        "date": "<date>",
        "createdAt": "<date>",
        "updatedAt": "<date>",
    }
    ```
