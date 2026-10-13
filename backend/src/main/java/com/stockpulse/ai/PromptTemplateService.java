package com.stockpulse.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PromptTemplateService {
    public String render(String type, String trigger, String context) {
        String file = type + "-" + trigger.toLowerCase().replace('_', '-') + ".txt";
        try (var input = new ClassPathResource("prompts/" + file).getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8) + "\n\n" + context;
        } catch (IOException exception) {
            throw new IllegalStateException("Missing prompt template: " + file, exception);
        }
    }
}