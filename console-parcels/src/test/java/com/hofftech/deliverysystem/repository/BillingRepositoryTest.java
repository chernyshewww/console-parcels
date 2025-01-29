package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.entity.BillingRecordEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BillingRepositoryTest {

    @Autowired
    private BillingRepository billingRepository;

    @BeforeEach
    @Sql(scripts = "/test-data.sql")
    void setUp() {
    }

    @Test
    void shouldFindSummaryByUserAndPeriod() {
        String user = "testUser";
        LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 31, 23, 59);

        List<BillingRecordEntity> records = billingRepository.findSummaryByUserAndPeriod(user, from, to);

        assertThat(records).isNotNull();
        assertThat(records).hasSize(2);
        assertThat(records)
                .extracting("user")
                .containsOnly(user);
        assertThat(records)
                .allMatch(record -> record.getTimestamp().isAfter(from.minusSeconds(1)))
                .allMatch(record -> record.getTimestamp().isBefore(to.plusSeconds(1)));
    }

    @Test
    void shouldReturnEmptyWhenNoRecordsFound() {
        String user = "nonExistingUser";
        LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 31, 23, 59);

        List<BillingRecordEntity> records = billingRepository.findSummaryByUserAndPeriod(user, from, to);

        assertThat(records).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenNoRecordsInDateRange() {
        String user = "testUser";
        LocalDateTime from = LocalDateTime.of(2030, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2030, 1, 31, 23, 59);

        List<BillingRecordEntity> records = billingRepository.findSummaryByUserAndPeriod(user, from, to);

        assertThat(records).isEmpty();
    }
}
