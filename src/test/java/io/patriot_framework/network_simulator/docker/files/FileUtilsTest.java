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

package io.patriot_framework.network_simulator.docker.files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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