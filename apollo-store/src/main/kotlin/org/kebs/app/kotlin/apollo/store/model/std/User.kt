package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name = "SD_USER")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long =0

    @Column(name = "NAME")
    @Basic
    var name: String?= null

    @Column(name = "PHONENUMBER")
    @Basic
    var phoneNumber:String?=null

    @Column(name = "EMAIL")
    @Basic
    var email:String?= null


    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<Role>? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (name != other.name) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (roles != other.roles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (roles?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(id=$id, name=$name, phoneNumber=$phoneNumber, email=$email, roles=$roles)"
    }


}
