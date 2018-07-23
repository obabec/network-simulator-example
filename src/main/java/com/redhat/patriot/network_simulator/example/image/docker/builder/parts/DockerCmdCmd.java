package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;

import java.util.List;

/**
 * The type Cmd docker file part.
 */
public class DockerCmdCmd implements DockerFilePart {
    /**
     * String line representing part of command line in Dockerfile.
     */
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    /**
     * Prepare specific CMD request for Dockerfile translation.
     *
     * @param commandWithArgs the command with args
     */
    public void setRequest(List<String> commandWithArgs) {
        String commandLine = "[";
        int sizeOf = commandWithArgs.size();
        for (int i = 0; i < sizeOf; i++) {
            if (i != (sizeOf - 1)) {
                commandLine = commandLine + '"' + commandWithArgs.get(i) + '"' + ", ";
            } else {
                commandLine = commandLine + '"' + commandWithArgs.get(i) + '"' + "]";
            }
        }
        setRequest(commandLine);
    }

    @Override
    public String translate() {
        return "CMD " + plainLine;
    }
}
