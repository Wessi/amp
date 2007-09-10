/**
 * PropertyListable.java (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * PropertyListable.java Implemented by Beans that can have their properties Listable
 * 
 * @author mihai
 * @package org.dgfoundation.amp
 * @since 09.09.2007
 */
public abstract class PropertyListable implements Cloneable {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyListableIgnore {
    }

    
    @Override
    public Object clone() throws CloneNotSupportedException {
	return super.clone();
    }
    
    protected static Logger logger = Logger.getLogger(PropertyListable.class);
    
    
    public abstract String getBeanName();
    
    
    @PropertyListableIgnore
    public String getJspFile() {
	return "/repository/aim/view/listableBean.jsp";
    }
    
    
    /**
         * provides a way to display this bean in HTML. Properties are automatically shown along with their values.
         * CollectionS are unfolded and excluded properties (internally used) are not shown.
         */
    
    @PropertyListableIgnore
    public Map getPropertiesMap() {
	Map<String, String> ret = new HashMap<String, String>();
	BeanInfo beanInfo = null;
	try {
	    beanInfo = Introspector.getBeanInfo(this.getClass());
	    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	    
	    for (int i = 0; i < propertyDescriptors.length; i++) {	
		if(propertyDescriptors[i].getName().equals("class")) continue;
		Method m = propertyDescriptors[i].getReadMethod();
		if(m.isAnnotationPresent(PropertyListableIgnore.class))continue;
		Object object = m.invoke(this, new Object[] {});
		if (object == null ) continue;
		ret.put(propertyDescriptors[i].getName(), object instanceof Collection ? Util.toCSString(
			(Collection) object, false) : object.toString());
	    }
	} catch (IntrospectionException e) {
	    logger.error(e);
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    logger.error(e);
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    logger.error(e);
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    logger.error(e);
	    e.printStackTrace();
	}
	return ret;

    }

}
