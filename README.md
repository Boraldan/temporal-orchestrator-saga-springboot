## Тестовый стенд: Temporal оркестратор + Saga компенсации в распределенных транзакциях

#### Stack: Java 21, Spring Boot 3, Temporal, REST API, PostgreSQL, Liquibase, MapStruct, Maven, Docker.

Этот сервис реализует бизнес-логику обработки заказов с использованием **Temporal Workflow**.
Он управляет полным жизненным циклом заказа, включая создание заказа, оплату, отправку и компенсацию в случае ошибок.

### при первом запуске, создаем namespace через CLI в контейнере temporal-admin-tools:
    > tctl --ns order-ns namespace register -rd 5

Этапами процесса заказа:

1. **Order Service**
    - Создание заказа (`createOrder`)
    - Обновление статуса заказа (`updateOrderStatus`)
    - Отмена заказа (`cancelOrder`) при необходимости

2. **Payment Service**
    - Создание платежа (`createPayment`)
    - Списание средств (`chargePayment`)
    - Отмена платежа (`cancelPayment`) при ошибках

3. **Shipping Service**
    - Создание доставки (`createShipping`)
    - Оплата и отправка (`chargeShipping`)
    - Отмена доставки (`cancelShipping`, `cancelShippingById`) при ошибках

## Управление процессом

Workflow реализует **Saga** — механизм компенсации действий при сбоях.  
Если на любом этапе процесса возникает ошибка, выполняются заранее определённые компенсационные действия для отмены уже выполненных шагов, обеспечивая консистентность данных.

## Docker Compose

В данной конфигурации поднимаются следующие контейнеры:

### Temporal Stack

- **temporal**  
  Сервер Temporal.

- **postgresql (temporal-postgresql)**  
  База данных PostgreSQL для хранения состояния Temporal.

- **temporal-admin-tools**  
  CLI-инструменты для администрирования Temporal.

- **temporal-ui**  
  UI для мониторинга и управления Workflow. Доступен на [http://localhost:8080](http://localhost:8080).

### Базы данных микросервисов

- **postgres-order-outbox**  
  PostgreSQL для реализации паттерна *Outbox* в сервисе api.

- **postgres-order**  
  PostgreSQL для **Order Service**.

- **postgres-payment**  
  PostgreSQL для **Payment Service**.

- **postgres-shipping**  
  PostgreSQL для **Shipping Service**.  
