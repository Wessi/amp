package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.module.aim.util.ValidationStatus;

public class ActivityInformation {

    private Long ampActivityId;

    private boolean edit;

    private boolean validate;

    @JsonProperty(ActivityEPConstants.VALIDATION_STATUS)
    private ValidationStatus validationStatus;

    @JsonProperty(ActivityEPConstants.IS_ACTIVITY_WORKSPACE_PRIVATE)
    private boolean activityWorkspacePrivate;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty(ActivityEPConstants.DAYS_FOR_AUTOMATIC_VALIDATION)
    private Integer daysForAutomaticValidation;

    public ActivityInformation(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
        this.validationStatus = ValidationStatus.UNKNOWN;
    }
    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public boolean isActivityWorkspacePrivate() {
        return activityWorkspacePrivate;
    }

    public void setActivityWorkspacePrivate(boolean activityWorkspacePrivate) {
        this.activityWorkspacePrivate = activityWorkspacePrivate;
    }

    public Integer getDaysForAutomaticValidation() {
        return daysForAutomaticValidation;
    }

    public void setDaysForAutomaticValidation(Integer daysForAutomaticValidation) {
        this.daysForAutomaticValidation = daysForAutomaticValidation;
    }
}
