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

    listCompletedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/completed/" + documentType), {
            params: {
                'page': page.toString(),
                'size': size.toString(),
            }
        })
    }

    getConsignmentDetails(consignmentUiid: string) : Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/document/details/" + consignmentUiid))
    }

    listAssignedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/assigned/" + documentType), {
            params: {
                'page': page.toString(),
                'size': size.toString(),
            }
        })
    }

    listManualAssignedCd(documentType: String, page: Number = 0, size: Number = 20): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/di/consignment/documents/manual/assigned/" + documentType), {
            params: {
                'page': page.toString(),
                'size': size.toString(),
            }
        })
    }

    consignmentMetadata(documentUuid: String): Observable<any> {
        return this.client.get("/api/v1/di/consignment/document/manifest/" + documentUuid)
    }

    documentTypes(): Observable<any> {
        return this.client.get("/api/v1/di/consignment/document/types")
    }

    applicationTypes(): Observable<any> {
        return this.client.get("/api/v1/di/application/types")
    }
}
