package org.example;

import java.sql.*;
import java.util.Optional;

public class DbMangager {
    public Connection getConnect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/exam3?useSSL=false";
        String user = "root";
        String pass = "tranthivietnga";
        return DriverManager.getConnection(url, user, pass);
    }

    public Customer getCustomerById(int i) throws SQLException {
        Connection connection = getConnect();
        Customer customer = null;
        String sql = "Select name,id from customer where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, i);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            customer = new Customer(id, name);
        }
        return Optional.ofNullable(customer)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found!"));
    }

    public Product getProductById(int id) throws SQLException {
        Connection connection = getConnect();
        Product product = null;
        String sql = "Select price,name,id from product where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int idProduct = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Double price = resultSet.getDouble("price");
            product = new Product(id, name, price);
        }
        return Optional.ofNullable(product)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    public Order saveOrderDetail(Order order) throws SQLException {
        String sql = "insert into `order` (id_customer, order_date, total) values (?, ?, ?);";
        Connection connection = getConnect();
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, order.getCustomer().getId());
        ps.setDate(2, order.getOrderDate());
        ps.setDouble(3, order.getTotal());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()){
            int idResult = rs.getInt(1);
            order.setId(idResult);
        }
        return order;
    }
}
