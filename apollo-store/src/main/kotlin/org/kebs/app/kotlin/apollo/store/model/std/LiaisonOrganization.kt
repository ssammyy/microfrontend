package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name="SD_LIAISON_ORGANIZATION")
class LiaisonOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long =0

    @Column(name="NAME")
    @Basic
    var name:String? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiaisonOrganization

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "$name"
    }


}
