package org.kebs.app.kotlin.apollo.api.controllers.diControllers

import org.kebs.app.kotlin.apollo.store.model.CdLaboratoryParametersEntity
import java.util.*


class DIListWrapper {
    private var labParameters: List<CdLaboratoryParametersEntity?>? = null

    fun getLabParameters(): List<CdLaboratoryParametersEntity?>? {
        return labParameters
    }

    fun setLabParameters(labParameters: List<CdLaboratoryParametersEntity?>?) {
        this.labParameters = labParameters
    }

    private var featureArrayList: ArrayList<CdLaboratoryParametersEntity> = ArrayList()

    fun featureWrapper() {}

    fun getFeatureArrayList(): ArrayList<CdLaboratoryParametersEntity>? {
        return featureArrayList
    }

    fun setFeatureArrayList(featureArrayList: ArrayList<CdLaboratoryParametersEntity>) {
        this.featureArrayList = featureArrayList
    }
}