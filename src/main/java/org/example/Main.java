package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("java Main <inputFile> [--out outputFile]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = null;
        Format format = Format.HTML;

        for (int i = 1; i < args.length; i++) {
            if ("--out".equals(args[i])) {
                if (i + 1 < args.length) {
                    outputFile = args[i + 1];
                } else {
                    System.err.println("Missing value for --out flag");
                    System.exit(1);
                }
            } else if ("--format".equals(args[i])) {
                if (i + 1 < args.length) {
                    try {
                        format = Format.valueOf(args[i + 1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid value for --format flag [should be html/ansi]");
                        System.exit(1);
                    }
                } else {
                    System.err.println("Missing value for --format flag");
                    System.exit(1);
                }
            }
        }

        processMarkdown(inputFile, outputFile, format);
    }

    private static void processMarkdown(String inputFile, String outputFile, Format format) {
        try {
            FileManager fileHandler = new FileManager(outputFile, inputFile);
            String file = fileHandler.readFile();
            MarkdownParser markdownParser = new MarkdownParser(file);
            String markdown = markdownParser.parse(format);
            if (outputFile != null) {
                fileHandler.writeFile(markdown);
            } else {
                System.out.println(markdown);
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            System.exit(1);
        } catch (IllegalStateException e) {
            System.err.println("Error parsing Markdown: " + e.getMessage());
            System.exit(1);
        }
    }
}