package com.stockpulse.suggestion;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, String> {
    java.util.List<ReorderSuggestion> findByStatusAndProductId(SuggestionStatus status, String productId);
    java.util.List<ReorderSuggestion> findByStatus(SuggestionStatus status);
    boolean existsByProductIdAndStatusAndTriggerReason(String productId, SuggestionStatus status, TriggerReason triggerReason);
    java.util.List<ReorderSuggestion> findByProductIdAndStatus(String productId, SuggestionStatus status);
}