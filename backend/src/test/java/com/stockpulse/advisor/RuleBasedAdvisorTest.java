package com.stockpulse.advisor;

import com.stockpulse.product.Category;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleBasedAdvisorTest {
    private final RuleBasedAdvisor advisor = new RuleBasedAdvisor();
    private AdvisorContext context(int stock, int threshold, BigDecimal velocity, BigDecimal average) {
        return new AdvisorContext("p", "Product", Category.HOME, new BigDecimal("100.00"), stock, threshold, velocity, average, "MANUAL", Instant.now());
    }
    @Test void raisesPriceTenPercentWhenStockIsLow() { assertEquals(new BigDecimal("110.00"), advisor.recommendPricing(context(4, 5, BigDecimal.ONE, BigDecimal.ONE)).recommendedPrice()); }
    @Test void raisesPriceFivePercentForDemandSpike() { assertEquals(new BigDecimal("105.00"), advisor.recommendPricing(context(20, 5, new BigDecimal("3"), BigDecimal.ONE)).recommendedPrice()); }
    @Test void calculatesMinimumReorderQuantity() { assertEquals(1, advisor.recommendReorder(context(20, 5, BigDecimal.ONE, BigDecimal.ONE)).recommendedQuantity()); }
}