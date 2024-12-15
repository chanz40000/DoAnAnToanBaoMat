package model;

import java.sql.Timestamp;

public class OrderSignature {
    private int id;
    private Order orderId;
    private String hash;
    private String signature;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor
    public OrderSignature(int id, Order orderId, String hash, String signature,  Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.hash = hash;
        this.signature = signature;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderSignature(Order orderId, String hash) {
        this.orderId = orderId;
        this.hash = hash;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }



    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }



    @Override
    public String toString() {
        return "OrderSignature{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", hash='" + hash + '\'' +
                ", signature='" + signature + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
