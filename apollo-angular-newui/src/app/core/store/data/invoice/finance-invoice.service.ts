import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import swal from "sweetalert2";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class FinanceInvoiceService {

    constructor(private http: HttpClient) {
    }

    addCorporateCustomer(data): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/corporate/add"), data)
    }

    updateCorporateCustomer(data: any, corporateId: any): Observable<any> {
        return this.http.put(ApiEndpointService.getEndpoint("/api/v1/corporate/update/"+corporateId), data)
    }

    getCorporateDetails(corporateId: any): Observable<any>{
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/corporate/details/"+corporateId))
    }

    listCorporateCustomers(page: number, pageSize: number): Observable<any> {
        let params = {}
        if (pageSize) {
            params['page'] = page
            params['size'] = pageSize
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/corporate/list"), {
            params: params
        })
    }

    loadBills(corporateId: any, page: number, pageSize: number): Observable<any>{
        let params = {}
        if (pageSize) {
            params['page'] = page
            params['size'] = pageSize
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/corporate/bills/"+corporateId), {
            params: params
        })
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
