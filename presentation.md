# Domain-Driven Design (DDD) на практике

## Что такое DDD?

Domain-Driven Design - это подход к разработке программного обеспечения, который фокусируется на моделировании предметной области и использовании универсального языка.

## Основные концепции DDD

### 1. Универсальный язык (Ubiquitous Language)
```java
// Пример из нашего кода
public enum OrderStatus {
    CREATED,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
```

### 2. Ограниченные контексты (Bounded Contexts)
В нашем примере:
- Order Management (управление заказами)
- Payment Processing (обработка платежей)

### 3. Агрегаты (Aggregates)
Агрегаты - это кластеры доменных объектов, которые должны обрабатываться как единое целое.

#### Основные характеристики агрегатов:
1. **Корень агрегата (Aggregate Root)**
   - Единственная точка входа для доступа к объектам внутри агрегата
   - Гарантирует целостность данных
   - В нашем примере `Order` является корнем агрегата

2. **Инварианты**
   - Правила, которые всегда должны выполняться
   - Пример из нашего кода:
   ```java
   public Order confirm() {
       if (status != OrderStatus.CREATED) {
           throw new IllegalStateException("Order can only be confirmed when in CREATED status");
       }
       return new Order(id, customerId, OrderStatus.CONFIRMED, totalAmount);
   }
   ```

3. **Границы транзакций**
   - Агрегат - это граница транзакции
   - Все изменения внутри агрегата атомарны
   - В нашем примере: создание заказа, подтверждение заказа - атомарные операции

#### Пример агрегата Order:
```java
@Value
public class Order {
    UUID id;
    String customerId;
    OrderStatus status;
    Money totalAmount;
    
    // Конструктор для создания нового заказа
    public Order(String customerId, Money totalAmount) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.status = OrderStatus.CREATED;
        this.totalAmount = totalAmount;
    }

    // Приватный конструктор для внутренних операций
    private Order(UUID id, String customerId, OrderStatus status, Money totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    // Доменная операция - подтверждение заказа
    public Order confirm() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order can only be confirmed when in CREATED status");
        }
        return new Order(id, customerId, OrderStatus.CONFIRMED, totalAmount);
    }
}
```

#### Правила работы с агрегатами:
1. Всегда обращайтесь к объектам через корень агрегата
2. Сохраняйте целостность агрегата
3. Используйте неизменяемые объекты (как в нашем примере с `@Value`)
4. Следите за границами агрегата

### 4. Value Objects
```java
@Value
public class Money {
    BigDecimal amount;
    String currency;
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
}
```

### 5. Службы предметной области (Domain Services)
Службы предметной области используются, когда операция не принадлежит естественным образом ни одному из объектов предметной области.

#### Когда использовать доменные сервисы:
1. **Операция относится к нескольким агрегатам**
   ```java
   public class OrderDomainService {
       public boolean canCustomerCreateOrder(String customerId) {
           // Проверяет состояние нескольких заказов
           // и применяет бизнес-правила
       }
   }
   ```

2. **Операция не имеет естественного места в агрегате**
   ```java
   public class OrderDomainService {
       public Money calculateDiscount(String customerId, Money orderAmount) {
           // Рассчитывает скидку на основе
           // истории заказов клиента
       }
   }
   ```

3. **Операция требует доступа к внешним ресурсам**
   - Интеграция с другими системами
   - Работа с внешними API
   - Доступ к базе данных

#### Характеристики доменных сервисов:
1. **Отсутствие состояния**
   - Сервисы не хранят данные
   - Все данные передаются через параметры

2. **Высокая связность**
   - Сервис решает одну конкретную задачу
   - Имеет четкие границы ответственности

3. **Интеграция моделей**
   - Может работать с несколькими агрегатами
   - Координирует взаимодействие между ними

#### Пример использования в юзкейсе:
```java
@RequiredArgsConstructor
public class CreateOrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;

    public Order execute(String customerId, BigDecimal amount, String currency) {
        // Проверяем возможность создания заказа
        if (!orderDomainService.canCustomerCreateOrder(customerId)) {
            throw new IllegalStateException("Customer cannot create new order");
        }

        Money totalAmount = Money.of(amount, currency);
        // Рассчитываем скидку
        Money discount = orderDomainService.calculateDiscount(customerId, totalAmount);
        Money finalAmount = totalAmount.subtract(discount);

        Order order = new Order(customerId, finalAmount);
        return orderRepository.save(order);
    }
}
```

## Слои DDD

### 1. Доменный слой (Domain Layer)
- Содержит бизнес-логику
- Не зависит от других слоев
- Включает:
  - Агрегаты
  - Value Objects
  - Доменные сервисы
  - Репозитории (интерфейсы)

### 2. Слой приложения (Application Layer)
- Координирует работу домена
- Содержит юзкейсы
```java
@RequiredArgsConstructor
public class CreateOrderUseCase {
    private final OrderRepository orderRepository;

    public Order execute(String customerId, BigDecimal amount, String currency) {
        Money totalAmount = Money.of(amount, currency);
        Order order = new Order(customerId, totalAmount);
        return orderRepository.save(order);
    }
}
```

### 3. Инфраструктурный слой (Infrastructure Layer)
- Реализует технические детали
- Включает:
  - Репозитории
  - Внешние сервисы
  - Базы данных

## Преимущества DDD

1. **Чистая архитектура**
   - Разделение ответственности
   - Независимость от фреймворков

2. **Поддерживаемость**
   - Понятная структура
   - Легкое тестирование

3. **Масштабируемость**
   - Независимые ограниченные контексты
   - Возможность микросервисной архитектуры

## Практические советы

1. Начните с моделирования домена
2. Используйте универсальный язык
3. Определите границы контекстов
4. Следуйте принципам SOLID
5. Пишите тесты

## Заключение

DDD - это не только архитектура, но и способ мышления о разработке программного обеспечения, который помогает создавать более качественные и поддерживаемые системы. 