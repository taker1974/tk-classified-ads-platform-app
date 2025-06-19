-- liquibase formatted sql

-- Notes:
-- Naming convention - see SQL Style Guide: snake_case, lowercase.

-- Notes for "image" table:
-- image data stored separately (disk/catalog/etc.).

-- changeSet kostusonline:cf3ff97a-6ab3-5f78-89f4-792055c27436 runOnChange:true
CREATE TABLE IF NOT EXISTS "image" (
    id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    "ad_id" BIGINT NOT NULL, -- no, not unique because of multiple images per ad allowed
    "name" VARCHAR(256) NOT NULL UNIQUE CHECK (
        LENGTH("name") BETWEEN 1 AND 256
    ),
    "size" BIGINT NOT NULL CHECK (
        "size" > 0
        AND "size" <= 10000000 -- 10MB per file is enough for most images
    ),
    "mediatype" VARCHAR(128) NOT NULL CHECK (
        LENGTH("mediatype") BETWEEN 3 AND 128
    ),
    FOREIGN KEY ("ad_id") REFERENCES "ad" ("id")
);

--changeSet kostusonline:fdc4b8e8-3712-5d9c-9a87-2b3305765cbf runOnChange:true
GRANT ALL ON "image" TO ads_god;
