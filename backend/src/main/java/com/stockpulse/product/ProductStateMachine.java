package com.stockpulse.product;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/** Centralizes lifecycle transitions so controllers and services cannot invent invalid states. */
public final class ProductStateMachine {
    private static final Map<ProductStatus, Set<ProductStatus>> ALLOWED = Map.of(
            ProductStatus.ACTIVE, EnumSet.of(ProductStatus.PRICE_REVIEW_PENDING, ProductStatus.OUT_OF_STOCK),
            ProductStatus.PRICE_REVIEW_PENDING, EnumSet.of(ProductStatus.ACTIVE, ProductStatus.OUT_OF_STOCK),
            ProductStatus.OUT_OF_STOCK, EnumSet.of(ProductStatus.ACTIVE));

    private ProductStateMachine() { }

    public static boolean canTransition(ProductStatus from, ProductStatus to) {
        return from == to || ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }

    public static ProductStatus statusForStock(int stockLevel, ProductStatus current, boolean pricingPending) {
        if (stockLevel == 0) return ProductStatus.OUT_OF_STOCK;
        if (pricingPending) return ProductStatus.PRICE_REVIEW_PENDING;
        return ProductStatus.ACTIVE;
    }

    public static void transition(Product product, ProductStatus target) {
        if (!canTransition(product.getStatus(), target)) {
            throw new IllegalStateException("Invalid product transition: " + product.getStatus() + " -> " + target);
        }
        product.setStatus(target);
    }
}