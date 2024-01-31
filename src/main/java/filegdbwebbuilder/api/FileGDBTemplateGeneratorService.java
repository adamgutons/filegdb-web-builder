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

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static filegdbwebbuilder.fileoutput.FileOutputUtils.*;

@Slf4j
@Service
public class FileGDBTemplateGeneratorService {

    private static final String FILE_DRIVER = "OpenFileGDB";
    private static final String TEMPLATE_NAME = "template.gdb";

    public FileGDBTemplateResult generateFileGDBTemplate(final FileGDBTemplate fileGDBTemplateConfiguration) {

        ogr.RegisterAll();

        final Path outputFilePath = Path.of(createUniqueTempDirectory().toString(), TEMPLATE_NAME);
        final DataSource fileTemplateDataSource =
                ogr.GetDriverByName(FILE_DRIVER).CreateDataSource(outputFilePath.toString());
        log.info("Data source created...");

        createOgrLayerObjects(fileGDBTemplateConfiguration, fileTemplateDataSource, createSpatialReference(fileGDBTemplateConfiguration));
        log.info("Layer objects created...");

        fileTemplateDataSource.delete();

        try {
            final File zippedOutputFile = zipFiles(List.of(outputFilePath.toFile()), outputFilePath.getParent(),
                    fileGDBTemplateConfiguration.getTemplateName() + ".zip");
            return FileGDBTemplateResult.builder()
                    .templateBase64(encodeOutputFileTemplateToBase64(zippedOutputFile.getPath())).build();
        } catch (Exception e) {
            throw new FileGDBTemplateServiceException("Unable to zip template file...", e);
        }
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

}
