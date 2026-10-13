package com.stockpulse.ai;

import java.util.List;
import java.util.Map;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LLMGateway implements LlmClient {
    private final LlmProperties properties;
    private final RestClient client;
    public LLMGateway(LlmProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.connectTimeout());
        factory.setReadTimeout(properties.readTimeout());
        this.client = RestClient.builder().requestFactory(factory).baseUrl(properties.baseUrl()).build();
    }
    @Override public String complete(String prompt) {
        if (properties.apiKey() == null || properties.apiKey().isBlank()) throw new IllegalStateException("LLM API key is not configured");
        var request = Map.of("model", properties.model(), "messages", List.of(Map.of("role", "user", "content", prompt)));
        var call = client.post().uri("/v1/chat/completions").header("Authorization", "Bearer " + properties.apiKey()).header("Content-Type", "application/json");
        if (properties.productHeader() != null && !properties.productHeader().isBlank()) call = call.header("product", properties.productHeader());
        if (properties.cookieEnabled() && properties.cookie() != null && !properties.cookie().isBlank()) call = call.header("Cookie", properties.cookie());
        Map<?, ?> response = call.body(request).retrieve().body(Map.class);
        Object choices = response == null ? null : response.get("choices");
        if (!(choices instanceof List<?> list) || list.isEmpty()) throw new IllegalStateException("LLM response contained no choices");
        Object message = ((Map<?, ?>) list.get(0)).get("message");
        return String.valueOf(((Map<?, ?>) message).get("content"));
    }
}