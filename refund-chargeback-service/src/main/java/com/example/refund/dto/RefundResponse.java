package com.example.refund.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.refund.entity.RefundStatus;

public class RefundResponse {

    private String refundRef;
    private String txnRef;
    private Long merchantId;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private RefundStatus status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	public String getRefundRef() {
		return refundRef;
	}
	public void setRefundRef(String refundRef) {
		this.refundRef = refundRef;
	}
	public String getTxnRef() {
		return txnRef;
	}
	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public RefundStatus getStatus() {
		return status;
	}
	public void setStatus(RefundStatus status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

    // getters and setters
}