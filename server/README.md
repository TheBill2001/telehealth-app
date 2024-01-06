# TeleHealth Server

## API documentation

The API tries it best to be RESTful. All data are in JSON format.

### Authentication

All authentication method start at `/api/auth`. The authentication uses Json Web Token (JWT).

#### Login `/api/auth/login`

-   Request - POST:

```
{username: <login>, password: <password>}
```

-   Response:
    -   200 - Login successful, a JWT token is set in cookie.
    -   401 - Wrong credentials.

#### Register `/api/auth/register`

-   Request - POST:

```
{
    username: <username>
    password: <password>
    userInfo: {
        phone: <phone>
        email: <email>
        name: <name>
        dateOfBirth: <YYYY-mm-dd>
        citizenID: <id>
    }
}
```

-   Response:
    -   200 - Register successful, a JWT token is set in cookie.
    -   409 - Username existed.
    -   Response 422 : Incorrect data

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
