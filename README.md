# Keycloak Sha512Whirlpool

Add a password hash provider to handle Sha512 hashed salted Whirlpool passwords inside Keycloak.

## Build JAR

```bash
./gradlew assemble -Pdependency.keycloak.version=${KEYCLOAK_VERSION}
```

## Build Docker image

```bash
docker build \
    --build-arg keycloak_version=${KEYCLOAK_VERSION} \
    -t d64ev/keycloak-sha512whirlpool \
    .
```

## Test with docker-compose

```bash
docker-compose up -d
```

## Install

### >= 17.0.0

```bash
curl -L https://github.com/d64ev/keycloak-sha512whirlpool/releases/download/v${KEYCLOAK_SHA512WHIRLPOOL_VERSION}/keycloak-sha512whirlpool-${KEYCLOAK_SHA512WHIRLPOOL_VERSION}.jar > ${KEYCLOAK_HOME}/providers/keycloak-sha512whirlpool-${KEYCLOAK_SHA512WHIRLPOOL_VERSION}.jar
```
You need to restart Keycloak.

### < 17.0.0

```bash
curl -L https://github.com/d64ev/keycloak-sha512whirlpool/releases/download/v${KEYCLOAK_SHA512WHIRLPOOL_VERSION}/keycloak-sha512whirlpool-${KEYCLOAK_SHA512WHIRLPOOL_VERSION}.jar > ${KEYCLOAK_HOME}/standalone/deployments/keycloak-sha512whirlpool-${KEYCLOAK_SHA512WHIRLPOOL_VERSION}.jar
>>>>>>> Stashed changes
```
You need to restart Keycloak.

## Run with Docker

```bash
docker run \
    -e KEYCLOAK_ADMIN=${KEYCLOAK_ADMIN} \
    -e KEYCLOAK_ADMIN_PASSWORD=${KEYCLOAK_ADMIN_PASSWORD} \
    -e KC_HOSTNAME=${KC_HOSTNAME} \
    d64ev/keycloak-sha512whirlpool \
    start
```

The image is based on [Keycloak official](https://quay.io/repository/keycloak/keycloak) one.

## How to use
Go to `Authentication` / `Policies` / `Password policy` and add hashing algorithm policy with value `sha512whirlpool`.

To test if installation works, create new user and set its credentials.

## Acknowledgements
This project is based on [keycloak-bcrypt](https://github.com/leroyguillaume/keycloak-bcrypt) by Leroy Guillaume. Thanks for the great work!
