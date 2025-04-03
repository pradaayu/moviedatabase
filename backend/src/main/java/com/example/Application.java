package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;

import controller.AuthController;
import controller.OmdbController;
import controller.UserController;
import controller.UserMovieController;
import controller.UserReviewController;
import service.AuthenticationService;
import service.UserMovieService;
import service.UserReviewService;
import service.UserService;

/**
 * Bootstraps the application: initializes & starts the application.
 * <br/>
 * This code initializes the application context, scans for components, and starts the embedded server (if applicable).
 * <br/>
 * Bootstrapping ensures that all necessary resources and configurations are in place for the application to function properly.
 */
@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class,
	    HibernateJpaAutoConfiguration.class,
	    R2dbcAutoConfiguration.class
	})
@EntityScan("model")  // Keep this if you need validation, but database ops won't work
@ComponentScan(
	    basePackages = {"com.example", "service", "controller", "config"}, //  Spring will scan for components in all the packages specified
	    excludeFilters = {
	        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
	            AuthController.class,
	            AuthenticationService.class,
	            // Add other database-dependent components here
	            UserController.class,
	            UserService.class,
	            UserMovieController.class,
	            UserMovieService.class,
	            UserReviewController.class,
	            UserReviewService.class,
	        })
	    }
	)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
