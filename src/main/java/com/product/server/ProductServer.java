package com.product.server;

import com.product.service.ProductServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductServer {
    private static final Logger logger = Logger.getLogger(ProductServer.class.getName());

    private Server server;

    public void startServer() {
        int port = 50052;
        try {
        server = ServerBuilder.forPort(port)
                .addService(new ProductServiceImpl())
                .build()
                .start();

        logger.log(Level.INFO, "Server Started on port "+port);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    logger.log(Level.ALL, "Server Shutdown in case of JVM Shutdown");

                    ProductServer.this.stopServer();
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server JVM Shutdown Exception", e);
        }

    }

    private void stopServer() {
        try {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.ALL, "Server Shutdown Error", e);
        }
    }

    public void blockUntilShutdown() {
        if(server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Unable to Stop Server", e);

            }
        }
    }

    public static void main(String[] args) {
        ProductServer productServer = new ProductServer();
        productServer.startServer();
        productServer.blockUntilShutdown();
    }
}

