package org.example;

import java.util.List;

public class Customer {
    private long id;
    private String name;

    public Customer() {
    }

    public Customer(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order makeOrder(List<OrderDetail> orderDetailList) {
        Order od = new Order(orderDetailList,this);
        return od;
    }
}
