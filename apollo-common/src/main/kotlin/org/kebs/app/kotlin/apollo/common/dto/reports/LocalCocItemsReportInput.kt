package org.kebs.app.kotlin.apollo.common.dto.reports

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource




class LocalCocItemsReportInput {

    var itemDataSource: JRBeanCollectionDataSource? = null

    fun getDataSources(): Map<String, JRBeanCollectionDataSource?> {
        val dataSources: MutableMap<String, JRBeanCollectionDataSource?> = HashMap()
        dataSources["itemDataSource"] = itemDataSource
        return dataSources
    }
}