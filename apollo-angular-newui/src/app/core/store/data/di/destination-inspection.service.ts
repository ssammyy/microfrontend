import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {Observable, throwError} from "rxjs";
import * as fileSaver from 'file-saver';
import {catchError, map} from "rxjs/operators";
import swal from "sweetalert2";
import {DatePipe} from "@angular/common";

@Injectable({
    providedIn: 'root'
})
export class DestinationInspectionService {


    constructor(private client: HttpClient) {
    }

    formatDownloadReport(report: any, reportName: string): any {
        let filters = {}
        Object.keys(report).forEach(k => {
            if (report[k]) {
                if (k == 'startDate') {
                    filters['date__start'] = report[k]
                } else if (k === 'endDate') {
                    filters['date__end'] = report[k]
                } else {
                    filters[k] = report[k]
                }
            }
        })
        let data = {
            filters: filters,
            reportName: reportName
        }
        return data
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
            page: page.toString(),
            size: size.toString()
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

    loadLaboratories(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/laboratory/list"))
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

    loadIdfDocuments(status: string, page: number, size: number, startDate?: string, keywords?: string): Observable<any> {
        let params = {
            status: status,
            page: String(page),
            size: String(size)
        }
        if (startDate) {
            params["date"] = startDate ? startDate : ""
        }
        if (keywords) {
            params['keywords'] = keywords
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/documents/idf"), {
            params: params
        })
    }

    loadDeclarationDocuments(status: string, page: number, size: number, startDate?: string, keywords?: string): Observable<any> {
        let params = {
            status: status,
            page: String(page),
            size: String(size)
        }
        if (startDate) {
            params["date"] = startDate ? startDate : ""
        }
        if (keywords) {
            params['keywords'] = keywords
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/documents/declaration"), {
            params: params
        })
    }

    loadManifestDocuments(status: string, startDate?: string, keywords?: string): Observable<any> {
        let params = {
            status: status,
        }
        if (startDate) {
            params["date"] = startDate ? startDate : null
        }
        if (keywords) {
            params['keywords'] = keywords
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/documents/manifest"), {
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

    demandNoteListAndSearch(search: any, date: any, dateEnd: any, status: any, page: number, size: number): Observable<any> {
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
        if (dateEnd) {
            params['end_date'] = new DatePipe('en-US').transform(dateEnd, 'dd-MM-yyyy');
        }
        if (status) {
            params['status'] = status
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand-notes/list"), {
            params: params
        })
    }

    uploadMinistryChecklist(file: File, comment: string, referenceNumber: string, id: any): Observable<any> {
        let fd = new FormData()
        fd.append("file", file)
        fd.append("referenceNumber", referenceNumber)
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
            if (contentDisposition) {
                const r = /(?:filename=")(.+)(?:")/
                filename = r.exec(contentDisposition)[1];
            }
        } catch (e) {
            console.error(e)
            filename = null
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

    downloadDocument(url, params: any = {}, data?: any) {
        let observable: Observable<any>
        if (data) {
            observable = this.client.post(ApiEndpointService.getEndpoint(url), data, {
                observe: 'response',
                responseType: 'blob',
                params: params
            });
        } else {
            observable = this.client.get(ApiEndpointService.getEndpoint(url), {
                observe: 'response',
                responseType: 'blob',
                params: params
            });
        }
        observable.pipe(map((res: HttpResponse<any>) => {
                if (res.ok) {
                    console.log(res.body)
                    let fileName = this.getFileName(res)
                    if (fileName) {
                        // @ts-ignore
                        let blob: any = new Blob([res.body], {type: res.headers.get("content-type")});
                        fileSaver.saveAs(blob, fileName);
                        return fileName
                    } else {
                        console.log(res.body.toString())
                    }
                } else {
                    console.log(res.body)
                    this.showError(res.body, null)
                }
                return null
            }
            ),
            catchError((err: HttpErrorResponse) => {
                console.log(err)
                var errorMessage = "Download failed, please try again latter"
                if (err.error instanceof Blob) {
                    // let txt = await err.error.stream().getReader()
                    err.error.text().then(txt => {
                        const res = JSON.parse(txt);
                        errorMessage = res.message
                    })
                }
                return throwError(errorMessage)
            })
        ).subscribe(
            res => {
                console.log(res)
            },
            err => {
                console.log(err)
                if (err instanceof Blob) {
                    console.log("DOWNLOAD ERROR:")
                } else {
                    this.showError(err.message ? err.message : "Download failed, please try again latter", null)
                }
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

    getDetails(url: string, params: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint(url), {
            params: params
        })
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

    uploadAuctionGoodsAndVehicles(selectedFile: File, cfsCode: any, fileType: any, categoryCode: any, auctionReportDate: string): Observable<any> {
        let fd = new FormData()
        fd.append("file", selectedFile)
        fd.append("categoryCode", categoryCode)
        fd.append("cfsCode", cfsCode)
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

    approveRejectAuctionItem(selectedFile: File, data: any, auctionId: number): Observable<any> {
        let fd = new FormData()
        fd.append("file", selectedFile)
        // Add request data
        Object.keys(data).forEach((s) => {
            fd.append(s, data[s])
        })

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

    showConfirmation(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            showDenyButton: true,
            confirmButtonText: 'Confirm',
            denyButtonText: `Cancel`,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn',
                denyButton: 'btn btn-danger form-wizard-next-btn',
            },
            // icon: 'question'
        }).then((result) => {
            if (fn) {
                fn(result.isConfirmed)
            }
        })
    }


    listMyCfs(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/port/freight/user-stations"))
    }

    loadCfs(page: number, size: number): Observable<any> {
        let params = {}
        if (size) {
            params['page'] = page
            params['size'] = size
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/config/cfs/stations"), {
            params: params
        })
    }

    saveCfs(fee: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/config/cfs/station"), fee)
    }

    updateCfs(fee: any, feeId: any): Observable<any> {
        return this.client.put(ApiEndpointService.getEndpoint("/api/v1/di/config/cfs/station/" + feeId), fee)
    }

    deleteCfs(feeId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/config/cfs/station/" + feeId))
    }

    loadInspectionFees(page: number, size: number): Observable<any> {
        let params = {}
        if (size) {
            params['page'] = page
            params['size'] = size
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/config/inspection/fee"), {
            params: params
        })
    }

    saveInspectionFee(fee: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/config/inspection/fee"), fee)
    }

    updateInspectionFee(fee: any, feeId: any): Observable<any> {
        return this.client.put(ApiEndpointService.getEndpoint("/api/v1/di/config/inspection/fee/" + feeId), fee)
    }

    deleteInspectionFee(feeId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/config/inspection/fee/" + feeId))
    }

    listRevenueLines(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/config/revenue/lines"))
    }

    listLaboratories(page: number, size: number): Observable<any> {
        let params = {}
        if (size) {
            params['page'] = page
            params['size'] = size
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/laboratory/list"), {
            params: params
        })
    }

    saveLaboratory(lab: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/laboratory/add"), lab)
    }

    updateLaboratory(lab: any, labId: any): Observable<any> {
        return this.client.put(ApiEndpointService.getEndpoint("/api/v1/laboratory/update/" + labId), lab)
    }

    deleteLaboratory(labId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/laboratory/" + labId))
    }

    listCustomOffices(page: number, size: number): Observable<any> {
        let params = {}
        if (size) {
            params['page'] = page
            params['size'] = size
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/config/custom/offices"), {
            params: params
        })
    }

    saveCustomOffice(lab: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/config/custom/office"), lab)
    }

    updateCustomOffice(lab: any, labId: any): Observable<any> {
        return this.client.put(ApiEndpointService.getEndpoint("/api/v1/di/config/custom/office/" + labId), lab)
    }

    deleteCustomOffice(labId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/config/custom/office/" + labId))
    }

    // Certificates
    loadCertificateDocument(keywords: string, category: string, documentType: string, page: number, size: number): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        params['category'] = category
        if (keywords) {
            params["keywords"] = keywords
        }
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/certificates/" + documentType.toLowerCase()), {
            params: params
        });
    }

}
