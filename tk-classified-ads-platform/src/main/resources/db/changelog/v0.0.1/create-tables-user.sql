-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.

-- changeSet kostusonline:ad2be79b-2fcd-5496-8296-b5223f07e1ae runOnChange:true
CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(32) UNIQUE NOT NULL CHECK (
        LENGTH("name") BETWEEN 4 AND 32
    ),
    "password" VARCHAR(256) NOT NULL CHECK (
        LENGTH("password") BETWEEN 8 AND 256
    ),
    "first_name" VARCHAR(16) NOT NULL CHECK (
        LENGTH("first_name") BETWEEN 2 AND 16
    ),
    "last_name" VARCHAR(16) NOT NULL CHECK (
        LENGTH("last_name") BETWEEN 2 AND 16
    ),
    "phone" VARCHAR(16) UNIQUE NOT NULL CHECK (
        LENGTH("phone") BETWEEN 11 AND 16
    ),
    "role" VARCHAR(20) NOT NULL CHECK (
        LENGTH("role") BETWEEN 1 AND 20
    )
);

--changeSet kostusonline:f2feed28-7fab-52c6-b023-f64112ffebc2 runOnChange:true
GRANT ALL ON "user" TO ads_god;

-- changeSet kostusonline:328fb348-d607-5651-9610-f410e9c955ab runOnChange:true
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_name ON "user" ("name");