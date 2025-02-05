package com.hofftech.deliverysystem.billing.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Table(name = "billing_record")
public class BillingRecordEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"user\"")
    private String user;
    private LocalDateTime timestamp;
    private String operationType;
    private int segments;
    private int parcels;
    private int cost;
}
