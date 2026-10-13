package com.stockpulse.advisor;

import java.math.BigDecimal;
import com.stockpulse.suggestion.SuggestionSource;

public record ReorderRecommendation(int recommendedQuantity, int suggestedLeadTimeDays,
                                    BigDecimal confidence, String reasoning, SuggestionSource source,
                                    String fallbackReason) {
    public ReorderRecommendation(int quantity, int leadTime, BigDecimal confidence, String reasoning) {
        this(quantity, leadTime, confidence, reasoning, SuggestionSource.RULE_BASED, null);
    }
}