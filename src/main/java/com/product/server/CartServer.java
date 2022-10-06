package com.product.server;

import com.product.service.CartServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartServer {
    private static final Logger logger = Logger.getLogger(CartServer.class.getName());
    private Server server;

    public static void main(String[] args) {
        CartServer cartServer = new CartServer();
        cartServer.startServer();
        cartServer.blockUntilShutdown();

    }
    private void startServer() {
    int port = 50051;

    try {
        server = ServerBuilder.forPort(port)
                .addService(new CartServiceImpl())
                .build()
                .start();
        logger.log(Level.INFO, "Server Started on port "+port);

    } catch (Exception exception) {
        logger.log(Level.SEVERE, "Server Startup Error on Port : "+port, exception);
    }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.log(Level.ALL, "Server Shutdown in case of JVM Shutdown");
                CartServer.this.stopServer();
            }
        });
    }

    private void stopServer() {
        try {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Server JVM Shutdown Exception", e);
        }
    }




    private void blockUntilShutdown() {
        if(server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Server JVM Shutdown Exception", e);
            }
        }
    }

}
