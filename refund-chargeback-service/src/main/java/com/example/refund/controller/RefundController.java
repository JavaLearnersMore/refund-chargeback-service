package com.example.refund.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.refund.dto.RefundRequest;
import com.example.refund.dto.RefundResponse;
import com.example.refund.service.RefundService;

@RestController
@RequestMapping("/rc/api/v1")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/refunds")
    @PreAuthorize("hasAnyRole('MERCHANT', 'SERVICE')")
    public ResponseEntity<RefundResponse> requestRefund( @Valid @RequestBody RefundRequest request) {

        RefundResponse response = refundService.requestRefund(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}