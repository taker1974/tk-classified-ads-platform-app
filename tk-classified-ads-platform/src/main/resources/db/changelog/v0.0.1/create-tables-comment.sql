-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.

-- Notes for "comment" table:
-- at this time there's no chains of comments.

-- changeSet kostusonline:1f6a8f2c-e170-525e-9e98-38a8edc930ae runOnChange:true
CREATE TABLE IF NOT EXISTS "comment" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "ad_id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "created_at" DATE NOT NULL,
    "text" VARCHAR(64) NOT NULL CHECK (
        LENGTH("text") BETWEEN 8 AND 64
    ),
    FOREIGN KEY ("ad_id") REFERENCES "ad" ("id"),
    FOREIGN KEY ("user_id") REFERENCES "user" ("id")
);

--changeSet kostusonline:93304614-a08d-5322-8369-b2295288ef14 runOnChange:true
GRANT ALL ON "comment" TO ads_god;
