import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  ApprovalDto, BSNumberDto,
  BSNumberSaveDto,
  ChargeSheetDto,
  ComplaintAssignDto, ComplaintsFilesFoundDto,
  CompliantRemediationDto,
  DataInspectorInvestDto,
  DataReportDto,
  DataReportParamsDto,
  DestructionNotificationDto, FieldReportAdditionalInfo, FieldReportBackDto,
  FuelEntityRapidTestDto,
  InspectionInvestigationReportDto, KebsStandardsDto,
  LaboratoryDto, LaboratoryEntityDto, LIMSFilesFoundDto, MsBroadProductCategory,
  MsDepartment,
  MsDivisionDetails, MsProducts, MsProductSubcategory,
  MsRecommendationDto, MSRemarksDto, MSSSFLabResultsDto, MSSSFPDFListDetailsDto,
  MsStandardProductCategory,
  PDFSaveComplianceStatusDto, PermitUcrSearch, PredefinedResourcesRequired,
  PreliminaryReportDto,
  PreliminaryReportFinal,
  PreliminaryReportItemsDto, RecommendationDto,
  RemediationDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto,
  SeizureDeclarationDto, SeizureDto, SeizureListDto,
  SSFSaveComplianceStatusDto, SSFSendingComplianceStatus, WorkPlanCountyTownDto,
  WorkPlanEntityDto,
  WorkPlanFeedBackDto, WorkPlanFilesFoundDto,
  WorkPlanFinalRecommendationDto,
  WorkPlanInspectionDto, WorkPlanProductDto,
  WorkPlanScheduleApprovalDto, WorkPlanScheduleOnsiteDto,
} from '../../../../core/store/data/ms/ms.model';
import {
  County,
  CountyService,
  loadCountyId,
  LoggedInUser,
  selectCountyIdData,
  selectUserInfo,
  Town,
  TownService,
} from '../../../../core/store';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {Store} from '@ngrx/store';
import {NgxSpinnerService} from 'ngx-spinner';
import {ActivatedRoute, Router} from '@angular/router';
import {
  BroadProductCategory,
  ProductCategories,
  Products,
  ProductSubcategory, RegionsEntityDto,
  StandardProductCategory,
} from '../../../../core/store/data/master/master.model';
import {Observable, throwError} from 'rxjs';


interface Post {
  startDate: Date;
  endDate: Date;
}

@Component({
  selector: 'app-work-plan-details',
  templateUrl: './work-plan-details.component.html',
  styleUrls: ['./work-plan-details.component.css'],
})
export class WorkPlanDetailsComponent implements OnInit {
  @ViewChild('demoForm') myForm;
  @ViewChild('closebutton') closebutton;
  @ViewChild('standardsInput') standardsInput: ElementRef;

  // @ViewChild('selectList', { static: false }) selectList: ElementRef;
  selectedDataSheet: DataReportDto;
  active: Number = 0;
  selectedFile: File;
  selectedRefNo: string;
  totalComplianceValue: Number = 0;
  selectedBatchRefNo: string;
  selectedPDFFileName: string;
  currDiv!: string;
  workPlanProductRefNo: string;
  currDivLabel!: string;
  submitted = false;
  addLabParamStatus = true;
  addProductsStatus = true;
  addSeizureProductsStatus = true;
  standardsArray = [];
  disableDivision = true;
  isImport = 0;
  defaultPageSize = 20;


  dataSaveWorkPlanCounties: WorkPlanCountyTownDto;
  dataSaveWorkPlanCountiesList: WorkPlanCountyTownDto[] = [];
  latestProgressReport: InspectionInvestigationReportDto;
  selectedSFFDetails: SampleSubmissionDto;
  selectedSeizedDetails: SeizureListDto;
  selectedPreliminaryReportDetails: InspectionInvestigationReportDto;
  selectedDataReportDetails: DataReportDto;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  totalCount = 12;

  totalCompliantValue = 0;
  averageCompliance: number = 0;

  addResourceRequiredForm!: FormGroup;

  addNewScheduleForm!: FormGroup;
  approveScheduleForm!: FormGroup;
  approvePreliminaryForm!: FormGroup;
  finalRemarkHODForm!: FormGroup;
  investInspectReportForm!: FormGroup;
  finalRecommendationDetailsForm!: FormGroup;
  finalRecommendationForm!: FormGroup;
  preliminaryRecommendationForm!: FormGroup;
  chargeSheetForm!: FormGroup;
  dataReportForm!: FormGroup;
  dataReportParamForm!: FormGroup;
  seizureDeclarationForm!: FormGroup;
  seizureForm!: FormGroup;
  fieldReportAdditionalInfortForm!: FormGroup;
  preliminaryReportForm!: FormGroup;
  preliminaryReportFinalForm!: FormGroup;
  preliminaryReportParamForm!: FormGroup;
  investInspectReportInspectorsForm!: FormGroup;
  actionOnSiezedGoodsForm!: FormGroup;
  startOnsiteActivitiesForm!: FormGroup;
  addCountyTownForm!: FormGroup;
  clientEmailNotificationForm!: FormGroup;
  ssfClientEmailNotificationForm!: FormGroup;
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
  verificationPermitForm!: FormGroup;
  scheduleRemediationForm!: FormGroup;
  notCompliantInvoiceForm!: FormGroup;
  remediationForm!: FormGroup;
  dataSaveApproveSchedule: WorkPlanScheduleApprovalDto;
  dataSaveApprovePreliminary: ApprovalDto;
  dataSaveFinalRemarks: WorkPlanFeedBackDto;
  dataSaveFinalReport: PreliminaryReportFinal;
  dataSaveChargeSheet: ChargeSheetDto;
  dataSaveDataReport: DataReportDto;
  dataSaveStartOnsiteActivities: WorkPlanScheduleOnsiteDto;
  dataSaveFieldReportAdditional: FieldReportAdditionalInfo;
  dataSaveActionOnSiezedGoods: FieldReportBackDto;
  dataSaveActionOnSiezedGoodsList: FieldReportBackDto[] = [];
  dataSaveDataInspectorInvestList: DataInspectorInvestDto[] = [];
  dataSaveBsNumber: string[] = [];
  dataSaveDataReportParamList: DataReportParamsDto[] = [];
  dataSaveDataReportUploadsList: WorkPlanFilesFoundDto[] = [];
  dataSaveDataReportParam: DataReportParamsDto;
  dataSaveDataInspectorInvest: DataInspectorInvestDto;
  dataSaveSeizure: SeizureListDto;
  dataSaveSeizureDeclaration: SeizureDto;
  dataSaveSeizureDeclarationList: SeizureDto[] = [];
  dataSaveInvestInspectReport: InspectionInvestigationReportDto;
  dataSavePreliminaryReport: PreliminaryReportDto | undefined;
  dataSavePreliminaryReportParamList: PreliminaryReportItemsDto[] = [];
  dataSavePreliminaryReportParam: PreliminaryReportItemsDto;
  dataSaveFinalRecommendation: WorkPlanFinalRecommendationDto;
  dataSaveDestructionNotification: DestructionNotificationDto;
  laboratories: LaboratoryDto[] = [];
  standards: KebsStandardsDto[] = [];

  predefinedResourcesRequired!: PredefinedResourcesRequired[];
  dataSaveResourcesRequired: PredefinedResourcesRequired;
  dataSaveResourcesRequiredList: PredefinedResourcesRequired[] = [];
  dataSaveFinalRecommendationDetails: RecommendationDto;
  dataSaveFinalRecommendationList: RecommendationDto[] = [];


  dataSaveAssignOfficer: ComplaintAssignDto;
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
  dataSSFSendComplianceStatus: SSFSendingComplianceStatus;
  dataSaveScheduleRemediation: CompliantRemediationDto;
  dataSaveNotCompliantInvoice: CompliantRemediationDto;
  dataSaveRemediation: RemediationDto;
  dataSaveWorkPlan: WorkPlanEntityDto;

  msDepartments: MsDepartment[];
  msDivisions: MsDivisionDetails[];
  standardProductCategory!: StandardProductCategory[];
  productCategories!: ProductCategories[];
  broadProductCategory!: BroadProductCategory[];
  products!: Products[];
  productSubcategory!: ProductSubcategory[];
  selectedRecommendationID: number;
  selectedRecommendationName: string;
  standardProductCategorySelected: 0;
  productCategoriesSelected: 0;
  broadProductCategorySelected: 0;
  productsSelected: 0;
  productSubcategorySelected: 0;
  departmentSelected: 0;
  labList: LaboratoryDto[];
  roles: string[];
  userLoggedInID: number;
  userProfile: LoggedInUser;
  blob: Blob;
  uploadedFilesDataReport: FileList;
  arrayOfUploadedDataReportFiles: File[] = [];
  uploadedFilesOnly: FileList;
  uploadedSuccessfulAppealFiles: FileList;
  uploadedAppealFiles: FileList;
  uploadedFilesDestination: FileList;
  uploadDestructionReportFiles: FileList;
  uploadedFiles: FileList;
  uploadedFilesSeizedGoods: FileList;
  fileToUpload: File | null = null;
  resetUploadedFiles: FileList;
  selectedCounty = 0;
  selectedRegion = 0;
  selectedTown = 0;
  selectedRegionName: string;
  selectedTownName: string;
  selectedCountyName: string;

  county$: Observable<County[]>;
  town$: Observable<Town[]>;

  attachments: any[];
  recommendationList: MsRecommendationDto[] = [];
  comments: any[];
  consignmentItems: any[];
  paymentFees: any[];
  configurations: any[];
  demandNotes: any[];
  checkLists: any[];
  supervisorTasks: any[];
  supervisorCharge = false;
  inspectionOfficer = false;

  selectedLabResults: MSSSFLabResultsDto;
  selectedProductRecommendation: WorkPlanProductDto;
  public scfParamSelected: number;
  public ssfSelectedID: number;
  public scfParamSelectedName: string;
  public selectedSSFDetails: SampleSubmissionDto;
  public ssfCountBSNumber: number;
  public scfCountSSF: number;
  public productsDestructionRecommendationDone = 0;
  public productsDestructionRecommendation = 0;
  public productsCountRecommendationHOD = 0;
  public productsCountRecommendationDirector = 0;


  public settingsResourceRequierd = {
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
      resourceName: {
        title: 'RESOURCE NAME',
        type: 'string',
        filter: false,
      },
      // batchNo: {
      //   title: 'BATCH NO',
      //   type: 'string',
      //   filter: false,
      // },
      // batchSize: {
      //   title: 'BATCH SIZE',
      //   type: 'string',
      //   filter: false,
      // },
      // sampleSize: {
      //   title: 'SAMPLE SIZE',
      //   type: 'string',
      //   filter: false,
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
  public settingsSeizedProducts = {
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
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">VIEW RECORD</i>'},
        {name: 'viewUpload', title: '<i class="btn btn-sm btn-primary">VIEW UPLOAD</i>'},
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
      marketTownCenter: {
        title: 'Market Town Center',
        type: 'string',
        filter: false,
      },
      nameOfOutlet: {
        title: 'Name Of Outlet',
        type: 'string',
        filter: false,
      },

      nameSeizingOfficer: {
        title: 'Name Seizing Officer',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsPreliminaryReport = {
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
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">VIEW RECORD</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      reportReference: {
        title: 'REPORT REFERENCE',
        type: 'string',
        filter: false,
      },
      reportTo: {
        title: 'REPORT TO',
        type: 'string',
        filter: false,
      },
      reportFrom: {
        title: 'REPORT FROM',
        type: 'string',
        filter: false,
      },
      reportSubject: {
        title: 'REPORT SUBJECT',
        type: 'string',
        filter: false,
      },
      version: {
        title: 'VERSION',
        type: 'number',
        filter: false,
        sort: true,
        sortDirection: 'desc'
      },
      createdBy: {
        title: 'CREATED BY',
        type: 'string',
        filter: false,
      },
      // createdOn: {
      //   title: 'CREATED ON',
      //   type: 'date',
      //   filter: false,
      // },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsSeizedProductsUpdate = {
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
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">VIEW RECORD</i>'},
        {name: 'viewUpload', title: '<i class="btn btn-sm btn-primary">VIEW UPLOAD</i>'},
        {name: 'updateRecords', title: '<i class="btn btn-sm btn-primary">UPDATE</i>'},
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
      marketTownCenter: {
        title: 'Market Town Center',
        type: 'string',
        filter: false,
      },
      nameOfOutlet: {
        title: 'Name Of Outlet',
        type: 'string',
        filter: false,
      },
      nameSeizingOfficer: {
        title: 'Name Seizing Officer',
        type: 'string',
        filter: false,
      },
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
    },
    pager: {
      display: true,
      perPage: 10,
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
  public settingsFinalReportFiles = {
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
      versionNumber: {
        title: 'VERSION NUMBER',
        type: 'string',
        filter: false,
        sort: true,
        sortDirection: 'desc'
      },
      createdBy: {
        title: 'CREATED BY',
        type: 'string',
        filter: false,
      },
      // createdOn: {
      //   title: 'CREATED ON',
      //   type: 'date',
      //   filter: false,
      // },
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
      // institution: {
      //   title: 'INSTITUTION',
      //   type: 'string',
      //   filter: false,
      // },
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
  public settingsDataReportParams = {
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
        {name: 'viewSavedDataReportRemarks', title: '<i class="btn btn-sm btn-primary">View Remarks</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      typeBrandName: {
        title: 'TYPE BRAND NAME',
        type: 'string',
        filter: false,
      },
      localImport: {
        title: 'LOCAL/IMPORT',
        type: 'string',
        filter: false,
      },
      complianceInspectionParameter: {
        title: 'COMPLIANCE (%Score)',
        type: 'string',
        filter: false,
      },
      measurementsResults: {
        title: 'MEASUREMENTS RESULTS',
        type: 'string',
        filter: false,
      },
      // dateVisit: {
      //   title: 'REMARKS',
      //   type: 'date',
      //   filter: false,
      // },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };

  public settingsDataReport = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        {name: 'viewRecord', title: '<i  class="btn btn-sm btn-primary">VIEW DATA REPORT DETAILS</i>'},
        // {name: 'viewUploads', title: '<i  class="btn btn-sm btn-primary">VIEW UPLOADS DETAILS</i>'},
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
      referenceNumber: {
        title: 'FORM REFERENCE NO',
        type: 'string',
        filter: false,
      },
      inspectorName : {
        title: 'NAME OF INSPECTOR',
        type: 'string',
        filter: false,
      },
      outletName: {
        title: 'OUTLET NAME',
        type: 'string',
        filter: false,
      },
      inspectionDate: {
        title: 'DATE OF INSPECTION',
        type: 'date',
        filter: false,
      },
      totalComplianceScore: {
        title: 'Total Compliance Score %',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsDataReportUpdate = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        {name: 'viewRecord', title: '<i  class="btn btn-sm btn-primary">VIEW DATA REPORT DETAILS</i>'},
        {name: 'updateRecord', title: '<i  class="btn btn-sm btn-primary">UPDATE DATA REPORT DETAILS</i>'},
        // {name: 'viewUploads', title: '<i  class="btn btn-sm btn-primary">VIEW UPLOADS DETAILS</i>'},
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
      referenceNumber: {
        title: 'FORM REFERENCE NO',
        type: 'string',
        filter: false,
      },
      inspectorName : {
        title: 'NAME OF INSPECTOR',
        type: 'string',
        filter: false,
      },
      outletName: {
        title: 'OUTLET NAME',
        type: 'string',
        filter: false,
      },
      inspectionDate: {
        title: 'DATE OF INSPECTION',
        type: 'date',
        filter: false,
      },
      totalComplianceScore: {
        title: 'Total Compliance Score %',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };

  public settingsSampleSubmitted = {
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
        {name: 'viewRecord', title: '<i  class="btn btn-sm btn-primary">VIEW SSF DETAILS</i>'},
        // {name: 'addBSNumber', title: '<i class="btn btn-sm btn-primary">ADD BS NUMBER</i>'},
        {name: 'viewLabResults', title: '<i class="btn btn-sm btn-primary">VIEW LAB RESULTS</i>'},
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
      fileRefNumber: {
        title: 'FILE REF NUMBER',
        type: 'string',
        filter: false,
      },
      nameProduct: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      lbIdTradeMark: {
        title: 'BRAND/TRADEMARK',
        type: 'string',
        filter: false,
      },
      // sampleReferences: {
      //   title: 'SAMPLE REF',
      //   type: 'string',
      //   filter: false,
      // },
      // disposal: {
      //   title: 'DISPOSAL',
      //   type: 'string',
      //   filter: false,
      // },
      bsNumber: {
        title: 'BS NUMBER',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsSampleSubmittedAddBsNumber = {
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
        {name: 'viewRecord', title: '<i  class="btn btn-sm btn-primary">VIEW SSF DETAILS</i>'},
        {name: 'addBSNumber', title: '<i class="btn btn-sm btn-primary">ADD BS NUMBER</i>'},
        // {name: 'viewLabResults', title: '<i class="btn btn-sm btn-primary">VIEW LAB RESULTS</i>'},
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
      fileRefNumber: {
        title: 'FILE REF NUMBER',
        type: 'string',
        filter: false,
      },
      nameProduct: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      lbIdTradeMark: {
        title: 'BRAND/TRADEMARK',
        type: 'string',
        filter: false,
      },
      // sampleReferences: {
      //   title: 'SAMPLE REF',
      //   type: 'string',
      //   filter: false,
      // },
      // disposal: {
      //   title: 'DISPOSAL',
      //   type: 'string',
      //   filter: false,
      // },
      bsNumber: {
        title: 'BS NUMBER',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsSampleSubmittedUpdate = {
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
        {name: 'updateRecord', title: '<i  class="btn btn-sm btn-primary">UPDATE SSF DETAILS</i>'},
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
      fileRefNumber: {
        title: 'FILE REF NUMBER',
        type: 'string',
        filter: false,
      },
      nameProduct: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      lbIdTradeMark: {
        title: 'BRAND/TRADEMARK',
        type: 'string',
        filter: false,
      },
      // sampleReferences: {
      //   title: 'SAMPLE REF',
      //   type: 'string',
      //   filter: false,
      // },
      // disposal: {
      //   title: 'DISPOSAL',
      //   type: 'string',
      //   filter: false,
      // },
      // bsNumber: {
      //   title: 'BS NUMBER',
      //   type: 'string',
      //   filter: false,
      // },
    },

    pager: {
      display: true,
      perPage: 20,
    },
  };
  public settingsWorkPlanProductsDirector = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View Recommendation</i>'},
        {name: 'addRemarkDirector', title: '<i class="btn btn-sm btn-primary">Add Remarks</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      productName: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      referenceNo: {
        title: 'REFERENCE NO',
        type: 'string',
        filter: false,
      },
      destructionRecommended: {
        title: 'DESTRUCTION RECOMMENDED',
        type: 'string',
        filter: false,
      },
      clientAppealed: {
        title: 'CLIENT APPEALED',
        type: 'string',
        filter: false,
      },
      destructionStatus: {
        title: 'DESTRUCTION STATUS',
        type: 'string',
        filter: false,
      },
      appealStatus: {
        title: 'APPEAL STATUS',
        type: 'date',
        filter: false,
      },
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
  public settingsWorkPlanProducts = {
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
        {name: 'addRecommendation', title: '<i class="btn btn-sm btn-primary">Add Recommendation</i>'},
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View Recommendation</i>'},
        // {name: 'addRemarkHod', title: '<i class="btn btn-sm btn-primary">ADD REMARKS</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      productName: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      referenceNo: {
        title: 'REFERENCE NO',
        type: 'string',
        filter: false,
      },
      destructionRecommended: {
        title: 'DESTRUCTION RECOMMENDED',
        type: 'string',
        filter: false,
      },
      clientAppealed: {
        title: 'CLIENT APPEALED',
        type: 'string',
        filter: false,
      },
      destructionStatus: {
        title: 'DESTRUCTION STATUS',
        type: 'string',
        filter: false,
      },
      appealStatus: {
        title: 'APPEAL STATUS',
        type: 'date',
        filter: false,
      },
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
  public settingsWorkPlanProductsIO = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [

        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View Recommendation</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      productName: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      referenceNo: {
        title: 'REFERENCE NO',
        type: 'string',
        filter: false,
      },
      destructionRecommended: {
        title: 'DESTRUCTION RECOMMENDED',
        type: 'string',
        filter: false,
      },
      clientAppealed: {
        title: 'CLIENT APPEALED',
        type: 'string',
        filter: false,
      },
      destructionStatus: {
        title: 'DESTRUCTION STATUS',
        type: 'string',
        filter: false,
      },
      appealStatus: {
        title: 'APPEAL STATUS',
        type: 'date',
        filter: false,
      },
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
  public settingsRecommendationList = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [

        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View Recommendation</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      productName: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      referenceNo: {
        title: 'REFERENCE NO',
        type: 'string',
        filter: false,
      },
      destructionRecommended: {
        title: 'DESTRUCTION RECOMMENDED',
        type: 'string',
        filter: false,
      },
      clientAppealed: {
        title: 'CLIENT APPEALED',
        type: 'string',
        filter: false,
      },
      destructionStatus: {
        title: 'DESTRUCTION STATUS',
        type: 'string',
        filter: false,
      },
      appealStatus: {
        title: 'APPEAL STATUS',
        type: 'date',
        filter: false,
      },
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

  public workPlanInspection: WorkPlanInspectionDto;
  public msCounties: {name: string, code: string}[];

  msRegions: RegionsEntityDto[] = null;
  msCountiesList: County[] = null;
  msTowns: Town[] = null;
  msTownsOriginal: Town[] = [];
  currentDateDetails = new Date();
  post: Post = {
    startDate: new Date(Date.now()),
    endDate: new Date(Date.now()),
  };



  constructor(
      private msService: MsService,
      // private dialog: MatDialog,
      private formBuilder: FormBuilder,
      private store$: Store<any>,
      private SpinnerService: NgxSpinnerService,
      private activatedRoute: ActivatedRoute,
      private countyService: CountyService,
      private townService: TownService,
      private router: Router) {
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$;
    countyService.getAll().subscribe();
  }

  ngOnInit(): void {
    this.currentDateDetails = new Date();

    if(this.workPlanInspection?.dataReportDto?.length!=0){
      this.calculateAverageCompliance();
    }

    console.log('currentdate' + this.currentDateDetails);

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.userLoggedInID = u.id;
      this.userProfile = u;
      return this.roles = u.roles;
    });

    this.assignOfficerForm = this.formBuilder.group({
      assignedRemarks: ['', Validators.required],
      assignedIo: ['', Validators.required],
    });

    this.finalRemarkHODForm = this.formBuilder.group({
      hodFeedBackRemarks: ['', Validators.required],
    });

    this.clientEmailNotificationForm = this.formBuilder.group({
      clientFullName: ['', Validators.required],
      clientEmail: ['', Validators.required],
      remarks: null,
    });

    this.ssfClientEmailNotificationForm = this.formBuilder.group({
      ssfID: null,
      failedParameters: null,
      outLetEmail: [''],
      manufactureEmail: [''],
      complainantEmail: [''],
      remarks: null,
    });

    this.approveScheduleForm = this.formBuilder.group({
      approvalStatus: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.finalRecommendationForm = this.formBuilder.group({
      recommendationId: null,
      hodRecommendationRemarks: ['', Validators.required],
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
      id: null,
      dataReportValueToClone: null,
      referenceNumber: ['', Validators.required],
      inspectionDate: null,
      inspectorName: ['', Validators.required],
      function: ['', Validators.required],
      department: ['', Validators.required],
      regionName: ['', Validators.required],
      county: ['', Validators.required],
      town: ['', Validators.required],
      marketCenter: ['', Validators.required],
      outletDetails: ['', Validators.required],
      outletName: ['', Validators.required],
      physicalLocation: ['', Validators.required],
      emailAddress: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      mostRecurringNonCompliant: ['', Validators.required],
      personMet: ['', Validators.required],
      samplesDrawnAndSubmitted: ['', Validators.required],
      sourceOfProductAndEvidence: ['', Validators.required],
      summaryFindingsActionsTaken: ['', Validators.required],
      finalActionSeizedGoods: ['', Validators.required],
      totalComplianceScore: ['', Validators.required],
      // remarks: ['', Validators.required],
    });

    this.investInspectReportForm = this.formBuilder.group({
      id: null,
      reportReference: null,
      reportClassification: ['', Validators.required],
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
      startDateInvestigationInspection: ['', Validators.required],
      endDateInvestigationInspection: ['', Validators.required],
      kebsInspectors: null,
      methodologyEmployed: ['', Validators.required],
      // findings: ['', Validators.required],
      conclusion: ['', Validators.required],
      recommendations: ['', Validators.required],
      statusActivity: ['', Validators.required],
      finalRemarkHod: null,
      // remarks: ['', Validators.required],
    });

    this.dataReportParamForm = this.formBuilder.group({
      id: null,
      productName: ['', Validators.required],
      typeBrandName: ['', Validators.required],
      localImport: ['', Validators.required],
      permitNumber: 'N/A',
      ucrNumber: 'N/A',
      complianceInspectionParameter: ['', Validators.required],
      measurementsResults: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.investInspectReportInspectorsForm = this.formBuilder.group({
      inspectorName: ['', Validators.required],
      designation: ['', Validators.required],
      institution: null,
    });

    this.seizureDeclarationForm = this.formBuilder.group({
      id: null,
      docID: null,
      mainSeizureID: null,
      descriptionProductsSeized: ['', Validators.required],
      product: ['', Validators.required],
      brand: ['', Validators.required],
      sector: ['', Validators.required],
      reasonSeizure: ['', Validators.required],
      seizureSerial: ['', Validators.required],
      quantity: ['', Validators.required],
      unit: ['', Validators.required],
      estimatedCost: ['', Validators.required],
      currentLocation: ['', Validators.required],
      productsDestruction: ['', Validators.required],
    });

    this.seizureForm = this.formBuilder.group({
      id: null,
      docID: null,
      seizureFormValueToClone: null,
      productField: ['', Validators.required],
      serialNumber: ['', Validators.required],
      marketTownCenter: ['', Validators.required],
      nameOfOutlet: ['', Validators.required],
      nameSeizingOfficer: ['', Validators.required],
      additionalOutletDetails: ['', Validators.required],
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

    this.fieldReportAdditionalInfortForm = this.formBuilder.group({
      performanceOnTestSamples: ['', Validators.required],
      actionOnSeizedGoods: null,
      actionOnSeizedGoodsRemarks: ['', Validators.required],
      actionsOnRecommendationGiven: ['', Validators.required],
      followUpActivities: ['', Validators.required],
      others: ['', Validators.required],
    });

    this.sampleSubmitForm = this.formBuilder.group({
      valueToClone: null,
      id: null,
      dataReportSelected: null,
      nameProduct: ['', Validators.required],
      packaging: ['', Validators.required],
      labellingIdentification: null,
      fileRefNumber: null,
      sizeTestSample: ['', Validators.required],
      sizeRefSample: null,
      condition: ['', Validators.required],
      sampleReferences: null,
      sendersName: ['', Validators.required],
      designation: ['', Validators.required],
      address: ['', Validators.required],
      sendersDate: this.msService.formatDate(new Date()),
      receiversName: null,
      productDescription: null,
      receiversDate: null,
      lbIdAnyAomarking: null,
      sampleCollectionDate: null,
      lbIdBatchNo: null,
      lbIdContDecl: null,
      lbIdDateOfManf: null,
      lbIdExpiryDate: null,
      lbIdTradeMark: null,
      noteTransResults: null,
      referencesStandards: ['', Validators.required],
      scfNo: null,
      cocNumber: null,
      testChargesKsh: null,
      receiptLpoNumber: null,
      invoiceNumber: null,
      disposal: ['', Validators.required],
      remarks: ['', Validators.required],
      sourceProductEvidence: null,
      sampleCollectionNumber: null,
    });


    this.sampleSubmitParamsForm = this.formBuilder.group({
      id: null,
      parameters: ['', Validators.required],
      laboratoryName: ['', Validators.required],
    });

    this.sampleSubmitBSNumberForm = this.formBuilder.group({
      bsNumber: ['', Validators.required],
      ssfID: null,
      submittedDate: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.pdfSaveComplianceStatusForm = this.formBuilder.group({
      complianceStatus: ['', Validators.required],
      failedParameters: [''],
      complianceRemarks: ['', Validators.required],

    });

    this.ssfSaveComplianceStatusForm = this.formBuilder.group({
      complianceStatus: ['', Validators.required],
      complianceRemarks: ['', Validators.required],
      totalCompliance: null,
      totalComplianceTest: null,
      failedParameters: [''],
    });

    this.verificationPermitForm = this.formBuilder.group({
      id: null,
      permitNumber: null,
      ucrNumber: null,
      productName: null,
      validityStatus: null,
    });

    this.finalRecommendationDetailsForm = this.formBuilder.group({
      recommendationId: ['', Validators.required],
      recommendationName: ['', Validators.required],
      otherRecommendationName: [''],
    });

    this.preliminaryReportForm = this.formBuilder.group({
      id : null,
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

    this.addCountyTownForm = this.formBuilder.group({
      regionId: ['', Validators.required],
      countyId: ['', Validators.required],
      townsId: ['', Validators.required],
      regionName: 'N/A',
      countyName: 'N/A',
      townsName: 'N/A',
    });


    this.preliminaryReportFinalForm = this.formBuilder.group({
      id : null,
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
      surveillanceConclusions : ['', Validators.required],
      surveillanceRecommendation : ['', Validators.required],
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

    this.addResourceRequiredForm = this.formBuilder.group({
      resourceName: ['', Validators.required],
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
      remarksStatus: null,
      remarks: null,
    });

    this.actionOnSiezedGoodsForm = this.formBuilder.group({
      actionOnSeizedGoodsDetails: ['', Validators.required],
    });

    this.startOnsiteActivitiesForm = this.formBuilder.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      remarks: null,
    });

    this.addNewScheduleForm = this.formBuilder.group({
      id: null,
      complaintDepartment: ['', Validators.required],
      divisionId: ['', Validators.required],
      nameActivity: ['', Validators.required],
      rationale: ['', Validators.required],
      scopeOfCoverage: ['', Validators.required],
      timeActivityDate: ['', Validators.required],
      timeActivityEndDate: ['', Validators.required],
      region: null,
      county: null,
      townMarketCenter: null,
      locationActivityOther: ['', Validators.required],
      productString: ['', Validators.required],
      resourcesRequired: null,
      budget: ['', Validators.required],
      remarks: null,
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
// Hof Reject
    if (this.workPlanInspection?.preliminaryReport?.rejectedStatus
        && this.workPlanInspection?.preliminaryReport?.approvedStatus === false
        && this.workPlanInspection?.preliminaryReport?.rejectedStatusHod === false
    ) {
      this.preliminaryReportForm.patchValue(this.workPlanInspection?.preliminaryReport);
      this.workPlanInspection?.preliminaryReport?.kebsOfficersName.forEach(inspector => {
        this.dataSaveDataInspectorInvestList.push(inspector);
      });
      this.workPlanInspection?.preliminaryReport?.parametersList.forEach(param => {
        this.dataSavePreliminaryReportParamList.push(param);
      });
    }
// Hod Reject
    if (this.workPlanInspection?.preliminaryReport?.rejectedStatusHod
        && this.workPlanInspection?.preliminaryReport?.rejectedStatus
        && this.workPlanInspection?.preliminaryReport?.approvedStatusHod === false ) {
      this.preliminaryReportForm.patchValue(this.workPlanInspection?.preliminaryReport);
      this.workPlanInspection?.preliminaryReport?.kebsOfficersName.forEach(inspector => {
        this.dataSaveDataInspectorInvestList.push(inspector);
      });
      this.workPlanInspection?.preliminaryReport?.parametersList.forEach(param => {
        this.dataSavePreliminaryReportParamList.push(param);
      });
    }

    if (this.workPlanInspection?.productList?.length > 0 && this.workPlanInspection?.preliminaryReportFinal?.approvedStatusHodFinal) {
      this.recommendationDetailsLoad();
    }

    if (this.workPlanInspection?.onsiteStartStatus === false ) {
      this.addNewScheduleForm.patchValue(this.workPlanInspection?.updateWorkPlan);
      this.msService.msDepartmentListDetails().subscribe(
          (dataDep: MsDepartment[]) => {
            this.msDepartments = dataDep;
            console.log(dataDep);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.msService.msDivisionListDetails().subscribe(
          (dataDiv: MsDivisionDetails[]) => {
            this.msDivisions = dataDiv;
            console.log(dataDiv);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.msService.msProductStandardCategoryListDetails().subscribe(
          (data1: MsStandardProductCategory[]) => {
            this.standardProductCategory = data1;
            console.log(data1);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.msService.msProductBroadCategoryListDetails().subscribe(
          (data2: MsBroadProductCategory[]) => {
            this.broadProductCategory = data2;
            console.log(data2);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.msService.msProductCategoryListDetails().subscribe(
          (data3: MsBroadProductCategory[]) => {
            this.productCategories = data3;
            console.log(data3);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.msService.msProductListDetails().subscribe(
          (data4: MsProducts[]) => {
            this.products = data4;
            console.log(data4);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.msService.msProductSubCategoryListDetails().subscribe(
          (data5: MsProductSubcategory[]) => {
            this.productSubcategory = data5;
            console.log(data5);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }


    if (this.workPlanInspection?.sampleSubmittedStatus === false) {
      this.msService.msLaboratoriesListDetails().subscribe(
          (data1: LaboratoryEntityDto[]) => {
            this.laboratories = data1;
            console.log(data1);
          },
          error => {
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
      this.loadStandards();

    }

    switch (this.workPlanInspection?.preliminaryReportFinal?.approvedStatusHodFinal) {
      case true:
        this.msService.MsRecommendationListDetails().subscribe(
            (data) => {
              this.recommendationList = data;
              this.SpinnerService.hide();
              console.log(data);
            },
            error => {
              this.SpinnerService.hide();
              console.log(error);
              this.msService.showError('AN ERROR OCCURRED');
            },
        );
        break;
    }

  }

  recommendationDetailsLoad() {
    let countRecommendationHOD = 0;
    let countRecommendationDirector = 0;
    let destructionRecommendedAllDone = 0;
    let destructionRecommended = 0;
    for (let i = 0; i < this.workPlanInspection?.productList.length; i++) {
      if (this.workPlanInspection?.productList[i].hodRecommendationStatus) {
        countRecommendationHOD++;
      }
      if (this.workPlanInspection?.productList[i].directorRecommendationStatus) {
        countRecommendationDirector++;
      }
      if (this.workPlanInspection?.productList[i].destructedStatus) {
        destructionRecommendedAllDone++;
      }
      if (this.workPlanInspection?.productList[i].destructionRecommended) {
        destructionRecommended++;
      }
      if (this.workPlanInspection?.productList[i].appealStatus) {
        destructionRecommendedAllDone++;
      }
    }
    this.productsDestructionRecommendationDone = destructionRecommendedAllDone;
    this.productsDestructionRecommendation = destructionRecommended;
    this.productsCountRecommendationHOD = countRecommendationHOD;
    this.productsCountRecommendationDirector = countRecommendationDirector;
    console.log(this.productsCountRecommendationHOD);
  }

  loadStandards() {
    this.msService.msStandardsListDetails().subscribe(
        (data1: KebsStandardsDto[]) => {
          this.standards = data1;
          console.log(data1);
        },
        error => {
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  get formActionOnSiezedGoodsForm(): any {
    return this.actionOnSiezedGoodsForm.controls;
  }

  get formAddCountyTownForm(): any {
    return this.addCountyTownForm.controls;
  }

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formAddResourceRequiredForm(): any {
    return this.addResourceRequiredForm.controls;
  }

  get formClientEmailNotificationForm(): any {
    return this.clientEmailNotificationForm.controls;
  }

  get formSSFClientEmailNotificationForm(): any {
    return this.ssfClientEmailNotificationForm.controls;
  }

  get formFinalRemarkHODForm(): any {
    return this.finalRemarkHODForm.controls;
  }

  get formFinalRecommendationDetailsForm(): any {
    return this.finalRecommendationDetailsForm.controls;
  }


  get formPreliminaryReportForm(): any {
    return this.preliminaryReportForm.controls;
  }

  get formPreliminaryFinalReportForm(): any {
    return this.preliminaryReportFinalForm.controls;
  }

  get formFinalRecommendationForm(): any {
    return this.finalRecommendationForm.controls;
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
    return this.chargeSheetForm?.controls;
  }

  get formInvestInspectReportForm(): any {
    return this.investInspectReportForm.controls;
  }

  get formFieldReportAdditionalInfoForm(): any {
    return this.fieldReportAdditionalInfortForm.controls;
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

  get formSeizureForm(): any {
    return this.seizureForm.controls;
  }

  get formPdfSaveComplianceStatusForm(): any {
    return this.pdfSaveComplianceStatusForm.controls;
  }

  get formSSFSaveComplianceStatusForm(): any {
    return this.ssfSaveComplianceStatusForm.controls;
  }

  get formStartOnsiteActivitiesForm(): any {
    return this.startOnsiteActivitiesForm.controls;
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

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
  }

  get formNotCompliantInvoiceForm(): any {
    return this.notCompliantInvoiceForm.controls;
  }

  get formRemediationForm(): any {
    return this.remediationForm.controls;
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onClickAddCountyTown() {
    this.dataSaveWorkPlanCounties = this.addCountyTownForm.value;
    console.log(this.dataSaveWorkPlanCounties);
    // tslint:disable-next-line:max-line-length
    const  countyDetails = this.dataSaveWorkPlanCountiesList.filter(x => this.dataSaveWorkPlanCounties.regionId === x.regionId && this.dataSaveWorkPlanCounties.countyId === x.countyId && this.dataSaveWorkPlanCounties.townsId === x.townsId).length;
    if (countyDetails > 0) {
      // tslint:disable-next-line:max-line-length
      this.msService.showWarning('You have already added this Region Name (' + this.dataSaveWorkPlanCounties.regionName + ') ,County Name (' + this.dataSaveWorkPlanCounties.countyName + ') and Town Name (' + this.dataSaveWorkPlanCounties.townsName + ')');
    } else {
      this.dataSaveWorkPlanCountiesList.push(this.dataSaveWorkPlanCounties);
    }
    this.msTowns = this.msTownsOriginal;
    this.addCountyTownForm.controls.countyId.disable();
    this.addCountyTownForm.controls.townsId.disable();
    this.addCountyTownForm.get('regionId').reset();
    this.addCountyTownForm.get('countyId').reset();
    this.addCountyTownForm.get('townsId').reset();
    // console.log(this.addCountyTownForm.value);
  }

  // Remove Form repeater values
  removeDataCountyTown(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveWorkPlanCountiesList.splice(index, 1);
    } else {
      this.dataSaveWorkPlanCountiesList.splice(index, index);
    }
  }

  updateSelectedRecommendation() {
    this.selectedRecommendationID = this.finalRecommendationDetailsForm?.get('recommendationId')?.value;
    // this.selectedRecommendationName = ;
    const valueFound = this.recommendationList?.filter(x => Number(this.selectedRecommendationID) === Number(x.id));
    for (let h = 0; h < valueFound.length; h++) {
      this.selectedRecommendationName = valueFound[h].recommendationName;
      console.log(`selectedRecommendationName set to ${valueFound[h].recommendationName}`);
    }
    console.log(`selectedRecommendationName set to ${this.selectedRecommendationName}`);
  }


  onClickAddDataRecommendationDetails() {
    this.dataSaveFinalRecommendationDetails = this.finalRecommendationDetailsForm.value;
    // tslint:disable-next-line:max-line-length
    const  resourceName = this.dataSaveFinalRecommendationList.filter(x => String(this.dataSaveFinalRecommendationDetails.recommendationName) === String(x.recommendationName)).length;
    if (resourceName > 0) {
      this.msService.showWarning('You have already added ' + this.dataSaveFinalRecommendationDetails.recommendationName);
    } else {
      this.dataSaveFinalRecommendationList.push(this.dataSaveFinalRecommendationDetails);
    }
    this.finalRecommendationDetailsForm?.reset();
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
    this.msService.msRegionListDetails().subscribe(
        (dataRegions: RegionsEntityDto[]) => {
          this.msRegions = dataRegions;
        },
        error => {
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    this.msService.msCountiesListDetails().subscribe(
        (dataCounties: County[]) => {
          this.msCountiesList = dataCounties;
        },
        error => {
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    this.msService.msTownsListDetails().subscribe(
        (dataTowns: Town[]) => {
          this.msTowns = dataTowns;
          this.msTownsOriginal = dataTowns;
        },
        error => {
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    this.msService.msPredefinedResourcesRequiredListDetails().subscribe(
        (data1: PredefinedResourcesRequired[]) => {
          this.predefinedResourcesRequired = data1;
          console.log(data1);
        },
        error => {
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );

  }

  openModalAddDetails(divVal: string): void {
    this.addProductsStatus = true;
    const arrHead = ['approveSchedule', 'uploadFiles', 'chargeSheetDetails', 'dataReportDetails', 'seizureDeclarationDetails', 'finalLabComplianceStatus',
      'addBsNumber', 'approvePreliminaryHOF', 'approvePreliminaryHOD', 'addPreliminaryRecommendation', 'approveFinalPreliminaryHOF', 'approveFinalPreliminaryHOD',
      'ssfAddComplianceStatus', 'addFinalRecommendationHOD', 'uploadDestructionNotificationFile',
      'clientAppealed', 'clientAppealedSuccessfully', 'uploadDestructionReport', 'addFinalRemarksHOD',
      'uploadChargeSheetFiles', 'uploadSCFFiles', 'uploadSSFFiles', 'uploadSeizureFiles', 'uploadDeclarationFiles', 'uploadDataReportFiles',
      'addNewScheduleDetails', 'openSampleSubmitModal', 'updateHOFHODPreliminary', 'createPreliminary', 'updateIOPreliminary', 'uploadFilesFinalReport', 'uploadFilesFinalReportHOFHOD',
      'approveFinalPreliminaryDirector', 'startOnsiteActivities'];

    // tslint:disable-next-line:max-line-length
    const arrHeadSave = ['APPROVE/REJECT SCHEDULED WORK-PLAN', 'ATTACH FILE(S) BELOW', 'ADD CHARGE SHEET DETAILS', 'ADD DATA REPORT DETAILS', 'ADD SEIZURE DECLARATION DETAILS', 'FINAL LAB RESULTS COMPLIANCE STATUS',
      'ADD BS NUMBER', 'APPROVE/REJECT PRELIMINARY REPORT', 'APPROVE/REJECT PRELIMINARY REPORT', 'ADD FINAL REPORT DETAILS', 'APPROVE/REJECT FINAL REPORT', 'APPROVE/REJECT FINAL REPORT',
      'ADD SSF LAB RESULTS COMPLIANCE STATUS', 'ADD FINAL RECOMMENDATION FOR THE SURVEILLANCE', 'UPLOAD DESTRUCTION NOTIFICATION TO BE SENT'
      , 'DID CLIENT APPEAL ?', 'ADD CLIENT APPEALED STATUS IF SUCCESSFULLY OR NOT', 'UPLOAD DESTRUCTION REPORT', 'ADD FINAL REMARKS FOR THE MS CONDUCTED',
      'ATTACH CHARGE SHEET FILE BELOW', 'ATTACH SAMPLE COLLECTION FILE BELOW', 'ATTACH SAMPLE SUBMISSION FILE BELOW', 'ATTACH SEIZURE FILE BELOW', 'ATTACH DECLARATION FILE BELOW', 'ATTACH DATA REPORT FILE BELOW',
      'UPDATE WORK-PLAN SCHEDULE DETAILS FILE', 'openSampleSubmitModal', 'updateHOFHODPreliminary', 'createPreliminary', 'updateIOPreliminary', 'UPLOAD FINAL REPORT', 'UPLOAD FINAL REPORT',
      'APPROVE/REJECT FINAL REPORT', 'KINDLY ADD START AND END DATE YOU WISH TO END ON-SITE ACTIVITIES'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }

    if (divVal === 'seizureDeclarationDetails') {
      this.dataSaveSeizureDeclarationList = [];
      this.seizureDeclarationForm.reset();
      this.seizureForm.reset();
      this.seizureForm.enable();
      this.addSeizureProductsStatus = true;
    }

    if (divVal === 'finalLabComplianceStatus') {
      let complianceLabs = 0;
      const totalCount = this.workPlanInspection?.sampleLabResults.length;
      for (let b = 0; b < this.workPlanInspection?.sampleLabResults.length; b++) {
        if (this.workPlanInspection?.sampleLabResults[b].ssfResultsList.complianceStatus === true) {
          complianceLabs++;
        }
      }
      this.ssfSaveComplianceStatusForm.controls.totalComplianceTest.disable();
      this.totalComplianceValue = (complianceLabs / totalCount) * 100;
    }

    if (divVal === 'createPreliminary') {
      this.updatePreliminaryReport();
    }

    if (divVal === 'updateHOFHODPreliminary') {
      this.updatePreliminaryReportHOFHODIO();
      console.log(divVal);
    }

    if (divVal === 'updateIOPreliminary') {
      this.updatePreliminaryReportHOFHODIO();
      console.log(divVal);
    }

    this.updateFieldReport();
    this.updateWorkPlan();
    // this.addFinalPreliminaryReport();
    // this.updateFinalPreliminaryReport();
    this.currDiv = divVal;
  }

  addFinalPreliminaryReport() {
    if (!this.workPlanInspection?.finalReportGenerated && this.workPlanInspection?.msPreliminaryReportStatus) {
      this.preliminaryReportFinalForm.patchValue(this.workPlanInspection?.preliminaryReport);
      this.dataSaveDataInspectorInvestList = [];
      for (let prod = 0; prod < this.workPlanInspection?.preliminaryReport?.kebsOfficersName.length; prod++) {
        this.dataSaveDataInspectorInvestList.push(this.workPlanInspection?.preliminaryReport?.kebsOfficersName[prod]);
      }
      this.dataSavePreliminaryReportParamList = [];
      for (let prod = 0; prod < this.workPlanInspection?.preliminaryReport?.parametersList.length; prod++) {
        // tslint:disable-next-line:no-non-null-assertion
        this.workPlanInspection!.preliminaryReport!.parametersList[prod].id = -1;
        this.dataSavePreliminaryReportParamList.push(this.workPlanInspection?.preliminaryReport?.parametersList[prod]);
      }
    }
  }

  updateFinalPreliminaryReport() {
    if (this.workPlanInspection?.finalReportGenerated && this.workPlanInspection?.msPreliminaryReportStatus) {
      this.preliminaryReportFinalForm.patchValue(this.workPlanInspection?.preliminaryReportFinal);
      this.dataSaveDataInspectorInvestList = [];
      for (let prod = 0; prod < this.workPlanInspection?.preliminaryReportFinal?.kebsOfficersName.length; prod++) {
        this.dataSaveDataInspectorInvestList.push(this.workPlanInspection?.preliminaryReportFinal?.kebsOfficersName[prod]);
      }
      this.dataSavePreliminaryReportParamList = [];
      for (let prod = 0; prod < this.workPlanInspection?.preliminaryReportFinal?.parametersList.length; prod++) {
        this.dataSavePreliminaryReportParamList.push(this.workPlanInspection?.preliminaryReportFinal?.parametersList[prod]);
      }
    }
  }

  updatePreliminaryReportHOFHODIO() {
    if (this.workPlanInspection?.msPreliminaryReportStatus) {
      this.selectedPreliminaryReportDetails = this.workPlanInspection.preliminaryReportListDto.find(pr => pr.id === this.workPlanInspection.latestPreliminaryReport);
      this.investInspectReportForm.patchValue(this.selectedPreliminaryReportDetails);
      this.dataSaveDataInspectorInvestList = [];
      for (let prod = 0; prod < this.selectedPreliminaryReportDetails?.kebsInspectors.length; prod++) {
        this.dataSaveDataInspectorInvestList.push(this.selectedPreliminaryReportDetails?.kebsInspectors[prod]);
      }

      this.dataSaveBsNumber = [];
      for (let prod = 0; prod < this.selectedPreliminaryReportDetails.bsNumbersList.length; prod++) {
        this.dataSaveBsNumber.push(this.selectedPreliminaryReportDetails.bsNumbersList[prod]);
      }

      console.log(this.selectedPreliminaryReportDetails);
    }
  }

  updatePreliminaryReport() {
    if (this.workPlanInspection?.investInspectReportStatus &&  this.workPlanInspection?.onsiteEndStatus) {
      this.investInspectReportForm.patchValue(this.workPlanInspection?.inspectionInvestigationDto);
      this.dataSaveDataInspectorInvestList = [];
      for (let prod = 0; prod < this.workPlanInspection?.inspectionInvestigationDto?.kebsInspectors.length; prod++) {
        this.dataSaveDataInspectorInvestList.push(this.workPlanInspection?.inspectionInvestigationDto?.kebsInspectors[prod]);
      }

      this.dataSaveBsNumber = [];
      for (let prod = 0; prod < this.workPlanInspection?.sampleSubmitted.length; prod++) {
        this.dataSaveBsNumber.push(this.workPlanInspection?.sampleSubmitted[prod].bsNumber);
      }
    }
  }

  updateDataReport(data: DataReportDto) {
    this.dataReportForm.patchValue(data);
    this.selectedDataReportDetails = data;
    this.totalCompliantValue = data?.totalComplianceScore;
    const paramDetails = data.productsList;
    this.dataSaveDataReportParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveDataReportParamList.push(paramDetails[i]);
    }
    // this.dataReportForm.disable();
    this.addProductsStatus = true;
    window.$('#dataReportModal').modal('show');
    // }
  }

  // viewUploadDataReport(data: DataReportDto) {
  //   this.dataReportForm.patchValue(data);
  //   this.selectedDataReportDetails = data;
  //   this.totalCompliantValue = data?.totalComplianceScore;
  //   const paramDetails = data.productsList;
  //   this.dataSaveDataReportParamList = [];
  //   for (let i = 0; i < paramDetails.length; i++) {
  //     this.dataSaveDataReportParamList.push(paramDetails[i]);
  //   }
  //   // this.dataReportForm.disable();
  //   this.addProductsStatus = true;
  //   window.$('#dataReportModal').modal('show');
  //   // }
  // }

  updateFieldReport() {
    if (this.workPlanInspection?.investInspectReportStatus &&  this.workPlanInspection?.onsiteEndStatus === false) {
      this.investInspectReportForm.patchValue(this.workPlanInspection?.inspectionInvestigationDto);
      this.dataSaveDataInspectorInvestList = [];
      for (let prod = 0; prod < this.workPlanInspection?.inspectionInvestigationDto?.kebsInspectors.length; prod++) {
        this.dataSaveDataInspectorInvestList.push(this.workPlanInspection?.inspectionInvestigationDto?.kebsInspectors[prod]);
      }
    }
  }

  updateWorkPlan() {
    if (!this.workPlanInspection?.onsiteStartStatus) {
      this.addNewScheduleForm.patchValue(this.workPlanInspection?.updateWorkPlan);
      this.dataSaveResourcesRequiredList = [];
      for (let prod = 0; prod < this.workPlanInspection?.updateWorkPlan?.resourcesRequired.length; prod++) {
        this.dataSaveResourcesRequiredList.push(this.workPlanInspection?.updateWorkPlan.resourcesRequired[prod]);
      }

      this.dataSaveWorkPlanCountiesList = [];
      for (let prod = 0; prod < this.workPlanInspection?.updateWorkPlan?.workPlanCountiesTowns.length; prod++) {
        this.dataSaveWorkPlanCountiesList.push(this.workPlanInspection?.updateWorkPlan.workPlanCountiesTowns[prod]);
      }
    }
  }


  public onCustomWorkPlanProductsAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecommendationRecord(event.data);
        break;
      case 'addRecommendation':
        this.addRecommendation(event.data);
        break;
      case 'addRemarkDirector':
        this.addRemarksDirector(event.data);
        break;
    }
  }

  addRecommendation(data: WorkPlanProductDto) {
    this.dataSaveFinalRecommendationList = [];
    if (data?.hodRecommendationStatus) {
      this.dataSaveFinalRecommendationList = data.recommendation;
    }
    this.workPlanProductRefNo = data.referenceNo;
    window.$('#finalRecommendationModal').modal('show');
    // }

  }

  addRemarksDirector(data: WorkPlanProductDto) {
    // if (!data.directorRecommendationStatus) {
    //   this.msService.showWarning('Remark(s) Have/Has already been added');
    // } else {
    this.workPlanProductRefNo = data.referenceNo;
    this.currDiv = 'addFinalRemarksDirector';
    this.currDivLabel = 'ADD REMARKS FOR THE RECOMMENDATION ADDED';
    window.$('#myModal1').modal('show');
    // }

  }

  public onCustomDataReportAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewDataReportRecord(event.data);
        break;
      case 'updateRecord':
        this.updateDataReport(event.data);
        break;
    }
  }


  public onCustomSSFAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewSSFRecord(event.data);
        break;
      case 'updateRecord':
        this.updateSSFRecord(event.data);
        break;
      case 'addBSNumber':
        this.addSSFBsNumberRecord(event.data);
        break;
      case 'viewLabResults':
        this.viewSSFLabResultsRecord(event.data);
        break;
    }
  }

  viewSSFLabResultsRecord(data: SampleSubmissionDto) {
    this.selectedSSFDetails = data;
    this.selectedLabResults = this.workPlanInspection?.sampleLabResults.find(lab => lab?.ssfResultsList?.bsNumber === data?.bsNumber);

    window.$('#myModal2').modal('hide');
    // window.$('.modal').remove();
    window.$('body').removeClass('modal-open');
    window.$('.modal-backdrop').remove();
    window.$('#sampleLabResultsModal').modal('show');
  }

  closeSSFLabResultsRecord() {
    window.$('#myModal2').modal('hide');
    window.$('#sampleLabResultsModal').modal('hide');
    window.$('body').removeClass('modal-open');
    window.$('.modal-backdrop').remove();

  }

  closePopUpsModal3() {
    window.$('#myModal3').modal('hide');
    window.$('body').removeClass('modal-open');
    window.$('.modal-backdrop').remove();
    window.$('#dataReportModal').modal('hide');
    window.$('body').removeClass('modal-open');
    window.$('.modal-backdrop').remove();

    setTimeout(function() {
      window.$('#dataReportModal').modal('show');
    }, 500);
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

  addSSFBsNumberRecord(data: SampleSubmissionDto) {
    if (data.bsNumber !== null && this.workPlanInspection?.bsNumberStatus) {
      this.msService.showError('BS Number Already Added And Pushed To LIMS');
    } else {
      this.currDivLabel = `ADD BS NUMBER FOR FILE REFERENCE NUMBER # ${data.fileRefNumber}`;
      this.currDiv = 'addBsNumber';
      this.sampleSubmitBSNumberForm.reset();
      this.ssfSelectedID = data.id;
      console.log('SSF ID BS NUMBER ADDING' + this.ssfSelectedID);
      window.$('#myModal1').modal('show');
    }
  }

  viewDataReportRecord(data: DataReportDto) {

    this.dataReportForm.patchValue(data);
    this.selectedDataReportDetails = data;
    this.totalCompliantValue = data?.totalComplianceScore;
    const paramDetails = data.productsList;
    this.dataSaveDataReportParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveDataReportParamList.push(paramDetails[i]);
    }

    this.dataSaveDataReportUploadsList = [];
    for (let i = 0; i < data.docList.length; i++) {
      const fileValue = this.workPlanInspection?.workPlanFiles.find(file => file.id === i);
      this.dataSaveDataReportUploadsList.push(fileValue);
    }

    this.dataReportForm.disable();
    this.addProductsStatus = false;
    window.$('#dataReportModal').modal('show');
  }

  viewSSFRecord(data: SampleSubmissionDto) {
    this.sampleSubmitForm.patchValue(data);
    this.selectedSFFDetails = data;
    const paramDetails = data.parametersList;
    this.dataSaveSampleSubmitParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSampleSubmitParamList.push(paramDetails[i]);
    }
    this.sampleSubmitForm.disable();
    this.addLabParamStatus = false;
    window.$('#sampleSubmitModal').modal('show');
  }

  viewRecommendationRecord(data: WorkPlanProductDto) {
    this.selectedProductRecommendation = data;

    window.$('#finalRecommendationView').modal('show');
  }

  updateSSFRecord(data: SampleSubmissionDto) {
    this.sampleSubmitForm.patchValue(data);
    const paramDetails = data.parametersList;
    this.dataSaveSampleSubmitParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSampleSubmitParamList.push(paramDetails[i]);
    }
    this.sampleSubmitForm.enable();
    this.addLabParamStatus = true;
    window.$('#sampleSubmitModal').modal('show');
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

  viewSSFPdfFile(ssfID: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.msService.loadSSFDetailsPDF(String(ssfID)).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = `Sample-Submission-${fileName}`;
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

  viewFieldReportPdfFile(workPlanGeneratedID: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.msService.loadFieldReportDetailsPDF(String(workPlanGeneratedID)).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = `Field-Report-${fileName}`;
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

  onClickSaveFinalRemarksDirector() {
    this.msService.showSuccessWith2Message('Are you sure your want to add the Remarks?', 'You won\'t be able to Update the remarks Details after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update the remarks you have added Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.saveFinalRemarksRecommendationDirector();
        });
  }

  saveFinalRemarksRecommendationDirector() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveFinalRemarks = {...this.dataSaveFinalRemarks, ...this.finalRemarkHODForm.value};
    this.msService.msWorkPlanScheduleDetailsFinalRemarksDirector(
        this.workPlanProductRefNo,
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
        this.dataSaveFinalRemarks,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;

          let countRecommendationHOD = 0;
          let countRecommendationDirector = 0;
          for (let i = 0; i < this.workPlanInspection?.productList.length; i++) {
            if (this.workPlanInspection?.productList[i].hodRecommendationStatus) {
              countRecommendationHOD++;
            }
            if (this.workPlanInspection?.productList[i].directorRecommendationStatus) {
              countRecommendationDirector++;
            }
          }
          this.productsCountRecommendationHOD = countRecommendationHOD;
          this.productsCountRecommendationDirector = countRecommendationDirector;


          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('FINAL REMARKS AND STATUS SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  onClickSaveClientAppealed(valid: boolean) {

    if(this.uploadedAppealFiles.length > 0){
        this.saveAppealFilesResults("CLIENT_APPEAL_DOCUMENT");
    }
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to add the details?', 'You won\'t be able to Update the Details after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and change the details/files Before Saving', 'SUCCESS', () => {
            this.saveClientAppealed(valid);
          });
    }
  }

  saveAppealFilesResults(docName: string){
    this.SpinnerService.show();
    const appealFiles = this.uploadedAppealFiles;
    const formData = new FormData();
    formData.append('referenceNo', this.workPlanInspection.referenceNumber);
    formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
    formData.append('docTypeName', docName);
    for (let i = 0; i < appealFiles.length; i++) {
      formData.append('docFile', appealFiles[i], appealFiles[i].name);
    }
    this.msService.saveWorkPlanFiles(formData).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          this.SpinnerService.hide();
          this.msService.showSuccess('APPEAL FILE(S) UPLOADED AND SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  saveSuccessfulAppealFilesResults(docName: string){
    this.SpinnerService.show();
    const successfulAppealFiles = this.uploadedSuccessfulAppealFiles;
    const formData = new FormData();
    formData.append('referenceNo', this.workPlanInspection.referenceNumber);
    formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
    formData.append('docTypeName', docName);
    for (let i = 0; i < successfulAppealFiles.length; i++) {
      formData.append('docFile', successfulAppealFiles[i], successfulAppealFiles[i].name);
    }
    this.msService.saveWorkPlanFiles(formData).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          this.SpinnerService.hide();
          this.msService.showSuccess('APPEAL FILE(S) UPLOADED AND SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }


  saveClientAppealed(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsClientAppealed(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.selectedProductRecommendation.referenceNo,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.recommendationDetailsLoad();
            this.msService.showSuccess('Client appeal status,Saved successfully');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveClientAppealedSuccessfully(valid: boolean) {
    if(this.uploadedSuccessfulAppealFiles.length > 0){
      this.saveSuccessfulAppealFilesResults("SUCCESSFUL/UNSUCCESSFUL_APPEAL_DOCUMENT");
    }
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to save the details?', 'You won\'t be able to Update the Details after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and change the details/files Before Saving', 'SUCCESS', () => {
            this.saveClientAppealedSuccessfully(valid);
          });
    }
  }

  saveClientAppealedSuccessfully(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsClientAppealedSuccessfully(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.selectedProductRecommendation.referenceNo,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.approvePreliminaryForm.reset();
            this.recommendationDetailsLoad();
            this.SpinnerService.hide();
            this.msService.showSuccess('Client appeal Successfully status,Saved successfully');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveApproveFinalPreliminaryHOF(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsApproveFinalPreliminaryHOF(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveApproveFinalPreliminaryHOD(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsApproveFinalPreliminaryHOD(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveApproveFinalPreliminaryDirector(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsApproveFinalPreliminaryDirector(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  // Remove Form repeater values
  removeDataRecommendation(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveFinalRecommendationList.splice(index, 1);
    } else {
      this.dataSaveFinalRecommendationList.splice(index, index);
    }
  }

  onClickSaveFinalRemarksHOD(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveFinalRemarks = {...this.dataSaveFinalRemarks, ...this.finalRemarkHODForm.value};
      this.msService.msWorkPlanScheduleDetailsFinalRemarksHOD(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveFinalRemarks,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REMARKS REAMRKS AND STATUS SAVED SUCCESSFULLY');
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

  onClickSaveFinalRecommendationHOD() {
    this.msService.showSuccessWith2Message('Are you sure your want to Submit the details?', 'You won\'t be able to revert back/Update after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update/remove some recommendation Before Saving', 'ONSITE ACTIVITIES ENDED SUCCESSFUL', () => {
          this.saveFinalRecommendationHOD();
        });
  }

  saveFinalRecommendationHOD() {
    this.SpinnerService.show();
    this.dataSaveFinalRecommendation = {...this.dataSaveFinalRecommendation, ...this.finalRecommendationForm.value};
    this.dataSaveFinalRecommendation.recommendationId = this.dataSaveFinalRecommendationList;
    this.msService.msWorkPlanScheduleDetailsFinalRecommendationHOD(
        this.workPlanProductRefNo,
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
        this.dataSaveFinalRecommendation,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          let countRecommendationHOD = 0;
          let countRecommendationDirector = 0;
          for (let i = 0; i < this.workPlanInspection?.productList.length; i++) {
            if (this.workPlanInspection?.productList[i].hodRecommendationStatus) {
              countRecommendationHOD++;
            }
            if (this.workPlanInspection?.productList[i].directorRecommendationStatus) {
              countRecommendationDirector++;
            }
          }
          this.productsCountRecommendationHOD = countRecommendationHOD;
          this.productsCountRecommendationDirector = countRecommendationDirector;

          this.SpinnerService.hide();
          this.msService.showSuccess('FINAL RECOMMENDATION SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  onClickSaveFinalReport(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveFinalReport = {...this.dataSaveFinalReport, ...this.preliminaryRecommendationForm.value};
      this.msService.msWorkPlanScheduleDetailsFinalReport(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveFinalReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR HAS OCCURRED');
          },
      );
    }
  }

  onClickStartOnsiteActivities(valid: boolean) {
    this.msService.showSuccessWith2Message('Are you sure your want to Start ON-SITE ACTIVITIES?', 'By clicking \'YES\' you will be staring the Timeliness for Onsite Activities!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update the work-Plan Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.startOnsiteActivities(valid);
        });
  }

  startOnsiteActivities(valid: boolean) {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveStartOnsiteActivities = {...this.dataSaveStartOnsiteActivities, ...this.startOnsiteActivitiesForm.value};
    this.msService.msWorkPlanScheduleDetailsStartOnsiteActivities(this.workPlanInspection.batchDetails.referenceNumber, this.workPlanInspection.referenceNumber, this.dataSaveStartOnsiteActivities).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('WORK-PLAN DETAILS SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          // this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  onClickSubmitForApproval() {
    this.msService.showSuccessWith2Message('Are you sure your want to Submit for approval?', 'You won\'t be able to revert Update Work-Plan after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update the work-Plan Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.submitForApproval();
        });
  }

  submitForApproval() {
    // if (valid) {
    this.SpinnerService.show();
    this.msService.msWorkPlanScheduleDetailsSubmitForApproval(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          // console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('WORK-PLAN DETAILS SUBMITTED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          // this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  onClickEndOnsiteActivities() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Onsite Activities?', 'You won\'t be able to revert back/Update Onsite Details after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update File(s) Before Saving', 'ONSITE ACTIVITIES ENDED SUCCESSFUL', () => {
          this.endOnsiteActivities();
        });
  }

  onClickSaveEndSSFAddingBsNumber() {
    this.msService.showSuccessWith2Message('Are you sure your want to End BS Number Adding?', 'You won\'t be able to revert Update BS Number(s) after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update Bs Number Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.endSSFAddingBsNumber();
        });
  }

  onClickEndAddingRecommendationHOD() {

    this.msService.showSuccessWith2Message('Are you sure your want to End Adding Of Recommendation?', 'You won\'t be able to revert back/Update Recommendation  Details after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update Details Before Saving', 'ONSITE ACTIVITIES ENDED SUCCESSFUL', () => {
          this.endEndAddingRecommendationHOD();
        });
  }

  onClickEndAddingRecommendationDirector() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Adding Of Recommendation/Remarks?', 'You won\'t be able to revert back/Update Recommendation/Remarks  Details after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update Details Before Saving', 'ONSITE ACTIVITIES ENDED SUCCESSFUL', () => {
          this.endEndAddingRecommendationDirector();
        });
  }

  onClickSaveEndRecommendationDone() {
    this.msService.showSuccessWith2Message('Are you sure your have done all Recommendation for Each Product?', 'You won\'t be able to revert after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and Finnish all Recommendation Needed Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.endRecommendationDone();
        });
  }


  endRecommendationDone() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
    this.msService.msWorkPlanScheduleDetailsEndRecommendationDone(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('ALL RECOMMENDATION HAVE BEEN MARKED AS DONE AND DETAILS SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  endOnsiteActivities() {
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
          this.msService.showSuccess('SCHEDULE ON-SITE DETAILS SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  endEndAddingRecommendationHOD() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
    this.msService.msWorkPlanScheduleDetailsEndAddingRecommendationHOD(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('RECOMMENDATION DETAILS SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  endEndAddingRecommendationDirector() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
    this.msService.msWorkPlanScheduleDetailsEndAddingRecommendationDirector(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('RECOMMENDATION DETAILS SAVED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  endSSFAddingBsNumber() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
    this.msService.msWorkPlanScheduleDetailsEndSSFAddingBsNumber(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('BS NUMBER ADDING ENDED SUCCESSFUL');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
  }

  onClickAddResource() {
    this.dataSaveResourcesRequired = this.addResourceRequiredForm.value;
    console.log(this.dataSaveResourcesRequired);
    // tslint:disable-next-line:max-line-length
    const  resourceName = this.dataSaveResourcesRequiredList.filter(x => String(this.dataSaveResourcesRequired.resourceName) === String(x.resourceName)).length;
    if (resourceName > 0) {
      console.log('ResourceFound =' + this.dataSaveResourcesRequired.resourceName);
      this.msService.showWarning('You have already added ' + this.dataSaveResourcesRequired.resourceName);
    } else {
      this.dataSaveResourcesRequiredList.push(this.dataSaveResourcesRequired);
      console.log('ResourceFound Not Found =' + this.dataSaveResourcesRequired.resourceName);
    }

    this.addResourceRequiredForm?.get('resourceName')?.reset();
  }

  // Remove Form repeater values
  removeDataResource(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveResourcesRequiredList.splice(index, 1);
    } else {
      this.dataSaveResourcesRequiredList.splice(index, index);
    }
  }


  onClickSaveAssignHof(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msWorkPlanInspectionScheduledUpdateAssignHOFDetails(this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,  this.dataSaveAssignOfficer).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('HOF ASSIGNED/RE-ASSISGNED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveAssignIO(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msWorkPlanInspectionScheduledUpdateAssignIODetails(this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,  this.dataSaveAssignOfficer).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('IO RE-ASSIGNED SUCCESSFULLY');
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
    if (this.uploadedFilesOnly.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Files Selected?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and click the button to update File(s) Before Saving', 'FILE(S) UPLOADED SUCCESSFUL', () => {
            this.saveFilesResults(docTypeName);
          });
    } else {
      this.msService.showError('NO FILE SELECTED FOR UPLOADED');
    }

  }

  onClickSaveFinalReportOfficerResults() {
    if (this.uploadedFilesOnly.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Files(Final Report) Selected?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and click the button to update File(s) Before Saving', 'FILE(S) UPLOADED SUCCESSFUL', () => {
            this.saveFilesUploadFinalReportResults();
          });
    } else {
      this.msService.showError('NO FILE SELECTED FOR UPLOADED');
    }

  }

  onClickSaveFinalReportHOFHODResults() {
    if (this.uploadedFilesOnly.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Files(Final Report) Selected?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and click the button to update File(s) Before Saving', 'FILE(S) UPLOADED SUCCESSFUL', () => {
            this.saveFilesUploadFinalReportResultsHOFHOD();
          });
    } else {
      this.msService.showError('NO FILE SELECTED FOR UPLOADED');
    }

  }

  saveFilesUploadFinalReportResults() {
    if (this.uploadedFilesOnly.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFilesOnly;
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
        // this.uploadedFiles.item(i).slice();
      }
      this.msService.saveWorkPlanFilesFinalReportIO(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFilesOnly = this.resetUploadedFiles;
            console.log(data);
            // this.loadStandards();
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT FILE(S) UPLOADED AND SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  saveFilesUploadFinalReportResultsHOFHOD() {
    if (this.uploadedFilesOnly.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFilesOnly;
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
        // this.uploadedFiles.item(i).slice();
      }
      this.msService.saveWorkPlanFilesFinalReportHOFHOD(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFilesOnly = this.resetUploadedFiles;
            console.log(data);
            // this.loadStandards();
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT FILE(S) UPLOADED AND SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  saveFilesResults(docTypeName: string) {
    if (this.uploadedFilesOnly.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFilesOnly;
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      formData.append('docTypeName', docTypeName);
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
        // this.uploadedFiles.item(i).slice();
      }
      this.msService.saveWorkPlanFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFilesOnly = this.resetUploadedFiles;
            console.log(data);
            // this.loadStandards();
            this.SpinnerService.hide();
            this.msService.showSuccess('FILE(S) UPLOADED SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }





  onClickSaveUploadedDestructionReport(docTypeName: string) {
    if (this.uploadDestructionReportFiles.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Files Selected?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and click the button to update File(s) Before Saving', 'FILE(S) UPLOADED SUCCESSFUL', () => {
            this.saveUploadedDestructionReport(docTypeName);
          });
    } else {
      this.msService.showError('NO FILE SELECTED FOR UPLOADED');
    }
  }

  saveUploadedDestructionReport(docTypeName: string) {
    if (this.uploadDestructionReportFiles.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadDestructionReportFiles;
      const formData = new FormData();
      formData.append('productReferenceNo', this.selectedProductRecommendation?.referenceNo );
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      formData.append('docTypeName', docTypeName);
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.msService.saveWorkPlanDestructionReportFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadDestructionReportFiles = null;
            console.log(data);
            this.recommendationDetailsLoad();
            this.SpinnerService.hide();
            this.msService.showSuccess('DESTRUCTION REPORT FILE(S) UPLOADED AND SAVED SUCCESSFULLY');
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


  onClickSaveFilesDestructionNotification(valid: boolean, docTypeName: string) {
    if (this.uploadedFilesDestination.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Files Selected?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and click the button to update File(s) Before Saving', 'FILE(S) UPLOADED SUCCESSFUL', () => {
            this.saveFilesDestructionNotification(valid, docTypeName);
          });
    } else {
      this.msService.showError('NO FILE IS UPLOADED FOR SAVING');
    }
  }

  saveFilesDestructionNotification(valid: boolean, docTypeName: string) {
    if (this.uploadedFilesDestination.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFilesDestination;
      this.dataSaveDestructionNotification = {...this.dataSaveDestructionNotification, ...this.clientEmailNotificationForm.value};
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection?.referenceNumber);
      formData.append('productReferenceNo', this.selectedProductRecommendation?.referenceNo );
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      formData.append('data', JSON.stringify(this.dataSaveDestructionNotification));
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.msService.saveWorkPlanDestructionNotificationFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFilesDestination = null;
            console.log(data);
            this.clientEmailNotificationForm.reset();
            this.recommendationDetailsLoad();
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

  onClickEndSampleSubmitted() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Sample Submission?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD SAMPLE SUBMISSION\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
          this.endSampleSubmitted();
        });
  }

  onClickEndDataReportAdding() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Data Report Adding?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD DATA REPORT\' button to update details Before Saving', 'DATA REPORT DETAILS SAVED SUCCESSFUL', () => {
          this.endDataReportAdding();
        });
  }

  endSampleSubmitted() {
    this.SpinnerService.show();
    this.msService.msWorkPlanScheduleEndSampleSubmitted(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('SAMPLE SUBMISSION ENDED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  endDataReportAdding() {
    this.SpinnerService.show();
    this.msService.msWorkPlanScheduleEndDataReportAdding(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('DATA REPORT ENDED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }


  onClickSaveSampleSubmitted() {
    this.submitted = true;
    let valuesToShow = '\'ADD SAMPLE SUBMISSION\'';
    if (this.sampleSubmitForm.get('id').value !== null) {
      valuesToShow = '\'UPDATE SSF DETAILS\'';
    }

    const standardsArrayControl = this.sampleSubmitForm.get('referencesStandards');
    console.log("Data in the form control before: "+standardsArrayControl.value);
    const newValues = [];
    for (let selectedStandard of this.standardsArray) {
      const valueInControl = standardsArrayControl.value;
      if (!valueInControl.includes(selectedStandard)) {
        newValues.push(selectedStandard);
      }
    }

    if (newValues.length > 0) {
      const updatedValue = standardsArrayControl.value.concat(newValues);
      standardsArrayControl.patchValue(updatedValue);
    }

    console.log("Data in the form control after: "+standardsArrayControl.value);
    if (this.sampleSubmitForm.valid && this.dataSaveSampleSubmitParamList.length !== 0 && this.standardsArray.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click the${valuesToShow}button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
            this.saveSampleSubmitted();
          });
    }
    else{
      this.msService.showError("Please Fill in all the fields!");
    }
  }

  saveSampleSubmitted() {
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
    this.submitted = true;
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'ADD BS NUMBER\' button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
            this.saveBSNumber(valid);
          });
    } else {
      this.msService.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
    }
  }


  saveBSNumber(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveSampleSubmitBSNumber = {...this.dataSaveSampleSubmitBSNumber, ...this.sampleSubmitBSNumberForm.value};
      this.msService.msWorkPlanInspectionScheduledAddSampleSubmissionBSNumber(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveSampleSubmitBSNumber).subscribe(
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
    this.submitted = true;
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the PDF?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'SAVE PDF\' button to updated the Details before saving`, 'PDF SAVED SUCCESSFUL', () => {
            this.savePDFSelected(valid);
          });
    } else {
      this.msService.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
    }
  }

  savePDFSelected(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataPDFSaveComplianceStatus = {...this.dataPDFSaveComplianceStatus, ...this.pdfSaveComplianceStatusForm.value};
      this.dataPDFSaveComplianceStatus.ssfID = this.selectedLabResults.ssfResultsList.sffId;
      this.dataPDFSaveComplianceStatus.bsNumber = this.selectedLabResults.ssfResultsList.bsNumber;
      this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      if (this.selectedLabResults.savedPDFFiles.length === 0) {
        this.msService.msWorkPlanInspectionScheduledSavePDFLIMS(this.workPlanInspection.batchDetails.referenceNumber,
            this.workPlanInspection.referenceNumber, this.dataPDFSaveComplianceStatus).subscribe(
            (data: any) => {
              this.workPlanInspection = data;
              // tslint:disable-next-line:max-line-length
              this.selectedLabResults = this.workPlanInspection.sampleLabResults.find(lab => lab.ssfResultsList.bsNumber === this.dataPDFSaveComplianceStatus.bsNumber);
              this.pdfSaveComplianceStatusForm.reset();
              console.log(data);
              this.SpinnerService.hide();
              this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY');
              window.$('#myModal2').modal('hide');
              // window.$('.modal').remove();
              window.$('body').removeClass('modal-open');
              window.$('.modal-backdrop').remove();
              window.$('#sampleLabResultsModal').modal('hide');
              this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY', () => {
                this.viewSSFLabResultsRecord(this.selectedSSFDetails);
              });
            },
            error => {
              this.SpinnerService.hide();
              console.log(error);
              this.msService.showError('AN ERROR OCCURRED');
            },
        );
      } else {
        const savedPdf  = this.selectedLabResults.savedPDFFiles.find(pdf => pdf.pdfName === this.selectedPDFFileName);
        if (savedPdf === null) {
          this.msService.msWorkPlanInspectionScheduledSavePDFLIMS(this.workPlanInspection.batchDetails.referenceNumber,
              this.workPlanInspection.referenceNumber, this.dataPDFSaveComplianceStatus).subscribe(
              (data: any) => {
                this.workPlanInspection = data;
                // tslint:disable-next-line:max-line-length
                this.selectedLabResults = this.workPlanInspection.sampleLabResults.find(lab => lab.ssfResultsList.bsNumber === this.dataPDFSaveComplianceStatus.bsNumber);
                this.pdfSaveComplianceStatusForm.reset();
                console.log(data);
                this.SpinnerService.hide();
                window.$('#myModal2').modal('hide');
                // window.$('.modal').remove();
                window.$('body').removeClass('modal-open');
                window.$('.modal-backdrop').remove();
                window.$('#sampleLabResultsModal').modal('hide');
                this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY', () => {
                  this.viewSSFLabResultsRecord(this.selectedSSFDetails);
                });
              },
              error => {
                this.SpinnerService.hide();
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
              },
          );
        } else {
          // tslint:disable-next-line:max-line-length
          this.selectedLabResults = this.workPlanInspection.sampleLabResults.find(lab => lab.ssfResultsList.bsNumber === this.dataPDFSaveComplianceStatus.bsNumber);
          this.pdfSaveComplianceStatusForm.reset();
          window.$('#myModal2').modal('hide');
          window.$('#sampleLabResultsModal').modal('hide');
          // window.$('.modal').remove();
          window.$('body').removeClass('modal-open');
          window.$('.modal-backdrop').remove();
          this.SpinnerService.hide();
          this.msService.showError('The Pdf selected With Name ' + this.selectedPDFFileName + ' Already Saved',
              () => {
                this.viewSSFLabResultsRecord(this.selectedSSFDetails);
              });
          // window.$('#sampleLabResultsModal').modal('show');
        }

      }
    }
  }

  onClickCloseSSF() {
    this.sampleSubmitForm.reset();
    this.sampleSubmitForm.enable();
    this.addLabParamStatus = true;
    this.dataSaveSampleSubmitParamList = [];
  }

  onClickSaveSSFLabResultsComplianceStatus(valid: boolean) {
    this.submitted = true;
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'ADD LAB RESULTS STATUS\' button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
            this.saveSSFLabResultsComplianceStatus(valid);
          });
    }
  }

  onClickSendSSFLabResultsComplianceStatus(valid: boolean) {
    this.submitted = true;
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Send the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'SEND RESULTS\' button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
            this.sendSSFLabResultsComplianceStatus(valid);
          });
    }
  }

  sendSSFLabResultsComplianceStatus(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSSFSendComplianceStatus = {...this.dataSSFSendComplianceStatus, ...this.ssfClientEmailNotificationForm.value};
      this.dataSSFSendComplianceStatus.ssfID = this.selectedLabResults?.ssfResultsList?.sffId;
      // this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      this.msService.msWorkPlanInspectionScheduledSendSSFComplianceStatus(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSSFSendComplianceStatus,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.closeSSFLabResultsRecord();
            this.msService.showSuccess('SSF LAB RESULTS SENT SUCCESSFULLY', () => {
              this.viewSSFLabResultsRecord(this.selectedSSFDetails);
            });
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }


  saveSSFLabResultsComplianceStatus(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSSFSaveComplianceStatus = {...this.dataSSFSaveComplianceStatus, ...this.ssfSaveComplianceStatusForm.value};
      this.dataSSFSaveComplianceStatus.ssfID = this.selectedLabResults?.ssfResultsList?.sffId;
      this.dataSSFSaveComplianceStatus.bsNumber = this.selectedLabResults?.ssfResultsList?.bsNumber;
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
            this.ssfSaveComplianceStatusForm.reset();
            this.closeSSFLabResultsRecord();
            this.msService.showSuccess('SSF COMPLIANCE STATUS SAVED SUCCESSFULLY', () => {
              this.viewSSFLabResultsRecord(this.selectedSSFDetails);
            });
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveFinalLabComplianceStatus(valid: boolean) {
    this.submitted = true;
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'ADD FINAL LAB RESULTS STATUS\' button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
            this.saveFinalLabResultsComplianceStatus(valid);
          });
    }
  }

  saveFinalLabResultsComplianceStatus(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSSFSaveComplianceStatus = {...this.dataSSFSaveComplianceStatus, ...this.ssfSaveComplianceStatusForm.value};
      this.dataSSFSaveComplianceStatus.ssfID = 0;
      this.dataSSFSaveComplianceStatus.bsNumber = 'TEST BS NUMBER';
      // this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      this.msService.msWorkPlanInspectionScheduledSaveFinalSSFComplianceStatus(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSSFSaveComplianceStatus,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('SSF COMPLIANCE STATUS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }


  goBack() {
    console.log('TEST 101' + this.workPlanInspection.batchDetails.referenceNumber);
    this.router.navigate([`/workPlan`, this.workPlanInspection.batchDetails.referenceNumber]);
  }

  viewWorkPlanFileSaved(data: ComplaintsFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
  }

  viewUploadsFileSaved(data: WorkPlanFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
  }

  viewSeizedProductsFileSaved(data: SeizureListDto) {
    const foundPdfFile = this.workPlanInspection?.workPlanFiles.find(lab => lab.id === data.docID);
    this.viewPdfFile(String(foundPdfFile.id), foundPdfFile.documentType, foundPdfFile.fileContentType);
  }

  // viewWorkPlanFileNotOnSiteSaved(fileID: number) {
  //   const data = this.workPlanInspection.workPlanFiles.filter(function (record) {return record.id === fileID});
  //   this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
  // }

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
          // this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }


  viewLIMSPDFRecord(data: LIMSFilesFoundDto, bsNumber: string) {
    console.log('TEST 101 REF NO VIEW: ' + data.fileName);
    this.viewLabResultsPdfFile(String(data.fileName), bsNumber, 'application/pdf');
  }

  viewLIMSPDFSaved(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);

    this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
  }

  updateSeizedDetails(data: SeizureListDto) {
    this.seizureForm.patchValue(data);
    this.selectedSeizedDetails = data;
    const paramDetails = data.seizureList;
    this.dataSaveSeizureDeclarationList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSeizureDeclarationList.push(paramDetails[i]);
    }
    this.addSeizureProductsStatus = true;
    this.seizureForm.enable();
    window.$('#seizureDeclarationModal').modal('show');
  }

  viewPreliminaryReportDetails(data: InspectionInvestigationReportDto) {
    this.seizureForm.patchValue(data);
    this.selectedPreliminaryReportDetails = data;

    window.$('#msProgressReportViewModal').modal('show');
  }

  updatePreliminaryReportDetails(data: InspectionInvestigationReportDto) {
    this.seizureForm.patchValue(data);
    this.selectedPreliminaryReportDetails = data;
    // const paramDetails = data.seizureList;
    // this.dataSaveSeizureDeclarationList = [];
    // for (let i = 0; i < paramDetails.length; i++) {
    //   this.dataSaveSeizureDeclarationList.push(paramDetails[i]);
    // }
    // this.seizureForm.disable();
    // this.addSeizureProductsStatus = false;
    window.$('#seizureDeclarationModal').modal('show');
  }

  viewSeizedDetails(data: SeizureListDto) {
    this.seizureForm.patchValue(data);
    this.selectedSeizedDetails = data;
    const paramDetails = data.seizureList;
    this.dataSaveSeizureDeclarationList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSeizureDeclarationList.push(paramDetails[i]);
    }
    this.seizureForm.disable();
    this.addSeizureProductsStatus = false;
    window.$('#seizureDeclarationModal').modal('show');
  }

  viewLIMSPDFSavedRemarks(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);
    this.selectedPDFFileName = data.pdfName;
    this.currDivLabel = `COMPLIANCE STATUS AND REMARKS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'viewPdfSaveCompliance';
    this.pdfSaveComplianceStatusForm.patchValue(data);

    window.$('#myModal2').modal('show');
  }

  viewSavedRemarks(data: MSRemarksDto) {
    this.currDivLabel = `REMARKS FOR ${data.processName}`;
    this.currDiv = 'viewSavedRemarks';
    this.remarksSavedForm.patchValue(data);

    window.$('#myModal1').modal('show');
    // this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/workPlanInspection/details/`,data.referenceNumber]);
  }

  viewSavedDataReportRemarks(data: DataReportParamsDto) {
    this.currDivLabel = `REMARKS FOR ${data.typeBrandName} BRAND`;
    this.currDiv = 'viewSavedDataReportRemarks';
    this.remarksSavedForm.patchValue(data);

    window.$('#myModal1').modal('show');
  }

  saveLIMSPDFRecord(data: LIMSFilesFoundDto) {
    console.log('TEST 101 REF NO SAVE: ' + data.fileName);
    this.selectedPDFFileName = data.fileName;
    this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'pdfSaveCompliance';

    window.$('#myModal2').modal('show');
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

  public onCustomSEIZEDAction(event: any): void {
    switch (event.action) {
      case 'updateRecords':
        this.updateSeizedDetails(event.data);
        break;
      case 'viewRecord':
        this.viewSeizedDetails(event.data);
        break;
      case 'viewUpload':
        this.viewSeizedProductsFileSaved(event.data);
        break;
    }
  }

  public onCustomPreliminaryReportAction(event: any): void {
    switch (event.action) {
      case 'updateRecords':
        this.updateSeizedDetails(event.data);
        break;
      case 'viewRecord':
        this.viewPreliminaryReportDetails(event.data);
        break;
      case 'viewUpload':
        this.viewSeizedProductsFileSaved(event.data);
        break;
    }
  }

  public onCustomRemarksViewAction(event: any): void {
    switch (event.action) {
      case 'viewSavedRemarks':
        this.viewSavedRemarks(event.data);
        break;
      case 'viewSavedDataReportRemarks':
        this.viewSavedDataReportRemarks(event.data);
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

  checkProductCompliance(permit: string) {
    console.log(permit);
    this.dataSaveDataReportParam = this.dataReportParamForm.value;
    const valueSelected = this.dataReportParamForm?.get('localImport')?.value;
    switch (valueSelected) {
      case 'Local':
        this.msService.loadPermitDetailsSearch(this.dataSaveDataReportParam.permitNumber).subscribe(
            (data: PermitUcrSearch) => {
              this.SpinnerService.hide();
              this.verificationPermitForm.patchValue(data);
              this.currDivLabel = `PERMIT FOUND WITH FOLLOWING DETAILS`;
              this.currDiv = 'verificationPermitDetails';
              this.verificationPermitForm.disabled;

              window.$('#myModal3').modal('show');

              // this.msService.showSuccess('DATA REPORT DETAILS SAVED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();

              console.log(error);
              // this.msService.showError('AN ERROR OCCURRED');
            },
        );
        break;
      case 'Import':
        this.msService.loadUCRDetailsSearch(this.dataSaveDataReportParam.ucrNumber).subscribe(
            (data: any) => {
              this.SpinnerService.hide();
              // this.msService.showSuccess('DATA REPORT DETAILS SAVED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();
              console.log(error);
              // this.msService.showError('AN ERROR OCCURRED');
            },
        );
        break;
    }
  }

  onClickAddDataReportParam() {
    this.dataSaveDataReportParam = this.dataReportParamForm.value;




    this.dataSaveDataReportParamList.push(this.dataSaveDataReportParam);
    this.dataReportParamForm?.get('productName')?.reset();
    this.dataReportParamForm?.get('typeBrandName')?.reset();
    this.dataReportParamForm?.get('localImport')?.reset();
    this.dataReportParamForm?.get('permitNumber')?.reset();
    this.dataReportParamForm?.get('permitNumber')?.setValue('N/A');
    this.dataReportParamForm?.get('ucrNumber')?.reset();
    this.dataReportParamForm?.get('ucrNumber')?.setValue('N/A');
    this.dataReportParamForm?.get('complianceInspectionParameter')?.reset();
    this.dataReportParamForm?.get('measurementsResults')?.reset();
    this.dataReportParamForm?.get('remarks')?.reset();
    const totalCount: number = this.dataSaveDataReportParamList.length;
    let compliantCount = 0;

    for (let i = 0; i < totalCount; i++) {
      if (Number(this.dataSaveDataReportParamList[i].complianceInspectionParameter) === 100) {
        compliantCount = compliantCount + 1;
      }
    }

    this.totalCompliantValue = (compliantCount / totalCount) * 100;
    this.isImport = 0;
    console.log('compliant count' + compliantCount);
    console.log('total count' + totalCount);
    console.log('compliance Value count' + this.totalCompliantValue);
  }


  onClickAddSeizureItems() {
    this.dataSaveSeizureDeclaration = this.seizureDeclarationForm.value;
    this.dataSaveSeizureDeclarationList.push(this.dataSaveSeizureDeclaration);
    this.seizureDeclarationForm?.reset();
  }

  removeSeizureProducts(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveSeizureDeclarationList.splice(index, 1);
    } else {
      this.dataSaveSeizureDeclarationList.splice(index, index);
    }
  }


  onClickAddDataInspectorOfficer() {
    this.dataSaveDataInspectorInvest = this.investInspectReportInspectorsForm.value;
    this.dataSaveDataInspectorInvestList.push(this.dataSaveDataInspectorInvest);
    this.investInspectReportInspectorsForm?.get('inspectorName')?.reset();
    this.investInspectReportInspectorsForm?.get('institution')?.reset();
    this.investInspectReportInspectorsForm?.get('designation')?.reset();
    // this.sta10FormA.reset();
  }

  onClickAddActionOnSiezedGoods() {
    this.dataSaveActionOnSiezedGoods = this.actionOnSiezedGoodsForm.value;
    this.dataSaveActionOnSiezedGoodsList.push(this.dataSaveActionOnSiezedGoods);
    this.actionOnSiezedGoodsForm?.get('actionOnSeizedGoodsDetails')?.reset();
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

  onClickAddDataSeizure() {
    this.dataSaveSeizureDeclaration = this.seizureDeclarationForm.value;
    this.dataSaveSeizureDeclarationList.push(this.dataSaveSeizureDeclaration);
    this.seizureDeclarationForm.reset();
  }

  onClickAddDataSampleSubmitParams() {
    this.dataSaveSampleSubmitParam = this.sampleSubmitParamsForm.value;
    this.dataSaveSampleSubmitParamList.push(this.dataSaveSampleSubmitParam);
    this.sampleSubmitParamsForm?.get('parameters')?.reset();
    // this.sampleSubmitParamsForm?.get('laboratoryName')?.reset();
  }

  removeDataReportParam(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveDataReportParamList.splice(index, 1);
    } else {
      this.dataSaveDataReportParamList.splice(index, index);
    }

    const totalCount: number = this.dataSaveDataReportParamList.length;
    let compliantCount = 0;

    for (let i = 0; i < totalCount; i++) {
      if (Number(this.dataSaveDataReportParamList[i].complianceInspectionParameter) === 100) {
        compliantCount = compliantCount + 1;
      }
    }

    this.totalCompliantValue = (compliantCount / totalCount) * 100;
    console.log('compliant count' + compliantCount);
    console.log('total count' + totalCount);
    console.log('complinace Value count' + this.totalCompliantValue);
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
  removeDataSeizureDeclarationList(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveSeizureDeclarationList.splice(index, 1);
    } else {
      this.dataSaveSeizureDeclarationList.splice(index, index);
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
  removeDataActionOnSiezedGoods(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveActionOnSiezedGoodsList.splice(index, 1);
    } else {
      this.dataSaveActionOnSiezedGoodsList.splice(index, index);
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
    if (this.uploadedFilesDataReport) {
      for ( let i = 0; i < this.uploadedFilesDataReport.length; i++) {
        this.arrayOfUploadedDataReportFiles.push(this.uploadedFilesDataReport[i]);
      }
    }
    this.submitted = true;
    if (this.dataReportForm.valid && this.dataSaveDataReportParamList.length !== 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD DATA REPORT\' button to update details Before Saving', 'DATA REPORT DETAILS SAVED SUCCESSFUL', () => {
            this.saveDataReport();
          });
    } else {
      this.msService.showError('Parameter List Is Empty');
    }
  }

  saveDataReport() {
    if (this.dataReportForm.valid && this.dataSaveDataReportParamList.length !== 0) {
      this.SpinnerService.show();

      this.dataSaveDataReport = {...this.dataSaveDataReport, ...this.dataReportForm.value};
      this.dataSaveDataReport.productsList = this.dataSaveDataReportParamList;
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber);
      formData.append('docTypeName', 'DATA_REPORT_UPLOAD');
      formData.append('data', JSON.stringify(this.dataSaveDataReport));
      if(this.uploadedFilesDataReport?.length > 0 ){
        const file = this.uploadedFilesDataReport;
        for (let i = 0; i < file.length; i++) {
          console.log(file[i]);
          formData.append('docFile', file[i], file[i].name);
        }
      }

      this.msService.msWorkPlanScheduleSaveDataReport(formData).subscribe(
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

  onClickEndSeizureDeclaration() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Seizure Adding Of Goods?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD SEIZED GOODS\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
          this.endSeizureDeclaration();
        });
  }

  onClickSaveSeizureDeclaration() {
    this.submitted = true;
    if (this.seizureForm.valid && this.uploadedFilesSeizedGoods?.length > 0 && this.dataSaveSeizureDeclarationList.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD SEIZED GOODS\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
            this.saveSeizureDeclaration();
          });
    } else {
      this.msService.showError('Fill in all the fields! (Make sure you\'ve uploaded a file)');
    }
  }

  saveSeizureDeclaration() {
    this.SpinnerService.show();
    const file = this.uploadedFilesSeizedGoods;
    this.dataSaveSeizure = {...this.dataSaveSeizure, ...this.seizureForm.value};
    this.dataSaveSeizure.seizureList = this.dataSaveSeizureDeclarationList;
    const formData = new FormData();
    formData.append('referenceNo', this.workPlanInspection.referenceNumber);
    formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber);
    formData.append('docTypeName', 'SEIZURE_UPLOAD');
    formData.append('data', JSON.stringify(this.dataSaveSeizure));
    for (let i = 0; i < file.length; i++) {
      console.log(file[i]);
      formData.append('docFile', file[i], file[i].name);
    }
    this.msService.msWorkPlanScheduleSaveSeizureDeclarationWithUpload(formData).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          this.workPlanInspection.currentDate = new Date();
          console.log('currentdate' + this.workPlanInspection.currentDate);
          this.uploadedFilesSeizedGoods = null;
          this.seizureForm.reset();
          this.myForm.reset();
          this.dataSaveSeizureDeclarationList = [];
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

  endSeizureDeclaration() {
    this.SpinnerService.show();
    this.msService.msWorkPlanScheduleEndSeizureDeclaration(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('SEIZURE AND DECLARATION ENDED SUCCESSFULLY');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  onClickSaveInvestInspectReport() {
    this.submitted = true;
    this.msService.showSuccessWith2Message('ARE YOU SURE YOU WANT TO SAVE THE INITIAL REPORT?', 'You can still update it later.',
        'You can click the \'ADD INITIAL REPORT\' button to update details Before Saving', 'INITIAL REPORT DETAILS SAVED SUCCESSFUL', () => {
          this.saveInitialReport();
        });

  }

  saveInitialReport(){
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
            this.msService.showSuccess('FIELD REPORT DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    } else if (this.investInspectReportForm.invalid) {
      this.msService.showError("PLEASE FILL IN ALL THE REQUIRED FIELDS");
    }
  }

  onClickSavePreliminaryReportWhichWasDataReport() {
    this.submitted = true;
    if (this.investInspectReportForm.valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD/UPDATE PROGRESS REPORT\' button to update details Before Saving', 'FIELD REPORT DETAILS SAVED SUCCESSFUL', () => {
            this.savePreliminaryReportWhichWasDataReport();
          });
    }
  }

  onClickUpdateHODHOFPreliminaryReportWhichWasDataReport() {
    this.submitted = true;
    if (this.investInspectReportForm.valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Update the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'UPDATE PROGRESS REPORT\' button to update details Before Saving', 'FIELD REPORT DETAILS SAVED SUCCESSFUL', () => {
            this.updatePreliminaryReportWhichWasDataReport();
          });
    }
  }

  savePreliminaryReportWhichWasDataReport() {
    this.submitted = true;

    if (this.investInspectReportForm.valid) {
      this.SpinnerService.show();
      this.dataSaveInvestInspectReport = {...this.dataSaveInvestInspectReport, ...this.investInspectReportForm.value};
      this.dataSaveInvestInspectReport.kebsInspectors = this.dataSaveDataInspectorInvestList;
      this.dataSaveInvestInspectReport.bsNumbersList = this.dataSaveBsNumber;
      this.msService.msWorkPlanScheduleSavePreliminaryReportData(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveInvestInspectReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('PROGRESS REPORT DETAILS SAVED SUCCESSFULLY');
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

  updatePreliminaryReportWhichWasDataReport() {
    this.submitted = true;

    if (this.investInspectReportForm.valid) {
      this.SpinnerService.show();
      this.dataSaveInvestInspectReport = {...this.dataSaveInvestInspectReport, ...this.investInspectReportForm.value};
      this.dataSaveInvestInspectReport.kebsInspectors = this.dataSaveDataInspectorInvestList;
      this.dataSaveInvestInspectReport.bsNumbersList = this.dataSaveBsNumber;
      this.msService.msWorkPlanScheduleUpdatePreliminaryReportData(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveInvestInspectReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('PROGRESS REPORT DETAILS UPDATED SUCCESSFULLY');
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

  onClickSaveFieldReportAdditionalInfort() {
    this.submitted = true;
    if (this.fieldReportAdditionalInfortForm.valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'UPDATE FIELD REPORT\' button to update details Before Saving', 'FIELD REPORT DETAILS SAVED SUCCESSFUL', () => {
            this.saveFieldReportAdditionalInform();
          });
    }
  }

  saveFieldReportAdditionalInform() {
    this.submitted = true;

    if (this.fieldReportAdditionalInfortForm.valid) {
      this.SpinnerService.show();
      const file = this.uploadedFiles;
      this.dataSaveFieldReportAdditional = {...this.dataSaveFieldReportAdditional, ...this.fieldReportAdditionalInfortForm.value};
      this.dataSaveFieldReportAdditional.actionOnSeizedGoods = this.dataSaveActionOnSiezedGoodsList;
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber);
      formData.append('docTypeName', 'FIELD_REPORT_UPLOAD');
      formData.append('data', JSON.stringify(this.dataSaveFieldReportAdditional));
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.msService.saveFieldReportAdditionalInform(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FIELD REPORT UPDATED DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    } else if (this.preliminaryReportForm.invalid) {
      this.msService.showError('KINDLY FILL IN THE FIELDS DETAILS REQUIRED');
    }
  }


  onClickSavePreliminaryReport() {
    this.submitted = true;
    if (this.preliminaryReportForm.valid && this.dataSavePreliminaryReportParamList.length !== 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD PRELIMINARY REPORT\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
            this.savePreliminaryReport();
          });
    }
  }

  savePreliminaryReport() {
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

  onClickSaveFinalPreliminaryReport() {
    this.submitted = true;
    if (this.preliminaryReportFinalForm.valid && this.dataSavePreliminaryReportParamList.length !== 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD FINAL REPORT\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
            this.saveFinalPreliminaryReport();
          });
    }
  }

  saveFinalPreliminaryReport() {
    this.submitted = true;

    if (this.preliminaryReportFinalForm.valid && this.dataSavePreliminaryReportParamList.length !== 0) {
      this.SpinnerService.show();
      this.dataSavePreliminaryReport = {...this.dataSavePreliminaryReport, ...this.preliminaryReportFinalForm.value};
      this.dataSavePreliminaryReport.kebsOfficersName = this.dataSaveDataInspectorInvestList;
      this.dataSavePreliminaryReport.parametersList = this.dataSavePreliminaryReportParamList;
      if (!this.workPlanInspection?.finalReportGenerated) {
        // @ts-ignore
        this.dataSavePreliminaryReport?.id = -1;
        for (let i = 0; i < this.dataSavePreliminaryReport.parametersList.length; i++) {
          // tslint:disable-next-line:no-non-null-assertion
          this.dataSavePreliminaryReport!.parametersList[i]!.id = -1;
        }
      }

      this.msService.msWorkPlanScheduleSaveFinalPreliminaryReport(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSavePreliminaryReport,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FINAL REPORT DETAILS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            // this.msService.showError('AN ERROR OCCURRED');
          },
      );

    } else if (this.preliminaryReportForm.invalid) {
      this.msService.showError('KINDLY FILL IN THE FIELDS AND OUTLET DETAILS REQUIRED');
    }
  }

  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
    }
  }

  onChangeSelectedImportType() {

    const valueSelected = this.dataReportParamForm?.get('localImport')?.value;

    switch (valueSelected) {
      case 'Local':
        this.isImport = 1;
        this.dataReportParamForm?.get('permitNumber')?.reset();
        this.dataReportParamForm?.get('ucrNumber')?.setValue('N/A');
        break;
      case 'Import':
        this.isImport = 2;
        this.dataReportParamForm?.get('ucrNumber')?.reset();
        this.dataReportParamForm?.get('permitNumber')?.setValue('N/A');
        break;
    }

  }


  onChangeSelectedDepartment() {
    // this.disableDivision = false;
    this.addNewScheduleForm.controls.divisionId.enable();
    this.departmentSelected = this.addNewScheduleForm?.get('complaintDepartment')?.value;
    this.standardProductCategorySelected = this.addNewScheduleForm?.get('standardCategory')?.value;
    this.broadProductCategorySelected = this.addNewScheduleForm?.get('broadProductCategory')?.value;
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedProductClassification() {
    this.standardProductCategorySelected = this.addNewScheduleForm?.get('standardCategory')?.value;
    this.broadProductCategorySelected = this.addNewScheduleForm?.get('broadProductCategory')?.value;
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedBroadProductCategory() {
    this.broadProductCategorySelected = this.addNewScheduleForm?.get('broadProductCategory')?.value;
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedProductCategory() {
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedMyProduct() {
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  updateSelectedRegion() {
    this.selectedRegion = this.addCountyTownForm?.get('regionId')?.value;
    this.selectedRegionName = this.msRegions.find(pr => pr.id === this.selectedRegion)?.region;
    this.addCountyTownForm.controls.countyId.enable();
    // this.msCounties = this.msCounties.sort((a, b) => a.county > b.county ? 1 : -1);
    // this.msCounties = this.msCounties.filter(x => String(this.dataSaveResourcesRequired.resourceName) === String(x.regionId));
    console.log(`region set to ${this.selectedRegion}`);
  }


  updateSelectedCounty() {
    this.selectedCounty = this.addCountyTownForm?.get('countyId')?.value;
    this.selectedCountyName = this.msCountiesList.find(pr => pr.id === this.selectedCounty)?.county;
    this.addCountyTownForm.controls.townsId.enable();
    console.log(`county selectedCountyName to ${this.selectedCountyName}`);
    console.log(`county set to ${this.selectedCounty}`);
    this.msTowns = this.msTowns.filter(x => String(this.selectedCounty) === String(x.countyId));
    console.log(`towns list set to ${this.msTowns}`);
    // this.msTowns = this.msTowns.sort((a, b) => a.town > b.town ? 1 : -1);
    // this.msService.msCountiesListDetails().subscribe(
    //     (dataCounties: County[]) => {
    //       this.selectedCountyName = dataCounties.find(x => x.id === this.selectedCounty).county;
    //     },
    // );
  }

  updateSelectedTown() {
    this.selectedTown = this.addCountyTownForm?.get('townsId')?.value;
    this.selectedTownName = this.msTowns.find(pr => pr.id === this.selectedTown)?.town;
    // // tslint:disable-next-line:no-shadowed-variable
    // this.selectedTownName = this.msTowns?.find(x => x.id === this.selectedTown).town;
    // console.log(`town set to ${this.selectedTown}`);
  }

  onChangeSelectedProductSubcategory() {
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onClickSaveWorkPlanScheduled() {
    this.submitted = true;
    if (this.addNewScheduleForm.valid && this.dataSaveResourcesRequiredList.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Update the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'UPDATE WORK-PLAN\' button to update details Before Saving', 'COMPLAINT SCHEDULE DETAILS SAVED SUCCESSFUL', () => {
            this.saveWorkPlanScheduled();
          });
    }
  }

  saveWorkPlanScheduled() {
    this.submitted = true;
    if (this.addNewScheduleForm.valid) {
      this.SpinnerService.show();
      if (this.workPlanInspection.submittedForApprovalStatus) {
        this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
        this.dataSaveWorkPlan.resourcesRequired = this.dataSaveResourcesRequiredList;
        this.dataSaveWorkPlan.workPlanCountiesTowns = this.dataSaveWorkPlanCountiesList;
        if (this.dataSaveWorkPlan?.remarks !== null) {
          this.msService.msUpdateWorkPlanScheduleDetails(
              this.workPlanInspection.batchDetails.referenceNumber,
              this.workPlanInspection.referenceNumber, this.dataSaveWorkPlan, String(1)).subscribe(
              (data: any) => {
                console.log(data);
                this.workPlanInspection = data;
                this.SpinnerService.hide();
                this.msService.showSuccess('WORK-PLAN PLAN SCHEDULED UPDATED SUCCESSFULLY AND SUBMITTED FOR APPROVAL');
              },
              error => {
                this.SpinnerService.hide();
                console.log(error);
                // this.msService.showError('AN ERROR OCCURRED');
              },
          );
        } else {
          this.SpinnerService.hide();
          this.msService.showSuccess('You have Updated the workPlan Schedule Details, But you have not added Reason for updating on' +
              ' the field called \'Reason For Updating WorkPlan\'');
        }

      } else {
        this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
        this.dataSaveWorkPlan.resourcesRequired = this.dataSaveResourcesRequiredList;
        this.msService.msUpdateWorkPlanScheduleDetails(
            this.workPlanInspection.batchDetails.referenceNumber,
            this.workPlanInspection.referenceNumber, this.dataSaveWorkPlan, String(0)).subscribe(
            (data: any) => {
              console.log(data);
              this.workPlanInspection = data;
              this.SpinnerService.hide();
              this.msService.showSuccess('WORK-PLAN PLAN SCHEDULED UPDATED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();
              console.log(error);
              // this.msService.showError('AN ERROR OCCURRED');
            },
        );
      }
    }
  }

  addStandard() {
    const standard = this.standardsInput.nativeElement.value;
    if (standard != '' && !this.standardsArray.includes(standard)) {
      this.standardsArray.push(standard);
    }
    this.standardsInput.nativeElement.value = '';
  }

  deleteItem(index: number) {
    this.standardsArray.splice(index, 1);
  }

  onClickCloneDataSSF() {

    const selectedClone = this.workPlanInspection?.sampleSubmitted.find(pr => pr.id === this.sampleSubmitForm?.get('valueToClone')?.value);
    this.sampleSubmitForm.patchValue(selectedClone);
    this.sampleSubmitForm?.get('id').setValue(0);
    const paramDetails = selectedClone.parametersList;
    this.dataSaveSampleSubmitParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSampleSubmitParamList.push(paramDetails[i]);
    }
    this.sampleSubmitForm.enable();
    this.addLabParamStatus = true;
  }

  onClickCloneDataReport(){
    const selectedClone = this.workPlanInspection?.dataReportDto.find(pr => pr.id === this.dataReportForm?.get('dataReportValueToClone')?.value);
    this.dataReportForm.patchValue(selectedClone);
    this.dataReportForm?.get('id').setValue(0);
    const paramDetails = selectedClone.productsList;
    this.dataSaveDataReportParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveDataReportParamList.push(paramDetails[i]);
    }
    this.dataReportForm.enable();
    this.addLabParamStatus = true;

  }
  onClickCloneSeizureForm(){
    const selectedClone = this.workPlanInspection?.seizureDeclarationDto.find(pr => pr.id === this.seizureForm?.get('seizureFormValueToClone')?.value);
    this.seizureForm.patchValue(selectedClone);
    this.seizureForm?.get('id').setValue(0);
    const paramDetails = selectedClone.seizureList;
    this.dataSaveSeizureDeclarationList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSeizureDeclarationList.push(paramDetails[i]);
    }
    this.seizureForm.enable();
    this.addLabParamStatus = true;
  }

  calculateAverageCompliance(){
    console.log("Function called");
    let dataReportData = this.workPlanInspection?.dataReportDto;
    let sumOfCompliance = 0;
    for (let i=1; i<dataReportData?.length; i++){
      sumOfCompliance += dataReportData[i].totalComplianceScore;
    }
    this.averageCompliance = sumOfCompliance/dataReportData?.length;
    if (isNaN(this.averageCompliance)){
      console.log("The average compliance is nan" + this.averageCompliance);
    }
  }

  viewFile(fileUploaded: File) {
    this.SpinnerService.hide();
    const blob = new Blob([fileUploaded.slice()], {type: fileUploaded.type});

    // tslint:disable-next-line:prefer-const
    let downloadURL = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = downloadURL;
    link.download = fileUploaded.name;
    link.click();
  }

  onSelectedDataReport(){
     this.selectedDataSheet = this.workPlanInspection?.dataReportDto.find(pr => pr.id === this.sampleSubmitForm?.get('dataReportSelected')?.value);
  }
}
