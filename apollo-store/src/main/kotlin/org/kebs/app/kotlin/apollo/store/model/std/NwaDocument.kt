package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name="SD_NWA_DOCUMENTS_TBL")
class NwaDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:String = ""

    @Column(name="NAME")
    @Basic
    var name:String =""

    @Column(name="TYPE")
    @Basic
    var type:String = ""

    @Column(name="ITEM_ID")
    @Basic
    var itemId:String? =null

    @Column(name="GROUP_ID")
    @Basic
    var groupId:String? =null

    @Lob
    lateinit var data: ByteArray

}
