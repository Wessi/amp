package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.springframework.transaction.annotation.Transactional;

@TranslatableClass
public class AmpAgreement implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;
	@TranslatableField
	private String title;
	private Date effectiveDate;
	private Date signatureDate;
	private Date closeDate;
	
	public AmpAgreement() {
		super();
	}
	
	public AmpAgreement(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getSignatureDate() {
		return signatureDate;
	}
	public void setSignatureDate(Date signatureDate) {
		this.signatureDate = signatureDate;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
}
