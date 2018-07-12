package com.redhat.patriot.network_simulator.example.files;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

/**
 * Class is providing utils for work with files and dirs.
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Method converting input stream to file, returning file`s absolute path. Used especially for Dockerfile stream from
     * target jar package to tmp dirs...
     *
     * @param inputStream stream of file content
     * @param name        name of created file
     * @return absolute path to created file
     */
    public String convertToFile(InputStream inputStream, String name) {

        File targetFile = new File(name);
        try {
            java.nio.file.Files.copy(
                    inputStream,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(inputStream);
        return targetFile.getAbsolutePath();
    }

    /**
     * Method provides deleting dir with all content inside.
     *
     * @param dir target directory
     */
    public void deleteDirWithFiles(File dir) {
        String[]entries = dir.list();
        for(String s: entries){
            File currentFile = new File(dir.getPath(),s);
            currentFile.delete();
        }
        dir.delete();
    }
    }

