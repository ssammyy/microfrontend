import {Injectable} from '@angular/core';
import {dev} from '../dev/dev';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserCompanyEntityDto, UserRequestEntityDto} from "../models/master-data-details";

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

  loadUsersRequests(): any {
    const params = new HttpParams()
      .set('page', '0')
      .set('page', '20');
    return this.http.get<any>(`${this.urlToUsers}load/users-requests`);
  }

  assignRoleToUser(userId: bigint, roleId: bigint, status: number): any {
    return this.http.post(`${this.urlToRbac}role/assign/${userId}/${roleId}/${status}`, null);
  }

  assignRoleToUserRequested(userId: bigint, roleId: bigint, status: number, requestId: string): any {
    return this.http.post(`${this.urlToRbac}user/request/role/assign/${userId}/${roleId}/${status}/${requestId}`, null);
  }

  sendUserRequest(data: UserRequestEntityDto, userId: bigint): any {
    return this.http.post(`${this.urlToUsers}${userId}/user-request`, data);
  }

  sendUserCompanyDetails(data: UserCompanyEntityDto, userId: bigint): any {
    return this.http.post(`${this.urlToUsers}${userId}/update/company-profile`, data);
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
