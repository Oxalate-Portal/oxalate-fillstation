# GitHub Copilot Instructions for Oxalate FillStation

## Repository Overview

This is a multi-module repository for the **Oxalate FillStation** application — a web application for managing gas cylinders and fill records for dive gas (oxygen/helium mixtures).

The repository contains two subprojects:

- **`oxalate-fillstation-frontend/`** — React/TypeScript frontend (Vite + Ant Design)
- **`oxalate-fillstation-backend/`** — Spring Boot Java backend (Gradle multi-module)

## Technology Stack

### Frontend (`oxalate-fillstation-frontend/`)

- **Language:** TypeScript (strict, no `any` types allowed)
- **Framework:** React with Vite
- **UI components:** Ant Design (`antd`)
- **Routing:** React Router
- **HTTP client:** Axios
- **Internationalisation:** `react-i18next` (supported locales: `de`, `en`, `es`, `fi`, `sv`)
- **Testing:** Jest + React Testing Library
- **Linting:** ESLint

### Backend (`oxalate-fillstation-backend/`)

- **Language:** Java 25
- **Framework:** Spring Boot 4
- **Build tool:** Gradle (multi-module: `api` + `service`)
- **Database:** PostgreSQL (latest stable); migrations managed with Flyway
- **Security:** Spring Security with JWT stored in a cookie
- **Email templates:** Thymeleaf (`spring-boot-starter-thymeleaf`)
- **API documentation:** OpenAPI / Swagger (annotations on interfaces in the `api` module)
- **Testing:** JUnit, Mockito, Testcontainers

## Project Structure

```
oxalate-fillstation/
├── VERSION                          # <major>.<minor> base version
├── documentation/
│   ├── PRD.md                       # Product Requirements Document
│   ├── USER.md                      # End-user documentation
│   └── ADMIN.md                     # Administrator documentation
├── oxalate-fillstation-frontend/    # React/TypeScript application
│   ├── src/
│   │   ├── api/                     # Axios API call modules
│   │   ├── components/              # Shared React components
│   │   ├── context/                 # React context providers (e.g. AuthContext)
│   │   ├── pages/                   # Page-level components
│   │   └── types/                   # TypeScript type definitions
│   └── generateBuildInfo.cjs        # Generates src/buildInfo.json from VERSION + git tags
└── oxalate-fillstation-backend/
    ├── api/                         # API module: interfaces, DTOs, enums
    └── service/                     # Service module: controllers, services, repositories
```

## Coding Conventions

### General

- All AI-agent tasks that modify the codebase **must** include corresponding tests.
- Documentation (`USER.md`, `ADMIN.md`) must be kept up-to-date whenever features change.
- Functions/methods must be documented with **JSDoc** (frontend) or **JavaDoc** (backend).

### Frontend

- **No `any` types** — every data structure must have an explicit TypeScript type defined in `src/types/`.
- Translations must use `react-i18next`. All user-visible strings must be wrapped in `t('key')` and defined in all supported locale files (`de`, `en`, `es`, `fi`, `sv`).
- Use Ant Design components for UI; do not introduce additional UI component libraries.
- When adding new API calls, add the function to the appropriate file in `src/api/`.

### Backend

- The `api` module contains **interface definitions**, DTOs, and enums only. Controllers in the `service` module implement these interfaces.
- All OpenAPI annotations (Swagger `@Operation`, `@ApiResponse`, etc.) and `jakarta.validation` annotations belong on the **interface** in the `api` module — keep controllers clean.
- Role-based access control is enforced in the backend using Spring Security method-level annotations (`@PreAuthorize`, etc.). Do not rely solely on frontend role checks.
- Passwords are always stored hashed; JWT secrets are supplied via environment variables only — never hard-coded.
- New database schema changes must be implemented as Flyway migration scripts.

## Testing Conventions

### Naming

- Unit tests: filename ends with `UTC` (e.g. `UserServiceUTC.java`, `UserServiceUTC.test.ts`)
- Integration tests: filename ends with `ITC` (e.g. `UserServiceITC.java`)
- Contract tests: filename ends with `CTC` (e.g. `UserControllerCTC.java`)

### Test case naming pattern

```
<methodName>_<scenario>_<Ok|Fail>
```

Example: `createUser_duplicateEmail_Fail`, `createUser_validInput_Ok`

For contract tests the method name corresponds to the REST endpoint interface method name.

### Backend

- Use **JUnit 5** and **Mockito** for unit tests.
- Use **Testcontainers** for integration tests that require a database.

### Frontend

- Use **Jest** and **React Testing Library**.
- Run tests with `yarn test` from the `oxalate-fillstation-frontend/` directory.

## Versioning

The `VERSION` file at the repository root contains the `<major>.<minor>` base version. The full semantic version (`<major>.<minor>.<patch>`) is derived automatically by the CI workflow based on existing git tags:

- If no tag with the given major/minor exists yet, the patch version starts at `0`.
- Otherwise it increments the highest existing patch by `1`.

The frontend build script (`generateBuildInfo.cjs`) reads `VERSION` and git tags to produce `src/buildInfo.json`, which is imported to display the version in the app footer.

## CI/CD (`.github/workflows/ci.yml`)

| Trigger | Jobs run |
|---|---|
| Push to any non-`main` branch | Build + test (frontend & backend) |
| Push / merge to `main` | Build + test → semantic version tag → Docker images pushed to GHCR → GitHub Release created |

Docker images are tagged: `latest`, `<major>`, `<major>.<minor>`, `<major>.<minor>.<patch>`.

## Roles

| Role | Permissions |
|---|---|
| `ROLE_USER` | Own cylinders and fill entries only |
| `ROLE_OPERATOR` | All user data (read); approve/reject registrations; zero-out fills; send notification emails |
| `ROLE_ADMIN` | All of the above + manage application configuration |

## Security Considerations

- JWT is stored in a **cookie** (not `localStorage`).
- The JWT secret is provided via an environment variable and must never appear in source code.
- The initial admin credentials for first-time setup are passed through a Docker Compose environment variable; the backend hashes the password before storing it.
- The application must comply with GDPR — users can request anonymisation of their personal data.
