package com.pradeep.inventoryservice.service;

import com.pradeep.inventoryservice.dto.InventoryResponse;
import com.pradeep.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

   @Transactional(readOnly = true)
   @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuId){
            log.info("Wait Started");
            Thread.sleep(10000);
            log.info("Wait ended");
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
