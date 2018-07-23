package com.redhat.patriot.network_simulator.example.image.docker.builder;

import com.redhat.patriot.network_simulator.example.image.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DockerFileBuilder implements Builder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerFileBuilder.class);
    private List<String> fileContent = new ArrayList<>();

    public List<String> getFileContent() {
        return fileContent;
    }

    public DockerFileBuilder from(String baseImage) {
        String fromLine = "FROM " + baseImage;
        this.fileContent.add(0, fromLine);
        return this;
    }

    public DockerFileBuilder cmd(List<String> commandWithArgs) {

        String commandLine = "";
        int sizeOf = commandWithArgs.size();
        for (int i = 0; i < sizeOf; i++) {
            if (i != (sizeOf - 1)) {
                commandLine = commandLine + '"' + commandWithArgs.get(i) + '"' + ", ";
            } else {
                commandLine = commandLine + '"' + commandWithArgs.get(i) + '"' + "]";
            }
        }

        commandLine = "CMD [" + commandLine;
        fileContent.add(commandLine);
        return this;
    }
    public DockerFileBuilder run(String command) {
        String runLine = "RUN " + command;
        fileContent.add(runLine);
        return this;
    }
    public DockerFileBuilder run(List<String> commandList) {

        for (int i = 0; i < commandList.size(); i++) {
            if (i == 0) {
                fileContent.add("RUN " + commandList.get(i) + " && \\");
            } else if (i == commandList.size() - 1) {
                 fileContent.add(commandList.get(i));
            } else {
                fileContent.add(commandList.get(i) + " && \\ ");
            }
        }
        return this;
    }

    public DockerFileBuilder env(String nameOfVar, String variable) {
        String varLine = "ENV " + nameOfVar + " " + variable;
        fileContent.add(varLine);
        return this;
    }

    public DockerFileBuilder workdir(String workDir) {
        String workDirLine = "WORKDIR " + workDir;
        fileContent.add(workDirLine);
        return this;
    }

    public DockerFileBuilder add(Path newFile, Path filePath) {
        String addFileLine = "ADD " + newFile + " " + filePath;
        fileContent.add(addFileLine);
        return this;
    }

    public DockerFileBuilder write(Path filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath.toString()))) {
            for (String line : fileContent) {
                pw.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


}
