package com.agronorte.application.usecase;

import com.agronorte.application.dto.OrderDTO;
import com.agronorte.application.port.out.OrderRepository;
import com.agronorte.application.port.out.ProductRepository;
import com.agronorte.domain.entity.Order;
import com.agronorte.domain.entity.OrderItem;
import com.agronorte.domain.entity.OrderStatus;
import com.agronorte.domain.exception.BusinessRuleException;
import com.agronorte.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDTO.OrderResponse create(OrderDTO.CreateOrderRequest request, UUID buyerId) {
        var order = Order.create(buyerId, request.merchantId(), request.notes());

        for (var itemReq : request.items()) {
            var product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", itemReq.productId()));
            order.addItem(product, itemReq.quantity());
            // Atualiza estoque imediatamente — em produção real, isso seria em um Saga ou
            // via evento
            product.decreaseStock(itemReq.quantity());
            productRepository.save(product);
        }

        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderDTO.OrderResponse findById(UUID id, UUID requesterId) {
        var order = findOrderOrThrow(id);
        assertAccess(order, requesterId);
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO.OrderResponse> findMyOrders(UUID buyerId, Pageable pageable) {
        return orderRepository.findByBuyerId(buyerId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO.OrderResponse> findMerchantOrders(UUID merchantId, Pageable pageable) {
        return orderRepository.findByMerchantId(merchantId, pageable).map(this::toResponse);
    }

    @Transactional
    public OrderDTO.OrderResponse updateStatus(UUID orderId, String action, UUID actorId) {
        var order = findOrderOrThrow(orderId);
        switch (action.toUpperCase()) {
            case "CONFIRM" -> {
                assertMerchantOrAdmin(order, actorId);
                order.confirm();
            }
            case "SHIP" -> {
                assertMerchantOrAdmin(order, actorId);
                order.ship();
            }
            case "DELIVER" -> {
                assertMerchantOrAdmin(order, actorId);
                order.deliver();
            }
            case "CANCEL" -> order.cancel();
            default -> throw new BusinessRuleException("Ação inválida: " + action);
        }
        return toResponse(orderRepository.save(order));
    }

    private Order findOrderOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    private void assertAccess(Order order, UUID userId) {
        if (!order.getBuyerId().equals(userId) && !order.getMerchantId().equals(userId)) {
            throw new BusinessRuleException("Acesso negado ao pedido.");
        }
    }

    private void assertMerchantOrAdmin(Order order, UUID actorId) {
        if (!order.getMerchantId().equals(actorId)) {
            throw new BusinessRuleException("Apenas o comerciante pode executar esta ação.");
        }
    }

    private OrderDTO.OrderResponse toResponse(Order o) {
        var items = o.getItems().stream().map(this::toItemResponse).toList();
        return new OrderDTO.OrderResponse(
                o.getId(), o.getBuyerId(), o.getMerchantId(), o.getStatus(),
                items, o.getTotalAmount().amount(), o.getNotes(),
                o.getCreatedAt(), o.getUpdatedAt());
    }

    private OrderDTO.OrderItemResponse toItemResponse(OrderItem i) {
        return new OrderDTO.OrderItemResponse(
                i.getId(), i.getProductId(), i.getProductName(),
                i.getUnitPrice().amount(), i.getQuantity(), i.getSubtotal().amount());
    }
}
