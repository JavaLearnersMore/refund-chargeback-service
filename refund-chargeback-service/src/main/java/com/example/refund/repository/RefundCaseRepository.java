package com.example.refund.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.refund.entity.RefundCase;

public interface RefundCaseRepository extends JpaRepository<RefundCase, Long> {

    Optional<RefundCase> findByRefundRef(String refundRef);

    boolean existsByTxnRefAndMerchantId(String txnRef, Long merchantId);
    
    long countByMerchantId(Long merchantId);
}