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

package io.patriot_framework.network_simulator.docker.image.docker.builder.parts;


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
            for (int i = 0; i < commands.size(); i++) {
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
