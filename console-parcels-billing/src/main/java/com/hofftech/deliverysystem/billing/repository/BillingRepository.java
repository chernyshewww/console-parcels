package com.hofftech.deliverysystem.billing.repository;

import com.hofftech.deliverysystem.billing.model.entity.BillingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<BillingEntity, Long> {

    @Query("SELECT b from BillingEntity b where b.user= :user and b.timestamp between :from and :to")
    List<BillingEntity> findSummaryByUserAndPeriod(String user, LocalDateTime from, LocalDateTime to);
}
