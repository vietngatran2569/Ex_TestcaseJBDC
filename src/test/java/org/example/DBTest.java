package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBTest {
    @Test
    public void itShouldGetCustomerId() throws SQLException {
        DbMangager db = new DbMangager();
        Customer customer = db.getCustomerById(1);
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(1);
    }

    @Test
    public void itShouldThrowExceptionIfCustomerNotFound() {
        DbMangager db = new DbMangager();
        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            db.getCustomerById(1000);
        });
        String expectedMessage = "Customer not found!";
        System.out.println(expectedMessage);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldGetProductById() throws SQLException {
        DbMangager db = new DbMangager();
        Product pro = db.getProductById(1);
        assertThat(pro).isNotNull();
        assertThat(pro.getProductId()).isEqualTo(1);

    }

    @Test
    public void itShouldThrowExceptionIfProductNotFound() {
        DbMangager db = new DbMangager();
        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            db.getProductById(10);
        });
        String expectedMessage = "Product not found!";
        System.out.println(expectedMessage);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldInsertOrderDetailProductToDB() throws SQLException {
        DbMangager db = new DbMangager();
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetail(new Product(1, "tao", 30), 10));
        orderDetails.add(new OrderDetail(new Product(2, "nho", 30), 10));
        orderDetails.add(new OrderDetail(new Product(3, "man", 30), 10));
        Order order = new Order(orderDetails, new Customer(1, "Nga"));
        order.setOrderDate(new Date(2000, 1, 3));
        Order newOrder = db.saveOrderDetail(order);

        assertThat(newOrder).isNotNull();
        assertThat(newOrder.getCustomer().getName()).isEqualTo("Nga");
       assertThat(newOrder.getOrderDetailList().size()).isEqualTo(3);
        assertThat(newOrder.getOrderDetailList().stream()
                .filter(orderDetail -> orderDetail.getProduct().getName().equals("nho")).count() >= 1).isTrue();
        assertThat(newOrder.getTotal()).isEqualTo(900);

    }
}
