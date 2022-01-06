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
interface DataHolder {
    fun getId(): Long?
    fun getTc_Title(): String?
    fun getName(): String?
    fun getV1(): String?
    fun getV2(): String?
    fun getV3(): String?
    fun getV4(): String?
    fun getV5(): String?
    fun getV6(): String?

}
