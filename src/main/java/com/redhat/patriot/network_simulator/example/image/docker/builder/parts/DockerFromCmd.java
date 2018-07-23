package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;

/**
 * The type From docker file part.
 */
public class DockerFromCmd implements DockerFilePart {
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    @Override
    public String translate() {
        return "FROM " + plainLine;
    }
}
