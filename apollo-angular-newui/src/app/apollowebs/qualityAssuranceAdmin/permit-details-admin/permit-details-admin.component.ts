import {Component, OnInit, TemplateRef, ViewChild, ViewEncapsulation} from '@angular/core';
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
    SSFComplianceStatusDetailsDto,
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
import {ApiResponseModel} from '../../../core/store/data/ms/ms.model';
import * as CryptoJS from 'crypto-js';
import {LoggedInUser, selectUserInfo} from '../../../core/store';
import {Store} from '@ngrx/store';


@Component({
    selector: 'app-permit-details-admin',
    templateUrl: './permit-details-admin.component.html',
    styleUrls: ['../../../../../node_modules/@ng-select/ng-select/themes/material.theme.css'],
    encapsulation: ViewEncapsulation.None


})
export class PermitDetailsAdminComponent implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    permitTypeName!: string;
    currDiv!: string;
    currDivLabel!: string;
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

    updateSectionForm: FormGroup;
    permitCompletenessForm: FormGroup;
    assignOfficerForm: FormGroup;
    addStandardsForm: FormGroup;
    scheduleInspectionForm: FormGroup;


    // DTOS
    loadedFirmType: FirmTypeEntityDto[] = [];
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

    openModalAddDetails(divVal: string): void {

        const arrHead = ['updateSection', 'permitCompleteness', 'assignOfficer', 'addStandardsDetails', 'scheduleInspectionDate', 'uploadSSC'];

        const arrHeadSave = ['Update Section', 'Is The Permit Complete', 'Select An officer', 'Add Standard details', 'Set The Date of Inspection', 'Upload scheme of supervision'];

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
    generateInspectionReport(permitId:string) {

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
