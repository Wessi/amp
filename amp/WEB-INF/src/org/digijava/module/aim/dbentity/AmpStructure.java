package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Output;

/**
 * Persister class for Structures
 * @author fferreyra
 */
@TranslatableClass (displayName = "Structure")
public class AmpStructure implements Serializable,Comparable, Versionable, Cloneable {
	private static Logger logger = Logger.getLogger(AmpStructure.class);
	private Long ampStructureId;
	@TranslatableField
	private String title;
	@TranslatableField
	private String description;
	private String latitude;
	private String longitude;
	private String shape;
	private java.sql.Timestamp creationdate;
	private AmpStructureType type;
	private Set activities;
	private Set<AmpStructureImg> images;
	
	public Set getActivities() {
		return activities;
	}
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	public Long getAmpStructureId() {
		return ampStructureId;
	}
	public void setAmpStructureId(Long ampStructureId) {
		this.ampStructureId = ampStructureId;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public AmpStructureType getType() {
		return type;
	}
	public void setType(AmpStructureType type) {
		this.type = type;
	}
	
	/**
	 * A simple string comparison to sort components by title
	 */

//	public int compareTo(AmpStructure o) {
//		try {
//			if(o.title == null && this.title == null) return 0;
//			if(o.title == null) return 1;
//			if(this.title == null) return -1;
//			if (this.title.compareToIgnoreCase(o.title) > 0) {
//				return 1;
//			} else if (this.title.compareToIgnoreCase(o.title) == 0) {
//				return -0;
//			}
//		} catch (Exception e) {
//			logger.error("Error", e);
//			return -1;
//		}
//		return -1;
//	}	
	
	
	public int compareTo(Object obj) {
		
		if (!(obj instanceof AmpStructure)) 
			throw new ClassCastException();
		
		AmpStructure ampStr = (AmpStructure) obj;
		if (this.title != null) {
			if (ampStr.title != null) {
				return (this.title.trim().toLowerCase().
						compareTo(ampStr.title.trim().toLowerCase()));
			} else {
				return (this.title.trim().toLowerCase().
						compareTo(""));
			}
		} else {
			if (ampStr.title != null) {
				return ("".compareTo(ampStr.title.trim().toLowerCase()));
			} else {
				return 0;
			}			
		}
	}
	
	//Do not override equals unless REALLY NEEDED!
	/*
	 * 
	@Override
	public boolean equals(Object obj) {
		AmpStructure target=(AmpStructure) obj;
		if (this.ampStructureId == null)
			return super.equals(obj);
		
		if (target!=null && this.ampStructureId!=null){
			return (target.getAmpStructureId().doubleValue()==this.getAmpStructureId().doubleValue());
		}
		return false;
		
	}
	*/
	public java.sql.Timestamp getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(java.sql.Timestamp creationdate) {
		this.creationdate = creationdate;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpStructure aux = (AmpStructure) obj;
		String original = this.title != null ? this.title : "";
		String copy = aux.title != null ? aux.title : "";
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "Title" }, new Object[] { this.title != null ? this.title
						: "Empty Title" }));
		if (this.description != null && !this.description.trim().equals("")) {
			out.getOutputs()
					.add(new Output(null, new String[] { "Description" }, new Object[] { this.description }));
		}
		if (this.creationdate != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Creation Date" }, new Object[] { this.creationdate }));
		}
		return out;
	}
	@Override
	public Object getValue() {
		String value = " " + this.creationdate + this.description /*+ this.activity*/;
		return value;
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpStructure aux = (AmpStructure) clone();
		aux.activities = new HashSet();
		aux.images = new HashSet();
		if (this.images != null){
			for(AmpStructureImg img : this.images){
				AmpStructureImg auxImg =(AmpStructureImg) img.clone();
				auxImg.setId(null);
				auxImg.setStructure(aux);
				aux.images.add(auxImg);
			}
		}
		//aux.activities.add(newActivity);
		aux.ampStructureId = null;
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public String getShape() {
		return shape;
	}
	public Set<AmpStructureImg> getImages() {
		return images;
	}
	public void setImages(Set<AmpStructureImg> images) {
		this.images = images;
	}

	@Override
	public String toString()
	{
		return String.format("AmpStructure[id=%s], title = %s, description = %s", this.ampStructureId, this.title, this.description);
	}
	
    public static String hqlStringForTitle(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "title").getSQLFunctionCall(idSource + ".ampStructureId");
    }

    public static String sqlStringForTitle(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "title").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForDescription(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "description").getSQLFunctionCall(idSource + ".ampStructureId");
    }

    public static String sqlStringForDescription(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "description").getSQLFunctionCall(idSource);
    }
}
