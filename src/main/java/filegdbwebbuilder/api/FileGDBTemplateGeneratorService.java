package filegdbwebbuilder.api;

import filegdbwebbuilder.api.exception.FileGDBTemplateServiceException;
import filegdbwebbuilder.entities.FeatureLayer;
import filegdbwebbuilder.entities.FileGDBTemplate;
import filegdbwebbuilder.entities.FileGDBTemplateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Base64;

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

        fileTemplateDataSource.delete();

        return FileGDBTemplateResult.builder().templateBase64(encodeOutputFileTemplateToBase64(outputFilePath)).build();

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
            fileGDBTemplateConfiguration.getFeatureLayers().forEach(featureLayerConfig -> {
                final Layer ogrLayer = fileTemplateDataSource.CreateLayer(featureLayerConfig.getName(), spatialReference,
                        featureLayerConfig.getGeometryType());

                createOgrFields(featureLayerConfig, ogrLayer);
            });

        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when creating ogr layer objects", e);
        }
    }

    private void createOgrFields(final FeatureLayer featureLayerConfig,
                                 final Layer ogrLayer) {
        try {
            featureLayerConfig.getLayerFields()
                    .forEach(featureLayerFieldConfig ->
                            ogrLayer.CreateField(new FieldDefn(featureLayerFieldConfig.getName(), featureLayerFieldConfig.getType())));
            log.info("Field objects created...");
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when creating ogr field objects", e);
        }
    }

    //TODO add to utils class
    private String encodeOutputFileTemplateToBase64(final String outputFilePath) {
        try {
            byte[] outputFileByteArray = FileUtils.readFileToByteArray(Path.of(outputFilePath).toFile());
            return Base64.getEncoder().encodeToString(outputFileByteArray);
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Error when reading template file to byte array...", e);
        }

    }

}
