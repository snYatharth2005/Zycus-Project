package com.stockpulse.advisor;

import com.stockpulse.product.Category;
import java.math.BigDecimal;
import java.time.Instant;

public record AdvisorContext(String productId, String productName, Category category,
                             BigDecimal currentPrice, int stockLevel, int reorderThreshold,
                             BigDecimal demandVelocity, BigDecimal categoryAverageVelocity,
                             String triggerReason, Instant timestamp) { }