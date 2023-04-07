import {Component, OnInit, QueryList, TemplateRef, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {
    AllPermitDetailsDto,
    AllSTA10DetailsDto,
    FilesListDto,
    FirmTypeEntityDto, InspectionReportDetailsDto,
    PermitEntityDetails,
    PermitEntityDto,
    PlantDetailsDto, RemarksAndStatusDto,
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
    ApiResponseModel, ComplaintsFilesFoundDto,
    LIMSFilesFoundDto,
    MSSSFLabResultsDto,
    MSSSFPDFListDetailsDto,
    SampleSubmissionDto,
} from '../../../core/store/data/ms/ms.model';
import * as CryptoJS from 'crypto-js';
import {LoggedInUser, selectUserInfo} from '../../../core/store';
import {Store} from '@ngrx/store';
import {Subject} from 'rxjs';
import {DataTableDirective} from 'angular-datatables';
import {StandardsDto} from '../../../core/store/data/master/master.model';
import {formatDate} from '@angular/common';


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
    remarksForm!: FormGroup;
    uploadForm!: FormGroup;
    uploadedFile: File;
    uploadedFiles: FileList;
    upLoadDescription: string;
    public allPermitData: PermitEntityDto[];
    form: FormGroup;
    memberReturnArray: any[] = [];
    uploadedFilesOnly: FileList;
    uploadedSsfFilesOnly: FileList;
    uploadedFilesScheme: FileList;
    uploadedFilesAssessmentReport: FileList;
    uploadedFilesJustification: FileList;
    public actionRequest: PermitEntityDetails | undefined;

    dtOptions: DataTables.Settings = {};
    dtOptionsSSF: DataTables.Settings = {};
    dtTriggerSSF: Subject<any> = new Subject<any>();
    dtOptionsRemarks: DataTables.Settings = {};
    dtTriggerRemarks: Subject<any> = new Subject<any>();
    dtOptionsSCS: DataTables.Settings = {};
    dtTriggerSCS: Subject<any> = new Subject<any>();
    dtOptionsAssessment: DataTables.Settings = {};
    dtTriggerAssessment: Subject<any> = new Subject<any>();
    dtOptionsJustification: DataTables.Settings = {};
    dtTriggerJustification: Subject<any> = new Subject<any>();
    dtOptionsVersion: DataTables.Settings = {};
    dtTriggerVersion: Subject<any> = new Subject<any>();
    dtOptionsDOCS: DataTables.Settings = {};
    dtTriggerDOCS: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;

    dateFormat = 'MMM dd yyyy';
    language = 'en';
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
    docSSFUploadForm: FormGroup;

    updateSectionForm: FormGroup;
    updateBrandForm: FormGroup;
    permitCompletenessForm: FormGroup;
    assignOfficerForm: FormGroup;
    assignAssessorForm: FormGroup;
    addStandardsForm: FormGroup;
    scheduleInspectionForm: FormGroup;
    pcmAddAmountToInvoiceForm: FormGroup;
    ssfDetailsForm: FormGroup;
    recommendationForm: FormGroup;
    recommendationApproveRejectForm: FormGroup;
    approveRejectPermitForm: FormGroup;
    approveRejectPermitReviewForm: FormGroup;
    approveRejectRecommendationInspectionReportPermitForm: FormGroup;
    approveRejectInspectionReportForm: FormGroup;
    pdfSaveComplianceStatusForm!: FormGroup;
    ssfSaveComplianceStatusForm!: FormGroup;
    viewInspectionInvoiceDetailsForm!: FormGroup;


    // DTOS
    loadedFirmType: FirmTypeEntityDto[] = [];
    loadedStandards: StandardsDto[] = [];
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    allPermitDetails: AllPermitDetailsDto;
    selectedLabResults!: MSSSFLabResultsDto;
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
    DMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;
    draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;

    inspectionReportDetailsDto: InspectionReportDetailsDto;


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
                title: 'PARAMETER',
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
                {name: 'viewRecord', title: '<a class="btn btn-sm btn-primary">View PDF</a>'},
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
                type: 'string',
                valuePrepareFunction: (dataTest) => {
                    if (dataTest) {
                        return 'SAVED';
                    }
                    return 'NOT SAVED';
                },
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
                type: 'string',
                valuePrepareFunction: (dataTest) => {
                    if (dataTest) {
                        return 'COMPLIANT';
                    }
                    return 'NON-COMPLIANT';
                },
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
    public tableData10: TableData;
    public tableData11: TableData;
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
            resubmittedDetails: null,
            // resubmittedDetails: ['']
            // resubmittedDetails: [this.resubmitDetail, Validators.required]
            // approvedRemarks: [{value: '', disabled: true}, Validators.required],
        });

        this.remarksForm = this.formBuilder.group({
            remarksValue: ['', Validators.required],
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

        this.assignAssessorForm = this.formBuilder.group({
            leadAssessorId: ['', Validators.required],
            assessorId: ['', Validators.required],
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

        this.updateBrandForm = this.formBuilder.group({
            commodityDescription: ['', Validators.required],
            tradeMark: ['', Validators.required],
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

        this.pcmAddAmountToInvoiceForm = this.formBuilder.group({
            itemDescName: ['', Validators.required],
            itemAmount: ['', Validators.required],
            description: null,
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
            docFileName: ['', Validators.required],
        });


        this.docSSFUploadForm = this.formBuilder.group({
            docSSFUpload: null,
        });

        this.recommendationForm = this.formBuilder.group({
            recommendationRemarks: ['', Validators.required],
        });

        this.recommendationApproveRejectForm = this.formBuilder.group({
            recommendationApprovalStatus: ['', Validators.required],
            recommendationApprovalRemarks: null,
        });

        this.approveRejectPermitForm = this.formBuilder.group({
            approvedRejectedStatus: ['', Validators.required],
            approvedRejectedRemarks: null,
        });

        this.approveRejectPermitReviewForm = this.formBuilder.group({
            pcmReviewApprovalStatus: ['', Validators.required],
            pcmReviewApprovalRemarks: null,
        });

        this.viewInspectionInvoiceDetailsForm = this.formBuilder.group({
            uploadID: null,
            paidDate: null,
            endingDate: null,
        });

        this.approveRejectRecommendationInspectionReportPermitForm = this.formBuilder.group({
            approvedRejectedStatus: ['', Validators.required],
            approvedRejectedRemarks: ['', Validators.required],
            recommendationApprovalRemarks: ['', Validators.required],
            inspectionReportID: ['', Validators.required],
            supervisorComments: ['', Validators.required],
        });

        this.approveRejectInspectionReportForm = this.formBuilder.group({
            approvedRejectedStatus: ['', Validators.required],
            inspectionReportID: ['', Validators.required],
            supervisorComments: null,
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
        // window.$('body').removeClass('modal-open');
        // window.$('.modal-backdrop').remove();
        window.$('#sampleLabResultsModal').modal('hide');
        window.$('body').removeClass('modal-open');
        window.$('.modal-backdrop').remove();

        setTimeout(function() {
            window.$('#sampleLabResultsModal').modal('show');
        }, 500);
    }

    viewSavedRemarks(data: RemarksAndStatusDto) {
        this.currDivLabel = `REMARKS FOR PROCESS # ${data.processName}`;
        this.currDiv = 'viewRemarksDetails';
        this.remarksForm.patchValue(data);

        window.$('#allProcessModel').modal('show');
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

    viewSavedFilesSaved(data: FilesListDto) {
        this.viewPdfFile(String(data.id), data.name, data.fileType);
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

        if (this.selectedLabResults.savedPDFFiles.length === 0) {
            console.log('TEST 101 REF NO SAVE: ' + data.fileName);
            this.selectedPDFFileName = data.fileName;
            this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
            this.currDiv = 'pdfSaveCompliance';
            window.$('#myModal2').modal('show');
        } else {
            console.log('TEST 101 REF NO SAVE: ' + data.fileName);
                const savedPdf  = this.selectedLabResults.savedPDFFiles.find(pdf => pdf?.pdfName === data?.fileName);
          if (savedPdf === null || savedPdf === undefined) {
                console.log('TEST 101 REF NO SAVE: ' + data.fileName);
                this.selectedPDFFileName = data.fileName;
                this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
                this.currDiv = 'pdfSaveCompliance';
                window.$('#myModal2').modal('show');
            } else {
                this.qaService.showWarning('The Pdf selected With Name ' + savedPdf.pdfName + ', Already Saved');
            }
        }
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
                // if (applicationType === 'application/pdf') {
                    window.open(downloadURL, '_blank');
            },
            error => {
                this.SpinnerService.hide();
                console.log(error);
                this.qaService.showError('AN ERROR OCCURRED');
            },
        );
    }


    openModalAddDetails(divVal: string): void {

        const arrHead = ['viewInspectionInvoiceDetails', 'updateSection', 'permitCompleteness', 'assignOfficer', 'addStandardsDetails', 'scheduleAssessmentDate', 'scheduleInspectionDate', 'uploadSSC', 'uploadJustificationReport', 'uploadAttachments',
            'pcmAddAmountToInvoice', 'pcmReviewApproval', 'addSSFDetails', 'ssfAddComplianceStatus', 'addRecommendationRemarks', 'approveRejectRecommendation', 'uploadSSF', 'approveRejectInspectionReport',
            'dmarkApplicationApproval', 'hodApprovalAssessmentReport', 'approveRejectJustification', 'approvePermitQAMHOD', 'approvePermitQAMHODRecommendationInspectionReport', 'approvePermitPSCMember', 'approvePermitPCM', 'updateBrand'];

        const arrHeadSave = ['Uploaded Inspection Invoice Details', 'Update Section', 'Is The Permit Complete', 'Select An officer', 'Add Standard details', 'Set The Date of Assessment', 'Set The Date of Inspection', 'Upload scheme of supervision', 'Upload Justification for diamond mark assessment', 'UPLOAD ATTACHMENTS',
            'ADD AMOUNT TO GENERATED INVOICE', 'APPROVE / REJECT PERMIT REVIEW BY PCM', 'Add SSF Details Below', 'ADD SSF LAB RESULTS COMPLIANCE STATUS', 'ADD RECOMMENDATION', 'APPROVE/REJECT RECOMMENDATION', 'UPLOAD SSF', 'APPROVE/REJECT GENERATED INSPECTION REPORT',
            'APPROVE/DEFER PERMIT BY PAC', 'APPROVE/REJECT ASSESSMENT REPORT BY RM/HOD', 'APPROVE/REJECT JUSTIFICATION FOR DIAMOND MARK ASSESSMENT BY RM/HOD', 'APPROVE/REJECT PERMIT BY QAM/HOD', 'APPROVE/REJECT (PERMIT,RECOMMENDATION,GENERATED INSPECTION REPORT) BY QAM/HOD', 'APPROVE/REJECT PERMIT BY PSC MEMBER', 'APPROVE/REJECT PERMIT BY PCM', 'UPDATE BRAND'];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }

        if (divVal === 'viewInspectionInvoiceDetails') {
            this.viewInspectionInvoiceDetailsForm.patchValue(this.allPermitDetails?.inspectionInvoiceUpload);
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
        } else if (this.allPermitDetails.permitDetails.permitTypeID === this.DMarkTypeID) {
            this.permitTypeName = 'Diamond';
        }

        // tslint:disable-next-line:max-line-length
        this.companyProfileID = this.allPermitDetails?.oldVersionList?.find(pr => pr.id === this.allPermitDetails.permitDetails.id).companyId;
        // console.log(this.companyProfileID)
        this.sta1 = this.allPermitDetails.sta1DTO;
        this.sta1Form.patchValue(this.sta1);

        if (this.allPermitDetails?.permitDetails?.permitTypeID === this.SMarkTypeID) {
            this.allSTA10Details = this.allPermitDetails.sta10DTO;
            this.sta10Form.patchValue(this.allSTA10Details.sta10FirmDetails);
            const qaSta10ID = this.allSTA10Details.sta10FirmDetails.id;
            this.qaService.viewSTA10PersonnelDetails(String(qaSta10ID)).subscribe(
                (data10Personal: STA10PersonnelDto[]) => {
                    this.sta10PersonnelDetails = data10Personal;
                },
            );
            this.qaService.viewSTA10ProductsManufactureDetails(String(qaSta10ID)).subscribe(
                (data10ProductsManufacture: STA10ProductsManufactureDto) => {
                    this.sta10ProductsManufactureDetail = data10ProductsManufacture;
                },
            );
            this.qaService.viewSTA10RawMaterialsDetails(String(qaSta10ID)).subscribe(
                (data10RawMaterialsDetails: STA10RawMaterialsDto[]) => {
                    this.sta10RawMaterialsDetails = data10RawMaterialsDetails;
                },
            );
            // this.qaService.viewSTA10RawMaterialsDetails(String(qaSta10ID)).subscribe(
            //     (data10RawMaterialsDetails: STA10RawMaterialsDto[]) => {
            //         this.sta10RawMaterialsDetails = data10RawMaterialsDetails;
            //     },
            // );

            // this.sta10PersonnelDetails = this.allSTA10Details.sta10PersonnelDetails;
            // this.sta10ProductsManufactureDetails = this.allSTA10Details.sta10ProductsManufactureDetails;
            // this.sta10RawMaterialsDetails = this.allSTA10Details.sta10RawMaterialsDetails;
            this.sta10MachineryAndPlantDetails = this.allSTA10Details.sta10MachineryAndPlantDetails;
            this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
            this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
            this.sta10FormF.patchValue(this.allSTA10Details.sta10FirmDetails);
        }

        this.inspectionReportDetailsDto = this.allPermitDetails.inspectionReportDetails;


        this.qaService.loadFirmPermitList().subscribe(
            (dataFirmType: FirmTypeEntityDto[]) => {
                this.loadedFirmType = dataFirmType;
            },
            error => {
                console.log(error);
                this.qaService.showError('AN ERROR OCCURRED');
            },
        );

        this.qaService.loadStandardListList().subscribe(
            (standardsList: StandardsDto[]) => {
                this.loadedStandards = standardsList;
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
        if (this.allPermitDetails?.permitDetails?.invoiceGenerated === true) {
            const invoiceDetailsList = this.allPermitDetails?.invoiceDetails?.invoiceDetailsList;
            let inspectionFee = 0;
            let permitFee = 0;
            let fMarkFee = 0;
            let paidStatus = 'NOT PAID';
            if (this.allPermitDetails?.permitDetails?.paidStatus) {
                paidStatus = 'PAID';
            }

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

            if (this.allPermitDetails?.permitDetails?.permitTypeID === this.DMarkTypeID) {
                const formattedArrayInvoiceDetails = [];
                formattedArrayInvoiceDetails.push(['Invoice Ref No', this.allPermitDetails.invoiceDetails.invoiceRef]);

                for (let h = 0; h < invoiceDetailsList.length; h++) {
                    if (invoiceDetailsList[h].permitStatus === true) {
                        permitFee = invoiceDetailsList[h].itemAmount;
                        formattedArrayInvoiceDetails.push(['DMARK Permit', `KSH ${permitFee}`]);
                    } else {
                        formattedArrayInvoiceDetails.push([invoiceDetailsList[h].itemDescName, `KSH ${invoiceDetailsList[h].itemAmount}`]);
                    }
                }
                // tslint:disable-next-line:max-line-length
                formattedArrayInvoiceDetails.push(['Sub Total Before Tax', `KSH ${this.allPermitDetails.invoiceDetails.subTotalBeforeTax}`]);
                formattedArrayInvoiceDetails.push(['Tax Amount', `KSH ${this.allPermitDetails.invoiceDetails.taxAmount}`]);
                formattedArrayInvoiceDetails.push(['Total Amount', `KSH ${this.allPermitDetails.invoiceDetails.totalAmount}`]);
                this.tableData12 = {
                    headerRow: ['Item', 'Details/Fee'],
                    dataRows: formattedArrayInvoiceDetails,
                };
            } else {
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
                        ['Paid STATUS', paidStatus],

                    ],
                };
            }


            this.tableData10 = {
                headerRow: ['Item', 'Details/Fee'],
                dataRows: [
                    ['Invoice Ref No', this.allPermitDetails?.invoiceDifferenceDetails?.invoiceRef],
                    ['Inspection Fee', `KSH ${inspectionFee}`],
                    ['FMARK Permit', `KSH ${fMarkFee}`],
                    ['SMARK Permit', `KSH ${permitFee}`],
                    // ['Description', this.allPermitDetails.invoiceDetails.description],
                    ['Sub Total Before Tax', `KSH ${this.allPermitDetails?.invoiceDifferenceDetails?.subTotalBeforeTax}`],
                    ['Tax Amount', `KSH ${this.allPermitDetails?.invoiceDifferenceDetails?.taxAmount}`],
                    ['Total Amount', `KSH ${this.allPermitDetails?.invoiceDifferenceDetails?.totalAmount}`],
                    ['Paid STATUS', paidStatus],

                ],
            };
            this.tableData11 = {
                headerRow: ['Item', 'Details/Fee'],
                dataRows: [
                    ['Invoice Ref No', this.allPermitDetails?.inspectionFeeInvoice?.invoiceRef],
                    ['Inspection Fee', `KSH ${inspectionFee}`],
                    ['FMARK Permit', `KSH ${fMarkFee}`],
                    ['SMARK Permit', `KSH ${permitFee}`],
                    // ['Description', this.allPermitDetails.invoiceDetails.description],
                    ['Sub Total Before Tax', `KSH ${this.allPermitDetails?.inspectionFeeInvoice?.subTotalBeforeTax}`],
                    ['Tax Amount', `KSH ${this.allPermitDetails?.inspectionFeeInvoice?.taxAmount}`],
                    ['Total Amount', `KSH ${this.allPermitDetails?.inspectionFeeInvoice?.totalAmount}`],
                    ['Paid STATUS', paidStatus],
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

    onClickSaveResubmitFormResults(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Resubmit the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'RE-SUBMIT APPLICATION\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveResubmitFormResults(valid);
            });
    }

    saveResubmitFormResults(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaUpdateResubmit(this.resubmitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('RE-SUBMITTED DETAILS, SUCCESSFULLY', () => {
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

    onClickSaveBrandFormResults(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'UPDATE BRAND\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveBrandFormResults(valid);
            });
    }

    saveBrandFormResults(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaUpdateBrand(this.updateBrandForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('BRAND DETAILS, UPDATED SUCCESSFULLY', () => {
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


    onClickSaveApproveRejectInspectionReportFormResults(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT GENERATED INSPECTION REPORT\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApproveRejectInspectionReportFormResults(valid);
            });
    }

    SaveApproveRejectInspectionReportFormResults(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApproveRejectInspectionReport(this.approveRejectInspectionReportForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('INSPECTION REPORT STATUS, SAVED SUCCESSFULLY', () => {
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


    onClickSaveApprovePermitQAMHOD(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT PERMIT\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitQAMHOD(valid);
            });
    }

    SaveApprovePermitQAMHOD(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitQAMHOD(this.approveRejectRecommendationInspectionReportPermitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT STATUS, SAVED SUCCESSFULLY', () => {
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

    onClickSaveApprovePermitPSCMember(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT PERMIT AWARDING\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitPSCMember(valid);
            });
    }

    SaveApprovePermitPSCMember(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitPSCMember(this.approveRejectPermitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT STATUS, SAVED SUCCESSFULLY', () => {
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

    onClickSaveApprovePermitPCM(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT PERMIT AWARDING\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitPCM(valid);
            });
    }

    SaveApprovePermitPCM(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitPCM(this.approveRejectPermitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT STATUS, SAVED SUCCESSFULLY', () => {
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

    onClickSaveApprovePermitPAC(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT PERMIT AWARDING\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitPAC(valid);
            });
    }

    SaveApprovePermitPAC(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitPAC(this.approveRejectPermitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT STATUS, SAVED SUCCESSFULLY', () => {
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

    onClickSaveApprovePermitAssessmentReport(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT ASSESSMENT REPORT\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitAssessmentReport(valid);
            });
    }

    SaveApprovePermitAssessmentReport(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitAssessmentReport(this.approveRejectPermitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('ASSESSMENT REPORT STATUS, SAVED SUCCESSFULLY', () => {
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

    onClickSaveApprovePermitJustification(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT JUSTIFICATION FOR DIAMOND MARK ASSESSMENT\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitJustification(valid);
            });
    }

    SaveApprovePermitJustification(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitJustification(this.approveRejectPermitForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('JUSTIFICATION FOR DIAMOND MARK ASSESSMENT STATUS, SAVED SUCCESSFULLY', () => {
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

    onClickSaveApprovePermitPCMReview(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT PERMIT REVIEW\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveApprovePermitPCMReview(valid);
            });
    }

    SaveApprovePermitPCMReview(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaApprovePermitPCMReview(this.approveRejectPermitReviewForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT STATUS, SAVED SUCCESSFULLY', () => {
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
                        this.allPermitDetails = data1?.data as AllPermitDetailsDto;
                        // tslint:disable-next-line:max-line-length
                        const pdfSavedDetails = this.allPermitDetails?.sampleLabResults.find(lab => lab?.ssfResultsList?.bsNumber === this.selectedSSFDetails?.bsNumber);
                        const pdfSave = pdfSavedDetails?.savedPDFFiles?.find(dat => dat?.pdfName === this.selectedPDFFileName);
                        this.pdfSaveComplianceStatusForm.get('pdfSavedID').setValue(pdfSave.id);
                        this.qaService.qaPermitSavePDFCompliance(this.pdfSaveComplianceStatusForm.value, this.permitID).subscribe(
                            (data: ApiResponseModel) => {
                                if (data.responseCode === '00') {
                                    this.SpinnerService.hide();
                                    this.loadPermitDetails(data);
                                    this.allPermitDetails = data?.data as AllPermitDetailsDto;
                                    // tslint:disable-next-line:max-line-length
                                    this.selectedLabResults = this.allPermitDetails?.sampleLabResults.find(lab => lab?.ssfResultsList?.bsNumber === this.selectedSSFDetails?.bsNumber);
                                    // tslint:disable-next-line:max-line-length
                                    this.qaService.showSuccess('PDF AND COMPLIANCE STATUS, SAVED SUCCESSFULLY', () => {this.closePopUpsModal2(); });
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

    // savePDFComplianceRemarksSelected() {
    //     if (valid) {
    //         this.SpinnerService.show();
    //         this.qaService.qaPermitSavePDF(this.pdfSaveComplianceStatusForm.value, this.permitID).subscribe(
    //             (data1: ApiResponseModel) => {
    //                 if (data1.responseCode === '00') {
    //                     // tslint:disable-next-line:max-line-length
    // tslint:disable-next-line:max-line-length
    //                     const pdfSavedDetails = this.allPermitDetails?.sampleLabResults.find(lab => lab?.ssfResultsList?.bsNumber === this.selectedSSFDetails?.bsNumber);
    //                     const pdfSave = pdfSavedDetails?.savedPDFFiles?.find(dat => dat?.pdfName === this.selectedPDFFileName);
    //                     this.pdfSaveComplianceStatusForm.get('pdfSavedID').setValue(pdfSave.pdfSavedId);
    //
    //                 } else {
    //                     this.SpinnerService.hide();
    //                     this.qaService.showError(data1.message);
    //                 }
    //             },
    //             error => {
    //                 this.SpinnerService.hide();
    //                 this.qaService.showError('AN ERROR OCCURRED');
    //             },
    //         );
    //     }
    // }


    onClickSaveAssignOfficerForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'Assign Officer\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveAssignOfficerForm(valid);
            });
    }

    onClickSaveAssignAssessorForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'Assign Assessor\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveAssignAssessorForm(valid);
            });
    }

    onClickSaveRecommendationApprovalForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'APPROVE/REJECT RECOMMENDATION\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveRecommendationApprovalForm(valid);
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

    SaveAssignAssessorForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaAssignAssessor(this.assignAssessorForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('LEAD ASSESSOR AND ASSESSOR ASSIGNED SUCCESSFULLY', () => {
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

    SaveRecommendationApprovalForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaRecommendationApprovalForm(this.recommendationApproveRejectForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('RECOMMENDATION STATUS SAVED SUCCESSFULLY', () => {
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

    onClickSaveAddRecommendationForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'ADD RECOMMENDATION\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveAddRecommendationForm(valid);
            });
    }

    saveAddRecommendationForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaAddRecommendation(this.recommendationForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('RECOMMENDATION ADDED SUCCESSFULLY', () => {
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

    onClickSaveScheduleAssessmentForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'SCHEDULE ASSESSMENT DATE\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveScheduleAssessmentForm(valid);
            });
    }

    SaveScheduleAssessmentForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaScheduleAssessment(this.scheduleInspectionForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('ASSESSMENT DATE ADDED SUCCESSFULLY', () => {
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

    onClickSavePcmAddAmountToInvoiceForm(valid: boolean) {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'ADD AMOUNT TO INVOICE\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SavePcmAddAmountToInvoiceForm(valid);
            });
    }

    SavePcmAddAmountToInvoiceForm(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaPcmAddAmountToInvoice(this.pcmAddAmountToInvoiceForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('EXTRA AMOUNT ADDED SUCCESSFULLY', () => {
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

    onClickSavePcmSubmitPayment() {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'SUBMIT \' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SavePcmSubmitForPayment();
            });
    }

    SavePcmSubmitForPayment() {
        // if (valid) {
            this.SpinnerService.show();
            this.qaService.qaPcmSubmitForPayment(this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('PERMIT SUBMITTED FOR PAYMENT', () => {
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
        // }
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

    onClickSaveSSFComplianceStatus(valid: boolean) {
        if (valid) {
            this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
                // tslint:disable-next-line:max-line-length
                `You can click \'ADD COMPLIANCE STATUS\' button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
                    this.saveSSFComplianceStatus(valid);
                });
        }
    }


    saveSSFComplianceStatus(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.qaService.qaSSFDetailsCompliance(this.ssfSaveComplianceStatusForm.value, this.permitID).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.loadPermitDetails(data);
                        this.qaService.showSuccess('SSF COMPLIANCE STATUS SAVED SUCCESSFULLY', () => {
                            this.closePopUpsModal2();
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
                        this.qaService.showSuccess('SCHEME OF SUPERVISION UPLOADED SUCCESSFULLY', () => {
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

    onClickSaveJustificationReportForm() {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'UPLOAD JUSTIFICATION FOR DIAMOND MARK ASSESSMENT\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveJustificationReportForm();
            });
    }


    saveJustificationReportForm() {
        if (this.uploadedFilesJustification.length > 0) {
            this.SpinnerService.show();
            const file = this.uploadedFilesJustification;
            const formData = new FormData();
            formData.append('permitID', String(this.permitID));
            formData.append('data', 'JUSTIFICATION_REPORT_DMARK_ASSESSMENT');
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.qaService.qaSaveJustificationReport(formData).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('JUSTIFICATION REPORT DMARK ASSESSMENT UPLOADED SUCCESSFULLY', () => {
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

    onClickSaveAssessmentReportForm() {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
            // tslint:disable-next-line:max-line-length
            'You can click the \'UPLOAD ASSESSMENT REPORT\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.saveAssessmentReportForm();
            });
    }


    saveAssessmentReportForm() {
        if (this.uploadedFilesAssessmentReport.length > 0) {
            this.SpinnerService.show();
            const file = this.uploadedFilesAssessmentReport;
            const formData = new FormData();
            formData.append('permitID', String(this.permitID));
            formData.append('data', 'FACTORY_ASSESSMENT_REPORT');
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.qaService.qaSaveAssessmentReport(formData).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('FACTORY ASSESSMENT REPORT UPLOADED SUCCESSFULLY', () => {
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

    onClickSaveUploadsSSF() {
        this.qaService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',

            'You can click the \'UPLOAD SSF\' button to update details', 'COMPLAINT ACCEPT/DECLINE SUCCESSFUL', () => {
                this.SaveUploadsSSF();
            });
    }

    SaveUploadsSSF() {
        if (this.uploadedSsfFilesOnly.length > 0) {
            this.SpinnerService.show();
            const file = this.uploadedSsfFilesOnly;
            const formData = new FormData();
            formData.append('permitID', String(this.permitID));
            formData.append('docSSFUpload', this.docSSFUploadForm.get('docSSFUpload').value);
            formData.append('data', 'SCHEME_OF_SUPERVISION');
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('SSFdocFile', file[i], file[i].name);
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

        let text = permitId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        console.log('encrypted', encrypted);
        this.router.navigate(['/new-inspection-report', encrypted]);


    }
    goToInspectionReportPage(inspectionReportId: string) {
        let text = inspectionReportId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        console.log('encrypted', encrypted);
        this.router.navigate(['/inspection-report', encrypted]);

    }

    goToFmarkPermitDetails(permitId: string) {
        console.log('Permit ID ', permitId);
        let text = permitId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        // this.router.navigate(['/permit-details-admin', encrypted]);
        this.router.navigate(['/permit-details-admin', encrypted]).then(page => { window.location.reload(); });



    }

    formatFormDate(date: string) {
        return formatDate(date, this.dateFormat, this.language);
    }

}
