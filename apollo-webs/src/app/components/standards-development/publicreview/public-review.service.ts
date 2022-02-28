import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {PublicReviewDraft, PublicReviewDraftComment, TCTasks} from "../../../shared/models/committee_module";
import {SacSecTasks} from "../../../shared/models/standard-development";

@Injectable({
  providedIn: 'root'
})
export class PublicReviewService {
  private baseUrl = 'http://localhost:8080/publicreview/';
  private apiurl = 'http://localhost:8080/';


  constructor(private http: HttpClient) {
  }

  getPublicReviewList(): Observable<any> {
    return this.http.get<PublicReviewDraft[]>(`${this.baseUrl}` + 'getprs')
  }

  preparePublicReview(publicReviewDraft: PublicReviewDraft): Observable<PublicReviewDraft> {
    return this.http.post<PublicReviewDraft>(`${this.baseUrl}` + 'preparepr', publicReviewDraft);
  }

  getPublicReviewCommentList(): Observable<any> {
    return this.http.get<PublicReviewDraftComment[]>(`${this.baseUrl}` + 'getprcomments')
  }

  preparePublicReviewDraftComment(publicReviewDraftComment: PublicReviewDraftComment): Observable<PublicReviewDraftComment> {
    console.log(publicReviewDraftComment)
    return this.http.post<PublicReviewDraftComment>(`${this.baseUrl}` + 'commentpr', publicReviewDraftComment);

  }

  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', `${this.apiurl}upload`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${this.apiurl}files`);
  }

  public getTCTasks(): Observable<TCTasks[]> {
    return this.http.get<TCTasks[]>(`${this.baseUrl}` + 'getTcTasks')
  }


  getPublicReview(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}publicreviewdraft/${id}`);
  }
}
