INSERT INTO users (name, email, password, language, status)
VALUES ('Administrator', 'admin@fillstation.local', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5lR6Z7x9fQ5M7Wf1JQY0LQ0KxYf4A1u', 'en', 'ACTIVE')
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.role_name = 'ROLE_ADMIN'
WHERE u.email = 'admin@fillstation.local'
ON CONFLICT (user_id, role_id) DO NOTHING;

