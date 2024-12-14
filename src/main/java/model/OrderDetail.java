package model;

import java.io.Serializable;

public class OrderDetail  implements Serializable {
    private int detailId;
    private Order order;
    private Product product;
    private int quantity;
    private double price;
    public OrderDetail(int detailId, Order order, Product product, int quantity, double price) {
        this.detailId = detailId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
    public OrderDetail(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
        this.quantity = quantity;
    }
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductId() {
        return product != null ? product.getProductId() : 0; // Trả về 0 nếu product null
    }

}
