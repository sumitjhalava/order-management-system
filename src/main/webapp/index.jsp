<!DOCTYPE html>
<html>
<head>
    <title>Order Management System API</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .endpoint { background: #f5f5f5; padding: 10px; margin: 10px 0; border-radius: 4px; }
        .method { font-weight: bold; color: #2c3e50; }
    </style>
</head>
<body>
    <h1>Order Management System - RESTful API</h1>
    
    <h2>API Endpoints</h2>
    
    <div class="endpoint">
        <div class="method">GET /orders</div>
        <div>Get all orders</div>
    </div>
    
    <div class="endpoint">
        <div class="method">GET /orders/{id}</div>
        <div>Get specific order by ID</div>
    </div>
    
    <div class="endpoint">
        <div class="method">POST /orders</div>
        <div>Create new order</div>
        <pre>
{
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
}
        </pre>
    </div>
    
    <div class="endpoint">
        <div class="method">PUT /orders/{id}</div>
        <div>Update order</div>
        <pre>
{
  "shipping_contact_mech_id": 2,
  "billing_contact_mech_id": 1
}
        </pre>
    </div>
    
    <div class="endpoint">
        <div class="method">DELETE /orders/{id}</div>
        <div>Delete order</div>
    </div>
    
    <h2>Database Setup</h2>
    <p>1. Create database: <code>CREATE DATABASE order_management_system;</code></p>
    <p>2. Run schema: <code>mysql -u root -p order_management_system &lt; database/schema.sql</code></p>
    <p>3. Insert data: <code>mysql -u root -p order_management_system &lt; database/data.sql</code></p>
    <p>4. Update credentials in <code>src/main/resources/database.properties</code></p>
</body>
</html>