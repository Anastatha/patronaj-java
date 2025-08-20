--liquibase formatted sql
-- changeset table:005
ALTER TABLE "user"
    ADD COLUMN label labels[] NOT NULL DEFAULT ARRAY[]::labels[];