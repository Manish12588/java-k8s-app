/**
 * K8s practice Java application package.
 */
package com.k8spractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Java K8s practice application.
 */
@SpringBootApplication
public final class Application {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private Application() {
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}