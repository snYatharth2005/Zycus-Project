package com.stockpulse.advisor;

import com.stockpulse.suggestion.PricingDirection;
import java.math.BigDecimal;

public record PricingRecommendation(BigDecimal recommendedPrice, PricingDirection direction,
                                    BigDecimal confidence, String reasoning) { }