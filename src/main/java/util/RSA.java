package util;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    KeyPair keyPair;
    PublicKey publicKey;
    PrivateKey privateKey;

    public RSA(){
        try {
            genKey(2048);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void genKey(int size)throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(size);
        keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public String getPublicKey(){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public String getPrivateKey(){
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public String[] generateKey(int size) throws Exception {
        // Tạo trình tạo cặp khóa
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(size);

        // Sinh cặp khóa
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Mã hóa khóa công khai và khóa riêng tư thành chuỗi Base64
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        // Trả về mảng chứa publicKey và privateKey
        return new String[]{publicKeyBase64, privateKeyBase64};
    }

    public PublicKey getPublicKeyFromString(String publicKeyBase64) throws Exception {
        // Giải mã chuỗi Base64 thành mảng byte
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // Sử dụng KeyFactory để tạo đối tượng PublicKey từ mảng byte
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public PrivateKey getPrivateKeyFromString(String privateKeyBase64) throws Exception {
        // Giải mã chuỗi Base64 thành mảng byte
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);

        // Sử dụng KeyFactory để tạo đối tượng PrivateKey từ mảng byte
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public boolean validateRSAKeys(String publicKeyBase64, String privateKeyBase64) {
        try {
            // Giải mã publicKey từ chuỗi Base64
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // Giải mã privateKey từ chuỗi Base64
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // Tạo thông điệp thử nghiệm
            String testMessage = "Hello RSA!";
            Cipher cipher = Cipher.getInstance("RSA");

            // Mã hóa bằng publicKey
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedMessage = cipher.doFinal(testMessage.getBytes());

            // Giải mã bằng privateKey
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

            // Kiểm tra thông điệp giải mã có khớp với ban đầu không
            String decryptedText = new String(decryptedMessage);
            return testMessage.equals(decryptedText);
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }


    public byte[]encrypt(String data){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            byte[]in = data.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(in);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }
    public String encryptBase64(String data)throws Exception{
        if (data == null) {
            throw new Exception("Encryption failed: encrypted data is null");
        }
        return Base64.getEncoder().encodeToString(encrypt(data));

    }
    public String decrypt(String base64)throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        byte[]in = Base64.getDecoder().decode(base64);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[]out = cipher.doFinal(in);
        return new String(out, StandardCharsets.UTF_8);
    }

}
