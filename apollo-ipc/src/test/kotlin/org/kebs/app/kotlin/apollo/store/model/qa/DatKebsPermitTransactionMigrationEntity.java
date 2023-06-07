package org.kebs.app.kotlin.apollo.store.model.qa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "DAT_KEBS_PERMIT_TRANSACTION_MIGRATION", schema = "APOLLO", catalog = "")
public class DatKebsPermitTransactionMigrationEntity {
    @Basic
    @Column(name = "FIRM_ID", nullable = true, precision = 0)
    private Long firmId;
    @Basic
    @Column(name = "POSITION", nullable = true, length = 26)
    private String position;
    @Basic
    @Column(name = "VAT_NO", nullable = true, length = 26)
    private String vatNo;
    @Basic
    @Column(name = "ANNUAL_TURNOVER", nullable = true, length = 26)
    private String annualTurnover;
    @Basic
    @Column(name = "SUSPENSION_STATUS", nullable = true, length = 26)
    private String suspensionStatus;
    @Basic
    @Column(name = "DATE_OF_ISSUE", nullable = true)
    private Date dateOfIssue;
    @Basic
    @Column(name = "EFFECTIVE_DATE", nullable = true)
    private Date effectiveDate;
    @Basic
    @Column(name = "DATE_OF_VISIT", nullable = true, length = 26)
    private String dateOfVisit;
    @Basic
    @Column(name = "INSPECTOR_NAME", nullable = true, length = 26)
    private String inspectorName;
    @Basic
    @Column(name = "INSPECTOR_REMARKS", nullable = true, length = 26)
    private String inspectorRemarks;
    @Basic
    @Column(name = "KS_NUMBER", nullable = true, length = 128)
    private String ksNumber;
    @Basic
    @Column(name = "PERMIT_TYPE", nullable = true, length = 26)
    private String permitType;
    @Basic
    @Column(name = "PRODUCT_NAME", nullable = true, length = 128)
    private String productName;
    @Basic
    @Column(name = "TITLE", nullable = true, length = 1024)
    private String title;
    @Basic
    @Column(name = "TOTAL_COST", nullable = true, length = 26)
    private String totalCost;
    @Basic
    @Column(name = "TOTAL_PAYMENT", nullable = true, length = 26)
    private String totalPayment;
    @Basic
    @Column(name = "TRADE_MARK", nullable = true, length = 128)
    private String tradeMark;
    @Basic
    @Column(name = "USER_ID", nullable = true, length = 26)
    private String userId;
    @Basic
    @Column(name = "PERMIT_NUMBER", nullable = true, length = 26)
    private String permitNumber;
    @Basic
    @Column(name = "DATE_OF_EXPIRY", nullable = true)
    private Date dateOfExpiry;
    @Basic
    @Column(name = "COMMODITY_DESCRIPTION", nullable = true, length = 26)
    private String commodityDescription;
    @Basic
    @Column(name = "ENABLED", nullable = true, length = 26)
    private String enabled;
    @Basic
    @Column(name = "BROAD_PRODUCT_CATEGORY", nullable = true, length = 26)
    private String broadProductCategory;
    @Basic
    @Column(name = "PRODUCT_CATEGORY", nullable = true, length = 26)
    private String productCategory;
    @Basic
    @Column(name = "PRODUCT", nullable = true, length = 26)
    private String product;
    @Basic
    @Column(name = "PRODUCT_SUB_CATEGORY", nullable = true, length = 26)
    private String productSubCategory;
    @Basic
    @Column(name = "STANDARD_CATEGORY", nullable = true, length = 26)
    private String standardCategory;
    @Basic
    @Column(name = "PRODUCT_STANDARD", nullable = true, length = 26)
    private String productStandard;
    @Basic
    @Column(name = "PERMIT_FOREIGN_STATUS", nullable = true, length = 26)
    private String permitForeignStatus;
    @Basic
    @Column(name = "END_OF_PRODUCTION_STATUS", nullable = true, length = 26)
    private String endOfProductionStatus;
    @Basic
    @Column(name = "DIVISION_ID", nullable = true, length = 26)
    private String divisionId;
    @Basic
    @Column(name = "SECTION_ID", nullable = true, length = 26)
    private String sectionId;
    @Basic
    @Column(name = "PERMIT_EXPIRED_STATUS", nullable = true, length = 26)
    private String permitExpiredStatus;
    @Basic
    @Column(name = "RENEWAL_STATUS", nullable = true, length = 26)
    private String renewalStatus;
    @Basic
    @Column(name = "COMPANY_NAME", nullable = true, length = 128)
    private String companyName;
    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 128)
    private String address;
    @Basic
    @Column(name = "POSTAL_CODE", nullable = true, length = 128)
    private String postalCode;
    @Basic
    @Column(name = "PHONE", nullable = true, length = 50)
    private String phone;
    @Basic
    @Column(name = "FAX", nullable = true, length = 26)
    private String fax;
    @Basic
    @Column(name = "EMAIL", nullable = true, length = 128)
    private String email;
    @Basic
    @Column(name = "PHYSICAL_ADDRESS", nullable = true, length = 128)
    private String physicalAddress;
    @Basic
    @Column(name = "PIN_NO", nullable = true, length = 26)
    private String pinNo;
    @Basic
    @Column(name = "VAT_NO2", nullable = true, length = 26)
    private String vatNo2;
    @Basic
    @Column(name = "REGION", nullable = true, length = 26)
    private String region;
    @Basic
    @Column(name = "TYPE", nullable = true, length = 26)
    private String type;
    @Basic
    @Column(name = "MIGRATED_STATUS", nullable = true, precision = 0)
    private BigInteger migratedStatus;
    @Basic
    @Column(name = "MIGRATION_REMARKS", nullable = true, length = 3500)
    private String migrationRemarks;
    @Basic
    @Column(name = "MIGRATION_BY", nullable = true, precision = 0)
    private BigInteger migrationBy;
    @Basic
    @Column(name = "MIGRATION_ON", nullable = true, length = 3500)
    private String migrationOn;

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public String getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(String annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public String getSuspensionStatus() {
        return suspensionStatus;
    }

    public void setSuspensionStatus(String suspensionStatus) {
        this.suspensionStatus = suspensionStatus;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getInspectorRemarks() {
        return inspectorRemarks;
    }

    public void setInspectorRemarks(String inspectorRemarks) {
        this.inspectorRemarks = inspectorRemarks;
    }

    public String getKsNumber() {
        return ksNumber;
    }

    public void setKsNumber(String ksNumber) {
        this.ksNumber = ksNumber;
    }

    public String getPermitType() {
        return permitType;
    }

    public void setPermitType(String permitType) {
        this.permitType = permitType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getBroadProductCategory() {
        return broadProductCategory;
    }

    public void setBroadProductCategory(String broadProductCategory) {
        this.broadProductCategory = broadProductCategory;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getStandardCategory() {
        return standardCategory;
    }

    public void setStandardCategory(String standardCategory) {
        this.standardCategory = standardCategory;
    }

    public String getProductStandard() {
        return productStandard;
    }

    public void setProductStandard(String productStandard) {
        this.productStandard = productStandard;
    }

    public String getPermitForeignStatus() {
        return permitForeignStatus;
    }

    public void setPermitForeignStatus(String permitForeignStatus) {
        this.permitForeignStatus = permitForeignStatus;
    }

    public String getEndOfProductionStatus() {
        return endOfProductionStatus;
    }

    public void setEndOfProductionStatus(String endOfProductionStatus) {
        this.endOfProductionStatus = endOfProductionStatus;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getPermitExpiredStatus() {
        return permitExpiredStatus;
    }

    public void setPermitExpiredStatus(String permitExpiredStatus) {
        this.permitExpiredStatus = permitExpiredStatus;
    }

    public String getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPinNo() {
        return pinNo;
    }

    public void setPinNo(String pinNo) {
        this.pinNo = pinNo;
    }

    public String getVatNo2() {
        return vatNo2;
    }

    public void setVatNo2(String vatNo2) {
        this.vatNo2 = vatNo2;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigInteger getMigratedStatus() {
        return migratedStatus;
    }

    public void setMigratedStatus(BigInteger migratedStatus) {
        this.migratedStatus = migratedStatus;
    }

    public String getMigrationRemarks() {
        return migrationRemarks;
    }

    public void setMigrationRemarks(String migrationRemarks) {
        this.migrationRemarks = migrationRemarks;
    }

    public BigInteger getMigrationBy() {
        return migrationBy;
    }

    public void setMigrationBy(BigInteger migrationBy) {
        this.migrationBy = migrationBy;
    }

    public String getMigrationOn() {
        return migrationOn;
    }

    public void setMigrationOn(String migrationOn) {
        this.migrationOn = migrationOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatKebsPermitTransactionMigrationEntity that = (DatKebsPermitTransactionMigrationEntity) o;
        return Objects.equals(firmId, that.firmId) && Objects.equals(position, that.position) && Objects.equals(vatNo, that.vatNo) && Objects.equals(annualTurnover, that.annualTurnover) && Objects.equals(suspensionStatus, that.suspensionStatus) && Objects.equals(dateOfIssue, that.dateOfIssue) && Objects.equals(effectiveDate, that.effectiveDate) && Objects.equals(dateOfVisit, that.dateOfVisit) && Objects.equals(inspectorName, that.inspectorName) && Objects.equals(inspectorRemarks, that.inspectorRemarks) && Objects.equals(ksNumber, that.ksNumber) && Objects.equals(permitType, that.permitType) && Objects.equals(productName, that.productName) && Objects.equals(title, that.title) && Objects.equals(totalCost, that.totalCost) && Objects.equals(totalPayment, that.totalPayment) && Objects.equals(tradeMark, that.tradeMark) && Objects.equals(userId, that.userId) && Objects.equals(permitNumber, that.permitNumber) && Objects.equals(dateOfExpiry, that.dateOfExpiry) && Objects.equals(commodityDescription, that.commodityDescription) && Objects.equals(enabled, that.enabled) && Objects.equals(broadProductCategory, that.broadProductCategory) && Objects.equals(productCategory, that.productCategory) && Objects.equals(product, that.product) && Objects.equals(productSubCategory, that.productSubCategory) && Objects.equals(standardCategory, that.standardCategory) && Objects.equals(productStandard, that.productStandard) && Objects.equals(permitForeignStatus, that.permitForeignStatus) && Objects.equals(endOfProductionStatus, that.endOfProductionStatus) && Objects.equals(divisionId, that.divisionId) && Objects.equals(sectionId, that.sectionId) && Objects.equals(permitExpiredStatus, that.permitExpiredStatus) && Objects.equals(renewalStatus, that.renewalStatus) && Objects.equals(companyName, that.companyName) && Objects.equals(address, that.address) && Objects.equals(postalCode, that.postalCode) && Objects.equals(phone, that.phone) && Objects.equals(fax, that.fax) && Objects.equals(email, that.email) && Objects.equals(physicalAddress, that.physicalAddress) && Objects.equals(pinNo, that.pinNo) && Objects.equals(vatNo2, that.vatNo2) && Objects.equals(region, that.region) && Objects.equals(type, that.type) && Objects.equals(migratedStatus, that.migratedStatus) && Objects.equals(migrationRemarks, that.migrationRemarks) && Objects.equals(migrationBy, that.migrationBy) && Objects.equals(migrationOn, that.migrationOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firmId, position, vatNo, annualTurnover, suspensionStatus, dateOfIssue, effectiveDate, dateOfVisit, inspectorName, inspectorRemarks, ksNumber, permitType, productName, title, totalCost, totalPayment, tradeMark, userId, permitNumber, dateOfExpiry, commodityDescription, enabled, broadProductCategory, productCategory, product, productSubCategory, standardCategory, productStandard, permitForeignStatus, endOfProductionStatus, divisionId, sectionId, permitExpiredStatus, renewalStatus, companyName, address, postalCode, phone, fax, email, physicalAddress, pinNo, vatNo2, region, type, migratedStatus, migrationRemarks, migrationBy, migrationOn);
    }
}
