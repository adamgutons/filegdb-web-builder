package filegdbwebbuilder.api;

import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import lombok.extern.slf4j.Slf4j;
import org.gdal.ogr.ogr;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileGDBTemplateGeneratorService {

    public FileGDBTemplateResult generateFileGDBTemplate(final FileGDBTemplate fileGDBTemplateConfiguration) {
        ogr.RegisterAll();
        log.info("Register GDAL complete");
        return FileGDBTemplateResult.builder().build();
    }

}
