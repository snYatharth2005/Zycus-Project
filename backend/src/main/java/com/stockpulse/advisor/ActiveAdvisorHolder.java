package com.stockpulse.advisor;

import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.stockpulse.ai.LlmProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ActiveAdvisorHolder {
    private static final Logger log = LoggerFactory.getLogger(ActiveAdvisorHolder.class);
    private final AdvisorRegistry registry;
    private final AtomicReference<String> active;
    @Autowired
    public ActiveAdvisorHolder(AdvisorRegistry registry, LlmProperties llm, @Value("${stockpulse.advisor.active:}") String configured) {
        this.registry = registry;
        String initial = configured == null || configured.isBlank() ? (llm.apiKey() != null && !llm.apiKey().isBlank() ? "ai" : "rule-based") : configured;
        registry.get(initial); this.active = new AtomicReference<>(initial);
        log.info("LLM key present: {}", llm.apiKey() != null && !llm.apiKey().isBlank() ? "yes" : "no");
        log.info("active strategy: {}", initial);
    }
    public ActiveAdvisorHolder(AdvisorRegistry registry, String configured) {
        this.registry = registry;
        registry.get(configured);
        this.active = new AtomicReference<>(configured);
    }
    public CommerceAdvisor current() { return registry.get(active.get()); }
    public String name() { return active.get(); }
    public String set(String name) { registry.get(name); active.set(name); return name; }
    public java.util.Set<String> available() { return registry.names(); }
}