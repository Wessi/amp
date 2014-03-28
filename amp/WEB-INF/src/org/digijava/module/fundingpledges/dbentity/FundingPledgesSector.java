// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.SectorUtil;

public class FundingPledgesSector {
	
	private Long id;
	private FundingPledges pledgeid;
	private AmpSector sector;
	private Float sectorpercentage;
	
	public int hashCode() {
		return sector.getAmpSectorId().hashCode();
	}
	
	public boolean equals(Object oth) {
		return sector.getAmpSectorId().equals(((FundingPledgesSector)oth).sector.getAmpSectorId());
	}
	
	public String toString(){
		return String.format("sector name = %s, sectorPercentage = %.2f", sector.getName(), sectorpercentage);
	}
	
	@java.lang.SuppressWarnings("all")
	public Long getId() {
		return this.id;
	}
	
	@java.lang.SuppressWarnings("all")
	public FundingPledges getPledgeid() {
		return this.pledgeid;
	}
	
	@java.lang.SuppressWarnings("all")
	public AmpSector getSector() {
		return this.sector;
	}
	
	@java.lang.SuppressWarnings("all")
	public Float getSectorpercentage() {
		return this.sectorpercentage;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setId(final Long id) {
		this.id = id;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setPledgeid(final FundingPledges pledgeid) {
		this.pledgeid = pledgeid;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSector(final AmpSector sector) {
		this.sector = sector;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSectorpercentage(final Float sectorpercentage) {
		this.sectorpercentage = sectorpercentage;
	}
}