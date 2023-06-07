package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name = "SD_ROLES")
class Role {
    @Id
    @GeneratedValue
    var role_id: Long =0

    var role: String?=null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (role_id != other.role_id) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = role_id.hashCode()
        result = 31 * result + (role?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Role(role_id=$role_id, role=$role)"
    }


}
