package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.record.command.BillingCommand;
import com.hofftech.deliverysystem.service.BillingService;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingCommandDispatcherImplTest {

    @Mock
    private BillingService billingService;

    @Mock
    private CommandParserService commandParserService;

    @Mock
    private OutputService outputService;

    @InjectMocks
    private BillingCommandHandlerImpl billingCommandHandler;

    @Test
    @DisplayName("Должен вернуть сообщение об ошибке, если команда неверна")
    void handle_ShouldReturnErrorMessage_WhenCommandIsInvalid() {
        String inputText = "/billing";

        when(commandParserService.parseBillingCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> billingCommandHandler.handle(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть сообщение об ошибке, если произошла непредвиденная ошибка")
    void handle_ShouldReturnErrorMessage_WhenUnexpectedErrorOccurs() {
        String inputText = "/billing -u user@example.com -from 2023-01-01 -to 2023-12-31";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        BillingCommand commandData = new BillingCommand("user@example.com", fromDate, toDate);

        when(commandParserService.parseBillingCommand(inputText)).thenReturn(commandData);
        when(billingService.getBillingSummaries(commandData.user(), commandData.fromDate(), commandData.toDate()))
                .thenThrow(new RuntimeException("Unexpected error"));

        String result = billingCommandHandler.handle(inputText);

        assertThat(result).isEqualTo("Произошла ошибка: Unexpected error");
    }
}
