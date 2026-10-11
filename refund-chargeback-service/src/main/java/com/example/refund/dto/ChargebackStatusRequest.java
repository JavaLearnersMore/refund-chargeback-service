package com.example.refund.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChargebackStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Remarks are required")
    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}