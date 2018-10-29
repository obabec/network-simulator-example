package com.redhat.patriot.network_simulator.example.image.docker.builder;

import com.redhat.patriot.network_simulator.example.image.Builder;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerAddCmd;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerCmdCmd;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerEntryCmd;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerEnvCmd;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerFilePart;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerFromCmd;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerRunCmd;
import com.redhat.patriot.network_simulator.example.image.docker.builder.parts.DockerWorkCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Docker file builder.
 */
public class DockerFileBuilder implements Builder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerFileBuilder.class);
    private List<DockerFilePart> fileContent = new ArrayList<>();

    /**
     * Gets file content.
     *
     * @return the file content
     */
    public List<DockerFilePart> getFileContent() {
        return fileContent;
    }

    /**
     * From docker file builder.
     *
     * @param baseImage the base image
     * @return the docker file builder
     */
    public DockerFileBuilder from(String baseImage) {
        DockerFilePart dockerFilePart = new DockerFromCmd();
        dockerFilePart.setRequest(baseImage);
        this.fileContent.add(0, dockerFilePart);
        return this;
    }

    /**
     * Cmd docker file builder.
     *
     * @param commandWithArgs the command with args
     * @return the docker file builder
     */
    public DockerFileBuilder cmd(List<String> commandWithArgs) {
        DockerFilePart cmdDockerFilePart = new DockerCmdCmd();
        ((DockerCmdCmd) cmdDockerFilePart).setRequest(commandWithArgs);
        fileContent.add(cmdDockerFilePart);
        return this;
    }

    /**
     * Run docker file builder.
     *
     * @param command the command
     * @return the docker file builder
     */
    public DockerFileBuilder run(String command) {
        DockerFilePart runDockerFilePart = new DockerRunCmd();
        runDockerFilePart.setRequest(command);
        fileContent.add(runDockerFilePart);
        return this;
    }

    /**
     * Run docker file builder.
     *
     * @param commandList the command list
     * @return the docker file builder
     */
    public DockerFileBuilder run(List<String> commandList) {
        DockerFilePart dockerFilePart = new DockerRunCmd();
        ((DockerRunCmd) dockerFilePart).setRequest(commandList);
        fileContent.add(dockerFilePart);
        return this;
    }

    /**
     * Env docker file builder.
     *
     * @param nameOfVar the name of var
     * @param variable  the variable
     * @return the docker file builder
     */
    public DockerFileBuilder env(String nameOfVar, String variable) {
        DockerFilePart envDockerFilePart = new DockerEnvCmd();
        ((DockerEnvCmd) envDockerFilePart).setRequest(nameOfVar, variable);
        fileContent.add(envDockerFilePart);
        return this;
    }

    /**
     * Workdir docker file builder.
     *
     * @param workDir the work dir
     * @return the docker file builder
     */
    public DockerFileBuilder workdir(String workDir) {
        DockerFilePart workDockerFilePart = new DockerWorkCmd();
        workDockerFilePart.setRequest(workDir);
        fileContent.add(workDockerFilePart);
        return this;
    }

    /**
     * Add docker file builder.
     *
     * @param newFile  the new file
     * @param filePath the file path
     * @return the docker file builder
     */
    public DockerFileBuilder add(Path newFile, Path filePath) {
        DockerFilePart addDockerFilePart = new DockerAddCmd();
        ((DockerAddCmd) addDockerFilePart).setRequest(newFile.toString(), filePath.toString());
        fileContent.add(addDockerFilePart);
        return this;
    }

    /**
     * Write docker file builder.
     *
     * @param filePath the file path
     * @return the docker file builder
     */
    public DockerFileBuilder write(Path filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath.toString()))) {
            for (DockerFilePart part : fileContent) {
                pw.println(part.translate());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Entrypoint docker file builder.
     *
     * @param entrypoint the entrypoint
     * @return the docker file builder
     */
    public DockerFileBuilder entrypoint(String entrypoint) {
        DockerFilePart entryDockerFilePart = new DockerEntryCmd();
        entryDockerFilePart.setRequest(entrypoint);
        fileContent.add(entryDockerFilePart);
        return this;
    }

}
