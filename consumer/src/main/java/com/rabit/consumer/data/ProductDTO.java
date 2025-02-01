package com.rabit.consumer.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductDTO {
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
}