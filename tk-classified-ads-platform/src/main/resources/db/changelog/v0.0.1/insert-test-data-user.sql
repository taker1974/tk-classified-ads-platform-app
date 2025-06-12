-- liquibase formatted sql

-- changeSet kostusonline:daec8310-ef97-5f56-92b5-ed9afd3ca934 runOnChange:true
INSERT INTO
    "user" (
        "name",
        "password",
        "first_name",
        "last_name",
        "phone",
        "role_id"
    )
SELECT 'anton', '12345678', 'Anton', 'Antonov', '+79223334455', r.id
FROM "user_role" r
WHERE
    r.name = 'ADMIN';

-- changeSet kostusonline:f4554b25-3223-5042-ad3c-06f05c659445 runOnChange:true
INSERT INTO
    "user" (
        "name",
        "password",
        "first_name",
        "last_name",
        "phone",
        "role_id"
    )
SELECT 'ivan', '12345678', 'Ivan', 'Ivanov', '+79223334466', r.id
FROM "user_role" r
WHERE
    r.name = 'USER';
