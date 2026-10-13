package com.stockpulse.suggestion;

import com.stockpulse.product.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class PricingSuggestion {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal currentPrice;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal recommendedPrice;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PricingDirection direction;
    @Column(nullable = false, precision = 4, scale = 3) private BigDecimal confidence;
    @Column(nullable = false, length = 2000) private String reasoning;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SuggestionStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TriggerReason triggerReason;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SuggestionSource source;
    private String fallbackReason;
    @Column(nullable = false) private String createdAt;
    private String decidedAt;
    @Column(unique = true) private String dedupeKey;

    protected PricingSuggestion() { }

    public PricingSuggestion(Product product, String dedupeKey) {
        this.product = product;
        this.dedupeKey = dedupeKey;
        this.currentPrice = BigDecimal.ZERO;
        this.recommendedPrice = BigDecimal.ZERO;
        this.direction = PricingDirection.HOLD;
        this.confidence = BigDecimal.ZERO;
        this.reasoning = "phase-1 test suggestion";
        this.status = SuggestionStatus.PENDING;
        this.triggerReason = TriggerReason.MANUAL;
        this.source = SuggestionSource.RULE_BASED;
        this.createdAt = java.time.Instant.now().toString();
    }

    public String getId() { return id; }
    public String getDedupeKey() { return dedupeKey; }
}