import {Component, OnInit, QueryList, TemplateRef, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {
    AllPermitDetailsDto,
    AllSTA10DetailsDto,
    FilesListDto,
    FirmTypeEntityDto,
    PermitEntityDetails,
    PermitEntityDto,
    PlantDetailsDto,
    SectionDto,
    SSFComplianceStatusDetailsDto, SSFDetailsDto,
    SSFPDFListDetailsDto,
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto,
} from '../../../core/store/data/qa/qa.model';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FileUploadValidators} from '@iplab/ngx-file-upload';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {TableData} from '../../../md/md-table/md-table.component';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {QaInternalService} from '../../../core/store/data/qa/qa-internal.service';
import {
    ApiResponseModel,
    LIMSFilesFoundDto,
    MSSSFLabResultsDto,
    MSSSFPDFListDetailsDto,
    SampleSubmissionDto
} from '../../../core/store/data/ms/ms.model';
import * as CryptoJS from 'crypto-js';
import {LoggedInUser, selectUserInfo} from '../../../core/store';
import {Store} from '@ngrx/store';
import {Subject} from 'rxjs';
import {DataTableDirective} from 'angular-datatables';


@Component({
    selector: 'app-permit-details-admin',
    templateUrl: './permit-details-admin.component.html',
    styleUrls: ['../../../../../node_modules/@ng-select/ng-select/themes/material.theme.css'],
    encapsulation: ViewEncapsulation.None,


})
export class PermitDetailsAdminComponent implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    permitTypeName!: string;
    currDiv!: string;
    currDivLabel!: string;
    selectedPDFFileName: string;
    roles: string[];
    userLoggedInID: number;
    companyProfileID: bigint;
    userProfile: LoggedInUser;
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
    uploadedFilesOnly: FileList;
    uploadedFilesScheme: FileList;
    public actionRequest: PermitEntityDetails | undefined;

    dtOptions: DataTables.Settings = {};
    dtOptionsSSF: DataTables.Settings = {};
    dtTriggerSSF: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;


    pdfSources: any;
    SelectedSectionId;
    pdfInvoiceBreakDownSources: any;
    pdfSourcesScheme: any;
    pdfUploadsView: any;
    selectCompletenessStatus!: string;
    selectForStatusStatus!: string;
    loading = false;


    /// FORMS
    sta1Form: FormGroup;
    sta10Form: FormGroup;
    sta10FormA: FormGroup;
    sta10FormB: FormGroup;
    sta10FormC: FormGroup;
    sta10FormD: FormGroup;
    sta10FormE: FormGroup;
    sta10FormF: FormGroup;
    sta10FormG: FormGroup;

    qaMCompleteness: FormGroup;
    assignOfficer: FormGroup;
    addStandards: FormGroup;
    factoryVisit: FormGroup;
    docFileNameForm: FormGroup;

    updateSectionForm: FormGroup;
    permitCompletenessForm: FormGroup;
    assignOfficerForm: FormGroup;
    addStandardsForm: FormGroup;
    scheduleInspectionForm: FormGroup;
    ssfDetailsForm: FormGroup;
    pdfSaveComplianceStatusForm!: FormGroup;
    ssfSaveComplianceStatusForm!: FormGroup;


    // DTOS
    loadedFirmType: FirmTypeEntityDto[] = [];
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    allPermitDetails: AllPermitDetailsDto;
    selectedLabResults: MSSSFLabResultsDto;
    selectedSSFDetails: SSFDetailsDto;
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


    public settingsLabResultsParam = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [],
            position: 'right', // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true,
        },
        noDataMessage: 'No data found',
        columns: {
            param: {
                title: 'PARAM',
                type: 'string',
                filter: false,
            },
            result: {
                title: 'RESULT',
                type: 'string',
                filter: false,
            },
            method: {
                title: 'METHOD',
                type: 'string',
                filter: false,
            },
        },
        pager: {
            display: true,
            perPage: 20,
        },
    };
    public settingsLIMSPDFFiles = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View PDF</i>'},
                {name: 'saveRecord', title: '<i class="btn btn-sm btn-primary">Save PDF</i>'},
            ],
            position: 'right', // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true,
        },
        noDataMessage: 'No data found',
        columns: {
            fileName: {
                title: 'FILE NAME',
                type: 'string',
                filter: false,
            },
            fileSavedStatus: {
                title: 'FILE SAVED STATUS',
                type: 'boolean',
                filter: false,
            },
        },
        pager: {
            display: true,
            perPage: 20,
        },
    };
    public settingsSavedPDFFiles = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                {name: 'viewPDFRemarks', title: '<i class="btn btn-sm btn-primary">View Remarks</i>'},
                {name: 'viewPDFRecord', title: '<i class="btn btn-sm btn-primary">View PDF</i>'},
            ],
            position: 'right', // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true,
        },
        noDataMessage: 'No data found',
        columns: {
            pdfName: {
                title: 'PDF NAME',
                type: 'string',
                filter: false,
            },
            complianceStatus: {
                title: 'COMPLIANCE STATUS',
                type: 'boolean',
                filter: false,
            },
        },
        pager: {
            display: true,
            perPage: 20,
        },
    };

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

    constructor(private formBuilder: FormBuilder,
                public router: Router,
                private route: ActivatedRoute,
                private store$: Store<any>,
                private qaService: QaService,
                private SpinnerService: NgxSpinnerService,
                private internalService: QaInternalService,
    ) {
    }

    id: any = '1';
    permitId: any;
    steps: any;

    // stepSoFar: | undefined;
    ngOnInit(): void {
        this.route.paramMap.subscribe(paramMap => {
            let key = '11A1764225B11AA1';
            const encrypted = paramMap.get('id');
            key = CryptoJS.enc.Utf8.parse(key);
            const decrypted = CryptoJS.AES.decrypt({ciphertext: CryptoJS.enc.Hex.parse(encrypted)}, key, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.ZeroPadding,
            });
            this.permitId = decrypted.toString(CryptoJS.enc.Utf8);

        });

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.userLoggedInID = u.id;
            this.userProfile = u;
            return this.roles = u.roles;
        });

        this.steps = 1;
        this.getSelectedPermit(this.permitId);

        this.sta1Form = this.formBuilder.group({
            commodityDescription: [{value: '', disabled: true}, Validators.required],
            applicantName: [{value: '', disabled: true}, Validators.required],
            sectionName: [{value: '', disabled: true}, Validators.required],
            permitForeignStatus: [],
            branchName: [{value: '', disabled: true}, Validators.required],
            tradeMark: [{value: '', disabled: true}, Validators.required],
            createFmark: [{value: '', disabled: true}],

        });
        this.sta10Form = this.formBuilder.group({
            totalNumberFemale: [{value: '', disabled: true}, Validators.required],
            totalNumberMale: [{value: '', disabled: true}, Validators.required],
            totalNumberPermanentEmployees: [{value: '', disabled: true}, Validators.required],
            totalNumberCasualEmployees: [{value: '', disabled: true}, Validators.required],
            averageVolumeProductionMonth: [{value: '', disabled: true}, Validators.required],

        });


        this.sta10FormA = this.formBuilder.group({
            personnelName: [],
            qualificationInstitution: [],
            dateOfEmployment: [],

        });

        this.ssfSaveComplianceStatusForm = this.formBuilder.group({
            ssfID: ['', Validators.required],
            resultsAnalysis: ['', Validators.required],
            complianceRemarks: ['', Validators.required],
        });

        this.pdfSaveComplianceStatusForm = this.formBuilder.group({
            pdfSavedID: null,
            complianceStatus: null,
            complianceRemarks: null,
            fileName: null,
            bsNumber: null,
            ssfID: null,
        });

        this.sta10FormB = this.formBuilder.group({
            productName: [],
            productBrand: [],
            productStandardNumber: [],
            available: [],
            permitNo: [],
        });

        this.sta10FormC = this.formBuilder.group({
            name: [],
            origin: [],
            specifications: [],
            qualityChecksTestingRecords: [],
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
            processMonitoringRecords: [],
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
            applicationDate: [{value: '', disabled: true}, Validators.required],
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
            uploadedFile: this.filesControl,
            // approvedRemarks: [{value: '', disabled: true}, Validators.required],
        });

        this.qaMCompleteness = this.formBuilder.group({
            hofQamCompletenessStatus: ['', Validators.required],
            hofQamCompletenessRemarks: ['', Validators.required],
        });
        this.assignOfficerForm = this.formBuilder.group({
            assignOfficerID: ['', Validators.required],
            assignRemarks: null,
        });

        this.addStandardsForm = this.formBuilder.group({
            productStandardID: ['', Validators.required],
            productStandardRemarks: null,
        });

        this.updateSectionForm = this.formBuilder.group({
            sectionId: ['', Validators.required],
            sectionRemarks: null,
        });

        this.permitCompletenessForm = this.formBuilder.group({
            hofQamCompletenessStatus: ['', Validators.required],
            rejectedForStatus: null,
            hofQamCompletenessRemarks: null,
            companyProfileID: null,
            updateFirmType: null,
            requesterComment: null,
            upgradeType: null,
        });

        this.factoryVisit = this.formBuilder.group({
            inspectionDate: ['', Validators.required],
            productStandardRemarks: ['', Validators.required],
        });

        this.scheduleInspectionForm = this.formBuilder.group({
            inspectionDate: ['', Validators.required],
            scheduleRemarks: null,
        });

        this.ssfDetailsForm = this.formBuilder.group({
            id: null,
            ssfNo: ['', Validators.required],
            ssfSubmissionDate: ['', Validators.required],
            bsNumber: ['', Validators.required],
            brandName: ['', Validators.required],
            productDescription: ['', Validators.required],
        });

        this.docFileNameForm = this.formBuilder.group({
            docFileName: null,
        });



        this.dropdownSettings = {
            singleSelection: true,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true,
        };
    }



    tabChange(ids: any) {
        this.id = ids;
        console.log(this.id);
    }

    nextStep(currentStep) {
        this.steps = currentStep += 1;
    }

    previousStep(currentStep) {
        this.steps = currentStep -= 1;
    }

    newinspectionform(NewInspectionForm) {
        console.log('Form is submitted');

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

    get formQamCompleteness(): any {
        return this.qaMCompleteness.controls;
    }

    get formAssignOfficer(): any {
        return this.assignOfficer.controls;
    }

    get formAddStandards(): any {
        return this.addStandards.controls;
    }

    get formfactoryVisit(): any {
        return this.factoryVisit.controls;
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


    public onCustomLIMSPDFViewAction(event: any): void {
        switch (event.action) {
            case 'viewPDFRecord':
                this.viewLIMSPDFSaved(event.data);
                break;
            case 'viewPDFRemarks':
                this.viewLIMSPDFSavedRemarks(event.data);
                break;
        }
    }

    closeSSFLabResultsRecord() {
        window.$('#myModal2').modal('hide');
        window.$('#sampleLabResultsModal').modal('hide');
        window.$('body').removeClass('modal-open');
        window.$('.modal-backdrop').remove();

    }

    closePopUpsModal2() {
        window.$('#myModal2').modal('hide');
        window.$('body').removeClass('modal-open');
        window.$('.modal-backdrop').remove();
        window.$('#sampleLabResultsModal').modal('hide');
        window.$('body').removeClass('modal-open');
        window.$('.modal-backdrop').remove();

        setTimeout(function() {
            window.$('#sampleLabResultsModal').modal('show');
        }, 500);
    }

    viewLIMSPDFSavedRemarks(data: MSSSFPDFListDetailsDto) {
        this.selectedPDFFileName = data.pdfName;
        this.currDivLabel = `COMPLIANCE STATUS AND REMARKS FOR PDF # ${this.selectedPDFFileName}`;
        this.currDiv = 'viewPdfSaveCompliance';
        this.pdfSaveComplianceStatusForm.patchValue(data);

        window.$('#myModal2').modal('show');
    }

    viewLIMSPDFSaved(data: MSSSFPDFListDetailsDto) {
        console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);

        this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
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
            error => {
                this.SpinnerService.hide();
                console.log(error);
                // this.msService.showError('AN ERROR OCCURRED');
            },
        );
    }



    viewSSFLabResultsRecord(data: SSFDetailsDto) {
        this.selectedSSFDetails = data;
        this.selectedLabResults = this.allPermitDetails?.sampleLabResults.find(lab => lab?.ssfResultsList?.bsNumber === data?.bsNumber);

        window.$('#myModal2').modal('hide');
        // window.$('.modal').remove();
        window.$('body').removeClass('modal-open');
        window.$('.modal-backdrop').remove();
        window.$('#sampleLabResultsModal').modal('show');
    }


    onCustomLIMSPDFAction(event: any, bsNumber: string): void {
        switch (event.action) {
            case 'viewRecord':
                this.viewLIMSPDFRecord(event.data, bsNumber);
                break;
            case 'saveRecord':
                this.saveLIMSPDFRecord(event.data);
                break;
        }
    }


    saveLIMSPDFRecord(data: LIMSFilesFoundDto) {
        console.log('TEST 101 REF NO SAVE: ' + data.fileName);
        this.selectedPDFFileName = data.fileName;
        this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
        this.currDiv = 'pdfSaveCompliance';

        window.$('#myModal2').modal('show');
    }


    viewLIMSPDFRecord(data: LIMSFilesFoundDto, bsNumber: string) {
        console.log('TEST 101 REF NO VIEW: ' + data.fileName);
        this.viewLabResultsPdfFile(String(data.fileName), bsNumber, 'application/pdf');
    }

    viewLabResultsPdfFile(fileName: string, bsNumber: string, applicationType: string): void {
        this.SpinnerService.show();
        this.qaService.loadFileDetailsLabResultsPDF(fileName, bsNumber).subscribe(
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
            error => {
                this.SpinnerService.hide();
                console.log(error);
                this.qaService.showError('AN ERROR OCCURRED');
            },
        );
    }


    openModalAddDetails(divVal: string): void {

        const arrHead = ['updateSection', 'permitCompleteness', 'assignOfficer', 'addStandardsDetails', 'scheduleInspectionDate', 'uploadSSC', 'uploadAttachments',
            'addSSFDetails', 'ssfAddComplianceStatus'];

        const arrHeadSave = ['Update Section', 'Is The Permit Complete', 'Select An officer', 'Add Standard details', 'Set The Date of Inspection', 'Upload scheme of supervision', 'UPLOAD ATTACHMENTS',
            'Add SSF Details Below', 'ADD SSF LAB RESULTS COMPLIANCE STATUS', ];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }

        this.currDiv = divVal;
    }

    updateSelectedStatus() {
        const valueSelected = this.permitCompletenessForm?.get('hofQamCompletenessStatus')?.value;
        if (valueSelected) {
            this.selectCompletenessStatus = 'Accept';
        } else {
            this.selectCompletenessStatus = 'Reject';
        }
    }

    updateSelectedRejectionStatus() {
        const valueSelected = this.permitCompletenessForm?.get('rejectedForStatus')?.value;
        if (valueSelected) {
            this.selectForStatusStatus = 'RejectAmendment';
        } else {
            this.selectForStatusStatus = 'RejectForUpgrade';
        }
    }


    public getSelectedPermit(permitId: any): void {
        this.SpinnerService.show();
        this.internalService.loadPermitDetail(permitId).subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {
                    this.loadPermitDetails(data);
                    this.SpinnerService.hide();
                } else {
                    this.SpinnerService.hide();
                    this.qaService.showError(data.message);
                    window.history.back();
                }
            },
        );
    }

    loadPermitDetails(data: ApiResponseModel) {
        let formattedArraySta10 = [];
        let formattedArrayOrdinaryFiles = [];
        let formattedArrayLabResultsList = [];
        let formattedArrayComplianceResultsList = [];
        let formattedArrayOlderVersionList = [];
        const formattedArrayInvoiceDetailsList = [];


        this.allPermitDetails = data?.data as AllPermitDetailsDto;
        this.permitID = String(this.allPermitDetails.permitDetails.id);
        // console.log('Permit ID added' + this.permitID)

        this.batchID = this.allPermitDetails.batchID;
        if (this.allPermitDetails.permitDetails.permitTypeID === this.SMarkTypeID) {
            this.permitTypeName = 'Standardization';
        } else if (this.allPermitDetails.permitDetails.permitTypeID === this.FMarkTypeID) {
            this.permitTypeName = 'Fortification';
        }

        // tslint:disable-next-line:max-line-length
        this.companyProfileID = this.allPermitDetails?.oldVersionList?.find(pr => pr.id === this.allPermitDetails.permitDetails.id).companyId;
        // console.log(this.companyProfileID)
        this.sta1 = this.allPermitDetails.sta1DTO;
        this.sta1Form.patchValue(this.sta1);

        this.allSTA10Details = this.allPermitDetails.sta10DTO;
        this.sta10Form.patchValue(this.allSTA10Details.sta10FirmDetails);
        this.sta10PersonnelDetails = this.allSTA10Details.sta10PersonnelDetails;
        this.sta10ProductsManufactureDetails = this.allSTA10Details.sta10ProductsManufactureDetails;
        this.sta10RawMaterialsDetails = this.allSTA10Details.sta10RawMaterialsDetails;
        this.sta10MachineryAndPlantDetails = this.allSTA10Details.sta10MachineryAndPlantDetails;
        this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
        this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
        this.sta10FormF.patchValue(this.allSTA10Details.sta10FirmDetails);

        this.qaService.loadFirmPermitList().subscribe(
            (dataFirmType: FirmTypeEntityDto[]) => {
                this.loadedFirmType = dataFirmType;
            },
            error => {
                console.log(error);
                this.qaService.showError('AN ERROR OCCURRED');
            },
        );

        if (this.allPermitDetails.sta10FilesList !== []) {
            this.sta10FileList = this.allPermitDetails.sta10FilesList;
            // tslint:disable-next-line:max-line-length
            formattedArraySta10 = this.sta10FileList.map(i => [i.name, i.fileType, i.documentType, i.versionNumber, i.id, i.document]);
            this.tableData2 = {
                headerRow: ['File Name', 'File Type', 'Document Description', 'Version Number', 'Action'],
                dataRows: formattedArraySta10,
            };
        }
        if (this.allPermitDetails.ordinaryFilesList !== []) {
            this.ordinaryFilesList = this.allPermitDetails.ordinaryFilesList;
            // tslint:disable-next-line:max-line-length
            formattedArrayOrdinaryFiles = this.ordinaryFilesList.map(i => [i.name, i.fileType, i.documentType, i.versionNumber, i.id]);
            this.tableData3 = {
                headerRow: ['File Name', 'File Type', 'Document Description', 'Version Number', 'Action'],
                dataRows: formattedArrayOrdinaryFiles,
            };
        }
        if (this.allPermitDetails.labResultsList.labResultsList !== []) {
            this.labResultsDetailsList = this.allPermitDetails.labResultsList.labResultsList;
            // tslint:disable-next-line:max-line-length
            formattedArrayLabResultsList = this.labResultsDetailsList.map(i => [i.pdfName, i.complianceStatus, i.sffId, i.complianceRemarks, i.pdfSavedId]);
            this.tableData4 = {
                headerRow: ['File Name', 'Compliant Status', 'View Remarks', 'View PDF'],
                dataRows: formattedArrayLabResultsList,
            };

            this.complianceResultsDetailsList = this.allPermitDetails.labResultsList.ssfResultsList;
            // tslint:disable-next-line:max-line-length
            formattedArrayComplianceResultsList = this.complianceResultsDetailsList.map(i => [i.sffId, i.bsNumber, i.complianceStatus, i.complianceRemarks]);
            this.tableData6 = {
                headerRow: ['BS Number', 'Compliant Status', 'View Remarks', 'Add Remarks'],
                dataRows: formattedArrayComplianceResultsList,
            };
        }
        if (this.allPermitDetails.oldVersionList !== []) {
            this.olderVersionDetailsList = this.allPermitDetails.oldVersionList;
            // tslint:disable-next-line:max-line-length
            formattedArrayOlderVersionList = this.olderVersionDetailsList.map(i => [i.permitRefNumber, i.createdOn, i.permitStatus, i.versionNumber, i.id]);
            this.tableData5 = {
                headerRow: ['Permit Ref Number', 'Created On', 'Status', 'Version Number', 'Action'],
                dataRows: formattedArrayOlderVersionList,
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
                    ['Total Amount', `KSH ${this.allPermitDetails.invoiceDetails.totalAmount}`],

                ],
            };
            this.SpinnerService.hide();

        }
    }

    onClickSaveSectionFormResults(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'UPDATE SECTION\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveSectionFormResults(valid);
            });
    }

    saveSectionFormResults(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaUpdateSection(this.updateSectionForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('SECTION DETAILS, SAVED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }

    onClickSavePermitCompletenessFormResults(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'REVIEW FOR COMPLETENESS\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SavePermitCompletenessFormResults(valid);
            });
    }

    SavePermitCompletenessFormResults(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaPermitCompleteness(this.permitCompletenessForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT COMPLETENESS STATUS, SAVED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }

    onClickSavePermitCompletenessForUpgradeFormResults(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'REVIEW FOR COMPLETENESS\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.savePermitCompletenessForUpgradeFormResults(valid);
            });
    }

    savePermitCompletenessForUpgradeFormResults(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaPermitCompletenessUpgrade(this.permitCompletenessForm.value, this.permitID).subscribe(
                (data1: ApiResponseModel) => {
                    this.qaService.qaUpdateDifferenceStatus(this.permitCompletenessForm.value, this.permitID).subscribe(
                        (data: ApiResponseModel) => {
                            if (data.responseCode === '00') {
                                this.SpinnerService.hide();
                                this.qaService.showSuccess('PERMIT COMPLETENESS STATUS, SAVED SUCCESSFULLY', () => {
                                    this.loadPermitDetails(data);
                                });
                            } else {
                                this.SpinnerService.hide();
                                this.qaService.showError(data.message);
                            }
                        },
                        error => {
                            this.SpinnerService.hide();
                            this.qaService.showError('AN ERROR OCCURRED');
                        },
                    );
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }


    onClickSavePDFSelected(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'Save PDF\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.savePDFSelected(valid);
            });
    }

    savePDFSelected(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaPermitSavePDF(this.pdfSaveComplianceStatusForm.value, this.permitID).subscribe(
                (data1: ApiResponseModel) => {
                    if (data1.responseCode === '00') {
                        // tslint:disable-next-line:max-line-length
                        const pdfSavedDetails = this.allPermitDetails?.sampleLabResults.find(lab => lab?.ssfResultsList?.bsNumber === this.selectedSSFDetails?.bsNumber);
                        const pdfSave = pdfSavedDetails?.savedPDFFiles?.find(dat => dat?.pdfName === this.selectedPDFFileName);
                        this.pdfSaveComplianceStatusForm.get('pdfSavedID').setValue(pdfSave.pdfSavedId);
                        this.qaService.qaPermitSavePDFCompliance(this.pdfSaveComplianceStatusForm.value, this.permitID).subscribe(
                            (data: ApiResponseModel) => {
                                if (data.responseCode === '00') {
                                    this.SpinnerService.hide();
                                    this.qaService.showSuccess('PDF AND COMPLINACE STATUS, SAVED SUCCESSFULLY', () => {
                                        this.loadPermitDetails(data);
                                    });
                                } else {
                                    this.SpinnerService.hide();
                                    this.qaService.showError(data.message);
                                }
                            },
                            error => {
                                this.SpinnerService.hide();
                                this.qaService.showError('AN ERROR OCCURRED');
                            },
                        );
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data1.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }


    onClickSaveAssignOfficerForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'Assign Officer\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveAssignOfficerForm(valid);
            });
    }

    SaveAssignOfficerForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaAssignOfficer(this.assignOfficerForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('OFFICER ASSIGNED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }

    onClickSaveAddStandardsForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'ADD/UPDATE STANDARDS DETAILS\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveAddStandardsForm(valid);
            });
    }

    SaveAddStandardsForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaStandardsAdd(this.addStandardsForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('STANDARDS ADDED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }


    onClickSaveScheduleInspectionForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'ADD/UPDATE STANDARDS DETAILS\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveScheduleInspectionForm(valid);
            });
    }

    SaveScheduleInspectionForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaScheduleInspectionReport(this.scheduleInspectionForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('INSPECTION DATE ADDED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }

    onClickSaveAddSSFDetails(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'ADD SSF & BS NUMBER\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveAddSSFDetails(valid);
            });
    }

    saveAddSSFDetails(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaSSFDetails(this.ssfDetailsForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('SSF & BS NUMBER ADDED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }

    onClickSaveSSFLabResultsComplianceStatus(valid: boolean) {
        if (valid) {
            this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
                // tslint:disable-next-line:max-line-length
                `You can click \'ADD COMPLIANCE STATUS\' button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
                    this.saveSSFLabResultsComplianceStatus(valid);
                });
        }
    }


    saveSSFLabResultsComplianceStatus(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaSSFDetailsCompliance(this.ssfDetailsForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('SSF COMPLIANCE STATUS SAVED SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }


    onClickSaveSSCForm() {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'UPLOAD SCHEME OF SUPERVISION AND CONTROL\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveSSCForm();
            });
    }


    saveSSCForm() {
        if (this.uploadedFilesScheme.length > 0) {
            this.SpinnerService.show();
            const file = this.uploadedFilesScheme;
            const formData = new FormData();
            formData.append('permitID', String(this.permitID));
            formData.append('data', 'SCHEME_OF_SUPERVISION');
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.qaService.qaSaveSCS(formData).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('SCHEME OF SUPERVISION UPLOADED SUCCESSFULL SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        } else {
            this.qaService.showError('NO FILE IS UPLOADED FOR SAVING');
        }
    }

    onClickSaveUploadsAttachments() {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'UPLOAD ATTACHMENTS\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveUploadsAttachments();
            });
    }


    saveUploadsAttachments() {
        if (this.uploadedFilesOnly.length > 0) {
            this.SpinnerService.show();
            const file = this.uploadedFilesOnly;
            const formData = new FormData();
            formData.append('permitID', String(this.permitID));
            formData.append('docFileName', this.docFileNameForm.get('docFileName').value);
            formData.append('data', 'SCHEME_OF_SUPERVISION');
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.qaService.qaSaveUploadFile(formData).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('UPLOAD(S) SAVED  SUCCESSFULLY', () => {
                            this.loadPermitDetails(data);
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        } else {
            this.qaService.showError('NO FILE IS UPLOADED FOR SAVING');
        }
    }



    reviewForCompleteness(formDirective): void {

        if (this.qaMCompleteness.valid) {

            this.SpinnerService.show();
            this.internalService.submitQaMCompleteness(this.permit_id, this.qaMCompleteness.value).subscribe(
                (response) => {

                    // let requestNumber = response.responseText+":"+response.body.body.requestNumber;
                    this.SpinnerService.hide();
                    formDirective.resetForm();
                    this.qaMCompleteness.reset();
                    this.qaService.showSuccess('Review Successfully submitted');
                });

        } else {
            this.qaService.showError('Please Try Again');

        }


    }

    generateInspectionReport(permitId: string) {

        var text = permitId;
        var key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        var encrypted = CryptoJS.AES.encrypt(text, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        console.log('encrypted', encrypted);
        this.router.navigate(['/new-inspection-report',encrypted])


    }

}
