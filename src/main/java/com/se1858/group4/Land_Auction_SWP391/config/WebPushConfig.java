package com.se1858.group4.Land_Auction_SWP391.config;

import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class WebPushConfig {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${vapid.public.key}")
    private String vapidPublicKey;

    @Value("${vapid.private.key}")
    private String vapidPrivateKey;

    @Bean
    public PushService pushService() throws Exception{
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        // Decode public key in correct X.509 format
        byte[] publicKeyBytes = Base64.getUrlDecoder().decode(vapidPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        System.out.println(publicKey);
        // Create PushService instance
        PushService pushService = new PushService()
                .setPublicKey(publicKey)
                .setPrivateKey(vapidPrivateKey);

        pushService.setSubject("mailto:giaptdhe186094@fpt.edu.vn"); // Thay bằng email của bạn hoặc thông tin liên hệ khác
        return pushService;
    }
}
