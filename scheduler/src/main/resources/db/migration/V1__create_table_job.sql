CREATE TABLE job (
    id SERIAL PRIMARY KEY,
    uuid VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL UNIQUE,
    version SMALLINT NOT NULL,
    cron VARCHAR NOT NULL,
    action VARCHAR NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    UNIQUE(name, version, active)
);