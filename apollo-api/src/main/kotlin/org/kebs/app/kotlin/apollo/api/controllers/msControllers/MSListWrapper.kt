package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import org.kebs.app.kotlin.apollo.store.model.MsDataReportParametersEntity
import java.util.*


class MSListWrapper {
    private var parameters: List<MsDataReportParametersEntity?>? = null

    fun getParameters(): List<MsDataReportParametersEntity?>? {
        return parameters
    }

    fun setParameters(parameters: List<MsDataReportParametersEntity?>?) {
        this.parameters = parameters
    }

    private var featureArrayList: ArrayList<MsDataReportParametersEntity> = ArrayList()

    fun featureWrapper() {}

    fun getFeatureArrayList(): ArrayList<MsDataReportParametersEntity>? {
        return featureArrayList
    }

    fun setFeatureArrayList(featureArrayList: ArrayList<MsDataReportParametersEntity>) {
        this.featureArrayList = featureArrayList
    }
}