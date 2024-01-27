package filegdbwebbuilder.api;

import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import org.springframework.stereotype.Service;

@Service
public class FileGDBTemplateGeneratorService {

    public FileGDBTemplateResult generateFileGDBTemplate(final FileGDBTemplate fileGDBTemplateConfiguration) {

        return FileGDBTemplateResult.builder().build();
    }

}
