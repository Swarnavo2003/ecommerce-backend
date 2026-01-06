# 🛒 Spring Boot eCommerce API

A comprehensive RESTful eCommerce backend built with Spring Boot, featuring JWT authentication, role-based access control, and complete shopping functionality.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Authentication](#-authentication)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Configuration](#-configuration)
- [Contributing](#-contributing)

## ✨ Features

- **User Management**: Registration, login with JWT authentication
- **Role-Based Access Control**: Admin, Seller, and User roles
- **Product Management**: CRUD operations for products with categories
- **Shopping Cart**: Add, update, and remove items from cart
- **Order Processing**: Place orders with address and payment details
- **Category Management**: Organize products by categories
- **Address Management**: Multiple shipping addresses per user
- **Pagination & Sorting**: Efficient data retrieval for large datasets
- **Search Functionality**: Search products by keywords

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: JPA/Hibernate
- **API Documentation**: SpringDoc OpenAPI 3.1.0
- **Build Tool**: Maven/Gradle
- **Java Version**: 17+

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ or Gradle 7+
- MySQL/PostgreSQL (or your preferred database)

### Installation

1. Clone the repository
```bash
git clone https://github.com/Swarnavo2003/ecommerce-backend.git
cd ecommerce-backend
```

2. Configure the database in `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build the project
```bash
mvn clean install
```

4. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔐 Authentication

This API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

### Getting Started with Authentication

1. **Register** a new user at `/api/auth/register`
2. **Login** at `/api/auth/login` to receive a JWT token
3. Use the token in subsequent requests

## 🌐 API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Login and get JWT token | Public |

### Products

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/public/products` | Get all products (paginated) | Public |
| GET | `/api/public/products/keyword/{keyword}` | Search products by keyword | Public |
| GET | `/api/public/categories/{categoryId}/products` | Get products by category | Public |
| GET | `/api/seller/products` | Get seller's products | Seller/Admin |
| POST | `/api/seller/categories/{categoryId}/product` | Add new product | Seller/Admin |
| PUT | `/api/seller/products/{productId}` | Update product | Seller/Admin |
| DELETE | `/api/seller/products/{productId}` | Delete product | Seller/Admin |

### Categories

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/public/categories` | Get all categories (paginated) | Public |
| POST | `/api/admin/categories` | Create category | Admin |
| PUT | `/api/admin/categories/{categoryId}` | Update category | Admin |
| DELETE | `/api/admin/categories/{categoryId}` | Delete category | Admin |

### Shopping Cart

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/carts/user/cart` | Get current user's cart | User |
| GET | `/api/carts` | Get all carts | Admin |
| POST | `/api/carts/products/{productId}/quantity/{quantity}` | Add product to cart | User |
| PUT | `/api/carts/products/{productId}/quantity/{operation}` | Update cart quantity | User |
| DELETE | `/api/carts/{cartId}/product/{productId}` | Remove product from cart | User |

### Orders

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/orders/place` | Place a new order | User |

### Addresses

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/addresses` | Get all addresses | Admin |
| GET | `/api/user/addresses` | Get current user's addresses | User |
| GET | `/api/addresses/{addressId}` | Get address by ID | User |
| POST | `/api/addresses` | Create new address | User |
| PUT | `/api/addresses/{addressId}` | Update address | User |
| DELETE | `/api/addresses/{addressId}` | Delete address | User |

## 📊 Database Schema

### Key Entities

- **User**: User account information with roles
- **Product**: Product details including price, quantity, and discounts
- **Category**: Product categorization
- **Cart**: Shopping cart with products
- **Order**: Order information with items and payment
- **OrderItem**: Individual items in an order
- **Payment**: Payment transaction details
- **Address**: User shipping addresses

## ⚙️ Configuration

### Application Properties

Key configurations in `application.properties`:
```properties
# Server Configuration
server.port=8080

# JWT Configuration
jwt.secret=your_secret_key
jwt.expiration=86400000

# Database Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Security Roles

- **ADMIN**: Full access to all endpoints
- **SELLER**: Can manage products and view seller-specific data
- **USER**: Can browse products, manage cart, and place orders

## 🔒 Security Features

- JWT-based stateless authentication
- Role-based authorization
- Password encryption using BCrypt
- CSRF protection disabled for REST API
- Session management set to STATELESS

## 📝 Request/Response Examples

### Register a New User
```json
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "123",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER"
  }
}
```

### Add Product to Cart
```json
POST /api/carts/products/1/quantity/2
Authorization: Bearer <your_jwt_token>
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📧 Contact

Swarnavo Majumder - [@Swarnavo2003](https://github.com/Swarnavo2003) - mswarnavo2003@gmail.com

Project Link: [https://github.com/Swarnavo2003/ecommerce-backend](https://github.com/Swarnavo2003/ecommerce-backend)

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

⭐ Star this repository if you find it helpful!