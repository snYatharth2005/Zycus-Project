package com.stockpulse.suggestion;

import com.stockpulse.advisor.*;
import com.stockpulse.product.*;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuggestionService {
    private final ProductService products; private final ActiveAdvisorHolder advisors;
    private final PricingSuggestionRepository pricing; private final ReorderSuggestionRepository reorder;
    public SuggestionService(ProductService products, ActiveAdvisorHolder advisors, PricingSuggestionRepository pricing, ReorderSuggestionRepository reorder) { this.products = products; this.advisors = advisors; this.pricing = pricing; this.reorder = reorder; }
    private AdvisorContext context(Product p, TriggerReason trigger) { return new AdvisorContext(p.getId(), p.getName(), p.getCategory(), p.getCurrentPrice(), p.getStockLevel(), p.getReorderThreshold(), p.getDemandVelocity(), p.getDemandVelocity(), trigger.name(), Instant.now()); }
    @Transactional public PricingSuggestion suggestPricing(String id, TriggerReason trigger) { Product p = products.get(id); PricingRecommendation r = advisors.current().recommendPricing(context(p, trigger)); return pricing.save(new PricingSuggestion(p, p.getCurrentPrice(), r.recommendedPrice(), r.direction(), r.confidence(), r.reasoning(), trigger, r.reasoning().startsWith("AI fallback") ? SuggestionSource.AI_FALLBACK_RULE_BASED : SuggestionSource.AI)); }
    @Transactional public ReorderSuggestion suggestReorder(String id, TriggerReason trigger) { Product p = products.get(id); ReorderRecommendation r = advisors.current().recommendReorder(context(p, trigger)); return reorder.save(new ReorderSuggestion(p, p.getStockLevel(), r.recommendedQuantity(), r.suggestedLeadTimeDays(), r.confidence(), r.reasoning(), trigger, r.reasoning().startsWith("AI fallback") ? SuggestionSource.AI_FALLBACK_RULE_BASED : SuggestionSource.AI)); }
    public List<PricingSuggestion> pricing(SuggestionStatus status, String productId) { if (productId != null) return pricing.findByStatusAndProductId(status == null ? SuggestionStatus.PENDING : status, productId); return status == null ? pricing.findAll() : pricing.findByStatus(status); }
    public List<ReorderSuggestion> reorder(SuggestionStatus status, String productId) { if (productId != null) return reorder.findByStatusAndProductId(status == null ? SuggestionStatus.PENDING : status, productId); return status == null ? reorder.findAll() : reorder.findByStatus(status); }
    @Transactional public PricingSuggestion decidePricing(String id, SuggestionStatus status) { if (status != SuggestionStatus.ACCEPTED && status != SuggestionStatus.REJECTED) throw new IllegalArgumentException("Decision must be ACCEPTED or REJECTED"); PricingSuggestion suggestion = pricing.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Pricing suggestion not found: " + id)); suggestion.decide(status); return suggestion; }
    @Transactional public ReorderSuggestion decideReorder(String id, SuggestionStatus status) { if (status != SuggestionStatus.ACCEPTED && status != SuggestionStatus.REJECTED) throw new IllegalArgumentException("Decision must be ACCEPTED or REJECTED"); ReorderSuggestion suggestion = reorder.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Reorder suggestion not found: " + id)); suggestion.decide(status); return suggestion; }
    public static PricingResponse pricingResponse(PricingSuggestion s) { return new PricingResponse(s.getId(), s.getProduct().getId(), s.getCurrentPrice(), s.getRecommendedPrice(), s.getDirection(), s.getConfidence(), s.getReasoning(), s.getStatus(), s.getTriggerReason(), s.getSource(), s.getFallbackReason(), s.getCreatedAt()); }
    public static ReorderResponse reorderResponse(ReorderSuggestion s) { return new ReorderResponse(s.getId(), s.getProduct().getId(), s.getCurrentStock(), s.getRecommendedQuantity(), s.getSuggestedLeadTimeDays(), s.getConfidence(), s.getReasoning(), s.getStatus(), s.getTriggerReason(), s.getSource(), s.getFallbackReason(), s.getCreatedAt()); }
    public record PricingResponse(String id, String productId, java.math.BigDecimal currentPrice, java.math.BigDecimal recommendedPrice, PricingDirection direction, java.math.BigDecimal confidence, String reasoning, SuggestionStatus status, TriggerReason triggerReason, SuggestionSource source, String fallbackReason, String createdAt) { }
    public record ReorderResponse(String id, String productId, int currentStock, int recommendedQuantity, int suggestedLeadTimeDays, java.math.BigDecimal confidence, String reasoning, SuggestionStatus status, TriggerReason triggerReason, SuggestionSource source, String fallbackReason, String createdAt) { }
}