package filegdbwebbuilder.api;

import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import lombok.extern.slf4j.Slf4j;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static filegdbwebbuilder.fileoutput.FileOutputUtils.createUniqueTempDirectory;

@Slf4j
@Service
public class FileGDBTemplateGeneratorService {

    private static final String GDB_EXTENSION = ".gdb";
    private static final String FILE_DRIVER = "OpenFileGDB";

    public FileGDBTemplateResult generateFileGDBTemplate(final FileGDBTemplate fileGDBTemplateConfiguration) {
        ogr.RegisterAll();
        log.info("Register GDAL complete");
        final String templateName = fileGDBTemplateConfiguration.getTemplateName() + GDB_EXTENSION;
        final String outputFilePath = Path.of(createUniqueTempDirectory().toString(), templateName).toString();
        final DataSource fileTemplateDataSource =
                ogr.GetDriverByName(FILE_DRIVER).CreateDataSource(outputFilePath);
        fileTemplateDataSource.delete();
        return FileGDBTemplateResult.builder().build();
    }

}
