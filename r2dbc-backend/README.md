# Setup

```
docker-compose up -d
docker exec -it postgres-teacher psql -U admin -d postgres

docker exec -it postgres-teacher psql -U admin -d postgres -c "CREATE DATABASE \"r2dbc-backend-pqsl\";"
```
