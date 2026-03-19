# Product Requirement Document

## Project description

There are two subprojects in this project: the frontend and the backend. The frontend is a web application that allows users to create and manage their
information as well as gas fills. The backend is a RESTful API that provides the necessary endpoints for the frontend to interact with the database.

## 1. Technologies used

The frontend uses React with Vite and for UI components it uses Ant Design. The code is written in TypeScript. The frontend uses React Router for navigation and
Axios for making HTTP requests to the backend. The frontend docker container is built using the NGINX image to which the production build of the React app is
copied.

The backend is built using latest stable Java as well as latest Spring Boot and uses PostgreSQL as the database. Backend uses Flyway to manage database
migrations.

The database server is the latest stable PostgreSQL version.

In addition, traefik is used as a reverse proxy to route requests to the appropriate containers in the docker-compose setup.

## 2. Features

### Registration

- Users can register for an account by providing their name, email address, preferred language (see below for valid options), and password. The registration
  process includes validation to ensure that the email address is in a valid format and that the password meets certain complexity requirements. Once the user
  submits the registration form, they will receive a confirmation email with a link to verify their email address. This helps to ensure that only valid email
  addresses are used for registration and helps to prevent spam accounts. The email is used as the login identifier for the user, and it must be unique across
  all users in the system. This means that two users cannot register with the same email address, which helps to ensure that each user has a unique account and
  can be easily identified in the system.
- In addition to the email verification process the account must also be approved by the operator. This means that after a user registers, their account will be
  in a pending state until an operator reviews and approves the registration. This helps to ensure that only legitimate users are able to access the system and
  helps to prevent unauthorized access.

### Authentication and Authorization

- Users can register and log in to the application.
- Users can be assigned different roles. The following roles are available:
    - Admin: Can manage configuration, users, and read all user data
    - Operators: Can manage their own data and view all user data
    - User: Can manage their own data.
      The same user account can have multiple roles, for example, a user can be both an operator and a regular user. This allows for flexible access control
      based on the user's needs and responsibilities.
- Passwords are securely hashed and stored in the database.
- JWT tokens are used for authentication and authorization. The token is passed in a cookie for secure storage. The TTL of the token is configurable by the
  administrator. The JWT secret is set through an environment variable and is not stored in the codebase.
- A special docker compose environment variable is used to set the initial admin user credentials. This allows for easy setup of the application without needing
  to manually create an admin user. When this variable is passed to the backend container, it will hash the new admin password and store it in the database.
  This ensures that there is a mechanism for resetting a forgotten admin password without needing to access the database directly.
- The application implements role-based access control (RBAC) to ensure that users can only access the features and data that they are authorized to access.
  This is implemented using Spring Security in the backend and role checks in the frontend.
- The application also includes a password reset feature that allows users to reset their password if they forget it. This is implemented using a secure
  token-based system that sends a password reset link to the user's email address.
- The application also includes a feature for users to update their profile information, such as their name and email address. This is implemented using a
  secure form that allows users to update their information while ensuring that their data is protected.
- There should be a mechanism for users to log out of the application, which will invalidate their JWT token and clear the authentication cookie.
- The application should also include a feature for users to view their login history, which can help them identify any unauthorized access to their account.
  This is implemented using a secure endpoint that allows users to view their login history while ensuring that their data is protected.
- The application should also include a feature which allows users to anonymize any personal information stored in the database, according to GDPR regulations.
  This is implemented using a secure endpoint that allows users to request the anonymization of their data, which will replace any personally identifiable
  information with anonymous identifiers while ensuring that the data remains usable for analysis and reporting purposes.
- When the user logs in, the frontend receives an answer which contains among other the roles of the user. This will then restrict what functionality is
  available to the user in the frontend. For example, if the user is an admin, they will have access to the admin panel where they can manage users and
  configuration. If the user is an operator, they will have access to the operator panel where they can manage their own data and view all user data. If the
  user is a regular user, they will only have access to their own data and will not be able to view other users' data.
- The backend should separately verify the user's roles for each request to ensure that they have the necessary permissions to access the requested resource.
  This is implemented using Spring Security's method-level security annotations, which allow for fine-grained access control based on user roles.

### Functionality

#### User functionality

Primary functionality is to allow the users to manage their cylinders and gas fills. This includes the following features:

- Users can add, edit, and delete their cylinders. Each cylinder has the following attributes: name, volume, working pressure, and serial number as well as a
  unique identifier per user. All fields are required.
- Users can add, edit, and delete their gas fills. Each gas fill entry has the following attributes: date, start pressure, end pressure, start oxygen
  percentage,
  start helium percentage, end oxygen percentage and end helium percentage and notes as well as a unique identifier. All fields are required except for notes.
  The helium percentage may be zero, but the oxygen percentage must be greater than zero. If the start helium percentage is greater than zero, then the end
  helium percentage must also be greater than zero.
- Calculation of added gasses is done automatically based on the start and end pressures and the start and end gas percentages. The app calculates the amount of
  oxygen and helium added to the cylinder during the fill, as well as the total amount of gas added. This information is displayed to the user in a clear and
  concise manner. The amount of gas added is calculated using the ideal gas law, which takes into account the volume of the cylinder, the change in pressure,
  and the change in gas percentages. These amounts are stored in the database as part of the fill entry. The app also provides a visual representation of the
  gas
  mixture in the cylinder, allowing users to easily see the composition of their gas fills. This can help users make informed decisions about their gas mixtures
  and ensure that they are filling their cylinders safely and efficiently.
- The user only has 24h to update their fill entry after creating it. After that, the fill entry is locked and cannot be edited or deleted. This is to ensure
  that the data remains accurate and reliable, as users may forget to update their fill entries or may not have the necessary information to update them after a
  certain period of time. Once the fill entry is locked, the frontend will not allow the user to edit or delete it, and the backend will reject any requests to
  update or delete the fill entry after the 24h period has passed.
- The fill entries should have the following status:
    - Active: The fill entry is active and can be edited or deleted by the user. This is the default status when a fill entry is created.
    - Locked: The fill entry is locked and cannot be edited or deleted by the user. This status is set automatically after 24h from the creation of the fill
      entry.
    - Zeroed: The fill entry has been zeroed by an operator and cannot be edited or deleted by the user. This status is set manually by an operator when they
      mark a fill entry as zeroed.
- The gas used by each user is aggregate and displayed on two levels:
    - The total amount of gas used by the user across all fills, which is displayed on the user's dashboard. This allows users to track their overall gas usage
      and monitor their consumption over time.
    - The amount of gas used since the last time the fills of the user was zeroed. This calculates the amount of gasses for all the fills made by the user with
      the state locked, or active
      Both of these aggregate values are shown in the user dashboard and are updated automatically whenever a new fill entry is created, edited, or deleted.
      This allows users to easily track their gas usage and make informed decisions about their fills.

##### Operator functionality

Accounts with operator role have the following additional functionality:

- Operators can see the full list of all users as a table. The table contains a link to the user-specific information. In addition to the information the page
  also contains the buttons for operators to resend a reset password link as well as updating the status of the user.
- The operators also see a button which allows them to send an email to all the users which have fills with the status of locked or active. The email will be
  based on a template
- Operators can view all user data, but they cannot edit or delete other users' data. This allows operators to monitor the activity of all users while ensuring
  that they cannot make unauthorized changes to the data.
- Operators can mark the fills of a user as zeroed. This is done by changing the status of the fill entry from active or locked to zeroed, which indicates that
  the fill entry has been zeroed and cannot be edited or deleted by the user. This allows operators to reset the gas usage for a user without needing to delete
  their fill entries, which can help to maintain the integrity of the data while still allowing for accurate tracking of gas usage. When the fills of an user is
  zeroed out, the system sends the user an email to notify them that their fills have been zeroed and their gas usage has been reset. This helps to ensure that
  users are aware of any changes to their data and can take appropriate action if necessary.
- Operators can see the pending registrations and approve or reject them. This allows operators to manage the user base and ensure that only legitimate users
  are able to access the system. When approving a pending registration, an email is sent to the user to notify them that their account has been approved, and
  they can now log in to the application. When rejecting no email is sent but the email can not be registered again. Instead, that address is locked.

#### Admin functionality

Accounts with admin role have the following additional functionality:

- The backend will maintain a configuration table in the database that contains group-key-triplets for various configuration settings. The admin panel will
  provide an interface for managing these configuration settings, allowing administrators to easily update and maintain the configuration of the application.
  This allows for easy management of the application's configuration without needing to access the database directly. The backend also provides an API endpoint
  for retrieving the configuration settings, which can be used by the frontend to dynamically adjust its behavior based on the current configuration. This
  allows for a more flexible and adaptable application that can easily accommodate changes in requirements or user needs.

### Other

#### Backend API

The backend should provide a RESTful API that allows the frontend to interact with the database and perform all necessary operations. The API should be
well-designed and follow best practices for RESTful API design, including proper use of HTTP methods, status codes,

There shall be a separate api module in the backend project which contains all the necessary code for any other service to connect to the service. This
means that the module contains the interface definitions which the controllers then implement, as well as data transfer objects (DTOs) and any enums used in
them. All possible annotation should be on the interface leaving the controllers as clutter free as possible.

In addition, the interfaces as well as the DTOs should be annotated with the necessary annotations to generate OpenAPI documentation for the API in addition to
also have the necessary annotations from jakarta.validation.

This separation of concerns helps to keep the codebase organized and maintainable, making it easier for developers to understand and
work with the API code. The API should be well-documented, with clear and concise documentation for each endpoint, including the expected input and output, as
well as any relevant error codes or messages. This documentation should be easily accessible to developers who are working with the API, and should be kept
up-to-date as the API evolves over time. The API should also include proper error handling and validation to ensure that it is robust and can handle unexpected
situations gracefully. This includes returning appropriate error codes and messages when invalid input is received, as well as handling any exceptions that may

#### Frontend types

The frontend code should always have the necessary types defined for all data structures used in the codebase. This includes defining types for API responses,
component props, and any other data structures used in the frontend. This helps to ensure that the code is type-safe and can catch potential issues at compile
time, rather than at runtime. The types should be defined in a clear and organized manner, making it easy for developers to understand and work with the
codebase. The types should also be kept up-to-date as the codebase evolves to ensure that they remain accurate and useful for developers working on the
frontend. This is especially important in a TypeScript codebase, where the use of types is a key feature that helps to improve code quality and maintainability.
By ensuring that all data structures have the necessary types defined, we can help to prevent bugs and improve the overall quality of the frontend codebase,
making it easier for developers to work with and maintain over time. This means that nowhere in the frontend code should there be any use of the `any` type, and
all data structures should have clear and accurate types defined. This includes defining types for API responses, component props, and any other data structures
used in the frontend. By following this practice, we can help to ensure that the frontend codebase is robust, maintainable, and easy to work with for developers
of all skill levels.

## Translations

Both the frontend and the backend should support multiple languages. Specifically the following languages should be supported: de, en, es, fi and sv.
The frontend UI should use react-i18next to handle all translations pertaining to the frontend functionality and the backend should likewise support the listed
languages in all the other communication such as emails with the user. The backend can use spring-boot-starter-thymeleaf for email templates in different
languages.

## Documentation

The codebase should be well-documented with clear and concise comments explaining the purpose and functionality of each component, function, and class. In
addition, the functions/methods should be documented using JSDoc for the frontend and JavaDoc for the backend to provide clear documentation for other
developers
who may work on the project in the future. The documentation should be kept up-to-date as the codebase evolves to ensure that it remains accurate and useful.

Finally there should be separate user and administration documentation located in the main documentation folder. The user documentation should provide clear
instructions on how to use the application, including how to register, log in, manage cylinders and gas fills, and any other relevant features. The
administration documentation should provide clear instructions on how to manage the application, including how to approve or reject user registrations, manage
user accounts, and any other relevant administrative features. Both sets of documentation should be written in a clear and concise manner, with step-by-step
instructions and screenshots where appropriate to help users and administrators understand how to use the application effectively. The documentation is done in
Markdown format and should be stored in the main documentation folder of the project. This documentation should as well be kept up-to-date as the application
evolves to ensure that it remains accurate and useful for users and administrators. This task should always be a part of the overall task performed by the
AI agents.

## Testing

The frontend code should be covered by unit test using Jest and React Testing Library. In addition, ESLint should be used to ensure code quality and
consistency.
The tests should be run automatically as part of the CI/CD pipeline to ensure that any issues are identified and addressed quickly. The tests should cover all
critical functionality of the frontend, including user interactions, state management, and API calls to the backend. The tests should also cover edge cases and
error handling to ensure that the frontend is robust and can handle unexpected situations gracefully.

The backend code should be covered by unit tests using JUnit and Mockito. Additionally, integration tests should be implemented to test the interaction between
the backend and the database, as well as end-to-end tests to test the entire application flow from the frontend to the backend and back. Testcontainers should
be used for integration testing to ensure that the tests are run in an environment that closely resembles the production environment. This will help to identify
any issues that may arise due to differences in the environment, such as database configuration or network settings. The tests should be run automatically as
part of the CI/CD pipeline to ensure that any issues are identified and addressed quickly.

The tests should be always be included in any AI agent task that involves modifying the codebase to ensure that the changes do not introduce any new issues or
regressions. The tests should be comprehensive and cover all critical functionality of the backend, including authentication and authorization, user management,
and data management. The tests should also cover edge cases and error handling to ensure that the backend is robust and can handle unexpected situations
gracefully. By including tests in every task, we can ensure that the codebase remains stable and reliable as it evolves over time.
