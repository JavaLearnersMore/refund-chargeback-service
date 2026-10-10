
package com.example.refund.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.refund.entity.ChargebackCase;

public interface ChargebackCaseRepository
        extends JpaRepository<ChargebackCase, Long> {

    boolean existsByCaseRef(String caseRef);
    
    Optional<ChargebackCase> findByCaseRef(String caseRef);
    
	long countByMerchantId(Long merchantId);
	
	long countByMerchantIdAndStatus(
	        Long merchantId, String status);
}