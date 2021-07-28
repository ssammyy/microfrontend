import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {
    AllPermitDetailsDto,
    AllSTA10DetailsDto,
    PermitEntityDetails,
    PlantDetailsDto,
    SectionDto,
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto
} from "../../core/store/data/qa/qa.model";
import {QaService} from "../../core/store/data/qa/qa.service";
import {ActivatedRoute, Router} from "@angular/router";
import swal from "sweetalert2";
import {ApiEndpointService} from '../../core/services/endpoints/api-endpoint.service';

@Component({
    selector: 'app-smark',
    templateUrl: './smark.component.html',
    styleUrls: ['./smark.component.css']
})
export class SmarkComponent implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    currDiv!: string;
    currDivLabel!: string;
    approveRejectSSCForm!: FormGroup;

    pdfSources: any;

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
    allPermitDetails: AllPermitDetailsDto;
    allSTA10Details: AllSTA10DetailsDto;
    permitEntityDetails: PermitEntityDetails;
    sta1: STA1;
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
    stepSoFar: | undefined;
    step = 1;
    public permitID!: string;
    batchID!: bigint;


    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    FMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private qaService: QaService,
        private formBuilder: FormBuilder
    ) {
    }

    ngOnInit(): void {

        this.getSelectedPermit();

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
        this.sta10FormG = this.formBuilder.group({});

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

        this.approveRejectSSCForm = this.formBuilder.group({
            approvedRejectedScheme: ['', Validators.required],
            approvedRejectedSchemeRemarks: [''],
            // approvedRemarks: ['', Validators.required],
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


        // if (this.allPermitDetails.permitDetails.permitAwardStatus === true) {
        this.qaService.loadCertificateDetailsPDF(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: any) => {
                this.pdfSources = data;
            },
        );
        // }

    }

    get formApproveRejectSSC(): any {
        return this.approveRejectSSCForm.controls;
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

    public getSelectedPermit(): void {
        this.route.fragment.subscribe(params => {
            // this.permitID = params;
            // console.log(this.permitID);
            this.qaService.loadPermitDetails(params).subscribe(
                (data: AllPermitDetailsDto) => {
                    this.allPermitDetails = new AllPermitDetailsDto();
                    this.allPermitDetails = data;
                    this.batchID = this.allPermitDetails.batchID;
                    // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);
                    this.qaService.viewSTA1Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (data) => {
                            this.sta1 = data;
                            this.sta1Form.patchValue(this.sta1);
                        },
                    );
                    console.log(`${this.sta10PersonnelDetails.length}`);
                    this.qaService.viewSTA10Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (data) => {
                            this.allSTA10Details = data;
                            this.sta10Form.patchValue(this.allSTA10Details.sta10FirmDetails);
                            this.sta10PersonnelDetails = this.allSTA10Details.sta10PersonnelDetails;
                            this.sta10ProductsManufactureDetails = this.allSTA10Details.sta10ProductsManufactureDetails;
                            this.sta10RawMaterialsDetails = this.allSTA10Details.sta10RawMaterialsDetails;
                            this.sta10MachineryAndPlantDetails = this.allSTA10Details.sta10MachineryAndPlantDetails;
                            this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
                            this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
                            this.sta10FormF.patchValue(this.allSTA10Details.sta10FirmDetails);
                        },
                    );
                },
            );

            // this.qaService.viewSTA10FirmDetails(String(this.allPermitDetails.permitDetails.id)).subscribe(
            //     (data) => {
            //       this.Sta10Details = data;
            //       this.sta10Form.patchValue(this.Sta10Details);
            //     },
            // );
            // console.log(`${this.sta10PersonnelDetails.length}`);
            // this.qaService.viewSTA10PersonnelDetails(String(this.Sta10Details.id)).subscribe(
            //     (data) => {
            //       console.log(`${this.sta10PersonnelDetails.length}`);
            //       this.sta10PersonnelDetails = data;
            //       console.log(`${this.sta10PersonnelDetails.length}`);
            //     },
            // );
        });

    }


    onClickSaveSTA1(valid: boolean) {
        this.qaService.updatePermitSTA1(String(String(this.allPermitDetails.permitDetails.id)), this.sta1Form.value).subscribe(
            (data) => {
                this.sta1 = data;
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

    onClickSaveSTA10(valid: boolean) {

        this.qaService.updateFirmDetailsSta10(`${this.permitEntityDetails.id}`, this.sta10FormF.value).subscribe(
            (data) => {
                this.Sta10Details = data;
                console.log(data);
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

    onClickSaveSTA10F(valid: boolean) {
        if (valid) {
            console.log(this.permitEntityDetails.id.toString());

            this.qaService.updateFirmDetailsSta10(String(this.allPermitDetails.permitDetails.id), this.sta10FormF.value).subscribe(
                (data) => {
                    this.Sta10Details = data;
                    console.log(data);
                    swal.fire({
                        title: 'Non-conforming Products Manufacturing Process saved!',
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

    submitApprovalRejectionSSC(): void {
        console.log(this.approveRejectSSCForm.value);
        this.qaService.submitSSCApprovalRejection(String(this.allPermitDetails.permitDetails.id), this.approveRejectSSCForm.value).subscribe(
            (data: PermitEntityDetails) => {
                this.allPermitDetails.permitDetails = data;
                swal.fire({
                    title: 'PERMIT APPLICATION SSC UPDATED SUCCESSFULLY!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                // this.router.navigate(['/invoiceDetails'], {fragment: this.allPermitDetails.batchID.toString()});

                // this.onUpdateReturnToList();
            },
        );
    }

    onClickSaveSTA10G(valid: boolean) {
        if (valid) {
            console.log(this.permitEntityDetails.id.toString());
            swal.fire({
                title: 'STA3 Form Completed! Proceed to submit application.!',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                },
                icon: 'success'
            });
            // this.router.navigate(['/permitdetails'], {fragment: String(this.allPermitDetails.permitDetails.id)});


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
                    console.log(data);
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
                    console.log(data);
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

    openModal(divVal: string): void {
        const arrHead = ['approveSSC'];
        const arrHeadSave = ['AGREE/CONSENT TO SCHEME OF SUPERVISION AND CONTROL'];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }

        this.currDiv = divVal;
        // this.modalService.show(this.editModal);
        // this.modalService.open();


    }

    onClickAddPersonnel() {
        this.sta10PersonnelDetail = this.sta10FormA.value;
        this.sta10PersonnelDetails.push(this.sta10PersonnelDetail);
        this.sta10FormA?.get('personnelName')?.reset();
        this.sta10FormA?.get('qualificationInstitution')?.reset();
        this.sta10FormA?.get('dateOfEmployment')?.reset();
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

    submitApplication(): void {

        this.qaService.submitPermitApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                if (this.allPermitDetails.permitDetails.permitTypeID === this.FMarkTypeID) {
                    swal.fire({
                        title: 'FMARK SUBMITTED SUCCESSFULLY PENDING PAYMENT!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                } else {
                    swal.fire({
                        title: 'SMARK SUBMITTED SUCCESSFULLY PENDING PAYMENT!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                }


                this.router.navigate(['/invoiceDetails'], {fragment: this.allPermitDetails.batchID.toString()});

                // this.onUpdateReturnToList();
            },
        );
    }

    submitRenewalApplication() {
        this.qaService.submitPermitRenewApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                console.log(AllPermitDetailsDto);
                if (this.allPermitDetails.permitDetails.permitTypeID === this.FMarkTypeID) {
                    swal.fire({
                        title: 'FMARK Renewed Successfully! Proceed to submit application.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                } else {
                    swal.fire({
                        title: 'SMARK Renewed Successfully! Proceed to submit application.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                }

                // this.router.navigate(['/permitdetails'], {fragment: String(this.AllPermitDetailsDto.permitDetails.id)});
            },
        );
    }

    submitApplicationForReviewHOD(): void {
        // tslint:disable-next-line:max-line-length
        // if (this.allPermitDetails.permitDetails.permitForeignStatus === true && this.allPermitDetails.permitDetails.permitTypeID === ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID) {
        this.qaService.submitPermitForReviewHODQAM(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                if (this.allPermitDetails.permitDetails.permitTypeID === this.FMarkTypeID) {
                    swal.fire({
                        title: 'FMARK SUBMITTED SUCCESSFULLY FOR REVIEW HOD/RM!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });

                } else {
                    swal.fire({
                        title: 'SMARK SUBMITTED SUCCESSFULLY FOR REVIEW HOD/RM!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });

                }

                // this.onUpdateReturnToList();
            },
        );
        // }
    }

    goToInvoiceGenerated() {
        this.router.navigate(['/invoiceDetails'], {fragment: String(this.allPermitDetails.batchID)});
    }
}
