/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_INSPECTION_GENERAL_FORM")
class InspectionGeneralFormEntity : Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "PRODUCT_COVERED")
    @Basic
    var productCovered: String? = null

    @Column(name = "BRANDS")
    @Basic
    var brands: String? = null

    @Column(name = "APPLICABLE_STANDARDS")
    @Basic
    var applicableStandards: String? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @JoinColumn(name = "MANUFACTURER_ID", referencedColumnName = "ID")
    @ManyToOne
    var manufacturerId: ManufacturersEntity? = null

//    @ManyToOne
//    var datKebsInspectionHaccpImplementationForm: InspectionHaccpImplementationFormEntity? = null

    @OneToMany(mappedBy = "datKebsInspectionGeneralFormByGeneralFormId")
    var datKebsInspectionLabTestsReportFormsById: MutableList<InspectionLabTestsReportFormEntity>? = mutableListOf()

    @OneToMany(mappedBy = "datKebsInspectionGeneralFormByGeneralFormId")
    var datKebsInspectionHaccpImplementationFormsById: MutableList<InspectionHaccpImplementationFormEntity>? = mutableListOf()


    @OneToMany(mappedBy = "datKebsInspectionGeneralFormByGeneralFormId")
    var datKebsInspectionOperationsProcessControlFormsById: MutableList<InspectionOperationsProcessControlFormEntity>? = mutableListOf()

    @OneToMany(mappedBy = "datKebsInspectionGeneralFormByGeneralFormId")
    var recomendationForms: MutableList<InspectionRecomendationFormEntity>? = mutableListOf()

    @OneToMany(mappedBy = "datKebsInspectionGeneralFormByGeneralFormId")
    var technicalForms: MutableList<InspectionTechnicalFormEntity>? = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as InspectionGeneralFormEntity
        if (id != that.id) return false
        if (if (productCovered != null) productCovered != that.productCovered else that.productCovered != null) return false
        if (if (brands != null) brands != that.brands else that.brands != null) return false
        if (if (applicableStandards != null) applicableStandards != that.applicableStandards else that.applicableStandards != null) return false
        if (if (varField1 != null) varField1 != that.varField1 else that.varField1 != null) return false
        if (if (varField2 != null) varField2 != that.varField2 else that.varField2 != null) return false
        if (if (varField3 != null) varField3 != that.varField3 else that.varField3 != null) return false
        if (if (varField4 != null) varField4 != that.varField4 else that.varField4 != null) return false
        if (if (varField5 != null) varField5 != that.varField5 else that.varField5 != null) return false
        if (if (varField6 != null) varField6 != that.varField6 else that.varField6 != null) return false
        if (if (varField7 != null) varField7 != that.varField7 else that.varField7 != null) return false
        if (if (varField8 != null) varField8 != that.varField8 else that.varField8 != null) return false
        if (if (varField9 != null) varField9 != that.varField9 else that.varField9 != null) return false
        if (if (varField10 != null) varField10 != that.varField10 else that.varField10 != null) return false
        if (if (createdBy != null) createdBy != that.createdBy else that.createdBy != null) return false
        if (if (createdOn != null) !createdOn!!.equals(that.createdOn) else that.createdOn != null) return false
        if (if (lastModifiedBy != null) lastModifiedBy != that.lastModifiedBy else that.lastModifiedBy != null) return false
        if (if (lastModifiedOn != null) !lastModifiedOn!!.equals(that.lastModifiedOn) else that.lastModifiedOn != null) return false
        if (if (updateBy != null) updateBy != that.updateBy else that.updateBy != null) return false
        if (if (updatedOn != null) !updatedOn!!.equals(that.updatedOn) else that.updatedOn != null) return false
        if (if (deleteBy != null) deleteBy != that.deleteBy else that.deleteBy != null) return false
        if (if (deletedOn != null) !deletedOn!!.equals(that.deletedOn) else that.deletedOn != null) return false
        return !if (version != null) version != that.version else that.version != null
    }

    override fun hashCode(): Int {
        var result = (id xor (id ushr 32)).toInt()
        result = 31 * result + if (productCovered != null) productCovered.hashCode() else 0
        result = 31 * result + if (brands != null) brands.hashCode() else 0
        result = 31 * result + if (applicableStandards != null) applicableStandards.hashCode() else 0
        result = 31 * result + if (varField1 != null) varField1.hashCode() else 0
        result = 31 * result + if (varField2 != null) varField2.hashCode() else 0
        result = 31 * result + if (varField3 != null) varField3.hashCode() else 0
        result = 31 * result + if (varField4 != null) varField4.hashCode() else 0
        result = 31 * result + if (varField5 != null) varField5.hashCode() else 0
        result = 31 * result + if (varField6 != null) varField6.hashCode() else 0
        result = 31 * result + if (varField7 != null) varField7.hashCode() else 0
        result = 31 * result + if (varField8 != null) varField8.hashCode() else 0
        result = 31 * result + if (varField9 != null) varField9.hashCode() else 0
        result = 31 * result + if (varField10 != null) varField10.hashCode() else 0
        result = 31 * result + if (createdBy != null) createdBy.hashCode() else 0
        result = 31 * result + if (createdOn != null) createdOn.hashCode() else 0
        result = 31 * result + if (lastModifiedBy != null) lastModifiedBy.hashCode() else 0
        result = 31 * result + if (lastModifiedOn != null) lastModifiedOn.hashCode() else 0
        result = 31 * result + if (updateBy != null) updateBy.hashCode() else 0
        result = 31 * result + if (updatedOn != null) updatedOn.hashCode() else 0
        result = 31 * result + if (deleteBy != null) deleteBy.hashCode() else 0
        result = 31 * result + if (deletedOn != null) deletedOn.hashCode() else 0
        result = 31 * result + if (version != null) version.hashCode() else 0
        return result
    }

}