import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {
    AllPermitDetailsDto,
    AllSTA10DetailsDto,
    FilesListDto,
    PermitDto,
    PermitEntityDetails,
    PermitEntityDto,
    PlantDetailsDto,
    SectionDto,
    SSFComplianceStatusDetailsDto,
    SSFPDFListDetailsDto,
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
import Swal from 'sweetalert2';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {FileUploadValidators} from '@iplab/ngx-file-upload';
import {TableData} from '../../../md/md-table/md-table.component';
import {HttpErrorResponse} from "@angular/common/http";

interface Permit {
    id: string;
    name: string;
}

@Component({
    selector: 'app-smark',
    templateUrl: './smark.component.html',
    styleUrls: ['./smark.component.css']
})
export class SmarkComponent implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    permitTypeName!: string;
    currDiv!: string;
    currDivLabel!: string;
    labResultsStatus!: string;
    labResultsRemarks!: string;
    approveRejectSSCForm!: FormGroup;
    resubmitForm!: FormGroup;
    uploadForm!: FormGroup;
    uploadedFile: File;
    uploadedFiles: FileList;
    upLoadDescription: string;
    public allPermitData: PermitEntityDto[];
    form: FormGroup;
    memberReturnArray: any[] = [];
    public actionRequest: PermitEntityDetails | undefined;


    pdfSources: any;
    SelectedSectionId;
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
    complianceResultsDetailsList: SSFComplianceStatusDetailsDto[];
    olderVersionDetailsList: PermitEntityDto[];
    sta10ManufacturingProcessDetail: STA10ManufacturingProcessDto;
    stepSoFar: | undefined;
    step = 1;
    labResults = false;
    resubmitDetail!: string;
    permitID!: string;
    batchID!: bigint;
    COMPLIANTSTATUS = 'COMPLIANT';
    NONCOMPLIANT = 'NON-COMPLIANT';

    private filesControl = new FormControl(null, FileUploadValidators.filesLimit(2));

    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    FMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;
    draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;

    public tableData1: TableData;
    public tableData2: TableData;
    public tableData3: TableData;
    public tableData4: TableData;
    public tableData6: TableData;
    public tableData5: TableData;
    public tableData12: TableData;
    blob: Blob;


    dropdownList = [];
    selectedItems = [];
    dropdownSettings = {};
    smarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID);
    private permit_id: string;


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
            commodityDescription: [{value: '', disabled: true}, Validators.required],
            applicantName: [{value: '', disabled: true}, Validators.required],
            sectionId: [{value: '', disabled: true}, Validators.required],
            permitForeignStatus: [],
            attachedPlant: [{value: '', disabled: true}, Validators.required],
            tradeMark: [{value: '', disabled: true}, Validators.required],
            createFmark: [{value: '', disabled: true}],
            // inputCountryCode: [{value: '', disabled: true}, Validators.required,Validators.pattern("[0-9 ]{11}")]

        });
        this.sta10Form = this.formBuilder.group({
            // firmName: [{value: '', disabled: true}, Validators.required],
            // statusCompanyBusinessRegistration: [{value: '', disabled: true}, Validators.required],
            // ownerNameProprietorDirector: [{value: '', disabled: true}, Validators.required],
            // postalAddress: [{value: '', disabled: true}, Validators.required],
            // contactPerson: [{value: '', disabled: true}, Validators.required],
            // telephone: [{value: '', disabled: true}, Validators.required],
            // emailAddress: [{value: '', disabled: true}, Validators.required],
            // physicalLocationMap: [{value: '', disabled: true}, Validators.required],
            // county: [{value: '', disabled: true}, Validators.required],
            // town: [{value: '', disabled: true}, Validators.required],
            totalNumberFemale: [{value: '', disabled: true}, Validators.required],
            totalNumberMale: [{value: '', disabled: true}, Validators.required],
            totalNumberPermanentEmployees: [{value: '', disabled: true}, Validators.required],
            totalNumberCasualEmployees: [{value: '', disabled: true}, Validators.required],
            averageVolumeProductionMonth: [{value: '', disabled: true}, Validators.required]

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
            handledManufacturingProcessRawMaterials: [{value: '', disabled: true}, Validators.required],
            handledManufacturingProcessInprocessProducts: [{value: '', disabled: true}, Validators.required],
            handledManufacturingProcessFinalProduct: [{value: '', disabled: true}, Validators.required],
            strategyInplaceRecallingProducts: [{value: '', disabled: true}, Validators.required],
            stateFacilityConditionsRawMaterials: [{value: '', disabled: true}, Validators.required],
            stateFacilityConditionsEndProduct: [{value: '', disabled: true}, Validators.required],
            testingFacilitiesExistSpecifyEquipment: [{value: '', disabled: true}, Validators.required],
            testingFacilitiesExistStateParametersTested: [{value: '', disabled: true}, Validators.required],
            testingFacilitiesSpecifyParametersTested: [{value: '', disabled: true}, Validators.required],
            calibrationEquipmentLastCalibrated: [{value: '', disabled: true}, Validators.required],
            handlingConsumerComplaints: [{value: '', disabled: true}, Validators.required],
            companyRepresentative: [{value: '', disabled: true}, Validators.required],
            applicationDate: [{value: '', disabled: true}, Validators.required]
        });

        this.approveRejectSSCForm = this.formBuilder.group({
            approvedRejectedScheme: ['', Validators.required],
            approvedRejectedSchemeRemarks: [''],
            // approvedRemarks: [{value: '', disabled: true}, Validators.required],
        });
        this.resubmitForm = this.formBuilder.group({
            resubmitRemarks: ['', Validators.required],
            resubmittedDetails: ['', Validators.required],
            // resubmittedDetails: ['']
            // resubmittedDetails: [this.resubmitDetail, Validators.required]
            // approvedRemarks: [{value: '', disabled: true}, Validators.required],
        });

        this.uploadForm = this.formBuilder.group({
            upLoadDescription: ['', Validators.required],
            uploadedFile: this.filesControl
            // approvedRemarks: [{value: '', disabled: true}, Validators.required],
        });


        this.qaService.loadSectionList().subscribe(
            (data: any) => {
                this.sections = data;
                // console.log(data);
            }
        );

        this.qaService.loadPlantList().subscribe(
            (data: any) => {
                this.plants = data;
                // console.log(data);
            }
        );


        // // if (this.allPermitDetails.permitDetails.permitAwardStatus === true) {
        // this.qaService.loadCertificateDetailsPDF(String(this.allPermitDetails.permitDetails.id)).subscribe(
        //     (data: any) => {
        //         this.pdfSources = data;
        //     },
        // );
        // }

        this.qaService.loadCompletelyPermitAwardedList(this.smarkID).subscribe(
            (data: any) => {
                this.allPermitData = data;
                for (let i = 0; i < data.length; i++) {
                    const obj = data[i];
                    let mem_id = obj.id
                    let names = obj.permitRefNumber;

                    let newArray = {'id': mem_id, 'name': names};
                    this.memberReturnArray.push(newArray)
                }
                this.dropdownList = this.memberReturnArray;


            })

        this.initForm();

        this.dropdownSettings = {
            singleSelection: true,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true
        };
    }

    initForm() {
        this.form = this.formBuilder.group({
            permits: ['', [Validators.required]]
        })
    }

    remarksDetails() {
        const formattedArrayRemarks = [];

        // if (this.allPermitDetails?.remarksDetails?.hofQamCompleteness !== null) {
        formattedArrayRemarks.push(['Completeness Remarks By HOD', '', 'hofQamCompletenessRemarks']);
        if (this.allPermitDetails?.remarksDetails?.labResultsCompleteness.remarksStatus === true) {
            this.labResults = true;
            console.log(this.labResults);
        }
        formattedArrayRemarks.push(['Lab Results Remarks By QAO', '', 'labResultsCompletenessRemarks']);
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

    get formResubmitRemarks(): any {
        this.resubmitForm.controls['resubmittedDetails'].setValue(this.resubmitDetail);
        return this.resubmitForm.controls;
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
            let formattedArrayComplianceResultsList = [];
            let formattedArrayOlderVersionList = [];
            const formattedArrayInvoiceDetailsList = [];
            this.SpinnerService.show();
            this.qaService.loadPermitDetails(params).subscribe(
                (data: AllPermitDetailsDto) => {
                    // this.SpinnerService.hide();
                    this.allPermitDetails = new AllPermitDetailsDto();
                    this.allPermitDetails = data;
                    this.batchID = this.allPermitDetails.batchID;
                    if (this.allPermitDetails.permitDetails.permitTypeID === this.SMarkTypeID) {
                        this.permitTypeName = 'Standardization';
                    } else if (this.allPermitDetails.permitDetails.permitTypeID === this.FMarkTypeID) {
                        this.permitTypeName = 'Fortification';
                    }
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
                            this.SpinnerService.hide();
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
                    if (this.allPermitDetails.labResultsList.labResultsList !== []) {
                        this.labResultsDetailsList = this.allPermitDetails.labResultsList.labResultsList;
                        // tslint:disable-next-line:max-line-length
                        formattedArrayLabResultsList = this.labResultsDetailsList.map(i => [i.pdfName, i.complianceStatus, i.sffId, i.complianceRemarks, i.pdfSavedId]);
                        this.tableData4 = {
                            headerRow: ['File Name', 'Compliant Status', 'View Remarks', 'View PDF'],
                            dataRows: formattedArrayLabResultsList
                        };

                        this.complianceResultsDetailsList = this.allPermitDetails.labResultsList.ssfResultsList;
                        // tslint:disable-next-line:max-line-length
                        formattedArrayComplianceResultsList = this.complianceResultsDetailsList.map(i => [i.sffId, i.bsNumber, i.complianceStatus, i.complianceRemarks]);
                        this.tableData6 = {
                            headerRow: ['BS Number', 'Compliant Status', 'View Remarks', 'Add Remarks'],
                            dataRows: formattedArrayComplianceResultsList
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
                        const invoiceDetailsList = this.allPermitDetails.invoiceDetails.invoiceDetailsList;
                        let inspectionFee = 0;
                        let permitFee = 0;
                        let fMarkFee = 0;

                        for (let h = 0; h < invoiceDetailsList.length; h++) {
                            if (invoiceDetailsList[h].permitStatus === true) {
                                permitFee = invoiceDetailsList[h].itemAmount;
                            }
                            if (invoiceDetailsList[h].inspectionStatus === true) {
                                inspectionFee = invoiceDetailsList[h].itemAmount;
                            }
                            if (invoiceDetailsList[h].fmarkStatus === true) {
                                fMarkFee = invoiceDetailsList[h].itemAmount;
                            }
                        }

                        this.tableData12 = {
                            headerRow: ['Item', 'Details/Fee'],
                            dataRows: [
                                ['Invoice Ref No', this.allPermitDetails.invoiceDetails.invoiceRef],
                                ['Inspection Fee', `KSH ${inspectionFee}`],
                                ['FMARK Permit', `KSH ${fMarkFee}`],
                                ['SMARK Permit', `KSH ${permitFee}`],
                                // ['Description', this.allPermitDetails.invoiceDetails.description],
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
        const arrHead = ['hofQamCompletenessRemarks', 'labResultsCompletenessRemarks', 'reviewRemarksPCMRemarks', 'recommendationRemarks', 'pscApprovalRejectionRemarks', 'pcmApprovalRejectionRemarks'];
        // tslint:disable-next-line:max-line-length
        const arrHeadSave = ['Completeness Remarks', 'Lab Result Compliance Remarks', 'PCM Review Remarks', 'Recommendation', 'PSC Remarks', 'PCM Approval/Rejection Remarks'];

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
        this.SpinnerService.show();
        this.qaService.loadFileDetailsPDF(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
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
        this.SpinnerService.show();
        this.qaService.loadFileDetailsPDF(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                return dataPdf;
            },
        );
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

    reSubmitWithRemarks(): void {
        console.log(this.resubmitForm.value);
        this.SpinnerService.show();
        // tslint:disable-next-line:max-line-length
        this.qaService.resubmitApplicationBack(String(this.allPermitDetails.permitDetails.id), this.resubmitForm.value).subscribe(
            (data: PermitEntityDetails) => {
                this.allPermitDetails.permitDetails = data;
                this.SpinnerService.hide();
                swal.fire({
                    title: 'PERMIT APPLICATION RE-SUBMITTED SUCCESSFULLY!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
            },
        );
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
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your application is complete?',
            text: 'You won\'t be able to make changes after submission!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.qaService.submitPermitApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
                    (data: AllPermitDetailsDto) => {
                        this.allPermitDetails = data;
                        this.SpinnerService.hide();
                        if (this.allPermitDetails.permitDetails.permitTypeID === this.FMarkTypeID) {
                            swalWithBootstrapButtons.fire(
                                'Submitted!',
                                'FMARK SUBMITTED SUCCESSFULLY PENDING PAYMENT!',
                                'success'
                            );
                        } else {
                            swalWithBootstrapButtons.fire(
                                'Submitted!',
                                'SMARK SUBMITTED SUCCESSFULLY PENDING PAYMENT!',
                                'success'
                            );
                        }
                        this.reloadCurrentRoute();
                        // this.router.navigate(['/invoiceDetails'], {fragment: this.allPermitDetails.batchID.toString()});

                        // this.onUpdateReturnToList();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You can click the \'UPDATE APPLICATION DETAILS\' button to complete.',
                    'error'
                );
            }
        });
    }

    submitRenewalApplication() {
        this.SpinnerService.show();


        if (this.allPermitDetails.permitDetails.versionNumber == null) {
            this.SpinnerService.hide();

            this.showModal(this.allPermitDetails.permitDetails);

        } else {

            this.qaService.submitPermitRenewApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
                (data: AllPermitDetailsDto) => {
                    this.allPermitDetails = data;
                    this.SpinnerService.hide();
                    // this.reloadCurrentRoute();
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

                    this.router.navigate(['/smarkpermitdetails'], {fragment: String(this.allPermitDetails.permitDetails.id)});
                },
            );
        }
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

    viewLabRemarks(status: boolean, remarksValue: string, viewDiv: string) {
        const arrHead = ['viewLabResultsRemarks', 'viewLabComplianceRemarks'];
        const arrHeadSave = ['LAB RESULTS DETAILS', 'Inspection Compliance Status'];

        for (let h = 0; h < arrHead.length; h++) {
            if (viewDiv === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }
        this.currDiv = viewDiv;

        switch (status) {
            case true:
                this.labResultsStatus = this.COMPLIANTSTATUS;
                break;
            case false:
                this.labResultsStatus = this.NONCOMPLIANT;
                break;
        }

        this.labResultsRemarks = remarksValue;
    }

    openModalUpload(viewDiv: string, resubmit: string) {
        const arrHeadResubmit = ['resubmitLabNonComplianceResults', 'schemeModal', 'resubmitDetails'];
        const arrHead = ['uploadModal', 'schemeModal', 'resubmitDetails'];
        const arrHeadSave = ['Upload PDF Documents Only', 'Agree/Consent to SSC', 'Resubmit Details With Remarks'];

        for (let h = 0; h < arrHead.length; h++) {
            if (viewDiv === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }
        this.currDiv = viewDiv;
        this.resubmitDetail = resubmit;
        console.log(this.resubmitDetail);
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

    showModal(allPermitDetails: PermitEntityDetails) {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        // console.log(task.taskId);
        // if (mode === 'edit') {
        this.actionRequest = allPermitDetails;
        button.setAttribute('data-target', '#renewMigratedPermitModal');
        // }

        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    getObjectListFromData(ids) {
        return this.memberReturnArray.filter(item => ids.includes(item.id))
    }


    updatePermitWithData(id: any) {
        if (this.form.value.permits === '') {
            swal.fire({
                title: 'Please Select A Permit',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-danger form-wizard-next-btn ',
                },
                icon: 'error'
            });
        } else {
            this.SpinnerService.show();

            let data = this.form.value.permits;
            for (let i = 0; i < data.length; i++) {
                const obj = data[i];
                this.permit_id = obj.id
            }

            const bar: PermitDto = {permitId: this.permit_id, permitIdBeingMigrated: id};




            this.qaService.updateMigratedPermit(this.permit_id, id, bar).subscribe(
                (response) => {
                    console.log(response);
                    this.SpinnerService.hide();

                    swal.fire({
                        title: 'Permit Updated Successfully! Proceed to renew permit.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    }).then(function() {
                        location.reload(); // this is your location reload.
                    });
                    this.router.navigate(['/smarkpermitdetails'], {fragment: String(this.allPermitDetails.permitDetails.id)});

                    // this.onClickSaveUploads(response.body.savedRowID)
                    // this.prepareSacSummaryFormGroup.reset();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    console.log(error.message);
                }
            );


        }
    }


}
