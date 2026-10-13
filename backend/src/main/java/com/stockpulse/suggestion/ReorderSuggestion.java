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
}