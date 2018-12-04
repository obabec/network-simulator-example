package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;

import java.util.List;

public class DockerExecFormTranslate {
    public String translateToExecForm(List<String> commandWithArgs) {
        String commandLine = "[";
        int sizeOf = commandWithArgs.size();
        for (int i = 0; i < sizeOf; i++) {
            if (i != (sizeOf - 1)) {
                commandLine = commandLine + '"' + commandWithArgs.get(i) + '"' + ", ";
            } else {
                commandLine = commandLine + '"' + commandWithArgs.get(i) + '"' + "]";
            }
        }
        return commandLine;
    }
}
