# Order Management System - RESTful API

A clean, production-ready RESTful API for order management built with Java Servlets, JDBC, and MySQL.

## Features

- Complete CRUD operations for orders
- RESTful API design with proper HTTP methods
- JSON request/response format
- Clean MVC architecture with DAO pattern
- Connection pooling with HikariCP
- Input validation and error handling
- Industry-level best practices

## Technology Stack

- **Backend**: Java 11, Servlets
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **JSON Processing**: Gson
- **Connection Pool**: HikariCP
- **Server**: Apache Tomcat 9.0+

## Quick Start

### 1. Database Setup
```bash
# Create database
mysql -u root -p -e "CREATE DATABASE order_management_system;"

# Run schema
mysql -u root -p order_management_system < database/schema.sql

# Insert sample data
mysql -u root -p order_management_system < database/data.sql
```

### 2. Configure Database
Update `src/main/resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/order_management_system
db.username=your_username
db.password=your_password
```

### 3. Build and Deploy
```bash
# Build
mvn clean package

# Deploy to Tomcat
cp target/order-management-system.war $TOMCAT_HOME/webapps/
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/orders` | Get all orders |
| GET | `/orders/{id}` | Get order by ID |
| POST | `/orders` | Create new order |
| PUT | `/orders/{id}` | Update order |
| DELETE | `/orders/{id}` | Delete order |

### Example Usage

**Create Order:**
```bash
curl -X POST http://localhost:8080/order-management-system/orders \
  -H "Content-Type: application/json" \
  -d '{
    "order_date": "2024-01-15",
    "customer_id": 1,
    "shipping_contact_mech_id": 1,
    "billing_contact_mech_id": 2,
    "order_items": [
      {
        "product_id": 1,
        "quantity": 2,
        "status": "ORDERED"
      }
    ]
  }'
```

**Get All Orders:**
```bash
curl http://localhost:8080/order-management-system/orders
```

## Project Structure

```
src/
├── main/
│   ├── java/com/hotwax/oms/
│   │   ├── dao/OrderDAO.java          # Data Access Layer
│   │   ├── model/                     # POJOs
│   │   │   ├── Customer.java
│   │   │   ├── ContactMech.java
│   │   │   ├── Product.java
│   │   │   ├── OrderHeader.java
│   │   │   └── OrderItem.java
│   │   ├── servlet/OrderServlet.java  # REST Controller
│   │   └── util/DatabaseConnection.java # DB Utility
│   ├── resources/
│   │   └── database.properties        # DB Config
│   └── webapp/
│       ├── index.jsp                  # API Documentation
│       └── WEB-INF/web.xml           # Servlet Config
├── database/
│   ├── schema.sql                     # Database Schema
│   └── data.sql                       # Sample Data
└── pom.xml                           # Maven Config
```

## Best Practices Implemented

✅ **Clean Architecture**: Proper MVC separation with DAO pattern  
✅ **Security**: PreparedStatement prevents SQL injection  
✅ **Performance**: HikariCP connection pooling  
✅ **Scalability**: BIGINT IDs prevent overflow  
✅ **Maintainability**: Clean code structure and naming  
✅ **Error Handling**: Proper exception handling and validation  

## Database Schema

- **Customer**: Customer information
- **Contact_Mech**: Addresses and contact details
- **Product**: Product catalog
- **Order_Header**: Order information
- **Order_Item**: Order line items with cascade delete

## Development

```bash
# Run tests
mvn test

# Package
mvn package

# Clean build
mvn clean compile
```

## License

MIT License - see LICENSE file for details.