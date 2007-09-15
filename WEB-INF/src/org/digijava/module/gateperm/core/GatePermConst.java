/**
 * GateConstants.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;

/**
 * GateConstants.java 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 05.09.2007
 */
public final class GatePermConst {
	
	public static Class[] availableGatesSingleton=null;
	

	/**
	 * list here all the available permissibles in the system. Since subclass search
	 * through reflection is not supported by Java, we need a list with them
	 * All Permissibles must extend the Permissible class
	 */
	public static final Class[] availablePermissibles = new Class[] {
			AmpActivity.class, AmpFieldsVisibility.class };

	/**
	 * Add here all the new actions that you may need to implement. Do not
	 * forget to add them into getImplementedActions for your Permissible
	 * GateConstants.Actions TODO description here
	 * 
	 * @author mihai
	 * @package org.digijava.module.gateperm.core
	 * @since 05.09.2007
	 */
	public static final class Actions {
		public static final String EDIT = "EDIT";

		public static final String NEW = "NEW";

		public static final String VIEW = "VIEW";

		public static final String PUBLISH = "PUBLISH";
	}
}
