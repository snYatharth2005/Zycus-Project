package com.stockpulse.suggestion;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class SuggestionController {
    private final SuggestionService service;
    public SuggestionController(SuggestionService service) { this.service = service; }
    public record Decision(SuggestionStatus status) { }
    @PostMapping("/products/{id}/suggest-pricing") public SuggestionService.PricingResponse pricing(@PathVariable String id) { return SuggestionService.pricingResponse(service.suggestPricing(id, TriggerReason.MANUAL)); }
    @PostMapping("/products/{id}/suggest-reorder") public SuggestionService.ReorderResponse reorder(@PathVariable String id) { return SuggestionService.reorderResponse(service.suggestReorder(id, TriggerReason.MANUAL)); }
    @GetMapping("/pricing-suggestions") public List<SuggestionService.PricingResponse> pricingList(@RequestParam(required = false) SuggestionStatus status, @RequestParam(required = false) String productId) { return service.pricing(status, productId).stream().map(SuggestionService::pricingResponse).toList(); }
    @GetMapping("/reorder-suggestions") public List<SuggestionService.ReorderResponse> reorderList(@RequestParam(required = false) SuggestionStatus status, @RequestParam(required = false) String productId) { return service.reorder(status, productId).stream().map(SuggestionService::reorderResponse).toList(); }
    @PatchMapping("/pricing-suggestions/{id}") public SuggestionService.PricingResponse decidePricing(@PathVariable String id, @RequestBody Decision decision) { return SuggestionService.pricingResponse(service.decidePricing(id, decision.status())); }
    @PatchMapping("/reorder-suggestions/{id}") public SuggestionService.ReorderResponse decideReorder(@PathVariable String id, @RequestBody Decision decision) { return SuggestionService.reorderResponse(service.decideReorder(id, decision.status())); }
}