package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;

/**
 * The type Add docker file part.
 */
public class DockerAddCmd implements DockerFilePart{
    /**
     * String line representing part of command line in Dockerfile.
     */
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    /**
     * Prepare specific ADD request line for translation and complete plainLine.
     *
     * @param newFile  the new file
     * @param filePath the file path
     */
    public void setRequest(String newFile, String filePath){
        setRequest(newFile + " " + filePath);
    }
    @Override
    public String translate() {
        return "ADD " + plainLine;
    }
}
