package com.product.service;

import com.product.client.ProductClient;
import com.product.model.Product;
import com.product.stubs.cart.CartDetail;
import com.product.stubs.cart.CartGrpc;
import com.product.stubs.cart.CartRequest;
import com.product.stubs.cart.CartResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CartServiceImpl extends CartGrpc.CartImplBase {
    private static final Logger logger = Logger.getLogger(CartServiceImpl.class.getName());

    @Override
    public void addToCart(CartRequest request, StreamObserver<CartResponse> responseObserver) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:50052").usePlaintext().build();
        ProductClient productClient = new ProductClient(managedChannel);
        List<Double> price = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        AtomicReference<String> message = new AtomicReference<>("");
        AtomicBoolean added = new AtomicBoolean(true);
        List<com.product.stubs.cart.Product> cartProducts = request.getProductList().stream().map(p -> {
            Product product = productClient.getProduct(p.getProductId());
            com.product.stubs.cart.Product.Builder builder = com.product.stubs.cart.Product.newBuilder();
            builder.setProductId(product.getId());

            if (p.getQuantity() <= product.getQuantity()) {
                builder.setQuantity(p.getQuantity());
                price.add(p.getQuantity() * product.getPrice());
                quantity.add(p.getQuantity());
            } else {
                logger.log(Level.INFO, "Unable to add product" + product.getId() + " - " + product.getName() + " available quantity " + product.getQuantity());
                builder.setQuantity(0);
                added.set(false);
                message.set("Unable to add product" + product.getId() + " - " + product.getName() + " available quantity " + product.getQuantity());
            }
            com.product.stubs.cart.Product cartProduct = builder.build();
            return cartProduct;
        }).collect(Collectors.toList());

        Double totalAmount = price.stream().reduce(0.0, (prev, p) -> prev+p);
        Integer totalQuantity = quantity.stream().reduce(0, (prev, q) -> prev+q);

        CartDetail cartDetail = CartDetail.newBuilder().setTotalAmount(totalAmount).addAllProduct(cartProducts).setTotalQuantity(totalQuantity).build();
        CartResponse cartResponse = CartResponse.newBuilder().setAdded(added.get()).setMessage(message.get()).setCartDetail(cartDetail).build();


        try {
            managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Unable to Shutdown managed Channel", e);
        }

        responseObserver.onNext(cartResponse);
        responseObserver.onCompleted();
    }
}
