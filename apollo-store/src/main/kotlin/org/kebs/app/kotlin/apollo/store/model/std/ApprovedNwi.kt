package org.kebs.app.kotlin.apollo.store.model.std


//class DataHolder {
//    @NotNull
//    var id: Long =0
//
//    @NotNull
//    @Email
//     val name: String? = null
//
//    @NotNull
//    @Min(10)
//     val sub: String? = null
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as DataHolder
//
//        if (id != other.id) return false
//        if (name != other.name) return false
//        if (sub != other.sub) return false
//
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = id.hashCode()
//        result = 31 * result + (name?.hashCode() ?: 0)
//        result = 31 * result + (sub?.hashCode() ?: 0)
//
//        return result
//    }
//
//    override fun toString(): String {
//        return "DataHolder(id=$id, name=$name, sub=$sub)"
//    }
//}
interface ApprovedNwi {
    fun getWorkplan_Id(): Long?
    fun getStage_Code(): String?
    fun getStage_Date(): String?
    fun getStage_Month(): String?
    fun getSub_Stage(): String?
    fun getTarget_Date(): String?
    fun getTitle(): String?
    fun getRequest_No(): String?
    fun getStatus(): String?
    fun getId(): String?
    fun getCreated_On(): String?
    fun getDeleted_On(): String?
    fun getModified_On(): String?
    fun getName_Of_Proposer(): String?
    fun getProposal_Title(): String?
    fun getCirculation_Date(): String?
    fun getClosing_Date(): String?
    fun getDate_Of_Presentation(): String?
    fun getDraft_Attached(): String?
    fun getDraft_Outline_Impossible(): String?
    fun getLiason_Organization(): String?
    fun getName_Of_Tc(): String?
    fun getOrganization(): String?
    fun getOutline_Attached(): String?
    fun getOutline_Sent_Later(): String?
    fun getPurpose(): String?
    fun getReference_Number(): String?
    fun getScope(): String?
    fun getSimilar_Standards(): String?
    fun getTarget_Dateb(): String?
    fun getLiason_Organizationb(): String?


}
