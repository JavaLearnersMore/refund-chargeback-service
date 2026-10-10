package com.example.refund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.refund.entity.CaseEvent;

public interface CaseEventRepository
        extends JpaRepository<CaseEvent, Long> {
}