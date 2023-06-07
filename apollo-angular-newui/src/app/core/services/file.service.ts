import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) {}

  downloadFile(): any {return this.http.get('http://localhost:4200/assets/davy.pdf', {responseType: 'blob'});
  }
}
