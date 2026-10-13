package com.stockpulse.product;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductStateMachineTest {
    @Test void allowsActiveToPendingAndBack() {
        assertTrue(ProductStateMachine.canTransition(ProductStatus.ACTIVE, ProductStatus.PRICE_REVIEW_PENDING));
        assertTrue(ProductStateMachine.canTransition(ProductStatus.PRICE_REVIEW_PENDING, ProductStatus.ACTIVE));
    }

    @Test void stockZeroAlwaysMeansOutOfStock() {
        assertEquals(ProductStatus.OUT_OF_STOCK,
                ProductStateMachine.statusForStock(0, ProductStatus.ACTIVE, true));
    }

    @Test void allowsRestockFromOutOfStock() {
        assertTrue(ProductStateMachine.canTransition(ProductStatus.OUT_OF_STOCK, ProductStatus.ACTIVE));
        assertFalse(ProductStateMachine.canTransition(ProductStatus.OUT_OF_STOCK, ProductStatus.PRICE_REVIEW_PENDING));
    }
}