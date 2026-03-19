CREATE TABLE fill_entries (
    id                   BIGSERIAL      PRIMARY KEY,
    user_id              BIGINT         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    cylinder_id          BIGINT         NOT NULL REFERENCES cylinders (id),
    fill_date            DATE           NOT NULL,
    start_pressure       NUMERIC(10, 2) NOT NULL,
    end_pressure         NUMERIC(10, 2) NOT NULL,
    start_o2_percentage  NUMERIC(6, 2)  NOT NULL DEFAULT 0,
    start_he_percentage  NUMERIC(6, 2)  NOT NULL DEFAULT 0,
    end_o2_percentage    NUMERIC(6, 2)  NOT NULL DEFAULT 0,
    end_he_percentage    NUMERIC(6, 2)  NOT NULL DEFAULT 0,
    o2_added             NUMERIC(12, 4),
    he_added             NUMERIC(12, 4),
    gas_added            NUMERIC(12, 4),
    notes                TEXT,
    status               VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    created_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_fill_entries_user_id     ON fill_entries (user_id);
CREATE INDEX idx_fill_entries_cylinder_id ON fill_entries (cylinder_id);
CREATE INDEX idx_fill_entries_status      ON fill_entries (status);
