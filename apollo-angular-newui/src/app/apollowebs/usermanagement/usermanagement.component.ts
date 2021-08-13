import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
    Go,
    loadResponsesFailure,
    loadResponsesSuccess,
    selectUserInfo,
    UserEntityDto,
    UserEntityService
} from '../../core/store';
import {Observable, of} from 'rxjs';
import {Titles, TitlesService} from '../../core/store/data/title';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {LoadingService} from '../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {MasterService} from '../../core/store/data/master/master.service';

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;


@Component({
    selector: 'app-usermanagement',
    templateUrl: './usermanagement.component.html',
    styleUrls: ['./usermanagement.component.css']
})
export class UsermanagementComponent implements OnInit {

    user: UserEntityDto;
    title$: Observable<Titles[]>;
    userManagementForm: FormGroup = new FormGroup({});
    public dataTable: DataTable;
    public dataTable2: DataTable;
    public dataTable3: DataTable;


    constructor(private store$: Store<any>,
                private _loading: LoadingService,
                private SpinnerService: NgxSpinnerService,
                private service: UserEntityService,
                private masterService: MasterService,
                private titleService: TitlesService,
                private router: Router,
                private formBuilder: FormBuilder) {
        this.title$ = titleService.entities$;
        titleService.getAll().subscribe();
    }

    ngOnInit(): void {
        let formattedArray = [];
        this.SpinnerService.show();
        this.masterService.loadUsers().subscribe(
            (data: any) => {
                formattedArray = data.map(i => [i.id, i.firstName, i.lastName, i.userName, i.email, i.registrationDate, i.status]);

                this.dataTable = {
                    headerRow: ['Full Name', 'User name', 'Email', 'Date Registered', 'Status', 'Actions'],
                    footerRow: ['Full Name', 'User name', 'Email', 'Date Registered', 'Status', 'Actions'],
                    dataRows: formattedArray
                };
            });



        this.dataTable2 = {
            headerRow: ['Role Name', 'Functions', 'Modules', 'Actions'],
            footerRow: ['Role Name', 'Functions', 'Modules', 'Actions'],

            dataRows: [
                ['STC Secretary', 'View Standards', 'Standards', 'btn-round'],

            ]
        };
        this.dataTable3 = {
            headerRow: ['Role Name', 'Menu', 'Submenus', 'Actions'],
            footerRow: ['Role Name', 'Menu', 'Submenu', 'Actions'],

            dataRows: [
                ['STC Secretary', 'Request', 'View Standard Request,Edit Standard Request', 'btn-round'],

            ]
        };

        this.userManagementForm = this.formBuilder.group({
                id: [''],
                email: ['', Validators.required],
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                personalContactNumber: ['', Validators.required],
                directorate: ['', Validators.required],
                designation: ['', Validators.required],
                department: ['', Validators.required],
                division: ['', Validators.required],
                section: ['', null],
                l1SubSubSection: ['', null],
                l2SubSubSection: ['', null],
                region: ['', Validators.required],
                county: ['', Validators.required],
                town: ['', Validators.required],
            },
            {
                // validators: ConfirmedValidator('credentials', 'confirmCredentials')
            });
        this.store$.select(selectUserInfo).subscribe(
            (l) => {
                console.log(`The id is ${l.id}`);
                return this.service.getByKey(`${l.id}/`).subscribe(
                    (u) => {
                        console.log(`The id is ${l.id} vs ${u.id}`);
                        this.userManagementForm.patchValue(u);
                        return this.user = u;
                    }, catchError((err: HttpErrorResponse) => {
                        console.log((err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`);
                        return of(loadResponsesFailure({
                            error: {
                                payload: 'Your request could not be processed at the moment, try again later.',
                                status: err.status,
                                response: '99'
                            }
                        }));
                    })
                );
            }
        );


    }
    gotoHome() {
        this.router.navigate(['/home']);  // define your component where you want to go

    }
    onClickSave(valid: boolean) {
        if (valid) {


            this.user = {...this.user, ...this.userManagementForm.value};


            this.service.update(this.user).subscribe(
                (a) => {

                    this.store$.dispatch(
                        loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Profile Successfully Updated.`,
                                status: 200
                            }
                        })
                    );
                    return this.store$.dispatch(Go({
                        payload: null,
                        link: 'profile',
                        redirectUrl: 'profile'
                    }));


                },
                catchError(
                    (err: HttpErrorResponse) => {
                        return of(loadResponsesFailure({
                            error: {
                                payload: err.error,
                                status: err.status,
                                response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                            }
                        }));
                    }));


        } else {
            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Some required details are missing, kindly recheck',
                    status: 100,
                    response: '05'
                }
            }));
        }
    }

    ngAfterViewInit() {
        $(`#datatables`).DataTable({
            'pagingType': 'full_numbers',
            'lengthMenu': [
                [10, 25, 50, -1],
                [10, 25, 50, 'All']
            ],
            responsive: true,
            language: {
                search: '_INPUT_',
                searchPlaceholder: 'Search records',
            }

        });
        $(`#datatablescd`).DataTable({
            'pagingType': 'full_numbers',
            'lengthMenu': [
                [10, 25, 50, -1],
                [10, 25, 50, 'All']
            ],
            responsive: true,
            language: {
                search: '_INPUT_',
                searchPlaceholder: 'Search records',
            }

        });

        const table = $('#datatables').DataTable();



        // Edit record
        table.on('click', '.edit', function (e) {
            let $tr = $(this).closest('tr');
            if ($($tr).hasClass('child')) {
                $tr = $tr.prev('.parent');
            }

            let data = table.row($tr).data();
        });

        // Delete a record
        table.on('click', '.remove', function (e) {
            const $tr = $(this).closest('tr');
            table.row($tr).remove().draw();
            e.preventDefault();
        });

        // Like record
        // table.on('click', '.like', function (e) {
        //     alert('You clicked on Like button');
        //     e.preventDefault();
        // });

        $('.card .material-datatables label').addClass('form-group');
    }

}
