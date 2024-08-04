--liquibase formatted sql

--changeset fetyukhin:audit-table

CREATE TABLE audit_logs(
    id UUID PRIMARY KEY,
    ip_address text,
    source_text text,
    translated_text text,
    status text,
    date timestamp
)