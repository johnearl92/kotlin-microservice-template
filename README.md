# Overview

This is a template for a kotlin-spring-boot-kotest Rest API project


# Pre-requisites
- Java 17
- kotlin
- Spring-boot
- kotest
- kotest spring
- docker
### TODO:
- kubernetes


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