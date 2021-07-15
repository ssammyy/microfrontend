import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Company, loadAuths, LoginCredentials} from '../../core/store';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {
    PermitEntityDetails,
    PlantDetailsDto,
    SectionDto, Sta10Dto, STA10MachineryAndPlantDto, STA10ManufacturingProcessDto, STA10PersonnelDto,
    STA10ProductsManufactureDto, STA10RawMaterialsDto
} from '../../core/store/data/qa/qa.model';
import swal from 'sweetalert2';
import {FileUploadValidators} from "@iplab/ngx-file-upload";

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
    permitEntityDetails: PermitEntityDetails;
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

    public uploadedFiles: Array<File> = [];
    public animation: boolean = false;
    public multiple: boolean = false;

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
          personnelName: ['', Validators.required],
          qualificationInstitution: ['', Validators.required],
          dateOfEmployment: ['', Validators.required]

      });

        this.sta10FormB = this.formBuilder.group({
            productName: ['', Validators.required],
            productBrand: ['', Validators.required],
            productStandardNumber: ['', Validators.required],
            available: ['', Validators.required],
            permitNo: []
        });

        this.sta10FormC = this.formBuilder.group({
            name: ['', Validators.required],
            origin: ['', Validators.required],
            specifications: ['', Validators.required],
            qualityChecksTestingRecords: ['', Validators.required]
        });

        this.sta10FormD = this.formBuilder.group({
            machineName: ['', Validators.required],
            typeModel: ['', Validators.required],
            countryOfOrigin: ['', Validators.required],
        });

        this.sta10FormE = this.formBuilder.group({
            processFlowOfProduction: ['', Validators.required],
            operations: ['', Validators.required],
            criticalProcessParametersMonitored: ['', Validators.required],
            frequency: ['', Validators.required],
            processMonitoringRecords: ['', Validators.required]
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
            if (this.permitEntityDetails == null) {
                this.qaService.savePermitSTA1('2', this.sta1Form.value).subscribe(
                    (data: PermitEntityDetails) => {
                        this.permitEntityDetails = data;
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
        this.qaService.updatePermitSTA1(String(this.permitEntityDetails.id), this.sta1Form.value).subscribe(
            (data: PermitEntityDetails) => {
              this.permitEntityDetails = data;
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
            console.log(this.permitEntityDetails.id.toString());
            if (this.Sta10Details == null) {
                this.qaService.saveFirmDetailsSta10(this.permitEntityDetails.id.toString(), this.sta10Form.value).subscribe(
                    (data) => {
                        this.Sta10Details = data;
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
                this.qaService.updateFirmDetailsSta10(`${this.permitEntityDetails.id}`, this.sta10FormF.value).subscribe(
                    (data) => {
                        this.Sta10Details = data;
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
            console.log(this.permitEntityDetails.id.toString());

                this.qaService.updateFirmDetailsSta10(this.permitEntityDetails.id.toString(), this.sta10FormF.value).subscribe(
                    (data) => {
                        this.Sta10Details = data;
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
                        //this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});
                    },
                );
        }
    }
    onClickSaveSTA10G(valid: boolean) {
        if (valid) {
            console.log(this.permitEntityDetails.id.toString());

            this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});


        }
    }

    onClickSaveSTAPersonnel(valid: boolean) {
        if (valid) {
            console.log(this.Sta10Details.id.toString());
            // if (this.sta10PersonnelDetails == null) {
            this.qaService.savePersonnelDetailsSta10(this.Sta10Details.id.toString(), this.sta10PersonnelDetails).subscribe(
                (data) => {
                    this.sta10PersonnelDetails = data;
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
            this.qaService.saveProductsManufacturedDetailsSta10(this.Sta10Details.id.toString(), this.sta10ProductsManufactureDetails).subscribe(
                (data) => {
                    this.sta10ProductsManufactureDetails = data;
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
            this.qaService.saveManufacturingProcessDetailsSta10(this.Sta10Details.id.toString(), this.sta10ManufacturingProcessDetails).subscribe(
                (data) => {
                    this.sta10ManufacturingProcessDetails = data;
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
    }

    onClickAddProductsManufacture() {
        this.sta10ProductsManufactureDetail = this.sta10FormB.value;
        this.sta10ProductsManufactureDetails.push(this.sta10ProductsManufactureDetail);
    }

    onClickAddRawMaterials() {
        this.sta10RawMaterialsDetail = this.sta10FormC.value;
        this.sta10RawMaterialsDetails.push(this.sta10RawMaterialsDetail);
    }

    onClickAddMachineryAndPlant() {
        this.sta10MachineryAndPlantDetail = this.sta10FormD.value;
        this.sta10MachineryAndPlantDetails.push(this.sta10MachineryAndPlantDetail);
    }

    onClickAddManufacturingProcess() {
        this.sta10ManufacturingProcessDetail = this.sta10FormE.value;
        this.sta10ManufacturingProcessDetails.push(this.sta10ManufacturingProcessDetail);
    }
}
