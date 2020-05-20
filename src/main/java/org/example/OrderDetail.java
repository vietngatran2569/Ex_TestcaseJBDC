package org.example;

public class OrderDetail {
    private Product product;
    private int quantity;

    public OrderDetail(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderDetail() {
    }

    public long getTotal() {
        return (long) (product.getPrice() * quantity);
    }

    public Product getProduct() {
        return product;
    }

}
