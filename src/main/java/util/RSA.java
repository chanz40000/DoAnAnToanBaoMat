package util;

import database.KeyUserDAO;
import database.OrderDAO;
import database.OrderSignatureDAO;
import database.UserDAO;
import model.KeyUser;
import model.Order;
import model.OrderSignature;
import model.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
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
    public static void main(String[] args) throws Exception {
//        System.out.println(new RSA().validateRSAKeys("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"));
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        UserDAO userDAO= new UserDAO();
        User user = userDAO.selectById(5);
        KeyUser userKey = keyUserDAO.selectByUserIdStatus(user.getUserId(), "ON");
        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.selectById(18);
        OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
        OrderSignature orderSignature = orderSignatureDAO.selectByOrderId(18);
        String publickey = userKey.getKey().trim();
//        System.out.println(publickey);
        String sign = "o1G67w/oo5bvpp8S57Py7gv2XA/YLnoDWxxyX8SKIIDRSJEEr4RH+DAS181e2S2fLilfsLYNXO5Kb8jq5rkCy73PIn2bR1mx5KlnjzOXBBmyDNKC6Xd8rm2fthfWrMLCnN+gM1cZWegsNsNViM2k1MpxgBrCSea9zINDEgW+0nGmV3drOFCmC0Z8hFGMd8if6IhCmNqpd2qeuH3OF3Y5lZGXEPKCsTvRm8192L/B3/GZaZQ7PtnWDgpvh7XBKUdeKaoS3sMDLyH9nEfu4f10jhEpy+HU/cfQ+rpmx+iXepIqOW6kZ7LXNXkmX6JgA6SKhpjn6ufdFWo8k2Kk2As9nA==";
        String publickey2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0GPLMqThEj0zFukUJB7zTEGRdcPvs0OG9b68noERv0Gpa3Z4lpc/ioRQybVdw+nJWltdrmGQksDNq6eyii5PDkjlgUhNqXo9djHzbIsOq+phiwzl4LxdDcVLyZ8ucr5yRq5cBBSalajRjP27EqVCycKRyPEhWLK7llqd1fXt16k6zP03j35xhiIpy3AcrUU04ki/wuOWdnoKRhP63Nbc13tx/IHocXVMv28q4pgGlUiVQEf8mc1zfNKiNEEotG2R+YSpBPDnJx8DH8tLDEtbDJOcOJvuXkm8KZJPZQ9O6vRYmedFPFThsr9gHBWJvXNJvtx6cuRHrIAFCGTQe0HZ0wIDAQAB";
        System.out.println("public key: "+publickey2);
        String sign2 = "o1G67w/oo5bvpp8S57Py7gv2XA/YLnoDWxxyX8SKIIDRSJEEr4RH+DAS181e2S2fLilfsLYNXO5Kb8jq5rkCy73PIn2bR1mx5KlnjzOXBBmyDNKC6Xd8rm2fthfWrMLCnN+gM1cZWegsNsNViM2k1MpxgBrCSea9zINDEgW+0nGmV3drOFCmC0Z8hFGMd8if6IhCmNqpd2qeuH3OF3Y5lZGXEPKCsTvRm8192L/B3/GZaZQ7PtnWDgpvh7XBKUdeKaoS3sMDLyH9nEfu4f10jhEpy+HU/cfQ+rpmx+iXepIqOW6kZ7LXNXkmX6JgA6SKhpjn6ufdFWo8k2Kk2As9nA==";
        System.out.println("sign: "+sign2);
        //        String hash = orderSignature.getHash();
        String hash = "d50c4a15e6939a3c805464ea6488cffdee1d8206b4ab35252ec601e63b612ae7";
        System.out.println("hash: "+hash);

//        validateSignature(hash, sign, publickey);
        System.out.println(validateSignature(hash, publickey2, sign2));

    }
    public static boolean validateSignature(String hash, String publicKeyBase64, String encryptedBase64) {
        try {
            // Decode public key từ Base64
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Decode đoạn mã hóa từ Base64
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);

            // Giải mã bằng public key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Chuyển byte array của hash về hex string
            String decryptedHash = byteArrayToHexString(decryptedBytes);
            System.out.println("Hash giai ma: "+decryptedHash);
            // So sánh hash truyền vào với kết quả giải mã
            return decryptedHash.equals(hash);
        } catch (IllegalArgumentException e) {
            // Lỗi khi decode Base64
            System.err.println("Loi: Du lieu Base64 khong hop le.");
        } catch (InvalidKeySpecException e) {
            // Lỗi khi public key không đúng định dạng
            System.err.println("Loi: Public key khong hop le.");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // Lỗi cấu hình thuật toán
            System.err.println("Loi: Thuat toan ma hoa khong duoc ho tro.");
        } catch (InvalidKeyException e) {
            // Lỗi khi public key không hợp lệ
            System.err.println("Loi: Public key khong dung.");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            // Lỗi khi giải mã không thành công
            System.err.println("Loi: Khong the giai ma du lieu.");
        } catch (Exception e) {
            // Bắt tất cả các lỗi khác
            System.err.println("Loi: " + e.getMessage());
        }

        // Trả về false nếu có lỗi xảy ra
        return false;
    }

    public static String decryptHash(String publicKeyBase64, String sign) {
        try {
            // Decode public key từ Base64
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Decode đoạn mã hóa từ Base64
            byte[] encryptedBytes = Base64.getDecoder().decode(sign);

            // Giải mã bằng public key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Chuyển byte array của hash về hex string
            String decryptedHash = byteArrayToHexString(decryptedBytes);

            // Trả về hash đã giải mã
            return decryptedHash;
        } catch (IllegalArgumentException e) {
            // Lỗi khi decode Base64
            System.err.println("Loi: Du lieu Base64 khong hop le.");
        } catch (InvalidKeySpecException e) {
            // Lỗi khi public key không đúng định dạng
            System.err.println("Loi: Public key khong hop le.");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // Lỗi cấu hình thuật toán
            System.err.println("Loi: Thuat toan ma hoa khong duoc ho tro.");
        } catch (InvalidKeyException e) {
            // Lỗi khi public key không hợp lệ
            System.err.println("Loi: Public key khong dung.");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            // Lỗi khi giải mã không thành công
            System.err.println("Loi: Khong the giai ma du lieu.");
        } catch (Exception e) {
            // Bắt tất cả các lỗi khác
            System.err.println("Loi: " + e.getMessage());
        }

        // Trả về null nếu có lỗi xảy ra
        return null;
    }


    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public static PublicKey getPublicKeyFromStrings(String key) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(keyBytes));
    }

}
