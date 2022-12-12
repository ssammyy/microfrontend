package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SL_ENTRY_NO_DATA_MIGRATION")
class StdLevyEntryNoDataMigrationEntity : Serializable{
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "SL_ENTRY_NO_DATA_MIGRATION_SEQ_GEN", sequenceName = "SL_ENTRY_NO_DATA_MIGRATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "SL_ENTRY_NO_DATA_MIGRATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "MANUFACTURER")
    @Basic
    var manufacturer: String? = null

    @Column(name = "REGISTRATION_NUMBER")
    @Basic
    var registrationNumber: String? = null

    @Column(name = "DIRECTOR_ID")
    @Basic
    var directorId: String? = null

    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null


}