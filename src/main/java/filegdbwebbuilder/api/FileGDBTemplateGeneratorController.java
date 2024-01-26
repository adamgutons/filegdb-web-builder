package filegdbwebbuilder.api;

import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.LayerField;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/filegdb", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class FileGDBTemplateGeneratorController {

    @GetMapping(params = "recent")
    public List<LayerField> test() {
        return List.of(LayerField.builder().name("test").build());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public FileGDBTemplate postTemplateConfig(final @RequestBody FileGDBTemplate fileGDBTemplate) {
        return fileGDBTemplate;
    }

}
