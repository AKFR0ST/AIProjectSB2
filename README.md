# CP Tasks Service

## Описание проекта
Сервис для обработки коммерческих предложений (КП).

Позволяет:
- создавать задачи на обработку файлов
- получать статус обработки
- извлекать данные
- получать ТОП-5 поставщиков

---

## Технологии

- Java 21
- Spring Boot 4
- Spring Security
- PostgreSQL
- Docker / Docker Compose
- Swagger (OpenAPI)
- JUnit + Mockito

---

## Архитектура

- Controller — REST API
- Service — бизнес-логика
- Repository — доступ к БД
- Entity — модель данных
- DTO — ответы API
- ExceptionHandler — обработка ошибок

---

## Запуск

### 1. Сборка

```bash
mvn clean package
```
### 2. Запуск
```bash
docker-compose up --build
```

## Доступ к сервису

После запуска приложения сервис доступен по адресу:

- REST API: http://localhost:8088
- Swagger UI: http://localhost:8088/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8088/v3/api-docs

---

## Аутентификация и авторизация

В проекте используется HTTP Basic Authentication с ролевой моделью доступа.

### Пользователи:

| Роль   | Логин  | Пароль   |
|--------|--------|----------|
| USER   | user   | password  |
| ADMIN  | admin  | admin     |

---

### Права доступа:

**USER:**
- создание задачи обработки (`POST /cp/tasks`)
- получение задачи (`GET /cp/tasks/{id}`)

**ADMIN:**
- доступ к аналитике поставщиков (`GET /cp/tasks/{id}/top`)

---

##  API примеры

###  Создание задачи

```bash
curl -X POST "http://localhost:8088/cp/tasks?fileName=test.pdf" \
-u user:password
```
Ответ:
```JSON
{
"taskId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
"status": "CREATED"
}
```

###  Получение задачи по ID

```bash
curl -X GET "http://localhost:8088/cp/tasks/{taskId}" \
-u user:password
```

###  Получение TOP-5 поставщиков (ADMIN)

```bash
curl -X GET "http://localhost:8088/cp/tasks/{taskId}/top" \
-u admin:admin
```

##  Обработка ошибок

Все ошибки возвращаются в едином формате:

```JSON
{
  "status": 404,
  "error": "Task not found",
  "timestamp": "2026-04-16T12:50:35"
}
```

Типовые коды:

400 — некорректный запрос
401 — не авторизован
403 — недостаточно прав
404 — сущность не найдена
500 — внутренняя ошибка сервера

##  Тестирование
Запуск unit-тестов:

```bash
mvn test
```

Покрытие включает:

- бизнес-логику сервиса
- обработку ошибок
- выбор TOP-5 поставщиков
- сценарии успешной и неуспешной обработки задач

##  Принятые компромиссы

В рамках тестового задания были приняты следующие упрощения:

- Используется mock-парсер вместо реального AI/ML обработки файлов
- @Async без очередей (Kafka/RabbitMQ)
- Basic Auth вместо JWT/OAuth2
- JSON хранится в PostgreSQL в поле jsonb как строка
- Hibernate auto-DDL вместо миграций (Flyway/Liquibase)

##  Возможные улучшения

- Переход на JWT-аутентификацию
- Добавление очередей (Kafka / RabbitMQ) для обработки задач
- Валидация и загрузка реальных файлов (Multipart)
- Кэширование топ-поставщиков (Redis)
- Разделение сервиса на микросервисы при росте нагрузки