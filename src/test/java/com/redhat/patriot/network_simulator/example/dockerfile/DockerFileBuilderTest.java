package com.redhat.patriot.network_simulator.example.dockerfile;

import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DockerFileBuilderTest {
    @Test
    public void dockerFileTest() {
        DockerFileBuilder dockerFileBuilder = new DockerFileBuilder();
        List<String> content = new ArrayList<>();
        List<String> expectedOrder = Arrays.asList("FROM", "RUN", "CMD", "WORKDIR", "ADD");
        List<String> actualFile = new ArrayList<>();
        String basePacakge = "ubuntu:18.04";
        String run = "apt-get update";
        String cmd = "/sbin/my_init";
        String workdir = "/";
        Path targetFile = Paths.get("testFile.txt");
        Path newFile = Paths.get("/tmp/testFile.txt");
        content = Arrays.asList(basePacakge, run, cmd, workdir, newFile.toString()+ " " + targetFile.toString());
        try {
            Path tmpDir = Files.createTempDirectory(Paths.get("/tmp"),"tmpDockerDir");
            Path dockerFile = Files.createTempFile(tmpDir, "Dockerfile", "");
            dockerFileBuilder.from(basePacakge).run(run).cmd(Arrays.asList(cmd))
                    .workdir(workdir).add(newFile, targetFile).write(newFile);

            try(BufferedReader bf = new BufferedReader(new FileReader(newFile.toString()))) {
                String line = bf.readLine();
                while (line != null) {
                    actualFile.add(line);
                    line = bf.readLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < actualFile.size(); i++) {
            assertTrue(actualFile.get(i).contains(expectedOrder.get(i)) &&
                    actualFile.get(i).contains(content.get(i)));
        }

    }
}
