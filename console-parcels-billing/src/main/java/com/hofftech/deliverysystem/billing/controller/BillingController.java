package com.hofftech.deliverysystem.billing.controller;

import com.hofftech.deliverysystem.billing.model.enums.Period;
import com.hofftech.deliverysystem.billing.model.record.BillingSummary;
import com.hofftech.deliverysystem.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
@Tag(name = "Delivery System Billing commands", description = "APIs for managing delivery system billing.")
public class BillingController {

    private final BillingService billingService;
    @GetMapping("/billing")
    @Operation(
            summary = "Get billing information",
            description = "This endpoint retrieves billing information for a user within the specified period.",
            tags = {"Billing"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Billing information retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid date format or input parameters")
    })
    public String billing(@RequestParam @Parameter(description = "The user for whom to retrieve billing information") String user,
                          @RequestParam(required = false) @Parameter(description = "Start date for the billing period") String from,
                          @RequestParam(required = false) @Parameter(description = "End date for the billing period") String to,
                          @Parameter(description = "Период") @RequestParam(defaultValue = "NONE", required = false) Period period) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate fromDate = LocalDate.parse(from, formatter);
            LocalDate toDate = LocalDate.parse(to, formatter);

            List<BillingSummary> billingSummaries = new ArrayList<>();
            if (period == Period.LAST_MONTH){
                billingSummaries = billingService.findBillingSummariesByLastMonth(user, fromDate, toDate);
            }
            else {
                billingSummaries = billingService.findBillings(user, fromDate, toDate);
            }

            return billingSummaries.stream()
                    .map(BillingSummary::toString)
                    .collect(Collectors.joining("\n"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}
