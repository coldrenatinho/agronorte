package com.agronorte.presentation.controller;

import com.agronorte.application.dto.ProductDTO;
import com.agronorte.application.usecase.ProductUseCase;
import com.agronorte.domain.entity.ProductCategory;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Gerenciamento de produtos agrícolas")
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping
    @Operation(summary = "Listar produtos ativos com paginação")
    public ResponseEntity<Page<ProductDTO.ProductResponse>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(productUseCase.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProductDTO.ProductResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productUseCase.findById(id));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Listar por categoria")
    public ResponseEntity<Page<ProductDTO.ProductResponse>> findByCategory(
            @PathVariable ProductCategory category,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productUseCase.findByCategory(category, pageable));
    }

    @GetMapping("/merchant/{merchantId}")
    @Operation(summary = "Listar produtos de um comerciante")
    public ResponseEntity<Page<ProductDTO.ProductResponse>> findByMerchant(
            @PathVariable UUID merchantId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productUseCase.findByMerchant(merchantId, pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MERCHANT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Criar produto (somente MERCHANT)")
    public ResponseEntity<ProductDTO.ProductResponse> create(
            @Valid @RequestBody ProductDTO.CreateProductRequest request,
            Authentication auth) {
        UUID merchantId = getMerchantId(auth);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productUseCase.create(request, merchantId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProductDTO.ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO.UpdateProductRequest request,
            Authentication auth) {
        return ResponseEntity.ok(productUseCase.update(id, request, getMerchantId(auth)));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('MERCHANT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar estoque")
    public ResponseEntity<Void> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO.StockUpdateRequest request,
            Authentication auth) {
        productUseCase.updateStock(id, request, getMerchantId(auth));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Desativar produto")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id, Authentication auth) {
        productUseCase.deactivate(id, getMerchantId(auth));
        return ResponseEntity.noContent().build();
    }

    // TODO: Integrar com MerchantUseCase para buscar o merchantId real pelo userId
    private UUID getMerchantId(Authentication auth) {
        // Placeholder: em produção, buscar merchantId pelo email do token
        return UUID.randomUUID();
    }
}
