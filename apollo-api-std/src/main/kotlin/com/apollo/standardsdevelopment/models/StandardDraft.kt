package com.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name="SD_STANDARD-DRAFT")
class StandardDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id: Long =0

    @Column(name="TITLE")
    @Basic
    var title:String?=null

    @Column(name="CD")
    @Basic
    var cd:String?= null


}