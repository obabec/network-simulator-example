package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;

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

    @Override
    public String translate() {
        return "ENTRYPOINT " + plainLine;
    }
}
