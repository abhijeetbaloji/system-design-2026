# Spring Boot Application Architecture: Online Food Delivery System

## 1. How the Java LLD Maps to Spring Boot

The core object-oriented low-level design maps directly to a clean, multi-layered Spring Boot 3 web application without polluting domain models with framework annotations:

```
[ HTTP Requests ]
       │
       ▼
┌────────────────────────────────────────────────────────┐
│ Controllers (REST Endpoints & HTTP Request Mapping)    │
│ RestaurantController, CartController, OrderController  │
└──────────────────────────┬─────────────────────────────┘
                           │ DTOs (Request / Response)
                           ▼
┌────────────────────────────────────────────────────────┐
│ Application Services (Use-Case Coordination)           │
│ RestaurantAppService, CartAppService, OrderAppService  │
└──────────────┬───────────────────────────┬─────────────┘
               │                           │
               ▼                           ▼
┌──────────────────────────────┐ ┌───────────────────────┐
│ Domain Models & Strategies   │ │ Repositories          │
│ Order, Cart, MenuItem,       │ │ ConcurrentHashMap     │
│ Strategies, Observers        │ │ in-memory stores      │
└──────────────────────────────┘ └───────────────────────┘
```

- **Domain Models Remain Pure**: `Order`, `Cart`, `MenuItem`, `Location`, and state machines are pure Java objects free of Spring annotations.
- **Strategies & Observers as Spring Beans**: Defined via `FoodDeliveryConfig` and injected into Application Services.
- **Application Services Coordinate Use Cases**: Handle transaction boundaries, input mapping, DTO conversion, and domain method invocations.

---

## 2. Which Classes Are Framework Components

The following classes are Spring Framework-managed stereotypes:
- **`@SpringBootApplication`**: `FoodDeliveryApplication` (application bootstrap).
- **`@Configuration`**: `FoodDeliveryConfig` (bean wiring for domain strategies and event publisher).
- **`@RestController`**: `RestaurantController`, `CartController`, `OrderController`, `DeliveryPartnerController`.
- **`@Service`**: `RestaurantApplicationService`, `CartApplicationService`, `OrderApplicationService`, `DeliveryApplicationService`.
- **`@Repository`**: `RestaurantRepository`, `OrderRepository`, `DeliveryPartnerRepository`, `CustomerRepository`, `CartRepository`.
- **`@RestControllerAdvice`**: `GlobalExceptionHandler` (centralized HTTP error translation).

---

## 3. Which Classes Belong to the Domain

The following classes form the core domain layer and contain zero Spring dependencies:
- **Entities & Aggregates**: `Restaurant`, `Menu`, `MenuItem`, `Cart`, `CartItem`, `Order`, `DeliveryPartner`, `Customer`.
- **Value Objects & Snapshots**: `Location`, `OrderItem`, `OrderBill`.
- **Enums & State Machines**: `OrderStatus`, `FoodCategory`, `PartnerStatus`, `PaymentMethod`.
- **Domain Strategies**:
  - `DeliveryFeeStrategy` (`DistanceBasedDeliveryFeeStrategy`, `SurgeDeliveryFeeStrategy`).
  - `DiscountStrategy` (`PercentageDiscountStrategy`, `FlatDiscountStrategy`, `NoDiscountStrategy`).
  - `DeliveryPartnerMatchingStrategy` (`NearestPartnerMatchingStrategy`, `HighestRatedPartnerMatchingStrategy`).
- **Domain Observers**: `OrderObserver`, `OrderEventPublisher`, `CustomerNotifier`, `RestaurantNotifier`, `DeliveryPartnerNotifier`, `OrderAuditLogger`.
- **Payment Abstractions**: `PaymentProcessor`, `CreditCardPaymentProcessor`, `UpiPaymentProcessor`, `CashOnDeliveryPaymentProcessor`, `PaymentProcessorFactory`.

---

## 4. Where Dependency Injection Is Used

All dependency injection is performed strictly via **constructor injection** (no field injection with `@Autowired`):
- `OrderApplicationService` receives its 4 repositories, `DeliveryFeeStrategy`, `PaymentProcessorFactory`, and `OrderEventPublisher` via constructor.
- `DeliveryApplicationService` receives `DeliveryPartnerRepository`, `OrderRepository`, `RestaurantRepository`, `DeliveryPartnerMatchingStrategy`, `OrderEventPublisher`, and `OrderApplicationService`.
- `RestaurantController`, `CartController`, `OrderController`, and `DeliveryPartnerController` receive their respective application services via constructor.

---

## 5. Whether Persistence Is Needed

For this Low-Level Design (LLD), persistence is handled by **in-memory thread-safe repositories** backed by `ConcurrentHashMap`. 
- **Why?** An LLD problem evaluates object-oriented domain modeling, encapsulation, business invariants, and design patterns. Introducing relational database schemas, Hibernate session lifecycles, and Docker containers adds infrastructural overhead that obscures the core object model.
- **Production Mapping**: In a distributed production system, these repository interfaces would be backed by PostgreSQL (for ACID order and transaction tables) and Redis (for ephemeral active cart sessions and geospatial driver indexing).

---

## 6. API Endpoints

### Restaurant & Menu Catalog
- `POST /api/v1/restaurants` — Register restaurant.
- `GET /api/v1/restaurants` — List all active restaurants.
- `GET /api/v1/restaurants/{id}` — Get restaurant details.
- `POST /api/v1/restaurants/{id}/menu-items` — Add a menu item.
- `GET /api/v1/restaurants/{id}/menu` — Get restaurant menu.

### Cart Management
- `GET /api/v1/carts/{customerId}` — View active cart.
- `POST /api/v1/carts/{customerId}/items` — Add item to cart (validates single-restaurant rule).
- `DELETE /api/v1/carts/{customerId}/items/{menuItemId}` — Remove item from cart.
- `DELETE /api/v1/carts/{customerId}` — Clear cart.

### Order Checkout & Lifecycle
- `POST /api/v1/orders/checkout` — Place order from cart with payment method & promo code.
- `GET /api/v1/orders/{orderId}` — Retrieve order details and current status.
- `POST /api/v1/orders/{orderId}/confirm` — Restaurant accepts order (`CONFIRMED`).
- `POST /api/v1/orders/{orderId}/preparing` — Kitchen begins food preparation (`PREPARING`).
- `POST /api/v1/orders/{orderId}/ready-for-pickup` — Kitchen marks food packed (`READY_FOR_PICKUP`).
- `POST /api/v1/orders/{orderId}/cancel` — Cancel order (allowed only before `PREPARING`).

### Courier Dispatch & Delivery
- `POST /api/v1/delivery/partners` — Register delivery partner.
- `POST /api/v1/delivery/dispatch/{orderId}` — Auto-match and assign available courier.
- `POST /api/v1/delivery/partners/{partnerId}/orders/{orderId}/pickup` — Mark picked up (`OUT_FOR_DELIVERY`).
- `POST /api/v1/delivery/partners/{partnerId}/orders/{orderId}/deliver` — Mark delivered (`DELIVERED`).

---

## 7. Data Transfer Objects (DTOs)

Jakarta Validation annotations (`@NotNull`, `@NotBlank`, `@Min`, `@DecimalMin`, `@DecimalMax`) enforce contract validity at the HTTP perimeter:
- **`CreateRestaurantRequest`**, **`AddMenuItemRequest`**, **`RestaurantResponse`**, **`MenuItemResponse`**.
- **`AddToCartRequest`**, **`CartResponse`**, **`CartItemResponse`**.
- **`CheckoutRequest`**, **`OrderResponse`**, **`OrderItemResponse`**, **`OrderBillResponse`**.
- **`CreatePartnerRequest`**, **`PartnerResponse`**.

---

## 8. Exception Handling

Centralized via `GlobalExceptionHandler` (`@RestControllerAdvice`):
- `ResourceNotFoundException` $\rightarrow$ `404 NOT_FOUND`
- `RestaurantMismatchException` $\rightarrow$ `400 BAD_REQUEST`
- `InvalidOrderStateException` $\rightarrow$ `400 BAD_REQUEST`
- `PaymentFailedException` $\rightarrow$ `402 PAYMENT_REQUIRED`
- `MethodArgumentNotValidException` $\rightarrow$ `400 BAD_REQUEST` with field-level validation errors.

---

## 9. Testing

`FoodDeliveryIntegrationTest` uses `@SpringBootTest` and `@AutoConfigureMockMvc` to verify:
1. Complete happy path from catalog registration to delivery completion.
2. HTTP 400 Bad Request error response when attempting to add items from multiple restaurants to the same cart.
3. HTTP 400 Bad Request error response when attempting an illegal state transition (e.g. cancelling an order after cooking has begun).

---

## 10. What Would Be Different in a True Production System

| Aspect | LLD In-Memory Design | True Production System |
| :--- | :--- | :--- |
| **Storage** | In-memory `ConcurrentHashMap` | PostgreSQL / MySQL with ACID transactions for Orders; DynamoDB / Redis for active Carts. |
| **Geospatial Indexing** | In-memory stream filter with Euclidean distance | Redis Geospatial (`GEOADD`, `GEORADIUS`) or PostgreSQL PostGIS for spherical Haversine spatial queries. |
| **Event Streaming** | In-memory `OrderEventPublisher` | Apache Kafka / AWS SNS+SQS for resilient async event fan-out across microservices. |
| **Payment Gateways** | Synchronous mock processor | Asynchronous two-phase payment authorization with webhook callback confirmation (Stripe, Razorpay). |
| **Live Tracking** | Synchronous polling API | WebSockets / Server-Sent Events (SSE) streaming live courier GPS coordinates to customer maps. |
