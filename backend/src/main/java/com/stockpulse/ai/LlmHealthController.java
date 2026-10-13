package com.stockpulse.ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/llm-health")
public class LlmHealthController {
    private final LlmClient client;
    public LlmHealthController(LlmClient client) { this.client = client; }
    public record Response(String status, String reason) { }
    @GetMapping public Response health() {
        try { client.complete("Reply with exactly: OK"); return new Response("ok", "test call succeeded"); }
        catch (RuntimeException exception) { return new Response("failed", safeReason(exception)); }
    }
    private String safeReason(RuntimeException exception) {
        String reason = exception.getMessage();
        if (reason == null || reason.isBlank() || reason.toLowerCase().contains("key") || reason.toLowerCase().contains("bearer")) return "LLM test call failed";
        return reason.length() > 200 ? reason.substring(0, 200) : reason;
    }
}