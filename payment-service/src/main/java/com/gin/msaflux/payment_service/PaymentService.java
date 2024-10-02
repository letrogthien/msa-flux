package com.gin.msaflux.payment_service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {
    public Mono<Object> returnUrl(ServerHttpRequest request) {
        Map<String, String> map;
        map = request.getQueryParams().toSingleValueMap();
        String vnpSecureHash = map.get("vnp_SecureHash");
        map.remove("vnp_SecureHashType");
        map.remove("vnp_SecureHash");
        return PaymentConfig.hashAllFields(map).flatMap(
                hash -> {
                    if (!hash.equals(vnpSecureHash)) {
                        return Mono.error(new RuntimeException("Hash doesn't match"));
                    }
                    if (!map.get("vnp_ResponseCode").equalsIgnoreCase("00")){
                        return Mono.just("PAYMENT_ERROR");
                    }
                    return Mono.just("PAYMENT_SUCCESSFULLY");
                }
        );
    }

    public Mono<String> createOrder(ServerHttpRequest serverRequest, int total, String orderInfo) {
        return PaymentConfig.getRandomNumber(8)
                .flatMap(vnpTxnRef ->
                        Mono.fromCallable(() -> {
                            String vnpVersion = "2.1.0";
                            String vnpCommand = "pay";
                            String vnpTmnCode = PaymentConfig.VNP_TMN_CODE;
                            String orderType = "order-type";
                            String ip = PaymentConfig.getIpAddress(serverRequest);

                            Map<String, String> vnpParams = new HashMap<>();
                            vnpParams.put("vnp_Version", vnpVersion);
                            vnpParams.put("vnp_Command", vnpCommand);
                            vnpParams.put("vnp_TmnCode", vnpTmnCode);
                            vnpParams.put("vnp_Amount", String.valueOf(total * 100));
                            vnpParams.put("vnp_CurrCode", "VND");

                            vnpParams.put("vnp_TxnRef", vnpTxnRef);
                            vnpParams.put("vnp_OrderInfo", orderInfo);
                            vnpParams.put("vnp_OrderType", orderType);

                            String locate = "vn";
                            vnpParams.put("vnp_Locale", locate);

                            String urlReturn = PaymentConfig.VNP_RETURN_URL;
                            vnpParams.put("vnp_ReturnUrl", urlReturn);
                            vnpParams.put("vnp_IpAddr", ip);

                            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                            String vnpCreateDate = formatter.format(cld.getTime());
                            vnpParams.put("vnp_CreateDate", vnpCreateDate);

                            cld.add(Calendar.MINUTE, 15);
                            String vnpExpireDate = formatter.format(cld.getTime());
                            vnpParams.put("vnp_ExpireDate", vnpExpireDate);

                            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
                            Collections.sort(fieldNames);
                            StringBuilder hashData = new StringBuilder();
                            StringBuilder query = new StringBuilder();
                            for (String fieldName : fieldNames) {
                                String fieldValue = vnpParams.get(fieldName);
                                if (fieldValue != null && !fieldValue.isEmpty()) {
                                    hashData.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                                            .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                                            .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                                    if (!fieldName.equals(fieldNames.getLast())) {
                                        query.append('&');
                                        hashData.append('&');
                                    }
                                }
                            }

                            String queryUrl = query.toString();
                            String vnpSecureHash = PaymentConfig.hmacSHA512(PaymentConfig.VNP_HASH_SECRET, hashData.toString());
                            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
                            return PaymentConfig.VNP_PAY_URL + "?" + queryUrl;
                        })
                );
    }

}
