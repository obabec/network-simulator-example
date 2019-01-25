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
 * The type Entry docker file part.
 */
public class DockerEntryCmd implements DockerFilePart{
    /**
     * Prepare specific ENTRYPOINT request for Dockerfile translation.
     */
    private String plainLine;
    @Override
    public void setRequest(String plainLine) {
        this.plainLine = plainLine;
    }

    /**
     * Prepare specific ENTRYPOINT in exec form. Use in combination with CMD.
     * @param commandWithArgs
     */
    public void setRequest(List<String> commandWithArgs) {
        DockerExecFormTranslate dockerExecFormTranslate = new DockerExecFormTranslate();
        setRequest(dockerExecFormTranslate.translateToExecForm(commandWithArgs));
    }

    @Override
    public String translate() {
        return "ENTRYPOINT " + plainLine;
    }
}
