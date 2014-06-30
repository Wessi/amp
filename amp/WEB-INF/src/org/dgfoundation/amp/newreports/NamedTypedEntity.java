package org.dgfoundation.amp.newreports;

/**
 * class describing a named entity to be used in a report
 * @author Dolghier Constantin
 *
 */
public abstract class NamedTypedEntity {
	
	/**
	 * the name of the entity, like "Primary Sector"
	 */
	protected final String entityName;
	
	/**
	 * the type of the entity, like "Pledge" or "Activity". 
	 */
	protected final ReportEntityType entityType;
	
	/**
	 * 
	 * @param columnName - the name of the column
	 * @param columnType - the type of entity to which the column pertains, like Pledge / Activity 
	 */
	public NamedTypedEntity(String entityName, ReportEntityType entityType) {
		this.entityName = entityName;
		if (this.entityName == null || this.entityName.isEmpty())
			throw new NullPointerException("columnName cannot be null or empty!");
				
		this.entityType = entityType;
		if (this.entityType == null)
			throw new NullPointerException("columnType cannot be null!");
	}
	
	/**
	 * equivalent to calling {@link #NamedTypedEntity(String, ENTITY_TYPE_ALL) )}
	 * @param columnName - the name of the column
	 */
	public NamedTypedEntity(String entityName) {
		this(entityName, ReportEntityType.ENTITY_TYPE_ALL);
	}
	
	public String getEntityName() {
		return this.entityName;
	}
	
	@Override public boolean equals(Object oth) {
		return this.entityName.equals(((NamedTypedEntity) oth).entityName);
	}
	
	@Override public int hashCode() {
		return this.entityName.hashCode();
	}
}
