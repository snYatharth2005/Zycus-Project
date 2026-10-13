package com.stockpulse.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecommendationParserTest {
    private final RecommendationParser parser = new RecommendationParser(new ObjectMapper());
    @Test void stripsThinkBlockFenceAndExtractsObject() {
        var result = parser.pricing("<think>private reasoning</think> text ```json {\"recommendedPrice\": 29.99, \"direction\": \"INCREASE\", \"confidence\": 1.4, \"reasoning\": \"protect stock\"} ``` trailing");
        assertEquals(new BigDecimal("29.99"), result.recommendedPrice());
        assertEquals(BigDecimal.ONE, result.confidence());
    }
    @Test void rejectsZeroPrice() { assertThrows(IllegalArgumentException.class, () -> parser.pricing("{\"recommendedPrice\":0,\"direction\":\"HOLD\",\"confidence\":0.5,\"reasoning\":\"x\"}")); }
    @Test void rejectsNegativeQuantity() { assertThrows(IllegalArgumentException.class, () -> parser.reorder("{\"recommendedQuantity\":-1,\"suggestedLeadTimeDays\":2,\"confidence\":0.5,\"reasoning\":\"x\"}")); }
    @Test void rejectsMalformedJson() { assertThrows(IllegalArgumentException.class, () -> parser.pricing("not json")); }
}