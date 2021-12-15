package com.synchrony.synchronyswat.utilities;

import com.opencsv.CSVReader;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Log4j2
public class CsvHelper {

    // pass additional parameter to select a Range of Rows
    public static List<Map<String, String>> getCSVData(File file, char delimiter, Integer... args) throws IOException {
        log.info("Get CSV data as List<Map<String, String>> for file: " + file.getName());
        List<Map<String, String>> records = new ArrayList<>();
        var lineNumber = 1;
        var startRow = (args.length >= 1) ? args[0] : 0;
        var endRow = (args.length >= 2) ? args[1] : 0;

        String[] record;
        try(
                var bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
                var csvReader = new CSVReader(bufferedReader, delimiter)
                ){
            var header = csvReader.readNext();

            while ((record = csvReader.readNext())!=null){
                lineNumber++;
                if((lineNumber >= startRow + 1) && (endRow == 0 || lineNumber <= endRow + 1)){
                    Map<String, String> rowMap = new HashMap<>();
                    for (int i = 0; i < record.length; i++) {
                        rowMap.put(header[i].trim(), record[i].trim());
                    }
                    records.add(rowMap);
                }
            }
        }
        return records;
    }

    public static Object[][] getCSVDataForDataProvider(File file, char delimiter) throws IOException {
        log.info("Get CSV data as Object[][] for file: " + file.getName());
        var records = new ArrayList<Object[]>();
        String[] record;
        try(
                var bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
                var csvReader = new CSVReader(bufferedReader, delimiter, '\"', 1)
        ){
            while ((record = csvReader.readNext())!=null){
                records.add(Arrays.copyOf(record, record.length, Object[].class));
            }
        }
        return records.toArray(new Object[records.size()][]);
    }

    public static Object[][] getCSVDataForDataProvider(String filePath, char delimiter) throws IOException {
        log.info("Get CSV data as Object[][] for file: " + filePath);
        var records = new ArrayList<Object[]>();
        String[] record;
        try(
                var bufferedReader = new BufferedReader(new FileReader(IoHelper.getFile(filePath), StandardCharsets.UTF_8));
                var csvReader = new CSVReader(bufferedReader, delimiter, '\"', 1)
        ){
            while ((record = csvReader.readNext())!=null){
                records.add(Arrays.copyOf(record, record.length, Object[].class));
            }
        }
        return records.toArray(new Object[records.size()][]);
    }
}
