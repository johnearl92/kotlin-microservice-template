# Overview

This is a template for a kotlin-spring-boot-kotest Rest API project


# Pre-requisites
- Java 17
- kotlin
- docker

# Stack and concepts applied
- kotlin
- spring-boot
- kotest
- structured log with ELK (elasticsearch-logstash-kibana)
- Oauth2 Authorization

# TODO:
- kubernetes
- webauthn
- use leveled api keys
- rate limiting mechanism
- implement api versioning
- allow list mechanism

# How To
 The local deployment will make use of the dev profile which will use H2 as database while the docker 
 deployment will use the prod in which it will use the MySQL database
## Run
```
./gradlew bootRun
```

## Build Docker Image
```agsl
bootBuildImage --imageName=earl/byob
```

# Swagger
go to `http://localhost:8080/swagger-ui/index.html#/`