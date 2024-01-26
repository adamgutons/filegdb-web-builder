package filegdbwebbuilder;

import filegdbwebbuilder.api.FileGDBTemplateGeneratorService;
import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FileGDBTemplateGeneratorServiceTests {

    @Mock
    private FileGDBTemplateGeneratorService fileGDBTemplateGeneratorService;

    @Test
    void generateFileGDBTemplateTest_successful() {

        final FileGDBTemplate fileGDBTemplate = FileGDBTemplate.builder().build();

        when(fileGDBTemplateGeneratorService.generateFileGDBTemplate(fileGDBTemplate))
                .thenReturn(FileGDBTemplateResult.builder().build());

        final FileGDBTemplateResult fileGDBTemplateResult = fileGDBTemplateGeneratorService.generateFileGDBTemplate(fileGDBTemplate);
        assertThat(fileGDBTemplateResult).isInstanceOf(FileGDBTemplateResult.class);

    }

}
