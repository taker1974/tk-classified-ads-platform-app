# tk-common-controller-advice

## About the module

Common module for implementing `@ControllerAdvice`.  
Spring Boot, Java.

## Deployment preparation

**Prerequisites**:

Software versions may vary. Development is done using Java 21: there are no explicit restrictions on using other software versions.  

Install JDK or JRE version 21 or higher (development is done on Java 21; there are no explicit restrictions on using other Java versions).  
Verify the installation, ensure `java`, `javac`, and `maven` (`mvn`) are available.  

## Dependency versions

```bash
mvn versions:display-dependency-updates
```

The _versions-maven-plugin_ allows automatic version updates in pom.xml:

```bash
mvn versions:use-latest-versions
```

## Build

See pom.xml. In the module root:

```Bash
mvn clean install
mvn compile javadoc:javadoc
```

```bash
mvn versions:display-dependency-updates
```
