package com.pradeep.inventoryservice.service;

import com.pradeep.inventoryservice.dto.InventoryResponse;
import com.pradeep.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

   @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuId){
            return  inventoryRepository.findBySkuIdIn(skuId)
                    .stream()
                    .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuId())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                    ).toList();
    }
}
