//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.06.19 at 04:51:40 GMT+04:00 
//


package org.digijava.module.ampharvester.jaxb10;


/**
 * Java content class for locationType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/java/PowerDot/source/aida/src/conf/org/digijava/module/ampharvester/jaxb10/AMP.1.0.xsd line 60)
 * <p>
 * <pre>
 * &lt;complexType name="locationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commitment" type="{}fundingDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="disbursement" type="{}fundingDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="expenditure" type="{}fundingDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="countryName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="iso" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="iso3" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="region" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface LocationType {


    /**
     * Gets the value of the Commitment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Commitment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommitment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.ampharvester.jaxb10.FundingDetailType}
     * 
     */
    java.util.List getCommitment();

    /**
     * Gets the value of the countryName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCountryName();

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCountryName(java.lang.String value);

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRegion();

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRegion(java.lang.String value);

    /**
     * Gets the value of the Expenditure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Expenditure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExpenditure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.ampharvester.jaxb10.FundingDetailType}
     * 
     */
    java.util.List getExpenditure();

    /**
     * Gets the value of the iso property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIso();

    /**
     * Sets the value of the iso property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIso(java.lang.String value);

    /**
     * Gets the value of the Disbursement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Disbursement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisbursement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.ampharvester.jaxb10.FundingDetailType}
     * 
     */
    java.util.List getDisbursement();

    /**
     * Gets the value of the iso3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIso3();

    /**
     * Sets the value of the iso3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIso3(java.lang.String value);

}