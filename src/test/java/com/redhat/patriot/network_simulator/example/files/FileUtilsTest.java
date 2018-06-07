package com.redhat.patriot.network_simulator.example.files;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type File utils test.
 */
class FileUtilsTest {

    /**
     * Convert to file test.
     */
    @Test
    void convertToFile() {

        try {
            Path tmpDir = Files.createTempDirectory(Paths.get(""),"tmpTestDir");
            FileUtils fileUtils = new FileUtils();
            Path testFile = Files.createTempFile(tmpDir, "testFile", ".tmp");
            writeToFile(testFile.toString());
            File testedFile = new File(fileUtils.convertToFile(new FileInputStream(testFile.toString()),tmpDir.toString() + "/testedFile"));

            assertEquals(true, org.apache.commons.io.FileUtils.contentEquals(testedFile,testFile.toFile()));
            fileUtils.deleteDirWithFiles(tmpDir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete dir with files test.
     */
    @Test
    void deleteDirWithFiles() {
        try {
            Path tempDir = Files.createTempDirectory(Paths.get(""), "tmpTestDir");
            Path testFile = Files.createTempFile(tempDir ,"testFile", ".tmp");
            FileUtils fileUtils = new FileUtils();
            fileUtils.deleteDirWithFiles(tempDir.toFile());
            assertEquals(false, tempDir.toFile().exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write to file.
     *
     * @param path the path
     */
    void writeToFile(String path) {
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            for (int i = 0; i < 50; i++) {
                writer.println(i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}