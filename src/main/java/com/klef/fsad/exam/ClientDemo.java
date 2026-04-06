package com.klef.fsad.exam;

import static spark.Spark.*;

public class ClientDemo {
    public static void main(String[] args) {
        // Start Embedded Web Server on port 8080
        port(8080);
        
        // Initialize the REST API routes
        CustomerController.initRoutes();

        // Optional: Keep a generic test route
        get("/", (req, res) -> {
            res.type("text/html");
            return "<html><body><h2>Spark Backend REST API is running.</h2>"
                    + "<p>Go to <code>/api/customers</code> to view available data.</p></body></html>";
        });

        System.out.println("Backend Server is successfully running.");
        System.out.println("API endpoints initialized on http://localhost:8080/api/customers");
    }
}
