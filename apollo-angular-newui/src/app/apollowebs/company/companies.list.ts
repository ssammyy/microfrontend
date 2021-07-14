import {Component, OnInit} from '@angular/core';
import {
    Company, CompanyService,
    Go, loadCompanyId
} from '../../core/store';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';

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
        private service: CompanyService,
        private store$: Store<any>,
    ) {
        this.filterName = '';
        this.companies$ = service.entities$;
        service.getAll().subscribe();

    }

    ngOnInit(): void {

    }

    editRecord(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/company'}));
    }


    onClickPlantDetails(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/companies/branches'}));
    }

    onClickDirectors(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/directors'}));
    }


}
