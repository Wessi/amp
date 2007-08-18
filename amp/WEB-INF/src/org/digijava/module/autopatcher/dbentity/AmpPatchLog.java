/**
 * AmpPatchLog.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.autopatcher.dbentity;

import java.io.Serializable;
import java.sql.Timestamp;

import org.dgfoundation.amp.Util;


/**
* AmpPatchLog.java
* TODO description here
* @author mihai
* @package org.digijava.module.autopatcher.dbentity
* @since 12.08.2007
 */
public class AmpPatchLog implements Serializable {
	
	private static final long serialVersionUID = -2312854280770940559L;
	private Long id;
	private Timestamp invoked;
	private Long elapsed;
	private AmpPatch patch;
	private String sql;
	private String response;
	private String actionName;
	private String actionReason;
	private Boolean successful;
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public Long getElapsed() {
		return elapsed;
	}
	public void setElapsed(Long elapsed) {
		this.elapsed = elapsed;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getInvoked() {
		return invoked;
	}
	public void setInvoked(Timestamp invoked) {
		this.invoked = invoked;
	}
	public AmpPatch getPatch() {
		return patch;
	}
	public void setPatch(AmpPatch patch) {
		this.patch = patch;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Boolean getSuccessful() {
		return successful;
	}
	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}
	public String getActionReason() {
		return actionReason;
	}
	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
	
	public String toString() {
		return Util.getBeanAsString(this, "", false);
	}
	
	public static final String ACTION_APPLY="apply";
	public static final String ACTION_ROLLBACK="rollback";
	
	public static final String REASON_PENDING_EXEC="pendingExecution";
	public static final String REASON_REQUESTED_BY_DEPENDENCY="requestedByDependency";
	public static final String REASON_TARGETS_FILTER="targetsFilterDoNotApply";
	
	
}
