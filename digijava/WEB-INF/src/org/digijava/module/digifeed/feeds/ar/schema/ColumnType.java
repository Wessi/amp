//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.20 at 12:51:52 PM IST 
//


package org.digijava.module.digifeed.feeds.ar.schema;


/**
 * Java content class for columnType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/mihai/programs/jwsdp-1.6/jaxb/bin/schema.xsd line 43)
 * <p>
 * <pre>
 * &lt;complexType name="columnType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ColumnType {


    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getOrder();

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setOrder(java.math.BigInteger value);

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getId();

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setId(java.math.BigInteger value);

}