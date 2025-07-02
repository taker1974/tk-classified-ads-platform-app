# tk-classified-ads-platform

## О приложении

Основной запускаемый проект доски объявлений с возможностью использования медиа, с комментированием.  
Поиск, CRUD.  

## Применяемые технологии

Java 21, openjdk,
Spring Boot 3.5.3, Lombok,  
Liquibase, PostgreSQL, Caffeine,  
Spring Security,
Mockito, JUnit, Testcontainers,
JavaDoc, Swagger,
Fedora Linux 42, MS VS Code,
GigaCode: на уровне Intellisense,  
DeepSeek: сложный поиск и сравнение, общие вопросы, рутина (описание БД в mermaid, тесты по описаниям), код-ревью.

## Подготовка к развёртыванию, запуск, документирование

Смотри [../ReadMe.md](https://github.com/taker1974/tk-classified-ads-platform-app/blob/main/ReadMe.md)

## Особенности текущей реализации

Enum на уровне БД транслируются в строки, но это рекомендуемый способ представления enum.

В методах сервисного слоя больше не применяются аннотации валидации (такие "валидации" - антипаттерн для Spring Boot). Вместо этого выполняются явные проверки на null.
