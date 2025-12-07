# JellyfinPlus

A Spring Boot application for Jellyfin instance management, built with hexagonal architecture.

## Features

- REST API for media management

## Architecture

- **Domain**: Business logic and entities
- **Application**: Use cases and services
- **Infrastructure**: Adapters for web and persistence

## Technologies

- Java 21
- Spring Boot 3.3.0
- Docker

## Running

1. Build: `mvn clean package`
2. Run: `java -jar target/jellyfinplus-1.0-SNAPSHOT.jar`

Or with Docker: `docker-compose up --build`
