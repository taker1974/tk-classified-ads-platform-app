# tk-classified-ads-platform-app

## О приложении

Доска объявлений с возможностью использования медиа, с комментированием.  
Поиск, CRUD.  
Java, Spring Boot.

## Применяемые технологии

Java - openjdk 24

## Подготовка к развёртыванию на узле

**Требуемое ПО**:

- PostgreSQL >= 15;
- Java >= 21.

Версии ПО могут быть другими. При разработке используется Postgres 15 и 17 и Java 21 и 24.  
Нет явных ограничений на использование других версий ПО.

**Postgres**:

- предварительно смотрим application.yml;
- создаём БД tk_ads, создаём пользователя "ads_god" с паролем "87654321", отдаём БД tk_ads во владение пользователю ads_god:

```Bash
$ sudo -u postgres psql
postgres=# CREATE DATABASE tk_ads;
CREATE DATABASE
postgres=# CREATE USER ads_god WITH LOGIN PASSWORD '87654321';
CREATE ROLE
postgres=# ALTER DATABASE tk_ads OWNER TO ads_god;
ALTER DATABASE
```

**При реальном использовании приложения, при дальнейшей перепубликации, логины и пароли, разумеется, следует скрывать: как минимум их надо просто передавать через параметры командной строки при запуске приложения.**

**Java**:

- установить JDK или JRE версии не ниже 21 (на этой версии ведётся разработка: нет явных ограничений на использование других версий Java);
- убедиться в правильности установки, в доступности java, javac, maven (mvn);

## Запуск приложения

Смотри pom.xml. В корне проекта:

```Bash
$mvn clean install
$java -jar target/tk-classified-ads-platform-<ваша.версия.ПО>.jar
```

Смотри application.yml, context-path.  
По условию для приложения на выбранном порту используется путь "/".  

В браузере:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Документация

### Swagger -> HTML

redoc-cli был перемещен в состав @redocly/cli.  
Можно использовать npx без установки:

```bash
npx @redocly/cli build-docs <путь-к-openapi-файлу>
```

Генерирование статического HTML из Swagger на примере основного приложения tk-classified-ads-platform:

```Bash
curl http://localhost:<порт>/api-docs -o tk-classified-ads-platform-api-spec.json
npx @redocly/cli build-docs tk-classified-ads-platform-api-spec.json -o tk-classified-ads-platform-swagger.html 
```

### JavaDoc -> HTML

```Bash
mvn compile javadoc:javadoc
```

## Другое

### Проверка версий зависимостей

Добавьте плагин в pom.xml:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>versions-maven-plugin</artifactId>
            <version>2.16.2</version>
        </plugin>
    </plugins>
</build>
```

Запустите проверку:

```bash
mvn versions:display-dependency-updates
```

Плагин _versions-maven-plugin_ позволяет обновить версии в pom.xml автоматически:

```bash
mvn versions:use-latest-versions
```

После выполнения проверьте изменения в pom.xml.

**Ограничения**
SNAPSHOT-версии: Плагин _versions-maven-plugin_ игнорирует их по умолчанию.  
Используйте флаг -DallowSnapshots=true, чтобы их включить.

Кастомные репозитории: Если артефакт размещен не в Maven Central, убедитесь, что  
репозиторий добавлен в pom.xml/settings.xml.
