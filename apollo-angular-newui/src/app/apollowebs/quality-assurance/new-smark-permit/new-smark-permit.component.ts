import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {
    AllSTA10DetailsDto,
    FilesListDto,
    PermitEntityDto,
    PermitProcessStepDto,
    PlantDetailsDto,
    SectionDto,
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto
} from '../../../core/store/data/qa/qa.model';
// permits
import {FileUploadValidators} from '@iplab/ngx-file-upload';
import {selectUserInfo, UserEntityService} from '../../../core/store';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {TitlesService} from "../../../core/store/data/title";
import swal from "sweetalert2";
import {NotificationService} from "../../../core/store/data/std/notification.service";

declare const $: any;

@Component({
    selector: 'app-new-smark-permit',
    templateUrl: './new-smark-permit.component.html',
    styleUrls: ['./new-smark-permit.component.css']
})
export class NewSmarkPermitComponent implements OnInit {
    fullname = '';
    status: number;
    loading = false;
    loadingText: string;

    setCloned = false;

    hideCloneButton = false;

    SelectedSectionId;
    sta1Form: FormGroup;
    sta10Form: FormGroup;
    sta10FormA: FormGroup;
    sta10FormB: FormGroup;
    sta10FormC: FormGroup;
    sta10FormD: FormGroup;
    sta10FormE: FormGroup;
    sta10FormF: FormGroup;
    sta10FormG: FormGroup;
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    sta1: STA1;
    allSta10Details: AllSTA10DetailsDto;
    permitProcessStep: PermitProcessStepDto | undefined;
    sta10Details: Sta10Dto;
    sta10FilesList: FilesListDto[] = [];
    sta10ProductsManufactureDetails: STA10ProductsManufactureDto[] = [];
    sta10ProductsManufactureDetail: STA10ProductsManufactureDto;
    sta10RawMaterialsDetails: STA10RawMaterialsDto[] = [];
    sta10RawMaterialsDetail: STA10RawMaterialsDto;
    sta10PersonnelDetails: STA10PersonnelDto[] = [];
    sta10PersonnelDetail: STA10PersonnelDto;
    sta10MachineryAndPlantDetails: STA10MachineryAndPlantDto [] = [];
    sta10MachineryAndPlantDetail: STA10MachineryAndPlantDto;
    sta10ManufacturingProcessDetails: STA10ManufacturingProcessDto  [] = [];
    sta10ManufacturingProcessDetail: STA10ManufacturingProcessDto;
    currBtn = 'A';
    stepSoFar: | undefined;
    step = 1;

    public uploadedFiles: FileList;
    public animation = false;
    public multiple = false;

    public permitID!: string;

    private filesControl = new FormControl(null, FileUploadValidators.filesLimit(2));

    public demoForm = new FormGroup({
        files: this.filesControl
    });
    smarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID);
    public allPermitData: PermitEntityDto[];

    selectedPermit: string;

    cloned: boolean;


    constructor(private store$: Store<any>,
                private router: Router,
                private qaService: QaService,
                private formBuilder: FormBuilder,
                private _loading: LoadingService,
                private SpinnerService: NgxSpinnerService,
                private route: ActivatedRoute,
                private service: UserEntityService,
                private titleService: TitlesService,
                private notifyService: NotificationService,
    ) {
    }

    ngOnInit(): void {
        this.cloned = false;
        this.sta1Form = this.formBuilder.group({
            commodityDescription: ['', Validators.required],
            applicantName: [],
            sectionId: ['', Validators.required],
            permitForeignStatus: [],
            attachedPlant: ['', Validators.required],
            tradeMark: ['', Validators.required],
            createFmark: [],
            // inputCountryCode: ['', Validators.required,Validators.pattern("[0-9 ]{11}")]


        });
        this.sta10Form = this.formBuilder.group({
            // firmName: ['', Validators.required],
            // statusCompanyBusinessRegistration: ['', Validators.required],
            // ownerNameProprietorDirector: ['', Validators.required],
            // postalAddress: ['', Validators.required],
            // contactPerson: ['', Validators.required],
            // telephone: ['', Validators.required],
            // emailAddress: ['', Validators.required],
            // physicalLocationMap: ['', Validators.required],
            // county: ['', Validators.required],
            // town: ['', Validators.required],
            totalNumberFemale: ['', Validators.required],
            totalNumberMale: ['', Validators.required],
            totalNumberPermanentEmployees: ['', Validators.required],
            totalNumberCasualEmployees: ['', Validators.required],
            averageVolumeProductionMonth: ['', Validators.required]

        });


        this.sta10FormA = this.formBuilder.group({
            personnelName: [],
            qualificationInstitution: [],
            dateOfEmployment: []

        });

        this.sta10FormB = this.formBuilder.group({
            productName: [],
            productBrand: [],
            // productStandardNumber: [],
            // available: [],
            // permitNo: []
        });

        this.sta10FormC = this.formBuilder.group({
            name: [],
            origin: [],
            specifications: [],
            qualityChecksTestingRecords: []
        });

        this.sta10FormD = this.formBuilder.group({
            typeModel: [],
            machineName: [],
            countryOfOrigin: [],
        });

        this.sta10FormE = this.formBuilder.group({
            processFlowOfProduction: [],
            operations: [],
            criticalProcessParametersMonitored: [],
            frequency: [],
            processMonitoringRecords: []
        });
        this.sta10FormG = this.formBuilder.group({});

        this.qaService.loadCloneSmarkPermitList(this.smarkID.toString()).subscribe(
            (data: any) => {
                this.allPermitData = data;
            });

        this.sta10FormF = this.formBuilder.group({
            handledManufacturingProcessRawMaterials: ['', Validators.required],
            handledManufacturingProcessInprocessProducts: ['', Validators.required],
            handledManufacturingProcessFinalProduct: ['', Validators.required],
            strategyInplaceRecallingProducts: ['', Validators.required],
            stateFacilityConditionsRawMaterials: ['', Validators.required],
            stateFacilityConditionsEndProduct: ['', Validators.required],
            testingFacilitiesExistSpecifyEquipment: ['', Validators.required],
            testingFacilitiesExistStateParametersTested: ['', Validators.required],
            testingFacilitiesSpecifyParametersTested: ['', Validators.required],
            calibrationEquipmentLastCalibrated: ['', Validators.required],
            handlingConsumerComplaints: ['', Validators.required],
            companyRepresentative: ['', Validators.required],
            applicationDate: ['', Validators.required]
        });
        // this.store$.select(selectCompanyInfoDtoStateData).subscribe(
        //     (d) => {
        //         if (d) {
        //             console.log(d)
        //            //(`${d.penaltyStatus}`);
        //             // return this.status = d.status;
        //             if(d.status == 0)
        //             {
        //                //("lklklklkl")
        //                 swal.fire({
        //                     allowOutsideClick: false,
        //                     allowEscapeKey: false,
        //                     title: 'Cancelled',
        //                     text: 'Your Company Has Been Closed.You Cannot Apply For A Permit.',
        //                     icon: 'error',
        //                     customClass: {confirmButton: "btn btn-info",},
        //                     buttonsStyling: false
        //                 }).then((result) => {
        //                     if (result.value) {
        //                         window.location.href = "/dashboard";
        //                     }
        //                 } )
        //             }
        //             else if(d.status==2)
        //             {
        //                 swal.fire({
        //                     allowOutsideClick: false,
        //                     allowEscapeKey: false,
        //                     title: 'Cancelled',
        //                     text: 'Your Company Has Been Suspended.You Cannot Apply For A Permit.',
        //                     icon: 'error',
        //                     customClass: {confirmButton: "btn btn-info",},
        //                     buttonsStyling: false
        //                 }).then((result) => {
        //                     if (result.value) {
        //                         window.location.href = "/dashboard";
        //                     }
        //                 } )
        //             }
        //             else {
        //             if(d.penaltyStatus == 1){
        //                 swal.fire({
        //                     allowOutsideClick: false,
        //                     allowEscapeKey: false,
        //                     title: 'Pending Levy Remittance',
        //                     text: 'Your Company has defaulted on Levy Payment.You are Reminded to Remit your Monthly Payment.',
        //                     icon: 'error',
        //                     customClass: {confirmButton: "btn btn-info",},
        //                     buttonsStyling: false
        //                 }).then((result) => {
        //                     // if (result.value) {
        //                     //     window.location.href = "/dashboard";
        //                     // }
        //                 } )
        //             }
        //
        //             else{


        this.qaService.loadSectionList().subscribe(
            (data: any) => {
                this.sections = data;
                //(data);
            }
        );

        this.qaService.loadPlantList().subscribe(
            (data: any) => {
                this.plants = data;
                //(data);
            }
        );

        this.getSelectedPermit();

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.fullname = u.fullName;
        });
        //  }


        //  }


        // permits


    }


    public getSelectedPermit(): void {
        this.route.fragment.subscribe(params => {
            this.permitID = params;
            //(this.permitID);
            if (this.permitID) {
                this.SpinnerService.show();
                this.qaService.viewSTA1Details(this.permitID).subscribe(
                    (data) => {
                        this.SpinnerService.hide();
                        this.sta1 = data;
                        this.sta1Form.patchValue(this.sta1);
                        this.qaService.viewSTA10Details(String(this.sta1.id)).subscribe(
                            (data1) => {
                                this.allSta10Details = data1;

                                //('TEST ALL STA10' + this.allSta10Details);
                                // this.sta10Details = this.allSta10Details.sta10FirmDetails;
                                this.sta10Form.patchValue(this.allSta10Details.sta10FirmDetails);
                                this.sta10PersonnelDetails = this.allSta10Details.sta10PersonnelDetails;
                                this.sta10ProductsManufactureDetails = this.allSta10Details.sta10ProductsManufactureDetails;
                                this.sta10RawMaterialsDetails = this.allSta10Details.sta10RawMaterialsDetails;
                                this.sta10MachineryAndPlantDetails = this.allSta10Details.sta10MachineryAndPlantDetails;
                                this.sta10ManufacturingProcessDetails = this.allSta10Details.sta10ManufacturingProcessDetails;
                                this.sta10ManufacturingProcessDetails = this.allSta10Details.sta10ManufacturingProcessDetails;
                                this.sta10FilesList = this.allSta10Details.sta10FilesList;
                                this.sta10FormF.patchValue(this.allSta10Details.sta10FirmDetails);

                            },
                        );
                    },
                );


            }
        });
    }


    public clonePermit(): void {
        this.loading = true
        this.loadingText = "Cloning"
        this.SpinnerService.show();
        this.route.fragment.subscribe(params => {
            this.permitID = this.selectedPermit;
            //(this.permitID);
            if (this.permitID) {
                this.SpinnerService.show();
                this.qaService.viewSTA1Details(this.permitID).subscribe(
                    (data) => {
                        this.SpinnerService.hide();
                        this.sta1 = data;
                        this.sta1Form.patchValue(this.sta1);
                        this.qaService.viewSTA10Details(String(this.sta1.id)).subscribe(
                            (data1) => {
                                this.allSta10Details = data1;

                                //('TEST ALL STA10' + this.allSta10Details);
                                this.sta10Details = this.allSta10Details.sta10FirmDetails;
                                this.sta10Form.patchValue(this.allSta10Details.sta10FirmDetails);
                                this.sta10PersonnelDetails = this.allSta10Details.sta10PersonnelDetails;
                                this.sta10ProductsManufactureDetails = this.allSta10Details.sta10ProductsManufactureDetails;
                                this.sta10RawMaterialsDetails = this.allSta10Details.sta10RawMaterialsDetails;
                                this.sta10MachineryAndPlantDetails = this.allSta10Details.sta10MachineryAndPlantDetails;
                                this.sta10ManufacturingProcessDetails = this.allSta10Details.sta10ManufacturingProcessDetails;
                                // this.sta10ManufacturingProcessDetails = this.allSta10Details.sta10ManufacturingProcessDetails;
                                this.sta10FilesList = this.allSta10Details.sta10FilesList;
                                this.sta10FormF.patchValue(this.allSta10Details.sta10FirmDetails);


                                // if(this.sta10FilesList.map())
                                // {
                                //
                                // }
                            },
                        );
                    },
                );

                this.setCloned = true
                this.loading = false
                this.hideCloneButton = false;
                this.SpinnerService.hide();


            }
        });
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
                    this.stepSoFar = {...this.sta10Form?.value};
                    break;
                case 3:
                    this.stepSoFar = {...this.sta10FormA?.value};
                    break;
                case 4:
                    this.stepSoFar = {...this.sta10FormB?.value};
                    break;
                case 5:
                    this.stepSoFar = {...this.sta10FormC?.value};
                    break;
                case 6:
                    this.stepSoFar = {...this.sta10FormD?.value};
                    break;
                case 7:
                    this.stepSoFar = {...this.sta10FormE?.value};
                    break;
                case 8:
                    this.stepSoFar = {...this.sta10FormF?.value};
                    break;
                case 9:
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
            }
            this.step += 1;
            //(`Clicked and step = ${this.step}`);
        }
    }


    get formSta1Form(): any {
        return this.sta1Form.controls;
    }

    get formSta10Form(): any {
        return this.sta10Form.controls;
    }

    get formSta10FormA(): any {
        return this.sta10FormA.controls;
    }

    get formSta10FormB(): any {
        return this.sta10FormB.controls;
    }

    get formSta10FormC(): any {
        return this.sta10FormC.controls;
    }

    get formSta10FormD(): any {
        return this.sta10FormD.controls;
    }

    get formSta10FormE(): any {
        return this.sta10FormE.controls;
    }

    get formSta10FormF(): any {
        return this.sta10FormF.controls;
    }

    onClickSaveSTA1(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.sta1 == null || this.setCloned == true) {
                this.SpinnerService.show();
                this.qaService.savePermitSTA1('2', this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false;
                        //(data);
                        this.step += 1;
                        this.currBtn = 'B';
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
                        this.loading = false;
                        this.step += 1;
                        this.SpinnerService.hide();
                        //(data);
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

    onClickSaveSTA10(valid: boolean) {
        if (valid) {
            this.loading = true

            this.SpinnerService.show();
            //(this.sta1.id.toString());
            if (this.sta10Details == null || this.setCloned == true) {
                this.qaService.saveFirmDetailsSta10(this.sta1.id.toString(), this.sta10Form.value).subscribe(
                    (data) => {
                        this.sta10Details = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false

                        //(data);
                        this.step += 1;
                        swal.fire({
                            title: 'Firm Details Saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
// this.router.navigate(['/users-list']);
                    },
                );
            } else {
                this.loading = true

                this.SpinnerService.show();
                this.qaService.updateFirmDetailsSta10(`${this.sta1.id}`, this.sta10Form.value).subscribe(
                    (data) => {
                        this.sta10Details = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        //(data);
                        this.step += 1;
                        this.loading = false

                        swal.fire({
                            title: 'Firm Details Updated!',
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

    onClickSaveSTA10F(valid: boolean) {
        if (valid) {
            this.loading = true

            this.SpinnerService.show();
            //(this.sta1.id.toString());

            this.qaService.updateFirmDetailsSta10(this.sta1.id.toString(), this.sta10FormF.value).subscribe(
                (data) => {
                    this.sta10Details = data;
                    this.onClickUpdateStep(this.step);
                    this.SpinnerService.hide();
                    this.loading = false

                    //(data);
                    this.step += 1;
                    swal.fire({
                        title: 'Non-Conforming Products Manufacturing Process saved!.',
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

    // onClickSaveSTA10G(valid: boolean) {
    //     if (valid) {
    //        //(this.sta1.id.toString());
    //         swal.fire({
    //             title: 'STA10 Form Completed! Proceed to submit application.!',
    //             buttonsStyling: false,
    //             customClass: {
    //                 confirmButton: 'btn btn-success form-wizard-next-btn ',
    //             },
    //             icon: 'success'
    //         });
    //         this.router.navigate(['/smarkpermitdetails'], {fragment: this.sta1.id.toString()});
    //
    //
    //     }
    // }

    onClickSaveSTAPersonnel(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.sta10PersonnelDetails.length > 0) {
                this.SpinnerService.show();
                //(this.sta10Details.id.toString());
                this.qaService.savePersonnelDetailsSta10(this.sta10Details.id.toString(), this.sta10PersonnelDetails).subscribe(
                    (data) => {
                        this.sta10PersonnelDetails = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false

                        //(data);
                        this.step += 1;
                        swal.fire({
                            title: 'Key Personnel Details Saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = false

                swal.fire({
                    title: 'Key Personnel Details missing!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'error'
                });
            }
        }
    }

    onClickSaveSTAProductsManufactured(valid: boolean) {
        if (valid) {
            //(this.sta10Details.id.toString());
            this.loading = true

            if (this.sta10ProductsManufactureDetails.length === 0) {
                this.SpinnerService.show();
                this.sta10ProductsManufactureDetail = this.sta10FormB.value;
                this.sta10ProductsManufactureDetails.push(this.sta10ProductsManufactureDetail);
                // tslint:disable-next-line:max-line-length
                this.qaService.saveProductsManufacturedDetailsSta10(this.sta10Details.id.toString(), this.sta10ProductsManufactureDetails).subscribe(
                    (data) => {
                        this.sta10ProductsManufactureDetails = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false

                        //(data);
                        this.step += 1;
                        swal.fire({
                            title: 'Products being Manufactured Saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = false

                this.step += 1;
            }

        }
    }

    onClickSaveSTARawMaterials(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.sta10RawMaterialsDetails.length > 0) {
                this.SpinnerService.show();
                //(this.sta10Details.id.toString());
                this.qaService.saveRawMaterialsDetailsSta10(this.sta10Details.id.toString(), this.sta10RawMaterialsDetails).subscribe(
                    (data) => {
                        this.sta10RawMaterialsDetails = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false

                        //(data);
                        this.step += 1;
                        swal.fire({
                            title: 'Raw Materials Details saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = false

                swal.fire({
                    title: 'Raw Materials Details missing!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'error'
                });


            }
        }
    }

    onClickSaveSTAMachineryPlant(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.sta10MachineryAndPlantDetails.length > 0) {
                this.SpinnerService.show();
                //(this.sta10Details.id.toString());
                this.qaService.saveMachineryPlantDetailsSta10(this.sta10Details.id.toString(), this.sta10MachineryAndPlantDetails).subscribe(
                    (data) => {
                        this.sta10MachineryAndPlantDetails = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        //(data);
                        this.step += 1;
                        this.loading = false

                        swal.fire({
                            title: 'Machinery And Plant Details saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = false

                swal.fire({
                    title: 'Machinery And Plant Details missing!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'error'
                });
            }
        }
    }

    onClickSaveSTAManufacturingProcess(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.sta10ManufacturingProcessDetails.length > 0) {
                this.SpinnerService.show();
                //(this.sta10Details.id.toString());
                // tslint:disable-next-line:max-line-length
                this.qaService.saveManufacturingProcessDetailsSta10(this.sta10Details.id.toString(), this.sta10ManufacturingProcessDetails).subscribe(
                    (data) => {
                        this.sta10ManufacturingProcessDetails = data;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false

                        //(data);
                        this.step += 1;
                        swal.fire({
                            title: 'Manufacturing Process Details saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = true

                swal.fire({
                    title: 'Manufacturing Process Details missing!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'error'
                });
            }
        }
    }


    onClickAddPersonnel() {
        if (this.sta10FormA?.get('personnelName')?.value == null) {
            this.showToasterError("Error", "Please Enter Personnel Name")
        } else if (this.sta10FormA?.get('qualificationInstitution')?.value == null) {
            this.showToasterError("Error", "Please Enter Qualification Institution Name")
        } else if (this.sta10FormA?.get('dateOfEmployment')?.value == null) {
            this.showToasterError("Error", "Please Enter Date Of Employment")
        } else {


            this.sta10PersonnelDetail = this.sta10FormA.value;
            this.sta10PersonnelDetails.push(this.sta10PersonnelDetail);
            this.sta10FormA?.get('personnelName')?.reset();
            this.sta10FormA?.get('qualificationInstitution')?.reset();
            this.sta10FormA?.get('dateOfEmployment')?.reset();
        }
        // this.sta10FormA.reset();
    }

    onClickAddProductsManufacture() {
        if (this.sta10FormB?.get('productName')?.value == null) {
            this.showToasterError("Error", "Please Enter Product Name")
        } else if (this.sta10FormB?.get('productBrand')?.value == null) {
            this.showToasterError("Error", "Please Enter Product Brand")
        } else if (this.sta10FormB?.get('productStandardNumber')?.value == null) {
            this.showToasterError("Error", "Please Enter Product Standard Number")
        } else if (this.sta10FormB?.get('available')?.value == null) {
            this.showToasterError("Error", "Please Enter Availability")
        } else if (this.sta10FormB?.get('permitNo')?.value == null) {
            this.showToasterError("Error", "Please Enter Permit Number")
        } else {


            this.sta10ProductsManufactureDetail = this.sta10FormB.value;
            this.sta10ProductsManufactureDetails.push(this.sta10ProductsManufactureDetail);
            this.sta10FormB?.get('productName')?.reset();
            this.sta10FormB?.get('productBrand')?.reset();
            this.sta10FormB?.get('productStandardNumber')?.reset();
            this.sta10FormB?.get('available')?.reset();
            this.sta10FormB?.get('permitNo')?.reset();
        }
    }


    onClickAddRawMaterials() {
        if (this.sta10FormC?.get('name')?.value == null) {
            this.showToasterError("Error", "Please Enter Raw Material Name")
        } else if (this.sta10FormC?.get('origin')?.value == null) {
            this.showToasterError("Error", "Please Enter Origin")
        } else if (this.sta10FormC?.get('specifications')?.value == null) {
            this.showToasterError("Error", "Please Enter Specification")
        } else if (this.sta10FormC?.get('qualityChecksTestingRecords')?.value == null) {
            this.showToasterError("Error", "Please Enter Quality Checks Testing Records")
        } else {
            this.sta10RawMaterialsDetail = this.sta10FormC.value;
            this.sta10RawMaterialsDetails.push(this.sta10RawMaterialsDetail);
            this.sta10FormC?.get('name')?.reset();
            this.sta10FormC?.get('origin')?.reset();
            this.sta10FormC?.get('specifications')?.reset();
            this.sta10FormC?.get('qualityChecksTestingRecords')?.reset();
        }
    }

    onClickAddMachineryAndPlant() {
        if (this.sta10FormD?.get('typeModel')?.value == null) {
            this.showToasterError("Error", "Please Enter Type Model")
        } else if (this.sta10FormD?.get('machineName')?.value == null) {
            this.showToasterError("Error", "Please Enter Machine Name")
        } else if (this.sta10FormD?.get('countryOfOrigin')?.value == null) {
            this.showToasterError("Error", "Please Enter Country Of Origin")
        } else {
            this.sta10MachineryAndPlantDetail = this.sta10FormD.value;
            this.sta10MachineryAndPlantDetails.push(this.sta10MachineryAndPlantDetail);
            this.sta10FormD?.get('typeModel')?.reset();
            this.sta10FormD?.get('machineName')?.reset();
            this.sta10FormD?.get('countryOfOrigin')?.reset();
        }
    }


    onClickAddManufacturingProcess() {
        if (this.sta10FormE?.get('processFlowOfProduction')?.value == null) {
            this.showToasterError("Error", "Please Enter Process Flow Of Production")
        } else if (this.sta10FormE?.get('operations')?.value == null) {
            this.showToasterError("Error", "Please Enter Operations")
        } else if (this.sta10FormE?.get('criticalProcessParametersMonitored')?.value == null) {
            this.showToasterError("Error", "Please Enter Critical Processes")
        } else if (this.sta10FormE?.get('frequency')?.value == null) {
            this.showToasterError("Error", "Please Enter Frequency")
        } else if (this.sta10FormE?.get('processMonitoringRecords')?.value == null) {
            this.showToasterError("Error", "Please Enter Process Monitoring Records")
        } else {
            this.sta10ManufacturingProcessDetail = this.sta10FormE.value;
            this.sta10ManufacturingProcessDetails.push(this.sta10ManufacturingProcessDetail);
            this.sta10FormE?.get('processFlowOfProduction')?.reset();
            this.sta10FormE?.get('operations')?.reset();
            this.sta10FormE?.get('criticalProcessParametersMonitored')?.reset();
            this.sta10FormE?.get('frequency')?.reset();
            this.sta10FormE?.get('processMonitoringRecords')?.reset();
        }
    }

    onClickSaveSTA10G() {
        if (this.sta10FilesList.length > 0) {
            if (this?.uploadedFiles) {
                this.fileListSaveDetails();
            } else {
                this.step += 1;
                swal.fire({
                    title: 'STA10 Form Completed! Proceed to submit application.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                this.router.navigate(['/smarkpermitdetails'], {fragment: String(this.sta1.id)});
            }
        } else if (this.uploadedFiles?.length > 0) {
            this.fileListSaveDetails();
        }

    }

    fileListSaveDetails() {
        const file = this.uploadedFiles;
        const formData = new FormData();
        for (let i = 0; i < file.length; i++) {
            //(file[i]);
            formData.append('docFile', file[i], file[i].name);
        }
        this.loading = true


        this.SpinnerService.show();
        this.qaService.uploadSTA10File(this.sta1.id.toString(), formData).subscribe(
            (data: any) => {
                this.SpinnerService.hide();
                this.loading = false

                //(data);
                this.onClickUpdateStep(this.step);
                this.step += 1;
                swal.fire({
                    title: 'STA10 Form Completed! Proceed to submit application.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                // this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});
            },
        );
        this.router.navigate(['/smarkpermitdetails'], {fragment: String(this.sta1.id)});
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

    showErrorMessage() {
        swal.fire({
            title: 'MISSING UPLOAD\'S DOCUMENTS.',
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'error'
        });
    }


// Remove Form repeater values
    removePersonnelDetails(index) {
        //(index);
        if (index === 0) {
            this.sta10PersonnelDetails.splice(index, 1);
        } else {
            this.sta10PersonnelDetails.splice(index, index);
        }
    }

    removeProductsManufacture(index) {
        if (index === 0) {
            this.sta10ProductsManufactureDetails.splice(index, 1);
        } else {
            this.sta10ProductsManufactureDetails.splice(index, index);
        }
    }

    removeRawMaterials(index) {
        if (index === 0) {
            this.sta10RawMaterialsDetails.splice(index, 1);
        } else {
            this.sta10RawMaterialsDetails.splice(index, index);
        }
    }

    removeMachineryAndPlantDetails(index) {
        if (index === 0) {
            this.sta10MachineryAndPlantDetails.splice(index, 1);
        } else {
            this.sta10MachineryAndPlantDetails.splice(index, index);
        }
    }

    removeManufacturingProcessDetails(index) {
        if (index === 0) {
            this.sta10ManufacturingProcessDetails.splice(index, 1);
        } else {
            this.sta10ManufacturingProcessDetails.splice(index, index);
        }
    }


    onselectSection() {
        //(this.SelectedSectionId);
        // this.SelectedSectionId=sselect;
        // this.SelectedSectionId = Selectedfood;
    }

    setClonedMethod() {
        this.hideCloneButton = true;
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

}
