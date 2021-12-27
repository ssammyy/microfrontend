import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import swal from "sweetalert2";

@Injectable({
    providedIn: 'root'
})
export class PVOCService {

    constructor(private http: HttpClient) {
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

    loadPartners(keyword: string, page: number, size: number): Observable<any> {
        let params = {}
        if (keyword) {
            params['keywords'] = keyword
        }
        if (size) {
            params['page'] = page
            params['size'] = size
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/partners/list"), {
            params: params
        })
    }

    addPartner(data: any): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/partners/add"), data)
    }

    updatePartner(data: any, partnerId: number): Observable<any> {
        return this.http.put(ApiEndpointService.getEndpoint("/api/v1/partners/update/" + partnerId), data)
    }

    addPartnerApiClient(data: any, partnerId: number): Observable<any>{
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/partners/create/api-client/" + partnerId), data)
    }

    loadPartnerDetails(partnerId: number): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/partners/details/" + partnerId))
    }

    public checkExemptionApplicable(): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/check/eligible");
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getWaiverCategories(): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/waiver/categories");
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public applyForImportExemption(data: any, files: File[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/apply");
        return this.sendFiles(data, files, url)
    }

    private sendFiles(data: any, files: File[], url: string) {
        console.log(url)
        let form = new FormData()
        form.append("data", JSON.stringify(data))

        for (let filesKey in files) {
            form.append("files", filesKey);
        }
        return this.http.post<any>(url, form).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public applyForImportWaiver(data: any, files: File[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/waiver/apply");
        console.log(url)
        return this.sendFiles(data, files, url)
    }
}
