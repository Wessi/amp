package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * The amp aid effectiveness indicator option domain class
 * An option could be anything, not just predefined
 */
public class AmpAidEffectivenessIndicatorOption implements Serializable {

    private Long ampIndicatorOptionId;
    private String ampIndicatorOptionName;
    private boolean isDefault;

    public AmpAidEffectivenessIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpAidEffectivenessIndicator indicator) {
        this.indicator = indicator;
    }

    private AmpAidEffectivenessIndicator indicator;

    public Long getAmpIndicatorOptionId() {
        return ampIndicatorOptionId;
    }

    public void setAmpIndicatorOptionId(Long ampIndicatorOptionId) {
        this.ampIndicatorOptionId = ampIndicatorOptionId;
    }

    public String getAmpIndicatorOptionName() {
        return ampIndicatorOptionName;
    }

    public void setAmpIndicatorOptionName(String ampIndicatorOptionName) {
        this.ampIndicatorOptionName = ampIndicatorOptionName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }


}
