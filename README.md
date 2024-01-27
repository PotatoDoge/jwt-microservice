
# JWT Generator

Microservice that is in charge of generating jwts for valid users, as well as registering new users.

Keypoints:
- Authorization
- Authentication
- New User Registration


## Run Locally

What you will need:
- Java 17
- PostgreSQL database running (username, password and connection url are set in the application.yml file)

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


## Docker

If you want to run this app in a docker container (assuming that Docker is running in your machine), follow these steps:

- Set the working directory to the project's root directory
- Run the following commands:

      docker-compose build --no-cache
      docker-compose up 

- This command will create containers for both the database and the jwt microservice
- 8080 and 5432 (or your own specified ports) must be free in order for this to run correctly
## Authors

- [@PotatoDoge](https://github.com/PotatoDoge)


## Notes
This microservice is based on the following template: https://github.com/PotatoDoge/Springboot-Security-Template

## Tags
- Springboot 3
- Springboot Security 6
- Java 17
- Json Web Token (JWT)