package com.rabit.consumer.data;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class ProductDTO {
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
}