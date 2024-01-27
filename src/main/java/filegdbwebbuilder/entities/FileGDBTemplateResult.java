package filegdbwebbuilder.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FileGDBTemplateResult {

    private String templateBase64;

}
