package com.redhat.patriot.network_simulator.example.image.docker.builder.parts;


import java.util.List;

/**
 * The type Run docker file part.
 */
public class DockerRunCmd implements DockerFilePart {
    /**
     * Prepare specific CMD request for Dockerfile translation.
     */
    private String plainLine;
    /**
     * The Commands.
     */
    private List<String> commands;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    /**
     * Prepare specific RUN request from command/s for Dockerfile translation.
     *
     * @param commands the commands
     */
    public void setRequest(List<String> commands) {
        this.commands = commands;
    }


    @Override
    public String translate() {
        String finalLine = "RUN ";
        if (commands != null && !commands.isEmpty()) {
            for (int i = 0; i < commands.size(); i++){
                if (commands.size() - 1 == i) {
                    finalLine = finalLine + commands.get(i);
                } else {
                    finalLine = finalLine + commands.get(i) + " && \\" + System.getProperty("line.separator");
                }
            }
            return finalLine;
        } else {
            return finalLine + plainLine;
        }

    }
}
