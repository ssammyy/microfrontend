//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.20 at 04:35:28 AM EAT 
//
package org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.kraPinIntegrations

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

/**
 *
 * Java class for receiveApprovedExmpOAFPDtls complex type.
 *
 *
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="receiveApprovedExmpOAFPDtls"&gt;
 * &lt;complexContent&gt;
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 * &lt;sequence&gt;
 * &lt;element name="loginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 * &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 * &lt;element name="approvedExmpOAFPData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 * &lt;/sequence&gt;
 * &lt;/restriction&gt;
 * &lt;/complexContent&gt;
 * &lt;/complexType&gt;
</pre> *
 *
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiveApprovedExmpOAFPDtls", propOrder = ["loginId", "password", "approvedExmpOAFPData"])
class ReceiveApprovedExmpOAFPDtls {
    /**
     * Gets the value of the loginId property.
     *
     * @return
     * possible object is
     * [String]
     */
    /**
     * Sets the value of the loginId property.
     *
     * @param value
     * allowed object is
     * [String]
     */
    var loginId: String? = null
    /**
     * Gets the value of the password property.
     *
     * @return
     * possible object is
     * [String]
     */
    /**
     * Sets the value of the password property.
     *
     * @param value
     * allowed object is
     * [String]
     */
    var password: String? = null
    /**
     * Gets the value of the approvedExmpOAFPData property.
     *
     * @return
     * possible object is
     * [String]
     */
    /**
     * Sets the value of the approvedExmpOAFPData property.
     *
     * @param value
     * allowed object is
     * [String]
     */
    var approvedExmpOAFPData: String? = null
}