package com.stockpulse.suggestion;

import org.springframework.data.jpa.repository.JpaRepository;
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, String> {
    long countByProductId(String productId);
    java.util.List<PricingSuggestion> findByStatusAndProductId(SuggestionStatus status, String productId);
    java.util.List<PricingSuggestion> findByStatus(SuggestionStatus status);
    boolean existsByProductIdAndStatusAndTriggerReason(String productId, SuggestionStatus status, TriggerReason triggerReason);
    java.util.List<PricingSuggestion> findByProductIdAndStatus(String productId, SuggestionStatus status);
}