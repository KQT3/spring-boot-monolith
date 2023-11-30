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

./gradlew bootJar

./gradlew nativeImage

docker run -it -v ${PWD}:/workdir --privileged -w /workdir ubuntu bash

apt-get update
apt-get install -y build-essential libz-dev zlib1g-dev
apt-get update
apt-get install -y curl

GRAALVM_VERSION=21.3.0
GRAALVM_TAR=graalvm-ce-java11-linux-amd64-$GRAALVM_VERSION.tar.gz
GRAALVM_URL=https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-$GRAALVM_VERSION/$GRAALVM_TAR

# Download GraalVM
curl -LJO $GRAALVM_URL

# Extract GraalVM
tar -xvf $GRAALVM_TAR

# Move GraalVM to /opt
mv graalvm-ce-java11-$GRAALVM_VERSION /opt/graalvm

export JAVA_HOME=/opt/graalvm
export PATH=$JAVA_HOME/bin:$PATH

java -version

