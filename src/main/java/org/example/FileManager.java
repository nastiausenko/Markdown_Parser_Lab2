package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private String out;
    private String path;

    public FileManager(String out, String path) {
        this.out = out;
        this.path = path;
    }

    public String readFile() throws IOException {
        Path pathToFile = Paths.get(path);
        if (!Files.exists(pathToFile)) {
            throw new IOException("File not found");
        }
        return Files.readString(pathToFile);
    }

    public void writeFile(String text) throws IOException {
        Path outputPath = Paths.get(out);
        if (!Files.exists(outputPath.getParent())) {
            Files.createDirectories(outputPath.getParent());
        }
        Files.writeString(outputPath, text);
    }
}
