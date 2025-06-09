-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.
-- Let's assume that we need to find users by name quickly and inserting/updating/deleting is rare.
-- We can create indecies for email/phone but it is not necessary: we creating just test application.

-- changeSet kostusonline:5dbb8585-6aae-5840-8a41-c3eec697fbbd runOnChange:true
CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(500) NOT NULL UNIQUE CHECK (
        LENGTH("name") BETWEEN 1 AND 500
    ),
    "password" VARCHAR(500) NOT NULL CHECK (
        LENGTH("password") BETWEEN 8 AND 500
    ),
    "date_of_birth" DATE CHECK (
        0 <= EXTRACT(
            YEAR
            FROM CURRENT_DATE
        ) - EXTRACT(
            YEAR
            FROM "date_of_birth"
        )
    )
);

--changeSet kostusonline:3b057740-531a-5897-9957-1730342f7779 runOnChange:true
GRANT ALL ON "user" TO bank_god;

-- changeSet kostusonline:8595f694-9670-5390-8403-253748d9f502 runOnChange:true
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_name ON "user" ("name");

-- changeSet kostusonline:50c56d6b-cc98-5b61-a80b-88949b849795 runOnChange:true
CREATE TABLE IF NOT EXISTS "account" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT UNIQUE NOT NULL, -- yes, unique here is ok
    "balance" DECIMAL NOT NULL DEFAULT 0 CHECK ("balance" >= 0),
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
);

-- changeSet kostusonline:0867f204-5570-5f3f-9507-5748f3710802 runOnChange:true
GRANT ALL ON "account" TO bank_god;

-- changeSet kostusonline:86d5d074-000b-5488-9778-0006357b028b runOnChange:true
CREATE TABLE IF NOT EXISTS "email_data" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT NOT NULL,
    "email" VARCHAR(200) NOT NULL UNIQUE CHECK (
        LENGTH("email") BETWEEN 5 AND 200
    ),
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
);

-- changeSet kostusonline:6165f950-0862-550d-8379-9228054f9276 runOnChange:true
GRANT ALL ON "email_data" TO bank_god;

-- changeSet kostusonline:0f862505-9f91-5610-9534-09d838669300 runOnChange:true
CREATE TABLE IF NOT EXISTS "phone_data" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT NOT NULL,
    "phone" VARCHAR(13) NOT NULL UNIQUE CHECK (
        LENGTH("phone") BETWEEN 11 AND 13
    ),
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
);

-- changeSet kostusonline:84584914-7991-518b-920e-208d27135709 runOnChange:true
GRANT ALL ON "phone_data" TO bank_god;
