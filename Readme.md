# Coupon Demo API

This is the `coupon-demo` API, a Spring Boot REST API for managing coupons. It provides endpoints to create, retrieve, update, and delete coupons, as well as apply them to a shopping cart.

## Getting Started

### Prerequisites

- Java 17 or later
- PostgreSQL Database
- Maven

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/brijeshivam/monkcomTask
   cd coupon-demo

2. Set up your PostgreSQL database and update application.properties with your database credentials.

3. Build the project:

    ```bash
   mvn clean install

4. Run the application:

    ```bash
    mvn spring-boot:run
After the application starts, the API will be available at http://localhost:8080/api/v1.

### API Endpoints
1. Get Coupon by ID
   Retrieve a specific coupon by its ID.
  
    URL: /api/v1/coupons/{id}
    Method: GET
    Response Status: 200 OK
    Example Request:
    ```bash
    curl -X GET http://localhost:8080/api/v1/coupons/1

2. Get All Coupons
   Retrieve all coupons.

    URL: /api/v1/coupons
    Method: GET
    Response Status: 200 OK
    Example Request:

    ```bash
    curl -X GET http://localhost:8080/api/v1/coupons

3. Add a New Coupon

   Add a new coupon to the system.

    URL: /api/v1/coupons
    Method: POST
    Content-Type: application/json
    Response Status: 201 Created
    Example Request:
    ```bash
    curl -X POST http://localhost:8080/api/v1/coupons \
    -H "Content-Type: application/json" \
    -d '{
    "type": "product-wise",
    "details": {
    "discount": 10,
    "product_id": 123
    }
    }'
4. Update Coupon by ID

   Update an existing coupon.

    URL: /api/v1/coupons/{id}
    Method: PUT
    Content-Type: application/json
    Response Status: 204 No Content
    Example Request:
    ```bash
    curl -X PUT http://localhost:8080/api/v1/coupons/1 \
    -H "Content-Type: application/json" \
    -d '{
    "type": "product-wise",
    "details": {
    "discount": 15,
    "product_id": 123
    }
    }'
5. Delete Coupon by ID

    Delete a coupon by its ID.

    URL: /api/v1/coupons/{id}
    Method: DELETE
    Response Status: 204 No Content
    Example Request:
    ```bash
    curl -X DELETE http://localhost:8080/api/v1/coupons/1
6. Get Applicable Coupons for a Cart

   Retrieve a list of applicable coupons for a given cart.

    URL: /api/v1/applicable-coupons
    Method: POST
    Content-Type: application/json
    Response Status: 200 OK
    Example Request:
    ```bash
    curl -X POST http://localhost:8080/api/v1/applicable-coupons \
    -H "Content-Type: application/json" \
    -d '{"cart": {
    "items": [
    {"product_id": 1, "quantity": 6, "price": 50}, // Product X
    {"product_id": 2, "quantity": 3, "price": 30}, // Product Y
    {"product_id": 3, "quantity": 2, "price": 25} // Product Z
    ]
    }}'
7. Apply a Specific Coupon to a Cart

   Apply a coupon by ID to a shopping cart.

    URL: /api/v1/apply-coupon/{id}
    Method: POST
    Content-Type: application/json
    Response Status: 200 OK
    Example Request:
    ```bash
    curl -X POST http://localhost:8080/api/v1/apply-coupon/1 \
    -H "Content-Type: application/json" \
    -d '{"cart": {
    "items": [
    {"product_id": 1, "quantity": 6, "price": 50}, // Product X
    {"product_id": 2, "quantity": 3, "price": 30}, // Product Y
    {"product_id": 3, "quantity": 2, "price": 25} // Product Z
    ]
    }}'
### Testing
Use tools like curl or Postman to interact with the endpoints.
The API responses will vary based on the data in your database.
Notes
Make sure the database connection and configurations are correctly set up in application.properties.
Use @PostConstruct for initialization, logging, or setup tasks.
License
MIT License.

### What is not implemented
#### Apply coupon by its ID. (/apply-coupon/{id})
#### Reason: Time constraints
##### Approach:
1. Take id and object body from the request posted by user.
2. Process this initially for semantic errors inside the Service class.
3. Get the specified coupon by its id from database.
4. If coupon type is cart-wise then compare cart total with its threshold price and calculate the discount.
5. If coupon type is product-wise then compare the cart with the product specified in coupon. If the exists then calculate applicable discount.
6. If coupon type is bxgy then compare the cart with the product and quantity specified in coupon. If the condition meets calculate applicable discount. 
7. Return the result to the user.

### Limitations
This is based on synchronous programming so not much suitable for scalable scenario.
Instead we can use Quarkus and its reactive programming for scalability.

### Assumptions
coupon_id is assumed to be an auto generated int value.
