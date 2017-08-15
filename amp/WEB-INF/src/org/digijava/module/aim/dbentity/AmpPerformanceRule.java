package org.digijava.module.aim.dbentity;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.performance.AmpCategoryValueSerializer;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Viorel Chihai
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AmpPerformanceRule {

    private Long id;

    private String name;

    private String description;

    @JsonProperty(PerformanceRuleConstants.FIELD_TYPE_CLASS_NAME)
    private String typeClassName;

    private Boolean enabled;

    @JsonSerialize(using = AmpCategoryValueSerializer.class)
    private AmpCategoryValue level;

    @JsonManagedReference
    private Set<AmpPerformanceRuleAttribute> attributes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeClassName() {
        return typeClassName;
    }

    public void setTypeClassName(String typeClassName) {
        this.typeClassName = typeClassName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AmpCategoryValue getLevel() {
        return level;
    }

    public void setLevel(AmpCategoryValue level) {
        this.level = level;
    }

    public Set<AmpPerformanceRuleAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AmpPerformanceRuleAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "PerformanceRule [id=" + id + ", name=" + name + ", typeClassName=" + typeClassName + ", enabled="
                + enabled + ", level=" + level + "]";
    }
}
