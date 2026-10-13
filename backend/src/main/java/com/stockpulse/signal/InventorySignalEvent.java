package com.stockpulse.signal;

import com.stockpulse.suggestion.TriggerReason;

public record InventorySignalEvent(String productId, TriggerReason triggerReason) { }