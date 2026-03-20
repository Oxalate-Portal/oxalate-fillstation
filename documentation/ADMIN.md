# Administration Documentation

## Oxalate FillStation – Administrator & Operator Guide

---

### 1. Initial Setup

#### 1.1 Environment Variables

The following environment variables must be set before starting the backend container:

| Variable                     | Required | Description                                                                 |
|------------------------------|----------|-----------------------------------------------------------------------------|
| `SPRING_DATASOURCE_URL`      | Yes      | JDBC URL for the PostgreSQL database                                        |
| `SPRING_DATASOURCE_USERNAME` | Yes      | Database username                                                           |
| `SPRING_DATASOURCE_PASSWORD` | Yes      | Database password                                                           |
| `JWT_SECRET`                 | Yes      | Secret key used to sign JWT tokens (min. 32 chars)                          |
| `JWT_TTL_MINUTES`            | No       | JWT token lifetime in minutes (default: 60)                                 |
| `SPRING_MAIL_HOST`           | Yes      | SMTP server hostname                                                        |
| `SPRING_MAIL_PORT`           | No       | SMTP port (default: 587)                                                    |
| `SPRING_MAIL_USERNAME`       | Yes      | SMTP authentication username                                                |
| `SPRING_MAIL_PASSWORD`       | Yes      | SMTP authentication password                                                |
| `APP_BASE_URL`               | Yes      | Public base URL of the application (e.g. `https://fillstation.example.com`) |
| `INITIAL_ADMIN_EMAIL`        | No       | Email address for the initial admin account                                 |
| `INITIAL_ADMIN_PASSWORD`     | No       | Password for the initial admin account                                      |

> If `INITIAL_ADMIN_EMAIL` and `INITIAL_ADMIN_PASSWORD` are set, the backend will create or update the admin account on startup. Use this to recover a forgotten admin password.

#### 1.2 Running with Docker Compose

A `compose.yaml` is provided in the `oxalate-fillstation-backend` directory. Configure the environment variables above (via a `.env` file or directly in `compose.yaml`) and run:

```bash
docker compose up -d
```

Flyway migrations run automatically on first start and create all required database tables.

---

### 2. User Management (Operator Role)

Navigate to the **Operator Panel** in the sidebar.

#### 2.1 Pending Registrations

The **Pending Registrations** tab lists all accounts awaiting approval.

- **Approve**: Activates the account and sends an approval email to the user.
- **Reject**: Locks the email address so it cannot be re-used for registration. No email is sent to the user.

#### 2.2 Users List

The **Users** tab displays all registered accounts in a table with the following columns:

| Column     | Description                            |
|------------|----------------------------------------|
| ID         | Internal user identifier               |
| Name       | Full name                              |
| Email      | Email address                          |
| Status     | PENDING / ACTIVE / LOCKED              |
| Roles      | ROLE_USER / ROLE_OPERATOR / ROLE_ADMIN |
| Registered | Account creation date                  |

**Available actions per user:**

- **Update status**: Change the account status (e.g. activate or lock an account).
- **Zero fills**: Mark all **Active** and **Locked** fills of the user as **Zeroed** and reset their "since last zero" counter. The user receives an email notification.
- **Send password reset**: Dispatch a password-reset link to the user's registered email address.

#### 2.3 Notifying Users

The **Notify Users** button sends a gas-usage notification email to all users who have at least one fill with status **Active** or **Locked**. Use this to remind users to review their outstanding fills.

---

### 3. Configuration Management (Admin Role)

Navigate to the **Admin Panel** in the sidebar.

The configuration table stores application-wide settings as **group / key / value** triplets.

#### 3.1 Add a Configuration Entry

1. Click **Add configuration**.
2. Enter the **Group**, **Key**, and **Value**.
3. Click **Save**.

#### 3.2 Edit / Delete a Configuration Entry

Use the **Edit** or **Delete** icons in the configuration table row.

---

### 4. Roles and Permissions

| Feature                          | User | Operator | Admin |
|----------------------------------|------|----------|-------|
| Manage own cylinders             | ✔    | ✔        | ✔     |
| Manage own fills                 | ✔    | ✔        | ✔     |
| View own gas usage               | ✔    | ✔        | ✔     |
| View all users                   |      | ✔        | ✔     |
| Approve / reject registrations   |      | ✔        | ✔     |
| Zero user fills                  |      | ✔        | ✔     |
| Send notification emails         |      | ✔        | ✔     |
| Manage application configuration |      |          | ✔     |

A single user account may hold multiple roles simultaneously.

---

### 5. Security Notes

- JWT tokens are stored in **HTTP-only cookies** and are not accessible via JavaScript.
- Passwords are hashed with **BCrypt** and never stored in plain text.
- The JWT secret is supplied via environment variable and is never committed to source control.
- Email addresses of rejected registrations are stored in a locked-email table to prevent future re-registration attempts.
- Users can request GDPR anonymisation of their personal data from the Dashboard.
