package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintRecommendationEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintCertificationSubCategoriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintEntity

class PvocComplaintDao {
    var complaintId: Long? = null
    var refNo: String? = null
    var complaintName: String? = null
    var phoneNo: String? = null
    var address: String? = null
    var cocNo: String? = null
    var rfcNo: String? = null
    var email: String? = null
    var complaintDescription: String? = null
    var pvocAgent: String? = null
    var pvocAgentId: Long? = null
    var categoryId: Long? = null
    var categoryName: String? = null
    var subCategoryId: Long? = null
    var subCategoryName: String? = null
    var recommendationId: Long? = null
    var recommendationAction: String? = null
    var recommendationDesc: String? = null

    companion object {
        fun fromEntity(complaint: PvocComplaintEntity): PvocComplaintDao {
            return PvocComplaintDao().apply {
                complaintId = complaint.id
                refNo = complaint.refNo
                rfcNo = complaint.rfcNo
                complaintName = complaint.complaintName
                phoneNo = complaint.phoneNo
                address = complaint.address
                cocNo = complaint.cocNo
                email = complaint.email
                complaintDescription = complaint.generalDescription
                pvocAgent = complaint.pvocAgent?.partnerName
                pvocAgentId = complaint.pvocAgent?.id
                categoryId = complaint.compliantNature?.id
                categoryName = complaint.compliantNature?.name
                subCategoryId = complaint.compliantSubCategory?.id
                subCategoryName = complaint.compliantSubCategory?.name
                recommendationId = complaint.recomendation?.id
                recommendationAction = complaint.recomendation?.action
                recommendationDesc = complaint.recomendation?.description
            }
        }

        fun fromList(complaints: List<PvocComplaintEntity>): List<PvocComplaintDao> {
            val daos = mutableListOf<PvocComplaintDao>()
            complaints.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocComplaintSubCategoryDao {
    var categoryId: Long? = null
    var name: String? = null

    companion object {
        fun fromEntity(category: PvocComplaintCertificationSubCategoriesEntity): PvocComplaintSubCategoryDao {
            return PvocComplaintSubCategoryDao().apply {
                categoryId = category.id
                name = category.name
            }
        }

        fun fromList(categories: List<PvocComplaintCertificationSubCategoriesEntity>): List<PvocComplaintSubCategoryDao> {
            val daos = mutableListOf<PvocComplaintSubCategoryDao>()
            categories.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocComplaintCategoryDao {
    var categoryId: Long? = null
    var name: String? = null
    var subCategories: List<PvocComplaintSubCategoryDao>? = null

    companion object {
        fun fromEntity(category: PvocComplaintCategoryEntity): PvocComplaintCategoryDao {
            val dao = PvocComplaintCategoryDao().apply {
                categoryId = category.id
                name = category.name
            }
            category.pvocComplaintCertificationSubCategoriesById?.let {
                dao.subCategories = PvocComplaintSubCategoryDao.fromList(it)
            }
            return dao
        }

        fun fromList(categories: List<PvocComplaintCategoryEntity>): List<PvocComplaintCategoryDao> {
            val daos = mutableListOf<PvocComplaintCategoryDao>()
            categories.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocComplaintRecommendationDao {
    var recommendationId: Long? = null
    var action: String? = null
    var description: String? = null

    companion object {
        fun fromEntity(recommendation: PvocComplaintRecommendationEntity): PvocComplaintRecommendationDao {
            val dao = PvocComplaintRecommendationDao().apply {
                recommendationId = recommendation.id
                action = recommendation.action
                description = recommendation.description
            }
            return dao
        }

        fun fromList(complaints: List<PvocComplaintRecommendationEntity>): List<PvocComplaintRecommendationDao> {
            val daos = mutableListOf<PvocComplaintRecommendationDao>()
            complaints.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}