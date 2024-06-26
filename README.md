## java-client-Service

### Запуск проекта


Для запуска проекта необходимо установить **Docker**, чтобы поднять контейнер с необходимыми технологиями.
После установки Docker выполните следующие команды в терминале:

`docker-compose up`

Для запуска в фоновом режиме используйте:

`docker-compose up -d`.

**ПЕРЕД ЗАПУСКОМ ПРОЕКТА**

В `application.yaml` установить `jwt.secret-key` ключ для генерации **JWT** токена

### Информация о проекте

Этот проект (микросервис) был создан для удовольствия и предоставляет следующие возможности:
- Редактирование/Получение инфомации о клиенте: `ClientController.java`
- Получение Session Token для Верификации пользовоталя установка/смена пароля, смена почты/телефона: `SessionController.java`
- Регистрация/Аунтификация/Авторизация клиента в сервисе: `UserController.java`

Автоматическая загрузка тестовых данных бд при помощи `liquid`.

Написанные интеграционные и модульные тесты `Mockito` `JUnit 5`

Для проверки функциональности можно использовать `Postman` [ссылка на Postman API](https://documenter.getpostman.com/view/31895087/2sA35G21aa)

### Стек проекта:

- Java 17
- Spring Framework (Boot, Security, Rest)
- PostgreSQL
- JUnit 5
- Mockito
- Hibernate
- Liquibase
- Docker
- Gradle
- JWT
- Swagger
- Postman
- Git
- REST API