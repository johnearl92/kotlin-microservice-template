# Overview

This is a template POC for an API service implemented in kotlin.
Refer to the Stack and concepts for the details on what was applied on this project.



# Pre-requisites
- Java 17
- kotlin
- docker

# Stack and concepts applied
- TDD
- kotlin
- spring-boot
- kotest
- structured log with ELK (elasticsearch-logstash-kibana)
- Oauth2 Authorization
- rate limiting mechanism
- implement api versioning

# TODO:
- kubernetes
- webauthn
- use leveled api keys
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