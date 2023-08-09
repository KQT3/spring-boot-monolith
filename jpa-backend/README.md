# Setup

```
docker-compose up -d
docker exec -it postgres-monolith psql -U admin -d postgres

docker exec -it postgres-monolith psql -U admin -d postgres -c "CREATE DATABASE \"jpa-backend-psql\";"
```
