package com.hofftech.deliverysystem.model.record.command;

import java.time.LocalDate;

public record BillingCommand(String user, LocalDate fromDate, LocalDate toDate) {
}
