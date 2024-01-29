package fileoutput;

import filegdbwebbuilder.fileoutput.FileOutputUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static filegdbwebbuilder.fileoutput.FileOutputUtils.createUniqueTempDirectory;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
class FileOutputUtilsTest {

    @Test
    void testCreateUniqueDirectory() {
        final Path uniqueDirectoryPath = FileOutputUtils.createUniqueTempDirectory();
        assertThat(uniqueDirectoryPath).isNotNull();
        assertThat(uniqueDirectoryPath.toFile()).exists();
        FileUtils.deleteQuietly(uniqueDirectoryPath.toFile());
    }

    @Test
    void testCreateUniqueDirectoryPathToFileGDB() {
        final String outputFilePath = Path.of(createUniqueTempDirectory().toString(), "template.gdb").toString();
        File createdFile = new File(outputFilePath);
        assertThat(outputFilePath).isNotNull();
        assertThat(createdFile).hasName("template.gdb");
        FileUtils.deleteQuietly(Paths.get(outputFilePath).getParent().toFile());
    }


    @Test
    void testDeleteFileOutputDirectory() {
        String fileName = "testFile.txt";
        final Path parentDirectory = FileOutputUtils.createUniqueTempDirectory();
        final File createdFile = new File(parentDirectory.toString(), fileName);

        assertThat(parentDirectory.toFile()).exists();
        FileOutputUtils.deleteFileOutputTempDirectory(parentDirectory.toFile());
        assertThat(createdFile).doesNotExist();
    }

}
