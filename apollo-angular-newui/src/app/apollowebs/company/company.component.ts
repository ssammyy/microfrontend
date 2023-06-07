import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {
    BusinessLines,
    BusinessLinesService,
    BusinessNatures, BusinessNaturesService,
    Company,
    CompanyService,
    County, CountyService, Go, loadCountyId, loadResponsesFailure, loadResponsesSuccess,
    Region, RegionService, selectCompanyData, selectCountyIdData, selectUserInfo,
    Town, TownService
} from '../../core/store';
import {Observable, of, throwError} from 'rxjs';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../core/store/data/std/notification.service";
import {LevyService} from "../../core/store/data/levy/levy.service";
import swal from "sweetalert2";

declare const $: any;
@Component({
    selector: 'app-company',
    templateUrl: './company.component.html',
    styleUrls: ['./company.component.css']
})
export class CompanyComponent implements OnInit {

    status: number;
    stepOneForm: FormGroup = new FormGroup({});
    stepTwoForm: FormGroup = new FormGroup({});
    stepThreeForm: FormGroup = new FormGroup({});
    suspendOperationsForm: FormGroup = new FormGroup({});
    resumeOperationsForm: FormGroup = new FormGroup({});
    closeOperationsForm: FormGroup = new FormGroup({});
    companySoFar: Partial<Company> | undefined;
    // @ts-ignore
    company: Company;
    businessLines$: Observable<BusinessLines[]>;
    businessNatures$: Observable<BusinessNatures[]>;
    region$: Observable<Region[]>;
    county$: Observable<County[]>;
    town$: Observable<Town[]>;
    selectedBusinessLine = 0;
    selectedBusinessNature = 0;
    selectedRegion = 0;
    selectedCounty = 0;
    selectedTown = 0;
    step = 1;
    loadingText: string;
    blob: Blob;
    public uploadedFiles:  FileList;

    constructor(
        private service: CompanyService,
        private linesService: BusinessLinesService,
        private naturesService: BusinessNaturesService,
        private regionService: RegionService,
        private countyService: CountyService,
        private townService: TownService,
        private store$: Store<any>,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private levyService: LevyService,
    ) {
        this.businessNatures$ = naturesService.entities$;
        this.businessLines$ = linesService.entities$;
        this.region$ = regionService.entities$;
        this.county$ = countyService.entities$;
        this.town$ = townService.entities$;
    }


    ngOnInit(): void {
        this.stepOneForm = new FormGroup({
            name: new FormControl('', [Validators.required]),
            registrationNumber: new FormControl('', [Validators.required]),
            kraPin: new FormControl('', [Validators.required]),
            yearlyTurnover: new FormControl('', [Validators.required]),
            directorIdNumber: new FormControl('', [Validators.required]),
            businessLines: new FormControl('', [Validators.required]),
            businessNatures: new FormControl('', [Validators.required]),
        });

        this.stepTwoForm = new FormGroup({
            postalAddress: new FormControl(),
            physicalAddress: new FormControl('', [Validators.required]),
            plotNumber: new FormControl('', [Validators.required]),
            companyEmail: new FormControl('', [Validators.required]),
            companyTelephone: new FormControl('', [Validators.required])
        });
        this.stepThreeForm = new FormGroup({
            buildingName: new FormControl(),
            streetName: new FormControl('', [Validators.required]),
            region: new FormControl('', [Validators.required]),
            county: new FormControl('', [Validators.required]),
            town: new FormControl('', [Validators.required])
        });
        this.store$.select(selectCompanyData).subscribe((d) => {
            console.log(`The id ${d.id}`);
            return this.company = d;
        });
        this.store$.select(selectCompanyData).pipe().subscribe((d) => {
            this.status = d.status;
            return this.status = d.status;
        });

        console.log(this.company)

        this.suspendOperationsForm = new FormGroup({
            name: new FormControl('', [Validators.required]),
            registrationNumber: new FormControl('', [Validators.required]),
            kraPin: new FormControl('', [Validators.required]),
            directorIdNumber: new FormControl('', [Validators.required]),
            id: new FormControl('', [Validators.required]),
            reason: new FormControl('', [Validators.required]),
            dateOfSuspension: new FormControl('', [Validators.required]),
        });
        this.resumeOperationsForm = new FormGroup({
            name: new FormControl('', [Validators.required]),
            registrationNumber: new FormControl('', [Validators.required]),
            kraPin: new FormControl('', [Validators.required]),
            directorIdNumber: new FormControl('', [Validators.required]),
            id: new FormControl('', [Validators.required]),
            reason: new FormControl('', [Validators.required]),
            dateOfSuspension: new FormControl('', [Validators.required]),
        });
        this.closeOperationsForm = new FormGroup({
            name: new FormControl('', [Validators.required]),
            registrationNumber: new FormControl('', [Validators.required]),
            kraPin: new FormControl('', [Validators.required]),
            directorIdNumber: new FormControl('', [Validators.required]),
            id: new FormControl('', [Validators.required]),
            reason: new FormControl('', [Validators.required]),
            dateOfClosure: new FormControl('', [Validators.required]),
        });

        this.stepOneForm.patchValue(this.company);
        this.stepTwoForm.patchValue(this.company);
        this.stepThreeForm.patchValue(this.company);
        this.suspendOperationsForm.patchValue(this.company);
        this.resumeOperationsForm.patchValue(this.company);
        this.closeOperationsForm.patchValue(this.company);
        console.log(`Select town ID inside is ${this.company.town}`);
        this.companySoFar = this.company;

        this.regionService.getAll().subscribe();
        this.countyService.getAll().subscribe();
        // townService.getAll().subscribe();
        this.naturesService.getAll().subscribe();
        this.linesService.getAll().subscribe();
        this.selectedCounty = this.company.county;
        // console.log(`county set to ${this.selectedCounty}`)
        this.store$.dispatch(loadCountyId({payload: this.company.county}));
        this.store$.select(selectCountyIdData).subscribe(
            (d) => {
                if (d) {
                    console.log(`Select county inside is ${d}`);
                    return this.townService.getAll();
                } else {
                    return throwError('Invalid request, Company id is required');
                }
            }
        );
        console.log(`Select town ID inside is ${this.company.town}`);
    }

    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }

    updateSelectedRegion() {
        this.selectedRegion = this.stepThreeForm?.get('region')?.value;
        console.log(`region set to ${this.selectedRegion}`);
    }

    updateSelectedCounty() {

        this.selectedCounty = this.stepThreeForm?.get('county')?.value;
        // console.log(`county set to ${this.selectedCounty}`)
        this.store$.dispatch(loadCountyId({payload: this.company.county}));
        this.store$.select(selectCountyIdData).subscribe(
            (d) => {
                if (d) {
                    console.log(`Select county inside is ${d}`);
                    return this.townService.getAll();
                } else {
                    return throwError('Invalid request, Company id is required');
                }
            }
        );
        console.log(`Select town ID inside is ${this.company.town}`);
        this.stepThreeForm?.get('town').setValue(this.company.town);
    }

    updateSelectedTown() {
        this.selectedTown = this.stepThreeForm?.get('town')?.value;
        console.log(`town set to ${this.selectedTown}`);
    }

    updateSelectedBusinessLine() {
        this.selectedBusinessLine = this.stepOneForm?.get('businessLines')?.value;
    }

    updateSelectedBusinessNatures() {
        this.selectedBusinessNature = this.stepOneForm?.get('businessNatures')?.value;
    }


    onClickPrevious() {
        if (this.step > 1) {
            this.step = this.step - 1;
        } else {
            this.step = 1;
        }
    }

    onClickNext(valid: boolean) {
        if (valid) {
            switch (this.step) {
                case 1:
                    this.companySoFar = {...this.companySoFar, ...this.stepOneForm?.value};
                    break;
                case 2:
                    this.companySoFar = {...this.companySoFar, ...this.stepTwoForm?.value};
                    break;
                case 3:
                    this.companySoFar = {...this.companySoFar, ...this.stepThreeForm?.value};
                    break;
            }
            this.step += 1;
            console.log(`Clicked and step = ${this.step}`);
        }
    }

    onClickSave(valid: boolean) {
        if (valid) {
            this.companySoFar = {...this.companySoFar, ...this.stepThreeForm.value};
            this.company = {...this.company, ...this.companySoFar};

            this.service.update(this.company).subscribe(
                (a) => {

                    this.store$.dispatch(
                        loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Successfully saved ${a.name}`,
                                status: 200
                            }
                        })
                    );
                    return this.store$.dispatch(Go({
                        payload: null,
                        link: 'dashboard/companies',
                        redirectUrl: 'dashboard/companies'
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

    selectStepOneClass(step: number): string {
        if (step === 1) {
            return 'active';
        } else {
            return '';
        }
    }

    selectStepTwoClass(step: number): string {
        console.log(`${step}`);
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        } else {
            return '';
        }
    }

    selectStepThreeClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        } else {
            return '';
        }
    }

    uploadSuspensionForm(): void {
            this.loadingText = "Saving";
            this.SpinnerService.show();
            this.levyService.suspendCompanyOperations(this.suspendOperationsForm.value).subscribe(
                (response) => {
                    console.log(response);
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Record Saved`);
                    this.suspendOperationsForm.reset();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    this.showToasterError('Error', `Error Saving Record`);
                    console.log(error.message);
                }
            );
            this.hideModelSuspension();

    }

    uploadResumptionForm(): void {
        this.loadingText = "Saving";
        this.SpinnerService.show();
        this.levyService.resumeCompanyOperations(this.resumeOperationsForm.value).subscribe(
            (response) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Record Saved`);
                this.resumeOperationsForm.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Saving Record`);
                console.log(error.message);
            }
        );
        this.hideModelSuspension();

    }

    uploadClosureForm(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.levyService.closeCompanyOperations(this.closeOperationsForm.value).subscribe(
            (response) => {
                this.onClickSaveUploads(response.body.savedRowID)
                console.log(response.body.savedRowID)
                this.closeOperationsForm.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.hideModelClosure()
    }
    onClickSaveUploads(operationClosureId: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.loadingText = "Saving...";
            this.SpinnerService.show();
            this.levyService.uploadWindingUpReport(operationClosureId, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Uploaded Successfully',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    console.log(error.message);
                }
            );
        }

    }

    @ViewChild('closeModalSuspension') private closeModalSuspension: ElementRef | undefined;

    public hideModelSuspension() {
        this.closeModalSuspension?.nativeElement.click();
    }

    @ViewChild('closeModalClosure') private closeModalClosure: ElementRef | undefined;

    public hideModelClosure() {
        this.closeModalClosure?.nativeElement.click();
    }

    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }
}
