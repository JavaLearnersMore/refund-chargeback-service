
package com.example.refund.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.refund.dto.MerchantRiskRatioResponse;
import com.example.refund.service.MerchantRiskRatioService;

@RestController
@RequestMapping("/rc/api/v1/reports/merchant")
public class MerchantRiskRatioController {

    private final MerchantRiskRatioService riskRatioService;

    public MerchantRiskRatioController(
            MerchantRiskRatioService riskRatioService) {
        this.riskRatioService = riskRatioService;
    }

    @GetMapping("/{merchantId}/ratio")
    public ResponseEntity<MerchantRiskRatioResponse> getRiskRatio(
            @PathVariable Long merchantId) {

        if (merchantId <= 0) {
            throw new IllegalArgumentException(
                    "Merchant ID must be greater than zero");
        }

        return ResponseEntity.ok(
                riskRatioService.getMerchantRiskRatio(merchantId));
    }
}