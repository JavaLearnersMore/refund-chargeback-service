package com.example.refund.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefundConfiguration {

    @Value("${refund.merchant.default-threshold}")
    private BigDecimal defaultThreshold;

    public BigDecimal getDefaultThreshold() {
        return defaultThreshold;
    }
}