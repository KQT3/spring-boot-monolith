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
