package com.pradeep.orderservice.service;

import com.pradeep.orderservice.dto.InventoryResponse;
import com.pradeep.orderservice.dto.OrderLineItemDto;
import com.pradeep.orderservice.dto.OrderRequest;
import com.pradeep.orderservice.event.OrderPlacedEvent;
import com.pradeep.orderservice.model.Order;
import com.pradeep.orderservice.model.OrderLineItems;
import com.pradeep.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webBuilder;

    // for custom traceid in sleuth zpikin
    private final Tracer tracer;

    // for kafka
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);


        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuId)
                .toList();

        log.info("Calling inventory service !!");

        // need to check if order is in stock using inventory call

        /* url if we call direct*/
       // String url="http://localhost:9092/api/inventory";

        /* url if we use  discovery server  direct
        * need to use laod balancer if have multiple instances
        * */
        String url="http://inventory-service/api/inventory";

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try(Tracer.SpanInScope spanInScope= tracer.withSpan(inventoryServiceLookup.start())) {

            InventoryResponse[] inventoryResponseArray = webBuilder.build().get()
                    .uri(url,
                            uriBuilder -> uriBuilder.queryParam("skuId", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

            if(allProductsInStock){
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
                return "order placed successfully";
            }else {
                throw new IllegalArgumentException("Product is not in stock ,please try again later");
            }

        }finally {
            inventoryServiceLookup.end();
        }



    }

    private OrderLineItems mapToDto(OrderLineItemDto orderLineItemDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuId(orderLineItemDto.getSkuId());
        orderLineItems.setPrice(orderLineItemDto.getPrice());
        orderLineItems.setQuantity(orderLineItemDto.getQuantity());
        return orderLineItems;
    }
}
