package com.hofftech.deliverysystem.service;

import org.springframework.stereotype.Service;

@Service
public class LoadCommandService {

    private static final String USER_SHORT_COMMAND = " -u \"";

    public String buildLoadCommand(
            String user,
            String trucks,
            String type,
            String out,
            String parcels,
            String output) {

        StringBuilder commandBuilder = new StringBuilder("/load");

        appendUser(commandBuilder, user);
        appendParcels(commandBuilder, parcels);
        appendTrucks(commandBuilder, trucks);
        appendType(commandBuilder, type);
        appendOutputFormat(commandBuilder, out);
        appendOutputFilename(commandBuilder, output);

        return commandBuilder.toString();
    }

    private void appendUser(StringBuilder commandBuilder, String user) {
        commandBuilder.append(USER_SHORT_COMMAND).append(user).append("\"");
    }

    private void appendParcels(StringBuilder commandBuilder, String parcels) {
        if (parcels != null) {
            commandBuilder.append(" -parcels \"").append(parcels.replace("\n", "\\n")).append("\"");
        }
    }

    private void appendTrucks(StringBuilder commandBuilder, String trucks) {
        if (trucks != null) {
            commandBuilder.append(" -trucks \"").append(trucks.replace("\n", "\\n")).append("\"");
        }
    }

    private void appendType(StringBuilder commandBuilder, String type) {
        commandBuilder.append(" -type \"").append(type).append("\"");
    }

    private void appendOutputFormat(StringBuilder commandBuilder, String out) {
        commandBuilder.append(" -out ").append(out);
    }

    private void appendOutputFilename(StringBuilder commandBuilder, String output) {
        if (output != null) {
            commandBuilder.append(" -out-filename \"").append(output).append("\"");
        }
    }
}
