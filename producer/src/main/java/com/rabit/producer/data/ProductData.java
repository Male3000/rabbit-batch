package com.rabit.producer.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProductData {
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
}
