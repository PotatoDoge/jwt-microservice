
## Docker

If you want to run this app in a docker container (assuming that Docker is running in your machine), follow these steps:

- Set the working directory to the project's root directory
- Run this command:

      docker-compose up 

- This command will create containers for both the database and the jwt microservice
- 8080 and 5432 (or your own specified ports) must be free in order for this to run correctly