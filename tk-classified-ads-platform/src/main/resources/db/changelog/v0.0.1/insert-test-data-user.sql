-- liquibase formatted sql

-- Password '12345678' is encoded to '$2a$10$dNwSTUHdpy8BEEgtQvWKLuhfh2rNSSJIZRG3PieITjsDPcphmGGoi';

-- changeSet kostusonline:daec8310-ef97-5f56-92b5-ed9afd3ca934 runOnChange:true
INSERT INTO
    "user" (
        "name",
        "password",
        "first_name",
        "last_name",
        "phone",
        "role"
    )
VALUES (
        'anton@anton.net',
        '$2a$10$dNwSTUHdpy8BEEgtQvWKLuhfh2rNSSJIZRG3PieITjsDPcphmGGoi',
        'Anton',
        'Antonov',
        '+79223334455',
        'ADMIN'
    ),
    (
        'ivan@ivan.da',
        '$2a$10$dNwSTUHdpy8BEEgtQvWKLuhfh2rNSSJIZRG3PieITjsDPcphmGGoi',
        'Ivan',
        'Ivanov',
        '+79223334466',
        'USER'
    );