package com.product.client;

import com.product.model.Product;
import com.product.stubs.product.ProductRequest;
import com.product.stubs.product.ProductResponse;
import com.product.stubs.product.ProductServiceGrpc;
import io.grpc.Channel;

public class ProductClient {
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub ;

    public ProductClient(Channel channel) {
        this.productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public Product getProduct(int productId) {
        ProductRequest productRequest = ProductRequest.newBuilder().setProductId(productId).build();
        ProductResponse productResponse = productServiceBlockingStub.getProductById(productRequest);
        Product product = getProduct(productResponse);
        return product;
    }

    private static Product getProduct(ProductResponse productResponse) {
        Product product = new Product();
        product.setId(productResponse.getProductId());
        product.setName(productResponse.getName());
        product.setPrice(productResponse.getPrice());
        product.setQuantity(productResponse.getQuantity());
        return product;
    }
}
