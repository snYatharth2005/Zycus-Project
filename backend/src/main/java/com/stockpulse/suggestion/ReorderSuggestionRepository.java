package com.stockpulse.suggestion;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, String> { }