package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp


//class DataHolder {
//    @NotNull
//    var id: Long =0
//
//    @NotNull
//    @Email
//     val name: String? = null
//
//    @NotNull
//    @Min(10)
//     val sub: String? = null
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as DataHolder
//
//        if (id != other.id) return false
//        if (name != other.name) return false
//        if (sub != other.sub) return false
//
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = id.hashCode()
//        result = 31 * result + (name?.hashCode() ?: 0)
//        result = 31 * result + (sub?.hashCode() ?: 0)
//
//        return result
//    }
//
//    override fun toString(): String {
//        return "DataHolder(id=$id, name=$name, sub=$sub)"
//    }
//}
interface ApprovedNwi {
    fun getWorkplan_Id(): Long?
    fun getStage_Code(): String?
    fun getStage_Date(): String?
    fun getStage_Month(): String?
    fun getSub_Stage(): String?
    fun getTarget_Date(): String?
    fun getTitle(): String?
    fun getRequest_No(): String?
    fun getStatus(): String?
    fun getId(): String?
    fun getCreated_On(): String?
    fun getDeleted_On(): String?
    fun getModified_On(): String?
    fun getName_Of_Proposer(): String?
    fun getProposal_Title(): String?
    fun getCirculation_Date(): String?
    fun getClosing_Date(): String?
    fun getDate_Of_Presentation(): String?
    fun getDraft_Attached(): String?
    fun getDraft_Outline_Impossible(): String?
    fun getLiason_Organization(): String?
    fun getName_Of_Tc(): String?
    fun getOrganization(): String?
    fun getOutline_Attached(): String?
    fun getOutline_Sent_Later(): String?
    fun getPurpose(): String?
    fun getReference_Number(): String?
    fun getScope(): String?
    fun getSimilar_Standards(): String?
    fun getTarget_Dateb(): String?
    fun getLiason_Organizationb(): String?


}

interface CommentsWithPdId {
    fun getCommentsId(): Long?
    fun getTitle(): String?
    fun getDocument_type(): String?

    fun getCIRCULATION_date(): String?

    fun getCLOSING_date(): String?
    fun getSTATUS(): Long?
    fun getORGANIZATION(): String?
    fun getCLAUSE(): String?
    fun getPARAGRAPH(): String?
    fun getCOMMENT_type(): String?
    fun getPROPOSED_change(): String?
    fun getOBSERVATION(): String?
    fun getCREATED_on(): String?
    fun getCOMMENTS_made(): String?
    fun getPD_name(): String?
    fun getCOMMENT_by(): String?
    fun getRECEIVED_by(): String?

    fun getRECIPIENT_id(): Long?

}

interface PdWithUserName {
    fun getId(): Long?
    fun getNWI_Id(): String?
    fun getPD_by(): String?

    fun getPD_name(): String?

    fun getCREATED_on(): String?

    fun getSTATUS(): String?

    fun getNUMBER_OF_COMMENTS(): String?


}

interface CdWithUserName {
    fun getCDID(): Long
    fun getPDID(): Long
    fun getCDBY(): String?
    fun getCDNAME(): String?
    fun getCREATEDON(): String?
    fun getSTATUS(): String?
    fun getAPPROVALSTATUS(): String?

    fun getNUMBEROFCOMMENTS(): String?
    fun getKS_NUMBER(): String?

}

interface CommentsWithCdId {
    fun getCommentsId(): Long?
    fun getTitle(): String?
    fun getDocument_type(): String?

    fun getCIRCULATION_date(): String?

    fun getCLOSING_date(): String?
    fun getSTATUS(): Long?
    fun getORGANIZATION(): String?
    fun getCLAUSE(): String?
    fun getPARAGRAPH(): String?
    fun getCOMMENT_type(): String?
    fun getPROPOSED_change(): String?
    fun getOBSERVATION(): String?
    fun getCREATED_on(): String?
    fun getCOMMENTS_made(): String?
    fun getCD_name(): String?
    fun getCOMMENT_by(): String?
    fun getRECEIVED_by(): String?

    fun getRECIPIENT_id(): Long?


}

interface PrdWithUserName {
    fun getId(): Long?
    fun getCD_Id(): String?
    fun getPRD_by(): String?

    fun getPRD_name(): String?

    fun getCREATED_on(): String?

    fun getSTATUS(): String?

    fun getNUMBER_OF_COMMENTS(): String?
    fun getKS_NUMBER(): String?

    fun getVAR_FIELD_1(): String?

}

interface CommentsWithPrdId {
    fun getCommentsId(): Long?
    fun getTitle(): String?
    fun getDocument_type(): String?

    fun getCIRCULATION_date(): String?

    fun getCLOSING_date(): String?
    fun getSTATUS(): Long?
    fun getORGANIZATION(): String?
    fun getCLAUSE(): String?
    fun getPARAGRAPH(): String?
    fun getCOMMENT_type(): String?
    fun getPROPOSED_change(): String?
    fun getOBSERVATION(): String?
    fun getCREATED_on(): String?
    fun getCOMMENTS_made(): String?
    fun getPRD_name(): String?
    fun getCOMMENT_by(): String?
    fun getRECEIVED_by(): String?

    fun getRECIPIENT_id(): Long?

    fun getUNLOGGEDINUSEREMAIL(): String?

    fun getUNLOGGEDINUSERNAMES(): String?

    fun getUNLOGGEDINUSERPHONE(): String?


}

interface BallotWithUserName {
    fun getId(): Long?
    fun getPRD_id(): String?

    fun getBallot_name(): String?

    fun getBallot_Draft_By(): String?

    fun getCREATED_on(): String?

    fun getSTATUS(): String?

    fun getNUMBER_OF_COMMENTS(): String?

    fun getVAR_FIELD_1(): String?

}

interface VotesWithBallotId {
    fun getBallotId(): Long?
    fun getAPPROVAL_STATUS(): String?
    fun getSTATUS(): Long?
    fun getBALLOT_name(): String?

    fun getCREATED_on(): String?
    fun getCOMMENTS_BY(): String?
    fun getCOMMENTS(): String?

    fun getUSER_id(): Long?



}
interface VotesTally {
    fun getBALLOT_ID(): Long?
    fun getBALLOTNAME(): String?
    fun getAPPROVED(): Long?
    fun getAPPROVEDWITHCOMMENTS(): Long?

    fun getDISAPPROVED(): Long?
    fun getABSTENTION(): Long?

    fun getSTATUS(): String?



}
interface SampleSubmissionDTO {
    fun getSSF_SUBMISSION_DATE(): Long?
    fun getFIRM_NAME(): String?
    fun getPHYSICAL_ADDRESS(): String?

    fun getTELEPHONE_NO(): String?

    fun getREGION(): String?

    fun getSECTION(): String?

    fun getPRODUCT(): String?

    fun getBRAND_NAME(): String?
    fun getSTANDARD_TITLE(): String?
    fun getCOMPLIANCE_REMARKS(): String?
    fun getCOMPLIANT_STATUS(): String?
    fun getINSPECTION_DATE(): String?
    fun getRESULTS_DATE(): String?

}
