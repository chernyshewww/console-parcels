package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.impl.TruckRepositoryImpl;
import com.hofftech.deliverysystem.service.BillingService;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
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
class UnloadCommandHandlerTest {

    @Mock
    private ParcelService parcelService;

    @Mock
    private TruckRepositoryImpl truckRepository;

    @Mock
    private CommandParserService commandParserService;

    @Mock
    private OutputService outputService;

    @Mock
    private FileService fileService;

    @Mock
    private BillingService billingService;

    @InjectMocks
    private UnloadCommandHandlerImpl unloadCommandHandler;

    @Test
    @DisplayName("Должен вернуть ошибку, если формат команды неверный")
    void execute_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/unload";

        when(commandParserService.parseUnloadCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = unloadCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если файл с грузовиками не найден")
    void execute_ShouldReturnError_WhenTrucksFileNotFound() {
        String inputText = "/unload -infile \"trucks.json\" -outfile \"parcels-with-count.csv\" --withcount";
        UnloadCommand commandData = mock(UnloadCommand.class);

        when(commandParserService.parseUnloadCommand(inputText)).thenReturn(commandData);
        when(truckRepository.loadFromFile(commandData.inputFileName())).thenThrow(new RuntimeException("Файл не найден"));

        String result = unloadCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Произошла ошибка при выгрузке посылок: Файл не найден");
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если произошла ошибка при выгрузке посылок")
    void execute_ShouldReturnError_WhenErrorDuringUnload() {
        String inputText = "/unload -infile \"trucks.json\" -outfile \"parcels-with-count.csv\" --withcount";
        UnloadCommand commandData = mock(UnloadCommand.class);
        List<Truck> trucks = new ArrayList<>();

        when(commandParserService.parseUnloadCommand(inputText)).thenReturn(commandData);
        when(truckRepository.loadFromFile(commandData.inputFileName())).thenReturn(trucks);
        when(parcelService.unloadParcelsFromTrucks(trucks)).thenThrow(new RuntimeException("Ошибка при выгрузке"));

        String result = unloadCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Произошла ошибка при выгрузке посылок: Ошибка при выгрузке");
    }
}
