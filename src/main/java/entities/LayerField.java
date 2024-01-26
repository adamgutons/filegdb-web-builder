package entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class LayerField {

    private String name;

    private Integer type;

}
