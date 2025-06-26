# tk-classified-ads-platform

## О приложении

Основной запускаемый проект доски объявлений с возможностью использования медиа, с комментированием.  
Поиск, CRUD.  

## Применяемые технологии

Java - openjdk 21,  
Spring Boot 3.5.3, Lombok,  
Mockito,  
JavaDoc, Swagger,  
Liquibase, PostgreSQL, Caffein,  
Spring Security.

## Подготовка к развёртыванию, запуск, документирование

Смотри [../ReadMe.md](https://github.com/taker1974/tk-classified-ads-platform-app/blob/main/ReadMe.md)

## Особенности текущей реализации

Enum на уровне БД транслируются в строки, но это рекомендуемый способ представления enum.

В методах сервисного слоя больше не применяются аннотации валидации (такие "валидации" - антипаттерн для Spring Boot). Вместо этого выполняются явные проверки на null.
