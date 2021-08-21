import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class DestinationInspectionService {

    constructor(private client: HttpClient) {
    }
    uploadForeignDocuments(file: File, fileType: string): Observable<any>{
        let fd=new FormData()
        fd.append("file", file)
        fd.append("file_type", fileType)
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/di/foreign-document"),fd)
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

    listCompletedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        var params = {
            'page': page.toString(),
            'size': size.toString(),
        }
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
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/completed"), {
            params: params
        })
    }

    getConsignmentDetails(consignmentUiid: string): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/details/" + consignmentUiid))
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
}
