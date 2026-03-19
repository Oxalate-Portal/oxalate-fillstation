CREATE TABLE cylinders (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name             VARCHAR(255)    NOT NULL,
    volume           NUMERIC(10, 2)  NOT NULL,
    working_pressure NUMERIC(10, 2)  NOT NULL,
    serial_number    VARCHAR(255)
);

CREATE INDEX idx_cylinders_user_id ON cylinders (user_id);
