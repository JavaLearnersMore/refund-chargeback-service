
package com.example.refund.dto;

import java.math.BigDecimal;

public class MerchantRiskRatioResponse {

    private Long merchantId;
    private long totalRefunds;
    private long totalChargebacks;
    private long lostChargebacks;
    private BigDecimal chargebackRatioPercent;
    private boolean aboveAlertThreshold;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public long getTotalRefunds() {
        return totalRefunds;
    }

    public void setTotalRefunds(long totalRefunds) {
        this.totalRefunds = totalRefunds;
    }

    public long getTotalChargebacks() {
        return totalChargebacks;
    }

    public void setTotalChargebacks(long totalChargebacks) {
        this.totalChargebacks = totalChargebacks;
    }

    public long getLostChargebacks() {
        return lostChargebacks;
    }

    public void setLostChargebacks(long lostChargebacks) {
        this.lostChargebacks = lostChargebacks;
    }

    public BigDecimal getChargebackRatioPercent() {
        return chargebackRatioPercent;
    }

    public void setChargebackRatioPercent(
            BigDecimal chargebackRatioPercent) {
        this.chargebackRatioPercent = chargebackRatioPercent;
    }

    public boolean isAboveAlertThreshold() {
        return aboveAlertThreshold;
    }

    public void setAboveAlertThreshold(
            boolean aboveAlertThreshold) {
        this.aboveAlertThreshold = aboveAlertThreshold;
    }
}