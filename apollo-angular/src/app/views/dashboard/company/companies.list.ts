import {Component, OnInit} from '@angular/core';
import {CompanyService} from "../../../core/store/data/companies/company/company.service";
import {Observable} from "rxjs";
import {Company} from "../../../core/store";

@Component({
  selector: 'app-companies',
  templateUrl: './companies.list.html',
  styles: []
})
export class CompaniesList implements OnInit {
  companies$: Observable<Company[]>;
  filterName: string;
  p = 1;

  constructor(
    private service: CompanyService
  ) {
    this.filterName = '';
    this.companies$ = service.entities$;
    service.getAll().subscribe();
  }

  ngOnInit(): void {
  }

  editRecord(theRecord: Company) {

  }
}
