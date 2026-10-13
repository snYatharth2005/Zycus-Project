package com.stockpulse;
import com.stockpulse.product.Product;
import com.stockpulse.product.ProductRepository;
import com.stockpulse.suggestion.PricingSuggestion;
import com.stockpulse.suggestion.PricingSuggestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("sqlite")
class SqlitePersistenceTest {
    @Autowired ProductRepository products;
    @Autowired PricingSuggestionRepository pricingSuggestions;

    @Test void seedLoadsIntoSqlite() {
        assertEquals(8, products.count());
        assertEquals("Organic Cotton T-Shirt", products.findById("PRD-003").orElseThrow().getName());
    }

    @Test
    @Transactional
    void optimisticVersionChangesOnUpdate() {
        Product product = products.findById("PRD-001").orElseThrow();
        long initialVersion = product.getVersion();
        product.setStockLevel(product.getStockLevel() - 1);
        products.saveAndFlush(product);
        assertTrue(product.getVersion() > initialVersion);
    }

    @Test void nullableDedupeKeyIsUniqueOnlyWhenPresent() {
        Product product = products.findById("PRD-001").orElseThrow();
        pricingSuggestions.saveAndFlush(new PricingSuggestion(product, null));
        pricingSuggestions.saveAndFlush(new PricingSuggestion(product, null));
        PricingSuggestion first = new PricingSuggestion(product, "PRD-001:MANUAL:PRICING");
        pricingSuggestions.saveAndFlush(first);
        PricingSuggestion duplicate = new PricingSuggestion(product, "PRD-001:MANUAL:PRICING");
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pricingSuggestions.saveAndFlush(duplicate));
        assertTrue(exception.getMessage().contains("UNIQUE")
                || exception.getCause() != null && exception.getCause().toString().contains("UNIQUE"));
    }
}