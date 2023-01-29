package com.pradeep.inventoryservice;

import com.pradeep.inventoryservice.model.Inventory;
import com.pradeep.inventoryservice.repository.InventoryRepository;
import com.pradeep.inventoryservice.service.InventoryService;
import lombok.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
			return args -> {
				Inventory inventory= Inventory.builder()
						.skuId("iphone14")
						.quantity(100)
						.build();

				Inventory inventory1=Inventory.builder()
						.skuId("iphone14-red")
						.quantity(0)
						.build();

				inventoryRepository.save(inventory);
				inventoryRepository.save(inventory1);
			};
	}

}
