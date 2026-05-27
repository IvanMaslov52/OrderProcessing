# Order Processing System — Starter Template

Минимальный шаблон микросервисного проекта для самостоятельной разработки.

## Структура
```
order-processing-simple/
├── docker-compose.yml        # вся инфраструктура одной командой
├── eureka-server/            # реестр сервисов (готов к запуску)
├── api-gateway/              # точка входа + роутинг
├── user-service/             # сервис пользователей (PostgreSQL)
├── order-service/            # сервис заказов (PostgreSQL + Redis + Kafka)
└── notification-service/     # сервис уведомлений (MongoDB + Kafka)
```

## Порты
| Сервис              | Порт |
|---------------------|------|
| Eureka              | 8761 |
| API Gateway         | 8000 |
| User Service        | 8081 |
| Order Service       | 8082 |
| Notification Service| 8083 |
| Keycloak            | 8080 |
| PostgreSQL          | 5432 |
| MongoDB             | 27017|
| Redis               | 6379 |
| Kafka               | 9092 |

## Быстрый старт

### 1. Поднять инфраструктуру
```bash
docker-compose up -d
```

### 2. Запустить сервисы в IDE
Запускать в порядке:
1. eureka-server
2. api-gateway
3. user-service, order-service, notification-service (любой порядок)

Проверить реестр: http://localhost:8761

## Что реализовать самостоятельно (TODO в коде)

### user-service
- [ ] Модель User (JPA Entity)
- [ ] UserRepository
- [ ] UserService + UserController
- [ ] Flyway миграция V1__create_users_table.sql

### order-service
- [ ] Модель Order + OrderStatus
- [ ] OrderRepository
- [ ] OrderService + OrderController
- [ ] Redis кэш с @Cacheable + Jitter TTL (RedisConfig)
- [ ] Kafka Producer → топик order.created
- [ ] Flyway миграция V1__create_orders_table.sql

### notification-service
- [ ] Модель Notification (MongoDB Document)
- [ ] NotificationRepository
- [ ] Kafka Consumer (@KafkaListener на order.created)
- [ ] Idempotency (уникальный индекс по orderId + type)
- [ ] NotificationController

### api-gateway
- [ ] Роуты в application.yml
- [ ] SecurityConfig (JWT валидация через Keycloak)
- [ ] 401 vs 403 (AuthenticationEntryPoint / AccessDeniedHandler)
