package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

@TranslatableClass (displayName = "Actor")
public class AmpActor  implements Serializable, Cloneable
{

	private Long ampActorId ;
	@TranslatableField
	private String name;
	private String nameTrimmed;
	private AmpMeasure measure;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmpActorId() {
		return ampActorId;
	}
	public void setAmpActorId(Long ampActorId) {
		this.ampActorId = ampActorId;
	}
	
	public AmpMeasure getMeasure() {
		return measure;
	}
	public void setMeasure(AmpMeasure measure) {
		this.measure = measure;
	}
	public String getNameTrimmed() {
		return name.replace(" ", "");
	}
	public void setNameTrimmed(String nameTrimmed) {
		this.nameTrimmed = nameTrimmed;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	

}
