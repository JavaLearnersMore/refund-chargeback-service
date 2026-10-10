
package com.example.refund.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.refund.dto.ChargebackRequest;
import com.example.refund.dto.ChargebackResponse;
import com.example.refund.dto.ChargebackStatusRequest;
import com.example.refund.entity.CaseEvent;
import com.example.refund.entity.ChargebackCase;
import com.example.refund.repository.CaseEventRepository;
import com.example.refund.repository.ChargebackCaseRepository;

@Service
public class ChargebackService {

    private final ChargebackCaseRepository repository;
    private final CaseEventRepository caseEventRepository;

    private static final Map<String, Set<String>> ALLOWED_TRANSITIONS;

    static {
        Map<String, Set<String>> transitions = new HashMap<>();

        transitions.put("OPENED",
                new HashSet<>(Collections.singletonList("EVIDENCE_COLLECTION")));

        transitions.put("EVIDENCE_COLLECTION",
                new HashSet<>(Collections.singletonList("REPRESENTED")));

        transitions.put("REPRESENTED",
                new HashSet<>(java.util.Arrays.asList("WON", "LOST")));

        transitions.put("WON",
                new HashSet<>(Collections.singletonList("CLOSED")));

        transitions.put("LOST",
                new HashSet<>(Collections.singletonList("CLOSED")));

        transitions.put("CLOSED", Collections.emptySet());

        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(transitions);
    }

    public ChargebackService(
            ChargebackCaseRepository repository,
            CaseEventRepository caseEventRepository) {

        this.repository = repository;
        this.caseEventRepository = caseEventRepository;
    }

    @Transactional
    public ChargebackResponse openCase(ChargebackRequest request) {

        ChargebackCase chargeback = new ChargebackCase();

        chargeback.setCaseRef(generateCaseRef());
        chargeback.setTxnRef(request.getTxnRef());
        chargeback.setCustomerId(request.getCustomerId());
        chargeback.setMerchantId(request.getMerchantId());
        chargeback.setReasonCode(request.getReasonCode());
        chargeback.setAmount(request.getAmount());
        chargeback.setStatus("OPENED");

        // Chargeback response deadline: 14 days after opening.
        chargeback.setDueDate(LocalDate.now().plusDays(14));

        ChargebackCase saved = repository.save(chargeback);

        return buildResponse(saved);
    }

    @Transactional
    public ChargebackResponse updateStatus(
            String caseRef,
            ChargebackStatusRequest request) {

        // Find the chargeback case.
        ChargebackCase chargeback = repository.findByCaseRef(caseRef)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Chargeback case not found: " + caseRef));

        String currentStatus = chargeback.getStatus();

        String newStatus = request.getStatus()
                .trim()
                .toUpperCase(Locale.ROOT);

        // Validate the requested status.
        if (!ALLOWED_TRANSITIONS.containsKey(newStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid chargeback status: " + newStatus);
        }

        // Validate the status transition.
        Set<String> allowedStatuses =
                ALLOWED_TRANSITIONS.getOrDefault(
                        currentStatus,
                        Collections.emptySet());

        if (!allowedStatuses.contains(newStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot transition chargeback " + caseRef
                            + " from " + currentStatus
                            + " to " + newStatus
                            + ". Allowed next states: " + allowedStatuses);
        }

        LocalDateTime now = LocalDateTime.now();
        String remarks = request.getRemarks().trim();

        // Update the chargeback record.
        chargeback.setStatus(newStatus);
        chargeback.setRemarks(remarks);
        chargeback.setUpdatedAt(now);

        ChargebackCase updated = repository.save(chargeback);

        // Identify the authenticated operator.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String actor = authentication != null
                ? authentication.getName()
                : "UNKNOWN";

        // Record the status change in the audit table.
        CaseEvent event = new CaseEvent();
        event.setCaseType("CHARGEBACK");
        event.setCaseId(updated.getId());
        event.setFromStatus(currentStatus);
        event.setToStatus(newStatus);
        event.setActor(actor);
        event.setRemarks(remarks);
        event.setTimestamp(now);

        caseEventRepository.save(event);

        return buildResponse(updated);
    }

    private String generateCaseRef() {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String random = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase(Locale.ROOT);

        return "CBCASE" + timestamp + random;
    }

    private ChargebackResponse buildResponse(ChargebackCase chargeback) {

        ChargebackResponse response = new ChargebackResponse();

        response.setCaseRef(chargeback.getCaseRef());
        response.setTxnRef(chargeback.getTxnRef());
        response.setCustomerId(chargeback.getCustomerId());
        response.setMerchantId(chargeback.getMerchantId());
        response.setReasonCode(chargeback.getReasonCode());
        response.setStatus(chargeback.getStatus());
        response.setAmount(chargeback.getAmount());
        response.setDueDate(chargeback.getDueDate());
        response.setCreatedAt(chargeback.getCreatedAt());
        response.setUpdatedAt(chargeback.getUpdatedAt());

        return response;
    }
}
