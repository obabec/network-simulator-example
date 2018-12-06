package io.patriot_framework.network_simulator.example.image.docker.builder.parts;

/**
 * The type Work docker file part.
 */
public class DockerWorkCmd implements DockerFilePart{
    /**
     * Prepare specific CMD request for Dockerfile translation.
     */
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    @Override
    public String translate() {
        return "WORKDIR " + plainLine;
    }
}
