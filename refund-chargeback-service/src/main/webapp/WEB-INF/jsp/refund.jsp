
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Refund and Chargeback Management</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/refund.css">

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            background: #f3f6fb;
            color: #243247;
            font-family: Arial, Helvetica, sans-serif;
            line-height: 1.5;
        }

        .refund-forms-container {
            width: min(100% - 32px, 1000px);
            margin: 32px auto;
        }

        .page-heading {
            margin: 0 0 8px;
            font-size: 28px;
            line-height: 1.3;
            color: #172b4d;
        }

        .page-description {
            margin: 0 0 26px;
            color: #64748b;
        }

        .refund-section {
            margin-bottom: 24px;
        }

        .refund-form-container {
            padding: 26px;
            background: #ffffff;
            border: 1px solid #e0e7f0;
            border-radius: 12px;
            box-shadow: 0 4px 14px rgba(24, 39, 75, 0.06);
        }

        .refund-form-container h2 {
            margin: 0 0 10px;
            font-size: 21px;
            color: #243b5a;
        }

        .section-description {
            margin: 0 0 20px;
            color: #64748b;
            font-size: 14px;
        }

        .refund-form-container form {
            display: flex;
            flex-direction: column;
            align-items: stretch;
            gap: 8px;
            width: 100%;
        }

        .form-field {
            display: flex;
            flex-direction: column;
            min-width: 0;
            margin-bottom: 8px;
        }

        .form-field label {
            display: block;
            margin-bottom: 7px;
            font-size: 14px;
            font-weight: 600;
            color: #334155;
        }

        .form-field input,
        .form-field select,
        .form-field textarea {
            display: block;
            width: 100%;
            min-width: 0;
            padding: 11px 12px;
            border: 1px solid #cbd5e1;
            border-radius: 6px;
            background: #fff;
            color: #1e293b;
            font-family: inherit;
            font-size: 14px;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .form-field input,
        .form-field select {
            min-height: 43px;
        }

        .form-field textarea {
            min-height: 95px;
            resize: vertical;
        }

        .form-field input:focus,
        .form-field select:focus,
        .form-field textarea:focus {
            border-color: #1769aa;
            box-shadow: 0 0 0 3px rgba(23, 105, 170, 0.12);
        }

        .refund-form-container button {
            align-self: flex-start;
            min-height: 42px;
            margin-top: 8px;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            background: #1769aa;
            color: white;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }

        .refund-form-container button:hover {
            background: #105384;
        }

        .refund-form-container button:disabled {
            opacity: 0.65;
            cursor: wait;
        }

        .refund-response-message {
            width: 100%;
            min-width: 0;
            margin-top: 22px;
            overflow-wrap: anywhere;
        }

        .response-status {
            margin: 0 0 10px;
            font-weight: 700;
            font-size: 14px;
        }

        .response-status.success {
            color: #167647;
        }

        .response-status.error {
            color: #b42318;
        }

        .response-details {
            width: 100%;
            overflow: hidden;
            border: 1px solid #dbe3ee;
            border-radius: 8px;
            background: #f8fafc;
        }

        .response-details summary {
            padding: 12px 15px;
            color: #334155;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            user-select: none;
        }

        .response-details[open] summary {
            border-bottom: 1px solid #dbe3ee;
        }

        .response-json {
            display: block;
            width: 100%;
            max-height: 400px;
            margin: 0;
            padding: 16px;
            overflow: auto;
            white-space: pre;
            overflow-wrap: normal;
            tab-size: 4;
            background: #f1f5f9;
            color: #1e293b;
            font-family: Consolas, "Courier New", monospace;
            font-size: 13px;
            line-height: 1.7;
        }

        .response-placeholder {
            color: #64748b;
            font-size: 14px;
        }

        @media (max-width: 600px) {
            .refund-forms-container {
                width: calc(100% - 20px);
                margin: 18px auto;
            }

            .page-heading {
                font-size: 23px;
            }

            .refund-form-container {
                padding: 18px;
            }

            .refund-form-container h2 {
                font-size: 19px;
            }

            .refund-form-container button {
                width: 100%;
            }

            .response-json {
                font-size: 12px;
            }
        }
    </style>
</head>

<body>

<div class="refund-forms-container">

    <h1 class="page-heading">Refund and Chargeback Management</h1>
    <p class="page-description">
        Manage refunds, chargeback cases, and merchant risk reports.
    </p>

    <!-- 1. Request Refund -->
    <section class="refund-section">
        <div class="refund-form-container">
            <h2>1. Request Refund</h2>
            <p class="section-description">
                Automatically completes eligible refunds or queues them
                for Ops approval according to the merchant's threshold.
            </p>

            <form id="refundForm">
                <div class="form-field">
                    <label for="txnRef">Transaction Reference</label>
                    <input type="text" id="txnRef"
                           placeholder="PGTXN20260823101500ABCDEF" required>
                </div>

                <div class="form-field">
                    <label for="merchantId">Merchant ID</label>
                    <input type="number" id="merchantId"
                           min="1" step="1" placeholder="1001" required>
                </div>

                <div class="form-field">
                    <label for="amount">Amount</label>
                    <input type="number" id="amount"
                           min="0.01" step="0.01"
                           placeholder="300.00" required>
                </div>

                <div class="form-field">
                    <label for="reason">Reason</label>
                    <textarea id="reason"
                              placeholder="Customer requested refund"
                              required></textarea>
                </div>

                <button type="submit">Request Refund</button>
            </form>

            <div id="refundResponseMessage"
                 class="refund-response-message" aria-live="polite"></div>
        </div>
    </section>

    <!-- 2. Approve Refund -->
    <section class="refund-section">
        <div class="refund-form-container">
            <h2>2. Ops: Approve Refund</h2>
            <p class="section-description">
                Approves and executes a refund pending Ops approval.
            </p>

            <form id="approveRefundForm">
                <div class="form-field">
                    <label for="approveRefundRef">Refund Reference</label>
                    <input type="text" id="approveRefundRef"
                           placeholder="RCRFD20260823104500ABC999" required>
                </div>

                <button type="submit">Approve Refund</button>
            </form>

            <div id="approveRefundResponse"
                 class="refund-response-message" aria-live="polite"></div>
        </div>
    </section>

    <!-- 3. Open Chargeback Case -->
    <section class="refund-section">
        <div class="refund-form-container">
            <h2>3. Open Chargeback Case</h2>
            <p class="section-description">
                Creates a chargeback case for a transaction.
            </p>

            <form id="chargebackForm">
                <div class="form-field">
                    <label for="chargebackTxnRef">Transaction Reference</label>
                    <input type="text" id="chargebackTxnRef"
                           placeholder="PGTXN20260823101500ABCDEF" required>
                </div>

                <div class="form-field">
                    <label for="customerId">Customer ID</label>
                    <input type="number" id="customerId"
                           min="1" step="1" placeholder="5001" required>
                </div>

                <div class="form-field">
                    <label for="chargebackMerchantId">Merchant ID</label>
                    <input type="number" id="chargebackMerchantId"
                           min="1" step="1" placeholder="1001" required>
                </div>

                <div class="form-field">
                    <label for="reasonCode">Reason Code</label>
                    <select id="reasonCode" required>
                        <option value="">Select reason</option>
                        <option value="GOODS_NOT_RECEIVED">GOODS_NOT_RECEIVED</option>
                        <option value="DUPLICATE_TRANSACTION">DUPLICATE_TRANSACTION</option>
                        <option value="FRAUDULENT_TRANSACTION">FRAUDULENT_TRANSACTION</option>
                        <option value="INCORRECT_AMOUNT">INCORRECT_AMOUNT</option>
                        <option value="PRODUCT_NOT_AS_DESCRIBED">PRODUCT_NOT_AS_DESCRIBED</option>
                        <option value="OTHER">OTHER</option>
                    </select>
                </div>

                <div class="form-field">
                    <label for="chargebackAmount">Amount</label>
                    <input type="number" id="chargebackAmount"
                           min="0.01" step="0.01"
                           placeholder="750.00" required>
                </div>

                <button type="submit">Open Chargeback Case</button>
            </form>

            <div id="chargebackResponse"
                 class="refund-response-message" aria-live="polite"></div>
        </div>
    </section>

    <!-- 4. Update Chargeback Status -->
    <section class="refund-section">
        <div class="refund-form-container">
            <h2>4. Ops: Update Chargeback Status</h2>
            <p class="section-description">
                Updates the chargeback lifecycle. The backend validates
                status transitions and handles fund reversal when marked LOST.
            </p>

            <form id="updateChargebackForm">
                <div class="form-field">
                    <label for="caseRef">Chargeback Case Reference</label>
                    <input type="text" id="caseRef"
                           placeholder="CBCASE20260823105000QWE456" required>
                </div>

                <div class="form-field">
                    <label for="chargebackStatus">New Status</label>
                    <select id="chargebackStatus" required>
                        <option value="">Select status</option>
                        <option value="EVIDENCE_COLLECTION">EVIDENCE_COLLECTION</option>
                        <option value="REPRESENTED">REPRESENTED</option>
                        <option value="WON">WON</option>
                        <option value="LOST">LOST</option>
                        <option value="CLOSED">CLOSED</option>
                    </select>
                </div>

                <div class="form-field">
                    <label for="chargebackRemarks">Remarks</label>
                    <textarea id="chargebackRemarks"
                              placeholder="Merchant could not provide proof of delivery"
                              required></textarea>
                </div>

                <button type="submit">Update Chargeback Status</button>
            </form>

            <div id="updateChargebackResponse"
                 class="refund-response-message" aria-live="polite"></div>
        </div>
    </section>

    <!-- 5. Merchant Risk Ratio Report -->
    <section class="refund-section">
        <div class="refund-form-container">
            <h2>5. Merchant Risk Ratio Report</h2>
            <p class="section-description">
                Retrieves the refund and chargeback risk report for a merchant.
            </p>

            <form id="riskReportForm">
                <div class="form-field">
                    <label for="reportMerchantId">Merchant ID</label>
                    <input type="number" id="reportMerchantId"
                           min="1" step="1" placeholder="1001" required>
                </div>

                <button type="submit">Get Risk Report</button>
            </form>

            <div id="riskReportResponse"
                 class="refund-response-message" aria-live="polite"></div>
        </div>
    </section>

</div>

<script>
    const contextPath = "${pageContext.request.contextPath}";

    const API = {
        refunds: contextPath + "/rc/api/v1/refunds",

        approveRefund: function(refundRef) {
            return contextPath + "/rc/internal/refunds/"
                + encodeURIComponent(refundRef) + "/approve";
        },

        chargebacks: contextPath + "/rc/api/v1/chargebacks",

        updateChargeback: function(caseRef) {
            return contextPath + "/rc/internal/chargebacks/"
                + encodeURIComponent(caseRef) + "/status";
        },

        merchantRiskReport: function(merchantId) {
            return contextPath + "/rc/api/v1/reports/merchant/"
                + encodeURIComponent(merchantId) + "/ratio";
        }
    };

    // Format JSON with indentation; preserve non-JSON response bodies.
    function formatResponseBody(responseText) {
        try {
            const parsed = JSON.parse(responseText);
            return JSON.stringify(parsed, null, 4);
        } catch (error) {
            return responseText || "(Empty response body)";
        }
    }

    // Common API method.
    async function callApi(url, method, data, responseElementId) {
        const output = document.getElementById(responseElementId);
        const form = output.closest(".refund-form-container")
                           .querySelector("form");
        const submitButton = form.querySelector('button[type="submit"]');

        output.replaceChildren();

        const loading = document.createElement("p");
        loading.className = "response-placeholder";
        loading.textContent = "Processing request...";
        output.appendChild(loading);

        submitButton.disabled = true;

        try {
            const options = {
                method: method,
                headers: {
                    "Accept": "application/json"
                }
            };

            if (data !== undefined && data !== null) {
                options.headers["Content-Type"] = "application/json";
                options.body = JSON.stringify(data);
            }

            const response = await fetch(url, options);
            const responseText = await response.text();

            output.replaceChildren();

            const status = document.createElement("p");
            status.className = "response-status "
                + (response.ok ? "success" : "error");
            status.textContent =
                "HTTP " + response.status + " " + response.statusText;

            output.appendChild(status);

            const details = document.createElement("details");
            details.className = "response-details";
            details.open = true;

            const summary = document.createElement("summary");
            summary.textContent = "Response Body (JSON) — Click to collapse";

            const pre = document.createElement("pre");
            pre.className = "response-json";
            pre.textContent = formatResponseBody(responseText);

            details.appendChild(summary);
            details.appendChild(pre);
            output.appendChild(details);

            // Keep the expand/collapse label synchronized.
            details.addEventListener("toggle", function() {
                summary.textContent = details.open
                    ? "Response Body (JSON) — Click to collapse"
                    : "Response Body (JSON) — Click to expand";
            });

            return {
                ok: response.ok,
                status: response.status,
                response: responseText
            };

        } catch (error) {
            output.replaceChildren();

            const errorStatus = document.createElement("p");
            errorStatus.className = "response-status error";
            errorStatus.textContent = "Request failed";

            const errorDetails = document.createElement("pre");
            errorDetails.className = "response-json";
            errorDetails.textContent = "Request error: " + error.message;

            output.appendChild(errorStatus);
            output.appendChild(errorDetails);

            return {
                ok: false,
                error: error.message
            };

        } finally {
            submitButton.disabled = false;
        }
    }

    // 1. Request Refund
    document.getElementById("refundForm")
        .addEventListener("submit", async function(event) {
            event.preventDefault();

            const data = {
                txnRef: document.getElementById("txnRef").value.trim(),
                merchantId: Number(document.getElementById("merchantId").value),
                amount: Number(document.getElementById("amount").value),
                reason: document.getElementById("reason").value.trim()
            };

            if (!data.txnRef ||
                !Number.isSafeInteger(data.merchantId) ||
                data.merchantId <= 0 ||
                !Number.isFinite(data.amount) ||
                data.amount <= 0 ||
                !data.reason) {
                alert("Enter valid refund details.");
                return;
            }

            await callApi(API.refunds, "POST", data, "refundResponseMessage");
        });

    // 2. Approve Refund
    document.getElementById("approveRefundForm")
        .addEventListener("submit", async function(event) {
            event.preventDefault();

            const refundRef = document.getElementById("approveRefundRef")
                                       .value.trim();

            if (!refundRef) {
                alert("Enter a refund reference.");
                return;
            }

            await callApi(
                API.approveRefund(refundRef),
                "PUT",
                null,
                "approveRefundResponse"
            );
        });

    // 3. Open Chargeback Case
    document.getElementById("chargebackForm")
        .addEventListener("submit", async function(event) {
            event.preventDefault();

            const data = {
                txnRef: document.getElementById("chargebackTxnRef").value.trim(),
                customerId: Number(document.getElementById("customerId").value),
                merchantId: Number(
                    document.getElementById("chargebackMerchantId").value
                ),
                reasonCode: document.getElementById("reasonCode").value,
                amount: Number(
                    document.getElementById("chargebackAmount").value
                )
            };

            if (!data.txnRef ||
                !Number.isSafeInteger(data.customerId) ||
                data.customerId <= 0 ||
                !Number.isSafeInteger(data.merchantId) ||
                data.merchantId <= 0 ||
                !data.reasonCode ||
                !Number.isFinite(data.amount) ||
                data.amount <= 0) {
                alert("Enter valid chargeback details.");
                return;
            }

            await callApi(
                API.chargebacks,
                "POST",
                data,
                "chargebackResponse"
            );
        });

    // 4. Update Chargeback Status
    document.getElementById("updateChargebackForm")
        .addEventListener("submit", async function(event) {
            event.preventDefault();

            const caseRef = document.getElementById("caseRef").value.trim();

            const data = {
                status: document.getElementById("chargebackStatus").value,
                remarks: document.getElementById("chargebackRemarks")
                                   .value.trim()
            };

            if (!caseRef || !data.status || !data.remarks) {
                alert("Enter the case reference, status, and remarks.");
                return;
            }

            if (data.status === "LOST" &&
                !confirm(
                    "You selected LOST. The backend may reverse funds "
                    + "through the Payment Gateway. Continue?"
                )) {
                return;
            }

            await callApi(
                API.updateChargeback(caseRef),
                "PUT",
                data,
                "updateChargebackResponse"
            );
        });

    // 5. Merchant Risk Ratio Report
    document.getElementById("riskReportForm")
        .addEventListener("submit", async function(event) {
            event.preventDefault();

            const merchantId = Number(
                document.getElementById("reportMerchantId").value
            );

            if (!Number.isSafeInteger(merchantId) || merchantId <= 0) {
                alert("Enter a valid merchant ID.");
                return;
            }

            await callApi(
                API.merchantRiskReport(merchantId),
                "GET",
                null,
                "riskReportResponse"
            );
        });
</script>

</body>
</html>
