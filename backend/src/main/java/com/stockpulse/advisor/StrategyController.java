package com.stockpulse.advisor;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/strategy")
public class StrategyController {
    private final ActiveAdvisorHolder holder;
    public StrategyController(ActiveAdvisorHolder holder) { this.holder = holder; }
    public record Response(String active, java.util.Set<String> available) { }
    public record Request(@NotBlank String active) { }
    @GetMapping public Response get() { return new Response(holder.name(), holder.available()); }
    @PutMapping public Response put(@jakarta.validation.Valid @RequestBody Request request) {
        try { return new Response(holder.set(request.active().trim().toLowerCase()), holder.available()); }
        catch (IllegalArgumentException exception) { throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, exception.getMessage(), exception); }
    }
}