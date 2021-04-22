import {Injectable} from '@angular/core';
import {dev} from '../dev/dev';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MasterDataService {

  public url = `${dev.connect}`;

  public MSUrl = 'ms/ui/';

  public sysAdmin = `${this.url}system/admin/`;

  constructor(private http: HttpClient) {
  }

  loadCounties(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}counties`);
  }

  loadTowns(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}towns`);
  }

  loadStandardProductCategory(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}standardProductCategory`);
  }

  loadProductCategories(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}productCategories`);
  }

  loadBroadProductCategory(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}broadProductCategory`);
  }

  loadProducts(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}products`);
  }

  loadProductSubcategory(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}productSubcategory`);
  }

  loadDivisions(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}divisions`);
  }

  loadDepartments(): any {
    return this.http.get<any>(`${this.url}${this.MSUrl}departments`);
  }

  loadDepartmentsSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/departments/load`);
  }

  loadDivisionsSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/divisions/load`);
  }

  loadRequestTypeSystemAdmin(status: number): any {
    return this.http.get<any>(`${this.sysAdmin}masters/userRequestType/loads/${status}`);
  }

  loadDirectorateSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/directorate/load`);
  }

  loadTitlesSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}security/titles/load`);
  }

  loadRolesSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}security/roles/load`);
  }

  loadAuthoritiesSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}security/authorities/load`);
  }

  loadDesignationsSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/designations/load`);
  }

  loadSectionSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/sections/load`);
  }

  loadL1SubSubSectionSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/subsections/l1/load`);
  }

  loadL2SubSubSectionSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/subsections/l2/load`);
  }

  loadRegionsSystemAdmin(): any {
    return this.http.get<any>(`${this.sysAdmin}masters/regions/load`);
  }


}
