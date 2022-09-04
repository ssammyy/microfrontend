import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {
  DISDTTasks,
  HOPTasks,
  HoSicTasks,
  KNWCommittee,
  KNWDepartment,
  KnwSecTasks,
  NWADiSdtJustification,
  NWAJustification,
  NWAPreliminaryDraft,
  NWAStandard,
  NWAWorkShopDraft,
  SacSecTasks,
  SPCSECTasks,
  StandardTasks, UpdateNwaGazette,
  UploadNwaGazette
} from "../models/standard-development";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StdDevelopmentNwaService {

  private apiServerUrl = 'http://localhost:8080/nwa/';
  constructor(private http: HttpClient) { }

  public getKNWDepartments(): any{
    return this.http.get<KNWDepartment[]>(`${this.apiServerUrl}`+'getKNWDepartments')
  }

  public getKNWCommittee(): any{
    return this.http.get<KNWCommittee[]>(`${this.apiServerUrl}`+'getKNWCommittee')
  }

  public prepareJustification(nwaJustification: NWAJustification) : Observable<any>{
    return this.http.post<NWAJustification>(`${this.apiServerUrl}`+'prepareJustification',nwaJustification)
  }
  public knwtasks(): Observable<KnwSecTasks[]>{
    return this.http.get<KnwSecTasks[]>(`${this.apiServerUrl}`+'knwtasks')
  }
  public getSPCSECTasks(): Observable<SPCSECTasks[]>{
    return this.http.get<SPCSECTasks[]>(`${this.apiServerUrl}`+'getSpcSecTasks')
  }
  public decisionOnJustification(nwaJustification: NWAJustification): Observable<NWAJustification>{
    return this.http.post<NWAJustification>(`${this.apiServerUrl}`+'decisionOnJustification', nwaJustification)
  }

  public prepareDisDtJustification(nwaDiSdtJustification: NWADiSdtJustification): Observable<NWADiSdtJustification>{
    return this.http.post<NWADiSdtJustification>(`${this.apiServerUrl}`+'prepareDiSdtJustification', nwaDiSdtJustification)
  }
  public getDiSdtTasks(): Observable<DISDTTasks[]>{
    return this.http.get<DISDTTasks[]>(`${this.apiServerUrl}`+'getDiSdtTasks')
  }
  public decisionOnDiSdtJustification(nwaDiSdtJustification: NWADiSdtJustification): Observable<NWADiSdtJustification>{
    return this.http.post<NWADiSdtJustification>(`${this.apiServerUrl}`+'decisionOnDiSdtJustification', nwaDiSdtJustification)
  }
  public preparePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): Observable<NWAPreliminaryDraft>{
    return this.http.post<NWAPreliminaryDraft>(`${this.apiServerUrl}`+'preparePreliminaryDraft', nwaPreliminaryDraft)
  }
  public decisionOnPD(nwaPreliminaryDraft: NWAPreliminaryDraft): Observable<NWAPreliminaryDraft>{
    return this.http.post<NWAPreliminaryDraft>(`${this.apiServerUrl}`+'decisionOnPd', nwaPreliminaryDraft)
  }
  public getHOPTasks(): Observable<HOPTasks[]>{
    return this.http.get<HOPTasks[]>(`${this.apiServerUrl}`+'getHOPTasks')
  }
  public editWorkshopDraft(nwaWorkShopDraft: NWAWorkShopDraft): Observable<NWAWorkShopDraft>{
    return this.http.post<NWAWorkShopDraft>(`${this.apiServerUrl}`+'editWorkshopDraft', nwaWorkShopDraft)
  }

  public getSacSecTasks(): Observable<SacSecTasks[]>{
    return this.http.get<SacSecTasks[]>(`${this.apiServerUrl}`+'getSacSecTasks')
  }
  public decisionOnWd(nwaWorkShopDraft: NWAWorkShopDraft): Observable<NWAWorkShopDraft>{
    return this.http.post<NWAWorkShopDraft>(`${this.apiServerUrl}`+'decisionOnWd', nwaWorkShopDraft)
  }
  public uploadNwaStandard(nWAStandard: NWAStandard): Observable<NWAStandard>{
    return this.http.post<NWAStandard>(`${this.apiServerUrl}`+'uploadNwaStandard', nWAStandard)
  }
  public getHoSicTasks(): Observable<HoSicTasks[]>{
    return this.http.get<HoSicTasks[]>(`${this.apiServerUrl}`+'getHoSiCTasks')
  }
  public uploadGazetteNotice(uploadNwaGazette: UploadNwaGazette): Observable<UploadNwaGazette>{
    return this.http.post<UploadNwaGazette>(`${this.apiServerUrl}`+'uploadGazetteNotice', uploadNwaGazette)
  }
  public updateGazettementDate(updateNwaGazette: UpdateNwaGazette): Observable<UpdateNwaGazette>{
    return this.http.post<UpdateNwaGazette>(`${this.apiServerUrl}`+'updateGazettementDate', updateNwaGazette)
  }
}
