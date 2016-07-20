/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.util;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;

/**
 * Customized ObjectMapper provider
 * 
 * @author Nadejda Mandrescu
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public ObjectMapper getContext(Class<?> type) {
        if (type.equals(JsonBean.class)) {
            configure();
            return mapper;
        }
        return null;
    }
    
    private void configure() {
        Map<String, Set<String>> jsonFiltersDef = EndpointUtils.getAndClearJsonFilters();
        
        // configure Json Filters
        SimpleFilterProvider sfp = new SimpleFilterProvider();
        if (jsonFiltersDef != null && !jsonFiltersDef.isEmpty()) {
            for (Entry<String, Set<String>> jsonFilterDef : jsonFiltersDef.entrySet()) {
                SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(jsonFilterDef.getValue());
                sfp.addFilter(jsonFilterDef.getKey(), filter);
            }
        }
        // if nothing to filter or invalid filter
        sfp.setFailOnUnknownId(false);
        mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setFilters(sfp);
    }

}