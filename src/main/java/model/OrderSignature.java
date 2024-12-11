package model;

import java.sql.Timestamp;

public class OrderSignature {
    private int id;
    private Order orderId;
    private String hash;
    private String signature;
    private StatusOrder statusId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor
    public OrderSignature(int id, Order orderId, String hash, String signature, StatusOrder statusId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.hash = hash;
        this.signature = signature;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public StatusOrder getStatusId() {
        return statusId;
    }

    public void setStatusId(StatusOrder statusId) {
        this.statusId = statusId;
    }

    @Override
    public String toString() {
        return "OrderSignature{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", hash='" + hash + '\'' +
                ", signature='" + signature + '\'' +
                ", statusId=" + statusId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
