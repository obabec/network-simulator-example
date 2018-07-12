package com.redhat.patriot.network_simulator.example.args;


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
