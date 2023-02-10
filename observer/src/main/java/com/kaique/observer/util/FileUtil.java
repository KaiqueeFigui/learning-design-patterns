package com.kaique.observer.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileUtil {

    public static List<String[]> readCsv(String fileContent, String filename) throws IOException, CsvException {
        var path = Files.createTempFile(filename, null);
        var file = path.toFile();
        try (Writer writer = new FileWriter(file); Reader reader = new FileReader(file); CSVReader csvReader = new CSVReader(reader)) {
            writer.write(fileContent);
            writer.flush();
            var lines = csvReader.readAll();
            return lines;
        }
    }

}
