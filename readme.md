# Order Processing System

Учебный микросервисный проект на Spring Boot для отработки Middle+ навыков.

## Стек
- Java 17, Spring Boot 3.2, Spring Cloud 2023
- PostgreSQL + Flyway (user-service, order-service)
- MongoDB (notification-service)
- Redis (кэш + distributed lock)
- Kafka (асинхронные события)
- Keycloak (аутентификация)
- Eureka (service discovery)
- Spring Cloud Gateway

## Сервисы и порты
| Сервис | Порт |
|--------|------|
| Eureka Server | 8761 |
| API Gateway | 8000 |
| User Service | 8081 |
| Order Service | 8082 |
| Notification Service | 8083 |
| Keycloak | 8080 |
| PostgreSQL | 5432 |
| MongoDB | 27017 |
| Redis | 6379 |
| Kafka | 9092 |

## Быстрый старт

### 1. Поднять инфраструктуру
```bash
docker-compose up -d postgres mongodb redis kafka zookeeper keycloak
```

### 2. Настроить Keycloak
- Открыть http://localhost:8080
- Войти admin/admin
- Создать Realm: `user_service`
- Создать Client: `springBootApplication` (type: confidential)
- Создать тестового пользователя

### 3. Запустить сервисы (в порядке)
```bash
# Собрать все модули
cd eureka-server && mvn clean package -DskipTests
cd ../api-gateway && mvn clean package -DskipTests
cd ../user-service && mvn clean package -DskipTests
cd ../order-service && mvn clean package -DskipTests
cd ../notification-service && mvn clean package -DskipTests

# Или запустить через Docker Compose полностью:
docker-compose up --build
```
