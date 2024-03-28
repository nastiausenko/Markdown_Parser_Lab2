package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileManagerTest {
    private FileManager fileManager;
    private Path inputPath;
    private Path outputPath;

    @BeforeEach
    public void beforeEach() {
        try {
            inputPath = Files.createTempFile("test", ".md");
            Files.writeString(inputPath, "some text");
            outputPath = Path.of(inputPath.getParent().toString(), "test.html");
            fileManager = new FileManager(outputPath.toString(), inputPath.toString());
        } catch (IOException e) {
            System.err.println( "Error creating temporary test file" );
        }
    }

    @Test
    void readExistentFileTest() {
        try {
            String result = fileManager.readFile();
            String expected = "some text";
            Assertions.assertEquals(expected, result);
        } catch (IOException e) {
            System.err.println("Error reading file");
        }
    }

    @Test
    void readNonExistentFileTest() {
        fileManager = new FileManager(outputPath.toString(), "nonExistentFile.md");
        Assertions.assertThrows(IOException.class, () -> fileManager.readFile());
    }

    @Test
    void writeExistentFileTest() {
        try {
            String expected = "writing file result";
            fileManager.writeFile(expected);

            String result = Files.readString(outputPath);
            Assertions.assertEquals(expected, result);
        } catch (IOException e) {
            System.err.println("Error writing file");
        }
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.deleteIfExists(inputPath);
            Files.deleteIfExists(outputPath);
        } catch (IOException e) {
            System.err.println("Error deleting temporary files");
        }
    }
}
