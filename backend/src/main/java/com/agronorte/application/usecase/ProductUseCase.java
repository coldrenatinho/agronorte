package com.agronorte.application.usecase;

import com.agronorte.application.dto.ProductDTO;
import com.agronorte.application.port.out.ProductRepository;
import com.agronorte.domain.entity.Product;
import com.agronorte.domain.entity.ProductCategory;
import com.agronorte.domain.exception.BusinessRuleException;
import com.agronorte.domain.exception.ResourceNotFoundException;
import com.agronorte.domain.valueobject.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO.ProductResponse create(ProductDTO.CreateProductRequest request, UUID merchantId) {
        var product = Product.create(
                request.name(),
                request.description(),
                new Price(request.price()),
                request.stockQuantity(),
                request.imageUrl(),
                request.category(),
                merchantId);
        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO.ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAllActive(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO.ProductResponse> findByMerchant(UUID merchantId, Pageable pageable) {
        return productRepository.findByMerchantId(merchantId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO.ProductResponse> findByCategory(ProductCategory category, Pageable pageable) {
        return productRepository.findByCategoryAndActive(category, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductDTO.ProductResponse findById(UUID id) {
        return toResponse(findProductOrThrow(id));
    }

    @Transactional
    public ProductDTO.ProductResponse update(UUID id, ProductDTO.UpdateProductRequest request, UUID merchantId) {
        var product = findProductOrThrow(id);
        assertOwnership(product, merchantId);
        product.updateDetails(
                request.name(), request.description(),
                new Price(request.price()), request.imageUrl(), request.category());
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void updateStock(UUID id, ProductDTO.StockUpdateRequest request, UUID merchantId) {
        var product = findProductOrThrow(id);
        assertOwnership(product, merchantId);
        if (request.operation() == ProductDTO.StockOperation.INCREASE) {
            product.increaseStock(request.quantity());
        } else {
            product.decreaseStock(request.quantity());
        }
        productRepository.save(product);
    }

    @Transactional
    public void deactivate(UUID id, UUID merchantId) {
        var product = findProductOrThrow(id);
        assertOwnership(product, merchantId);
        product.deactivate();
        productRepository.save(product);
    }

    private Product findProductOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }

    private void assertOwnership(Product product, UUID merchantId) {
        if (!product.getMerchantId().equals(merchantId)) {
            throw new BusinessRuleException("Você não tem permissão para alterar este produto.");
        }
    }

    private ProductDTO.ProductResponse toResponse(Product p) {
        return new ProductDTO.ProductResponse(
                p.getId(), p.getName(), p.getDescription(),
                p.getPrice().amount(), p.getStockQuantity(),
                p.getImageUrl(), p.getCategory(), p.getMerchantId(), p.isActive());
    }
}
