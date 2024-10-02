package com.gin.msaflux.payment_service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component

public class PaymentConfig {
    private static final Random rnd = new Random();
    @Value("${vnp.url.pay}")
    public static String VNP_PAY_URL ;
    @Value("${vnp.url.return}")
    public static String VNP_RETURN_URL  ;
    @Value("${vnp.tmn.code}")
    public static String VNP_TMN_CODE ;
    @Value("${vnp.hash.secret}")
    public static String VNP_HASH_SECRET ;




    public static Mono<String> hashAllFields(Map<String, String> fields) {
        return Mono.fromCallable(()->{
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);
            StringBuilder sb = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = fields.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    sb.append(fieldName);
                    sb.append("=");
                    sb.append(fieldValue);
                }
                if (itr.hasNext()) {
                    sb.append("&");
                }
            }
            return hmacSHA512(VNP_HASH_SECRET,sb.toString());
        });
    }


    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }


    public static String getIpAddress(ServerHttpRequest request) {

            String ipAddress;
            try {
                ipAddress = request.getHeaders().getFirst("X-Forwarded-For");
                if (ipAddress == null || ipAddress.isEmpty()) {
                    ipAddress = "Unknown";
                }
            } catch (Exception e) {
                ipAddress = "Invalid IP: " + e.getMessage();
            }
            return ipAddress;

    }

    public static Mono<String> getRandomNumber(int len) {
        return Mono.fromCallable(() -> {
            String chars = "0123456789";
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(chars.charAt(rnd.nextInt(chars.length())));
            }
            return sb.toString();
        });
    }
}
