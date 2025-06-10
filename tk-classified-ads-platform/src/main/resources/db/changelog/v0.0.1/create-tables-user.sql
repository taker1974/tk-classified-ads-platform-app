-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.

-- changeSet kostusonline:b457e5d2-6edb-547e-ad28-4a81f76ddfc6 runOnChange:true
CREATE TABLE IF NOT EXISTS "user_role" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(32) NOT NULL UNIQUE CHECK (
        LENGTH("name") BETWEEN 1 AND 32
    )
);

-- changeSet kostusonline:6491d60e-f52f-5209-aa5b-2fc9079183e1 runOnChange:true
GRANT ALL ON "user_role" TO ads_god;

-- changeSet kostusonline:e78bcab3-333f-588d-a4f4-40de92387b29 runOnChange:true
INSERT INTO "user_role" ("name") VALUES ('ADMIN'), ('USER');

-- changeSet kostusonline:39a8625d-146e-55ed-970e-861b110ab2fa runOnChange:true
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_role ON "user_role" ("name");

-- changeSet kostusonline:ad2be79b-2fcd-5496-8296-b5223f07e1ae runOnChange:true
CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(32) UNIQUE NOT NULL CHECK (
        LENGTH("name") BETWEEN 4 AND 32
    ),
    "password" VARCHAR(16) NOT NULL CHECK (
        LENGTH("password") BETWEEN 8 AND 16
    ),
    "first_name" VARCHAR(16) NOT NULL CHECK (
        LENGTH("first_name") BETWEEN 2 AND 16
    ),
    "last_name" VARCHAR(16) NOT NULL CHECK (
        LENGTH("last_name") BETWEEN 2 AND 16
    ),
    "phone" VARCHAR(16) UNIQUE NOT NULL CHECK (
        LENGTH("last_name") BETWEEN 11 AND 16
    ),
    "role_id" BIGINT NOT NULL,
    FOREIGN KEY ("role_id") REFERENCES "user_role" ("id")
);

--changeSet kostusonline:f2feed28-7fab-52c6-b023-f64112ffebc2 runOnChange:true
GRANT ALL ON "user" TO ads_god;

-- changeSet kostusonline:328fb348-d607-5651-9610-f410e9c955ab runOnChange:true
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_name ON "user" ("name");