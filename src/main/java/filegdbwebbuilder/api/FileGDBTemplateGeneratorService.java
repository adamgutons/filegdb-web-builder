package filegdbwebbuilder.api;

import filegdbwebbuilder.api.exception.FileGDBTemplateServiceException;
import filegdbwebbuilder.entities.FeatureLayer;
import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import lombok.extern.slf4j.Slf4j;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static filegdbwebbuilder.fileoutput.FileOutputUtils.createUniqueTempDirectory;

@Slf4j
@Service
public class FileGDBTemplateGeneratorService {

    private static final String FILE_DRIVER = "OpenFileGDB";
    private static final String TEMPLATE_NAME = "template.gdb";

    public FileGDBTemplateResult generateFileGDBTemplate(final FileGDBTemplate fileGDBTemplateConfiguration) {

        ogr.RegisterAll();

        final String outputFilePath = Path.of(createUniqueTempDirectory().toString(), TEMPLATE_NAME).toString();
        final DataSource fileTemplateDataSource =
                ogr.GetDriverByName(FILE_DRIVER).CreateDataSource(outputFilePath);
        log.info("Data source created...");
        createOgrLayerObjects(fileGDBTemplateConfiguration, fileTemplateDataSource, createSpatialReference(fileGDBTemplateConfiguration));
        log.info("Layer objects created...");
        createOgrFieldObjects(fileGDBTemplateConfiguration, fileTemplateDataSource);
        log.info("Field objects created...");
        fileTemplateDataSource.delete();

        return FileGDBTemplateResult.builder().build();

    }

    private SpatialReference createSpatialReference(final FileGDBTemplate fileGDBTemplateConfiguration) {
        try {
            final SpatialReference spatialReference = new SpatialReference();
            spatialReference.ImportFromEPSG(fileGDBTemplateConfiguration.getSpatialReferenceCode());
            log.info("Spatial reference created...");
            return spatialReference;
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when creating spatial reference", e);
        }
    }

    private void createOgrLayerObjects(final FileGDBTemplate fileGDBTemplateConfiguration,
                                       final DataSource fileTemplateDataSource,
                                       final SpatialReference spatialReference) {
        try {
            fileGDBTemplateConfiguration.getFeatureLayers().forEach(layerField ->
                    fileTemplateDataSource.CreateLayer(layerField.getName(), spatialReference, layerField.getGeometryType()));
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when creating ogr layer objects", e);
        }
    }

    private void createOgrFieldObjects(final FileGDBTemplate fileGDBTemplateConfiguration,
                                       final DataSource fileTemplateDataSource) {
        try {
            fileGDBTemplateConfiguration.getFeatureLayers()
                    .forEach(featureLayerConfig -> createOgrField(featureLayerConfig, fileTemplateDataSource));
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when creating ogr layer objects", e);
        }
    }

    private void createOgrField(final FeatureLayer featureLayerConfiguration,
                                final DataSource fileTemplateDataSource) {
        try {
            final Layer ogrLayer = fileTemplateDataSource.GetLayer(featureLayerConfiguration.getName());
            featureLayerConfiguration.getLayerFields()
                    .forEach(layerFieldConfig ->
                            ogrLayer.CreateField(new FieldDefn(layerFieldConfig.getName(), layerFieldConfig.getType())));
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when creating ogr field objects", e);
        }
    }


}
