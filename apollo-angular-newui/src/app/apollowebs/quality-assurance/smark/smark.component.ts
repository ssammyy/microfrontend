import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {
    AllPermitDetailsDto,
    AllSTA10DetailsDto, FilesListDto,
    PermitEntityDetails, PermitEntityDto,
    PlantDetailsDto,
    SectionDto, SSFPDFListDetailsDto,
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto
} from '../../../core/store/data/qa/qa.model';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {ActivatedRoute, Router} from '@angular/router';
import swal from 'sweetalert2';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {FileUploadValidators} from '@iplab/ngx-file-upload';
import {TableData} from '../../../md/md-table/md-table.component';

@Component({
    selector: 'app-smark',
    templateUrl: './smark.component.html',
    styleUrls: ['./smark.component.css']
})
export class SmarkComponent implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    currDiv!: string;
    currDivLabel!: string;
    labResultsStatus!: string;
    labResultsRemarks!: string;
    approveRejectSSCForm!: FormGroup;
    uploadForm!: FormGroup;
    uploadedFile: File;
    uploadedFiles: FileList;
    upLoadDescription: string;

    pdfSources: any;
    pdfInvoiceBreakDownSources: any;
    pdfSourcesScheme: any;
    pdfUploadsView: any;

    loading = false;
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

    sta10FileList: FilesListDto[];
    ordinaryFilesList: FilesListDto[];
    labResultsDetailsList: SSFPDFListDetailsDto[];
    olderVersionDetailsList: PermitEntityDto[];
    sta10ManufacturingProcessDetail: STA10ManufacturingProcessDto;
    stepSoFar: | undefined;
    step = 1;
    public permitID!: string;
    batchID!: bigint;

    private filesControl = new FormControl(null, FileUploadValidators.filesLimit(2));

    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    FMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;
    draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;

    public tableData1: TableData;
    public tableData2: TableData;
    public tableData3: TableData;
    public tableData4: TableData;
    public tableData5: TableData;
    public tableData12: TableData;
    blob: Blob;


    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private qaService: QaService,
        private formBuilder: FormBuilder,
        private _loading: LoadingService,
        private SpinnerService: NgxSpinnerService
    ) {
    }

    ngOnInit(): void {

        this.getSelectedPermit();
        this.remarksDetails();

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

        this.uploadForm = this.formBuilder.group({
            upLoadDescription: ['', Validators.required],
            uploadedFile: this.filesControl
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


        // // if (this.allPermitDetails.permitDetails.permitAwardStatus === true) {
        // this.qaService.loadCertificateDetailsPDF(String(this.allPermitDetails.permitDetails.id)).subscribe(
        //     (data: any) => {
        //         this.pdfSources = data;
        //     },
        // );
        // }

    }

    remarksDetails() {
        const formattedArrayRemarks = [];

        if (this.allPermitDetails?.remarksDetails?.hofQamCompleteness !== null) {
            formattedArrayRemarks.push(['Completeness Remarks By HOD', '', 'hofQamCompletenessRemarks']);
        }
        // if (this.allPermitDetails.remarksDetails.pcmReviewApprovalRemarks !== null) {
        formattedArrayRemarks.push(['Review Remarks By PCM', '', 'reviewRemarksPCMRemarks']);
        // }
        // if (this.allPermitDetails.remarksDetails.pscMemberApprovalRemarks !== null) {
        formattedArrayRemarks.push(['PSC Members Approval/Rejection Remarks', '', 'pscApprovalRejectionRemarks']);
        // }
        // if (this.allPermitDetails.remarksDetails.pcmApprovalRemarks !== null) {
        formattedArrayRemarks.push(['PCM Approval/Rejection Remarks', '', 'pcmApprovalRejectionRemarks']);
        // }

        this.tableData1 = {
            headerRow: ['Remark Details', 'Action'],
            dataRows: formattedArrayRemarks
        };
    }

    get formUploadForm(): any {
        return this.uploadForm.controls;
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
            const permitID = params;
            // localStorage.setItem('permitID')
            // console.log(this.permitID);
            //  this.SpinnerService.show();
            let formattedArraySta10 = [];
            let formattedArrayOrdinaryFiles = [];
            let formattedArrayLabResultsList = [];
            let formattedArrayOlderVersionList = [];
            const formattedArrayInvoiceDetailsList = [];
            this.qaService.loadPermitDetails(params).subscribe(
                (data: AllPermitDetailsDto) => {
                    // this.SpinnerService.hide();
                    this.allPermitDetails = new AllPermitDetailsDto();
                    this.allPermitDetails = data;
                    this.batchID = this.allPermitDetails.batchID;
                    // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);
                    this.qaService.viewSTA1Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (dataSta1) => {
                            this.sta1 = dataSta1;
                            this.sta1Form.patchValue(this.sta1);
                        },
                    );
                    console.log(`${this.sta10PersonnelDetails.length}`);
                    this.qaService.viewSTA10Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (dataSta10) => {
                            this.allSTA10Details = dataSta10;
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

                    if (this.allPermitDetails.sta10FilesList !== []) {
                        this.sta10FileList = this.allPermitDetails.sta10FilesList;
                        // tslint:disable-next-line:max-line-length
                        formattedArraySta10 = this.sta10FileList.map(i => [i.name, i.fileType, i.documentType, i.versionNumber, i.id, i.document]);
                        this.tableData2 = {
                            headerRow: ['File Name', 'File Type', 'Document Description', 'Version Number', 'Action'],
                            dataRows: formattedArraySta10
                        };
                    }
                    if (this.allPermitDetails.ordinaryFilesList !== []) {
                        this.ordinaryFilesList = this.allPermitDetails.ordinaryFilesList;
                        // tslint:disable-next-line:max-line-length
                        formattedArrayOrdinaryFiles = this.ordinaryFilesList.map(i => [i.name, i.fileType, i.documentType, i.versionNumber, i.id]);
                        this.tableData3 = {
                            headerRow: ['File Name', 'File Type', 'Document Description', 'Version Number', 'Action'],
                            dataRows: formattedArrayOrdinaryFiles
                        };
                    }
                    if (this.allPermitDetails.labResultsList !== []) {
                        this.labResultsDetailsList = this.allPermitDetails.labResultsList;
                        // tslint:disable-next-line:max-line-length
                        formattedArrayLabResultsList = this.labResultsDetailsList.map(i => [i.pdfName, i.complianceStatus, i.sffId, i.complianceRemarks, i.pdfSavedId]);
                        this.tableData4 = {
                            headerRow: ['File Name', 'Compliant Status', 'View Remarks', 'View PDF'],
                            dataRows: formattedArrayLabResultsList
                        };
                    }
                    if (this.allPermitDetails.oldVersionList !== []) {
                        this.olderVersionDetailsList = this.allPermitDetails.oldVersionList;
                        // tslint:disable-next-line:max-line-length
                        formattedArrayOlderVersionList = this.olderVersionDetailsList.map(i => [i.permitRefNumber, i.createdOn, i.permitStatus, i.versionNumber, i.id]);
                        this.tableData5 = {
                            headerRow: ['Permit Ref Number', 'Created On', 'Status', 'Version Number', 'Action'],
                            dataRows: formattedArrayOlderVersionList
                        };
                    }
                    // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);
                    if (this.allPermitDetails.permitDetails.permitAwardStatus === true) {
                        this.qaService.loadCertificateDetailsPDF(String(this.allPermitDetails.permitDetails.id)).subscribe(
                            (dataCertificatePdf: any) => {
                                this.pdfSources = dataCertificatePdf;
                            },
                        );
                    }
                    if (this.allPermitDetails.permitDetails.invoiceGenerated === true) {
                        this.tableData12 = {
                            headerRow: ['Item', 'Details/Fee'],
                            dataRows: [
                                ['Invoice Ref No', this.allPermitDetails.invoiceDetails.invoiceRef],
                                ['Description', this.allPermitDetails.invoiceDetails.description],
                                ['Sub Total Before Tax', `KSH ${this.allPermitDetails.invoiceDetails.subTotalBeforeTax}`],
                                ['Tax Amount', `KSH ${this.allPermitDetails.invoiceDetails.taxAmount}`],
                                ['Total Amount', `KSH ${this.allPermitDetails.invoiceDetails.totalAmount}`]

                            ]
                        };
                    }
                },
            );
        });

    }

    openModalRemarks(divVal: string): void {
        const arrHead = ['hofQamCompletenessRemarks', 'reviewRemarksPCMRemarks', 'recommendationRemarks', 'pscApprovalRejectionRemarks', 'pcmApprovalRejectionRemarks'];
        // tslint:disable-next-line:max-line-length
        const arrHeadSave = ['Completeness Remarks', 'PCM Review Remarks', 'Recommendation', 'PSC Remarks', 'PCM Approval/Rejection Remarks'];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }
        this.currDiv = divVal;
    }

    reloadComponent() {
        const currentUrl = this.router.url;
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([currentUrl]);
    }

    reloadCurrentRoute() {
        location.reload();
    }

    viewPdfFile(pdfId: string, fileName: string, applicationType: string): void {
        this.qaService.loadFileDetailsPDF(pdfId).subscribe(
            (dataPdf: any) => {
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    downloadPdfFile(data: string, fileName: string): void {
        this.blob = new Blob([data], {type: 'application/pdf'});

        // tslint:disable-next-line:prefer-const
        let downloadURL = window.URL.createObjectURL(this.blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = fileName;
        link.click();
    }

    getPdfFile(pdfId: string): any {
        this.qaService.loadFileDetailsPDF(pdfId).subscribe(
            (dataPdf: any) => {
                return dataPdf;
            },
        );
    }


    onClickSaveSTA1(valid: boolean) {
        this.SpinnerService.show();
        this.qaService.updatePermitSTA1(String(String(this.allPermitDetails.permitDetails.id)), this.sta1Form.value).subscribe(
            (data) => {
                this.sta1 = data;
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

    onClickSaveSTA10(valid: boolean) {
        this.SpinnerService.show();
        this.qaService.updateFirmDetailsSta10(`${this.permitEntityDetails.id}`, this.sta10FormF.value).subscribe(
            (data) => {
                this.Sta10Details = data;
                console.log(data);
                this.SpinnerService.hide();
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
            this.SpinnerService.show();
            console.log(this.permitEntityDetails.id.toString());

            this.qaService.updateFirmDetailsSta10(String(this.allPermitDetails.permitDetails.id), this.sta10FormF.value).subscribe(
                (data) => {
                    this.Sta10Details = data;
                    this.SpinnerService.hide();
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
        this.SpinnerService.show();
        // tslint:disable-next-line:max-line-length
        this.qaService.submitSSCApprovalRejection(String(this.allPermitDetails.permitDetails.id), this.approveRejectSSCForm.value).subscribe(
            (data: PermitEntityDetails) => {
                this.allPermitDetails.permitDetails = data;
                this.SpinnerService.hide();
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
            this.SpinnerService.show();
            console.log(this.permitEntityDetails.id.toString());
            this.SpinnerService.hide();
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
            this.SpinnerService.show();
            console.log(this.Sta10Details.id.toString());
            // if (this.sta10PersonnelDetails == null) {
            this.qaService.savePersonnelDetailsSta10(this.Sta10Details.id.toString(), this.sta10PersonnelDetails).subscribe(
                (data) => {
                    this.sta10PersonnelDetails = data;
                    console.log(data);
                    this.SpinnerService.hide();
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
            this.SpinnerService.show();
            console.log(this.Sta10Details.id.toString());
            // tslint:disable-next-line:max-line-length
            this.qaService.saveProductsManufacturedDetailsSta10(this.Sta10Details.id.toString(), this.sta10ProductsManufactureDetails).subscribe(
                (data) => {
                    this.sta10ProductsManufactureDetails = data;
                    console.log(data);
                    this.SpinnerService.hide();
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
            this.SpinnerService.show();
            console.log(this.Sta10Details.id.toString());
            this.qaService.saveRawMaterialsDetailsSta10(this.Sta10Details.id.toString(), this.sta10RawMaterialsDetails).subscribe(
                (data) => {
                    this.sta10RawMaterialsDetails = data;
                    console.log(data);
                    this.SpinnerService.hide();
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
            this.SpinnerService.show();
            this.qaService.saveMachineryPlantDetailsSta10(this.Sta10Details.id.toString(), this.sta10MachineryAndPlantDetails).subscribe(
                (data) => {
                    this.sta10MachineryAndPlantDetails = data;
                    console.log(data);
                    this.SpinnerService.hide();
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
            this.SpinnerService.show();
            // tslint:disable-next-line:max-line-length
            this.qaService.saveManufacturingProcessDetailsSta10(this.Sta10Details.id.toString(), this.sta10ManufacturingProcessDetails).subscribe(
                (data) => {
                    this.sta10ManufacturingProcessDetails = data;
                    console.log(data);
                    this.SpinnerService.hide();
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

        this.SpinnerService.show();
        this.qaService.submitPermitApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                this.SpinnerService.hide();
                this.reloadCurrentRoute();
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


                // this.router.navigate(['/invoiceDetails'], {fragment: this.allPermitDetails.batchID.toString()});

                // this.onUpdateReturnToList();
            },
        );
    }

    submitRenewalApplication() {
        this.SpinnerService.show();
        this.qaService.submitPermitRenewApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                this.SpinnerService.hide();
                this.reloadCurrentRoute();
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
        this.SpinnerService.show();
        this.qaService.submitPermitForReviewHODQAM(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                this.SpinnerService.hide();
                this.reloadCurrentRoute();
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


    goToPermitOlderVersion(permitID: string) {
        swal.fire({
            title: 'YOU ARE VIEWING THE OLDER VERSION.',
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
        });
        this.router.navigate(['/smarkpermitdetails'], {fragment: permitID});

    }

    viewLabRemarks(status: string, remarksValue: string) {
        this.currDiv = 'viewLabResultsRemarks';
        this.currDivLabel = 'LAB RESULTS DETAILS';
        switch (status) {
            case 'true':
                this.labResultsStatus = 'COMPLIANT';
                break;
            case 'false':
                this.labResultsStatus = 'NOT-COMPLIANT';
                break;
        }

        this.labResultsRemarks = remarksValue;
    }

    openModalUpload(viewDiv: string) {
        this.currDiv = viewDiv;
        this.currDivLabel = 'Upload PDF Documents Only';
    }

    openModalViewUpload(viewDiv: string) {
        this.currDiv = viewDiv;
        this.currDivLabel = 'VIEWS PDF Documents Only';
    }

    uploadDocument() {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            // tslint:disable-next-line:max-line-length
            this.qaService.uploadFile(String(this.allPermitDetails.permitDetails.id), this.upLoadDescription, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.reloadCurrentRoute();
                    console.log(data);
                    swal.fire({
                        title: 'UPLOADED SUCCESSFULLY',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    // this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});
                },
            );
            // this.router.navigate(['/permitdetails'], {fragment: String(this.sta1.id)});
        }
    }

    goToPayment() {
        this.router.navigate(['/invoice/consolidate_invoice']);
    }

    goToNewApplication() {
        this.router.navigate(['/smark/newSmarkPermit']);
    }

    goToInvoiceGenerated() {
        this.router.navigate(['/invoiceDetails'], {fragment: String(this.allPermitDetails.batchID)});
    }
}
