package com.stockpulse.advisor;

import com.stockpulse.suggestion.PricingDirection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class RuleBasedAdvisor implements CommerceAdvisor {
    @Override public String name() { return "rule-based"; }
    @Override public PricingRecommendation recommendPricing(AdvisorContext c) {
        BigDecimal price = c.currentPrice();
        if (c.stockLevel() < c.reorderThreshold()) return new PricingRecommendation(price.multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP), PricingDirection.INCREASE, new BigDecimal("0.70"), "Stock is below the reorder threshold; protect remaining inventory.");
        if (c.categoryAverageVelocity().signum() > 0 && c.demandVelocity().compareTo(c.categoryAverageVelocity().multiply(BigDecimal.valueOf(2))) > 0) return new PricingRecommendation(price.multiply(new BigDecimal("1.05")).setScale(2, RoundingMode.HALF_UP), PricingDirection.INCREASE, new BigDecimal("0.65"), "Demand velocity is more than twice the category average.");
        return new PricingRecommendation(price, PricingDirection.HOLD, new BigDecimal("0.60"), "Inventory and demand are within the normal range.");
    }
    @Override public ReorderRecommendation recommendReorder(AdvisorContext c) { return new ReorderRecommendation(Math.max(1, c.reorderThreshold() * 3 - c.stockLevel()), 7, new BigDecimal("0.75"), "Restore stock to approximately three reorder thresholds."); }
}