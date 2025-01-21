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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class UnloadCommandHandlerTest {

    @Mock
    private ParcelService parcelService;

    @Mock
    private TruckRepository truckRepository;

    @Mock
    private CommandParserService commandParserService;

    @Mock
    private OutputService outputService;

    @Mock
    private FileService fileService;

    @Mock
    private BillingService billingService;

    private UnloadCommandHandlerImpl unloadCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unloadCommandHandler = new UnloadCommandHandlerImpl(parcelService, truckRepository, commandParserService, outputService, fileService, billingService);
    }

    @Test
    void execute_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/unload";

        when(commandParserService.parseUnloadCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = unloadCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }

    @Test
    void execute_ShouldReturnError_WhenTrucksFileNotFound() {
        String inputText = "/unload -infile \"trucks.json\" -outfile \"parcels-with-count.csv\" --withcount";
        UnloadCommand commandData = mock(UnloadCommand.class);

        when(commandParserService.parseUnloadCommand(inputText)).thenReturn(commandData);
        when(truckRepository.loadTrucksFromFile(commandData.inputFileName())).thenThrow(new RuntimeException("Файл не найден"));

        String result = unloadCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Произошла ошибка при выгрузке посылок: Файл не найден");
    }

    @Test
    void execute_ShouldReturnError_WhenErrorDuringUnload() {
        String inputText = "/unload -infile \"trucks.json\" -outfile \"parcels-with-count.csv\" --withcount";
        UnloadCommand commandData = mock(UnloadCommand.class);
        List<Truck> trucks = new ArrayList<>();

        when(commandParserService.parseUnloadCommand(inputText)).thenReturn(commandData);
        when(truckRepository.loadTrucksFromFile(commandData.inputFileName())).thenReturn(trucks);
        when(parcelService.unloadParcelsFromTrucks(trucks)).thenThrow(new RuntimeException("Ошибка при выгрузке"));

        String result = unloadCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Произошла ошибка при выгрузке посылок: Ошибка при выгрузке");
    }
}
