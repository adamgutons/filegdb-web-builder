package filegdbwebbuilder.fileoutput;


import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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
        } catch (Exception e) {
            throw new FileOutputUtilsException(String.format("Unable to create temp file directory %s",
                    tempDirectoryPath), e);
        }

        return tempDirectoryPath;
    }

    public static void deleteFileOutputTempDirectory(File tempDirectory) {
        try {
            FileUtils.deleteDirectory(tempDirectory);
        } catch (Exception e) {
            throw new FileOutputUtilsException(String.format("Unable to delete file directory %s",
                    tempDirectory), e);
        }
    }

    public static File createFile(Path outputFolder, String fileName) throws IOException {
        return Files.createFile(outputFolder.resolve(fileName)).toFile();
    }

    public static File zipFiles(List<File> filesToZip, Path outputFolder, String zippedFileName) throws IOException {

        File outputZipFile = createFile(outputFolder, zippedFileName);

        try (FileOutputStream fos = new FileOutputStream(outputZipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (File fileToZip : filesToZip) {
                if (fileToZip.isDirectory()) {
                    zipDirectory(fileToZip, fileToZip.getName(), zipOut);
                } else {
                    zipFile(fileToZip, "", zipOut);
                }
            }
        } catch (Exception e) {
            throw new FileOutputUtilsException(String.format("Unable to zip output file(s): %s", outputZipFile), e);
        }

        return outputZipFile;
    }

    private static void zipDirectory(File directory, String parentPath, ZipOutputStream zipOut) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, parentPath + File.separator + file.getName(), zipOut);
                } else {
                    zipFile(file, parentPath, zipOut);
                }
            }
        }
    }

    private static void zipFile(File file, String parentPath, ZipOutputStream zipOut) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            // file name is parent path when zipping CSV, EXCEL, etc. FileGDB parent path is the .gdb directory
            ZipEntry zipEntry = new ZipEntry(Paths.get(parentPath, file.getName()).toString());
            zipOut.putNextEntry(zipEntry);
            fis.transferTo(zipOut);
            zipOut.closeEntry();
        }
    }

}
