
# JWT Generator

Microservice that is in charge of generating jwts for valid users, as well as registering new users.

Keypoints:
- Authorization
- Authentication
- New User Registration


## Run Locally

What you will need:
- Docker (active)

Clone the project

```bash
  git clone https://github.com/PotatoDoge/jwt-microservice.git
```

Go to the project directory

```bash
  cd jwt-microservice
```

Run spring boot application

```bash
  ./mvnw spring-boot:run
```

Run the following commands:
```bash
    docker volume create auth-ms-volume
    docker-compose build --no-cache
    docker-compose up -d
```
- This command will create containers for both the database and the jwt microservice
- 8080 and 5432 (or your own specified ports) must be free in order for this to run correctly

## Usage/Examples

*__Registration endpoint__*

*POST* → http://localhost:8080/api/v1/auth/register

*Body*:

    {
        "email":"test@mail.com",
        "password":"12345",
        "role":"ADMIN"
    }

- No auth

*This request will register the email, password and role in the database*

*__Authentication endpoint__*

*POST* → http://localhost:8080/api/v1/auth/authenticate

*Body*:

    {
        "email":"test@mail.com",
        "password":"12345",
    }
- No auth

*This request will check if the user exists in the database, and if it exists, it will return a 200 status response code and a JWT as a header*

*__Test secure endpoint__*

*GET* → http://localhost:8080/demo

- Bearer Token (token returned from auth endpoint)

*This request will return a message like "Hello world from secure endpoint"*

## Authors

- [@PotatoDoge](https://github.com/PotatoDoge)


## Notes
This microservice is based on the following template: https://github.com/PotatoDoge/Springboot-Security-Template

## Tags
- Springboot 3
- Springboot Security 6
- Java
- Json Web Token (JWT)
- Docker