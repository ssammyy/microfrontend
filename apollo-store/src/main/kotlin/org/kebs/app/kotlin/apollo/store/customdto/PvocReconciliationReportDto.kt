package org.kebs.app.kotlin.apollo.store.customdto

interface PvocReconciliationReportDto {
    val month : String
    val inspectionfeesum: Long?

    val verificationfeesum: Long?

    val royaltiestokebssum: Long?
    val penaltySum : Long?
}