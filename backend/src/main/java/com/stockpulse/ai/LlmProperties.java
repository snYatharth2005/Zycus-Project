package com.stockpulse.ai;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stockpulse.llm")
public record LlmProperties(String provider, String model, String baseUrl, String apiKey,
                             Duration connectTimeout, Duration readTimeout, boolean cookieEnabled,
                             String cookie, String productHeader) { }