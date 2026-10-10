package com.example.refund.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.refund.config.RefundConfiguration;
import com.example.refund.dto.RefundRequest;
import com.example.refund.dto.RefundResponse;
import com.example.refund.entity.RefundCase;
import com.example.refund.entity.RefundStatus;
import com.example.refund.repository.RefundCaseRepository;

@Service
public class RefundService {

    private final RefundCaseRepository refundCaseRepository;
    private final RefundConfiguration refundConfiguration;

    public RefundService(RefundCaseRepository refundCaseRepository,RefundConfiguration refundConfiguration) {
        this.refundCaseRepository = refundCaseRepository;
        this.refundConfiguration = refundConfiguration;
    }

    @Transactional
    public RefundResponse requestRefund(RefundRequest request) {

        // Prevent duplicate refund
        if (refundCaseRepository
                .existsByTxnRefAndMerchantId(
                        request.getTxnRef(),
                        request.getMerchantId())) {

            throw new IllegalArgumentException(
                    "Refund already requested for transaction: "
                            + request.getTxnRef());
        }

        BigDecimal threshold = refundConfiguration.getDefaultThreshold();

        LocalDateTime now = LocalDateTime.now();

        RefundCase refundCase = new RefundCase();

        refundCase.setRefundRef(generateRefundRef());
        refundCase.setTxnRef(request.getTxnRef());
        refundCase.setMerchantId(request.getMerchantId());
        refundCase.setRequestedAmount(request.getAmount());
        refundCase.setReason(request.getReason());

        /*
         * Auto approval
         */
        if (request.getAmount().compareTo(threshold) <= 0) {

            refundCase.setApprovedAmount(request.getAmount());
            refundCase.setStatus(RefundStatus.COMPLETED);

        } else {

            refundCase.setApprovedAmount(null);
            refundCase.setStatus(RefundStatus.PENDING_OPS);
        }

        RefundCase saved = refundCaseRepository.save(refundCase);

        return buildResponse(saved, now);
    }

    private String generateRefundRef() {

        String timestamp =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(
                                "yyyyMMddHHmmss"));

        String random =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 6)
                        .toUpperCase();

        return "RCRFD" + timestamp + random;
    }

    private RefundResponse buildResponse(
            RefundCase refundCase,
            LocalDateTime updatedAt) {

        RefundResponse response = new RefundResponse();

        response.setRefundRef(refundCase.getRefundRef());
        response.setTxnRef(refundCase.getTxnRef());
        response.setMerchantId(refundCase.getMerchantId());
        response.setRequestedAmount(refundCase.getRequestedAmount());
        response.setApprovedAmount(refundCase.getApprovedAmount());
        response.setStatus(refundCase.getStatus());
        response.setReason(refundCase.getReason());
        response.setCreatedAt(refundCase.getCreatedAt());
        response.setUpdatedAt(refundCase.getUpdatedAt());

        return response;
    }
    

	@Transactional
	public RefundResponse approveRefund(String refundRef) {
	
	    RefundCase refundCase = refundCaseRepository
	            .findByRefundRef(refundRef)
	            .orElseThrow(() ->
	                    new IllegalArgumentException(
	                            "Refund not found: " + refundRef));

	    if (refundCase.getStatus() != RefundStatus.PENDING_OPS) {
	        throw new IllegalStateException(
	                "Refund is not pending Ops approval. Current status: "
	                        + refundCase.getStatus());
	    }
	
	    LocalDateTime now = LocalDateTime.now();
	
	    // Approve the full requested amount
	    refundCase.setApprovedAmount(
	            refundCase.getRequestedAmount());
	
	    // Mark the refund completed
	    refundCase.setStatus(RefundStatus.COMPLETED);
	
	    RefundCase saved = refundCaseRepository.save(refundCase);
	
	    return buildResponse(saved, now);
	}
}