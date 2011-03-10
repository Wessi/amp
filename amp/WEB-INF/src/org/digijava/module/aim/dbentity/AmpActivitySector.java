package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.util.Output;

public class AmpActivitySector implements Versionable, Serializable, Cloneable {

	private Long ampActivitySectorId;
	
	private AmpActivity activityId;
	
	private AmpSector sectorId;
	
	private Float sectorPercentage;
        
        private AmpClassificationConfiguration classificationConfig;

        public AmpClassificationConfiguration getClassificationConfig() {
            return classificationConfig;
        }

        public void setClassificationConfig(AmpClassificationConfiguration classificationConfig) {
            this.classificationConfig = classificationConfig;
        }


	public Long getAmpActivitySectorId() {
		return ampActivitySectorId;
	}

	public void setAmpActivitySectorId(Long ampActivitySectorId) {
		this.ampActivitySectorId = ampActivitySectorId;
	}

	public AmpActivity getActivityId() {
		return activityId;
	}

	public void setActivityId(AmpActivity activityId) {
		this.activityId = activityId;
	}

	public AmpSector getSectorId() {
		return sectorId;
	}

	public void setSectorId(AmpSector sectorId) {
		this.sectorId = sectorId;
	}

	public Float getSectorPercentage() {
		return sectorPercentage;
	}

	public void setSectorPercentage(Float sectorPercentage) {
		this.sectorPercentage = sectorPercentage;
	}
	
	public String toString() {
		return sectorId!=null?sectorId.getName():"";
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpActivitySector aux = (AmpActivitySector) obj;
		if (this.classificationConfig.equals(aux.getClassificationConfig())
				&& this.sectorId.getAmpSectorId().equals(aux.getSectorId().getAmpSectorId())) {
			return true;
		}
		return false;
	}
	
	public Object getValue() {
		return this.sectorPercentage;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		String scheme = "[" + this.classificationConfig.getClassification().getSecSchemeName() + "]";
		String name = "";
		if (this.sectorId.getParentSectorId() != null) {
			name = " - " + "[" + this.sectorId.getParentSectorId().toString() + "]";
			if (this.sectorId.getParentSectorId().getParentSectorId() != null) {
				name = " - " + "[" + this.sectorId.getParentSectorId().getParentSectorId().toString() + "]" + name;
				if (this.sectorId.getParentSectorId().getParentSectorId().getParentSectorId() != null) {
					name = " - " + "["
							+ this.sectorId.getParentSectorId().getParentSectorId().getParentSectorId().toString()
							+ "]" + name;
				}
			} else {
				name += " - [" + this.sectorId.getName() + "]";
			}
		} else {
			name += " - [" + this.sectorId.getName() + "]";
		}
		out.getOutputs().add(new Output(null, new String[] { scheme + name + " - Percentage: "}, new Object[] { this.sectorPercentage }));
		return out;
	}
	
	@Override
	public Object prepareMerge(AmpActivity newActivity) throws CloneNotSupportedException {
		this.activityId = newActivity;
		this.ampActivitySectorId = null;
		this.sectorId = (AmpSector) this.sectorId.clone();
		//this.sectorId.setAmpSectorId(null);
		return this;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}