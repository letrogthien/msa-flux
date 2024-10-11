package com.gin.msaflux.payment_service.services.impl;

import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.common.kafka.payload.PaymentPayload;
import com.gin.msaflux.common.kafka.status.PaymentMethod;
import com.gin.msaflux.common.kafka.status.Status;
import com.gin.msaflux.payment_service.PaymentConfig;
import com.gin.msaflux.payment_service.kafka.KafkaUtils;
import com.gin.msaflux.payment_service.models.Payment;
import com.gin.msaflux.payment_service.repositories.PaymentRepository;
import com.gin.msaflux.payment_service.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final KafkaUtils kafkaUtils;
    private final PaymentRepository paymentRepository;


    @Override
    public Mono<Void> paymentMethod(PaymentPayload paymentPayload) {
        PaymentMethod type = paymentPayload.getPaymentType();
        Payment payment = Payment.builder()
                .paymentMethod(type)
                .transactionId(String.valueOf(Math.random())) // Ideally use a secure ID generator instead of Math.random
                .amount(paymentPayload.getTotal())
                .createdAt(LocalDateTime.now())
                .status(Status.PENDING)
                .build();

        // Save the payment and send the Kafka message if the type is DIRECT
        return paymentRepository.save(payment)
                .flatMap(savedPayment -> {
                    if (type.equals(PaymentMethod.DIRECT)) {
                        OrderPayload orderPayload = OrderPayload.builder().orderId(paymentPayload.getOrderInfo()).build();
                        return kafkaUtils.sendMessage("order-approved-notify", orderPayload);
                    }
                    return Mono.empty(); // Do nothing for other payment types
                }).then();
    }



    @Override
    public Mono<Object> returnUrl(ServerHttpRequest request) {
        Map<String, String> map;
        map = request.getQueryParams().toSingleValueMap();
        String vnpSecureHash = map.get("vnp_SecureHash");
        map.remove("vnp_SecureHashType");
        map.remove("vnp_SecureHash");
        return PaymentConfig.hashAllFields(map).flatMap(
                hash -> {
                    String order = map.get("vnp_OrderInfo");

                    if (!hash.equals(vnpSecureHash) || !map.get("vnp_ResponseCode").equalsIgnoreCase("00")) {
                        OrderPayload rejectOrderPayload = OrderPayload.builder().orderId(order).build();
                        return kafkaUtils.sendMessage("order-reject", rejectOrderPayload)
                                .then(Mono.error(new RuntimeException("PAYMENT_ERROR")));
                    }
                    OrderPayload orderPayload = OrderPayload.builder().orderId(order).build();
                    return kafkaUtils.sendMessage("order-approved-notify", orderPayload)
                            .then(Mono.error(new RuntimeException("SUCCESS")));
                }
        );
    }

    @Override
    public Mono<String> createOrder(ServerHttpRequest serverRequest, double total, String orderInfo) {
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

    @Override
    public Mono<Payment> getByOrderId(String id) {
        return paymentRepository.findByOrderId(id);
    }

}
