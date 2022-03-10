import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {Observable} from "rxjs";
import * as fileSaver from 'file-saver';
import {map} from "rxjs/operators";
import swal from "sweetalert2";
import {DatePipe} from "@angular/common";

@Injectable({
    providedIn: 'root'
})
export class DestinationInspectionService {


    constructor(private client: HttpClient) {
    }

    // Check if role is in required privileges
    hasRole(privileges: string[], roles: any[]): boolean {
        for (let role of roles) {
            for (let p of privileges) {
                if (role == p) {
                    return true
                }
            }
        }
        return false
    }

    loadExchangeMessages(status: any, flowDirection: any, exchangeFile: any, date: any, page: any, size: any): Observable<any> {
        let params = {
            direction: 'desc',
            flowDirection: flowDirection,
            page: page,
            size: size
        }
        if (exchangeFile) {
            params['exchangeFile'] = exchangeFile
        }
        if (status) {
            params['fileStatus'] = status
        }
        if (date) {
            params['date'] = new DatePipe('en-US').transform(date, 'dd-MM-yyyy');
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/files/list"), {
            params: params
        })
    }

    loadExchangeMessageFile(messageId): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/files/get/" + messageId), {
            params: {}
        })
    }

    resendExchangeMessageFile(messageId): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/files/resend/" + messageId), {})
    }

    loadExchangeStats(date: any): Observable<any> {
        let params = {}
        if (date) {
            params['date'] = date
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/files/stats/" + status), {
            params: params
        })
    }

    loadIncompleteIdfDocuments(params): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/files/incomplete/idf"), {
            params: params
        })
    }

    loadPersonalDashboard(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/dashboard/personal"))
    }

    loadAllDashboard(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/dashboard/all"))
    }

    uploadConversionRates(file: File, fileType: any): Observable<any> {
        let fd = new FormData()
        fd.append("file", file)
        fd.append("docType", fileType)
        fd.append("file_type", fileType)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/upload/exchange-rate"), fd)
    }

    loadConversionRates(params: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/exchange-rates"), {
            params: params
        })
    }

    sendDemandNote(data: any, consignmentUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/generate/" + consignmentUuid), data)
    }

    listDemandNotes(consignmentId): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/list/" + consignmentId))
    }

    loadMyTasks(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/my/tasks"))
    }

    deleteTask(taskId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/my/task/" + taskId))
    }

    loadChecklists(itemUuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/checklists/" + itemUuid))
    }

    downloadChecklist(checkListId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/checklist/download/" + checkListId))
    }

    listOfficersForConsignment(consignmentUuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/user/officers/" + consignmentUuid))
    }

    listOfficersInMyStation(designation: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/user/in-my-station/" + designation))
    }

    approveReject(data: any, consignmentUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/approve-reject/" + consignmentUuid), data)
    }

    getAllPorts(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/ports"))
    }

    loadMinistryStations(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/ministry/stations"))
    }

    userBlacklistTypes(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/user/blacklist/types"))
    }

    getAllPortFreightStations(portId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/port/freight/stations/" + portId))
    }

    assignPort(data: any, consignmentUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/assign-port/" + consignmentUuid), data)
    }

    sendCertificateOfInspection(data: any, consignmentUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/send-coi/" + consignmentUuid), data)
    }

    approveRejectItems(data: any, consignmentUuid: any, itemId: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/item/compliance/approve-reject/" + itemId + "/" + consignmentUuid), data)
    }

    sendConsignmentDocumentAction(data: any, consignmentUuid: any, actionName: string): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/" + actionName + "/" + consignmentUuid), data)
    }

    searchConsignmentDocuments(data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/search"), data)
    }

    assignInspectionOfficer(data: any, consignmentUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/assign-io/" + consignmentUuid), data)
    }

    getInspectionUiConfigurations(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/cd/inspection/configuration"))
    }

    demandNoteFees(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand-note/fees"))
    }

    demandNoteStats(date: any): Observable<any> {
        let params = {}
        if (date) {
            params['date'] = date
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand-notes/stats"), {
            params: params
        })
    }

    demandNoteListAndSearch(search: any, date: any, status: number, page: number, size: number): Observable<any> {
        let params = {
            page: page.toString(),
            size: size.toString()
        }
        if (search) {
            params['trx'] = search
        }
        if (date) {
            params['date'] = new DatePipe('en-US').transform(date, 'dd-MM-yyyy');
        }
        if (status) {
            params['status'] = status
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand-notes/list"), {
            params: params
        })
    }

    uploadMinistryChecklist(file: File, comment: string, id: any): Observable<any> {
        let fd = new FormData()
        fd.append("file", file)
        fd.append("comment", comment)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspection/checklist/" + id), fd)
    }

    requestMinistryChecklist(data: any, id: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspections/request/" + id), data)
    }

    uploadForeignDocuments(file: File, fileType: string, partnerId: any, documentType: any): Observable<any> {
        let fd = new FormData()
        fd.append("file", file)
        fd.append("docType", documentType)
        fd.append("file_type", fileType)
        fd.append("partnerId", partnerId)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/foreign/cd/upload"), fd)
    }

    uploadConsignmentDocumentAttachment(file: File, description: string, documentUuid: string): Observable<any> {
        let fd = new FormData()
        fd.append("file", file)
        fd.append("description", description)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/attachments/upload/" + documentUuid), fd)
    }

    listMinistryInspections(status: number, page: number, size: number): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspections/" + status), {
            params: {
                'page': page.toString(),
                'size': size.toString(),
            }
        })
    }

    getMinistryInspections(itemId: number): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspection/details/" + itemId))
    }

    downloadMinistryCheckList(itemId: number): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspection/download/" + itemId))
    }

    loadCustomsDeclaration(itemId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/customs/declaration/" + itemId))
    }

    loadIdfDocumentDetails(itemId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/inspection/idf/details/" + itemId))
    }

    listCompletedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        let params = {
            'page': page.toString(),
            'size': size.toString(),
        };
        if (documentType) {
            params['cdTypeUuid'] = documentType
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/completed"), {
            params: params
        })
    }

    listSectionOngoingCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        var params = {
            'page': page.toString(),
            'size': size.toString(),
        }
        if (documentType) {
            params['cdTypeUuid'] = documentType
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/ongoing"), {
            params: params
        })
    }

    getConsignmentDetails(consignmentUiid: string): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/details/" + consignmentUiid))
    }

    getFileName(response: HttpResponse<Blob>) {
        let filename: string;
        try {
            const contentDisposition: string = response.headers.get('content-disposition');
            console.log(contentDisposition)
            const r = /(?:filename=")(.+)(?:")/
            filename = r.exec(contentDisposition)[1];
        } catch (e) {
            console.error(e)
            filename = 'FILE.pdf'
        }
        return filename
    }

    loadLabResults(itemUuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/lab-results/" + itemUuid))
    }

    loadLabResultsDocuments(ssfId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/lab-result/ssf-files/" + ssfId))
    }

    deleteAttachmentDocument(attachmentId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/attachments/" + attachmentId))
    }

    downloadDocument(url, params: any = {}) {
        this.client.get(ApiEndpointService.getEndpoint(url), {
            observe: 'response',
            responseType: 'blob',
            params: params
        })
            .pipe(map((res: HttpResponse<any>) => {
                    if (res.ok) {
                        let fileName = this.getFileName(res)
                        if (!fileName) {
                            fileName = "sample.pdf"
                        }
                        // @ts-ignore
                        let blob: any = new Blob([res.body], {type: res.headers.get("content-type")});
                        fileSaver.saveAs(blob, fileName);
                        return fileName
                    } else {
                        this.showError(res.body, null)
                    }
                }
            ))
            .subscribe(
                res => {
                    console.log(res)
                },
                error => {
                    console.log(error)
                    this.showError(error.message ? error.message : "Download failed, please try again latter", null)
                }
            )
    }

    listAssignedCd(documentType: String, page: Number = 0, size: Number = 20, otherParams: any = {}): Observable<any> {
        let params = {}
        if (otherParams) {
            params = otherParams
        }
        params['page'] = page.toString()
        params['size'] = size.toString()

        if (documentType) {
            params['cdTypeUuid'] = documentType
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/assigned"), {
            params: params
        })
    }

    listManualAssignedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        let params = {
            'page': page.toString(),
            'size': size.toString(),
        }
        if (documentType) {
            params['cdTypeUuid'] = documentType
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/manual/assigned"), {
            params: params
        })
    }

    consignmentManifest(documentUuid: String): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/manifest/" + documentUuid))
    }

    documentTypes(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/types"))
    }

    applicationTypes(): Observable<any> {
        return this.client.get("/api/v1/di/application/types")
    }

    getAuditComments(id: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/audit/" + id))
    }

    // Download attachment
    downloadAttachment(id: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/cd/download/attachments/" + id))
    }

    getConsignmentAttachments(consignmentId: string): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/attachments/" + consignmentId))
    }

    loadItemDetails(itemId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item/" + itemId))
    }

    loadChecklistConfigs(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/check-list/configurations"));
    }

    saveChecklist(itemUuid: any, data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/save-checklist/" + itemUuid), data);
    }

    loadCorDetails(cdUuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/inspection/cor/details/" + cdUuid));
    }

    loadCocDetails(cdUuid: any, docType: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/cd/certificate/" + docType + "/details/" + cdUuid));
    }

    saveSSFDetails(data: any, itemUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item-ssf/" + itemUuid), data);
    }

    saveSCFDetails(data: any, itemUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item-scf/" + itemUuid), data);
    }

    demandNoteDetails(demandNoteId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/details/" + demandNoteId))
    }

    loadSupervisorTasks(uuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/tasks/" + uuid));
    }

    submitDemandNote(demandNoteId: any, data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/submit/" + demandNoteId), data)
    }

    submitOtherDemandNote(demandNoteId: any, data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/other/demand/note/submit/" + demandNoteId), data)
    }

    deleteDemandNote(demandNoteId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/delete/" + demandNoteId))
    }

    getDetails(url: string): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint(url))
    }

    updateSSFResults(data: any, itemUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item-ssf-result/" + itemUuid), data);
    }

    getIsmApplications(applicationStatus, page: number, pageSize: number): Observable<any> {
        let params = {}
        if (pageSize) {
            params['page'] = page
            params['size'] = pageSize
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/ism/list/" + applicationStatus), {
            params: params
        })
    }

    getIsmApplicationDetails(applicationId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/ism/details/" + applicationId))
    }

    approveRejectIsm(data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/ism/approve-reject"), data)
    }

    listAuctionItems(keyword: any, action: any, page: number, pageSize: number): Observable<any> {
        let params = {}
        if (pageSize) {
            params['page'] = page
            params['size'] = pageSize
        }
        if (keyword && keyword.length > 0) {
            params['keyword'] = keyword
            return this.client.get(ApiEndpointService.getEndpoint("/api/v1/auction/auctions/search"), {
                params: params
            })
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/auction/auctions/" + action), {
            params: params
        })
    }

    listAuctionCategories(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/auction/categories"))
    }

    uploadAuctionGoodsAndVehicles(selectedFile: File, fileType: any, categoryCode: any, auctionReportDate: string): Observable<any> {
        let fd = new FormData()
        fd.append("file", selectedFile)
        fd.append("categoryCode", categoryCode)
        fd.append("listingDate", auctionReportDate)
        fd.append("file_type", fileType)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/auction/auction/upload"), fd)
    }

    getAuctionItemDetails(requestId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/auction/auction/details/" + requestId))
    }

    loadAuctionCategories(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/auction/categories"))
    }

    assignAuctionInspectionOfficer(data: any, auctionId: number): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/auction/auction/assign/" + auctionId), data)
    }

    uploadAuctionReport(file: File, auctionId: number, remarks: any): Observable<any> {
        let fd = new FormData()
        fd.append("file", file)
        fd.append("remarks", remarks)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/auction/auction/attachment/upload/" + auctionId), fd)
    }

    requestAuctionPayment(data: any, auctionId: number): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/auction/auction/generate/demand-note/" + auctionId), data)
    }

    approveRejectAuctionItem(selectedFile: File, remarks: any, approve: any, auctionId: number): Observable<any> {
        let fd = new FormData()
        fd.append("file", selectedFile)
        fd.append("remarks", remarks)
        fd.append("approve", approve)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/auction/auction/approve-reject/" + auctionId), fd)
    }

    addAuctionItem(data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/auction/auction/add"), data)
    }

    showSuccess(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
        }).then(() => {
            if (fn) {
                fn()
            }
        })
    }

    showError(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'error'
        }).then(() => {
            if (fn) {
                fn()
            }
        })
    }


}
