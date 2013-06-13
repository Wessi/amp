/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.Output;

import java.util.Date;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
@TranslatableClass
public class AmpActivityVersion extends AmpActivityFields implements Versionable{
	
	/**
	 * 
	 * NOTE:
	 *    All new fields should be added in {@link AmpActivityFields}
	 *    
	 */
	
	public AmpActivityVersion() {
	}

	public AmpActivityVersion(Long ampActivityId, String name, Date updatedDate, AmpTeamMember modifiedBy, String ampid) {
		this.ampActivityId=ampActivityId;
		this.name=name;
		//this.budget=budget;
		this.updatedDate=updatedDate;
		this.modifiedBy = modifiedBy;
		this.ampId=ampid;
	}

	public AmpActivityVersion(Long ampActivityId, String name, String ampid) {
		this.ampActivityId=ampActivityId;
		this.name=name;
		this.ampId=ampid;
	}

    @Override
    public boolean equalsForVersioning(Object obj) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object getValue() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Output getOutput() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        throw new AssertionError("Not implemented");
    }
}