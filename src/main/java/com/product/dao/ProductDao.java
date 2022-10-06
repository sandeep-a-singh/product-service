package com.product.dao;

import com.product.db.H2DatabaseConnection;
import com.product.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDao {
    private static final Logger logger = Logger.getLogger(ProductDao.class.getName());

    public Product getProductById(int productId) {
        Product product = new Product();

        try (Connection connection = H2DatabaseConnection.getConnectionToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from product where id=?")) {
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setExpiry(resultSet.getDate("expiry"));
                product.setPrice(resultSet.getDouble("price"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to retrieve User record", e);

        }
        return product;
    }
}
