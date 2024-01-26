package entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class FeatureLayer {

    private String name;

    private List<LayerField> layerFields;

    private Integer geometryType;

    private String spatialReferenceCode;

}
