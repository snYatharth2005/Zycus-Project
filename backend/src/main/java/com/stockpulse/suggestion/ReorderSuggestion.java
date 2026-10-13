package com.stockpulse.suggestion;

import com.stockpulse.product.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ReorderSuggestion {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private Product product;
    @Column(nullable = false) private int currentStock;
    @Column(nullable = false) private int recommendedQuantity;
    @Column(nullable = false) private int suggestedLeadTimeDays;
    @Column(nullable = false, precision = 4, scale = 3) private BigDecimal confidence;
    @Column(nullable = false, length = 2000) private String reasoning;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SuggestionStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TriggerReason triggerReason;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SuggestionSource source;
    private String fallbackReason;
    @Column(nullable = false) private String createdAt;
    private String decidedAt;
    @Column(unique = true) private String dedupeKey;

    protected ReorderSuggestion() { }

    public ReorderSuggestion(Product product, int currentStock, int quantity, int leadTime, BigDecimal confidence,
                              String reasoning, TriggerReason triggerReason, SuggestionSource source) {
        this.product = product; this.currentStock = currentStock; this.recommendedQuantity = quantity;
        this.suggestedLeadTimeDays = leadTime; this.confidence = confidence; this.reasoning = reasoning;
        this.status = SuggestionStatus.PENDING; this.triggerReason = triggerReason; this.source = source;
        this.createdAt = java.time.Instant.now().toString();
    }
    public String getId() { return id; }
    public Product getProduct() { return product; }
    public int getCurrentStock() { return currentStock; }
    public int getRecommendedQuantity() { return recommendedQuantity; }
    public int getSuggestedLeadTimeDays() { return suggestedLeadTimeDays; }
    public BigDecimal getConfidence() { return confidence; }
    public String getReasoning() { return reasoning; }
    public SuggestionStatus getStatus() { return status; }
    public TriggerReason getTriggerReason() { return triggerReason; }
    public SuggestionSource getSource() { return source; }
    public String getFallbackReason() { return fallbackReason; }
    public String getCreatedAt() { return createdAt; }
    public void decide(SuggestionStatus status) { this.status = status; this.decidedAt = java.time.Instant.now().toString(); this.dedupeKey = null; }
}