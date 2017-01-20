package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;


/**
 * {@link AmpTeam} class JSON serializer
 * 
 * @author Nadejda Mandrescu
 */
public class AmpTeamSerializer extends AmpJsonSerializer<AmpTeam> {
    private ObjectMapper mapper = new ObjectMapper();
    private AmpARFilter arFilter;
    private final SimpleDateFormat sdfIn = new SimpleDateFormat(AmpARFilter.SDF_IN_FORMAT_STRING);
    private final SimpleDateFormat sdfApiOut = new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT);
    
    @Override
    protected void serialize(AmpTeam ampTeam) throws IOException {
        writeField("id", ampTeam.getAmpTeamId());
        writeField("name", getTranslations("name"));
        writeField("description", getTranslations("description"));
        writeField("workspace-group", ampTeam.getWorkspaceGroup() == null ? null : ampTeam.getWorkspaceGroup().getValue());
        
        writeField("workspace-lead-id", ampTeam.getTeamLead() == null ? null : ampTeam.getTeamLead().getAmpTeamMemId());
        writeField("child-workspaces", ampTeam.getChildrenWorkspaces());
        writeField("add-activity", ampTeam.getAddActivity());
        writeField("is-computed", ampTeam.getComputation());
        writeField("hide-draft", ampTeam.getHideDraftActivities());
        writeField("is-cross-team-validation", ampTeam.getCrossteamvalidation());
        writeField("use-filter", ampTeam.getUseFilter());
        writeField("parent-workspace-id", ampTeam.getParentTeamId() == null ? null : ampTeam.getParentTeamId().getAmpTeamId());
        writeField("access-type", ampTeam.getAccessType());
        writeField("permission-strategy", ampTeam.getPermissionStrategy());
        writeField("fm-template-id", ampTeam.getFmTemplate() == null ? null : ampTeam.getFmTemplate().getId());
        writeField("workspace-prefix", ampTeam.getWorkspacePrefix() == null ? null : ampTeam.getWorkspacePrefix().getValue());
        
        if (Boolean.TRUE.equals(ampTeam.getComputation()) && ampTeam.getOrganizations() != null 
                && !ampTeam.getOrganizations().isEmpty()) {
            List<Long> computedOrgs = ((Collection<AmpOrganisation>) ampTeam.getOrganizations()).stream()
                    .map(org -> org.getAmpOrgId()).collect(Collectors.toList());
            writeField("organizations", computedOrgs);
        }
        
        if (ampTeam.getFilterDataSet() != null && !ampTeam.getFilterDataSet().isEmpty()) {
        	arFilter = FilterUtil.buildFilterFromSource(ampTeam);
            Map<String, Object> filters = new TreeMap<String, Object>();
            for (AmpTeamFilterData filter : ampTeam.getFilterDataSet()) {
                if (!AmpARFilter.SETTINGS_PROPERTIES.contains(filter.getPropertyName()) 
                        && StringUtils.isNotBlank(filter.getValue())) {
                    filters.put(filter.getPropertyName(), getFilterValue(filter, filters.get(filter.getPropertyName())));
                }
            }
            writeField("workspace-filters", filters);
        }
    }
    
    private Object getFilterValue(AmpTeamFilterData filter, Object existing) throws IOException {
        try {
            Class<?> clazz = Class.forName(filter.getPropertyClassName());
            if (Collection.class.isAssignableFrom(clazz)) {
                Class<? extends Collection<Long>> collectionClass = (Class<Collection<Long>>) clazz;
                Collection<Long> set = existing == null ? collectionClass.newInstance() : (Collection) existing;
                set.add(Long.valueOf(filter.getValue()));
                return set;
            }
            if (AmpARFilter.DATE_PROPERTIES.contains(filter.getPropertyName())) {
            	// no dynamic dates filter conversion, just passing that config further as it is 
            	Field dateField = AmpARFilter.class.getDeclaredField(filter.getPropertyName());
            	dateField.setAccessible(true);
            	return sdfApiOut.format(sdfIn.parse((String) dateField.get(arFilter)));
            }
            Object value = mapper.readValue(filter.getValue(), clazz);
            return value;
        } catch (Exception e) {
            System.out.println(filter.toString());
            throw new IOException(e);
        }
    }
    
}
