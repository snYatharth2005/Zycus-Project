package com.stockpulse.product;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repository;
    public ProductService(ProductRepository repository) { this.repository = repository; }
    @Transactional public Product create(ProductDtos.CreateRequest request) {
        if (repository.existsById(request.id())) throw new IllegalArgumentException("Product already exists: " + request.id());
        return repository.save(new Product(request.id(), request.sku(), request.name(), request.category(), request.currentPrice(), request.stockLevel(), request.reorderThreshold(), request.demandVelocity(), request.stockLevel() == 0 ? ProductStatus.OUT_OF_STOCK : ProductStatus.ACTIVE));
    }
    public Product get(String id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Product not found: " + id)); }
    public List<Product> list(ProductStatus status, Category category) { return repository.findAll().stream().filter(p -> status == null || p.getStatus() == status).filter(p -> category == null || p.getCategory() == category).toList(); }
    @Transactional public Product updateStock(String id, int stock) { Product p = get(id); p.setStockLevel(stock); p.setStatus(stock == 0 ? ProductStatus.OUT_OF_STOCK : ProductStatus.ACTIVE); return p; }
    @Transactional public Product order(String id, int quantity) { Product p = get(id); if (quantity > p.getStockLevel()) throw new IllegalArgumentException("Order quantity exceeds stock"); p.setStockLevel(p.getStockLevel() - quantity); p.setDemandVelocity(p.getDemandVelocity().add(BigDecimal.valueOf(quantity))); p.setStatus(p.getStockLevel() == 0 ? ProductStatus.OUT_OF_STOCK : ProductStatus.ACTIVE); return p; }
    public static ProductDtos.Response response(Product p) { return new ProductDtos.Response(p.getId(), p.getSku(), p.getName(), p.getCategory(), p.getCurrentPrice(), p.getStockLevel(), p.getReorderThreshold(), p.getDemandVelocity(), p.getStatus(), p.getCreatedAt(), p.getUpdatedAt()); }
}