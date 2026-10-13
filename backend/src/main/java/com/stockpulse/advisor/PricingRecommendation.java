package com.stockpulse.advisor;

import com.stockpulse.suggestion.PricingDirection;
import com.stockpulse.suggestion.SuggestionSource;
import java.math.BigDecimal;

public record PricingRecommendation(BigDecimal recommendedPrice, PricingDirection direction,
                                    BigDecimal confidence, String reasoning, SuggestionSource source,
                                    String fallbackReason) {
    public PricingRecommendation(BigDecimal price, PricingDirection direction, BigDecimal confidence, String reasoning) {
        this(price, direction, confidence, reasoning, SuggestionSource.RULE_BASED, null);
    }
}