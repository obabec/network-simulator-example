package com.redhat.patriot.network_simulator.example.image.builder;

import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DockerFileBuilderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerFileBuilderTest.class);

    @Test
    public void testFileBuild() {
        DockerFileBuilder dockerFileBuilder = new DockerFileBuilder();
        LOGGER.info(dockerFileBuilder.getFileContent().toString());
    }
}
