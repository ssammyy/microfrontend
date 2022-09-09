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

    loadPartnerNames(): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/partners/names"))
    }

    addPartner(data: any): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/partners/add"), data)
    }

    updatePartner(data: any, partnerId: number): Observable<any> {
        return this.http.put(ApiEndpointService.getEndpoint("/api/v1/partners/update/" + partnerId), data)
    }

    addPartnerApiClient(data: any, partnerId: number): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/partners/create/api-client/" + partnerId), data)
    }

    loadPartnerDetails(partnerId: number): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/partners/details/" + partnerId))
    }

    loadPartnerCountries(): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/partners/countries"))
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

    manufacturerWaiver(status: string, page: number, size: number): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        params['status'] = status
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/manufacturer/waiver/history"), {
            params: params
        })
    }

    public applyForImportExemption(data: any, files: File[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/apply");
        return this.sendFiles(data, files, url)
    }


    manufacturerExemptionHistory(status: string, page: number, size: number): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        params['status'] = status
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/manufacturer/exemption/history"), {
            params: params
        })
    }

    private sendFiles(data: any, files: File[], url: string) {
        console.log(url)
        let form = new FormData()
        form.append("data", JSON.stringify(data))

        for (let filesKey in files) {
            form.append("files", files[filesKey]);
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

    public listComplaintRequests(status: string, page: any, size: any): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/exemptions/get/" + status), {
            params: params
        })
    }

    public listExemptionApplications(keyword: any, status: string, page: any, size: any): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        if (keyword) {
            params['keyword'] = keyword
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/get/" + status), {
            params: params
        })
    }

    getExemptionApplicationDetails(exemptionId: any): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/details/" + exemptionId))
    }

    updateExemptionStatus(exemptionId: any, data: any): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/status/update/" + exemptionId), data)
    }

    public listComplaintApplications(keywords: string, status: string, page: any, size: any): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        if (keywords) {
            params["keywords"] = keywords
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/get/" + status), {
            params: params
        })
    }

    getComplaintApplicationDetails(complaintId: any): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/details/" + complaintId))
    }

    manufacturerComplaintHistory(status: string, page: number, size: number): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        if (status) {
            params["status"] = status
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/manufacturer/history"), {
            params: params
        })
    }

    updateComplaintStatus(complaintId: any, data: any): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/status/update/" + complaintId), data)
    }

    getComplaintCategories(): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/categories"))
    }

    getComplaintRecommendations(): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/recommendations"))
    }

    public fileComplaint(data: any, files: File[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/complaint/file");
        return this.sendFiles(data, files, url)
    }

    public listWaiverApplications(keywords: string, status: string, page: any, size: any): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        if (keywords) {
            params["keywords"] = keywords
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/waiver/get/" + status), {
            params: params
        })
    }

    getWaiverApplicationDetails(waiverId: any): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/waiver/details/" + waiverId))
    }


    loadCorDocument(keywords: string, reviewStatus: number, page: number, size: number): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        params['rev_status'] = reviewStatus
        if (keywords) {
            params["keywords"] = keywords
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/monitoring/foreign/cor"), {
            params: params
        });
    }

    loadCorDetails(documentId: any): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/monitoring/foreign/cor/" + documentId))
    }

    loadCocDocument(keywords: string, reviewStatus: number, documentType: string, page: number, size: number): Observable<any> {
        let params = {}
        params["size"] = size
        params["page"] = page
        params['rev_status'] = reviewStatus
        if (keywords) {
            params["keywords"] = keywords
        }
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/monitoring/foreign/" + documentType.toLowerCase()), {
            params: params
        });
    }

    loadCocDetails(documentId: any): Observable<any> {
        return this.http.get(ApiEndpointService.getEndpoint("/api/v1/pvoc/monitoring/foreign/cocorcoi/" + documentId))
    }

    sendPvocQuery(data: any): Observable<any> {
        return this.http.post(ApiEndpointService.getEndpoint("/api/v1/pvoc/kebs/query/request"), data)
    }
}
