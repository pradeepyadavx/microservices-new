package com.pradeep.orderservice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Table
@Entity(name = "t_order_line_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skuId;

    private BigDecimal price;

    private Integer quantity;
}
