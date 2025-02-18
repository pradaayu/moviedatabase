package com.example;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

public class Server {
    // Define CORS header names as constants for better maintainability
    private static final HttpString CORS_ALLOW_ORIGIN = new HttpString("Access-Control-Allow-Origin");
    private static final HttpString CORS_ALLOW_METHODS = new HttpString("Access-Control-Allow-Methods");
    private static final HttpString CORS_ALLOW_HEADERS = new HttpString("Access-Control-Allow-Headers");

    public static void main(String[] args) {
        // Create a resource handler for static files
        ResourceHandler resourceHandler = new ResourceHandler(
            new ClassPathResourceManager(Server.class.getClassLoader(), "static")
        );

        // Create the server
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(exchange -> {
                    // Set CORS headers for development
                    // Using putString instead of put for header values
                	exchange.getResponseHeaders()
	                    .put(CORS_ALLOW_ORIGIN, "*")
	                    .put(CORS_ALLOW_METHODS, "GET, POST, OPTIONS")
	                    .put(CORS_ALLOW_HEADERS, "Content-Type");

                    if (exchange.getRequestMethod().equalToString("OPTIONS")) {
                        exchange.setStatusCode(StatusCodes.NO_CONTENT);
                        exchange.endExchange();
                        return;
                    }

                    // Handle API requests
                    if (exchange.getRequestPath().startsWith("/api/")) {
                        handleApiRequest(exchange);
                        return;
                    }

                    // Serve static files
                    resourceHandler.handleRequest(exchange);
                })
                .build();

        // Start the server
        server.start();
        System.out.println("Server started on port 8080");
    }

    private static void handleApiRequest(HttpServerExchange exchange) {
        if (exchange.getRequestPath().equals("/api/hello")) {
            // Using the built-in Headers.CONTENT_TYPE constant
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"message\": \"Hello from Undertow!\"}");
        } else {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
            exchange.getResponseSender().send("API endpoint not found");
        }
    }
}