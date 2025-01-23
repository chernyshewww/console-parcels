package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.record.command.BillingCommand;
import com.hofftech.deliverysystem.service.BillingService;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BillingCommandHandlerImpl implements CommandHandler {

    private final BillingService billingService;
    private final CommandParserService commandParserService;
    private final OutputService outputService;

    @Override
    public String handle(String text) {
        try {
            BillingCommand commandData = commandParserService.parseBillingCommand(text);

            var billingDetails = billingService.getBillingSummaries(
                    commandData.user(),
                    commandData.fromDate(),
                    commandData.toDate()
            );

            return outputService.formatBillingResponse(billingDetails);
        } catch (InvalidCommandException e) {
            log.error("Invalid billing command", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while processing billing command", e);
            return "Произошла ошибка: " + e.getMessage();
        }
    }
}
