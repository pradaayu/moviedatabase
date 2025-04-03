package com.example;

import java.io.File;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

public class Server {
    // Define CORS header names as constants for better maintainability
    private static final HttpString CORS_ALLOW_ORIGIN = new HttpString("Access-Control-Allow-Origin");
    private static final HttpString CORS_ALLOW_METHODS = new HttpString("Access-Control-Allow-Methods");
    private static final HttpString CORS_ALLOW_HEADERS = new HttpString("Access-Control-Allow-Headers");
    private static final String webroot = "../frontend/public";

    public static void main(String[] args) {
    	File root  = new File(webroot);
        if (!root.exists() || !root.isDirectory()) {
            System.err.println("Error: Static file directory does not exist: " + root.getAbsolutePath());
            return;
        }
        
        // Handle static files in a streamlined way
        HttpHandler staticContentHandler = new ResourceHandler(
        	    new FileResourceManager(root, 1024) 
        	    // 1024 => This is the transfer buffer size (in bytes), used for efficient file reading.
        	)
        		.setWelcomeFiles("index.html")
        		.setDirectoryListingEnabled(false);  // Enable directory listing
//        ResourceHandler staticContentHandler = new ResourceHandler(
//        		new FileResourceManager(new File(webroot), 1024)
//        ).setDirectoryListingEnabled(true);

        // API Routing Handler
        HttpHandler apiHandler = Handlers.routing()
                .get("/user", userHandler())
                .get("/review", reviewHandler());
        
        // Wrap API Handler with CORS Handling
        HttpHandler corsHandler = addCorsHeaders(apiHandler);
        
        // Create a PathHandler for routing
        PathHandler pathHandler = Handlers.path(notFoundHandler())
                .addPrefixPath("/", staticContentHandler) // Serve static files
                .addPrefixPath("/api", corsHandler); // Serve API endpoints
        
        // Create the server
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(pathHandler
                    // Set CORS headers for development
                    // Using putString instead of put for header values
//                	exchange.getResponseHeaders()
//	                    .put(CORS_ALLOW_ORIGIN, "*")
//	                    .put(CORS_ALLOW_METHODS, "GET, POST, OPTIONS")
//	                    .put(CORS_ALLOW_HEADERS, "Content-Type");
//
//                    if (exchange.getRequestMethod().equalToString("OPTIONS")) {
//                        exchange.setStatusCode(StatusCodes.NO_CONTENT);
//                        exchange.endExchange();
//                        return;
//                    }
//
//                    // Handle API requests
//                    if (exchange.getRequestPath().startsWith("/api/")) {
//                        handleApiRequest(exchange);
//                        return;
//                    }
//
//                    // Serve static files
//                    resourceHandler.handleRequest(exchange);
                )
                .build();

        // Start the server
        server.start();
        System.out.println("Server started on port 8080");
    }
    
    // CORS Middleware
    private static HttpHandler addCorsHeaders(HttpHandler nextHandler) {
        return exchange -> {
            exchange.getResponseHeaders().put(CORS_ALLOW_ORIGIN, "*");
            exchange.getResponseHeaders().put(CORS_ALLOW_METHODS, "GET, POST, OPTIONS");
            exchange.getResponseHeaders().put(CORS_ALLOW_HEADERS, "Content-Type");

            if (exchange.getRequestMethod().equalToString("OPTIONS")) {
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
                exchange.endExchange();
            } else {
                nextHandler.handleRequest(exchange);
            }
        };
    }

    // API Handlers
    private static HttpHandler userHandler() {
        return exchange -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"message\": \"Hello from Undertow!\"}");
        };
    }

    private static HttpHandler reviewHandler() {
        return exchange -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"message\": \"Greetings from the API!\"}");
        };
    }  
    

    private static HttpHandler notFoundHandler() {
        return exchange -> {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"error\": \"Not Found\"}");
        };
    }
    
//    private static void handleApiRequest(HttpServerExchange exchange) {
//        if (exchange.getRequestPath().equals("/api/hello")) {
//            // Using the built-in Headers.CONTENT_TYPE constant
//            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
//            exchange.getResponseSender().send("{\"message\": \"Hello from Undertow!\"}");
//        } else {
//            exchange.setStatusCode(StatusCodes.NOT_FOUND);
//            exchange.getResponseSender().send("API endpoint not found");
//        }
//    }
}