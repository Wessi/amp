package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.SectorUtil;

public class AmpSectorScheme implements Serializable
{
	private Long ampSecSchemeId ;
	private String secSchemeCode ;
	private String secSchemeName ;
	
	/**
	 * @return
	 */
	public Long getAmpSecSchemeId() {
		return ampSecSchemeId;
	}

	/**
	 * @return
	 */
	public String getSecSchemeCode() {
		return secSchemeCode;
	}

	/**
	 * @return
	 */
	public String getSecSchemeName() {
		return secSchemeName;
	}

	/**
	 * @param long1
	 */
	public void setAmpSecSchemeId(Long long1) {
		ampSecSchemeId = long1;
	}

	/**
	 * @param string
	 */
	public void setSecSchemeCode(String string) {
		secSchemeCode = string;
	}

	/**
	 * @param string
	 */
	public void setSecSchemeName(String string) {
		secSchemeName = string;
	}
        
       public boolean isUsed() {
        boolean used = true;
        try {
            used = SectorUtil.isClassificationUsed(ampSecSchemeId);

        } catch (DgException ex) {
            ex.printStackTrace();

        }
        return used;
    }

}
