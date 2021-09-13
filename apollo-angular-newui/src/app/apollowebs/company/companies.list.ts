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
        // location.reload();
    }

    ngOnInit(): void {
        this.companies$ = this.service.entities$;
        this.service.clearCache();
        this.service.getAll().subscribe();
    }

    editRecord(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company'}));
    }


    onClickPlantDetails(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/branches'}));
    }

    onClickDirectors(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/directors'}));
    }

    viewRecord(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/view'}));
    }
}
