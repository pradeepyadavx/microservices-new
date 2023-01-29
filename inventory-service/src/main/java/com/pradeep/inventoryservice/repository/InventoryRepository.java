package com.pradeep.inventoryservice.repository;

import com.pradeep.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository  extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findBySkuId(String skuId);
}
