import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {
    BatchFileFuelSaveDto,
    FuelBatchDetailsDto,
    FuelEntityAssignOfficerDto,
    FuelEntityDto,
    FuelEntityRapidTestDto,
    FuelInspectionDto,
    SampleCollectionDto,
    SampleSubmissionDto
} from "./ms.model";
import swal from "sweetalert2";

@Injectable({
    providedIn: 'root'
})
export class MsService {

    constructor(private http: HttpClient) {
    }

    fuelBatchDetailsListExamples(): FuelBatchDetailsDto[]{
        let test101 = new FuelBatchDetailsDto();
        test101.id = 123;
        test101.region = 'test';
        test101.county = 'test';
        test101.town = 'test';
        test101.referenceNumber = 'test';
        test101.batchFileYear = 'test';
        test101.remarks = 'test';
        test101.batchClosed = true

        let test : FuelBatchDetailsDto[] = [];
        test.push(test101)

        return test
    }

    // Check if role is in required privileges
    public hasRole(privileges: string[], roles: any[]): boolean {
        for (let role of roles) {
            for (let p of privileges) {
                if (role == p) {
                    return true;
                }
            }
        }
        return false;
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

    public loadMSFuelBatchList(page: string,records: string): Observable<FuelBatchDetailsDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.ALL_BATCH_LIST);
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelBatchDetailsDto[]>(url).pipe(
            map(function (response: FuelBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public addNewMSFuelBatch(data: BatchFileFuelSaveDto): Observable<FuelInspectionDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.ADD_BATCH);
        return this.http.post<FuelInspectionDto[]>(url, data).pipe(
            map(function (response: FuelInspectionDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public closeMSFuelBatch(referenceNo: string): Observable<FuelBatchDetailsDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.CLOSE_BATCH);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<FuelBatchDetailsDto[]>(url, null, {params}).pipe(
            map(function (response: FuelBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionList(batchReferenceNo: string): Observable<FuelInspectionDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo);
        return this.http.get<FuelInspectionDto[]>(url, {params}).pipe(
            map(function (response: FuelInspectionDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionAddSchedule(batchReferenceNo: string, data: FuelEntityDto): Observable<FuelInspectionDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo);
        return this.http.post<FuelInspectionDto[]>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionScheduledDetails(batchReferenceNo: string, referenceNo: string): Observable<FuelInspectionDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.get<FuelInspectionDto>(url, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionScheduledAssignOfficer(batchReferenceNo: string, referenceNo: string, data: FuelEntityAssignOfficerDto): Observable<FuelInspectionDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_ASSIGN_OFFICER);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionScheduledRapidTest(batchReferenceNo: string, referenceNo: string, data: FuelEntityRapidTestDto): Observable<FuelInspectionDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_RAPID_TEST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionScheduledAddSampleCollection(batchReferenceNo: string, referenceNo: string, data: SampleCollectionDto): Observable<FuelInspectionDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_COLLECT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionScheduledAddSampleSubmission(batchReferenceNo: string, referenceNo: string, data: SampleSubmissionDto): Observable<FuelInspectionDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public msFuelInspectionScheduledAddSampleSubmissionBSNumber(batchReferenceNo: string, referenceNo: string, bsNumber: string): Observable<FuelInspectionDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION_BS_NUMBER);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo)
            .set('bsNumber', bsNumber);
        return this.http.post<FuelInspectionDto>(url, null, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

}
