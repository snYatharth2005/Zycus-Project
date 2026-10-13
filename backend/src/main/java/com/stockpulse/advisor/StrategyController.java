package com.stockpulse.advisor;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/strategy")
public class StrategyController {
    private final ActiveAdvisorHolder holder;
    public StrategyController(ActiveAdvisorHolder holder) { this.holder = holder; }
    public record Response(String active, java.util.Set<String> available) { }
    public record Request(@NotBlank String name) { }
    @GetMapping public Response get() { return new Response(holder.name(), holder.available()); }
    @PutMapping public Response put(@jakarta.validation.Valid @RequestBody Request request) { return new Response(holder.set(request.name()), holder.available()); }
}