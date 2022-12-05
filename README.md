### Counter Application

This simple project is designed to manipulate counters using REST API for [the given task](https://gist.github.com/taiberium/dd4a8af2327e18327a1d7c32a83a923c).

#### Swagger UI url path with available methods:
[http://{your-domain}/swagger-ui/#/](http://{your-domain}/swagger-ui/#/)

---
#### Used technologies and frameworks:
* Java 17
* Spring Boot (Web MVC, Test)
* Swagger UI (OpenApi)

#### Build & deployment tools:
* Gradle
---

### Build, package & run:
Be sure you got to have installed Java 17.
1. _To Start_: run the command below in project's root directory using Terminal or CMD
```bash
./gradlew bootRun
```
or using Docker in an isolated environment
```bash
./gradlew bootJar & docker run --name counter-app --rm -d $(docker build -q .)
```
2. _To stop_: run the next command in the same directory:
```bash
docker stop counter-app
```
---
#### Additional Links
These additional references should also help you:
* [About me](https://github.com/salaheev)

