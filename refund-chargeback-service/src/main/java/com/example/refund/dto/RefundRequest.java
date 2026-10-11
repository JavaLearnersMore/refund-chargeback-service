package com.example.refund.dto;

import java.math.BigDecimal;

import javax.validation.constraints.*;

public class RefundRequest {

    @NotBlank(message = "Transaction reference is required")
    @Size(max = 50, message = "Transaction reference cannot exceed 50 characters")
    private String txnRef;

    @NotNull(message = "Merchant ID is required")
    @Positive(message = "Merchant ID must be positive")
    private Long merchantId;

    @NotNull(message = "Refund amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be greater than 0")
    @Digits(integer = 17, fraction = 2,
            message = "Refund amount must have maximum 2 decimal places")
    private BigDecimal amount;

    @NotBlank(message = "Refund reason is required")
    @Size(min = 3, max = 500,
            message = "Reason must be between 3 and 500 characters")
    private String reason;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}