package com.stockpulse.ai;

import com.stockpulse.advisor.*;
import com.stockpulse.product.Category;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.stockpulse.suggestion.SuggestionSource;

class AiAdvisorTest {
    private final AdvisorContext context = new AdvisorContext("p", "Product", Category.HOME, new BigDecimal("100.00"), 4, 5, BigDecimal.ONE, BigDecimal.ONE, "INVENTORY_LOW", Instant.now());
    @Test void malformedResponseFallsBackToRuleBased() {
        AiAdvisor advisor = new AiAdvisor(prompt -> "bad", new RecommendationParser(new com.fasterxml.jackson.databind.ObjectMapper()), new RuleBasedAdvisor(), new PromptTemplateService());
        assertEquals(new BigDecimal("110.00"), advisor.recommendPricing(context).recommendedPrice());
        var result = advisor.recommendPricing(context); assertTrue(result.reasoning().startsWith("AI fallback:")); assertEquals(SuggestionSource.AI_FALLBACK_RULE_BASED, result.source()); assertNotNull(result.fallbackReason());
    }
    @Test void excessivePriceFallsBack() {
        LlmClient client = prompt -> "{\"recommendedPrice\": 1000, \"direction\": \"INCREASE\", \"confidence\": 0.9, \"reasoning\": \"bad\"}";
        AiAdvisor advisor = new AiAdvisor(client, new RecommendationParser(new com.fasterxml.jackson.databind.ObjectMapper()), new RuleBasedAdvisor(), new PromptTemplateService());
        assertEquals(new BigDecimal("110.00"), advisor.recommendPricing(context).recommendedPrice());
    }
    @Test void validatedResponseIsAi() {
        LlmClient client = prompt -> "{\"recommendedPrice\": 110, \"direction\": \"INCREASE\", \"confidence\": 0.9, \"reasoning\": \"valid\"}";
        AiAdvisor advisor = new AiAdvisor(client, new RecommendationParser(new com.fasterxml.jackson.databind.ObjectMapper()), new RuleBasedAdvisor(), new PromptTemplateService());
        assertEquals(SuggestionSource.AI, advisor.recommendPricing(context).source());
    }
}