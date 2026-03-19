CREATE TABLE login_history (
    id         BIGSERIAL    PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    login_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(100)
);

CREATE INDEX idx_login_history_user_id ON login_history (user_id);
