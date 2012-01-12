package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.SectorDimension;
import org.digijava.module.aim.util.AmpComboboxDisplayable;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.HierarchyListableComparator;
import org.digijava.module.aim.util.Identifiable;

import edu.emory.mathcs.backport.java.util.TreeSet;


public class AmpSector implements Serializable, Comparable, Identifiable, ARDimensionable, HierarchyListable, AmpComboboxDisplayable
{
	private Long ampSectorId ;
	private AmpSector parentSectorId ;
	private String sectorCode ;
	private String name ;
	private String type; 
	private AmpOrganisation ampOrgId;
	private AmpSectorScheme ampSecSchemeId;
	private String description ;
	private String language ;
	private String version ;
	private Set aidlist ;
    private Set indicators;
    private Set<AmpSector> sectors;
    
    public Set<AmpSector> getSectors() {
		return sectors;
	}

	public void setSectors(Set<AmpSector> sectors) {
		this.sectors = sectors;
	}

	private String sectorCodeOfficial;
	
	private String segmentCode;
	private transient Collection<AmpSector> transientChildren;
	
	public String getSegmentCode() {
		return segmentCode;
	}

	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}

	/**
	 * @return
	 */
	public Set getAidlist() {
		return aidlist;
	}

	/**
	 * @return
	 */
/*	public Long getAmpDacSectorId() {
		return ampDacSectorId;
	}*/

	/**
	 * @return
	 */
	public AmpSector getParentSectorId() {
		return parentSectorId;
	}

	/**
	 * @return
	 */
	public Long getAmpSectorId() {
		return ampSectorId;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getSectorCode() {
		return sectorCode;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param set
	 */
	public void setAidlist(Set set) {
		aidlist = set;
	}

	/**
	 * @param long1
	 */
/*	public void setAmpDacSectorId(Long long1) {
		ampDacSectorId = long1;
	} */

	/**
	 * @param long1
	 */
	public void setParentSectorId(AmpSector sec) {
		this.parentSectorId = sec;
	}

	/**
	 * @param long1
	 */
	public void setAmpSectorId(Long long1) {
		ampSectorId = long1;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setSectorCode(String string) {
		sectorCode = string;
	}

	/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}
public AmpOrganisation getAmpOrgId() {
		return ampOrgId;
	}

public void setAmpOrgId(AmpOrganisation org) {
		this.ampOrgId = org;
	}

	/**
	 * @return
	 */
	public AmpSectorScheme getAmpSecSchemeId() {
		return ampSecSchemeId;
	}

	/**
	 * @param scheme
	 */
	public void setAmpSecSchemeId(AmpSectorScheme scheme) {
		ampSecSchemeId = scheme;
	}


	public int compareTo(Object o) {
		return ampSectorId.compareTo(((AmpSector)o).getAmpSectorId());
	}
	
	public String toString() {
		return name;
	}

	public Object getIdentifier() {
		return this.getAmpSectorId();
	}

	public Set getIndicators() {
		return indicators;
	}

	public void setIndicators(Set indicators) {
		this.indicators = indicators;
	}

	public Class getDimensionClass() {
	    return SectorDimension.class;
	}

	public String getSectorCodeOfficial() {
		return sectorCodeOfficial;
	}

	public void setSectorCodeOfficial(String sectorCodeOfficial) {
		this.sectorCodeOfficial = sectorCodeOfficial;
	}

	@Override
	public int getCountDescendants() {
		int ret = 1;
		if ( this.getChildren() != null ) {
			for ( HierarchyListable hl: this.getChildren() )
				ret += hl.getCountDescendants();
		}
		return ret;
	}

	@Override
	public String getLabel() {
		return this.name;
	}

	@Override
	public String getUniqueId() {
		return this.ampSectorId.toString();
	}

	@Override
	public Collection<AmpSector> getChildren() {
		if (transientChildren == null)
			transientChildren	= new TreeSet( new HierarchyListableComparator() );
		return transientChildren;
	}

	@Override
	public AmpComboboxDisplayable getParent() {
		return parentSectorId;
	}

	@Override
	public Collection getSiblings() {
		return sectors;
	}

	@Override
	public Collection getVisibleSiblings() {
		return getChildren();
	}

    public String getAdditionalSearchString() {
        return this.sectorCode;
    }

	

	
}
