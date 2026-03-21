# User Documentation

## Oxalate FillStation – User Guide

### 1. Getting Started

The application footer displays the current build version and build timestamp to help with support and troubleshooting.

#### 1.1 Registration

1. Open the application in your browser and click **Register**.
2. Fill in your **full name**, **email address**, preferred **language** (German, English, Spanish, Finnish, or Swedish), and a **password**.
3. Click **Register**. A confirmation email will be sent to the address you provided.
4. Click the verification link in the email to confirm your address.
5. Your account will then be reviewed by an operator. Once approved, you will receive a second email and can log in.

> **Note:** If you try to register with an email that was previously rejected, registration will not be possible.

#### 1.2 Logging In

1. Go to the **Login** page.
2. Enter your registered email address and password.
3. Click **Login**. On success, you are redirected to your **Dashboard**.

#### 1.3 Forgotten Password

1. Click **Forgot password** on the Login page.
2. Enter your email address and click **Send reset link**.
3. Follow the link in the email you receive to set a new password.

---

### 2. Dashboard

The Dashboard shows an overview of your gas usage:

| Section             | Description                                                          |
|---------------------|----------------------------------------------------------------------|
| **Total usage**     | Cumulative O₂, He, and total gas (in litres) across all your fills   |
| **Since last zero** | Usage since an operator last reset your counters                     |
| **Login history**   | A table of your recent login events with timestamps and IP addresses |

You can also **anonymise your account** from the Dashboard. This replaces your personal information (name, email) with anonymous identifiers in compliance with GDPR. The action is irreversible.

---

### 3. Managing Cylinders

Navigate to **Cylinders** in the sidebar.

#### 3.1 Add a Cylinder

1. Click **Add cylinder**.
2. Enter the **name**, **volume** (litres), **working pressure** (bar), and **serial number**.
3. Click **Save**.

#### 3.2 Edit a Cylinder

1. Find the cylinder in the table and click the **Edit** (pencil) icon.
2. Update any field and click **Save**.

#### 3.3 Delete a Cylinder

1. Click the **Delete** (bin) icon for the cylinder.
2. Confirm the deletion in the dialog.

> Cylinders that have associated fill entries cannot be deleted until all related fills are removed.

---

### 4. Managing Gas Fills

Navigate to **Fills** in the sidebar.

#### 4.1 Add a Fill

1. Click **Add fill**.
2. Select a **cylinder** and the **fill date**.
3. Enter start and end pressures (bar) and start/end O₂ and He percentages.
4. Optionally add **notes**.
5. Click **Save**. The amounts of O₂, He, and total gas added are calculated automatically.

#### 4.2 Fill Statuses

| Status     | Meaning                                               |
|------------|-------------------------------------------------------|
| **Active** | Recently created; editable and deletable for 24 hours |
| **Locked** | Older than 24 hours; cannot be edited or deleted      |
| **Zeroed** | Marked by an operator as accounted for                |

> Once a fill is **Locked** or **Zeroed** it cannot be modified.

#### 4.3 Edit / Delete a Fill

An **Edit** or **Delete** button is shown only while the fill is in **Active** status (within 24 hours of creation). After that, the buttons are disabled.

---

### 5. Profile

You can update your **name**, **email**, and preferred **language** from the profile settings. Changes take effect immediately.

---

### 6. Logging Out

Click **Logout** in the navigation sidebar. Your session token is invalidated immediately.
