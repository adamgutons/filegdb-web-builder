package filegdbwebbuilder.fileoutput;


import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@UtilityClass
public class FileOutputUtils {

    public static String generateUniqueTempDirectoryName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String randomUUID = UUID.randomUUID().toString();
        return timestamp + "_" + randomUUID;
    }

    public static Path createUniqueTempDirectory() {
        Path tempDirectoryPath = Path.of(generateUniqueTempDirectoryName());
        try {
            if (!Files.exists(tempDirectoryPath)) {
                Files.createDirectories(tempDirectoryPath);
            }
        } catch (IOException e) {
            throw new FileOutputUtilsException(String.format("Unable to create temp file directory %s",
                    tempDirectoryPath));
        }

        return tempDirectoryPath;
    }

    public static void deleteFileOutputTempDirectory(File tempDirectory) {
        try {
            FileUtils.deleteDirectory(tempDirectory);
        } catch (IOException e) {
            throw new FileOutputUtilsException(String.format("Unable to delete file directory %s",
                    tempDirectory));
        }
    }

}
