# Book Store API Documentation

This document describes all the REST API endpoints for the book store system with products, customers, orders, reviews, and ratings.

## Base URL

```
http://localhost:8080/api/v1
```

## Important: Prototype Simplification

This is a **prototype implementation** that uses simple `List` responses instead of `Spring Data PageImpl`. All list endpoints return complete lists for simplicity, making API responses clean and serialization-stable JSON structures.

## 1. Products Endpoints

### 1.1 Create Product

- **Endpoint:** `POST /products`
- **Description:** Create a new product (book)
- **Request Body:**

```json
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "9780743273565",
  "price": 12.99,
  "description": "A classic novel of the Jazz Age",
  "coverImage": "url_to_image"
}
```

- **Response:** 201 Created with ProductDTO

### 1.2 Get Product by ID

- **Endpoint:** `GET /products/{id}`
- **Response:** 200 OK with ProductDTO or 404 Not Found

### 1.3 Get All Products

- **Endpoint:** `GET /products`
- **Response:** 200 OK with List of ProductDTO

### 1.4 Basic Search (Search in Author and Title)

- **Endpoint:** `GET /products/search/basic`
- **Query Parameters:**
  - `searchTerm` (required): Word or phrase to search
- **Description:** Searches for a word or phrase in both author and title fields
- **Example:** `/products/search/basic?searchTerm=Gatsby`
- **Response:** 200 OK with List of ProductDTO

### 1.5 Advanced Search (Search by Criteria)

- **Endpoint:** `GET /products/search/criteria`
- **Query Parameters:**
  - `title` (optional): Book title
  - `author` (optional): Author name
  - `isbn` (optional): ISBN number
- **Description:** Search books by any combination of author, title, and ISBN
- **Example:** `/products/search/criteria?author=Fitzgerald&title=Gatsby`
- **Response:** 200 OK with List of ProductDTO

### 1.6 Update Product

- **Endpoint:** `PUT /products/{id}`
- **Request Body:** Same as Create (all fields optional)
- **Response:** 200 OK with updated ProductDTO

### 1.7 Delete Product

- **Endpoint:** `DELETE /products/{id}`
- **Response:** 204 No Content

---

## 2. Customers Endpoints

### 2.1 Create Customer

- **Endpoint:** `POST /customers`
- **Request Body:**

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "555-1234",
  "address": "123 Main St",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA"
}
```

- **Response:** 201 Created with CustomerDTO

### 2.2 Get Customer by ID

- **Endpoint:** `GET /customers/{id}`
- **Response:** 200 OK with CustomerDTO or 404 Not Found

### 2.3 Get All Customers

- **Endpoint:** `GET /customers`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of CustomerDTO

### 2.4 Get Customer by Email

- **Endpoint:** `GET /customers/search/email`
- **Query Parameters:**
  - `email` (required): Customer email
- **Response:** 200 OK with CustomerDTO or 404 Not Found

### 2.5 Update Customer

- **Endpoint:** `PUT /customers/{id}`
- **Request Body:** Same as Create (all fields optional)
- **Response:** 200 OK with updated CustomerDTO

### 2.6 Delete Customer

- **Endpoint:** `DELETE /customers/{id}`
- **Response:** 204 No Content

---

## 3. Orders Endpoints

### 3.1 Create Order

- **Endpoint:** `POST /orders`
- **Query Parameters:**
  - `customerId` (required): Customer ID
  - `notes` (optional): Order notes
- **Response:** 201 Created with OrderDTO

### 3.2 Get Order by ID

- **Endpoint:** `GET /orders/{id}`
- **Response:** 200 OK with OrderDTO or 404 Not Found

### 3.3 Get All Orders

- **Endpoint:** `GET /orders`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of OrderDTO

### 3.4 Get Orders by Customer

- **Endpoint:** `GET /orders/customer/{customerId}`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of OrderDTO

### 3.5 Get Orders by Status

- **Endpoint:** `GET /orders/status/{status}`
- **Path Parameters:**
  - `status`: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of OrderDTO

### 3.6 Update Order Status

- **Endpoint:** `PUT /orders/{id}/status`
- **Query Parameters:**
  - `status` (required): New order status
- **Response:** 200 OK with updated OrderDTO

### 3.7 Add Item to Order

- **Endpoint:** `POST /orders/{orderId}/items`
- **Query Parameters:**
  - `productId` (required): Product ID
  - `quantity` (required): Quantity to add
- **Response:** 200 OK with updated OrderDTO

### 3.8 Delete Order

- **Endpoint:** `DELETE /orders/{id}`
- **Response:** 204 No Content

---

## 4. Reviews Endpoints

### 4.1 Create Draft Review

- **Endpoint:** `POST /reviews`
- **Request Body:**

```json
{
  "productId": 1,
  "customerId": 1,
  "title": "Great book!",
  "content": "This book is amazing and I loved every page of it."
}
```

- **Description:** Creates a draft review that can be previewed before publishing. The book does not need to be one the customer bought.
- **Response:** 201 Created with ReviewDTO

### 4.2 Get Review by ID

- **Endpoint:** `GET /reviews/{id}`
- **Response:** 200 OK with ReviewDTO or 404 Not Found

### 4.3 Get Published Reviews for Product

- **Endpoint:** `GET /reviews/product/{productId}`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of published ReviewDTO

### 4.4 Get Reviews by Customer

- **Endpoint:** `GET /reviews/customer/{customerId}`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of ReviewDTO

### 4.5 Preview Draft Review

- **Endpoint:** `GET /reviews/preview`
- **Query Parameters:**
  - `productId` (required): Product ID
  - `customerId` (required): Customer ID
- **Description:** Allows users to preview their review before publishing
- **Response:** 200 OK with draft ReviewDTO or 404 Not Found

### 4.6 Publish Review

- **Endpoint:** `POST /reviews/{id}/publish`
- **Description:** Changes review status from DRAFT to PUBLISHED
- **Response:** 200 OK with published ReviewDTO

### 4.7 Update Draft Review

- **Endpoint:** `PUT /reviews/{id}`
- **Request Body:**

```json
{
  "title": "Updated title",
  "content": "Updated review content"
}
```

- **Description:** Update a draft review (only draft reviews can be updated)
- **Response:** 200 OK with updated ReviewDTO

### 4.8 Archive Review

- **Endpoint:** `POST /reviews/{id}/archive`
- **Description:** Archive a review (changes status to ARCHIVED)
- **Response:** 200 OK with archived ReviewDTO

### 4.9 Delete Review

- **Endpoint:** `DELETE /reviews/{id}`
- **Response:** 204 No Content

---

## 5. Ratings Endpoints

### 5.1 Rate Book (1-5 Stars)

- **Endpoint:** `POST /ratings`
- **Request Body:**

```json
{
  "productId": 1,
  "customerId": 1,
  "score": 4
}
```

- **Description:** Rate a book from 1 (bad) to 5 (good). The book does not need to be one the customer bought. Updates rating if customer already rated this book.
- **Validation:** Score must be between 1 and 5
- **Response:** 201 Created with RatingDTO

### 5.2 Get Rating by ID

- **Endpoint:** `GET /ratings/{id}`
- **Response:** 200 OK with RatingDTO or 404 Not Found

### 5.3 Get Rating by Product and Customer

- **Endpoint:** `GET /ratings/product/{productId}/customer/{customerId}`
- **Response:** 200 OK with RatingDTO or 404 Not Found

### 5.4 Get All Ratings for Product

- **Endpoint:** `GET /ratings/product/{productId}`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of RatingDTO

### 5.5 Get Average Rating for Product

- **Endpoint:** `GET /ratings/product/{productId}/average`
- **Response:** 200 OK with average rating as Double

### 5.6 Get All Ratings by Customer

- **Endpoint:** `GET /ratings/customer/{customerId}`
- **Query Parameters:** `page`, `size`, `sort`
- **Response:** 200 OK with Page of RatingDTO

### 5.7 Delete Rating

- **Endpoint:** `DELETE /ratings/{id}`
- **Response:** 204 No Content

---

## Data Models

### ProductDTO

```json
{
  "id": 1,
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "9780743273565",
  "price": 12.99,
  "description": "A classic novel",
  "coverImage": "url",
  "slug": "the-great-gatsby",
  "createdAt": "2026-02-23T10:00:00",
  "updatedAt": "2026-02-23T10:00:00",
  "averageRating": 4.5,
  "totalRatings": 10
}
```

### CustomerDTO

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "555-1234",
  "address": "123 Main St",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA",
  "createdAt": "2026-02-23T10:00:00",
  "updatedAt": "2026-02-23T10:00:00"
}
```

### OrderDTO

```json
{
  "id": 1,
  "customerId": 1,
  "customerName": "John Doe",
  "totalAmount": 25.98,
  "status": "PENDING",
  "items": [...],
  "notes": "Gift wrap if possible",
  "createdAt": "2026-02-23T10:00:00",
  "updatedAt": "2026-02-23T10:00:00"
}
```

### ReviewDTO

```json
{
  "id": 1,
  "productId": 1,
  "productTitle": "The Great Gatsby",
  "customerId": 1,
  "customerName": "John Doe",
  "title": "Great book!",
  "content": "This book is amazing...",
  "status": "PUBLISHED",
  "createdAt": "2026-02-23T10:00:00",
  "updatedAt": "2026-02-23T10:00:00"
}
```

### RatingDTO

```json
{
  "id": 1,
  "productId": 1,
  "productTitle": "The Great Gatsby",
  "customerId": 1,
  "customerName": "John Doe",
  "score": 4,
  "createdAt": "2026-02-23T10:00:00",
  "updatedAt": "2026-02-23T10:00:00"
}
```

---

## Usage Examples

### Example 1: Create and Search for Products

```bash
# Create a product
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"title":"The Great Gatsby","author":"F. Scott Fitzgerald","isbn":"9780743273565","price":12.99}'

# Basic search
curl http://localhost:8080/api/v1/products/search/basic?searchTerm=Gatsby

# Advanced search
curl "http://localhost:8080/api/v1/products/search/criteria?author=Fitzgerald&title=Gatsby"
```

### Example 2: Create Customer and Order

```bash
# Create customer
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","phone":"555-1234","address":"123 Main St"}'

# Create order
curl -X POST "http://localhost:8080/api/v1/orders?customerId=1"

# Add item to order
curl -X POST "http://localhost:8080/api/v1/orders/1/items?productId=1&quantity=2"
```

### Example 3: Create and Publish Review

```bash
# Create draft review
curl -X POST http://localhost:8080/api/v1/reviews \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"customerId":1,"title":"Great book!","content":"Amazing..."}'

# Preview review
curl "http://localhost:8080/api/v1/reviews/preview?productId=1&customerId=1"

# Publish review
curl -X POST http://localhost:8080/api/v1/reviews/1/publish
```

### Example 4: Rate a Book

```bash
# Rate a book (1-5 stars)
curl -X POST http://localhost:8080/api/v1/ratings \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"customerId":1,"score":5}'

# Get average rating
curl http://localhost:8080/api/v1/ratings/product/1/average
```

---

## Features Implemented

1. ✅ **Basic Search**: Search for books by word or phrase in author and title fields
2. ✅ **Advanced Search**: Search by any combination of author, title, and ISBN
3. ✅ **Book Ratings**: Users can rate books 1-5 (good to bad). Books don't need to be purchased
4. ✅ **Book Reviews**: Users can write reviews with preview functionality before publishing
5. ✅ **CRUD Operations**: Complete Create, Read, Update, Delete operations for:
   - Products (Books)
   - Customers
   - Orders
   - Order Items
6. ✅ **Database Integration**: PostgreSQL with JPA/Hibernate
7. ✅ **Pagination**: All list endpoints support pagination
8. ✅ **Error Handling**: Proper HTTP status codes and error responses

---

## Technology Stack

- **Framework**: Spring Boot 4.0.3
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Gradle
- **Language**: Java 21
- **Additional Libraries**: Lombok
