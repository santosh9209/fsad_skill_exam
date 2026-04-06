package com.klef.fsad.exam;

import com.google.gson.Gson;
import static spark.Spark.*;

public class CustomerController {

    private static CustomerDao customerDao = new CustomerDao();
    private static Gson gson = new Gson();

    public static void initRoutes() {
        // Enable CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });

        // 1. GET all customers
        get("/api/customers", (req, res) -> {
            return gson.toJson(customerDao.getAllCustomers());
        });

        // 2. GET customer by ID
        get("/api/customers/:id", (req, res) -> {
            Long id = Long.parseLong(req.params(":id"));
            CustomerAccount customer = customerDao.getCustomerById(id);
            if (customer != null) {
                return gson.toJson(customer);
            } else {
                res.status(404);
                return "{\"message\": \"Customer not found\"}";
            }
        });

        // 3. POST new customer
        post("/api/customers", (req, res) -> {
            CustomerAccount newCustomer = gson.fromJson(req.body(), CustomerAccount.class);
            CustomerAccount createdCustomer = customerDao.addCustomer(newCustomer);
            if (createdCustomer != null) {
                res.status(201);
                return gson.toJson(createdCustomer);
            } else {
                res.status(500);
                return "{\"message\": \"Failed to create customer\"}";
            }
        });

        // 4. PUT update existing customer
        put("/api/customers/:id", (req, res) -> {
            Long id = Long.parseLong(req.params(":id"));
            CustomerAccount updatedCustomerInfo = gson.fromJson(req.body(), CustomerAccount.class);
            CustomerAccount updated = customerDao.updateCustomer(id, updatedCustomerInfo);
            if (updated != null) {
                return gson.toJson(updated);
            } else {
                res.status(404);
                return "{\"message\": \"Customer not found or failed to update\"}";
            }
        });

        // 5. DELETE customer
        delete("/api/customers/:id", (req, res) -> {
            Long id = Long.parseLong(req.params(":id"));
            boolean deleted = customerDao.deleteCustomer(id);
            if (deleted) {
                return "{\"message\": \"Customer deleted successfully\"}";
            } else {
                res.status(404);
                return "{\"message\": \"Customer not found or failed to delete\"}";
            }
        });
    }
}
