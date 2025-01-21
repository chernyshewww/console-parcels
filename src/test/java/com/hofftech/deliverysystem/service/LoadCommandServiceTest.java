package com.hofftech.deliverysystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class LoadCommandServiceTest {

    private LoadCommandService loadCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loadCommandService = new LoadCommandService();
    }

    @Test
    void buildLoadCommand_ShouldBuildCommandWithAllParameters() {
        String user = "TestUser";
        String file = "parcels.csv";
        String trucks = "3x3\\n6x2";
        String type = "Одна машина - Одна посылка";
        String out = "json-file";
        String parcels = "Посылка Тип 1\\nКУБ";
        String output = "output.json";

        String result = loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output);

        assertThat(result).isEqualTo(
                "/load -u \"TestUser\" -parcels-text \"Посылка Тип 1\\nКУБ\" -parcels-file \"parcels.csv\" -trucks \"3x3\\n6x2\" -type \"Одна машина - Одна посылка\" -out json-file -out-filename \"output.json\""
        );
    }

    @Test
    void buildLoadCommand_ShouldBuildCommandWithoutParcelsText() {
        String user = "TestUser";
        String file = "parcels.csv";
        String trucks = "3x3\\n6x2";
        String type = "Одна машина - Одна посылка";
        String out = "json-file";
        String parcels = null;
        String output = "output.json";

        String result = loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output);

        assertThat(result).isEqualTo(
                "/load -u \"TestUser\" -parcels-file \"parcels.csv\" -trucks \"3x3\\n6x2\" -type \"Одна машина - Одна посылка\" -out json-file -out-filename \"output.json\""
        );
    }

    @Test
    void buildLoadCommand_ShouldBuildCommandWithoutParcelsFile() {
        String user = "TestUser";
        String file = null;
        String trucks = "3x3\\n6x2";
        String type = "Одна машина - Одна посылка";
        String out = "text";
        String parcels = "Посылка Тип 1\\nКУБ";
        String output = null;

        String result = loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output);

        assertThat(result).isEqualTo(
                "/load -u \"TestUser\" -parcels-text \"Посылка Тип 1\\nКУБ\" -trucks \"3x3\\n6x2\" -type \"Одна машина - Одна посылка\" -out text"
        );
    }

    @Test
    void buildLoadCommand_ShouldBuildCommandWithoutOutputFilename() {
        String user = "TestUser";
        String file = "parcels.csv";
        String trucks = "3x3\\n6x2";
        String type = "Одна машина - Одна посылка";
        String out = "text";
        String parcels = "Посылка Тип 1\\nКУБ";
        String output = null;

        String result = loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output);

        assertThat(result).isEqualTo(
                "/load -u \"TestUser\" -parcels-text \"Посылка Тип 1\\nКУБ\" -parcels-file \"parcels.csv\" -trucks \"3x3\\n6x2\" -type \"Одна машина - Одна посылка\" -out text"
        );
    }

    @Test
    void buildLoadCommand_ShouldBuildMinimalCommand() {
        String user = "MinimalUser";
        String file = null;
        String trucks = null;
        String type = "Одна машина - Одна посылка";
        String out = "text";
        String parcels = null;
        String output = null;

        String result = loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output);

        assertThat(result).isEqualTo(
                "/load -u \"MinimalUser\" -type \"Одна машина - Одна посылка\" -out text"
        );
    }
}
