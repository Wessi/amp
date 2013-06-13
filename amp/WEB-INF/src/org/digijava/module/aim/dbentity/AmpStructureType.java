package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

@TranslatableClass
public class AmpStructureType implements ARDimensionable, Serializable{

	private static final long serialVersionUID = 1L;

	private Long typeId;
	@TranslatableField
	private String name;
	private String graphicType;
	private byte[] iconFile;
	private String iconFileContentType;

	private transient Set<AmpStructure> structures;

	public Set<AmpStructure> getStructures() {
		return structures;
	}

	public void setStructures(Set<AmpStructure> structures) {
		this.structures = structures;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(AmpStructureType o) {
		try {
			if (this.name.compareToIgnoreCase(o.getName()) > 0) {
				return 1;
			} else if (this.name.compareToIgnoreCase(o.getName()) == 0) {
				return -0;
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}

	@Override
	public Class getDimensionClass() {
		return null;
	}

	public void setGraphicType(String graphicType) {
		this.graphicType = graphicType;
	}

	public String getGraphicType() {
		return graphicType;
	}

	public void setIconFile(byte[] iconFile) {
		this.iconFile = iconFile;
	}

	public byte[] getIconFile() {
		return iconFile;
	}

	public void setIconFileContentType(String iconFileContentType) {
		this.iconFileContentType = iconFileContentType;
	}

	public String getIconFileContentType() {
		return iconFileContentType;
	}	
}
