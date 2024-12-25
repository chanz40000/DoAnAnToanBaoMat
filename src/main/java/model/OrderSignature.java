package model;

import java.sql.Timestamp;

public class OrderSignature {
    private int id;
    private Order orderId;
    private String signature;
    private String hashOrder;
    private boolean isSignatureVerified;
    private KeyUser keyUser;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor
//    public OrderSignature(int id, Order orderId,  String signature,  Timestamp createdAt, Timestamp updatedAt,boolean isSignatureVerified) {
//        this.id = id;
//        this.orderId = orderId;
//        this.signature = signature;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.isSignatureVerified = isSignatureVerified;
//    }

    public OrderSignature(int id, Order orderId, String signature, String hashOrder, boolean isSignatureVerified, KeyUser keyUser, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.signature = signature;
        this.hashOrder = hashOrder;
        this.isSignatureVerified = isSignatureVerified;
        this.keyUser = keyUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderSignature(Order orderId, String signature, boolean isSignatureVerified, KeyUser keyUser, Timestamp createdAt) {
        this.orderId = orderId;
        this.signature = signature;
        this.isSignatureVerified = isSignatureVerified;
        this.keyUser = keyUser;
        this.createdAt = createdAt;
    }

    public OrderSignature(String hashOrder, Order orderId) {
        this.hashOrder = hashOrder;
        this.orderId = orderId;
    }

    public String getHashOrder() {
        return hashOrder;
    }

    public void setHashOrder(String hashOrder) {
        this.hashOrder = hashOrder;
    }

    public KeyUser getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(KeyUser keyUser) {
        this.keyUser = keyUser;
    }

    public OrderSignature(Order orderId, String hash) {
        this.orderId = orderId;
    }

    public boolean isSignatureVerified() {
        return isSignatureVerified;
    }

    public void setSignatureVerified(boolean signatureVerified) {
        isSignatureVerified = signatureVerified;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                ", signature='" + signature + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
