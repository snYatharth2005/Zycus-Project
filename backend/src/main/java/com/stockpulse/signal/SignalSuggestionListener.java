package com.stockpulse.signal;

import com.stockpulse.suggestion.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SignalSuggestionListener {
    private static final Logger log = LoggerFactory.getLogger(SignalSuggestionListener.class);
    private final SuggestionService suggestions;
    public SignalSuggestionListener(SuggestionService suggestions) { this.suggestions = suggestions; }
    @Async("signalExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSignal(InventorySignalEvent event) {
        try { suggestions.suggestPricing(event.productId(), event.triggerReason()); suggestions.suggestReorder(event.productId(), event.triggerReason()); }
        catch (RuntimeException exception) { log.error("Async recommendation failed for product {} trigger {}", event.productId(), event.triggerReason(), exception); }
    }
}