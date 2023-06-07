import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";

@Injectable({
    providedIn: 'root'
})
export class SystemService {

    constructor(private client: HttpClient) {
    }


    loadApiClients(page: Number, size: Number): Observable<any> {
        return this.client.get(ApiEndpointService.getEndpoint("/api/v1/client/list"))
    }

    addApiClient(data: any): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/client/add"), data)
    }

    updateApiClient(data: any, clientId: number): Observable<any> {
        return this.client.post(ApiEndpointService.getEndpoint("/api/v1/client/update/" + clientId), data)
    }


}
