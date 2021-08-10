import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {LoginCredentials, selectUserInfo} from '../../../core/store';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {
    AllPermitDetailsDto,
    PermitEntityDetails,
    PermitProcessStepDto,
    PlantDetailsDto,
    SectionDto, STA1, STA3
} from '../../../core/store/data/qa/qa.model';
import swal from 'sweetalert2';
import {FileUploadValidators} from '@iplab/ngx-file-upload';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {delay} from 'rxjs/operators';
import {NgxSpinnerService} from 'ngx-spinner';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';

declare const $: any;

@Component({
    selector: 'app-new-dmark-permit',
    templateUrl: './new-dmark-permit.component.html',
    styleUrls: ['./new-dmark-permit.component.css']
})
export class NewDmarkPermitComponent implements OnInit {

    public isLoading = false;
    loading = false;
    fullname = '';
    sta1Form: FormGroup;
    sta3FormA: FormGroup;
    sta3FormB: FormGroup;
    sta3FormC: FormGroup;
    sta3FormD: FormGroup;
    public credential: LoginCredentials;
    returnUrl: string;
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    sta1: STA1;
    sta3: STA3;
    permitProcessStep: PermitProcessStepDto | undefined;
    stepSoFar: | undefined;
    step = 1;
    currBtn = 'A';
    checkN: number;
    public uploadedFile: File;
    public uploadedFiles: FileList;
    public animation = true;
    public multiple = true;

    public permitID!: string;

    private filesControl = new FormControl(null, FileUploadValidators.filesLimit(2));
    DMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;

    public demoForm = new FormGroup({
        files: this.filesControl
    });

    allPermitDetails: AllPermitDetailsDto;

    constructor(private store$: Store<any>,
                private router: Router,
                private route: ActivatedRoute,
                private qaService: QaService,
                private formBuilder: FormBuilder,
                private _loading: LoadingService,
                private SpinnerService: NgxSpinnerService
    ) {
    }

    ngOnInit(): void {

        this.sta1Form = this.formBuilder.group({
            id: [''],
            commodityDescription: ['', Validators.required],
            sectionId: ['', Validators.required],
            permitForeignStatus: ['', Validators.required],
            attachedPlant: ['', Validators.required],
            tradeMark: ['', Validators.required],
            applicantName: []

        });

        this.sta3FormA = this.formBuilder.group({
            id: [''],
            produceOrdersOrStock: ['', Validators.required],
            issueWorkOrderOrEquivalent: ['', Validators.required],
            identifyBatchAsSeparate: ['', Validators.required],
            productsContainersCarryWorksOrder: ['', Validators.required],
            isolatedCaseDoubtfulQuality: ['', Validators.required]

        });

        this.sta3FormB = this.formBuilder.group({
            id: [''],
            headQaQualificationsTraining: ['', Validators.required],
            reportingTo: ['', Validators.required],
            separateQcid: ['', Validators.required],
            testsRelevantStandard: ['', Validators.required],
            spoComingMaterials: ['', Validators.required],
            spoProcessOperations: ['', Validators.required],
            spoFinalProducts: ['', Validators.required],
            monitoredQcs: ['', Validators.required],
            qauditChecksCarried: ['', Validators.required],
            informationQcso: ['', Validators.required],

        });

        this.sta3FormC = this.formBuilder.group({
            id: [''],
            mainMaterialsPurchasedSpecification: ['', Validators.required],
            adoptedReceiptMaterials: ['', Validators.required],
            storageFacilitiesExist: ['', Validators.required],

        });

        this.sta3FormD = this.formBuilder.group({
            id: [''],
            stepsManufacture: ['', Validators.required],
            maintenanceSystem: ['', Validators.required],
            qcsSupplement: ['', Validators.required],
            qmInstructions: ['', Validators.required],
            testEquipmentUsed: ['', Validators.required],
            indicateExternalArrangement: ['', Validators.required],
            levelDefectivesFound: ['', Validators.required],
            levelClaimsComplaints: ['', Validators.required],
            independentTests: ['', Validators.required],
            indicateStageManufacture: ['', Validators.required],

        });


        this.qaService.loadSectionList().subscribe(
            (data: any) => {
                this.sections = data;
                console.log(data);
            }
        );

        this.qaService.loadPlantList().subscribe(
            (data: any) => {
                this.plants = data;
                console.log(data);
            }
        );

        this.getSelectedPermit();

        this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `/dmark`;

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.fullname = u.fullName;
        });

    }

    public getSelectedPermit(): void {
        this.route.fragment.subscribe(params => {
            this.permitID = params;
            console.log(this.permitID);
            if (this.permitID) {
                this.qaService.viewSTA1Details(this.permitID).subscribe(
                    (data) => {
                        this.sta1 = data;
                        this.sta1Form.patchValue(this.sta1);
                    },
                );

                this.qaService.viewSTA3Details(this.permitID).subscribe(
                    (data) => {
                        this.sta3 = data;
                        this.sta3FormA.patchValue(this.sta3);
                        this.sta3FormB.patchValue(this.sta3);
                        this.sta3FormC.patchValue(this.sta3);
                        this.sta3FormD.patchValue(this.sta3);
                    },
                );

            }
        });
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
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
                case 2:
                    this.stepSoFar = {...this.sta3FormA?.value};
                    break;
                case 3:
                    this.stepSoFar = {...this.sta3FormB?.value};
                    break;
                case 4:
                    this.stepSoFar = {...this.sta3FormC?.value};
                    break;
                case 5:
                    this.stepSoFar = {...this.sta3FormD?.value};
                    break;
                case 6:
                    this.stepSoFar = {...this.sta3FormD?.value};
                    break;
            }
            this.step += 1;
            // console.log(`Clicked and step = ${this.step}`);
        }
    }


    get formSta1Form(): any {
        return this.sta1Form.controls;
    }

    get formSta3FormA(): any {
        return this.sta3FormA.controls;
    }

    get formSta3FormB(): any {
        return this.sta3FormB.controls;
    }

    get formSta3FormC(): any {
        return this.sta3FormC.controls;
    }

    get formSta3FormD(): any {
        return this.sta3FormD.controls;
    }

    onClickSaveSTA1(valid: boolean) {
        if (valid) {
            if (this.sta1 == null) {
                this.SpinnerService.show();
                this.qaService.savePermitSTA1(String(this.DMarkTypeID), this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        this.onClickUpdateStep(this.step);
                        console.log(data);
                        this.SpinnerService.hide();
                        this.step += 1;
                        this.currBtn = 'B';
                        this.isLoading = false;
                        swal.fire({
                            title: 'STA1 Form saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.SpinnerService.show();
                this.qaService.updatePermitSTA1(String(this.sta1.id), this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        this.onClickUpdateStep(this.step);
                        this.step += 1;
                        this.isLoading = false;
                        this.SpinnerService.hide();
                        console.log(data);
                        swal.fire({
                            title: 'STA1 Form updated!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            }
        }
    }

    onClickSaveSTA3A(valid: boolean) {
        if (valid) {
            if (this.sta3 == null) {
                this.SpinnerService.show();
                console.log(this.sta1.id.toString());
                this.sta3 = {...this.sta3, ...this.sta3FormA.value};
                this.qaService.savePermitSTA3(this.sta1.id.toString(), this.sta3).subscribe(
                    (data: any) => {
                        this.onClickUpdateStep(this.step);
                        console.log(data);
                        this.SpinnerService.hide();
                        this.sta3 = data;
                        this.step += 1;
                        swal.fire({
                            title: 'STA3: Factory Organisation Details saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.SpinnerService.show();
                this.sta3 = {...this.sta3, ...this.sta3FormA.value};
                this.qaService.updatePermitSTA3(this.sta1.id.toString(), this.sta3).subscribe(
                    (data: any) => {
                        this.onClickUpdateStep(this.step);
                        console.log(data);
                        this.SpinnerService.hide();
                        this.sta3 = data;
                        this.step += 1;
                        swal.fire({
                            title: 'STA3: Quality Control/Inspection Staff Details saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            }
        }
    }

    onClickUpdateSTA3B(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.sta3 = {...this.sta3, ...this.sta3FormB.value};
            this.qaService.updatePermitSTA3(this.sta1.id.toString(), this.sta3).subscribe(
                (data: any) => {
                    this.onClickUpdateStep(this.step);
                    console.log(data);
                    this.SpinnerService.hide();
                    this.sta3 = data;
                    this.step += 1;
                    swal.fire({
                        title: 'STA3: Factory Organisation Details updated!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        }
    }

    onClickUpdateSTA3C(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.sta3 = {...this.sta3, ...this.sta3FormC.value};
            this.qaService.updatePermitSTA3(this.sta1.id.toString(), this.sta3).subscribe(
                (data: any) => {
                    this.onClickUpdateStep(this.step);
                    console.log(data);
                    this.SpinnerService.hide();
                    this.sta3 = data;
                    this.step += 1;
                    swal.fire({
                        title: 'STA3: Quality Control/Inspection Staff Details saved!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        }
    }

    onClickUpdateSTA3D(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.sta3 = {...this.sta3, ...this.sta3FormD.value};
            this.qaService.updatePermitSTA3(this.sta1.id.toString(), this.sta3).subscribe(
                (data: any) => {
                    this.onClickUpdateStep(this.step);
                    console.log(data);
                    this.SpinnerService.hide();
                    this.sta3 = data;
                    this.step += 1;
                    swal.fire({
                        title: 'STA3 Form Completed! Proceed to Upload attachments.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        }
    }

    onClickUpdateStep(stepNumber: number) {
        if (this.sta1) {
            this.permitProcessStep = new PermitProcessStepDto();
            this.permitProcessStep.permitID = this.sta1.id;
            this.permitProcessStep.processStep = stepNumber;
            this.qaService.savePermitProcessStep(this.permitProcessStep).subscribe(
                (data) => {
                    // this.sta1 = data;
                },
            );
        }
    }

    goToPermit() {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.qaService.uploadSTA3File(this.sta1.id.toString(), formData).subscribe(
                (data: any) => {
                    this.onClickUpdateStep(this.step);
                    this.SpinnerService.hide();
                    console.log(data);
                    this.step += 1;
                    swal.fire({
                        title: 'STA3 Form Completed! Proceed to submit application.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    // this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});
                },
            );
            this.router.navigate(['/permitdetails'], {fragment: String(this.sta1.id)});
        }
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
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

    listenToLoading(): void {
        this._loading.loadingSub
            .pipe(delay(0)) // This prevents a ExpressionChangedAfterItHasBeenCheckedError for subsequent requests
            .subscribe((loading) => {
                this.loading = loading;
            });
    }

}
