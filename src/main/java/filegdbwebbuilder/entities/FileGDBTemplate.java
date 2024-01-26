package filegdbwebbuilder.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class FileGDBTemplate {

    private static final String TEMPLATE_NAME = "template.gdb";

    private List<FeatureLayer> featureLayers;

    private String spatialReferenceCode;

}
