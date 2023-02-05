import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {
    AllPermitDetailsDto,
    AllSTA10DetailsDto,
    FilesListDto,
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
} from "../../../core/store/data/qa/qa.model";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {FileUploadValidators} from "@iplab/ngx-file-upload";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {TableData} from "../../../md/md-table/md-table.component";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";

@Component({
    selector: 'app-permit-details-admin',
    templateUrl: './permit-details-admin.component.html',
    styleUrls: ['./permit-details-admin.component.css']
})
export class PermitDetailsAdminComponent implements OnInit {
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

    constructor(      private formBuilder: FormBuilder,
                      public router: Router, private route: ActivatedRoute, private qaService: QaService, private SpinnerService: NgxSpinnerService, private internalService: QaInternalService,
    ) {
    }

    id: any = "1";
    permitId: any
    steps: any;

    // stepSoFar: | undefined;
    ngOnInit(): void {
        this.route.paramMap.subscribe(paramMap => {
            this.permitId = decodeURIComponent(paramMap.get('id'));
        });

        // this.route.paramMap
        //     .pipe(map(() => window.history.state))
        //     .subscribe(state => {
        //         if (state && state.highlight != undefined) {
        //             this.permitId = state && state.highlight;
        //         } else {
        //             this.router.navigateByUrl('/smark-admin');
        //
        //         }
        //     });
        this.steps = 1;
        this.getSelectedPermit(this.permitId)
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
        console.log("Form is submitted")

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


    public getSelectedPermit(permitId: any): void {

        let formattedArraySta10 = [];
        let formattedArrayOrdinaryFiles = [];
        let formattedArrayLabResultsList = [];
        let formattedArrayComplianceResultsList = [];
        let formattedArrayOlderVersionList = [];
        const formattedArrayInvoiceDetailsList = [];
        this.SpinnerService.show();
        this.internalService.loadPermitDetail(permitId).subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {
                    // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
                    this.allPermitDetails = new AllPermitDetailsDto();
                    this.allPermitDetails = data?.data as AllPermitDetailsDto;
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
                    // console.log(`${this.sta10PersonnelDetails.length}`);

                    this.allSTA10Details = this.allPermitDetails.sta10DTO;
                    this.sta10Form.patchValue(this.allSTA10Details.sta10FirmDetails);
                    this.sta10PersonnelDetails = this.allSTA10Details.sta10PersonnelDetails;
                    this.sta10ProductsManufactureDetails = this.allSTA10Details.sta10ProductsManufactureDetails;
                    this.sta10RawMaterialsDetails = this.allSTA10Details.sta10RawMaterialsDetails;
                    this.sta10MachineryAndPlantDetails = this.allSTA10Details.sta10MachineryAndPlantDetails;
                    this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
                    this.sta10ManufacturingProcessDetails = this.allSTA10Details.sta10ManufacturingProcessDetails;
                    this.sta10FormF.patchValue(this.allSTA10Details.sta10FirmDetails);


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
                        this.SpinnerService.hide();

                    }
                } else {
                    this.SpinnerService.hide()
                    this.qaService.showError("Unable To View Permit.")
                    window.history.back();
                }
            }
        );
    }


}
