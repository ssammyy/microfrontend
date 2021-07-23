import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {
    PermitEntityDetails, PermitProcessStepDto,
    PlantDetailsDto,
    SectionDto, STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto
} from '../../core/store/data/qa/qa.model';
import swal from 'sweetalert2';
import {FileUploadValidators} from "@iplab/ngx-file-upload";

declare const $: any;

@Component({
    selector: 'app-new-smark-permit',
    templateUrl: './new-smark-permit.component.html',
    styleUrls: ['./new-smark-permit.component.css']
})
export class NewSmarkPermitComponent implements OnInit {
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
    permitProcessStep: PermitProcessStepDto | undefined;
    Sta10Details: Sta10Dto;
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

    constructor(private store$: Store<any>,
                private router: Router,
                private qaService: QaService,
                private formBuilder: FormBuilder,
                private route: ActivatedRoute) {
    }

    ngOnInit(): void {


        this.sta1Form = this.formBuilder.group({
          commodityDescription: ['', Validators.required],
          applicantName: ['', Validators.required],
          sectionId: ['', Validators.required],
          permitForeignStatus: [],
          attachedPlant: ['', Validators.required],
          tradeMark: ['', Validators.required],
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
            productStandardNumber: [],
            available: [],
            permitNo: []
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
        this.sta10FormG = this.formBuilder.group({

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
                    this.stepSoFar = {...this.sta1Form?.value};
                    break;
            }
            this.step += 1;
            console.log(`Clicked and step = ${this.step}`);
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
    selectStepFourClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }
        if (step === 3) {
            return 'activated';
        }
        if (step === 4) {
            return 'activated';
        } else {
            return '';
        }
    }

    selectStepFiveClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        }if (step === 4) {
            return 'activated';
        }if (step === 5) {
            return 'activated';
        } else {
            return '';
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
            if (this.sta1 == null) {
                this.qaService.savePermitSTA1('2', this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        this.onClickUpdateStep(this.step);
                        console.log(data);
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
                this.qaService.updatePermitSTA1(String(this.sta1.id), this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        this.onClickUpdateStep(this.step);
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

    onClickSaveSTA10(valid: boolean) {
        if (valid) {
            console.log(this.sta1.id.toString());
            if (this.Sta10Details == null) {
                this.qaService.saveFirmDetailsSta10(this.sta1.id.toString(), this.sta10Form.value).subscribe(
                    (data) => {
                        this.Sta10Details = data;
                        this.onClickUpdateStep(this.step);
                        console.log(data);
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
                this.qaService.updateFirmDetailsSta10(`${this.sta1.id}`, this.sta10FormF.value).subscribe(
                    (data) => {
                        this.Sta10Details = data;
                        this.onClickUpdateStep(this.step);
                        console.log(data);
                        this.step += 1;
                        swal.fire({
                            title: 'STA10 Form Updated!',
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
            console.log(this.sta1.id.toString());

            this.qaService.updateFirmDetailsSta10(this.sta1.id.toString(), this.sta10FormF.value).subscribe(
                (data) => {
                    this.Sta10Details = data;
                    this.onClickUpdateStep(this.step);
                    console.log(data);
                    this.step += 1;
                    swal.fire({
                        title: 'Nonconforming Products Manufacturing Process saved!',
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
    //         console.log(this.sta1.id.toString());
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
            console.log(this.Sta10Details.id.toString());
            // if (this.sta10PersonnelDetails == null) {
            this.qaService.savePersonnelDetailsSta10(this.Sta10Details.id.toString(), this.sta10PersonnelDetails).subscribe(
                (data) => {
                    this.sta10PersonnelDetails = data;
                    this.onClickUpdateStep(this.step);
                    console.log(data);
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
        }
    }

    onClickSaveSTAProductsManufactured(valid: boolean) {
        if (valid) {
            console.log(this.Sta10Details.id.toString());
            // tslint:disable-next-line:max-line-length
            this.qaService.saveProductsManufacturedDetailsSta10(this.Sta10Details.id.toString(), this.sta10ProductsManufactureDetails).subscribe(
                (data) => {
                    this.sta10ProductsManufactureDetails = data;
                    this.onClickUpdateStep(this.step);
                    console.log(data);
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
        }
    }

    onClickSaveSTARawMaterials(valid: boolean) {
        if (valid) {
            console.log(this.Sta10Details.id.toString());
            this.qaService.saveRawMaterialsDetailsSta10(this.Sta10Details.id.toString(), this.sta10RawMaterialsDetails).subscribe(
                (data) => {
                    this.sta10RawMaterialsDetails = data;
                    this.onClickUpdateStep(this.step);
                    console.log(data);
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
        }
    }

    onClickSaveSTAMachineryPlant(valid: boolean) {
        if (valid) {
            console.log(this.Sta10Details.id.toString());
            this.qaService.saveMachineryPlantDetailsSta10(this.Sta10Details.id.toString(), this.sta10MachineryAndPlantDetails).subscribe(
                (data) => {
                    this.sta10MachineryAndPlantDetails = data;
                    this.onClickUpdateStep(this.step);
                    console.log(data);
                    this.step += 1;
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
        }
    }

    onClickSaveSTAManufacturingProcess(valid: boolean) {
        if (valid) {
            console.log(this.Sta10Details.id.toString());
            // tslint:disable-next-line:max-line-length
            this.qaService.saveManufacturingProcessDetailsSta10(this.Sta10Details.id.toString(), this.sta10ManufacturingProcessDetails).subscribe(
                (data) => {
                    this.sta10ManufacturingProcessDetails = data;
                    this.onClickUpdateStep(this.step);
                    console.log(data);
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
        }
    }


    onClickAddPersonnel() {
        this.sta10PersonnelDetail = this.sta10FormA.value;
        this.sta10PersonnelDetails.push(this.sta10PersonnelDetail);
        this.sta10FormA?.get('personnelName')?.reset();
        this.sta10FormA?.get('qualificationInstitution')?.reset();
        this.sta10FormA?.get('dateOfEmployment')?.reset();
        // this.sta10FormAA.reset();
    }

    onClickAddProductsManufacture() {
        this.sta10ProductsManufactureDetail = this.sta10FormB.value;
        this.sta10ProductsManufactureDetails.push(this.sta10ProductsManufactureDetail);
        this.sta10FormB?.get('productName')?.reset();
        this.sta10FormB?.get('productBrand')?.reset();
        this.sta10FormB?.get('productStandardNumber')?.reset();
        this.sta10FormB?.get('available')?.reset();
        this.sta10FormB?.get('permitNo')?.reset();
    }


    onClickAddRawMaterials() {
        this.sta10RawMaterialsDetail = this.sta10FormC.value;
        this.sta10RawMaterialsDetails.push(this.sta10RawMaterialsDetail);
        this.sta10FormC?.get('name')?.reset();
        this.sta10FormC?.get('origin')?.reset();
        this.sta10FormC?.get('specifications')?.reset();
        this.sta10FormC?.get('qualityChecksTestingRecords')?.reset();
    }

    onClickAddMachineryAndPlant() {
        this.sta10MachineryAndPlantDetail = this.sta10FormD.value;
        this.sta10MachineryAndPlantDetails.push(this.sta10MachineryAndPlantDetail);
        this.sta10FormD?.get('typeModel')?.reset();
        this.sta10FormD?.get('machineName')?.reset();
        this.sta10FormD?.get('countryOfOrigin')?.reset();
    }


    onClickAddManufacturingProcess() {
        this.sta10ManufacturingProcessDetail = this.sta10FormE.value;
        this.sta10ManufacturingProcessDetails.push(this.sta10ManufacturingProcessDetail);
        this.sta10FormE?.get('processFlowOfProduction')?.reset();
        this.sta10FormE?.get('operations')?.reset();
        this.sta10FormE?.get('criticalProcessParametersMonitored')?.reset();
        this.sta10FormE?.get('frequency')?.reset();
        this.sta10FormE?.get('processMonitoringRecords')?.reset();
    }

    onClickSaveSTA10G() {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.qaService.uploadSTA10File(this.sta1.id.toString(), formData).subscribe(
                (data: any) => {
                    console.log(data);
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
            this.router.navigate(['/smarkpermitdetails'], {fragment: this.sta1.id.toString()});
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
}
