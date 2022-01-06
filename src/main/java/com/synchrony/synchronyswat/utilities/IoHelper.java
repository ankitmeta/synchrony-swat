package com.synchrony.synchronyswat.utilities;

import lombok.extern.log4j.Log4j2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


@Log4j2
public class IoHelper {
    public static String getFileData(File file) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try(Stream<String> stream = Files.lines(Paths.get(file.getPath()))){
            stream.forEach(s -> fileContent.append(s).append("\n"));
        }
        log.info(String.format("File %s data loaded in string", file.getName()));
        return fileContent.toString();
    }

    public static File getFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if(Files.exists(path.toAbsolutePath())){
            return path.toFile();
        }else {
            log.error("Error in getting file: {}", filePath);
            throw new FileNotFoundException(filePath + " not found");
        }
    }
}
