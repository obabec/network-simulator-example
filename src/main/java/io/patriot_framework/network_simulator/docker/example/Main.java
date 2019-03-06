/*
 * Copyright 2019 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.network_simulator.docker.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import io.patriot_framework.network_simulator.docker.DockerExample;
import io.patriot_framework.network_simulator.docker.args.CommandLineParser;
import io.patriot_framework.network_simulator.docker.cleanup.Cleaner;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * The type Main.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        DockerExample dockerExample = new DockerExample();
        CommandLineParser cmdArgs = new CommandLineParser();
        CmdLineParser parser = new CmdLineParser(cmdArgs);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            e.printStackTrace();
        }

        if (cmdArgs.isClean()) {
            LOGGER.info("Cleaning docker.");
            DockerClient dockerClient = DockerClientBuilder.
                    getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
            Cleaner cleaner = new Cleaner();
            cleaner.cleanUp(Arrays.asList("client_network", "server_network"),
                    Arrays.asList("comm_client", "comm_server", "router"));
        } else {
            LOGGER.info("Generating enviropment");
            dockerExample.genererateEnviroment();
        }
        dockerExample.genererateEnviroment();
    }


}
