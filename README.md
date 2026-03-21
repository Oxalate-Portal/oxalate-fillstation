# Oxalate Fill Station

This project is a web-based app for fill stations of dive gas mixtures. It allows users to manage their cylinders and
record the gas mixtures they fill. The app is built using React for the frontend and Java Spring Boot for the backend,
with a PostgreSQL database to store the data.

To simplify the installation, the project uses Docker Compose with Traefik as reverse proxy to run the frontend,
backend, and PostgreSQL database in separate containers.

## Default Administrator Account

Flyway seeds a default administrator account on first database initialization:

- Email: `admin@fillstation.local`
- Role: `ROLE_ADMIN`

Set or reset the administrator password on startup by providing:

- `INITIAL_ADMIN_EMAIL`
- `INITIAL_ADMIN_PASSWORD`

When these environment variables are present, the backend will create the admin account if missing, or update the
existing account password and enforce the admin role.

## API Documentation (Swagger UI)

When the backend is running locally with the default configuration, Swagger UI is available at:

- http://localhost:8080/swagger-ui/index.html

## Build Version in Frontend

The frontend build process generates `src/buildInfo.json` from the repository `VERSION` file and git tags.
The app footer shows the generated semantic version and build timestamp.

## Unified CI/CD

One workflow file (`.github/workflows/ci.yml`) handles both frontend and backend:

- On non-`main` branches: build + test only.
- On `main`: build + test, resolve semantic version from `VERSION` and tags, tag release commit, build/push frontend
  and backend images to GHCR, and create a GitHub release.

