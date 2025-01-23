package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.BillingService;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.LoadingStrategy;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadCommandDispatcherTest {

    @Mock
    private ParcelService parcelService;

    @Mock
    private TruckService truckService;

    @Mock
    private StrategyHelper strategyHelper;

    @Mock
    private CommandParserService commandParserService;

    @Mock
    private OutputService outputService;

    @Mock
    private BillingService billingService;

    @InjectMocks
    private LoadCommandHandlerImpl loadCommandHandler;

    @Test
    @DisplayName("Должен вернуть ошибку, если указана неизвестная стратегия")
    void handle_ShouldReturnError_WhenUnknownStrategy() {
        String inputText = "/load -parcels-file \"parcels.csv\" -trucks \"3x3\\n10x10\" -type \"Равномерное распределение\" -out text";
        LoadCommand commandData = mock(LoadCommand.class);

        when(commandParserService.parseLoadCommand(inputText)).thenReturn(commandData);
        when(strategyHelper.determineStrategy(commandData.strategyType())).thenReturn(null);

        String result = loadCommandHandler.handle(inputText);

        assertThat(result).isEqualTo("Ошибка! Указан неизвестный тип стратегии.");
    }

    @Test
    @DisplayName("Должен выбросить исключение InvalidCommandException, если формат команды неверный")
    void handle_ShouldThrowInvalidCommandException_WhenInvalidCommandFormat() {
        String inputText = "/load";

        when(commandParserService.parseLoadCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> loadCommandHandler.handle(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если загрузка не удалась")
    void handle_ShouldReturnError_WhenLoadingFails() {
        String inputText = "/load -parcels-file \"parcels.csv\" -trucks \"3x3\\n10x10\" -type \"Равномерное распределение\" -out text";
        LoadCommand commandData = mock(LoadCommand.class);
        List<Parcel> parcels = new ArrayList<>();
        List<Truck> trucks = new ArrayList<>();
        LoadingStrategy strategy = mock(LoadingStrategy.class);

        when(commandParserService.parseLoadCommand(inputText)).thenReturn(commandData);
        when(parcelService.loadParcels(commandData)).thenReturn(parcels);
        when(truckService.parseTruckSizes(commandData.trucksText())).thenReturn(trucks);
        when(strategyHelper.determineStrategy(commandData.strategyType())).thenReturn(strategy);
        when(strategy.loadParcels(parcels, trucks)).thenThrow(new IllegalStateException("Ошибка при размещении посылок"));

        String result = loadCommandHandler.handle(inputText);

        assertThat(result).isEqualTo("Ошибка! Не удалось разместить все посылки в грузовиках. Проверьте размеры и количество.");
    }
}
