package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;

/**
 * The type Env docker file part.
 */
public class DockerEnvCmd implements DockerFilePart{
    /**
     * String line representing part of command line in Dockerfile.
     */
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    /**
     * Prepare request of system environment variable for Dockerfile translation.
     *
     * @param nameOfVar the name of var
     * @param var       the var
     */
    public void setRequest(String nameOfVar, String var) {
        setRequest(nameOfVar + " " + var);
    }

    @Override
    public String translate() {
        return "ENV " + plainLine;
    }
}
