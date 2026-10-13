package com.stockpulse.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockpulse.advisor.PricingRecommendation;
import com.stockpulse.advisor.ReorderRecommendation;
import com.stockpulse.suggestion.PricingDirection;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class RecommendationParser {
    private static final Pattern THINK = Pattern.compile("(?s)<think>.*?</think>");
    private final ObjectMapper mapper;
    public RecommendationParser(ObjectMapper mapper) { this.mapper = mapper; }
    public PricingRecommendation pricing(String raw) {
        try { return validate(mapper.readTree(json(raw)).traverse(mapper).readValueAs(PricingRecommendation.class)); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid pricing recommendation", e); }
    }
    public ReorderRecommendation reorder(String raw) {
        try { return validate(mapper.readTree(json(raw)).traverse(mapper).readValueAs(ReorderRecommendation.class)); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid reorder recommendation", e); }
    }
    private String json(String raw) {
        String cleaned = THINK.matcher(raw == null ? "" : raw).replaceAll("").replace("```json", "").replace("```", "").trim();
        int start = cleaned.indexOf('{'); int end = cleaned.lastIndexOf('}');
        if (start < 0 || end <= start) throw new IllegalArgumentException("No JSON object found");
        return cleaned.substring(start, end + 1);
    }
    private PricingRecommendation validate(PricingRecommendation r) {
        if (r.recommendedPrice() == null || r.recommendedPrice().signum() <= 0 || r.direction() == null || r.reasoning() == null || r.reasoning().isBlank() || r.reasoning().length() > 2000) throw new IllegalArgumentException("Invalid pricing fields");
        BigDecimal confidence = clamp(r.confidence());
        if ((r.direction() == PricingDirection.INCREASE && r.recommendedPrice().compareTo(BigDecimal.ZERO) <= 0) || (r.direction() == PricingDirection.HOLD && r.recommendedPrice().signum() <= 0)) throw new IllegalArgumentException("Invalid pricing direction");
        return new PricingRecommendation(r.recommendedPrice(), r.direction(), confidence, r.reasoning().trim());
    }
    private ReorderRecommendation validate(ReorderRecommendation r) {
        if (r.recommendedQuantity() <= 0 || r.suggestedLeadTimeDays() < 0 || r.reasoning() == null || r.reasoning().isBlank() || r.reasoning().length() > 2000) throw new IllegalArgumentException("Invalid reorder fields");
        return new ReorderRecommendation(r.recommendedQuantity(), r.suggestedLeadTimeDays(), clamp(r.confidence()), r.reasoning().trim());
    }
    private BigDecimal clamp(BigDecimal value) { if (value == null) return BigDecimal.ZERO; return value.max(BigDecimal.ZERO).min(BigDecimal.ONE); }
}