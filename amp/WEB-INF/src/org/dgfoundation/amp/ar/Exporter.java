/**
 * Exporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Abstract class subclassed by any Exporter that provides alternate
 * output schemas (PDF,XLS,CSV...) for Viewable entities. 
 * Exporters are java based viewers for Viewable items. They behave in the same way as jsp includes for html. Thus,
 * an exporter for an object that has childen (like Reports or Columns) will always call any child exporters before exiting.
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 */
public abstract class Exporter {
	/**
	 * the parent of this exporter, if any. This is used to provide access to parent's properties.
	 */
	protected Exporter parent;

	/**
	 * the item that is required to export
	 */
	protected Viewable item;
	
	protected AmpReports metadata; 

	/**
	 * @return Returns the metadata.
	 */
	public AmpReports getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata The metadata to set.
	 */
	public void setMetadata(AmpReports metadata) {
		this.metadata = metadata;
	}

	/**
	 * Constructs a new exporter object as a child of an already instantiated
	 * Exporter object.
	 * 
	 * @param parent
	 *            the parent of this Exporter
	 * @param item
	 */
	public Exporter(Exporter parent, Viewable item) {
		this.parent = parent;
		this.item = item;
		if(parent!=null) this.metadata=parent.getMetadata();
	}

	public Exporter() {
	}

	/**
	 * method implemented by any exporter, that creates the required objects that can be displayed in output. 
	 * If the Viewable item also holds children, Viewable.invokeExporter will be called for any of them 
	 *
	 */
	public abstract void generate();

}
