package io.patriot_framework.network_simulator.example.image.docker.builder.parts;

import java.util.List;

/**
 * The type Entry docker file part.
 */
public class DockerEntryCmd implements DockerFilePart{
    /**
     * Prepare specific ENTRYPOINT request for Dockerfile translation.
     */
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    /**
     * Prepare specific ENTRYPOINT in exec form. Use in combination with CMD.
     * @param commandWithArgs
     */
    public void setRequest(List<String> commandWithArgs) {
        DockerExecFormTranslate dockerExecFormTranslate = new DockerExecFormTranslate();
        setRequest(dockerExecFormTranslate.translateToExecForm(commandWithArgs));
    }

    @Override
    public String translate() {
        return "ENTRYPOINT " + plainLine;
    }
}
