package com.stockpulse.advisor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AdvisorRegistry {
    private final Map<String, CommerceAdvisor> advisors;
    public AdvisorRegistry(java.util.List<CommerceAdvisor> advisors) { this.advisors = advisors.stream().collect(Collectors.toUnmodifiableMap(CommerceAdvisor::name, Function.identity())); }
    public CommerceAdvisor get(String name) { CommerceAdvisor advisor = advisors.get(name); if (advisor == null) throw new IllegalArgumentException("Unknown advisor: " + name); return advisor; }
    public java.util.Set<String> names() { return advisors.keySet(); }
}