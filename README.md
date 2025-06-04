# Clean DDD Example

Этот проект демонстрирует применение принципов Domain-Driven Design (DDD) в Java-приложении с использованием Spring Boot и REST API.

## Требования

- Java 17
- Gradle

## Структура проекта

Проект следует принципам DDD и имеет следующую структуру:

```
src/main/java/com/example/
├── domain/           # Доменный слой (агрегаты, value objects, доменные сервисы, интерфейсы репозиториев)
│   └── order/
├── application/      # Слой приложения (юзкейсы)
│   └── order/
├── infrastructure/   # Инфраструктурный слой (реализации репозиториев и интеграции)
│   └── persistence/
├── presentation/     # Презентационный слой (REST API, DTO, контроллеры)
│   └── order/
└── Application.java  # Точка входа (Spring Boot)
```

## Слои DDD

- **Presentation Layer** — REST API, DTO, контроллеры
- **Application Layer** — юзкейсы, оркестрация
- **Domain Layer** — бизнес-логика, агрегаты, value objects, доменные сервисы
- **Infrastructure Layer** — интеграция с БД, внешними сервисами

## Примеры запросов

### Создать заказ
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"customer123","amount":100.00,"currency":"USD"}'
```

### Подтвердить заказ
```bash
curl -X POST http://localhost:8080/api/orders/{orderId}/confirm
```

## Запуск проекта

```bash
./gradlew bootRun
```

## Тестирование

```bash
./gradlew test
```