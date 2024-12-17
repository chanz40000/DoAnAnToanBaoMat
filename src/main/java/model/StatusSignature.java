package model;

public class StatusSignature {
    private int statusSignatureId;
    private String statusSignatureName;

    public StatusSignature(int statusSignatureId, String statusSignatureName) {
        this.statusSignatureId = statusSignatureId;
        this.statusSignatureName = statusSignatureName;
    }

    public StatusSignature(String statusSignatureName) {
        this.statusSignatureName = statusSignatureName;
    }

    public StatusSignature(int statusSignatureId) {
        this.statusSignatureId = statusSignatureId;
    }

    public int getStatusSignatureId() {
        return statusSignatureId;
    }

    public void setStatusSignatureId(int statusSignatureId) {
        this.statusSignatureId = statusSignatureId;
    }

    public String getStatusSignatureName() {
        return statusSignatureName;
    }

    public void setStatusSignatureName(String statusSignatureName) {
        this.statusSignatureName = statusSignatureName;
    }
}
