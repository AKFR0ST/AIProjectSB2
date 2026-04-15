package com.sb2.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String name;
    private BigDecimal price;
    private String supplier;
}
