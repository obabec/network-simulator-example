package io.patriot_framework.network_simulator.example.image.docker.builder.parts;

/**
 * The interface Docker file part.
 */
public interface DockerFilePart {
    /**
     * Prepare request for Dockerfile translation.
     *
     * @param plainLine the plain line
     */
    void setRequest(String plainLine);

    /**
     * Translate request for specific Dockerfile command line.
     *
     * @return the string
     */
    String translate();
}
