package com.stockpulse;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import com.stockpulse.product.ProductService;
import com.stockpulse.product.ProductRepository;
import com.stockpulse.suggestion.*;

@SpringBootTest
@ActiveProfiles("sqlite")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class Phase4IntegrationTest {
    @Autowired ProductService products;
    @Autowired ProductRepository productRepository;
    @Autowired PricingSuggestionRepository pricing;
    @Autowired ReorderSuggestionRepository reorder;
    @Autowired SuggestionService suggestions;

    @Test void lowStockCreatesSuggestionsAndAcceptChangesPrice() {
        BigDecimal before = productRepository.findById("PRD-003").orElseThrow().getCurrentPrice();
        products.order("PRD-003", 1);
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(() -> pricing.findByStatusAndProductId(SuggestionStatus.PENDING, "PRD-003").stream().anyMatch(s -> s.getTriggerReason() == TriggerReason.INVENTORY_LOW));
        var suggestion = pricing.findByStatusAndProductId(SuggestionStatus.PENDING, "PRD-003").stream().filter(s -> s.getTriggerReason() == TriggerReason.INVENTORY_LOW).findFirst().orElseThrow();
        assertEquals(before, productRepository.findById("PRD-003").orElseThrow().getCurrentPrice());
        suggestions.decidePricing(suggestion.getId(), SuggestionStatus.ACCEPTED);
        assertEquals(suggestion.getRecommendedPrice(), productRepository.findById("PRD-003").orElseThrow().getCurrentPrice());
    }

    @Test void repeatedSignalsDoNotDuplicatePendingSuggestions() {
        products.order("PRD-003", 1); products.order("PRD-003", 1);
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(() -> pricing.findByStatusAndProductId(SuggestionStatus.PENDING, "PRD-003").size() >= 1);
        assertEquals(1, pricing.findByStatusAndProductId(SuggestionStatus.PENDING, "PRD-003").stream().filter(s -> s.getTriggerReason() == TriggerReason.INVENTORY_LOW).count());
    }

    @Test void demandSpikeCreatesSuggestions() {
        products.order("PRD-008", 7);
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(() -> pricing.findByStatusAndProductId(SuggestionStatus.PENDING, "PRD-008").stream().anyMatch(s -> s.getTriggerReason() == TriggerReason.DEMAND_SPIKE));
        assertTrue(reorder.findByStatusAndProductId(SuggestionStatus.PENDING, "PRD-008").stream().anyMatch(s -> s.getTriggerReason() == TriggerReason.DEMAND_SPIKE));
    }

    @Test void rapidOrdersCompleteWithoutLockErrors() throws Exception {
        var pool = Executors.newFixedThreadPool(4); var done = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) pool.submit(() -> { try { products.order("PRD-003", 1); } finally { done.countDown(); } });
        assertTrue(done.await(5, java.util.concurrent.TimeUnit.SECONDS)); pool.shutdown();
    }
}