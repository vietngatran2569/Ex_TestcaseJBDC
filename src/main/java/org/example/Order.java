package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

public class Order {
    private int id;
    private List<OrderDetail> orderDetailList;
    private long total;
    private Customer customer;
    private Date orderDate;

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public Order(List<OrderDetail> orderDetailList, Customer customer) {
        this.orderDetailList = orderDetailList;
        this.customer = customer;
        total = orderDetailList.stream()
                .mapToLong(orderDetail -> orderDetail.getTotal())
                .sum();
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getTotal() {
        return total;
    }

    public void generate(OutputStream out) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("./src/main/resources/orderTempl.html")));
        //System.out.println(content);
        String result = String.format(content,customer.getName(), getProductListRow(orderDetailList), total + "");
        try(PrintWriter writer = new PrintWriter(out)) {
            writer.print(result);
        };
    }

    public String getProductListRow(List<OrderDetail> orderDetails) {
        //<tr><td>product name</td><td>price</td><tr>
        //<tr><td>product name</td><td>price</td><tr>
//        StringBuilder builder = new StringBuilder();
        return orderDetails.stream().map(orderDetail -> getProductRow(orderDetail))
                 .reduce("", (x, y) -> x + y);
    }

    public String getProductRow(OrderDetail orderDetail) {
        //<tr><td>product name</td><td>price</td><tr>
        return String.format("<tr><td>%s</td><td>%s</td><tr>",
                orderDetail.getProduct().getName(),
                orderDetail.getTotal() + "");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }
}
