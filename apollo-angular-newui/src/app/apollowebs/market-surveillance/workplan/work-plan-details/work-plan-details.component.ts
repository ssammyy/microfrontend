import { Component, OnInit } from '@angular/core';
import {
  AllComplaintsDetailsDto, ApprovalDto,
  BSNumberSaveDto,
  ChargeSheetDto,
  ComplaintAdviceRejectDto,
  ComplaintApproveRejectAdviceWhereDto,
  ComplaintAssignDto,
  ComplaintClassificationDto,
  ComplaintsFilesFoundDto,
  CompliantRemediationDto,
  CountriesEntityDto,
  CountryListDto, DataInspectorInvestDto,
  DataReportDto,
  DataReportParamsDto,
  FuelEntityAssignOfficerDto,
  FuelEntityRapidTestDto,
  FuelInspectionDto, InspectionInvestigationReportDto,
  LaboratoryDto,
  LIMSFilesFoundDto,
  MsDepartment,
  MsDivisionDetails,
  MSRemarksDto,
  MSSSFPDFListDetailsDto,
  PDFSaveComplianceStatusDto,
  PreliminaryReportDto, PreliminaryReportFinal, PreliminaryReportItemsDto,
  RemediationDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto, SeizureDeclarationDto,
  SSFSaveComplianceStatusDto,
  WorkPlanInspectionDto,
  WorkPlanScheduleApprovalDto,
} from '../../../../core/store/data/ms/ms.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  BroadProductCategory,
  ProductCategories,
  Products,
  ProductSubcategory,
  StandardProductCategory,
} from '../../../../core/store/data/master/master.model';
import {LoggedInUser, selectUserInfo} from '../../../../core/store';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {Store} from '@ngrx/store';
import {NgxSpinnerService} from 'ngx-spinner';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-work-plan-details',
  templateUrl: './work-plan-details.component.html',
  styleUrls: ['./work-plan-details.component.css'],
})
export class WorkPlanDetailsComponent implements OnInit {
  active: Number = 0;
  selectedRefNo: string;
  selectedBatchRefNo: string;
  selectedPDFFileName: string;
  currDiv!: string;
  currDivLabel!: string;
  submitted = false;
  approveScheduleForm!: FormGroup;
  approvePreliminaryForm!: FormGroup;
  preliminaryRecommendationForm!: FormGroup;
  chargeSheetForm!: FormGroup;
  dataReportForm!: FormGroup;
  dataReportParamForm!: FormGroup;
  seizureDeclarationForm!: FormGroup;
  investInspectReportForm!: FormGroup;
  preliminaryReportForm!: FormGroup;
  preliminaryReportParamForm!: FormGroup;
  investInspectReportInspectorsForm!: FormGroup;
  // chargeSheetForm!: FormGroup;

  assignOfficerForm!: FormGroup;

  remarksSavedForm!: FormGroup;
  rapidTestForm!: FormGroup;
  sampleCollectForm!: FormGroup;
  sampleCollectItemsForm!: FormGroup;
  sampleSubmitForm!: FormGroup;
  sampleSubmitParamsForm!: FormGroup;
  sampleSubmitBSNumberForm!: FormGroup;
  pdfSaveComplianceStatusForm!: FormGroup;
  ssfSaveComplianceStatusForm!: FormGroup;
  scheduleRemediationForm!: FormGroup;
  notCompliantInvoiceForm!: FormGroup;
  remediationForm!: FormGroup;
  dataSaveApproveSchedule: WorkPlanScheduleApprovalDto;
  dataSaveApprovePreliminary: ApprovalDto;
  dataSaveFinalPreliminary: PreliminaryReportFinal;
  dataSaveChargeSheet: ChargeSheetDto;
  dataSaveDataReport: DataReportDto;
  dataSaveDataInspectorInvestList: DataInspectorInvestDto[] = [];
  dataSaveDataReportParamList: DataReportParamsDto[] = [];
  dataSaveDataReportParam: DataReportParamsDto;
  dataSaveDataInspectorInvest: DataInspectorInvestDto;
  dataSaveSeizureDeclaration: SeizureDeclarationDto;
  dataSaveInvestInspectReport: InspectionInvestigationReportDto;
  dataSavePreliminaryReport: PreliminaryReportDto;
  dataSavePreliminaryReportParamList: PreliminaryReportItemsDto[] = [];
  dataSavePreliminaryReportParam: PreliminaryReportItemsDto;


  dataSaveAssignOfficer: FuelEntityAssignOfficerDto;
  dataSaveRapidTest: FuelEntityRapidTestDto;
  dataSaveSampleCollect: SampleCollectionDto;
  dataSaveSampleCollectItems: SampleCollectionItemsDto;
  dataSaveSampleCollectItemsList: SampleCollectionItemsDto[] = [];
  dataSaveSampleSubmit: SampleSubmissionDto;
  dataSaveSampleSubmitParam: SampleSubmissionItemsDto;
  dataSaveSampleSubmitParamList: SampleSubmissionItemsDto[] = [];
  dataSaveSampleSubmitBSNumber: BSNumberSaveDto;
  dataPDFSaveComplianceStatus: PDFSaveComplianceStatusDto;
  dataSSFSaveComplianceStatus: SSFSaveComplianceStatusDto;
  dataSaveScheduleRemediation: CompliantRemediationDto;
  dataSaveNotCompliantInvoice: CompliantRemediationDto;
  dataSaveRemediation: RemediationDto;

  labList: LaboratoryDto[];
  roles: string[];
  userLoggedInID: number;
  userProfile: LoggedInUser;
  blob: Blob;
  uploadedFiles: FileList;

  attachments: any[];
  comments: any[];
  consignmentItems: any[];
  paymentFees: any[];
  configurations: any[];
  demandNotes: any[];
  checkLists: any[];
  supervisorTasks: any[];
  supervisorCharge = false;
  inspectionOfficer = false;

  public settingsSampleCollectItems = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      // id: {
      //   title: '#',
      //   type: 'string',
      //   filter: false
      // },
      productBrandName: {
        title: 'PRODUCT BRAND NAME',
        type: 'string',
        filter: false,
      },
      batchNo: {
        title: 'BATCH NO',
        type: 'string',
        filter: false,
      },
      batchSize: {
        title: 'BATCH SIZE',
        type: 'string',
        filter: false,
      },
      sampleSize: {
        title: 'SAMPLE SIZE',
        type: 'string',
        filter: false,
      },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsSampleSubmittedItems = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      // id: {
      //   title: '#',
      //   type: 'string',
      //   filter: false
      // },
      parameters: {
        title: 'PARAMETERS',
        type: 'string',
        filter: false,
      },
      laboratoryName: {
        title: 'LABORATORY NAME',
        type: 'string',
        filter: false,
      },
      // batchSize: {
      //   title: 'BATCH SIZE',
      //   type: 'string'
      // },
      // sampleSize: {
      //   title: 'SAMPLE SIZE',
      //   type: 'string'
      // },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsLabResultsParam = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
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
      // batchSize: {
      //   title: 'BATCH SIZE',
      //   type: 'string'
      // },
      // sampleSize: {
      //   title: 'SAMPLE SIZE',
      //   type: 'string'
      // },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
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
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
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
      // method: {
      //   title: 'METHOD',
      //   type: 'string',
      //   filter: false
      // },
      // batchSize: {
      //   title: 'BATCH SIZE',
      //   type: 'string'
      // },
      // sampleSize: {
      //   title: 'SAMPLE SIZE',
      //   type: 'string'
      // },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
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
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
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
      // complianceRemarks: {
      //   title: 'COMPLIANCE REMARKS',
      //   type: 'string',
      //   filter: false
      // },
      complianceStatus: {
        title: 'COMPLIANCE STATUS',
        type: 'boolean',
        filter: false,
      },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsRemarks = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        {name: 'viewSavedRemarks', title: '<i class="btn btn-sm btn-primary">View Remarks</i>'},
        // {name: 'viewPDFRecord', title: '<i class="btn btn-sm btn-primary">View</i>'}
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      processName: {
        title: 'PROCESS NAME',
        type: 'string',
        filter: false,
      },
      processBy: {
        title: 'PROCESS BY',
        type: 'string',
        filter: false,
      },
      // complianceStatus: {
      //   title: 'COMPLIANCE STATUS',
      //   type: 'boolean',
      //   filter: false
      // },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsWorkPlanFiles = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewPDFRemarks', title: '<i class="btn btn-sm btn-primary">View Remarks</i>'},
        {name: 'viewPDFRecord', title: '<i class="btn btn-sm btn-primary">View</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      documentType: {
        title: 'File NAME',
        type: 'string',
        filter: false,
      },
      fileName: {
        title: 'DOCUMENT TYPE',
        type: 'string',
        filter: false,
      },
      // complianceStatus: {
      //   title: 'COMPLIANCE STATUS',
      //   type: 'boolean',
      //   filter: false
      // },
      // sampled: {
      //   title: 'Sampled',
      //   type: 'string'
      // },
      // inspectionDate: {
      //   title: 'Inspection Date',
      //   type: 'date'
      // },
      // sampleUpdated: {
      //   title: 'Sample Updated',
      //   type: 'custom',
      //   renderComponent: ConsignmentStatusComponent
      // }
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsPreliminaryReportOfficers = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      inspectorName: {
        title: 'INSPECTOR NAME',
        type: 'string',
        filter: false,
      },
      institution: {
        title: 'INSTITUTION',
        type: 'string',
        filter: false,
      },
      designation: {
        title: 'DESIGNATION',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsPreliminaryReportOutlets = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      marketCenter: {
        title: 'MARKET CENTER',
        type: 'string',
        filter: false,
      },
      nameOutlet: {
        title: 'NAME OF OUTLET',
        type: 'string',
        filter: false,
      },
      sector: {
        title: 'SECTOR',
        type: 'string',
        filter: false,
      },
      dateVisit: {
        title: 'DATE OF VISIT',
        type: 'date',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };

  public workPlanInspection: WorkPlanInspectionDto;
  public msCounties: {name: string, code: string}[];


  constructor(
      private msService: MsService,
      // private dialog: MatDialog,
      private formBuilder: FormBuilder,
      private store$: Store<any>,
      private SpinnerService: NgxSpinnerService,
      private activatedRoute: ActivatedRoute,
      private router: Router) {
  }

  ngOnInit(): void {
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.userLoggedInID = u.id;
      this.userProfile = u;
      return this.roles = u.roles;
    });

    this.assignOfficerForm = this.formBuilder.group({
      assignedUserID: ['', Validators.required],
      remarks: null,
    });

    this.approveScheduleForm = this.formBuilder.group({
      approvalStatus: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.approvePreliminaryForm = this.formBuilder.group({
      approvalStatus: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.preliminaryRecommendationForm = this.formBuilder.group({
      surveillanceConclusions: ['', Validators.required],
      surveillanceRecommendation: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.chargeSheetForm = this.formBuilder.group({
      christianName: ['', Validators.required],
      surname: ['', Validators.required],
      sex: ['', Validators.required],
      nationality: ['', Validators.required],
      age: ['', Validators.required],
      addressDistrict: ['', Validators.required],
      addressLocation: ['', Validators.required],
      firstCount: ['', Validators.required],
      particularsOffenceOne: ['', Validators.required],
      secondCount: ['', Validators.required],
      particularsOffenceSecond: ['', Validators.required],
      dateArrest: ['', Validators.required],
      withWarrant: ['', Validators.required],
      applicationMadeSummonsSue: ['', Validators.required],
      dateApprehensionCourt: ['', Validators.required],
      bondBailAmount: ['', Validators.required],
      remandedAdjourned: ['', Validators.required],
      complainantName: ['', Validators.required],
      complainantAddress: ['', Validators.required],
      prosecutor: ['', Validators.required],
      witnesses: ['', Validators.required],
      sentence: ['', Validators.required],
      finePaid: ['', Validators.required],
      courtName: ['', Validators.required],
      courtDate: ['', Validators.required],
      // remarks: ['', Validators.required],
    });

    this.dataReportForm = this.formBuilder.group({
      referenceNumber: ['', Validators.required],
      inspectionDate: ['', Validators.required],
      inspectorName: ['', Validators.required],
      function: ['', Validators.required],
      department: ['', Validators.required],
      regionName: ['', Validators.required],
      town: ['', Validators.required],
      marketCenter: ['', Validators.required],
      outletDetails: ['', Validators.required],
      personMet: ['', Validators.required],
      summaryFindingsActionsTaken: ['', Validators.required],
      finalActionSeizedGoods: ['', Validators.required],
      // remarks: ['', Validators.required],
    });

    this.investInspectReportForm = this.formBuilder.group({
      reportReference: ['', Validators.required],
      reportTo: ['', Validators.required],
      reportThrough: ['', Validators.required],
      reportFrom: ['', Validators.required],
      reportSubject: ['', Validators.required],
      reportTitle: ['', Validators.required],
      reportDate: ['', Validators.required],
      reportRegion: ['', Validators.required],
      reportDepartment: ['', Validators.required],
      reportFunction: ['', Validators.required],
      backgroundInformation: ['', Validators.required],
      objectiveInvestigation: ['', Validators.required],
      dateInvestigationInspection: ['', Validators.required],
      kebsInspectors: null,
      methodologyEmployed: ['', Validators.required],
      conclusion: ['', Validators.required],
      recommendations: ['', Validators.required],
      statusActivity: ['', Validators.required],
      finalRemarkHod: null,
      // remarks: ['', Validators.required],
    });

    this.dataReportParamForm = this.formBuilder.group({
      typeBrandName: ['', Validators.required],
      localImport: ['', Validators.required],
      complianceInspectionParameter: ['', Validators.required],
      measurementsResults: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.investInspectReportInspectorsForm = this.formBuilder.group({
      inspectorName: ['', Validators.required],
      institution: ['', Validators.required],
      designation: ['', Validators.required],
    });

    this.seizureDeclarationForm = this.formBuilder.group({
      seizureTo: ['', Validators.required],
      seizurePremises: ['', Validators.required],
      seizureRequirementsStandards: ['', Validators.required],
      // goodsName: ['', Validators.required],
      goodsManufactureTrader: ['', Validators.required],
      goodsAddress: ['', Validators.required],
      goodsPhysical: ['', Validators.required],
      // goodsLocation: ['', Validators.required],
      goodsMarkedBranded: ['', Validators.required],
      goodsPhysicalSeal: ['', Validators.required],
      descriptionGoods: ['', Validators.required],
      goodsQuantity: ['', Validators.required],
      goodsThereforei: ['', Validators.required],
      nameInspector: ['', Validators.required],
      designationInspector: ['', Validators.required],
      dateInspector: ['', Validators.required],
      nameManufactureTrader: ['', Validators.required],
      designationManufactureTrader: ['', Validators.required],
      dateManufactureTrader: ['', Validators.required],
      nameWitness: ['', Validators.required],
      designationWitness: ['', Validators.required],
      dateWitness: ['', Validators.required],
      declarationTakenBy: ['', Validators.required],
      declarationOnThe: ['', Validators.required],
      declarationDayOf: ['', Validators.required],
      declarationMyName: ['', Validators.required],
      declarationIresideAt: ['', Validators.required],
      declarationIemployeedAs: ['', Validators.required],
      declarationIemployeedOf: ['', Validators.required],
      declarationSituatedAt: ['', Validators.required],
      declarationStateThat: ['', Validators.required],
      // declarationIdNumber: ['', Validators.required],
      // remarks: ['', Validators.required],
    });

    this.sampleCollectForm = this.formBuilder.group({
      nameManufacturerTrader: ['', Validators.required],
      addressManufacturerTrader: ['', Validators.required],
      samplingMethod: ['', Validators.required],
      reasonsCollectingSamples: ['', Validators.required],
      anyRemarks: ['', Validators.required],
      designationOfficerCollectingSample: ['', Validators.required],
      nameOfficerCollectingSample: ['', Validators.required],
      dateOfficerCollectingSample: ['', Validators.required],
      nameWitness: ['', Validators.required],
      designationWitness: ['', Validators.required],
      dateWitness: ['', Validators.required],
    });

    this.sampleCollectItemsForm = this.formBuilder.group({
      productBrandName: ['', Validators.required],
      batchNo: ['', Validators.required],
      batchSize: ['', Validators.required],
      sampleSize: ['', Validators.required],
    });

    this.sampleSubmitForm = this.formBuilder.group({
      nameProduct: ['', Validators.required],
      packaging: ['', Validators.required],
      labellingIdentification: ['', Validators.required],
      fileRefNumber: ['', Validators.required],
      referencesStandards: ['', Validators.required],
      sizeTestSample: ['', Validators.required],
      sizeRefSample: ['', Validators.required],
      condition: ['', Validators.required],
      sampleReferences: ['', Validators.required],
      sendersName: ['', Validators.required],
      designation: ['', Validators.required],
      address: ['', Validators.required],
      sendersDate: ['', Validators.required],
      receiversName: ['', Validators.required],
      testChargesKsh: ['', Validators.required],
      receiptLpoNumber: ['', Validators.required],
      invoiceNumber: ['', Validators.required],
      disposal: ['', Validators.required],
      remarks: ['', Validators.required],
      sampleCollectionNumber: ['', Validators.required],
    });


    this.sampleSubmitParamsForm = this.formBuilder.group({
      parameters: ['', Validators.required],
      laboratoryName: ['', Validators.required],
    });

    this.sampleSubmitBSNumberForm = this.formBuilder.group({
      bsNumber: ['', Validators.required],
      submittedDate: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.pdfSaveComplianceStatusForm = this.formBuilder.group({
      complianceStatus: ['', Validators.required],
      complianceRemarks: ['', Validators.required],
    });

    this.ssfSaveComplianceStatusForm = this.formBuilder.group({
      complianceStatus: ['', Validators.required],
      complianceRemarks: ['', Validators.required],
    });

    this.preliminaryReportForm = this.formBuilder.group({
      reportTo : ['', Validators.required],
      reportFrom : ['', Validators.required],
      reportSubject : ['', Validators.required],
      reportTitle : ['', Validators.required],
      reportDate : ['', Validators.required],
      surveillanceDateFrom : ['', Validators.required],
      surveillanceDateTo : ['', Validators.required],
      reportBackground : ['', Validators.required],
      kebsOfficersName : null,
      surveillanceObjective : ['', Validators.required],
      // surveillanceConclusions : ['', Validators.required],
      // surveillanceRecommendation : ['', Validators.required],
      // remarks : ['', Validators.required],
    });

    this.scheduleRemediationForm = this.formBuilder.group({
      dateOfRemediation: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.preliminaryReportParamForm = this.formBuilder.group({
      marketCenter: ['', Validators.required],
      nameOutlet: ['', Validators.required],
      sector: ['', Validators.required],
      dateVisit: ['', Validators.required],
      // numberProductsPhysicalInspected: ['', Validators.required],
      // compliancePhysicalInspection: ['', Validators.required],
    });

    this.notCompliantInvoiceForm = this.formBuilder.group({
      remarks: ['', Validators.required],
      volumeFuelRemediated: ['', Validators.required],
      subsistenceTotalNights: ['', Validators.required],
      transportAirTicket: ['', Validators.required],
      transportInkm: ['', Validators.required],
    });

    this.remediationForm = this.formBuilder.group({
      productType: ['', Validators.required],
      quantityOfFuel: ['', Validators.required],
      contaminatedFuelType: ['', Validators.required],
      applicableKenyaStandard: ['', Validators.required],
      remediationProcedure: ['', Validators.required],
      volumeOfProductContaminated: ['', Validators.required],
      volumeAdded: ['', Validators.required],
      totalVolume: ['', Validators.required],
    });

    this.remarksSavedForm = this.formBuilder.group({
      processBy: null,
      remarksDescription: null,
    });

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.selectedRefNo = rs.get('referenceNumber');
          this.selectedBatchRefNo = rs.get('batchReferenceNumber');
          this.loadData(this.selectedRefNo, this.selectedBatchRefNo);
        },
    );
  }

  loadDataToBeUsed() {
    this.msCounties = this.msService.getAllCountriesList();
    // this.msService.msCountriesListDetails().subscribe(
    //     (dataCounties: CountriesEntityDto[]) => {
    //       this.msCounties = dataCounties;
    //       console.log(dataCounties);
    //     },
    //     error => {
    //       console.log(error);
    //       this.msService.showError('AN ERROR OCCURRED');
    //     },
    // );
  }

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formPreliminaryReportForm(): any {
    return this.preliminaryReportForm.controls;
  }

  get formPreliminaryRecommendationForm(): any {
    return this.preliminaryRecommendationForm.controls;
  }

  get formPreliminaryReportParamForm(): any {
    return this.preliminaryReportParamForm.controls;
  }

  get formApproveScheduleForm(): any {
    return this.approveScheduleForm.controls;
  }

  get formApprovePreliminaryForm(): any {
    return this.approvePreliminaryForm.controls;
  }

  get formChargeSheetForm(): any {
    return this.chargeSheetForm.controls;
  }

  get formInvestInspectReportForm(): any {
    return this.investInspectReportForm.controls;
  }

  get formInvestInspectReportInspectorsForm(): any {
    return this.investInspectReportInspectorsForm.controls;
  }

  get formDataReportForm(): any {
    return this.dataReportForm.controls;
  }

  get formDataReportParamForm(): any {
    return this.dataReportParamForm.controls;
  }

  get formSeizureDeclarationForm(): any {
    return this.seizureDeclarationForm.controls;
  }

  get formPdfSaveComplianceStatusForm(): any {
    return this.pdfSaveComplianceStatusForm.controls;
  }

  get formSSFSaveComplianceStatusForm(): any {
    return this.ssfSaveComplianceStatusForm.controls;
  }

  get formSampleCollectForm(): any {
    return this.sampleCollectForm.controls;
  }
  get formSampleCollectItemsForm(): any {
    return this.sampleCollectItemsForm.controls;
  }

  get formSampleSubmitForm(): any {
    return this.sampleSubmitForm.controls;
  }

  get formSampleSubmitParamsForm(): any {
    return this.sampleSubmitParamsForm.controls;
  }

  get formSampleSubmitBSNumberForm(): any {
    return this.sampleSubmitBSNumberForm.controls;
  }

  get formScheduleRemediationForm(): any {
    return this.scheduleRemediationForm.controls;
  }

  get formNotCompliantInvoiceForm(): any {
    return this.notCompliantInvoiceForm.controls;
  }

  get formRemediationForm(): any {
    return this.remediationForm.controls;
  }

  private loadData(referenceNumber: string, batchReferenceNumber: string ): any {
    this.SpinnerService.show();
    this.msService.msWorkPlanScheduleDetails(batchReferenceNumber, referenceNumber).subscribe(
        (data) => {
          this.workPlanInspection = data;
          this.loadDataToBeUsed();
          this.SpinnerService.hide();
          console.log(data);
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );

  }

  openModalAddDetails(divVal: string): void {
    const arrHead = ['approveSchedule', 'uploadFiles', 'chargeSheetDetails', 'dataReportDetails', 'seizureDeclarationDetails',
       'addBsNumber', 'approvePreliminaryHOF', 'approvePreliminaryHOD', 'addPreliminaryRecommendation',
      'ssfAddComplianceStatus'];

    // tslint:disable-next-line:max-line-length
    const arrHeadSave = ['APPROVE/REJECT SCHEDULED WORK-PLAN', 'ATTACH FILE(S) BELOW', 'ADD CHARGE SHEET DETAILS', 'ADD DATA REPORT DETAILS', 'ADD SEIZURE DECLARATION DETAILS',
      'ADD BS NUMBER', 'APPROVE/REJECT PRELIMINARY REPORT', 'APPROVE/REJECT PRELIMINARY REPORT', 'ADD RECOMMENDATION DETAILS',
      'ADD SSF LAB RESULTS COMPLIANCE STATUS'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }
    this.currDiv = divVal;
  }


  viewLabResultsPdfFile(fileName: string, bsNumber: string, applicationType: string): void {
    this.SpinnerService.show();
    this.msService.loadFileDetailsLabResultsPDF(fileName, bsNumber).subscribe(
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
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  viewSampleCollectPdfFile(sampleCollectionID: string, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.msService.loadSampleCollectionDetailsPDF(sampleCollectionID).subscribe(
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
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  viewRemediationInvoicePdfFile(fuelInspectionId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.msService.loadRemediationInvoiceDetailsPDF(String(fuelInspectionId)).subscribe(
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
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  onClickSaveApproveScheduleResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
      this.msService.msWorkPlanScheduleDetailsApprove(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApproveSchedule,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('WORK-PLAN DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveApprovePreliminaryHOF(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsApprovePreliminaryHOF(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('PRELIMINARY STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveApprovePreliminaryHOD(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsApprovePreliminaryHOD(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('PRELIMINARY STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveFinalPreliminary(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveFinalPreliminary = {...this.dataSaveFinalPreliminary, ...this.preliminaryRecommendationForm.value};
      this.msService.msWorkPlanScheduleDetailsFinalPreliminary(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveFinalPreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL PRELIMINARY DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR HAS OCCURRED');
          },
      );
    }
  }

  onClickStartOnsiteActivities() {
    // if (valid) {
      this.SpinnerService.show();
      this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
      this.msService.msWorkPlanScheduleDetailsStartOnsiteActivities(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('WORK-PLAN DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    // }
  }

  onClickEndOnsiteActivities() {
    // if (valid) {
      this.SpinnerService.show();
      this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
      this.msService.msWorkPlanScheduleDetailsEndOnsiteActivities(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('WORK-PLAN ON-SITE DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    // }
  }


  onClickSaveAssignOfficerBatch(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
      this.msService.msFuelInspectionScheduledAssignOfficer(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveAssignOfficer).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('OFFICER ASSIGNED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveFilesResults(docTypeName: string) {
    if (this.uploadedFiles.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFiles;
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      formData.append('docTypeName', docTypeName);
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.msService.saveWorkPlanFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FILE(S) UPLOADED SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    } else {
      this.msService.showError('NO FILE IS UPLOADED FOR SAVING');
    }
  }

  onClickSaveSampleCollected() {
    this.submitted = true;
    if (this.sampleCollectForm.invalid) {
      return;
    }

    if (this.sampleCollectForm.valid && this.dataSaveSampleCollectItemsList.length !== 0) {
      this.SpinnerService.show();
      this.dataSaveSampleCollect = {...this.dataSaveSampleCollect, ...this.sampleCollectForm.value};
      this.dataSaveSampleCollect.productsList = this.dataSaveSampleCollectItemsList;

      this.msService.msWorkPlanInspectionScheduledAddSampleCollection(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveSampleCollect).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('SAMPLE COLLECTION SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    } else {
      this.msService.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
    }
  }

  onClickSaveSampleSubmitted() {
    this.submitted = true;
    if (this.sampleSubmitForm.invalid) {
      return;
    }
    if (this.sampleSubmitForm.valid && this.dataSaveSampleSubmitParamList.length !== 0) {
      this.SpinnerService.show();
      this.dataSaveSampleSubmit = {...this.dataSaveSampleSubmit, ...this.sampleSubmitForm.value};
      this.dataSaveSampleSubmit.parametersList = this.dataSaveSampleSubmitParamList;

      this.msService.msWorkPlanInspectionScheduledAddSampleSubmission(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveSampleSubmit).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('SAMPLE SUBMISSION SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    } else {
      this.msService.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
    }
  }

  onClickSaveBSNumber(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveSampleSubmitBSNumber = {...this.dataSaveSampleSubmitBSNumber, ...this.sampleSubmitBSNumberForm.value};
      this.msService.msWorkPlanInspectionScheduledAddSampleSubmissionBSNumber(this.workPlanInspection.batchDetails.referenceNumber, this.workPlanInspection.referenceNumber, this.dataSaveSampleSubmitBSNumber).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('BS NUMBER ADDED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSavePDFSelected(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataPDFSaveComplianceStatus = {...this.dataPDFSaveComplianceStatus, ...this.pdfSaveComplianceStatusForm.value};
      this.dataPDFSaveComplianceStatus.ssfID = this.workPlanInspection.sampleLabResults.ssfResultsList.sffId;
      this.dataPDFSaveComplianceStatus.bsNumber = this.workPlanInspection.sampleLabResults.ssfResultsList.bsNumber;
      this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      if (this.workPlanInspection.sampleLabResults.savedPDFFiles.length === 0) {
        this.msService.msWorkPlanInspectionScheduledSavePDFLIMS(this.workPlanInspection.batchDetails.referenceNumber,
            this.workPlanInspection.referenceNumber, this.dataPDFSaveComplianceStatus).subscribe(
            (data: any) => {
              this.workPlanInspection = data;
              console.log(data);
              this.SpinnerService.hide();
              this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();
              console.log(error);
              this.msService.showError('AN ERROR OCCURRED');
            },
        );
      } else {
        for (const savedPdf of this.workPlanInspection.sampleLabResults.savedPDFFiles) {
          if (savedPdf.pdfName !== this.selectedPDFFileName) {
            this.msService.msWorkPlanInspectionScheduledSavePDFLIMS(this.workPlanInspection.batchDetails.referenceNumber,
                this.workPlanInspection.referenceNumber, this.dataPDFSaveComplianceStatus).subscribe(
                (data: any) => {
                  this.workPlanInspection = data;
                  console.log(data);
                  this.SpinnerService.hide();
                  this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY');
                },
                error => {
                  this.SpinnerService.hide();
                  console.log(error);
                  this.msService.showError('AN ERROR OCCURRED');
                },
            );
          } else {
            this.SpinnerService.hide();
            this.msService.showError('The Pdf selected With Name ' + this.selectedPDFFileName + ' Already Saved');
          }
        }
      }
    }
  }

  onClickSaveSSFLabResultsComplianceStatus(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSSFSaveComplianceStatus = {...this.dataSSFSaveComplianceStatus, ...this.ssfSaveComplianceStatusForm.value};
      this.dataSSFSaveComplianceStatus.ssfID = this.workPlanInspection.sampleLabResults.ssfResultsList.sffId;
      this.dataSSFSaveComplianceStatus.bsNumber = this.workPlanInspection.sampleLabResults.ssfResultsList.bsNumber;
      // this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      this.msService.msWorkPlanInspectionScheduledSaveSSFComplianceStatus(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSSFSaveComplianceStatus,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('LAB RESULTS COMPLIANCE STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }


  onClickSaveScheduleRemediation(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveScheduleRemediation = {...this.dataSaveScheduleRemediation, ...this.scheduleRemediationForm.value};
      this.msService.msFuelInspectionScheduledRemediation(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveScheduleRemediation,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('REMEDIATION SCHEDULE SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveNotCompliantInvoice(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveNotCompliantInvoice = {...this.dataSaveNotCompliantInvoice, ...this.notCompliantInvoiceForm.value};
      this.msService.msFuelInspectionNotCompliantRemediationInvoice(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveNotCompliantInvoice).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('REMEDIATION INVOICE GENERATED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveRemediationForm(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveRemediation = {...this.dataSaveRemediation, ...this.remediationForm.value};
      this.msService.msFuelInspectionRemediation(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveRemediation).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('REMEDIATION DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveEndRemediation() {
    // if (valid) {
    this.SpinnerService.show();
    // this.dataSaveRemediation = {...this.dataSaveRemediation, ...this.remediationForm.value};
    // tslint:disable-next-line:max-line-length
    this.msService.msFuelInspectionEnd(this.workPlanInspection.batchDetails.referenceNumber, this.workPlanInspection.referenceNumber).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('FUEL INSPECTION ENDED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }


  goBack() {
    console.log('TEST 101' + this.workPlanInspection.batchDetails.referenceNumber);
    this.router.navigate([`/workPlan`, this.workPlanInspection.batchDetails.referenceNumber]);
  }

  viewWorkPlanFileSaved(data: ComplaintsFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
  }

  viewPdfFile(pdfId: string, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.msService.loadFileDetailsPDF(pdfId).subscribe(
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
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }


  viewLIMSPDFRecord(data: LIMSFilesFoundDto, bsNumber: string) {
    console.log('TEST 101 REF NO VIEW: ' + data.fileName);
    this.viewLabResultsPdfFile(String(data.fileName), bsNumber, 'application/pdf');
    // this.router.navigate([`/epra/workPlanInspection/details/`,data.referenceNumber]);
  }

  viewLIMSPDFSaved(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);

    this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/workPlanInspection/details/`,data.referenceNumber]);
  }

  viewLIMSPDFSavedRemarks(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);
    this.selectedPDFFileName = data.pdfName;
    this.currDivLabel = `COMPLIANCE STATUS AND REMARKS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'viewPdfSaveCompliance';
    this.pdfSaveComplianceStatusForm.patchValue(data);

    window.$('#myModal1').modal('show');
    // this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/workPlanInspection/details/`,data.referenceNumber]);
  }

  viewSavedRemarks(data: MSRemarksDto) {
    this.currDivLabel = `REMARKS FOR ${data.processName}`;
    this.currDiv = 'viewSavedRemarks';
    this.remarksSavedForm.patchValue(data);

    window.$('#myModal1').modal('show');
    // this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/workPlanInspection/details/`,data.referenceNumber]);
  }

  saveLIMSPDFRecord(data: LIMSFilesFoundDto) {
    console.log('TEST 101 REF NO SAVE: ' + data.fileName);
    this.selectedPDFFileName = data.fileName;
    this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'pdfSaveCompliance';

    window.$('#myModal1').modal('show');
    // $('#myModal1').modal('show');
    // this.openModalAddDetails('assignOfficer')
    // this.router.navigate([`/epra/workPlanInspection/details/`,data.referenceNumber]);
  }

  public onCustomLIMSPDFAction(event: any, bsNumber: string): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewLIMSPDFRecord(event.data, bsNumber);
        break;
      case 'saveRecord':
        this.saveLIMSPDFRecord(event.data);
        break;
    }
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

  public onCustomRemarksViewAction(event: any): void {
    switch (event.action) {
      case 'viewSavedRemarks':
        this.viewSavedRemarks(event.data);
        break;
    }
  }

  public onCustomWorkPlanFileViewAction(event: any): void {
    switch (event.action) {
      case 'viewPDFRecord':
        this.viewWorkPlanFileSaved(event.data);
        break;
    }
  }

  onClickSaveChargeSheet() {
    this.submitted = true;
    if (this.chargeSheetForm.invalid) {
      return;
    }
    if (this.chargeSheetForm.valid) {
      this.SpinnerService.show();
      this.dataSaveChargeSheet = {...this.dataSaveChargeSheet, ...this.chargeSheetForm.value};
      this.msService.msWorkPlanScheduleSaveChargeSheet(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveChargeSheet,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('CHARGE SHEET DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }


  onClickAddDataReportParam() {
    this.dataSaveDataReportParam = this.dataReportParamForm.value;
    this.dataSaveDataReportParamList.push(this.dataSaveDataReportParam);
    this.dataReportParamForm?.get('typeBrandName')?.reset();
    this.dataReportParamForm?.get('localImport')?.reset();
    this.dataReportParamForm?.get('complianceInspectionParameter')?.reset();
    this.dataReportParamForm?.get('measurementsResults')?.reset();
    this.dataReportParamForm?.get('remarks')?.reset();
    // this.sta10FormA.reset();
  }

  onClickAddDataInspectorOfficer() {
    this.dataSaveDataInspectorInvest = this.investInspectReportInspectorsForm.value;
    this.dataSaveDataInspectorInvestList.push(this.dataSaveDataInspectorInvest);
    this.investInspectReportInspectorsForm?.get('inspectorName')?.reset();
    this.investInspectReportInspectorsForm?.get('institution')?.reset();
    this.investInspectReportInspectorsForm?.get('designation')?.reset();
    // this.sta10FormA.reset();
  }

  onClickAddPreliminaryReportParam() {
    this.dataSavePreliminaryReportParam = this.preliminaryReportParamForm.value;
    this.dataSavePreliminaryReportParamList.push(this.dataSavePreliminaryReportParam);
    this.preliminaryReportParamForm?.get('marketCenter')?.reset();
    this.preliminaryReportParamForm?.get('nameOutlet')?.reset();
    this.preliminaryReportParamForm?.get('sector')?.reset();
    this.preliminaryReportParamForm?.get('dateVisit')?.reset();
    // this.sta10FormA.reset();
  }

  onClickAddDataSampleCollectItems() {
    this.dataSaveSampleCollectItems = this.sampleCollectItemsForm.value;
    this.dataSaveSampleCollectItemsList.push(this.dataSaveSampleCollectItems);
    this.sampleCollectItemsForm?.get('productBrandName')?.reset();
    this.sampleCollectItemsForm?.get('batchNo')?.reset();
    this.sampleCollectItemsForm?.get('batchSize')?.reset();
    this.sampleCollectItemsForm?.get('sampleSize')?.reset();

  }

  onClickAddDataSampleSubmitParams() {
    this.dataSaveSampleSubmitParam = this.sampleSubmitParamsForm.value;
    this.dataSaveSampleSubmitParamList.push(this.dataSaveSampleSubmitParam);
    this.sampleSubmitParamsForm?.get('parameters')?.reset();
    this.sampleSubmitParamsForm?.get('laboratoryName')?.reset();
  }

  // Remove Form repeater values
  removeDataReportParam(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveDataReportParamList.splice(index, 1);
    } else {
      this.dataSaveDataReportParamList.splice(index, index);
    }
  }

  removePreliminaryReportParam(index) {
    console.log(index);
    if (index === 0) {
      this.dataSavePreliminaryReportParamList.splice(index, 1);
    } else {
      this.dataSavePreliminaryReportParamList.splice(index, index);
    }
  }

  // Remove Form repeater values
  removeDataSampleSubmitParam(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveSampleSubmitParamList.splice(index, 1);
    } else {
      this.dataSaveSampleSubmitParamList.splice(index, index);
    }
  }

  // Remove Form repeater values
  removeDataInvestInspectReportInspectors(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveDataInspectorInvestList.splice(index, 1);
    } else {
      this.dataSaveDataInspectorInvestList.splice(index, index);
    }
  }
  // Remove Form repeater values
  removeDataSampleCollectItems(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveSampleCollectItemsList.splice(index, 1);
    } else {
      this.dataSaveSampleCollectItemsList.splice(index, index);
    }
  }

  onClickSaveDataReport() {
    this.submitted = true;
    if (this.dataReportForm.invalid) {
      return;
    }
    if (this.dataReportForm.valid && this.dataSaveDataReportParamList.length !== 0) {
      this.SpinnerService.show();
      this.dataSaveDataReport = {...this.dataSaveDataReport, ...this.dataReportForm.value};
      this.dataSaveDataReport.productsList = this.dataSaveDataReportParamList;
      this.msService.msWorkPlanScheduleSaveDataReport(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveDataReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('DATA REPORT DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }

  onClickSaveSeizureDeclaration() {
    this.submitted = true;
    if (this.seizureDeclarationForm.invalid) {
      return;
    }
    if (this.seizureDeclarationForm.valid) {
      this.SpinnerService.show();
      this.dataSaveSeizureDeclaration = {...this.dataSaveSeizureDeclaration, ...this.seizureDeclarationForm.value};
      this.msService.msWorkPlanScheduleSaveSeizureDeclaration(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveSeizureDeclaration,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('SEIZURE AND DECLARATION DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }

  onClickSaveInvestInspectReport() {
    this.submitted = true;

    if (this.investInspectReportForm.valid) {
      this.SpinnerService.show();
      this.dataSaveInvestInspectReport = {...this.dataSaveInvestInspectReport, ...this.investInspectReportForm.value};
      this.dataSaveInvestInspectReport.kebsInspectors = this.dataSaveDataInspectorInvestList;
      this.msService.msWorkPlanScheduleSaveInvestInspectReport(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveInvestInspectReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('INSPECTION AND INVESTIGATION REPORT DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    } else if (this.investInspectReportForm.invalid) {
      this.msService.showError('KINDLY FILL IN THE FIELDS REQUIRED');
    }
  }

  onClickSavePreliminaryReport() {
    this.submitted = true;

    if (this.preliminaryReportForm.valid && this.dataSavePreliminaryReportParamList.length !== 0) {
      this.SpinnerService.show();
      this.dataSavePreliminaryReport = {...this.dataSavePreliminaryReport, ...this.preliminaryReportForm.value};
      this.dataSavePreliminaryReport.kebsOfficersName = this.dataSaveDataInspectorInvestList;
      this.dataSavePreliminaryReport.parametersList = this.dataSavePreliminaryReportParamList;
      this.msService.msWorkPlanScheduleSavePreliminaryReport(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSavePreliminaryReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('PRELIMINARY REPORT DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    } else if (this.preliminaryReportForm.invalid) {
      this.msService.showError('KINDLY FILL IN THE FIELDS AND OUTLET DETAILS REQUIRED');
    }
  }
}