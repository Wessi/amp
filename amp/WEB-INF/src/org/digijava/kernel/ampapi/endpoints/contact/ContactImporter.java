package org.digijava.kernel.ampapi.endpoints.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectConversionException;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ContactImporter extends ObjectImporter {

    private AmpContact contact;

    public ContactImporter() {
        super(AmpContact.class, new InputValidatorProcessor(InputValidatorProcessor.getContactValidators()));
    }

    public List<ApiErrorMessage> createContact(JsonBean newJson) {
        return importContact(null, newJson);
    }

    public List<ApiErrorMessage> updateContact(Long contactId, JsonBean newJson) {
        return importContact(contactId, newJson);
    }

    private List<ApiErrorMessage> importContact(Long contactId, JsonBean newJson) {
        this.newJson = newJson;

        List<APIField> fieldsDef = AmpFieldsEnumerator.PRIVATE_ENUMERATOR.getContactFields();

        AmpTeamMember createdBy = TeamMemberUtil.getAmpTeamMember(getCreatedByIdOrNull());
        if (createdBy == null) {
            String msg = "Invalid team member in " + ContactEPConstants.CREATED_BY + " field.";
            return Collections.singletonList(SecurityErrors.INVALID_TEAM.withDetails(msg));
        }

        try {
            if (contactId == null) {
                contact = new AmpContact();
            } else {
                contact = (AmpContact) PersistenceManager.getSession().load(AmpContact.class, contactId);
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

    private Long getCreatedByIdOrNull() {
        Object idObj = newJson.get(ContactEPConstants.CREATED_BY);
        if (idObj instanceof Number) {
            return ((Number) idObj).longValue();
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

    @Override
    protected void configureCustom(Object obj, APIField fieldDef) {
        super.configureCustom(obj, fieldDef);

        if (obj instanceof AmpContactProperty) {
            ((AmpContactProperty) obj).setName(fieldDef.getDiscriminator());
        }
    }

    public AmpContact getContact() {
        return contact;
    }
}
