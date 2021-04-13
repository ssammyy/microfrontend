import {Injectable} from '@angular/core';
import {dev} from '../dev/dev';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MarketSurveillanceService {

  public apiUrl = `${dev.connect}ms/ui/`;
  public urlToComplaint = `${this.apiUrl}complaint/`;

  constructor(private http: HttpClient) {
  }

  postMSComplaint(customerDetails: any, complaintDetails: any, locationDetails: any): any {
    console.log({customerDetails, complaintDetails, locationDetails});
    return this.http.post<any>(`${this.urlToComplaint}detail/new/save`, {
      customerDetails,
      complaintDetails,
      locationDetails
    });
  }

  loadMSTypes(): any {
    return this.http.get<any>(`${this.apiUrl}ms-types`);
  }

  loadMSComplaintList(): any {
    // console.log(this.http.head());
    return this.http.get<any>(`${this.urlToComplaint}list`);
  }

  loadMSComplaintDetail(refNumber: any): any {
    console.log(refNumber);
    const params = new HttpParams()
      .set('refNumber', refNumber);
    return this.http.get<any>(`${this.urlToComplaint}detail/view`, {params});
  }

  updateComplaintKEBSMandate(approveData: any, refNumber: string): any {
    console.log(approveData);
    const params = new HttpParams()
      .set('refNumber', refNumber);
    return this.http.put<any>(`${this.urlToComplaint}detail/update/approve`, approveData, {params});
  }

  updateComplaintKEBSNotWithInMandate(rejectData: any, refNumber: string): any {
    console.log(rejectData);
    const params = new HttpParams()
      .set('refNumber', refNumber);
    return this.http.put<any>(`${this.urlToComplaint}detail/update/reject`, rejectData, {params});
  }

  updateComplaintKEBSNotWithInMandateButOGA(adviceData: any, refNumber: string): any {
    console.log(adviceData);
    const params = new HttpParams()
      .set('refNumber', refNumber);
    return this.http.put<any>(`${this.urlToComplaint}detail/update/advice`, adviceData, {params});
  }

  assignComplaintToIO(assignData: any, refNumber: string): any {
    console.log(assignData);
    const params = new HttpParams()
      .set('refNumber', refNumber);
    return this.http.put<any>(`${this.urlToComplaint}detail/update/assign`, assignData, {params});
  }

  loadComplaintSearch(complaintSearch: any): any {
    console.log(complaintSearch);
    return this.http.post(`${this.urlToComplaint}search`, complaintSearch);
  }
}
