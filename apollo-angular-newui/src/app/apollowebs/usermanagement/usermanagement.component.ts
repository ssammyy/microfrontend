import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
    Go,
    loadResponsesFailure,
    loadResponsesSuccess,
    selectUserInfo,
    UserEntityDto,
    UserEntityService
} from '../../core/store';
import {Observable, of, Subject} from 'rxjs';
import {Titles, TitlesService} from '../../core/store/data/title';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {LoadingService} from '../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {MasterService} from '../../core/store/data/master/master.service';
import {
    Counties,
    Department,
    DesignationEntityDto,
    DirectoratesEntityDto,
    DivisionDetails,
    RegionsEntityDto,
    SectionsEntityDto,
    SubSectionsL1EntityDto,
    SubSectionsL2EntityDto,
    TitlesEntityDto,
    Towns
} from '../../../../../apollo-webs/src/app/shared/models/master-data-details';
import swal from 'sweetalert2';
import {DataTableDirective} from "angular-datatables";
import {SACSummary} from "../../core/store/data/std/request_std.model";

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

    tasks: UserEntityDto[] = [];

    title$: Observable<Titles[]>;
    addUserFormGroup: FormGroup = new FormGroup({});
    public dataTable!: DataTable;
    public dataTable2!: DataTable;
    public dataTable3!: DataTable;

    public directoratesEntityDto!: DirectoratesEntityDto[];
    public designationEntityDto!: DesignationEntityDto[];
    public regionsEntityDto!: RegionsEntityDto[];
    public countiesDto!: Counties[];
    public townsDto!: Towns[];
    public departmentDto!: Department[];
    public divisionDto!: DivisionDetails[];
    public sectionsEntityDto!: SectionsEntityDto[];
    public subSectionsL1EntityDto!: SubSectionsL1EntityDto[];
    public subSectionsL2EntityDto!: SubSectionsL2EntityDto[];

    titles!: TitlesEntityDto[];
    directorates: DirectoratesEntityDto[] = [];
    designations: DesignationEntityDto[] = [];
    department: Department[] = [];
    division: DivisionDetails[] = [];
    sections: SectionsEntityDto[] = [];
    l1SubSubSections: SubSectionsL1EntityDto[] = [];
    l2SubSubSections: SubSectionsL2EntityDto[] = [];
    counties: Counties[] = [];
    towns: Towns[] = [];
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;
    loadingText: string;

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

    get formAddUser(): any {
        return this.addUserFormGroup.controls;
    }

    ngOnInit(): void {
        this.loadingText = "Retrieving Data Please Wait ...."
        this.SpinnerService.show();
        let formattedArray = [];
        this.SpinnerService.show();
        this.masterService.loadUsers().subscribe(
            (data: any) => {
                this.SpinnerService.hide();
                this.tasks = data;

                formattedArray = data.map(i => [i.id, i.firstName, i.lastName, i.userName, i.email, i.registrationDate, i.status]);
                this.SpinnerService.hide();
                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }

                // this.dataTable = {
                //     headerRow: ['Full Name', 'User name', 'Email', 'Date Registered', 'Status', 'Actions'],
                //     footerRow: ['Full Name', 'User name', 'Email', 'Date Registered', 'Status', 'Actions'],
                //     dataRows: formattedArray
                // };
            });


        //
        // this.dataTable2 = {
        //     headerRow: ['Role Name', 'Functions', 'Modules', 'Actions'],
        //     footerRow: ['Role Name', 'Functions', 'Modules', 'Actions'],
        //
        //     dataRows: [
        //         ['STC Secretary', 'View Standards', 'Standards', 'btn-round'],
        //
        //     ]
        // };
        // this.dataTable3 = {
        //     headerRow: ['Role Name', 'Menu', 'Submenus', 'Actions'],
        //     footerRow: ['Role Name', 'Menu', 'Submenu', 'Actions'],
        //
        //     dataRows: [
        //         ['STC Secretary', 'Request', 'View Standard Request,Edit Standard Request', 'btn-round'],
        //
        //     ]
        // };

        this.getALlRecommendedDetails();

        this.addUserFormGroup = this.formBuilder.group({
            title: ['', Validators.required],
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
        });
        //
        // this.store$.select(selectUserInfo).subscribe(
        //     (l) => {
        //         console.log(`The id is ${l.id}`);
        //         return this.service.getByKey(`${l.id}/`).subscribe(
        //             (u) => {
        //                 console.log(`The id is ${l.id} vs ${u.id}`);
        //                 this.userManagementForm.patchValue(u);
        //                 return this.user = u;
        //             }, catchError((err: HttpErrorResponse) => {
        // tslint:disable-next-line:max-line-length
        //                 console.log((err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`);
        //                 return of(loadResponsesFailure({
        //                     error: {
        //                         payload: 'Your request could not be processed at the moment, try again later.',
        //                         status: err.status,
        //                         response: '99'
        //                     }
        //                 }));
        //             })
        //         );
        //     }
        // );


    }

    public getALlRecommendedDetails(): void {
        this.masterService.loadTitlesSystemAdmin(1).subscribe(
            (data: any) => {
                this.titles = data;
                console.log(data);
            }
        );

        this.masterService.loadDirectorateSystemAdmin(1).subscribe(
            (data: any) => {
                this.directoratesEntityDto = data;
                console.log(data);
            }
        );

        this.masterService.loadDesignationsSystemAdmin(1).subscribe(
            (data: any) => {
                this.designationEntityDto = data;
                console.log(data);
            }
        );

        this.masterService.loadDepartmentsSystemAdmin(1).subscribe(
            (data: any) => {
                this.departmentDto = data;
                console.log(data);
            }
        );

        this.masterService.loadDivisionsSystemAdmin(1).subscribe(
            (data: any) => {
                this.divisionDto = data;
                console.log(data);
            }
        );

        this.masterService.loadSectionSystemAdmin(1).subscribe(
            (data: any) => {
                this.sectionsEntityDto = data;
                console.log(data);
            }
        );

        this.masterService.loadL1SubSubSectionSystemAdmin(1).subscribe(
            (data: any) => {
                this.subSectionsL1EntityDto = data;
                console.log(data);
            }
        );

        this.masterService.loadL2SubSubSectionSystemAdmin(1).subscribe(
            (data: any) => {
                this.subSectionsL2EntityDto = data;
                console.log(data);
            }
        );

        this.masterService.loadRegionsSystemAdmin(1).subscribe(
            (data: any) => {
                this.regionsEntityDto = data;
                console.log(data);
            }
        );

        this.masterService.loadCountiesSystemAdmin(1).subscribe(
            (data: any) => {
                this.countiesDto = data;
                console.log(data);
            }
        );

        this.masterService.loadTownsSystemAdmin(1).subscribe(
            (data: any) => {
                this.townsDto = data;
                console.log(data);
            }
        );
    }

    onSelectDirectorate(directorateID: number): any {
        this.designations = this.designationEntityDto.filter((item) => {
            return item.directorateId === Number(directorateID);
        });
        this.department = this.departmentDto.filter((item1) => {
            return item1.directorateId === Number(directorateID);
        });
    }

    onSelectDepartment(departmentID: number): any {
        this.division = this.divisionDto.filter((item) => {
            return item.departmentId === Number(departmentID);
        });
    }

    onSelectDivision(divisionID: number): any {
        this.sections = this.sectionsEntityDto.filter((item) => {
            return item.divisionId === Number(divisionID);
        });
    }

    onSelectSection(sectionID: number): any {
        this.l1SubSubSections = this.subSectionsL1EntityDto.filter((item) => {
            return item.sectionId === Number(sectionID);
        });
    }

    onSelectL1SubSubSection(l1SubSubSectionID: number): any {
        this.l2SubSubSections = this.subSectionsL2EntityDto.filter((item) => {
            return item.subSectionL1Id === Number(l1SubSubSectionID);
        });
    }

    onSelectRegion(regionID: number): any {
        this.counties = this.countiesDto.filter((item) => {
            return item.regionId === Number(regionID);
        });
    }

    onSelectCounty(countyID: number): any {
        this.towns = this.townsDto.filter((item) => {
            return item.countyId === Number(countyID);
        });
    }


    gotoHome() {
        this.router.navigate(['/home']);  // define your component where you want to go

    }

    onClickSave() {

        this.masterService.registerEmployee(this.addUserFormGroup.value).subscribe(
            (data: any) => {
                this.user = data;
                swal.fire({
                    title: 'User Created Successfully',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                this.router.navigate(['/userDetails'], {fragment: String(this.user.id)});
            });
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

            const data = table.row($tr).data();
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
