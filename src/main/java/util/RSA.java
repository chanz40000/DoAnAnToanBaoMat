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

            String testMessage = "Test message";
            // Ký thông điệp bằng Private Key
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(testMessage.getBytes());
            byte[] signedData = signature.sign();

            // Xác minh chữ ký bằng Public Key
            signature.initVerify(publicKey);
            signature.update(testMessage.getBytes());
            return signature.verify(signedData);
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
    public static void main(String[] args) {
        System.out.println(new RSA().validateRSAKeys("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"));
    }

    public boolean verifySignature(String data, String signatureBase64, String publicKeyBase64) {
        try {
            byte[] datas = data.getBytes();
            // Chuyển chuỗi khóa công khai Base64 thành PublicKey
            PublicKey publicKey = getPublicKeyFromString(publicKeyBase64);

            // Giải mã chữ ký từ Base64 thành mảng byte
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            // Tạo đối tượng Signature với thuật toán SHA256withRSA
            Signature signature = Signature.getInstance("SHA256withRSA");

            // Khởi tạo đối tượng Signature với khóa công khai
            signature.initVerify(publicKey);

            // Cập nhật dữ liệu cần xác minh (phải trùng khớp với dữ liệu đã ký ban đầu)
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            // Xác thực chữ ký và trả về kết quả
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
