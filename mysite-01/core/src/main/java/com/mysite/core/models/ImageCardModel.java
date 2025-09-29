package com.mysite.core.models;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import lombok.Getter;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.wcm.core.components.models.Image;
import static com.day.cq.dam.api.DamConstants.DC_DESCRIPTION;

@Getter
@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class ImageCardModel implements ComponentExporter {
    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Self
    private Image image;

    @ValueMapValue
    private String caption;
    @ValueMapValue
    private boolean damCaption;

    @ValueMapValue
    private String accessibilityLabel;

    public String getFileReference() {
        return image.getFileReference();
    }

    public String getAlt() {
        return image.getAlt();
    }

    public String getCaption() {
        // if (damCaption && isNotBlank(image.getFileReference()))
        if (damCaption && image != null) {
            String fileRef = image.getFileReference();
            if (fileRef != null && !fileRef.trim().isEmpty()) {
                String metadataPath = image.getFileReference() + "/jcr:content/metadata";
                Resource metadataRes = resourceResolver.getResource(metadataPath);
                ValueMap metadataMap = ResourceUtil.getValueMap(metadataRes);
                String damDesc = metadataMap.get(DC_DESCRIPTION, String.class);
                if (damDesc != null && !damDesc.trim().isEmpty()) {
                    return damDesc;
                }
            }
        }
        return caption;
    }

    @Override
    public String getExportedType() {
        throw new UnsupportedOperationException("Unimplemented method 'getExportedType'");
    }

}