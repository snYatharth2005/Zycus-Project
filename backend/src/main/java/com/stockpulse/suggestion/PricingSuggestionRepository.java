package com.stockpulse.suggestion;

import org.springframework.data.jpa.repository.JpaRepository;
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, String> {
    long countByProductId(String productId);
}