-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.

-- Notes for "avatar" table:
-- avatar image data stored separately (disk/catalog/etc.).

-- changeSet kostusonline:771af808-a117-5daa-bcd2-e3ad6fba461e runOnChange:true
CREATE TABLE IF NOT EXISTS "avatar" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT NOT NULL UNIQUE,
    "name" VARCHAR(256) NOT NULL UNIQUE CHECK (
        LENGTH("name") BETWEEN 1 AND 256
    ),
    "size" BIGINT NOT NULL CHECK (
        "size" > 0
        AND "size" <= 10000000
    ),
    "mediatype" VARCHAR(128) NOT NULL CHECK (
        LENGTH("mediatype") BETWEEN 3 AND 128
    ),
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
);

--changeSet kostusonline:82f32d36-7208-5b50-a0bd-d961580f8937 runOnChange:true
GRANT ALL ON "avatar" TO ads_god;
