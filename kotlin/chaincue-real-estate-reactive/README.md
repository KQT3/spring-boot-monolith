https://www.youtube.com/watch?v=2MYSLP2vgps

https://github.com/realdavidvega/kotlin-playground/blob/main/kotlin/code/_03_spring_data_r2dbc/src/main/kotlin/com/example/r2dbc/users/UserController.kt


# Setup

```
docker-compose up -d
docker exec -it postgres-monolith psql -U admin -d postgres

docker exec -it postgres-monolith psql -U admin -d postgres -c "CREATE DATABASE \"chaincue-real-estate-db\";"
```

- Create file `resources/application-secret.properties`

```
spring.security.oauth2.client.registration.keycloak.client-secret=
```
