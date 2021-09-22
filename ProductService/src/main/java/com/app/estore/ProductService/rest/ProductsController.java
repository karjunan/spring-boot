package com.app.estore.ProductService.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @PostMapping
    public String createProduct() {
        return "create Product";
    }


    @GetMapping
    public String getProduct() {

        return "Get Product";
    }

    @PutMapping
    public String updateProduct() {

        return "Put request";
    }

    @DeleteMapping
    public String deleteProduct() {

        return "Delete request";
    }

}
