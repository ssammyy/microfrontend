package com.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_PRODUCTS")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long =0

    @Column(name="NAME")
    @Basic
    var name:String? = null

    @Column(name="DESCRIPTION")
    @Basic
    var description:String? = null

    @Column(name="STATUS")
    @Basic
    var status:Int? = null

    @OneToMany
    @JoinColumn(name="PRODUCT_ID")
    var productSubCategory:Set<ProductSubCategory>?=null



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

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (description != other.description) return false
        if (status != other.status) return false
        if (productSubCategory != other.productSubCategory) return false
        if (varField1 != other.varField1) return false
        if (varField2 != other.varField2) return false
        if (varField3 != other.varField3) return false
        if (varField4 != other.varField4) return false
        if (varField5 != other.varField5) return false
        if (varField6 != other.varField6) return false
        if (varField7 != other.varField7) return false
        if (varField8 != other.varField8) return false
        if (varField9 != other.varField9) return false
        if (varField10 != other.varField10) return false
        if (createdBy != other.createdBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deleteBy != other.deleteBy) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (status ?: 0)
        result = 31 * result + (productSubCategory?.hashCode() ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)
        result = 31 * result + (varField10?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deleteBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Product(id=$id, name=$name, description=$description, status=$status, productSubCategory=$productSubCategory, varField1=$varField1, varField2=$varField2, varField3=$varField3, varField4=$varField4, varField5=$varField5, varField6=$varField6, varField7=$varField7, varField8=$varField8, varField9=$varField9, varField10=$varField10, createdBy=$createdBy, createdOn=$createdOn, modifiedBy=$modifiedBy, modifiedOn=$modifiedOn, deleteBy=$deleteBy, deletedOn=$deletedOn)"
    }


}
