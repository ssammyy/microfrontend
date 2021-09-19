import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {Observable} from "rxjs";
import * as fileSaver from 'file-saver';
import {map} from "rxjs/operators";
import swal from "sweetalert2";

@Injectable({
    providedIn: 'root'
})
export class DestinationInspectionService {

    constructor(private client: HttpClient) {
    }
    sendDemandNote(data: any, consignmentUuid: any): Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/generate/"+consignmentUuid),data)
    }
    listDemandNotes(consignmentId): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/list/"+consignmentId))
    }
    loadMyTasks(): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/my/tasks"))
    }
    loadChecklists(itemUuid: any): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/checklists/"+itemUuid))
    }
    downloadChecklist(checkListId: any): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/checklist/download/"+checkListId))
    }
    listOfficersForConsignment(consignmentUuid: any): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/user/officers/"+consignmentUuid))
    }
    approveReject(data: any, consignmentUuid: any):  Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/approve-reject/"+consignmentUuid),data)
    }
    getAllPorts(): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/ports"))
    }
    loadMinistryStations(): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/ministry/stations"))
    }

    userBlacklistTypes(): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/user/blacklist/types"))
    }
    getAllPortFreightStations(portId:any): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/port/freight/stations/"+portId))
    }
    assignPort(data: any, consignmentUuid: any): Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/assign-port/"+consignmentUuid),data)
    }
    sendCertificateOfInspection(data: any, consignmentUuid: any): Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/send-coi/"+consignmentUuid),data)
    }

    sendConsignmentDocumentAction(data: any, consignmentUuid: any,actionName: string): Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/"+actionName+"/"+consignmentUuid),data)
    }
    assignInspectionOfficer(data: any, consignmentUuid: any):  Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/assign-io/"+consignmentUuid),data)
    }
    getInspectionUiConfigurations(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/cd/inspection/configuration"))
    }
    demandNoteFees(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand-note/fees"))
    }
    uploadMinistryChecklist(file: File, comment: string, id: any): Observable<any>{
        let fd=new FormData()
        fd.append("file", file)
        fd.append("comment", comment)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspection/checklist/"+id),fd)
    }

    requestMinistryChecklist(data: any,id: any): Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/ministry/inspections/request/"+id),data)
    }
    uploadForeignDocuments(file: File, fileType: string, documentType:any): Observable<any>{
        let fd=new FormData()
        fd.append("file", file)
        fd.append("docType",documentType)
        fd.append("file_type", fileType)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/foreign/cd/upload"),fd)
    }

    uploadConsignmentDocumentAttachment(file: File, description: string, documentUuid: string): Observable<any>{
        let fd=new FormData()
        fd.append("file", file)
        fd.append("description", description)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/attachments/upload/"+documentUuid),fd)
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
    downloadMinistryCheckList(itemId: number): Observable<any>{
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
        }
        catch (e) {
            console.error(e)
            filename = 'myfile.pdf'
        }
        return filename
    }
    deleteAttachmentDocument(attachmentId: any): Observable<any> {
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/attachments/"+attachmentId))
    }
    downloadDocument(url){
        this.client.get(ApiEndpointService.getEndpoint(url),{ observe: 'response',responseType: 'blob'})
            .pipe(map((res: HttpResponse<Blob>)=>{
                    let fileName=this.getFileName(res)
                    if(!fileName){
                        fileName="sample.pdf"
                    }
                    // @ts-ignore
                    let blob: any = new Blob([res.body], { type: res.headers.get("content-type") });
                    const url = window.URL.createObjectURL(blob);
                    //window.open(url);
                    //window.location.href = response.url;
                    fileSaver.saveAs(blob, fileName);
                    return fileName
                }
            ))
            .subscribe(
                res=>{
                    console.log(res)
                }
            )
    }

    listAssignedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        let params = {
            'page': page.toString(),
            'size': size.toString(),
        }
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

    consignmentMetadata(documentUuid: String): Observable<any> {
        return this.client.get("/api/v1/di/consignment/document/manifest/" + documentUuid)
    }

    documentTypes(): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/types"))
    }

    applicationTypes(): Observable<any> {
        return this.client.get("/api/v1/di/application/types")
    }

    getAuditComments(id: any): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/audit/"+id))
    }

    // Download attachment
    downloadAttachment(id: any) : Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/cd/download/attachments/"+id))
    }

    getConsignmentAttachments(consignmentId: string) : Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/attachments/"+consignmentId))
    }

    loadItemDetails(itemId: any): Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item/"+itemId))
    }

    loadChecklistConfigs() : Observable<any>{
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/check-list/configurations"));
    }
    saveChecklist(itemUuid: any, data: any) : Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/save-checklist/"+itemUuid),data);
    }

    loadCorDetails(cdUuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/inspection/cor/details/"+cdUuid));
    }
    loadCocDetails(cdUuid: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/inspection/coc/details/"+cdUuid));
    }

    saveSSFDetails(data: any,itemUuid:any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item-ssf/"+itemUuid),data);
    }

    saveSCFDetails(data: any,itemUuid:any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item-scf/"+itemUuid),data);
    }

    demandNoteDetails(demandNoteId: any): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/details/"+demandNoteId))
    }
    submitDemandNote(demandNoteId: any,data: any): Observable<any>{
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/submit/"+demandNoteId),data)
    }
    deleteDemandNote(demandNoteId: any): Observable<any>{
        return this.client.delete(ApiEndpointService.getEndpoint("/api/v1/di/demand/note/submit/"+demandNoteId))
    }

    getDetails(url: string): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint(url))
    }
    updateSSFResults(data: any, itemUuid: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/item-ssf-result/"+itemUuid),data);
    }

    showSuccess(message: string, fn?:Function){
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
        }).then(()=>{
            if(fn){
                fn()
            }
        })
    }
    showError(message: string, fn?:Function){
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'error'
        }).then(()=>{
            if(fn){
                fn()
            }
        })
    }


}
