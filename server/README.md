# TeleHealth Server

## API documentation

The API tries it best to be RESTful. All data are in JSON format.

### Authentication

All authentication method start at `/api/auth`. The authentication uses Json Web Token (JWT).

#### Login `/api/auth/login`

-   Request:

```
{username: <login>, password: <password>}
```

-   Response:
    -   200 - Login successful, a JWT token is set in cookie.
    -   401 - Wrong credentials.

#### Register `/api/auth/register`

-   Request:

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
    -   200 - Login successful, a JWT token is set in cookie.
    -   409 - Username existed.

#### Ping `/api/auth/ping`

Check if token is valid. Send token via cookie header.

-   Response:
    -   200 - Valid
    -   401 - Not valid
