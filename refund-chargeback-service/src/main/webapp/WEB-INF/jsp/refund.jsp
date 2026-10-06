<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html>

<head>

    <title>Refund</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/refund.css">

</head>

<body>

<div class="refund-forms-container">

    <div class="refund-section">

        <div class="refund-form-container">

            <h2>Request Refund</h2>

            <form id="refundForm">

                <label>Transaction Reference</label>
                <input type="text"
                       id="txnRef"
                       placeholder="PGTXN20260823101500ABCDEF"
                       required>

                <label>Merchant ID</label>
                <input type="number"
                       id="merchantId"
                       placeholder="1001"
                       required>

                <label>Amount</label>
                <input type="number"
                       id="amount"
                       placeholder="300.00"
                       step="0.01"
                       min="0.01"
                       required>

                <label>Reason</label>
                <input type="text"
                       id="reason"
                       placeholder="Customer requested refund"
                       required>

                <button type="submit">
                    Request Refund
                </button>

            </form>

            <div id="refundResponseMessage"
                 class="refund-response-message">
            </div>

        </div>

    </div>

</div>
<script>

document.getElementById("refundForm").addEventListener("submit", function(event) {

    event.preventDefault();

    const data = {

        txnRef: document.getElementById("txnRef").value,

        merchantId: Number(document.getElementById("merchantId").value),

        amount: Number(document.getElementById("amount").value),

        reason: document.getElementById("reason").value

    };


    fetch(
        "${pageContext.request.contextPath}/rc/api/v1/refunds",
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(data)
        }
    )

    .then(async response => {

        const result = await response.json();

        document.getElementById(
            "refundResponseMessage"
        ).innerHTML =
            "<pre>" +
            JSON.stringify(result, null, 2) +
            "</pre>";

    })

    .catch(error => {

        document.getElementById(
            "refundResponseMessage"
        ).innerHTML =
            "<pre>" +
            error.message +
            "</pre>";

    });

});

</script>

</body>

</html>
