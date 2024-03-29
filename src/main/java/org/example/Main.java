package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsageAndExit();
        }

        String inputFile = args[0];
        String outputFile = null;
        Format format = null;

        for (int i = 1; i < args.length; i++) {
            if ("--out".equals(args[i])) {
                outputFile = parseOutputFile(args, i);
            } else if ("--format".equals(args[i])) {
                format = parseFormat(args, i);
            }
        }

        if (format == null) {
            if (outputFile != null) {
                format = Format.HTML;
            } else {
                format = Format.ANSI;
            }
        }

        processMarkdown(inputFile, outputFile, format);
    }

    private static void printUsageAndExit() {
        System.err.println("java Main <inputFile> [--out outputFile] [--format html/ansi]");
        System.exit(1);
    }

    private static String parseOutputFile(String[] args, int index) {
        if (index + 1 < args.length) {
            return args[index + 1];
        } else {
            System.err.println("Missing value for --out flag");
            System.exit(1);
            return null;
        }
    }

    private static Format parseFormat(String[] args, int index) {
        if (index + 1 < args.length) {
            try {
                return Format.valueOf(args[index + 1].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid value for --format flag [should be html/ansi]");
                System.exit(1);
                return null;
            }
        } else {
            System.err.println("Missing value for --format flag");
            System.exit(1);
            return null;
        }
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