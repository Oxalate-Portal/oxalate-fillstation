CREATE TABLE configuration (
    id           BIGSERIAL PRIMARY KEY,
    group_name   VARCHAR(100) NOT NULL,
    config_key   VARCHAR(100) NOT NULL,
    config_value TEXT         NOT NULL,
    UNIQUE (group_name, config_key)
);

CREATE INDEX idx_configuration_group ON configuration (group_name);
