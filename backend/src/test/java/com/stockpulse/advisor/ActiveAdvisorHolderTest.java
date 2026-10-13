package com.stockpulse.advisor;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActiveAdvisorHolderTest {
    @Test void switchesOnlyToRegisteredAdvisor() {
        CommerceAdvisor one = new Stub("one");
        ActiveAdvisorHolder holder = new ActiveAdvisorHolder(new AdvisorRegistry(List.of(one)), "one");
        assertEquals("one", holder.name());
        assertThrows(IllegalArgumentException.class, () -> holder.set("missing"));
        assertEquals("one", holder.name());
    }
    private record Stub(String name) implements CommerceAdvisor {
        public PricingRecommendation recommendPricing(AdvisorContext c) { return null; }
        public ReorderRecommendation recommendReorder(AdvisorContext c) { return null; }
    }
}