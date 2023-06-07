import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {
    CallForApplication,
    Document,
    HOFRecommendationTask,
    ReviewApplicationTask,
    SubmitApplication,
    TCMemberDetails
} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {TechnicalCommittee} from "./std.model";

@Injectable({
    providedIn: 'root'
})
export class MembershipToTcService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;
    private apiMembershipToTCUrlAnonymous = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/membershipToTC/`;


    constructor(private http: HttpClient) {
    }

    public uploadCallForApplications(callForApplication: CallForApplication): Observable<any> {
        console.log(callForApplication);
        return this.http.post<CallForApplication>(`${this.apiMembershipToTCUrl}` + 'submitCallForApplication', callForApplication)
    }

    public getApplicantTasks(): Observable<TechnicalCommittee[]> {
        return this.http.get<TechnicalCommittee[]>(`${this.apiMembershipToTCUrlAnonymous}` + 'getCallForApplications')
    }

    public onSubmitApplication(submitApplication: SubmitApplication): Observable<any> {
        console.log(submitApplication);
        return this.http.post<SubmitApplication>(`${this.apiMembershipToTCUrlAnonymous}` + 'submitTCMemberApplication', submitApplication)
    }

    public getApplicationsForReview(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getApplicationsForReview')
    }

    public decisionOnApplications(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'decisionOnApplicantRecommendation',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }
    public rejectApplicantRecommendation(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'rejectApplicantRecommendation',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }

    public getHOFRecommendation(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRecommendationsFromHOF')
    }
    public getRejectedFromHOF(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRejectedFromHOF')
    }

    public getAllApplications(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getAllApplications')
    }


    public completeReview(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'completeSPCReview',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }
    public resubmitReview(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'resubmitReview',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }
    public nonRecommend(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'nonRecommend',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }

    public getRecommendationsFromSPC(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRecommendationsFromSPC')
    }

    public decisionOnSPCRecommendation(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number, decision: string): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
            .set('decision', String(decision))

        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'decisionOnSPCRecommendation',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }
    public decisionOnSACRecommendation(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number, decision: string): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
            .set('decision', String(decision))

        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'decisionOnSACRecommendation',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }

    public getAcceptedMembers(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getAcceptedFromSPC')

    }

    public getSacAccepted(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getSacAccepted')

    }

    public getRejectedFromSPC(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRejectedFromSPC')

    }

    public getNscRejected(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getNscRejected')

    }


    public sendAppointmentEmail(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number,data: FormData,): Observable<any> {
        return this.http.post<any>(`${this.apiMembershipToTCUrl}` + 'approve', data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'tCApplicationId':  String(tCApplicationId)}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );


    }

    public approveAppointmentEmail(tCApplicationId: string): Observable<any> {

        const url = `${this.apiMembershipToTCUrlAnonymous}` + 'approve';
        const params = new HttpParams()
            .set('applicationID', tCApplicationId);

        return this.http.get<any>(url, {params, responseType: 'text' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public approveAppointmentEmailAuthorizer(tCApplicationId: string): Observable<any> {

        const url = `${this.apiMembershipToTCUrlAnonymous}` + 'approveUserApplication';
        const params = new HttpParams()
            .set('applicationID', tCApplicationId);

        return this.http.get<any>(url, {params, responseType: 'text' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }
    public rejectAppointmentEmailAuthorizer(tCApplicationId: string): Observable<any> {

        const url = `${this.apiMembershipToTCUrlAnonymous}` + 'rejectUserApplication';
        const params = new HttpParams()
            .set('applicationID', tCApplicationId);

        return this.http.get<any>(url, {params, responseType: 'text' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getApprovedMembers(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getApproved')
    }

    public decisionOnForwardToHodIct(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number, decision: string): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
            .set('decision', String(decision))
        const url = `${this.apiMembershipToTCUrl}` + 'forwardToHodIct';
        return this.http.get<any>(url, {params, responseType: 'text' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }


    public getMembersToCreateCredentials(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getCredentials')
    }

    public decisionOnCreateCredentials(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))

        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'createdCredentials',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }

    public getMembersCreatedCredentials(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getAllUsersCreatedCredentials')
    }

    public sendInductionEmail(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))

        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'induction',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }

    public approveInductionEmail(tCApplicationId: string): Observable<any> {

        const url = `${this.apiMembershipToTCUrlAnonymous}` + 'getInduction';
        const params = new HttpParams()
            .set('applicationID', tCApplicationId);

        return this.http.get<any>(url, {params, responseType: 'text' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }


    public getAllUsersApprovedForInduction(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getAllUsersApprovedForInduction')
    }

    public sendEmailForFirstMeeting(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number, meetingDate: string): Observable<any> {
        const params = new HttpParams()
            .set('tCApplicationId', String(tCApplicationId))
            .set('meetingDate', String(meetingDate))


        return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'sendEmailForFirstMeeting',
            reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
            .pipe(
                map(function (response: any) {
                    return response;
                }),
                catchError((fault: HttpErrorResponse) => {
                    // console.warn(`getAllFault( ${fault.message} )`);
                    return throwError(fault);
                })
            );
    }


    public getTCMemberCreationTasks(): Observable<HOFRecommendationTask[]> {
        return this.http.get<HOFRecommendationTask[]>(`${this.apiMembershipToTCUrl}` + 'getTCMemberCreationTasks')
    }


    public saveTCMember(tCMemberDetails: TCMemberDetails): Observable<any> {
        return this.http.post<TCMemberDetails>(`${this.apiMembershipToTCUrl}` + 'saveTCMember', tCMemberDetails)
    }

    //upload justification Document
    public uploadFileDetails(draftStandardID: string, data: FormData, doctype: string, nomineeName: string): Observable<any> {
        const url = `${this.apiMembershipToTCUrlAnonymous}file-upload`;

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'callForTCApplicationId': draftStandardID, 'type': doctype, 'nomineeName': nomineeName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public viewDEditedApplicationPDF(applicationId: any, doctype: string): Observable<any> {
        const url = `${this.apiMembershipToTCUrl}` + 'view/CurriculumVitae';
        const params = new HttpParams()
            .set('draftStandardId', applicationId)
            .set('doctype', doctype);

        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getUserCV(requestId: string, documentType: string, documentTypeDef: string): Observable<any> {

        const url = `${this.apiMembershipToTCUrl}getUserCv`;
        const params = new HttpParams()
            .set('requestId', requestId)
            .set('documentType', documentType)
            .set('documentTypeDef', documentTypeDef)


        return this.http.get<Document>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
}
