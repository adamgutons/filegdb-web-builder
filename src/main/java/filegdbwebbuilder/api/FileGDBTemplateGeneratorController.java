package filegdbwebbuilder.api;

import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/filegdb", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class FileGDBTemplateGeneratorController {

    private final FileGDBTemplateGeneratorService fileGDBTemplateGeneratorService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public FileGDBTemplateResult generateTemplateConfiguration(final @RequestBody FileGDBTemplate fileGDBTemplateConfiguration) {
        return fileGDBTemplateGeneratorService.generateFileGDBTemplate(fileGDBTemplateConfiguration);
    }

}
