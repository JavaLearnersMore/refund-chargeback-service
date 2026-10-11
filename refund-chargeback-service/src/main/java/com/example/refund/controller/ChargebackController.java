
package com.example.refund.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.refund.dto.ChargebackRequest;
import com.example.refund.dto.ChargebackResponse;
import com.example.refund.dto.ChargebackStatusRequest;
import com.example.refund.service.ChargebackService;

@RestController
@RequestMapping
public class ChargebackController {

    private final ChargebackService chargebackService;

    public ChargebackController(ChargebackService chargebackService) {
        this.chargebackService = chargebackService;
    }

    @PostMapping("/rc/api/v1/chargebacks")
    public ResponseEntity<ChargebackResponse> openChargeback(
            @Valid @RequestBody ChargebackRequest request) {

        ChargebackResponse response = chargebackService.openCase(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/rc/internal/chargebacks/{caseRef}/status")
    public ResponseEntity<ChargebackResponse> updateStatus(
            @PathVariable String caseRef, @Valid @RequestBody ChargebackStatusRequest request) {

        ChargebackResponse response = chargebackService.updateStatus(caseRef, request);

        return ResponseEntity.ok(response);
    }
}