package org.kebs.app.kotlin.apollo.store.model.invoice;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "CFG_KEBS_TABLE_SOURCES", schema = "APOLLO", catalog = "")
public class CfgKebsTableSourcesEntity {
    private long id;
    private String tableName;
    private String description;
    private Long status;
    private String varField1;
    private String varField2;
    private String varField3;
    private String varField4;
    private String varField5;
    private String varField6;
    private String varField7;
    private String varField8;
    private String varField9;
    private String varField10;
    private String createdBy;
    private Timestamp createdOn;
    private String modifiedBy;
    private Timestamp modifiedOn;
    private String deleteBy;
    private Timestamp deletedOn;

    @Id
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TABLE_NAME")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Basic
    @Column(name = "VAR_FIELD_1")
    public String getVarField1() {
        return varField1;
    }

    public void setVarField1(String varField1) {
        this.varField1 = varField1;
    }

    @Basic
    @Column(name = "VAR_FIELD_2")
    public String getVarField2() {
        return varField2;
    }

    public void setVarField2(String varField2) {
        this.varField2 = varField2;
    }

    @Basic
    @Column(name = "VAR_FIELD_3")
    public String getVarField3() {
        return varField3;
    }

    public void setVarField3(String varField3) {
        this.varField3 = varField3;
    }

    @Basic
    @Column(name = "VAR_FIELD_4")
    public String getVarField4() {
        return varField4;
    }

    public void setVarField4(String varField4) {
        this.varField4 = varField4;
    }

    @Basic
    @Column(name = "VAR_FIELD_5")
    public String getVarField5() {
        return varField5;
    }

    public void setVarField5(String varField5) {
        this.varField5 = varField5;
    }

    @Basic
    @Column(name = "VAR_FIELD_6")
    public String getVarField6() {
        return varField6;
    }

    public void setVarField6(String varField6) {
        this.varField6 = varField6;
    }

    @Basic
    @Column(name = "VAR_FIELD_7")
    public String getVarField7() {
        return varField7;
    }

    public void setVarField7(String varField7) {
        this.varField7 = varField7;
    }

    @Basic
    @Column(name = "VAR_FIELD_8")
    public String getVarField8() {
        return varField8;
    }

    public void setVarField8(String varField8) {
        this.varField8 = varField8;
    }

    @Basic
    @Column(name = "VAR_FIELD_9")
    public String getVarField9() {
        return varField9;
    }

    public void setVarField9(String varField9) {
        this.varField9 = varField9;
    }

    @Basic
    @Column(name = "VAR_FIELD_10")
    public String getVarField10() {
        return varField10;
    }

    public void setVarField10(String varField10) {
        this.varField10 = varField10;
    }

    @Basic
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CREATED_ON")
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Basic
    @Column(name = "MODIFIED_BY")
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Basic
    @Column(name = "MODIFIED_ON")
    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Basic
    @Column(name = "DELETE_BY")
    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    @Basic
    @Column(name = "DELETED_ON")
    public Timestamp getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Timestamp deletedOn) {
        this.deletedOn = deletedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgKebsTableSourcesEntity that = (CfgKebsTableSourcesEntity) o;
        return id == that.id &&
                Objects.equals(tableName, that.tableName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(status, that.status) &&
                Objects.equals(varField1, that.varField1) &&
                Objects.equals(varField2, that.varField2) &&
                Objects.equals(varField3, that.varField3) &&
                Objects.equals(varField4, that.varField4) &&
                Objects.equals(varField5, that.varField5) &&
                Objects.equals(varField6, that.varField6) &&
                Objects.equals(varField7, that.varField7) &&
                Objects.equals(varField8, that.varField8) &&
                Objects.equals(varField9, that.varField9) &&
                Objects.equals(varField10, that.varField10) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(modifiedBy, that.modifiedBy) &&
                Objects.equals(modifiedOn, that.modifiedOn) &&
                Objects.equals(deleteBy, that.deleteBy) &&
                Objects.equals(deletedOn, that.deletedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableName, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn);
    }
}
