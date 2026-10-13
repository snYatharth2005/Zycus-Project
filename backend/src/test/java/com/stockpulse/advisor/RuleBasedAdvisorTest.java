package com.stockpulse.advisor;

import com.stockpulse.product.Category;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.stockpulse.suggestion.SuggestionSource;

class RuleBasedAdvisorTest {
    private final RuleBasedAdvisor advisor = new RuleBasedAdvisor();
    private AdvisorContext context(int stock, int threshold, BigDecimal velocity, BigDecimal average) {
        return new AdvisorContext("p", "Product", Category.HOME, new BigDecimal("100.00"), stock, threshold, velocity, average, "MANUAL", Instant.now());
    }
    @Test void raisesPriceTenPercentWhenStockIsLow() { var result = advisor.recommendPricing(context(4, 5, BigDecimal.ONE, BigDecimal.ONE)); assertEquals(new BigDecimal("110.00"), result.recommendedPrice()); assertEquals(SuggestionSource.RULE_BASED, result.source()); assertNull(result.fallbackReason()); }
    @Test void raisesPriceFivePercentForDemandSpike() { assertEquals(new BigDecimal("105.00"), advisor.recommendPricing(context(20, 5, new BigDecimal("3"), BigDecimal.ONE)).recommendedPrice()); }
    @Test void calculatesMinimumReorderQuantity() { var result = advisor.recommendReorder(context(20, 5, BigDecimal.ONE, BigDecimal.ONE)); assertEquals(1, result.recommendedQuantity()); assertEquals(SuggestionSource.RULE_BASED, result.source()); }
}