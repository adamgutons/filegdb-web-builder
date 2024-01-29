package filegdbwebbuilder.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class FileGDBTemplate {

    private String templateName;

    private List<FeatureLayer> featureLayers;

    private String spatialReferenceCode;

}
