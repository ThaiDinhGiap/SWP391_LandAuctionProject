//package com.se1858.group4.Land_Auction_SWP391.utility;
//
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.Security;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//public class VapidKeyGenerator {
//
//    static {
//        // Thêm BouncyCastleProvider
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    public static void main(String[] args) {
//        try {
//            // Khởi tạo KeyPairGenerator với thuật toán EC (Elliptic Curve) và BouncyCastle
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
//            keyPairGenerator.initialize(256); // Kích thước khóa 256 bit (P-256)
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//
//            // Đảm bảo định dạng khóa công khai (SPKI) theo chuẩn VAPID
//            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
//            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
//            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//            byte[] encodedPublicKey = keyFactory.generatePublic(x509EncodedKeySpec).getEncoded();
//
//            // Mã hóa khóa công khai và khóa riêng thành chuỗi Base64 không có padding
//            String publicKey = Base64.getUrlEncoder().withoutPadding().encodeToString(encodedPublicKey);
//            String privateKey = Base64.getUrlEncoder().withoutPadding().encodeToString(keyPair.getPrivate().getEncoded());
//
//            System.out.println("Public Key: " + publicKey);
//            System.out.println("Private Key: " + privateKey);
//
//        } catch (Exception e) {
//            System.err.println("Error generating VAPID keys: " + e.getMessage());
//        }
//    }
//}
