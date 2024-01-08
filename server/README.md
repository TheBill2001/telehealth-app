# TeleHealth Server

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

### Symptom - GET `/api/symptom`

Get user's symptoms from token:

-   Response:

    -   `note` is optional.
    -   `severity` is range from 1 to 10.

    ```json
    {
        "user": "<userId>",
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
