-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.

-- Notes for "ad" table:
-- price is a decimal value for convenience.

-- changeSet kostusonline:3081a2d1-6ba9-5534-a9e3-8c088577cf93 runOnChange:true
CREATE TABLE IF NOT EXISTS "ad" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT NOT NULL,
    "title" VARCHAR(32) NOT NULL CHECK (
        LENGTH("title") BETWEEN 4 AND 32
    ),
    "price" DECIMAL NOT NULL DEFAULT 0 CHECK (
        "price" >= 0
        AND "price" <= 10000000
    ),
    "description" VARCHAR(64) NOT NULL CHECK (
        LENGTH("description") BETWEEN 8 AND 64
    ),
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
);

--changeSet kostusonline:0ed358ea-f991-54c2-ba5c-9aebfc0fcfd5 runOnChange:true
GRANT ALL ON "ad" TO ads_god;
