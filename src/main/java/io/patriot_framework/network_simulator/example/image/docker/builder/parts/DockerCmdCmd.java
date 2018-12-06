package io.patriot_framework.network_simulator.example.image.docker.builder.parts;

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
        DockerExecFormTranslate dockerExecFormTranslate = new DockerExecFormTranslate();
        setRequest(dockerExecFormTranslate.translateToExecForm(commandWithArgs));
    }


    @Override
    public String translate() {
        return "CMD " + plainLine;
    }
}
