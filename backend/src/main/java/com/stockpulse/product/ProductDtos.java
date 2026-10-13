package com.stockpulse.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public final class ProductDtos {
    private ProductDtos() { }
    public record CreateRequest(@NotBlank String id, @NotBlank String sku, @NotBlank String name, Category category,
                                 @DecimalMin("0.01") BigDecimal currentPrice, @Min(0) int stockLevel,
                                 @Min(1) int reorderThreshold, @Min(0) BigDecimal demandVelocity) { }
    public record StockRequest(@Min(0) int stockLevel) { }
    public record OrderRequest(@Min(1) int quantity) { }
    public record Response(String id, String sku, String name, Category category, BigDecimal currentPrice,
                           int stockLevel, int reorderThreshold, BigDecimal demandVelocity, ProductStatus status,
                           String createdAt, String updatedAt) { }
}