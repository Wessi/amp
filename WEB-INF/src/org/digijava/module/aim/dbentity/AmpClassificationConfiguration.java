package org.digijava.module.aim.dbentity;


public class AmpClassificationConfiguration {
	
	public static final String PRIMARY_CLASSIFICATION_CONFIGURATION_NAME	= "Primary";
	public static final String SECONDARY_CLASSIFICATION_CONFIGURATION_NAME	= "Secondary";
	
	
	private Long id;
    private String name;
    private AmpSectorScheme classification;
    private boolean multisector ;
    private boolean primary;

    public AmpSectorScheme getClassification() {
        return classification;
    }

    public void setClassification(AmpSectorScheme classification) {
        this.classification = classification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMultisector() {
        return multisector;
    }

    public void setMultisector(boolean multisector) {
        this.multisector = multisector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimary() {
        return primary;
    }
    /**
     *
     * There must be only one primary configuration in database
     */
    
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
        

}
