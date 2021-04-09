import {Injectable} from '@angular/core';
import {dev} from '../dev/dev';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdministratorService {

  public url = `${dev.connect}system/admin/security/`;

  public urlToUsers = `${this.url}users/`;
  public urlToRbac = `${this.url}rbac/`;

  constructor(private http: HttpClient) {
  }

  loadUsers(): any {
    return this.http.get<any>(`${this.urlToUsers}load`);
  }

  assignRoleToUser(userId: bigint, roleId: bigint, status: number): any {
    return this.http.post(`${this.urlToRbac}role/assign/${userId}/${roleId}/${status}`, null);
  }

  assignCfsToUser(userProfileId: bigint, cfsId: bigint, status: number): any {
    return this.http.post(`${this.urlToRbac}cfs/assign/${userProfileId}/${cfsId}/${status}`, null);
  }

  loadUserSearch(userSearch: any): any {
    console.log(userSearch);
    return this.http.post(`${this.urlToUsers}search`, userSearch);
  }

  loadUserDetails(userID: any): any {
    console.log(userID);
    const params = new HttpParams()
      .set('userID', userID);
    return this.http.get<any>(`${this.urlToUsers}user-details`, {params});
  }

  registerEmployee(UserEntityDto: any): any {
    console.log(UserEntityDto);
    return this.http.post(`${this.urlToUsers}`, UserEntityDto);
  }
}
