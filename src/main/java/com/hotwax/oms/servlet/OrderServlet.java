package com.hotwax.oms.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hotwax.oms.dao.OrderDAO;
import com.hotwax.oms.model.OrderHeader;
import com.hotwax.oms.model.OrderItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all orders
                response.getWriter().write(gson.toJson(orderDAO.getAllOrders()));
            } else {
                // Get specific order
                long orderId = Long.parseLong(pathInfo.substring(1));
                OrderHeader order = orderDAO.getOrderById(orderId);
                
                if (order != null) {
                    response.getWriter().write(gson.toJson(order));
                } else {
                    sendErrorResponse(response, 404, "Order not found");
                }
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(response, 400, "Invalid order ID");
        } catch (SQLException e) {
            sendErrorResponse(response, 500, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String requestBody = getRequestBody(request);
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
            
            OrderHeader order = new OrderHeader();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date orderDate = sdf.parse(jsonObject.get("order_date").getAsString());
            order.setOrderDate(orderDate);
            order.setCustomerId(jsonObject.get("customer_id").getAsLong());
            order.setShippingContactMechId(jsonObject.get("shipping_contact_mech_id").getAsLong());
            order.setBillingContactMechId(jsonObject.get("billing_contact_mech_id").getAsLong());

            long orderId = orderDAO.createOrder(order);
            
            // Create order items if provided
            if (jsonObject.has("order_items")) {
                jsonObject.getAsJsonArray("order_items").forEach(item -> {
                    try {
                        JsonObject itemObj = item.getAsJsonObject();
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrderId(orderId);
                        orderItem.setProductId(itemObj.get("product_id").getAsLong());
                        orderItem.setQuantity(itemObj.get("quantity").getAsInt());
                        orderItem.setStatus(itemObj.get("status").getAsString());
                        orderDAO.createOrderItem(orderItem);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            response.setStatus(201);
            response.getWriter().write("{\"order_id\":" + orderId + "}");
            
        } catch (ParseException | SQLException e) {
            sendErrorResponse(response, 400, "Invalid request data");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");

        try {
            String requestBody = getRequestBody(request);
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
            String pathInfo = request.getPathInfo();
            long orderId = Long.parseLong(pathInfo.substring(1));

            long shippingId = jsonObject.get("shipping_contact_mech_id").getAsLong();
            long billingId = jsonObject.get("billing_contact_mech_id").getAsLong();
            
            orderDAO.updateOrder(orderId, shippingId, billingId);
            response.getWriter().write("{\"message\":\"Order updated\"}");
            
        } catch (SQLException e) {
            sendErrorResponse(response, 500, "Database error");
        } catch (NumberFormatException e) {
            sendErrorResponse(response, 400, "Invalid order ID");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();
            long orderId = Long.parseLong(pathInfo.substring(1));
            
            orderDAO.deleteOrder(orderId);
            response.getWriter().write("{\"message\":\"Order deleted\"}");
            
        } catch (SQLException e) {
            sendErrorResponse(response, 500, "Database error");
        } catch (NumberFormatException e) {
            sendErrorResponse(response, 400, "Invalid order ID");
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}