-- liquibase formatted sql

-- Generated token for Anton:
-- eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjo1LCJzdWIiOiJBbnRvbkBnbWFpbC5jb20iLCJpYXQiOjE3NDg1ODAwNjEsImV4cCI6MTc0ODY2NjQ2MX0.eUvDBPrhk6IVOVNJUyOp95atnkjxfLaYpEEVz4ii9xg

-- changeSet kostusonline:e49d6346-9ce6-56c6-b699-7a833edfa7e8 runOnChange:true
INSERT INTO
    "user" (
        "name",
        "date_of_birth",
        "password"
    )
VALUES (
        'Anton',
        '1980-12-30',
        '12345678'
    ),
    (
        'Ivan',
        '1990-03-15',
        '12345678'
    ),
    (
        'Olga',
        '1985-06-01',
        '12345678'
    ),
    (
        'Sergey',
        '1970-01-01',
        '12345678'
    );

-- changeSet kostusonline:b5f1b36e-4442-5324-a995-0592036b330b runOnChange:true
INSERT INTO
    "account" ("user_id", "balance")
SELECT u.id, 10.0
FROM "user" u;

-- changeSet kostusonline:eea18619-fa15-540c-b5b6-2b790c9f7f7f runOnChange:true
INSERT INTO
    "email_data" ("user_id", "email")
SELECT u.id, CONCAT(u.name, '@gmail.com')
FROM "user" u;

-- changeSet kostusonline:f9fd5a5a-d374-5e39-aee2-53f9e23f7d46 runOnChange:true
INSERT INTO
    "phone_data" ("user_id", "phone")
SELECT u.id, CONCAT('+7922333445', u.id)
FROM "user" u;