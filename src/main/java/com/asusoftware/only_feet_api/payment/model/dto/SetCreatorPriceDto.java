package com.asusoftware.only_feet_api.payment.model.dto;

import lombok.Data;

@Data
public class SetCreatorPriceDto {
    private Long amountCents; // ex: 999 pentru 9.99 EUR
    private String currency;  // ex: "eur"
}

