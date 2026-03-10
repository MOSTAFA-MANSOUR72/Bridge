# Bridge — Complete Project Documentation

> A Spring Boot–based B2B/B2C e-commerce backend platform that bridges buyers and sellers.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Architecture Overview](#3-architecture-overview)
4. [Project Structure](#4-project-structure)
5. [Data Model (Entities)](#5-data-model-entities)
6. [Security & Authentication](#6-security--authentication)
7. [API Reference](#7-api-reference)
   - [Authentication](#71-authentication-apiauthpost)
   - [Products](#72-products-apiproducts)
   - [Categories](#73-categories-apicategories)
   - [Cart](#74-cart-apicart)
   - [Orders](#75-orders-apiorder)
   - [Product Reviews](#76-product-reviews-apiproduct-review)
   - [Wishlist](#77-wishlist-apiwishlist)
   - [Password Management](#78-password-management-apipassword)
8. [Role-Based Access Control](#8-role-based-access-control)
9. [Configuration Reference](#9-configuration-reference)
10. [Setup & Running Locally](#10-setup--running-locally)
11. [Key Service Layer Details](#11-key-service-layer-details)
12. [Exception Handling](#12-exception-handling)

---

## 1. Project Overview

**Bridge** is a Spring Boot REST API that acts as the backbone for an e-commerce marketplace. It supports three types of users — **Buyers**, **Sellers**, and **Admins** — each with distinct capabilities:

| Actor   | Capabilities |
|---------|-------------|
| Buyer   | Browse products, manage cart, place orders, write reviews, manage wishlist |
| Seller  | List and manage their own products, manage categories, fulfill orders |
| Admin   | Manage all users, categories, and products |

**Base URL:** `http://localhost:8080`  
**API Documentation (Swagger UI):** `http://localhost:8080/swagger-ui/index.html`  
**OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## 2. Technology Stack

| Category         | Technology                          | Version     |
|-----------------|--------------------------------------|-------------|
| Language         | Java                                | 21 (LTS)    |
| Framework        | Spring Boot                         | 3.4.2       |
| Security         | Spring Security + JWT (JJWT)        | 0.11.5      |
| Persistence      | Spring Data JPA / Hibernate         | —           |
| Database         | MySQL                               | ≥ 8.x       |
| Validation       | Spring Validation (Jakarta)         | —           |
| Email            | Spring Boot Starter Mail (SMTP)     | —           |
| PDF Generation   | iTextPDF                            | 5.5.13.3    |
| API Docs         | SpringDoc OpenAPI (Swagger UI)      | 2.1.0       |
| Boilerplate      | Lombok                              | 1.18.34     |
| Build Tool       | Maven                               | ≥ 3.8       |
| Dev Tooling      | Spring DevTools                     | —           |

---

## 3. Architecture Overview

The application follows a layered architecture:

```
HTTP Request
     │
     ▼
┌─────────────────────┐
│   Controller Layer  │  REST endpoints, input mapping, HTTP status codes
└─────────────────────┘
     │
     ▼
┌─────────────────────┐
│   Service Layer     │  Business logic, transaction management
└─────────────────────┘
     │
     ▼
┌─────────────────────┐
│  Repository Layer   │  Spring Data JPA interfaces, DB queries
└─────────────────────┘
     │
     ▼
┌─────────────────────┐
│   Entity / DB       │  JPA entities mapped to MySQL tables
└─────────────────────┘
```

**Cross-cutting concerns:**
- **Security Filter Chain** (`JwtAuthenticationFilter`) runs before every request.
- **DTOs** decouple the API contract from internal entities.
- **Mappers** translate between DTOs and entities.
- **Startup Seeder** (`CommandLineRunner` in `AppConfig`) seeds a default Seller, Admin, and Electronics category on every start.

---

## 4. Project Structure

```
Bridge/
├── pom.xml                                  # Maven build descriptor
├── README.md
├── DOCUMENTATION.md                         # ← This file
└── src/
    └── main/
        ├── resources/
        │   └── application.properties       # All configuration
        └── java/com/market/bridge/
            ├── BridgeApplication.java        # Entry point
            ├── controller/                  # REST controllers (8 files)
            │   ├── AuthenticationController.java
            │   ├── ProductController.java
            │   ├── CategoryController.java
            │   ├── CartController.java
            │   ├── OrderController.java
            │   ├── PasswordController.java
            │   ├── ProductReviewController.java
            │   └── WishlistController.java
            ├── service/                     # Interfaces + Implementations
            │   ├── Authentication/
            │   ├── Cart/
            │   ├── Category/
            │   ├── MailService/
            │   ├── Order/
            │   ├── PasswordChange/
            │   ├── Product/
            │   ├── ProductReview/
            │   ├── UserDetailsService/
            │   └── Wishlist/
            ├── repository/                  # Spring Data JPA repos (10 files)
            ├── entity/                      # JPA entities
            │   ├── Product.java
            │   ├── Category.java
            │   ├── ProductReview.java
            │   ├── Address.java
            │   ├── cart/
            │   │   ├── Cart.java
            │   │   └── CartItem.java
            │   ├── order/
            │   │   ├── SingleOrder.java
            │   │   └── OrderItem.java
            │   ├── users/
            │   │   ├── Buyer.java
            │   │   ├── Seller.java
            │   │   ├── Admin.java
            │   │   └── UserEntity.java      # Spring Security UserDetails adapter
            │   └── enums/
            │       ├── OrderStatus.java
            │       └── SystemRoles.java
            ├── dto/                         # Request & Response DTOs
            │   ├── authentication/
            │   ├── cartItem/
            │   ├── category/
            │   ├── order/
            │   ├── product/
            │   └── ProductReview/
            ├── mapper/                      # Entity ↔ DTO mappers
            ├── exception/                   # Custom exceptions (4 files)
            └── security/
                ├── AppConfig.java           # AuthProvider, BCrypt, seeder
                └── jwt/
                    ├── JwtService.java
                    ├── JwtAuthenticationFilter.java
                    ├── SecurityConfiguration.java
                    └── util.java            # Thread-local user context
```

---

## 5. Data Model (Entities)

### 5.1 User Types

The system uses three **separate database tables** for users — they do not share a JPA inheritance hierarchy but are unified at runtime by `UserEntity` (a `UserDetails` adapter).

#### Buyer (`buyer` table)

| Field         | Type        | Constraints       | Notes                          |
|---------------|-------------|-------------------|--------------------------------|
| `id`          | Long        | PK, auto-gen      |                                |
| `username`    | String      | NOT NULL, UNIQUE  |                                |
| `password`    | String      | NOT NULL          | BCrypt-encoded                 |
| `email`       | String      | NOT NULL, UNIQUE  |                                |
| `phoneNumber` | String      | NOT NULL, UNIQUE  |                                |
| `roles`       | String      | NOT NULL          | Value: `"BUYER"`               |
| `address`     | Address     | OneToOne          | Cascade ALL                    |
| `orders`      | List\<SingleOrder\> | OneToMany  | Mapped by `buyer`          |
| `productReviews` | List\<ProductReview\> | OneToMany | Mapped by `buyer`     |
| `cart`        | Cart        | OneToOne          | Mapped by `buyer`              |
| `wishlist`    | Set\<Product\> | ManyToMany     | Join table: `wishlist`         |
| `createdAt`   | LocalDate   | Auto              |                                |
| `modifiedAt`  | LocalDate   | Auto              |                                |

#### Seller (`seller` table)

| Field         | Type           | Constraints       | Notes                      |
|---------------|----------------|-------------------|----------------------------|
| `id`          | Long           | PK, auto-gen      |                            |
| `username`    | String         | NOT NULL, UNIQUE  |                            |
| `password`    | String         | NOT NULL          | BCrypt-encoded             |
| `email`       | String         | NOT NULL, UNIQUE  |                            |
| `phoneNumber` | String         | NOT NULL, UNIQUE  |                            |
| `companyName` | String         | Optional          |                            |
| `roles`       | String         | NOT NULL          | Value: `"SELLER"`          |
| `address`     | Address        | OneToOne          | Cascade ALL                |
| `products`    | List\<Product\> | OneToMany        | Mapped by `seller`         |
| `createdAt`   | LocalDate      | Auto              |                            |
| `modifiedAt`  | LocalDate      | Auto              |                            |

#### Admin (`admin` table)

Fields: `id`, `username`, `password`, `email`, `phoneNumber`, `roles` (`"ADMIN"`).

---

### 5.2 Product (`product` table)

| Field         | Type               | Notes                                             |
|---------------|--------------------|---------------------------------------------------|
| `id`          | Long (PK)          |                                                   |
| `name`        | String             |                                                   |
| `description` | String             |                                                   |
| `price`       | Double             |                                                   |
| `minOrder`    | Long               | Minimum order quantity                            |
| `quantity`    | Long               | Stock quantity                                    |
| `rating`      | Float              | Aggregate rating                                  |
| `brandName`   | String             |                                                   |
| `seller`      | Seller (ManyToOne) | FK: `Seller_id`                                   |
| `categories`  | List\<Category\>   | ManyToMany via `product_category` join table       |
| `orderItems`  | List\<OrderItem\>  | OneToMany, Cascade ALL                            |
| `productReviews` | List\<ProductReview\> | OneToMany, Cascade ALL                      |
| `images`      | List\<String\>     | `@ElementCollection` → `product_images` table     |
| `createdAt`   | LocalDate          | Auto                                              |
| `modifiedAt`  | LocalDate          | Auto                                              |

---

### 5.3 Category (`category` table)

| Field            | Type            | Notes                                          |
|-----------------|-----------------|------------------------------------------------|
| `id`             | Long (PK)       |                                                |
| `name`           | String          |                                                |
| `parentCategoryId` | Long          | `null` = root/parent category                  |
| `products`       | List\<Product\> | ManyToMany via `product_category`              |

Supports **hierarchical (parent–child) categories**. A `null` `parentCategoryId` indicates a top-level (parent) category.

---

### 5.4 Cart & CartItem

- **Cart** (`cart` table): one `Cart` per `Buyer` (OneToOne). Holds a list of `CartItem`.
- **CartItem** (`cart_item` table): references `Cart` and `Product`, stores `quantity`.

---

### 5.5 Order

- **SingleOrder** (`single_order` table): belongs to a `Buyer`, has a list of `OrderItem`, tracks `totalPrice`, `totalQuantity`, `status` (enum `OrderStatus`), `paymentMethod`, `estimatedDeliveryDate`.
- **OrderItem** (`order_item` table): links `SingleOrder` ↔ `Product` with quantity and price.

#### OrderStatus Enum
```
PENDING → SHIPPED → DELIVERED
                 → CANCELLED
```

---

### 5.6 ProductReview (`product_review` table)

| Field       | Type    | Notes                             |
|------------|---------|-----------------------------------|
| `id`        | Long    | PK                                |
| `product`   | Product | ManyToOne                         |
| `buyer`     | Buyer   | ManyToOne                         |
| `review`    | String  | Review text                       |
| `rating`    | float   | Numeric rating                    |
| `createdAt` | LocalDate | Auto                             |
| `modifiedAt`| LocalDate | Auto                             |

> **Constraint**: One review per buyer per product (enforced at service level).

---

### 5.7 Database Diagram (Summary)

```
Buyer ──────┬── (1:1) → Cart ──── (1:N) → CartItem ─── (N:1) → Product
            ├── (1:N) → SingleOrder ─── (1:N) → OrderItem ── (N:1) → Product
            ├── (1:N) → ProductReview ─────────────────────────────── (N:1) → Product
            └── (N:N) ──────────────────────────────────────────────> Product (Wishlist)

Seller ─── (1:N) → Product ─── (N:N) → Category

Category ─── parentCategoryId → Category (self-referencing)
```

---

## 6. Security & Authentication

### 6.1 Overview

The application uses **stateless JWT-based authentication** via Spring Security. There is no HTTP session; every request must carry a valid Bearer token.

**Flow:**

```
Client
  │─── POST /api/auth/register or /api/auth/login ──► AuthenticationController
  │◄── JWT token ────────────────────────────────────
  │
  │─── GET/POST/... /api/... (Authorization: Bearer <token>) ──►
  │         JwtAuthenticationFilter validates token
  │         SecurityContextHolder populated with user
  │         Controller / @PreAuthorize check executes
```

### 6.2 JWT Configuration

| Property                    | Default Value                   | Description                                  |
|-----------------------------|---------------------------------|----------------------------------------------|
| `security.jwt.secret-key`   | 64-char hex string              | HMAC-SHA256 signing key (Base64-encoded)     |
| `security.jwt.expiration`   | `3600000` (1 hour in ms)        | Access token TTL                             |
| `security.mail.expiration`  | `60000` (1 minute in ms)        | Password-reset token TTL                     |

The `JwtService` uses **JJWT 0.11.5** with `HS256` algorithm. The subject claim stores the **username**.

### 6.3 Public Endpoints (No Auth Required)

| Path Pattern        | Description                        |
|--------------------|------------------------------------|
| `POST /api/auth/**` | Register and login                 |
| `GET /api/products/**` | Browse all products (public)    |
| `GET /api/categories/**` | Browse categories (public)    |
| `GET /api/password/**` | Request password reset email    |
| `POST /api/password/**` | Submit new password with token |
| `/swagger-ui/**`   | Swagger UI                         |
| `/v3/api-docs*/**` | OpenAPI spec                       |

### 6.4 UserDetails Resolution

`ComposedUserDetailsService` attempts to load a user by username from **all three repositories** (Buyer → Seller → Admin) in order. It wraps the found entity in a `UserEntity` which implements `UserDetails` and maps the role string to `GrantedAuthority` with the `ROLE_` prefix.

### 6.5 CORS Configuration

Allowed origins (configured in `SecurityConfiguration`):
- `http://localhost:3000`
- `http://localhost:5173`
- `https://yourdomain.com`

Allowed methods: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`, `PATCH`  
Allowed headers: `Authorization`, `Content-Type`, `X-Requested-With`

---

## 7. API Reference

> **Authentication**: All protected endpoints require the header:
> ```
> Authorization: Bearer <your-jwt-token>
> ```

---

### 7.1 Authentication (`/api/auth` — POST)

#### `POST /api/auth/register`

Registers a new user. The `roles` field determines which user type to create.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "secret123",
  "email": "john@example.com",
  "phoneNumber": "01012345678",
  "roles": "BUYER",
  "companyName": "Acme Corp",
  "address": {
    "street": "123 Main St",
    "city": "Cairo",
    "state": "Cairo",
    "zipCode": "12345",
    "country": "Egypt"
  }
}
```

| Field         | Required For | Description                                        |
|--------------|--------------|----------------------------------------------------|
| `username`    | All          |                                                    |
| `password`    | All          | Stored BCrypt-hashed                               |
| `email`       | All          |                                                    |
| `phoneNumber` | All          |                                                    |
| `roles`       | All          | `"BUYER"`, `"SELLER"`, or `"ADMIN"` (case-insensitive) |
| `companyName` | SELLER only  | Optional for sellers                               |
| `address`     | BUYER, SELLER| Optional address object                            |

**Response:** `200 OK` — Returns a JWT token string.

**Errors:**
- `400 Bad Request` — Invalid role value.
- `409 Conflict` — Username or email already exists (`DuplicateResourceException`).

---

#### `POST /api/auth/login`

Authenticates an existing user.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "secret123"
}
```

**Response:** `200 OK`
```json
{
  "token": "<jwt-access-token>",
  "user": { ... }
}
```

**Errors:**
- `401 Unauthorized` — Invalid credentials.

---

### 7.2 Products (`/api/products`)

#### `POST /api/products` — Create Product
**Auth:** `SELLER` only  
**Content-Type:** `multipart/form-data`

**Form Fields:**

| Field         | Type           | Description                         |
|--------------|----------------|-------------------------------------|
| `name`        | String         | Product name                        |
| `description` | String         | Product description                 |
| `price`       | Double         | Unit price                          |
| `minOrder`    | Long           | Minimum order quantity              |
| `quantity`    | Long           | Available stock                     |
| `brandName`   | String         | Brand name                          |
| `categoryIds` | List\<Long\>   | IDs of categories to assign         |
| `images`      | MultipartFile[]| Product images (saved to disk)      |

**Response:** `201 Created` — Returns the created product object.

---

#### `PUT /api/products` — Update Product
**Auth:** `SELLER` only  
**Content-Type:** `multipart/form-data`

Same fields as create, plus `id` of the product to update. New images replace existing ones.

**Response:** `200 OK` — `"Product updated successfully"`

**Errors:** `404 Not Found` — Product not found.

---

#### `GET /api/products` — Get All Products (Paginated)
**Auth:** Public

**Query Parameters:**

| Param        | Default | Description           |
|-------------|---------|----------------------|
| `pageNumber` | `0`     | Zero-based page index |
| `pageSize`   | `50`    | Items per page        |

**Response:** `200 OK` — Paginated list of `ProductResponse`.

---

#### `GET /api/products/{id}` — Get Product by ID
**Auth:** Public

**Path Variable:** `id` — Product ID

**Response:** `200 OK` — `ProductResponse` object.

**Errors:** `404 Not Found` — `"Product not found"`.

---

#### `GET /api/products/category/{categoryId}` — Get Products by Category
**Auth:** Public

**Path Variable:** `categoryId`  
**Query Params:** `pageNumber` (default `0`), `pageSize` (default `50`)

**Response:** `200 OK` — Paginated list of products in that category.

---

#### `DELETE /api/products/{id}` — Delete Product
**Auth:** `ADMIN` or `SELLER`

**Path Variable:** `id`

**Response:** `200 OK` — `"Product deleted successfully"`

---

### 7.3 Categories (`/api/categories`)

**Auth:** `ADMIN` or `SELLER` for all endpoints (enforced at class level via `@PreAuthorize`).

> **Note:** Browse endpoints (`GET`) are also listed as public in the security filter chain, so they can be called without a token.

#### `GET /api/categories` — Get Parent Categories
Returns all root-level categories (`parentCategoryId = null`).

**Response:** `200 OK` — List of categories.

---

#### `GET /api/categories/child/{parentId}` — Get Child Categories
**Path Variable:** `parentId`

**Response:** `200 OK` — List of child categories for the given parent.

---

#### `GET /api/categories/{id}` — Get Category by ID
**Path Variable:** `id`

**Response:** `200 OK` — Single category object.

---

#### `POST /api/categories` — Create Category
**Request Body:**
```json
{
  "name": "Smartphones",
  "parentCategoryId": 1
}
```
Set `parentCategoryId` to `null` to create a top-level category.

**Response:** `200 OK` — Created category.

---

#### `PUT /api/categories` — Update Category
**Request Body:**
```json
{
  "id": 3,
  "name": "Mobile Phones",
  "parentCategoryId": 1
}
```

**Response:** `200 OK` — Updated category.

---

#### `DELETE /api/categories/{id}` — Delete Category
**Path Variable:** `id`

**Response:** `200 OK`.

---

### 7.4 Cart (`/api/cart`)

**Auth:** `BUYER` only (enforced at class level).

#### `GET /api/cart` — Get All Cart Items
Returns all cart items for the authenticated buyer.

**Response:** `207 Multi-Status` — List of cart items.

---

#### `POST /api/cart` — Add Item to Cart
**Request Body:**
```json
{
  "productId": 5,
  "quantity": 2
}
```

**Response:** `200 OK` — Updated cart item or confirmation.

---

#### `PUT /api/cart` — Update Cart Item Quantity
**Request Body:**
```json
{
  "cartItemId": 12,
  "quantity": 4
}
```

**Response:** `200 OK` — Updated cart item.

---

#### `DELETE /api/cart/{cartItemId}` — Remove Item from Cart
**Path Variable:** `cartItemId`

**Response:** `200 OK` — `"Cart item deleted successfully"`

---

### 7.5 Orders (`/api/order`)

**Auth:** `BUYER`, `ADMIN`, or `SELLER`.

#### `POST /api/order` — Create Order
**Request Body:**
```json
{
  "productId": 5,
  "quantity": 2,
  "paymentMethod": "CREDIT_CARD",
  "estimatedDeliveryDate": "2025-04-01"
}
```

**Response:** `201 Created` — `OrderResponse` object with full order details.

---

#### `GET /api/order/{orderId}` — Get Order by ID
**Path Variable:** `orderId`

**Response:** `200 OK` — `OrderResponse` object.

---

#### `GET /api/order` — Get All Orders (Paginated)
**Query Parameters:**

| Param  | Default | Description           |
|------|---------|----------------------|
| `page` | `0`     | Zero-based page index |
| `size` | `10`    | Items per page        |

**Response:** `200 OK` — `Page<OrderResponse>`.

---

#### `PUT /api/order/{orderId}/cancel` — Cancel Order
**Path Variable:** `orderId`

**Response:** `200 OK` — Cancellation confirmation message.

---

#### `PUT /api/order/{orderId}/status` — Update Order Status
**Path Variable:** `orderId`  
**Query Parameter:** `status` — e.g., `SHIPPED`, `DELIVERED`, `CANCELLED`

**Response:** `200 OK` — Updated `OrderResponse`.

---

### 7.6 Product Reviews (`/api/product-review`)

**Auth:** Varies — login required for write operations.

#### `POST /api/product-review` — Add Review
**Auth:** `BUYER` (one review per product per buyer)

**Request Body:**
```json
{
  "productId": 5,
  "review": "Great product! Fast shipping.",
  "rating": 4.5
}
```

**Response:** `201 Created` — Review object.

---

#### `PUT /api/product-review` — Update Review
**Auth:** `BUYER` (must be the owner)

**Request Body:**
```json
{
  "id": 10,
  "review": "Updated review text.",
  "rating": 5.0
}
```

**Response:** `200 OK` — Updated review object.

---

#### `DELETE /api/product-review/{id}` — Delete Review
**Auth:** `BUYER` (must be the owner)  
**Path Variable:** `id`

**Response:** `200 OK` — `"Product review deleted successfully."`

---

#### `GET /api/product-review/{productId}` — Get Reviews for Product
**Auth:** Public

**Path Variable:** `productId`  
**Query Params:** `pageNumber` (default `0`), `pageSize` (default `50`)

**Response:** `200 OK` — Paginated list of reviews.

---

### 7.7 Wishlist (`/api/wishlist`)

**Auth:** `BUYER` only.

#### `POST /api/wishlist/{productId}` — Add to Wishlist
**Path Variable:** `productId`

**Response:** `201 Created` — Confirmation.

---

#### `DELETE /api/wishlist/{productId}` — Remove from Wishlist
**Path Variable:** `productId`

**Response:** `200 OK` — `"Product removed from wishlist successfully."`

---

#### `DELETE /api/wishlist` — Clear Entire Wishlist

**Response:** `200 OK` — `"Wishlist cleared successfully."`

---

#### `GET /api/wishlist` — Get Wishlist (Paginated)
**Query Params:** `pageNumber` (default `0`), `pageSize` (default `50`)

**Response:** `200 OK` — Paginated list of wishlist products.

---

### 7.8 Password Management (`/api/password`)

These endpoints are **public** (no auth required).

#### `GET /api/password/send/{Gmail}` — Send Reset Email
**Path Variable:** `Gmail` (the user's registered email/gmail)

Sends a short-lived (1-minute) JWT reset token to the specified email address.

**Response:** `200 OK` — `"Email sent successfully"`

---

#### `POST /api/password/change` — Change Password
**Request Body:**
```json
{
  "token": "<reset-jwt-token>",
  "password": "newSecurePassword123"
}
```

**Response:** `200 OK` — `"Password changed successfully"`

**Errors:**
- `400 Bad Request` — Token expired or invalid.
- `404 Not Found` — User not found.

---

## 8. Role-Based Access Control

| Endpoint Group      | PUBLIC | BUYER | SELLER | ADMIN |
|---------------------|:------:|:-----:|:------:|:-----:|
| `POST /api/auth/**` | ✅     | ✅    | ✅     | ✅    |
| `GET /api/products/**` | ✅  | ✅    | ✅     | ✅    |
| `POST /api/products` | ❌    | ❌    | ✅     | ❌    |
| `PUT /api/products`  | ❌    | ❌    | ✅     | ❌    |
| `DELETE /api/products/{id}` | ❌ | ❌ | ✅   | ✅    |
| `GET /api/categories/**` | ✅ | ✅   | ✅     | ✅    |
| `POST/PUT/DELETE /api/categories/**` | ❌ | ❌ | ✅ | ✅ |
| `GET /api/cart`     | ❌    | ✅    | ❌     | ❌    |
| `POST /api/cart`    | ❌    | ✅    | ❌     | ❌    |
| `PUT /api/cart`     | ❌    | ✅    | ❌     | ❌    |
| `DELETE /api/cart/**` | ❌  | ✅    | ❌     | ❌    |
| `POST /api/order`   | ❌    | ✅    | ✅     | ✅    |
| `GET /api/order/**` | ❌    | ✅    | ✅     | ✅    |
| `PUT /api/order/**` | ❌    | ✅    | ✅     | ✅    |
| `POST /api/product-review` | ❌ | ✅ | ❌   | ❌    |
| `PUT /api/product-review`  | ❌ | ✅ | ❌   | ❌    |
| `DELETE /api/product-review/**` | ❌ | ✅ | ❌ | ❌  |
| `GET /api/product-review/**` | ✅ | ✅ | ✅  | ✅    |
| `GET/POST/DELETE /api/wishlist/**` | ❌ | ✅ | ❌ | ❌ |
| `GET/POST /api/password/**` | ✅ | ✅ | ✅  | ✅    |

---

## 9. Configuration Reference

All configuration lives in `src/main/resources/application.properties`.

```properties
# === Application ===
spring.application.name=bridge

# === Database (MySQL) ===
spring.datasource.url=jdbc:mysql://localhost:3306/spring
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create-drop    # CAUTION: drops/recreates schema on every restart

# === JWT ===
security.jwt.secret-key=d2b98752c898d23b2586fe79af25bd32869fca6b15d364ae765a9e7ec7b7358f
security.jwt.expiration=3600000              # 1 hour in milliseconds
security.mail.expiration=60000              # 1 minute in milliseconds

# === SMTP Email (Gmail) ===
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<your-email>
spring.mail.password=<your-app-password>     # Use Gmail App Password, not regular password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# === File Upload ===
file.upload-dir=<images-dir>                 # Absolute path to image storage directory
```

> **⚠️ Warning:** `spring.jpa.hibernate.ddl-auto=create-drop` will **drop and recreate all database tables** on every application restart. For production, change to `update` or `validate`.

> **⚠️ Warning:** The `security.jwt.secret-key` in the repository is a sample key. **Replace it** with a securely generated random secret before deploying.

---

## 10. Setup & Running Locally

### Prerequisites

- **JDK 21+**
- **Maven 3.8+**
- **MySQL 8.x** running locally
- **Gmail account** with an [App Password](https://support.google.com/accounts/answer/185833) for SMTP

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/MOSTAFA-MANSOUR72/bridge.git
cd bridge
```

**2. Create the MySQL database**
```sql
CREATE DATABASE spring;
```

**3. Configure `application.properties`**

Update the following values:
```properties
spring.datasource.username=<your-mysql-username>
spring.datasource.password=<your-mysql-password>
spring.mail.username=<your-gmail>
spring.mail.password=<your-app-password>
file.upload-dir=/path/to/images/directory/
```

**4. Build & Run**
```bash
mvn spring-boot:run
```

**5. Verify startup**

On first run, the `CommandLineRunner` in `AppConfig` seeds:
- A **Seller** user: `username=user`, `password=password`
- An **Admin** user: `username=admin`, `password=admin`
- An **Electronics** parent category

Access:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## 11. Key Service Layer Details

### Authentication Service (`AuthenticationServiceImpl`)

- `buyerRegister(request)` — Checks for username/email uniqueness across **all three user tables**, creates `Buyer`, returns JWT.
- `sellerRegister(request)` — Same flow for `Seller`; includes `companyName`.
- `adminRegister(request)` — Same flow for `Admin`.
- `authenticate(request)` — Delegates to `AuthenticationManager`, generates `AuthResponse` (token + user details).
- `findDuplicate(username, email)` — Checks all three repos before allowing registration.

### JWT Service (`JwtService`)

- Generates standard access tokens (HS256, expiry from config).
- Generates short-lived **mail tokens** for password reset (separate 1-minute expiry).
- `isTokenValid(token, userDetails)` — Verifies subject matches and token is not expired.
- `setUp(username)` — Populates a thread-local `util` context with current user's username and ID for use downstream in service layers.

### Image Upload

Product images are stored on the **local filesystem** at the path defined by `file.upload-dir`. The absolute file path is stored in the `product_images` table (element collection on `Product`). There is no cloud storage integration by default.

---

## 12. Exception Handling

The project defines these custom exceptions (in `com.market.bridge.exception`):

| Exception                  | Typical HTTP Status | Triggered When                                  |
|---------------------------|--------------------|-------------------------------------------------|
| `DuplicateResourceException` | `409 Conflict`   | Username or email already registered            |
| `ResourceNotFoundException`  | `404 Not Found`  | Entity not found by ID                          |
| `ValidationException`        | `400 Bad Request`| Invalid input (e.g., unknown role)             |

> A global exception handler (`@ControllerAdvice`) is expected to map these to appropriate HTTP responses. Check for a `GlobalExceptionHandler` class in the `exception` package for exact mappings.

---

## Contact

| Field   | Value                                                        |
|--------|--------------------------------------------------------------|
| Author  | Mostafa Mansour                                              |
| Email   | mostafamansour76272@gmail.com                                |
| GitHub  | [MOSTAFA-MANSOUR72](https://github.com/MOSTAFA-MANSOUR72)   |
| Repo    | [github.com/MOSTAFA-MANSOUR72/bridge](https://github.com/MOSTAFA-MANSOUR72/bridge) |
