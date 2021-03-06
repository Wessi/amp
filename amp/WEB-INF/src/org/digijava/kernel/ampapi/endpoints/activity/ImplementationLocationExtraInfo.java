package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class ImplementationLocationExtraInfo extends CategoryValueExtraInfo {

    @JsonProperty("implementation-levels")
    private final List<Long> implementationLevels;

    public ImplementationLocationExtraInfo(Integer index, List<Long> implementationLevels) {
        super(index);
        this.implementationLevels = implementationLevels;
    }
}
