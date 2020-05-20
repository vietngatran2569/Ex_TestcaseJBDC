package org.example;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

public class OrderTest {

    @Test
    public void customerCanMakeOrder() {
        Customer customer = new Customer();
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );
        Order order = customer.makeOrder(orderDetailList);
        assertThat(order).isNotNull();
    }

    @Test
    public void orderShouldContain2Products() {
        Customer customer = new Customer();
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );
        Order order = customer.makeOrder(orderDetailList);
        assertThat(order).isNotNull();
        assertThat(order.getOrderDetailList().size()).isEqualTo(2);
    }

    @Test
    public void orderShouldCalculateTheTotalProductInCart() {
        Customer customer = new Customer();
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );
        Order order = customer.makeOrder(orderDetailList);
        assertThat(order).isNotNull();
        assertThat(order.getOrderDetailList().size()).isEqualTo(2);
        assertThat(order.getTotal()).isEqualTo(11000);
    }

    @Test
    public void shouldGenerateHTML() throws IOException {
        Customer customer = new Customer();
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );

        OutputStream out = new FileOutputStream(new File("./orderHTML.html"));
        customer.makeOrder(orderDetailList).generate(out);

        List<String> contents = Files.readAllLines(Paths.get("./orderHTML.html"));
        assertThat(contents).isNotNull();
        assertThat(contents.size()).isGreaterThan(0);
    }

    @Test
    public void fileShouldContainsProductInIt() throws IOException {
        Customer customer = new Customer();
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );

        OutputStream out = new FileOutputStream(new File("./orderHTML.html"));
        customer.makeOrder(orderDetailList).generate(out);

        String contents = new String(Files.readAllBytes(Paths.get("./orderHTML.html")));
        assertThat(contents).isNotNull();
        assertThat(contents).contains("Laptop");
        assertThat(contents).contains("Tivi");
        assertThat(contents).contains("2000");
        assertThat(contents).contains("9000");

    }

    @Test
    public void fileShouldContainsTotal() throws IOException {
        Customer customer = new Customer();
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );

        OutputStream out = new FileOutputStream(new File("./orderHTML.html"));
        customer.makeOrder(orderDetailList).generate(out);

        String contents = new String(Files.readAllBytes(Paths.get("./orderHTML.html")));
        assertThat(contents).isNotNull();
        assertThat(contents).contains("Total");
        assertThat(contents).contains("11000");

    }

    @Test
    public void fileShouldContainsTotal1() throws IOException {
        Customer customer = new Customer(1, "nga");
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );

        OutputStream out = new FileOutputStream(new File("./orderHTML.html"));
        customer.makeOrder(orderDetailList).generate(out);

        String contents = new String(Files.readAllBytes(Paths.get("./orderHTML.html")));
        assertThat(contents).isNotNull();
        assertThat(contents).contains("Total");
        assertThat(contents).contains("11000");
        assertThat(contents).contains("nga");
    }
    @Test
    public void itShouldSaveOrderDetailToDatabase() throws IOException {
        Customer customer = new Customer(1, "nga");
        List<OrderDetail> orderDetailList = Arrays.asList(
                new OrderDetail(new Product(1, "Laptop", 1000), 2),
                new OrderDetail(new Product(2, "Tivi", 3000), 3)
        );

        OutputStream out = new FileOutputStream(new File("./orderHTML.html"));
        customer.makeOrder(orderDetailList).generate(out);

        String contents = new String(Files.readAllBytes(Paths.get("./orderHTML.html")));
        assertThat(contents).isNotNull();
        assertThat(contents).contains("Total");
        assertThat(contents).contains("11000");
        assertThat(contents).contains("nga");
    }

    @Test
    public void customerCanMakeOrder10() throws SQLException, IOException {
        Customer customer = new Customer(10,"nga");
        List<OrderDetail> orderDetailList = new ArrayList();
        OrderDetail orderDetail = new OrderDetail();
        Connection connection = Connect.getJDBCConnection();
        String sql = "SELECT customer.name as latName,product.id,product.name,product.price,order.quantity FROM `customer` INNER JOIN `order` on customer.id=order.id_customer INNER JOIN `product` on product.id=order.id_product";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while ((rs.next())) {
                orderDetailList.add(new OrderDetail(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("price")),
                        rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Order order = customer.makeOrder(orderDetailList);
        assertThat(order).isNotNull();

        OutputStream out = new FileOutputStream(new File("./orderHTML.html"));
        customer.makeOrder(orderDetailList).generate(out);

        String contents = new String(Files.readAllBytes(Paths.get("./orderHTML.html")));
        assertThat(contents).isNotNull();
        assertThat(contents).contains("Total");
        assertThat(contents).contains("130000");
        assertThat(contents).contains("nga");
    }

}
