package com.agronorte.presentation.controller;

import com.agronorte.application.dto.OrderDTO;
import com.agronorte.application.usecase.OrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo pedido")
    public ResponseEntity<OrderDTO.OrderResponse> create(
            @Valid @RequestBody OrderDTO.CreateOrderRequest request,
            Authentication auth) {
        UUID buyerId = UUID.randomUUID(); // TODO: resolver pelo email do token
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderUseCase.create(request, buyerId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<OrderDTO.OrderResponse> findById(
            @PathVariable UUID id, Authentication auth) {
        UUID requesterId = UUID.randomUUID(); // TODO: resolver pelo email do token
        return ResponseEntity.ok(orderUseCase.findById(id, requesterId));
    }

    @GetMapping("/my")
    @Operation(summary = "Meus pedidos (como comprador)")
    public ResponseEntity<Page<OrderDTO.OrderResponse>> myOrders(
            Authentication auth,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        UUID buyerId = UUID.randomUUID(); // TODO: resolver pelo email do token
        return ResponseEntity.ok(orderUseCase.findMyOrders(buyerId, pageable));
    }

    @GetMapping("/merchant")
    @Operation(summary = "Pedidos recebidos pelo comerciante")
    public ResponseEntity<Page<OrderDTO.OrderResponse>> merchantOrders(
            Authentication auth,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        UUID merchantId = UUID.randomUUID(); // TODO: resolver pelo email do token
        return ResponseEntity.ok(orderUseCase.findMerchantOrders(merchantId, pageable));
    }

    @PatchMapping("/{id}/status/{action}")
    @Operation(summary = "Atualizar status do pedido (CONFIRM, SHIP, DELIVER, CANCEL)")
    public ResponseEntity<OrderDTO.OrderResponse> updateStatus(
            @PathVariable UUID id,
            @PathVariable String action,
            Authentication auth) {
        UUID actorId = UUID.randomUUID(); // TODO: resolver pelo email do token
        return ResponseEntity.ok(orderUseCase.updateStatus(id, action, actorId));
    }
}
