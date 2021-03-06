package org.digijava.kernel.ampapi.endpoints.contact;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectConversionException;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ContactImporter extends ObjectImporter {

    private AmpContact contact;

    public ContactImporter() {
        super(new InputValidatorProcessor(InputValidatorProcessor.getContactValidators()),
                AmpFieldsEnumerator.getPrivateContactEnumerator().getContactFields());
    }

    public List<ApiErrorMessage> createContact(JsonBean newJson) {
        return importContact(null, newJson);
    }

    public List<ApiErrorMessage> updateContact(Long contactId, JsonBean newJson) {
        return importContact(contactId, newJson);
    }

    private List<ApiErrorMessage> importContact(Long contactId, JsonBean newJson) {
        this.newJson = newJson;


        List<APIField> fieldsDef = getApiFields();
        
        Object contactJsonId = newJson.get(ContactEPConstants.ID);
        
        if (contactJsonId != null) {
            if (contactId != null) {
                if (!contactId.equals(getLongOrNull(contactJsonId))) {
                    return singletonList(ContactErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.ID));
                }
            } else {
                return singletonList(ContactErrors.FIELD_READ_ONLY.withDetails(ContactEPConstants.ID));
            }
        }

        Object createdById = newJson.get(ContactEPConstants.CREATED_BY);
        AmpTeamMember createdBy = TeamMemberUtil.getAmpTeamMember(getLongOrNull(createdById));
        if (createdById != null && createdBy == null) {
            return singletonList(ContactErrors.FIELD_INVALID_VALUE.withDetails(ContactEPConstants.CREATED_BY));
        }
        if (contactId == null && createdBy == null) {
            TeamMember teamMember = TeamMemberUtil.getLoggedInTeamMember();
            if (teamMember != null) {
                createdBy = TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
            } else {
                return singletonList(ContactErrors.FIELD_REQUIRED.withDetails(ContactEPConstants.CREATED_BY));
            }
        }

        try {
            if (contactId == null) {
                contact = new AmpContact();
            } else {
                contact = (AmpContact) PersistenceManager.getSession().get(AmpContact.class, contactId);
                
                if (contact == null) {
                    ApiErrorResponse.reportResourceNotFound(ContactErrors.CONTACT_NOT_FOUND);
                }
                
                cleanImportableFields(fieldsDef, contact);
            }

            contact = (AmpContact) validateAndImport(contact, null, fieldsDef, newJson.any(), null, null);

            if (contact == null) {
                throw new ObjectConversionException();
            }

            setupBeforeSave(contact, createdBy);
            PersistenceManager.getSession().saveOrUpdate(contact);

            PersistenceManager.flushAndCommit(PersistenceManager.getSession());
        } catch (ObjectConversionException | RuntimeException e) {
            PersistenceManager.rollbackCurrentSessionTx();

            if (e instanceof RuntimeException) {
                throw new RuntimeException("Failed to import contact", e);
            }
        }

        return new ArrayList<>(errors.values());
    }

    private Long getLongOrNull(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            return null;
        }
    }

    private void setupBeforeSave(AmpContact contact, AmpTeamMember createdBy) {
        if (contact.getId() == null) {
            contact.setCreator(createdBy);
        }
        contact.getProperties().forEach(p -> p.setContact(contact));
        contact.getOrganizationContacts().forEach(o -> o.setContact(contact));
    }

    public AmpContact getContact() {
        return contact;
    }
    
    @Override
    protected boolean ignoreUnknownFields() {
        return AmpOfflineModeHolder.isAmpOfflineMode();
    }
    
}
