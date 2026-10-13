package com.stockpulse.advisor;

import java.math.BigDecimal;

public record ReorderRecommendation(int recommendedQuantity, int suggestedLeadTimeDays,
                                    BigDecimal confidence, String reasoning) { }