package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.TruckRepository;
import com.hofftech.deliverysystem.service.BillingService;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UnloadCommandDispatcherTest {

    @Mock
    private ParcelService parcelService;

    @Mock
    private TruckRepository truckRepository;

    @Mock
    private CommandParserService commandParserService;

    @Mock
    private OutputService outputService;

    @Mock
    private TruckService truckService;

    @Mock
    private FileService fileService;

    @Mock
    private BillingService billingService;

    @InjectMocks
    private UnloadCommandHandlerImpl unloadCommandHandler;

    @Test
    @DisplayName("Должен вернуть ошибку, если формат команды неверный")
    void handle_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/unload";

        when(commandParserService.parseUnloadCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = unloadCommandHandler.handle(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если произошла ошибка при выгрузке посылок")
    void handle_ShouldReturnError_WhenErrorDuringUnload() {
        String inputText = "/unload  -outfile \"parcels-with-count.csv\" --withcount";
        UnloadCommand commandData = mock(UnloadCommand.class);
        List<Long> truckIds = new ArrayList<>();

        when(commandParserService.parseUnloadCommand(inputText)).thenReturn(commandData);
        when(parcelService.findParcelsByTruckIds(truckIds)).thenThrow(new RuntimeException("Ошибка при выгрузке"));

        String result = unloadCommandHandler.handle(inputText);

        assertThat(result).isEqualTo("Произошла ошибка при выгрузке посылок: Ошибка при выгрузке");
    }
}
