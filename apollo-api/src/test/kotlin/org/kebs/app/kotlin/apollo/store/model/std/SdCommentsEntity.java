package org.kebs.app.kotlin.apollo.store.model.std;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "SD_COMMENTS", schema = "APOLLO", catalog = "")
public class SdCommentsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "USER_ID", nullable = false, precision = 0)
    private BigInteger userId;
    @Basic
    @Column(name = "TITLE", nullable = true, length = 350)
    private String title;
    @Basic
    @Column(name = "DOCUMENT_TYPE", nullable = true, length = 350)
    private String documentType;
    @Basic
    @Column(name = "CIRCULATION_DATE", nullable = true)
    private Timestamp circulationDate;
    @Basic
    @Column(name = "CLOSING_DATE", nullable = true)
    private Timestamp closingDate;
    @Basic
    @Column(name = "RECIPIENT_ID", nullable = false, precision = 0)
    private BigInteger recipientId;
    @Basic
    @Column(name = "ORGANIZATION", nullable = true, length = 350)
    private String organization;
    @Basic
    @Column(name = "CLAUSE", nullable = true, length = 350)
    private String clause;
    @Basic
    @Column(name = "PARAGRAPH", nullable = true, length = 350)
    private String paragraph;
    @Basic
    @Column(name = "COMMENT_TYPE", nullable = true, length = 350)
    private String commentType;
    @Basic
    @Column(name = "PROPOSED_CHANGE", nullable = true, length = 350)
    private String proposedChange;
    @Basic
    @Column(name = "OBSERVATION", nullable = true, length = 350)
    private String observation;
    @Basic
    @Column(name = "STATUS", nullable = false, precision = 0)
    private byte status;
    @Basic
    @Column(name = "CREATED_BY", nullable = true, length = 100)
    private String createdBy;
    @Basic
    @Column(name = "CREATED_ON", nullable = true)
    private Timestamp createdOn;
    @Basic
    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    private String modifiedBy;
    @Basic
    @Column(name = "MODIFIED_ON", nullable = true)
    private Timestamp modifiedOn;
    @Basic
    @Column(name = "DELETE_BY", nullable = true, length = 100)
    private String deleteBy;
    @Basic
    @Column(name = "DELETED_ON", nullable = true)
    private Timestamp deletedOn;
    @Basic
    @Column(name = "PD_ID", nullable = true, length = 350)
    private String pdId;
    @Basic
    @Column(name = "CD_ID", nullable = true, length = 350)
    private String cdId;
    @Basic
    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    private String varField3;
    @Basic
    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    private String varField4;
    @Basic
    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    private String varField5;
    @Basic
    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    private String varField6;
    @Basic
    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    private String varField7;
    @Basic
    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    private String varField8;
    @Basic
    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    private String varField9;
    @Basic
    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    private String varField10;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Timestamp getCirculationDate() {
        return circulationDate;
    }

    public void setCirculationDate(Timestamp circulationDate) {
        this.circulationDate = circulationDate;
    }

    public Timestamp getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Timestamp closingDate) {
        this.closingDate = closingDate;
    }

    public BigInteger getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(BigInteger recipientId) {
        this.recipientId = recipientId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getProposedChange() {
        return proposedChange;
    }

    public void setProposedChange(String proposedChange) {
        this.proposedChange = proposedChange;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Timestamp getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Timestamp deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getPdId() {
        return pdId;
    }

    public void setPdId(String pdId) {
        this.pdId = pdId;
    }

    public String getCdId() {
        return cdId;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }

    public String getVarField3() {
        return varField3;
    }

    public void setVarField3(String varField3) {
        this.varField3 = varField3;
    }

    public String getVarField4() {
        return varField4;
    }

    public void setVarField4(String varField4) {
        this.varField4 = varField4;
    }

    public String getVarField5() {
        return varField5;
    }

    public void setVarField5(String varField5) {
        this.varField5 = varField5;
    }

    public String getVarField6() {
        return varField6;
    }

    public void setVarField6(String varField6) {
        this.varField6 = varField6;
    }

    public String getVarField7() {
        return varField7;
    }

    public void setVarField7(String varField7) {
        this.varField7 = varField7;
    }

    public String getVarField8() {
        return varField8;
    }

    public void setVarField8(String varField8) {
        this.varField8 = varField8;
    }

    public String getVarField9() {
        return varField9;
    }

    public void setVarField9(String varField9) {
        this.varField9 = varField9;
    }

    public String getVarField10() {
        return varField10;
    }

    public void setVarField10(String varField10) {
        this.varField10 = varField10;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SdCommentsEntity that = (SdCommentsEntity) o;
        return status == that.status && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(title, that.title) && Objects.equals(documentType, that.documentType) && Objects.equals(circulationDate, that.circulationDate) && Objects.equals(closingDate, that.closingDate) && Objects.equals(recipientId, that.recipientId) && Objects.equals(organization, that.organization) && Objects.equals(clause, that.clause) && Objects.equals(paragraph, that.paragraph) && Objects.equals(commentType, that.commentType) && Objects.equals(proposedChange, that.proposedChange) && Objects.equals(observation, that.observation) && Objects.equals(createdBy, that.createdBy) && Objects.equals(createdOn, that.createdOn) && Objects.equals(modifiedBy, that.modifiedBy) && Objects.equals(modifiedOn, that.modifiedOn) && Objects.equals(deleteBy, that.deleteBy) && Objects.equals(deletedOn, that.deletedOn) && Objects.equals(pdId, that.pdId) && Objects.equals(cdId, that.cdId) && Objects.equals(varField3, that.varField3) && Objects.equals(varField4, that.varField4) && Objects.equals(varField5, that.varField5) && Objects.equals(varField6, that.varField6) && Objects.equals(varField7, that.varField7) && Objects.equals(varField8, that.varField8) && Objects.equals(varField9, that.varField9) && Objects.equals(varField10, that.varField10);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, documentType, circulationDate, closingDate, recipientId, organization, clause, paragraph, commentType, proposedChange, observation, status, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, pdId, cdId, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10);
    }
}
