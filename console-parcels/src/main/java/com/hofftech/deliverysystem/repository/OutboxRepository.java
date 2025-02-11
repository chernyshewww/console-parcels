package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.domain.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
}


