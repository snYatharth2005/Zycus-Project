package com.stockpulse.product;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ProductDtos.Response create(@Valid @RequestBody ProductDtos.CreateRequest request) { return ProductService.response(service.create(request)); }
    @GetMapping public List<ProductDtos.Response> list(@RequestParam(required = false) ProductStatus status, @RequestParam(required = false) Category category) { return service.list(status, category).stream().map(ProductService::response).toList(); }
    @GetMapping("/{id}") public ProductDtos.Response get(@PathVariable String id) { return ProductService.response(service.get(id)); }
    @PatchMapping("/{id}/stock") public ProductDtos.Response stock(@PathVariable String id, @Valid @RequestBody ProductDtos.StockRequest request) { return ProductService.response(service.updateStock(id, request.stockLevel())); }
    @PostMapping("/{id}/orders") public ProductDtos.Response order(@PathVariable String id, @Valid @RequestBody ProductDtos.OrderRequest request) { return ProductService.response(service.order(id, request.quantity())); }
}