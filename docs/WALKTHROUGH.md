# StockPulse walkthrough

`ProductController.order` calls `ProductService.order`, which decrements stock, increments velocity, and calls `SignalDetector.detect`. The service publishes `InventorySignalEvent`; `SignalSuggestionListener.onSignal` receives it with `@TransactionalEventListener(AFTER_COMMIT)` and `@Async("signalExecutor")`, then calls `SuggestionService.suggestPricing` and `suggestReorder`. The active advisor returns explicit provenance. `SuggestionService.decidePricing` changes `currentPrice`, supersedes other pending pricing suggestions, and recomputes status in one transaction.

## Interview questions

1. Why after commit? Rolled-back orders must not create durable recommendations.
2. Why async? HTTP mutations return without waiting for LLM or recommendation persistence.
3. How are duplicates prevented? Pending product/type/trigger checks plus a unique dedupe key.
4. Why one advisor interface? Runtime switching does not change commerce orchestration.
5. What if AI fails? `AiAdvisor` independently falls back to `RuleBasedAdvisor` and records a safe reason.
6. When does price change? Only an accepted pricing decision changes it.
7. How are conflicts reported? JPA optimistic-lock exceptions become HTTP 409 ProblemDetail.
8. Why SQLite? It is a persistent zero-setup demo while PostgreSQL remains available.
9. Why bounded async execution? It limits database and gateway pressure.
10. What are the growth seams? Cost/margin, suppliers, auth, audit, PostgreSQL scale-out, and policy controls.