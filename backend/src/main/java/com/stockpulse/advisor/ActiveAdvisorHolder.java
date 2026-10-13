package com.stockpulse.advisor;

import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActiveAdvisorHolder {
    private final AdvisorRegistry registry;
    private final AtomicReference<String> active;
    public ActiveAdvisorHolder(AdvisorRegistry registry, @Value("${stockpulse.advisor.active:rule-based}") String initial) { this.registry = registry; registry.get(initial); this.active = new AtomicReference<>(initial); }
    public CommerceAdvisor current() { return registry.get(active.get()); }
    public String name() { return active.get(); }
    public String set(String name) { registry.get(name); active.set(name); return name; }
    public java.util.Set<String> available() { return registry.names(); }
}