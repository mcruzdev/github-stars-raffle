# GitHub Stars Raffle

Web app built with Quarkus that loads recent GitHub stargazers and lets you draw a random winner from the loaded participants.

The browser UI is served from the application itself. On page load it fetches the participant list from the backend, shows the loaded stargazers, and enables the raffle button only after data is available.

## What the app does

- Calls GitHub’s stargazers API for the configured repository.
- Filters the results to include only recent stars.
- Exposes the participant list through `/quarkus-flow/stargazers`.
- Lets you reload participants and draw a winner in the browser.

## Prerequisites

- Java 25
- Maven Wrapper (`./mvnw`)
- Internet access to reach GitHub’s API
- Docker if you want to build or run container images
- GraalVM is optional for native builds; you can also use container-based native compilation

## Configuration

The backend endpoint used by the workflow is configured in [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
github.quarkiverse.quarkus-flow.url=https://api.github.com/repos/quarkiverse/quarkus-flow/stargazers?per_page=100
```

This URL points to GitHub’s stargazers API for the `quarkiverse/quarkus-flow` repository.

## Run in development mode

Start the application with live reload:

```shell
./mvnw quarkus:dev
```

Open the app at:

- <http://localhost:8080>
- Quarkus Dev UI: <http://localhost:8080/q/dev/>

## Build and run the JVM application

Package the application:

```shell
./mvnw package
```

Run the packaged app:

```shell
java -jar target/quarkus-app/quarkus-run.jar
```

## Build a native executable

Build locally with GraalVM:

```shell
./mvnw package -Dnative
```

If you do not have GraalVM installed, use a container-based native build:

```shell
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Run the generated executable:

```shell
./target/github-stars-raffle-1.0.0-SNAPSHOT-runner
```

## Docker

The repository includes Dockerfiles under `src/main/docker/` for JVM and native deployments.

### JVM image

```shell
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/github-stars-raffle-jvm .
docker run -i --rm -p 8080:8080 quarkus/github-stars-raffle-jvm
```

### Native image

```shell
./mvnw package -Dnative
docker build -f src/main/docker/Dockerfile.native -t quarkus/github-stars-raffle-native .
docker run -i --rm -p 8080:8080 quarkus/github-stars-raffle-native
```

### Native micro image

```shell
./mvnw package -Dnative
docker build -f src/main/docker/Dockerfile.native-micro -t quarkus/github-stars-raffle-native-micro .
docker run -i --rm -p 8080:8080 quarkus/github-stars-raffle-native-micro
```

## How the raffle flow works

1. The browser calls `/quarkus-flow/stargazers`.
2. `StargazersResource` starts the workflow and returns the participant set as JSON.
3. `StargazersWorkflow` requests GitHub’s stargazers API and filters the results to recent stars.
4. The UI renders the loaded participants and enables the **Draw winner** button.
5. Clicking **Draw winner** selects one of the loaded participants at random.

## Learn more

- Quarkus: <https://quarkus.io/>
- Native builds: <https://quarkus.io/guides/maven-tooling>
- Docker-based native builds: <https://quarkus.io/guides/building-native-image>
