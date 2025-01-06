# 프로젝트 시작

## https://start.spring.io/

Developer Tools

- Spring Configuration Processor
- Spring Boot DevTools

Web

- Spring Web
- Spring Reactive Web

SQL

- Spring Data JPA
- MariaDB Driver
- H2 Database

NOSQL

- Spring Data Redis

Security

- Spring Security
- OAuth2 Client

I/O

- Validation

Project

- Gradle - Kotlin

Language

- Kotlin

## 압축 풀기

```
git clone ...
cd papaya
unzip -o api.zip "api/*" && mv -f api/{.,}* . 2>/dev/null ; rm -r api
```

## application-local.yml

```yml
spring:
  application:
    name: api
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: "jdbc:mariadb://localhost/db"
    username: test
    password: test
    hikari:
      connectionInitSql: "SET NAMES utf8mb4"

  jpa:
    hibernate:
      ddl-auto: validate # validate, create, create-drop., update, none
    # `hibernate.ddl-auto` 설정이 `generate-ddl`보다 우선하므로 설정이 무시됨
    # generate-ddl: true
    show-sql: true
    open-in-view: false # 허용 시 뷰 렌더링 중에도 지연 로딩 쿼리가 실행될 수도 있음
logging:
  level:
    root: INFO
```

## 주요 의존성

* QueryDSL
* JWT
* Netty DNS Resolver