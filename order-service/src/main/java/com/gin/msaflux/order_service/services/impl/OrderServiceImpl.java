package com.gin.msaflux.order_service.services.impl;

import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.common.kafka.status.Status;
import com.gin.msaflux.order_service.kafka.KafkaUtils;
import com.gin.msaflux.order_service.models.Order;
import com.gin.msaflux.order_service.repositories.OrderRepository;
import com.gin.msaflux.order_service.request.CreateOrderRequest;
import com.gin.msaflux.order_service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaUtils kafkaUtils;
    @Override
    public Mono<Order> createOrder(CreateOrderRequest createOrderRequest) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {

                    String userId = securityContext.getAuthentication().getName();
                    List<Order.OrderItem> orderItems = createOrderRequest.getItems().stream()
                            .map(item -> new Order.OrderItem(item.getProductId(), item.getQuantity(), item.getPrice()))
                            .toList();

                    Order.Address address = new Order.Address(
                            createOrderRequest.getShippingAddress().getAddressLine(),
                            createOrderRequest.getShippingAddress().getCity(),
                            createOrderRequest.getShippingAddress().getCountry());

                    double totalAmount= orderItems.stream().mapToDouble(orderItem -> orderItem.getQuantity() * orderItem.getPrice()).sum();

                    Order order = Order.builder()
                            .createdAt(LocalDateTime.now())
                            .customerId(userId)
                            .items(orderItems)
                            .paymentType(createOrderRequest.getPaymentType())
                            .status(Status.PENDING)
                            .totalAmount(totalAmount)
                            .shippingAddress(address)
                            .updatedAt(LocalDateTime.now()).build();
                    return orderRepository.save(order)
                            // send to inventory
                            .flatMap(savedOrder -> kafkaUtils.sendMessage("inventory-check", OrderPayload.builder().orderId(savedOrder.getId())
                                    .orderItems(
                                            savedOrder.getItems().stream().map(i -> new OrderPayload.OrderItemPayload(i.getProductId(), i.getQuantity(), i.getPrice())).toList()
                                    ).totalPrice(totalAmount)
                                    .paymentType(savedOrder.getPaymentType()).build())
                                    .thenReturn(savedOrder));
                });
    }

    @Override
    public Mono<Order> rejectingOrder(String orderId) {
        return orderRepository.findById(orderId).flatMap(
                order -> {
                    order.setStatus(Status.REJECTED);
                    order.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(order);
                }
        );
    }

    @Override
    public Mono<Order> approveOrder(String orderId) {
        return orderRepository.findById(orderId).flatMap(
                order -> {
                    order.setStatus(Status.ACCEPTED);
                    order.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(order);
                }
        );
    }
}
