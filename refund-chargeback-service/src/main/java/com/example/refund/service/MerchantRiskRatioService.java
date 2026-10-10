
package com.example.refund.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.refund.dto.MerchantRiskRatioResponse;
import com.example.refund.repository.ChargebackCaseRepository;
import com.example.refund.repository.RefundCaseRepository;

@Service
public class MerchantRiskRatioService {

    private static final BigDecimal ALERT_THRESHOLD =
            new BigDecimal("5.00");

    private final ChargebackCaseRepository chargebackRepository;
    private final RefundCaseRepository refundRepository;

    public MerchantRiskRatioService(
            ChargebackCaseRepository chargebackRepository,
            RefundCaseRepository refundRepository) {
        this.chargebackRepository = chargebackRepository;
        this.refundRepository = refundRepository;
    }

    @Transactional(readOnly = true)
    public MerchantRiskRatioResponse getMerchantRiskRatio(
            Long merchantId) {

        long totalRefunds = refundRepository.countByMerchantId(merchantId);

        long totalChargebacks =
                chargebackRepository.countByMerchantId(merchantId);

        long lostChargebacks =
                chargebackRepository.countByMerchantIdAndStatus(
                        merchantId, "LOST");

        BigDecimal ratio = BigDecimal.ZERO;

        if (totalRefunds > 0) {
            ratio = BigDecimal.valueOf(lostChargebacks)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(
                            BigDecimal.valueOf(totalRefunds),
                            2,
                            RoundingMode.HALF_UP);
        }

        MerchantRiskRatioResponse response =
                new MerchantRiskRatioResponse();

        response.setMerchantId(merchantId);
        response.setTotalRefunds(totalRefunds);
        response.setTotalChargebacks(totalChargebacks);
        response.setLostChargebacks(lostChargebacks);
        response.setChargebackRatioPercent(ratio);
        response.setAboveAlertThreshold(
                ratio.compareTo(ALERT_THRESHOLD) > 0);

        return response;
    }
}