package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class OrderVerify  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int productId;
    private int quantity;


    public OrderVerify(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeInt(productId);
        oos.writeInt(quantity);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        this.productId = ois.readInt();
        this.quantity = ois.readInt();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
