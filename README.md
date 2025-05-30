# Bridge Application

The **Bridge Application** is a Spring Boot-based e-commerce platform that facilitates interactions between buyers and sellers. It provides features such as product management, category management, cart functionality, authentication, and more.

---

## Features

- **Authentication & Authorization**:
    - Role-based access control (Buyer, Seller, Admin).
    - JWT-based authentication.
    - Password reset via email.

- **Product Management**:
    - Add, update, delete, and retrieve products.
    - Filter products by category, seller, or brand.

- **Category Management**:
    - Add, update, delete, and retrieve categories.
    - Support for parent-child category relationships.

- **Cart Management**:
    - Add, update, and delete items in the cart.
    - Retrieve all cart items for a buyer.

- **Order Management**:
    - Manage orders with statuses (e.g., Pending, Shipped, Delivered).

- **Email Notifications**:
    - Email alerts for password reset.

---

## Technologies Used

- **Backend**:
    - Java
    - Spring Boot
    - Spring Security
    - Hibernate (JPA)

- **Database**:
    - MySQL

- **Build Tool**:
    - Maven

- **Other**:
    - JWT for authentication
    - JavaMailSender for email notifications
    - Multipart file handling for product images

---

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.8.1 or higher
- **MySQL**: Ensure a MySQL database is running locally
- **SMTP Configuration**: A valid Gmail account for email notifications

---

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/MOSTAFA-MANSOUR72/bridge.git
   cd bridge
   ```

2. **Configure the Database**:
   Update the database credentials in `src/main/resources/application.properties`:
   ```ini
   spring.datasource.url=jdbc:mysql://localhost:3306/bridge
   spring.datasource.username=<your-username>
   spring.datasource.password=<your-password>
   ```

3. **Configure Email**:
   Update the email credentials in `application.properties`:
   ```ini
   spring.mail.username=<your-email>
   spring.mail.password=<your-app-password>
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
    - API Base URL: `http://localhost:8080`
    - Swagger UI: `http://localhost:8080/swagger-ui/`

---

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user (Buyer/Seller).
- `POST /api/auth/login` - Login and retrieve a JWT token.

### Products
- `POST /api/products` - Add a new product (Seller only).
- `PUT /api/products` - Update a product (Seller only).
- `GET /api/products` - Retrieve all products.
- `GET /api/products/{id}` - Retrieve a product by ID.
- `DELETE /api/products/{id}` - Delete a product (Seller only).

### Categories
- `POST /api/categories` - Add a new category.
- `PUT /api/categories` - Update a category.
- `GET /api/categories` - Retrieve all parent categories.
- `GET /api/categories/child/{parentId}` - Retrieve child categories.
- `DELETE /api/categories/{id}` - Delete a category.

### Cart
- `POST /api/cart` - Add an item to the cart.
- `PUT /api/cart` - Update a cart item.
- `GET /api/cart` - Retrieve all cart items.
- `DELETE /api/cart/{cartItemId}` - Remove an item from the cart.

### Product Reviews
- `POST /api/product-review` - Add a new review for a product (Buyer only, one review per product).
- `PUT /api/product-review` - Update an existing review (Buyer only, must be the review owner).
- `DELETE /api/product-review/{id}` - Delete a review by its ID (Buyer only, must be the review owner).
- `GET /api/product-review/{productId}` - Retrieve paginated reviews for a specific product.

### Password Management
- `GET /api/password/send/{gmail}` - Send a password reset token.
- `POST /api/password/change` - Change the password using the token.

---

## Project Structure

- `controller/` - REST controllers for handling API requests.
- `service/` - Business logic for the application.
- `repository/` - JPA repositories for database interactions.
- `entity/` - Entity classes representing database tables.
- `dto/` - Data Transfer Objects for API requests and responses.
- `security/` - Security configuration and JWT utilities.
- `mapper/` - Mappers for converting between entities and DTOs.

---

## Contact

For any inquiries or issues, please contact:
- **Name**: Mostafa Mansour
- **Email**: mostafamansour76272@gmail.com
- **GitHub**: [MOSTAFA-MANSOUR72](https://github.com/MOSTAFA-MANSOUR72)