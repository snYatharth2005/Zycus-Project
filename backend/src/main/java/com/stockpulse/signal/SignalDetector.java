package com.stockpulse.signal;

import com.stockpulse.product.Product;
import com.stockpulse.product.ProductRepository;
import com.stockpulse.suggestion.TriggerReason;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SignalDetector {
    private final ProductRepository products;
    private final BigDecimal multiplier;
    public SignalDetector(ProductRepository products, @Value("${stockpulse.demand-spike-multiplier:3.0}") BigDecimal multiplier) { this.products = products; this.multiplier = multiplier; }
    public List<TriggerReason> detect(Product product) {
        List<TriggerReason> signals = new ArrayList<>();
        if (product.getStockLevel() < product.getReorderThreshold()) signals.add(TriggerReason.INVENTORY_LOW);
        BigDecimal average = products.findAll().stream().filter(p -> p.getCategory() == product.getCategory()).map(Product::getDemandVelocity).reduce(BigDecimal.ZERO, BigDecimal::add);
        long count = products.findAll().stream().filter(p -> p.getCategory() == product.getCategory()).count();
        if (count > 0 && average.divide(BigDecimal.valueOf(count), 4, java.math.RoundingMode.HALF_UP).multiply(multiplier).compareTo(product.getDemandVelocity()) < 0) signals.add(TriggerReason.DEMAND_SPIKE);
        return signals;
    }
}