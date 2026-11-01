# School Equipment Lending Portal

A full-stack application for managing school equipment inventory and lending requests. Students can browse equipment and submit requests; staff and administrators have separate workspaces for viewing equipment and processing requests. The current backend is a Spring Boot application, paired with a React frontend and a PostgreSQL database.

## Overview

The portal centralizes equipment records, categories, availability, request status, and user accounts. It is organized as a learning/project application with a Java REST backend and a browser-based user interface.

## Features

- Sign up and sign in using email and password; the UI fetches available user types from the server.
- Maintain equipment records, categories, and availability types (administrator UI).
- Browse equipment as a student or staff member.
- Submit equipment requests with quantity and purpose, and view/filter a student's requests.
- Review requests as staff or administrator. The service supports pending, approved, rejected, and closed states, validates supported transitions, checks stock when approving, and updates stock on approval and closing.
- Keep expected and actual return dates and basic created/modified audit fields on records.
- Use session-aware client routing for separate administrator, staff, and student pages.

The implemented workflow does not include real-time notifications. The UI and backend should be treated as a project implementation rather than a production-ready access-control boundary; see [Authentication and security](#authentication-and-security).

## User roles

The frontend defines three role workspaces:

| Role | Implemented UI capabilities |
| --- | --- |
| Administrator | Manage equipment, categories, availability statuses, and requests. |
| Staff | View equipment and manage requests. |
| Student | Browse equipment, submit requests, and track or cancel requests. |

Role names are loaded from the `user_types` table. The signup form offers the user types returned by the backend; the project does not document an administrator provisioning or invitation workflow.

## Technology stack

| Area | Technologies present in the project |
| --- | --- |
| Backend | Java 21, Spring Boot 3.5.16, Spring Web, Spring Data JPA, Spring Security, Bean Validation, Maven |
| Frontend | React 19, React Router 7, Axios, Material UI, React Icons, React Toastify, date-fns, Create React App (`react-scripts`) |
| Database | PostgreSQL; Hibernate/JPA validates the existing schema at startup |
| Tests | Spring Boot Test, Spring Security Test, JUnit-based unit, controller, security, repository, and integration tests |

## Architecture and project structure

The frontend is a separate React application. It makes credentialed Axios requests to the Spring Boot backend, which uses controller, service, repository, DTO, and JPA entity layers.

```text
.
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/schoolportal/
│       │   ├── config/       # Spring Security and CORS configuration
│       │   ├── controller/   # HTTP endpoints
│       │   ├── dto/          # API transfer objects
│       │   ├── entity/       # JPA entities
│       │   ├── repository/   # Spring Data repositories
│       │   ├── security/     # Session authorization filter
│       │   └── service/      # Application and business logic
│       └── test/             # Backend tests
├── frontend/
│   ├── package.json
│   └── src/
│       ├── components/       # Role-specific and shared UI components
│       ├── contexts/         # Session context
│       ├── pages/            # Auth, admin, staff, student, and common pages
│       ├── services/         # Axios API calls grouped by feature/role
│       └── styles/           # CSS grouped by page/component area
├── db_backup/                # PostgreSQL SQL dump
└── screenshots/              # Existing application screenshots
```

## Database design

The JPA model and included PostgreSQL dump describe these tables:

| Table | Purpose and relationships |
| --- | --- |
| `users` | User identity and password hash; references `user_types`. |
| `user_types` | User role/type lookup. |
| `equipments` | Equipment name, description, total and remaining quantities; references `equipment_categories` and `equipment_availability_types`. |
| `equipment_categories` | Equipment category lookup and description. |
| `equipment_availability_types` | Equipment availability state lookup. |
| `equipment_requests` | Requested quantity, purpose, dates, and audit fields; references `equipments` and `request_status_types`. `created_by` identifies the requesting user in application logic. |
| `request_status_types` | Request status lookup. |

The entity mappings explicitly represent the equipment-to-category, equipment-to-availability, request-to-equipment, request-to-status, and user-to-user-type many-to-one relationships. The SQL dump also defines constraints and foreign keys. The application config uses `spring.jpa.hibernate.ddl-auto=validate`; the database schema must exist before startup.

## Authentication and security

- Passwords are encoded and checked with Spring Security's BCrypt password encoder.
- Successful login creates an HTTP session with the user ID, user type, and name. The session inactivity timeout is set to one hour.
- The frontend sends requests with cookies (`withCredentials: true`); the backend CORS configuration allows `http://localhost:3000` with credentials.
- React `PrivateRoute` guards role-specific UI routes, and the UI checks session state through the session-info endpoint.
- **Backend authorization caveat:** the custom `SessionAuthorizationFilter` checks paths beginning `/api/`, `/admin/`, `/staff/`, or `/student/`. The current controllers instead use legacy paths such as `/EquipmentServlet` and `/EquipmentRequestServlet`, which do not match those protected prefixes. Therefore the filter's role checks do not protect the current controller routes. Do not rely on the frontend route guards as server-side authorization.
- CSRF protection is disabled in the current security configuration. Revisit CSRF and server-side authorization before deployment beyond local development.

## Screenshots

The repository contains application screenshots in `screenshots/`. A few are shown below.

![Application screenshot 1](screenshots/School_Equipment_Lending_Portal_App_Demo(1).jpg)

![Application screenshot 2](screenshots/School_Equipment_Lending_Portal_App_Demo(2).jpg)

![Application screenshot 3](screenshots/School_Equipment_Lending_Portal_App_Demo(3).jpg)

## Prerequisites

- Java 21
- Maven (or use the Maven wrapper if one is added; none is present in this repository)
- Node.js and npm
- PostgreSQL
- Git

## Installation and setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd SELP-Spring-Boot
```

Replace `<repository-url>` with the URL of your Git remote.

### 2. Prepare PostgreSQL

Create a PostgreSQL database, then create its tables using the supplied plain SQL dump or another schema that matches the JPA entities. The dump contains both schema and data; inspect it before importing it into a database you intend to keep.

For example, with PostgreSQL command-line tools:

```bash
createdb school_equipment_lending_portal
psql -d school_equipment_lending_portal -f db_backup/dump-school_equipment_lending_portal-202511091559.sql
```

The default datasource URL in `backend/src/main/resources/application.properties` targets a local database named `school_equipment_lending_portal`. Configure the database connection for your own environment before starting the application. Keep credentials out of source control.

### 3. Configure and run the backend

The backend is a Maven project. From the repository root:

```bash
cd backend
mvn spring-boot:run
```

The configured server port is `8080`, with context path `/school-equipment-lending-portal`.

The checked-in properties file currently contains local datasource settings. For a safer local setup, provide your own settings outside version control or override Spring's datasource properties at runtime. Common overrides are `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`. Do not commit real credentials.

### 4. Install and run the frontend

In a second terminal, from the repository root:

```bash
cd frontend
npm install
npm start
```

The frontend development server uses Create React App's default port `3000`. Its Axios base URL is currently configured in `frontend/src/services/baseURL/api.js` as `http://localhost:8080/school-equipment-lending-portal`; the backend CORS configuration also allows the local frontend origin on port `3000`.

## Configuration reference

| Setting | Current source/default | Notes |
| --- | --- | --- |
| Backend port | `server.port=8080` in `backend/src/main/resources/application.properties` | Spring Boot server port. |
| Backend context path | `/school-equipment-lending-portal` | Prefix for controller URLs. |
| Database URL | Local PostgreSQL URL in backend properties | Override for your local database. |
| Database username/password | Present as local values in backend properties | Do not reuse or publish them; supply private values in your environment. |
| Frontend API URL | `frontend/src/services/baseURL/api.js` | Currently points to the local backend context path. |
| Allowed frontend origin | `http://localhost:3000` in `SecurityConfig` | Update backend CORS configuration if using another frontend origin. |

No dedicated `.env` file or frontend environment-variable convention is configured in the repository.

## Running tests

Backend tests are run with Maven:

```bash
cd backend
mvn test
```

The test suite includes service and repository tests, controller tests, security/CORS tests, and integration tests for authentication, sessions, equipment, categories, availability types, and requests. Test configuration points to a PostgreSQL database named `school_equipment_lending_portal_test` and uses Hibernate `create-drop`; ensure that test database is available and safe for disposable test data.

The frontend defines the Create React App `test` script (`npm test`), but no frontend test files are present in the tracked source tree at the time this README was prepared.

## API overview

All backend routes are under `/school-equipment-lending-portal`. The names retain the earlier `Servlet` suffix even though they are Spring MVC controllers.

| Endpoint group | Implemented operations |
| --- | --- |
| `/LoginServlet` | `POST` login |
| `/SignupServlet` | `POST` signup |
| `/LogoutServlet` | `POST` logout |
| `/SessionInfoServlet` | `GET` current session information |
| `/UserTypesServlet` | `GET` available user types |
| `/EquipmentServlet` | `GET` list, `POST` create, `PUT /{equipmentId}` update, `DELETE /{equipmentId}` delete |
| `/EquipmentCategoryServlet` | `GET` list, `POST` create, `PUT /{categoryId}` update, `DELETE /{categoryId}` delete |
| `/EquipmentAvailabilityTypesServlet` | `GET` availability types |
| `/RequestStatusTypesServlet` | `GET` request statuses |
| `/EquipmentRequestServlet` | `POST` create, `GET /{id}` list by status (optional `statusFilterId` for student/status), `PUT /{requestId}` update, `DELETE /{requestId}` delete |

See the controllers under `backend/src/main/java/com/schoolportal/controller/` for request and response DTO details.

## Possible future improvements

These are suggestions, not claims about current functionality:

- Apply server-side authorization consistently to every protected controller route and enable an appropriate CSRF strategy for session-based authentication.
- Move all environment-specific configuration out of checked-in properties and provide a documented local configuration template.
- Add frontend automated tests and API documentation, and add database migrations for repeatable schema setup.
- Add operational features such as notifications or reporting if required by the school workflow.

## Project information

**Project:** School Equipment Lending Portal (SELP)  
**Repository:** SELP-Spring-Boot  
**Author / maintainers:** Not identified in the repository metadata reviewed for this README.
