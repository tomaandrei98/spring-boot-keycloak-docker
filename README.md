# Keycloak + Spring Boot Demo

## Overview

This project demonstrates how to integrate **Keycloak** with a **Spring Boot (OAuth2 Resource Server)** application for role-based access control using JWT tokens.
It uses:

* **Keycloak** as the Identity Provider (IdP) for authentication and authorization.
* **Spring Security** for protecting API endpoints.
* **Docker Compose** to run the entire stack locally.

The example includes:

* Public endpoint (no authentication required)
* Private endpoint for users with `client_user` role
* Private endpoint for admins with `client_admin` role
* Pre-configured Keycloak realm with sample users, roles, and clients.

---

## Features

* **Role-based access control** using Keycloak realm and client roles.
* **JWT authentication** with Spring Security.
* **Stateless REST APIs** (no sessions).
* **Custom JWT Authority Converter** to map Keycloak roles to Spring Security authorities.
* **Dockerized setup** for both Keycloak and the Spring Boot app.
* **Postman collection** for testing APIs.

---

## Tech Stack

* **Java 21**
* **Spring Boot 3.5.4**
* **Spring Security (OAuth2 Resource Server)**
* **Keycloak 26.3.2**
* **Docker Compose**
* **Maven**

---

## Project Structure

```
.
├── realm-export.json         # Pre-configured Keycloak realm
├── docker-compose.yml        # Runs Keycloak + Spring Boot
├── pom.xml                   # Maven dependencies
├── src/main/java
│   ├── controller            # REST endpoints
│   ├── security              # Security configuration
├── postman_collection.json   # Postman API tests
└── Dockerfile                # Spring Boot container build
```

---

## Setup & Installation

### 1. Prerequisites

Make sure you have:

* Docker & Docker Compose installed
* Java 21 installed
* Maven installed

### 2. Clone the repository

```bash
git clone https://github.com/tomaandrei98/spring-boot-keycloak-docker
cd keycloak-spring-demo
```

### 3. Start the services

```bash
docker compose up --build
```

This will start:

* **Keycloak** at `http://localhost:8080`
* **Spring Boot API** at `http://localhost:8081`

---

## Keycloak Configuration (Pre-loaded)

The realm `Toma` is automatically imported with:

* **Clients**

    * `toma-rest-api` (public client)
* **Roles**

    * Realm: `user`, `admin`
    * Client: `client_user`, `client_admin`
* **Users**

    * **Admin**:
      Username: `toma`
      Password: `toma`
      Roles: `admin`, `client_admin`
    * **User**:
      Username: `ionut`
      Password: `ionut`
      Roles: `user`, `client_user`

---

## API Endpoints

| Method | Endpoint                  | Auth Required | Role Required  |
| ------ | ------------------------- | ------------- | -------------- |
| GET    | `/api/v1/resource/public` | No            | None           |
| GET    | `/api/v1/resource/user`   | Yes           | `client_user`  |
| GET    | `/api/v1/resource/admin`  | Yes           | `client_admin` |

---

## How to Test

### 1. Get a Token

**Admin Token**

```bash
curl -X POST \
  -d "grant_type=password" \
  -d "client_id=toma-rest-api" \
  -d "username=toma" \
  -d "password=toma" \
  http://localhost:8080/realms/Toma/protocol/openid-connect/token
```

**User Token**

```bash
curl -X POST \
  -d "grant_type=password" \
  -d "client_id=toma-rest-api" \
  -d "username=ionut" \
  -d "password=ionut" \
  http://localhost:8080/realms/Toma/protocol/openid-connect/token
```

---

### 2. Call APIs

**Public**

```bash
curl http://localhost:8081/api/v1/resource/public
```

**Private User**

```bash
curl -H "Authorization: Bearer <USER_TOKEN>" \
     http://localhost:8081/api/v1/resource/user
```

**Private Admin**

```bash
curl -H "Authorization: Bearer <ADMIN_TOKEN>" \
     http://localhost:8081/api/v1/resource/admin
```

---

## Postman Collection

You can import the included Postman collection (`keycloak.postman_collection.json`) to test:

* Token retrieval for user/admin
* Public endpoint
* Private endpoints with appropriate tokens

---

## Security Details

* Uses **`JwtAuthConverter`** to extract both realm and client roles from the JWT and map them to Spring Security authorities.
* Stateless authentication with `SessionCreationPolicy.STATELESS`.
* Role-based method security with `@PreAuthorize`.