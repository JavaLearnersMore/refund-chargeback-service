
package com.example.refund.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.refund.dto.RefundResponse;
import com.example.refund.service.RefundService;

@RestController
@RequestMapping("/rc/internal/refunds")
public class RefundOpsController {

    private final RefundService refundService;

    public RefundOpsController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PutMapping("/{refundRef}/approve")
    @PreAuthorize("hasAnyRole('OPS', 'ADMIN')")
    public ResponseEntity<RefundResponse> approveRefund(@PathVariable String refundRef) {

    	System.out.println("Inside approveRefund()");
        RefundResponse response = refundService.approveRefund(refundRef);

        return ResponseEntity.ok(response);
    }
}