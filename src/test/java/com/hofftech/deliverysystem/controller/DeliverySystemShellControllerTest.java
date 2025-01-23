package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandDispatcher;
import com.hofftech.deliverysystem.service.LoadCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeliverySystemShellControllerTest {

    @Mock
    private CommandDispatcher commandDispatcher;

    @Mock
    private LoadCommandService loadCommandService;

    @InjectMocks
    private DeliverySystemShellController deliverySystemShellController;

    @Test
    @DisplayName("Создание посылки должно вернуть результат выполнения команды")
    void testCreate() {
        String name = "Parcel1";
        String form = "TT\nTT";
        char symbol = 'P';
        String expectedCommand = "/create -name \"Parcel1\" -form \"TT\nTT\" -symbol \"P\"";

        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Command executed");

        String result = deliverySystemShellController.create(name, form, symbol);

        assertThat(result).isEqualTo("Command executed");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }

    @Test
    @DisplayName("Поиск посылки должен вернуть результат выполнения команды")
    void testFind() {
        String name = "Parcel1";
        String expectedCommand = "/find \"Parcel1\"";

        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Parcel found");

        String result = deliverySystemShellController.find(name);

        assertThat(result).isEqualTo("Parcel found");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }

    @Test
    @DisplayName("Редактирование посылки должно вернуть результат выполнения команды")
    void testEdit() {
        String id = "123";
        String name = "UpdatedParcel";
        String form = "UU\nUU";
        char symbol = 'U';
        String expectedCommand = "/edit -id \"123\" -name \"UpdatedParcel\" -form \"UU\nUU\" -symbol \"U\"";

        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Parcel edited");

        String result = deliverySystemShellController.edit(id, name, form, symbol);

        assertThat(result).isEqualTo("Parcel edited");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }

    @Test
    @DisplayName("Удаление посылки должно вернуть результат выполнения команды")
    void testDelete() {
        String name = "Parcel1";
        String expectedCommand = "/delete \"Parcel1\"";

        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Parcel deleted");

        String result = deliverySystemShellController.delete(name);

        assertThat(result).isEqualTo("Parcel deleted");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }

    @Test
    @DisplayName("Загрузка данных должна вернуть результат выполнения команды")
    void testLoad() {
        String user = "testuser@example.com";
        String file = "parcels.csv";
        String trucks = "3x3\n10x10";
        String type = "Равномерное распределение";
        String out = "json-file";
        String parcels = "Куб\nПосылка тип 2";
        String output = "trucks.json";
        String expectedCommand = "/load -u \"testuser@example.com\" -file \"parcels.csv\" -trucks \"3x3\n10x10\" -type \"Равномерное распределение\" -out \"json-file\" -parcels \"Куб\nПосылка тип 2\" -output \"trucks.json\"";

        when(loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output)).thenReturn(expectedCommand);
        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Load executed");

        String result = deliverySystemShellController.load(user, file, trucks, type, out, parcels, output);

        assertThat(result).isEqualTo("Load executed");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }

    @Test
    @DisplayName("Выгрузка данных должна вернуть результат выполнения команды")
    void testUnload() {
        String user = "testuser@example.com";
        String infile = "trucks.json";
        String outfile = "parcels-with-count.csv";
        boolean count = true;
        String expectedCommand = "/unload -u \"testuser@example.com\" -infile \"trucks.json\" -outfile \"parcels-with-count.csv\" --withcount";

        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Unload executed");

        String result = deliverySystemShellController.unload(user, infile, outfile, count);

        assertThat(result).isEqualTo("Unload executed");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }

    @Test
    @DisplayName("Выставление счета должно вернуть результат выполнения команды")
    void testBilling() {
        String user = "testuser@example.com";
        String from = "2023-01-01";
        String to = "2023-12-31";
        String expectedCommand = "/billing -u \"testuser@example.com\" -from \"2023-01-01\" -to \"2023-12-31\"";

        when(commandDispatcher.dispatchCommand(expectedCommand)).thenReturn("Billing details");

        String result = deliverySystemShellController.billing(user, from, to);

        assertThat(result).isEqualTo("Billing details");
        verify(commandDispatcher).dispatchCommand(expectedCommand);
    }
}
