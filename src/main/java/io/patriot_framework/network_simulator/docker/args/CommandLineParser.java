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

package io.patriot_framework.network_simulator.docker.args;


import org.kohsuke.args4j.Option;

/**
 * Parser using arg4j
 */
public class CommandLineParser {
    @Option(name = "-c", usage = "Clean docker.")
    private boolean clean = false;

    /**
     * Is clean boolean.
     *
     * @return the boolean
     */
    public boolean isClean() {
        return clean;
    }
}
