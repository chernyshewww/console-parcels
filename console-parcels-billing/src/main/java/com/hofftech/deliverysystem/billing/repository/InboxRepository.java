package com.hofftech.deliverysystem.billing.repository;

import com.hofftech.deliverysystem.billing.model.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {
}

