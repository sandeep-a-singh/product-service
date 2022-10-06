package com.product.service;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import com.product.dao.ProductDao;
import com.product.stubs.cart.Product;
import com.product.stubs.product.ProductRequest;
import com.product.stubs.product.ProductResponse;
import com.product.stubs.product.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    ProductDao productDao = new ProductDao();

    @Override
    public void getProductById(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        com.product.model.Product product = productDao.getProductById(request.getProductId());

        ProductResponse response = ProductResponse.newBuilder()
                .setProductId(product.getId())
                .setExpiry(Timestamps.fromMillis(product.getExpiry().getTime()))
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
