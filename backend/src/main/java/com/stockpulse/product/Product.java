package com.stockpulse.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String sku;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Category category;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;
    @Column(nullable = false)
    private int stockLevel;
    @Column(nullable = false)
    private int reorderThreshold;
    @Column(nullable = false)
    private BigDecimal demandVelocity;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ProductStatus status;
    // Nullable seams for sprint 2 margin and supplier integrations.
    private BigDecimal costPrice;
    private BigDecimal marginFloor;
    private String supplierId;
    @Version
    private Long version;
    @Column(nullable = false, updatable = false)
    private String createdAt;
    @Column(nullable = false)
    private String updatedAt;

    protected Product() { }

    public Product(String id, String sku, String name, Category category, BigDecimal currentPrice,
                   int stockLevel, int reorderThreshold, BigDecimal demandVelocity, ProductStatus status) {
        this.id = id; this.sku = sku; this.name = name; this.category = category;
        this.currentPrice = currentPrice.setScale(2); this.stockLevel = stockLevel;
        this.reorderThreshold = reorderThreshold; this.demandVelocity = demandVelocity;
        this.status = status;
    }

    @PrePersist void onCreate() { createdAt = java.time.Instant.now().toString(); updatedAt = createdAt; }
    @PreUpdate void onUpdate() { updatedAt = java.time.Instant.now().toString(); }

    public String getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public int getStockLevel() { return stockLevel; }
    public int getReorderThreshold() { return reorderThreshold; }
    public BigDecimal getDemandVelocity() { return demandVelocity; }
    public ProductStatus getStatus() { return status; }
    public BigDecimal getCostPrice() { return costPrice; }
    public BigDecimal getMarginFloor() { return marginFloor; }
    public String getSupplierId() { return supplierId; }
    public Long getVersion() { return version; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setStatus(ProductStatus status) { this.status = status; }
    public void setCurrentPrice(BigDecimal price) { this.currentPrice = price.setScale(2); }
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
    public void setDemandVelocity(BigDecimal velocity) { this.demandVelocity = velocity; }
}