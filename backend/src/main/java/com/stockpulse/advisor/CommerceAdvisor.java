package com.stockpulse.advisor;

public interface CommerceAdvisor {
    String name();
    PricingRecommendation recommendPricing(AdvisorContext context);
    ReorderRecommendation recommendReorder(AdvisorContext context);
}