import {Injectable} from '@angular/core';
import {dev} from '../dev/dev';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ComplaintsService {
  public url = `${dev.connect}/api/ms/ui/complaint/`;

  constructor(private http: HttpClient) { }

  complaintList( listData: any ): any {
    return this.http.post<any>(this.url + 'list/266f9769-0a28-491c-8e05-aee12e9db5d0', listData);
  }
}
