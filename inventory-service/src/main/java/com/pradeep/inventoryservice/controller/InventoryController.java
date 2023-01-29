package com.pradeep.inventoryservice.controller;

import com.pradeep.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku-id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-id") String skuId){
        return inventoryService.isInStock(skuId);
    }
}
