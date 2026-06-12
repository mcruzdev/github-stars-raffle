# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A Quarkus web app that loads recent stargazers of a GitHub repo and lets you draw a random winner in the browser. It serves as a demo of the **quarkus-flow** extension (Quarkiverse implementation of the CNCF Serverless Workflow spec).

Requires Java 25 (`.sdkmanrc` pins `25.r25-mandrel`). Internet access to api.github.com is needed at runtime.

## Commands

```shell
./mvnw quarkus:dev          # dev mode with live reload, app at http://localhost:8080, Dev UI at /q/dev/
./mvnw package              # build JVM app (runs tests); run with: java -jar target/quarkus-app/quarkus-run.jar
./mvnw test                 # run unit tests
./mvnw test -Dtest=ClassName            # run a single test class
./mvnw package -Dnative     # native build with local GraalVM (also enables integration tests via failsafe)
./mvnw package -Dnative -Dquarkus.native.container-build=true   # native build without local GraalVM
```

Integration tests (`*IT`) are skipped by default (`skipITs=true`); the `native` profile enables them.

## Architecture

The request flow spans three pieces under `src/main/java/guru/quarkus/`:

1. **`StargazersResource`** (`/quarkus-flow/stargazers`) — JAX-RS endpoint. Each GET starts a workflow instance (`workflow.startInstance()`, reactive via Mutiny `Uni`) and maps the resulting workflow model to a `Set<Stargazer>`.
2. **`StargazersWorkflow`** — extends quarkus-flow's `Flow` and defines the workflow declaratively in `descriptor()` using the Serverless Workflow fluent DSL (`FuncWorkflowBuilder`/`FuncDSL`). Two tasks chained: an HTTP GET to GitHub's stargazers API (with the `application/vnd.github.star+json` Accept header, required so responses include `starred_at`), then a Java function task that filters to recent stars.
3. **`Stargazer`** — Jackson record bound to the GitHub API response (`starred_at`, nested `user` with `avatar_url`).

The frontend is a single static page at `src/main/resources/META-INF/resources/index.html` (Bootstrap via CDN, vanilla JS). It fetches the participant list from the backend on load and does the random draw entirely client-side.

Configuration lives in `src/main/resources/application.properties`: `github.repo` selects the repository, and `github.quarkiverse.quarkus-flow.url` (derived from it) is the stargazers API URL injected into the workflow.
