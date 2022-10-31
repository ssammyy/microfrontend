import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  ApprovalDto,
  BSNumberSaveDto,
  ChargeSheetDto,
  ComplaintAssignDto, ComplaintsFilesFoundDto,
  CompliantRemediationDto,
  DataInspectorInvestDto,
  DataReportDto,
  DataReportParamsDto,
  DestructionNotificationDto,
  FuelEntityRapidTestDto,
  InspectionInvestigationReportDto, KebsStandardsDto,
  LaboratoryDto, LaboratoryEntityDto, LIMSFilesFoundDto, MsBroadProductCategory,
  MsDepartment,
  MsDivisionDetails, MsProducts, MsProductSubcategory,
  MsRecommendationDto, MSRemarksDto, MSSSFLabResultsDto, MSSSFPDFListDetailsDto,
  MsStandardProductCategory,
  PDFSaveComplianceStatusDto, PredefinedResourcesRequired,
  PreliminaryReportDto,
  PreliminaryReportFinal,
  PreliminaryReportItemsDto, RecommendationDto,
  RemediationDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto,
  SeizureDeclarationDto, SeizureDto, SeizureListDto,
  SSFSaveComplianceStatusDto,
  WorkPlanEntityDto,
  WorkPlanFeedBackDto,
  WorkPlanFinalRecommendationDto,
  WorkPlanInspectionDto, WorkPlanProductDto,
  WorkPlanScheduleApprovalDto,
} from '../../../../core/store/data/ms/ms.model';
import {
  BroadProductCategory,
  ProductCategories,
  Products,
  ProductSubcategory,
  StandardProductCategory,
} from '../../../../core/store/data/master/master.model';
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
import {Observable, throwError} from 'rxjs';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {Store} from '@ngrx/store';
import {NgxSpinnerService} from 'ngx-spinner';
import {ActivatedRoute, Router} from '@angular/router';
import {ConsignmentStatusComponent} from '../../../../core/shared/customs/consignment-status/consignment-status.component';

@Component({
  selector: 'app-complaint-plan-details',
  templateUrl: './complaint-plan-details.component.html',
  styleUrls: ['./complaint-plan-details.component.css'],
})
export class ComplaintPlanDetailsComponent implements OnInit {

  active: Number = 0;
  selectedRefNo: string;
  selectedBatchRefNo: string;
  selectedPDFFileName: string;
  currDiv!: string;
  currDivLabel!: string;
  submitted = false;
  addLabParamStatus = true;
  defaultPageSize = 20;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;

  totalCompliantValue = 0;

  addNewScheduleForm!: FormGroup;
  approveScheduleForm!: FormGroup;
  approvePreliminaryForm!: FormGroup;
  finalRemarkHODForm!: FormGroup;
  finalRecommendationForm!: FormGroup;
  finalRecommendationDetailsForm!: FormGroup;
  preliminaryRecommendationForm!: FormGroup;
  chargeSheetForm!: FormGroup;
  dataReportForm!: FormGroup;
  dataReportParamForm!: FormGroup;
  seizureDeclarationForm!: FormGroup;
  investInspectReportForm!: FormGroup;
  preliminaryReportForm!: FormGroup;
  preliminaryReportParamForm!: FormGroup;
  investInspectReportInspectorsForm!: FormGroup;
  clientEmailNotificationForm!: FormGroup;
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
  dataSaveFinalRemarks: WorkPlanFeedBackDto;
  dataSaveFinalReport: PreliminaryReportFinal;
  dataSaveChargeSheet: ChargeSheetDto;
  dataSaveDataReport: DataReportDto;
  dataSaveDataInspectorInvestList: DataInspectorInvestDto[] = [];
  dataSaveDataReportParamList: DataReportParamsDto[] = [];
  dataSaveDataReportParam: DataReportParamsDto;
  dataSaveDataInspectorInvest: DataInspectorInvestDto;
  dataSaveSeizureDeclaration: SeizureDto;
  dataSaveSeizureDeclarationList: SeizureDto[] = [];
  dataSaveInvestInspectReport: InspectionInvestigationReportDto;
  dataSaveFinalRecommendationDetails: RecommendationDto;
  dataSaveFinalRecommendationList: RecommendationDto[] = [];
  dataSavePreliminaryReport: PreliminaryReportDto;
  dataSavePreliminaryReportParamList: PreliminaryReportItemsDto[] = [];
  dataSavePreliminaryReportParam: PreliminaryReportItemsDto;
  dataSaveFinalRecommendation: WorkPlanFinalRecommendationDto;
  dataSaveDestructionNotification: DestructionNotificationDto;
  laboratories: LaboratoryDto[] = [];
  standards: KebsStandardsDto[] = [];


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
  standardProductCategorySelected: 0;
  productCategoriesSelected: 0;
  broadProductCategorySelected: 0;
  productsSelected: 0;
  productSubcategorySelected: 0;
  departmentSelected: 0;

  labList: LaboratoryDto[];
  roles: string[];
  userLoggedInID: number;
  selectedRecommendationID: number;
  selectedRecommendationName: string;
  workPlanProductRefNo: string;
  userProfile: LoggedInUser;
  blob: Blob;
  uploadedFiles: FileList;
  resetUploadedFiles: FileList;
  selectedCounty = 0;
  selectedTown = 0;
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
  public scfParamSelected: number;
  public ssfSelectedID: number;
  public scfParamSelectedName: string;
  public selectedSSFDetails: SampleSubmissionDto;
  public ssfCountBSNumber: number;
  public scfCountSSF: number;

  public settingsResourceRequired = {
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
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View RECOMMENDATION(s)</i>'},
        {name: 'addRemarkDirector', title: '<i class="btn btn-sm btn-primary">ADD REMARKS</i>'},
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
        {name: 'addRecommendation', title: '<i class="btn btn-sm btn-primary">ADD RECOMMENDATION</i>'},
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View RECOMMENDATION(s)</i>'},
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
        // {name: 'updateRecords', title: '<i class="btn btn-sm btn-primary">UPDATE</i>'}
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
      descriptionProductsSeized: {
        title: 'Description Products Seized',
        type: 'string',
        filter: false,
      },
      brand: {
        title: 'Brand',
        type: 'string',
        filter: false,
      },
      sector: {
        title: 'Sector',
        type: 'string',
        filter: false,
      },
      reasonSeizure: {
        title: 'Reason Seizure',
        type: 'string',
        filter: false,
      },
      nameSeizingOfficer: {
        title: 'Name Seizing Officer',
        type: 'string',
        filter: false,
      },
      seizureSerial: {
        title: 'Seizure Serial',
        type: 'string',
        filter: false,
      },
      quantity: {
        title: 'Quantity',
        type: 'string',
        filter: false,
      },
      unit: {
        title: 'Unit',
        type: 'string',
        filter: false,
      },
      estimatedCost: {
        title: 'Estimated Cost',
        type: 'string',
        filter: false,
      },
      currentLocation: {
        title: 'Current Location',
        type: 'string',
        filter: false,
      },
      productsDestruction: {
        title: 'Products Destruction',
        type: 'string',
        filter: false,
      },
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
      descriptionProductsSeized: {
        title: 'Description Products Seized',
        type: 'string',
        filter: false,
      },
      brand: {
        title: 'Brand',
        type: 'string',
        filter: false,
      },
      sector: {
        title: 'Sector',
        type: 'string',
        filter: false,
      },
      reasonSeizure: {
        title: 'Reason Seizure',
        type: 'string',
        filter: false,
      },
      nameSeizingOfficer: {
        title: 'Name Seizing Officer',
        type: 'string',
        filter: false,
      },
      seizureSerial: {
        title: 'Seizure Serial',
        type: 'string',
        filter: false,
      },
      quantity: {
        title: 'Quantity',
        type: 'string',
        filter: false,
      },
      unit: {
        title: 'Unit',
        type: 'string',
        filter: false,
      },
      estimatedCost: {
        title: 'Estimated Cost',
        type: 'string',
        filter: false,
      },
      currentLocation: {
        title: 'Current Location',
        type: 'string',
        filter: false,
      },
      productsDestruction: {
        title: 'Products Destruction',
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
      nameProduct: {
        title: 'PRODUCT BRAND NAME',
        type: 'string',
        filter: false,
      },
      fileRefNumber: {
        title: 'FILE REF NUMBER',
        type: 'string',
        filter: false,
      },
      disposal: {
        title: 'DISPOSAL',
        type: 'string',
        filter: false,
      },
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
      nameProduct: {
        title: 'PRODUCT BRAND NAME',
        type: 'string',
        filter: false,
      },
      fileRefNumber: {
        title: 'FILE REF NUMBER',
        type: 'string',
        filter: false,
      },
      disposal: {
        title: 'DISPOSAL',
        type: 'string',
        filter: false,
      },
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
      nameProduct: {
        title: 'PRODUCT BRAND NAME',
        type: 'string',
        filter: false,
      },
      fileRefNumber: {
        title: 'FILE REF NUMBER',
        type: 'string',
        filter: false,
      },
      disposal: {
        title: 'DISPOSAL',
        type: 'string',
        filter: false,
      },
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

  public workPlanInspection: WorkPlanInspectionDto;
  public msCounties: {name: string, code: string}[];


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
      remarks: ['', Validators.required],
    });

    this.approveScheduleForm = this.formBuilder.group({
      approvalStatus: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.finalRecommendationForm = this.formBuilder.group({
      recommendationId: null,
      hodRecommendationRemarks: ['', Validators.required],
    });

    this.finalRecommendationDetailsForm = this.formBuilder.group({
      recommendationId: ['', Validators.required],
      recommendationName: ['', Validators.required],
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
      totalComplianceScore: ['', Validators.required],
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
      findings: ['', Validators.required],
      conclusion: ['', Validators.required],
      recommendations: ['', Validators.required],
      statusActivity: ['', Validators.required],
      finalRemarkHod: null,
      // remarks: ['', Validators.required],
    });

    this.dataReportParamForm = this.formBuilder.group({
      id: null,
      typeBrandName: ['', Validators.required],
      localImport: ['', Validators.required],
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
      marketTownCenter: ['', Validators.required],
      nameOfOutlet: ['', Validators.required],
      descriptionProductsSeized: ['', Validators.required],
      brand: ['', Validators.required],
      sector: ['', Validators.required],
      reasonSeizure: ['', Validators.required],
      nameSeizingOfficer: ['', Validators.required],
      seizureSerial: ['', Validators.required],
      quantity: ['', Validators.required],
      unit: ['', Validators.required],
      estimatedCost: ['', Validators.required],
      currentLocation: ['', Validators.required],
      productsDestruction: ['', Validators.required],
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
      id: null,
      nameProduct: ['', Validators.required],
      packaging: ['', Validators.required],
      labellingIdentification: ['', Validators.required],
      fileRefNumber: ['', Validators.required],
      referencesStandards: ['', Validators.required],
      sizeTestSample: ['', Validators.required],
      sizeRefSample: null,
      condition: ['', Validators.required],
      sampleReferences: null,
      sendersName: ['', Validators.required],
      designation: ['', Validators.required],
      address: ['', Validators.required],
      sendersDate: ['', Validators.required],
      receiversName: null,
      testChargesKsh: null,
      receiptLpoNumber: null,
      invoiceNumber: null,
      disposal: ['', Validators.required],
      remarks: ['', Validators.required],
      sampleCollectionNumber: null,
    });


    this.sampleSubmitParamsForm = this.formBuilder.group({
      id: null,
      parameters: ['', Validators.required],
      laboratoryName: ['', Validators.required],
    });

    this.sampleSubmitBSNumberForm = this.formBuilder.group({
      bsNumber: ['', Validators.required],
      ssfID: ['', Validators.required],
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
      remarksStatus: null,
      remarks: null,
    });

    this.addNewScheduleForm = this.formBuilder.group({
      id: null,
      complaintDepartment: ['', Validators.required],
      divisionId: ['', Validators.required],
      nameActivity: ['', Validators.required],
      timeActivityDate: ['', Validators.required],
      county: ['', Validators.required],
      townMarketCenter: ['', Validators.required],
      locationActivityOther: ['', Validators.required],
      standardCategory: ['', Validators.required],
      broadProductCategory: ['', Validators.required],
      productCategory: ['', Validators.required],
      product: ['', Validators.required],
      productSubCategory: ['', Validators.required],
      resourcesRequired: ['', Validators.required],
      budget: ['', Validators.required],
      // remarks: ['', Validators.required],
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

    if (this.workPlanInspection?.rejectedStatus && this.workPlanInspection?.approvedStatus === false ) {
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


    if (this.workPlanInspection?.sampleSubmittedStatus === false && this.workPlanInspection?.scfDocId !== null) {
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

    switch (this.workPlanInspection?.preliminaryReport?.approvedStatusHodFinal) {
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

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formClientEmailNotificationForm(): any {
    return this.clientEmailNotificationForm.controls;
  }

  get formFinalRemarkHODForm(): any {
    return this.finalRemarkHODForm.controls;
  }

  get formPreliminaryReportForm(): any {
    return this.preliminaryReportForm.controls;
  }

  get formFinalRecommendationForm(): any {
    return this.finalRecommendationForm.controls;
  }

  get formFinalRecommendationDetailsForm(): any {
    return this.finalRecommendationDetailsForm.controls;
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

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
  }

  get formNotCompliantInvoiceForm(): any {
    return this.notCompliantInvoiceForm.controls;
  }

  get formRemediationForm(): any {
    return this.remediationForm.controls;
  }

  updateSelectedRecommendation() {
    this.selectedRecommendationID = this.sampleSubmitParamsForm?.get('recommendationId')?.value;
    this.selectedRecommendationName = this.recommendationList.find(x => x.id === this.selectedRecommendationID).recommendationName;
    console.log(`town set to ${this.selectedTown}`);
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
    const arrHead = ['approveSchedule', 'uploadFiles', 'chargeSheetDetails', 'dataReportDetails', 'seizureDeclarationDetails', 'finalLabComplianceStatus',
      'addBsNumber', 'approvePreliminaryHOF', 'approvePreliminaryHOD', 'addPreliminaryRecommendation', 'approveFinalPreliminaryHOF', 'approveFinalPreliminaryHOD',
      'ssfAddComplianceStatus', 'addFinalRecommendationHOD', 'uploadDestructionNotificationFile',
      'clientAppealed', 'clientAppealedSuccessfully', 'uploadDestructionReport', 'addFinalRemarksHOD',
      'uploadChargeSheetFiles', 'uploadSCFFiles', 'uploadSSFFiles', 'uploadSeizureFiles', 'uploadDeclarationFiles', 'uploadDataReportFiles',
      'addNewScheduleDetails'];

    // tslint:disable-next-line:max-line-length
    const arrHeadSave = ['APPROVE/REJECT SCHEDULED WORK-PLAN', 'ATTACH FILE(S) BELOW', 'ADD CHARGE SHEET DETAILS', 'ADD DATA REPORT DETAILS', 'ADD SEIZURE DECLARATION DETAILS', 'FINAL LAB RESULTS COMPLIANCE STATUS',
      'ADD BS NUMBER', 'APPROVE/REJECT PRELIMINARY REPORT', 'APPROVE/REJECT PRELIMINARY REPORT', 'ADD FINAL REPORT DETAILS', 'APPROVE/REJECT FINAL REPORT', 'APPROVE/REJECT FINAL REPORT',
      'ADD SSF LAB RESULTS COMPLIANCE STATUS', 'ADD FINAL RECOMMENDATION FOR THE SURVEILLANCE', 'UPLOAD DESTRUCTION NOTIFICATION TO BE SENT'
      , 'DID CLIENT APPEAL ?', 'ADD CLIENT APPEALED STATUS IF SUCCESSFULLY OR NOT', 'UPLOAD DESTRUCTION REPORT', 'ADD FINAL REMARKS FOR THE MS CONDUCTED',
      'ATTACH CHARGE SHEET FILE BELOW', 'ATTACH SAMPLE COLLECTION FILE BELOW', 'ATTACH SAMPLE SUBMISSION FILE BELOW', 'ATTACH SEIZURE FILE BELOW', 'ATTACH DECLARATION FILE BELOW', 'ATTACH DATA REPORT FILE BELOW',
      'UPDATE WORK-PLAN SCHEDULE DETAILS FILE'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }

    this.updateDataReport();
    this.updateFieldReport();
    this.currDiv = divVal;
  }

  updateDataReport() {
    if (this.workPlanInspection?.dataReportStatus && this.workPlanInspection?.onsiteEndStatus == false) {
      this.dataReportForm.patchValue(this.workPlanInspection?.dataReportDto);
      this.totalCompliantValue = this.workPlanInspection?.dataReportDto?.totalComplianceScore;
      this.dataSaveDataReportParamList = [];
      for (let prod = 0; prod < this.workPlanInspection?.dataReportDto?.productsList.length; prod++) {
        this.dataSaveDataReportParamList.push(this.workPlanInspection?.dataReportDto.productsList[prod]);
      }
    }
  }

  updateFieldReport() {
    if (this.workPlanInspection?.investInspectReportStatus &&  this.workPlanInspection?.onsiteEndStatus == false) {
      this.investInspectReportForm.patchValue(this.workPlanInspection?.inspectionInvestigationDto);
      this.dataSaveDataInspectorInvestList = [];
      for (let prod = 0; prod < this.workPlanInspection?.inspectionInvestigationDto?.kebsInspectors.length; prod++) {
        this.dataSaveDataInspectorInvestList.push(this.workPlanInspection?.inspectionInvestigationDto?.kebsInspectors[prod]);
      }
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

  public onCustomWorkPlanProductsAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewSSFRecord(event.data);
        break;
      case 'addRecommendation':
        this.addRecommendation(event.data);
        break;
      case 'addRemarkDirector':
        this.addRemarksDirector(event.data);
        break;
    }
  }

  viewSSFLabResultsRecord(data: SampleSubmissionDto) {

    this.selectedLabResults = this.workPlanInspection.sampleLabResults.find(lab => lab.ssfResultsList.bsNumber === data.bsNumber);

    window.$('#myModal2').modal('hide');
    // window.$('.modal').remove();
    window.$('body').removeClass('modal-open');
    window.$('.modal-backdrop').remove();
    window.$('#sampleLabResultsModal').modal('show');
  }

  addSSFBsNumberRecord(data: SampleSubmissionDto) {
    if (data.bsNumber !== null && this.workPlanInspection?.bsNumberStatus) {
      this.msService.showError('BS Number Already Added And Pushed To LIMS');
    } else {
      this.currDivLabel = `ADD BS NUMBER FOR FILE REFERENCE NUMBER # ${data.fileRefNumber}`;
      this.currDiv = 'addBsNumber';
      this.sampleSubmitBSNumberForm.reset();
      this.ssfSelectedID = data.id;
      window.$('#myModal1').modal('show');
    }
  }

  viewSSFRecord(data: SampleSubmissionDto) {
    this.sampleSubmitForm.patchValue(data);
    const paramDetails = data.parametersList;
    this.dataSaveSampleSubmitParamList = [];
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSampleSubmitParamList.push(paramDetails[i]);
    }
    this.sampleSubmitForm.disable();
    this.addLabParamStatus = false;
    window.$('#sampleSubmitModal').modal('show');
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

  addRecommendation(data: WorkPlanProductDto) {
    if (!data.hodRecommendationStatus) {
      this.msService.showWarning('Recommendation(s) Have/Has already been added');
    } else {
      this.workPlanProductRefNo = data.referenceNo;
      window.$('#finalRecommendationModal').modal('show');
    }

  }

  addRemarksDirector(data: WorkPlanProductDto) {
    if (!data.directorRecommendationStatus) {
      this.msService.showWarning('Remark(s) Have/Has already been added');
    } else {
      this.workPlanProductRefNo = data.referenceNo;
      this.currDiv = 'addFinalRemarksDirector';
      this.currDivLabel = 'ADD REMARKS FOR THE RECOMMENDATION ADDED';
      window.$('#myModal1').modal('show');
    }

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

  onClickSaveClientAppealed(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsClientAppealed(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.SpinnerService.hide();
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
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveApprovePreliminary = {...this.dataSaveApprovePreliminary, ...this.approvePreliminaryForm.value};
      this.msService.msWorkPlanScheduleDetailsClientAppealedSuccessfully(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber,
          this.dataSaveApprovePreliminary,
      ).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            console.log(data);
            this.approvePreliminaryForm.reset();
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

  onClickSubmitForApproval() {
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
          this.msService.showError('AN ERROR OCCURRED');
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
    this.msService.showSuccessWith2Message('Are you sure your want to End BS Number Adding?', 'You won\'t be able to Update BS Number(s) after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update Bs Number Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.endSSFAddingBsNumber();
        });
  }

  onClickSaveEndFinalRecommendationHOD() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Final Recommendation Adding?', 'You won\'t be able to Update Recommendation Details after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update the Recommendation you have added Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.endEndFinalRecommendation();
        });
  }

  onClickSaveEndFinalRemarksDirector() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Adding Final Remarks for the  Recommendation given?', 'You won\'t be able to Update Remarks Details after submission!',
        // tslint:disable-next-line:max-line-length
        'You can go back and  update the Remarks you have added Before Saving', 'BS NUMBER ADDING ENDED SUCCESSFUL', () => {
          this.endEndFinalRemarksRecommendation();
        });
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

  endEndFinalRecommendation() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
    this.msService.msWorkPlanScheduleDetailsEndFinalRecommendationHOD(
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

  endEndFinalRemarksRecommendation() {
    // if (valid) {
    this.SpinnerService.show();
    this.dataSaveApproveSchedule = {...this.dataSaveApproveSchedule, ...this.approveScheduleForm.value};
    this.msService.msWorkPlanScheduleDetailsEndFinalRecommendationRemarksDirector(
        this.workPlanInspection.batchDetails.referenceNumber,
        this.workPlanInspection.referenceNumber,
    ).subscribe(
        (data: any) => {
          this.workPlanInspection = data;
          console.log(data);
          this.SpinnerService.hide();
          this.msService.showSuccess('FINAL REMARKS ADDING ENDED SUCCESSFUL');
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
    // }
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
    if (this.uploadedFiles.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Files Selected?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can go back and click the button to update File(s) Before Saving', 'FILE(S) UPLOADED SUCCESSFUL', () => {
            this.saveFilesResults(docTypeName);
          });
    } else {
      this.msService.showError('NO FILE SELECTED FOR UPLOADED');
    }

  }

  saveFilesResults(docTypeName: string) {
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
        // this.uploadedFiles.item(i).slice();
      }
      this.msService.saveWorkPlanFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFiles = this.resetUploadedFiles;
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
    }
  }



  onClickSaveUploadedDestructionReport(docTypeName: string) {
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
      this.msService.saveWorkPlanDestructionReportFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFiles = null;
            console.log(data);
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
    if (this.uploadedFiles.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFiles;
      this.dataSaveDestructionNotification = {...this.dataSaveDestructionNotification, ...this.clientEmailNotificationForm.value};
      const formData = new FormData();
      formData.append('referenceNo', this.workPlanInspection.referenceNumber);
      formData.append('batchReferenceNo', this.workPlanInspection.batchDetails.referenceNumber );
      formData.append('data', JSON.stringify(this.dataSaveDestructionNotification));
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.msService.saveWorkPlanDestructionNotificationFiles(formData).subscribe(
          (data: any) => {
            this.workPlanInspection = data;
            this.uploadedFiles = null;
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

  onClickEndSampleSubmitted() {
    this.msService.showSuccessWith2Message('Are you sure your want to End Sample Submission?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD SEIZED GOODS\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
          this.endSampleSubmitted();
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


  onClickSaveSampleSubmitted() {
    this.submitted = true;
    let valuesToShow = '\'ADD SAMPLE SUBMISSION\'';
    if (this.sampleSubmitForm.get('id').value !== null) {
      valuesToShow = '\'UPDATE SSF DETAILS\'';
    }
    if (this.sampleSubmitForm.valid && this.dataSaveSampleSubmitParamList.length !== 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click the${valuesToShow}button to updated the Details before saving`, 'SAMPLE SUBMISSION ADDED/UPDATED SUCCESSFUL', () => {
            this.saveSampleSubmitted();
          });
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
        for (const savedPdf of this.selectedLabResults.savedPDFFiles) {
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
    this.router.navigate([`/complaintPlan`, this.workPlanInspection.batchDetails.referenceNumber]);
  }

  viewWorkPlanFileSaved(data: ComplaintsFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
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
          this.msService.showError('AN ERROR OCCURRED');
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

  updateSeizedDetails(data: SeizureDto) {
    this.seizureDeclarationForm.patchValue(data);
    window.$('#seizureDeclarationModal').modal('show');
  }

  viewLIMSPDFSavedRemarks(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);
    this.selectedPDFFileName = data.pdfName;
    this.currDivLabel = `COMPLIANCE STATUS AND REMARKS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'viewPdfSaveCompliance';
    this.pdfSaveComplianceStatusForm.patchValue(data);

    window.$('#myModal1').modal('show');
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


  onClickAddDataReportParam() {
    this.dataSaveDataReportParam = this.dataReportParamForm.value;
    this.dataSaveDataReportParamList.push(this.dataSaveDataReportParam);
    this.dataReportParamForm?.get('typeBrandName')?.reset();
    this.dataReportParamForm?.get('localImport')?.reset();
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
    console.log('compliant count' + compliantCount);
    console.log('total count' + totalCount);
    console.log('complinace Value count' + this.totalCompliantValue);
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

  onClickAddDataRecommendationDetails() {
    this.dataSaveFinalRecommendationDetails = this.finalRecommendationDetailsForm.value;
    this.dataSaveFinalRecommendationList.push(this.dataSaveFinalRecommendationDetails);
    this.finalRecommendationDetailsForm?.reset();
  }

  // Remove Form repeater values
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
  removeDataRecommendation(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveFinalRecommendationList.splice(index, 1);
    } else {
      this.dataSaveFinalRecommendationList.splice(index, index);
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

  onClickEndSeizureDeclaration() {
      this.msService.showSuccessWith2Message('Are you sure your want to End Seizure Adding Of Goods?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD SEIZED GOODS\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
            this.endSeizureDeclaration();
          });
  }

  onClickSaveSeizureDeclaration() {
    this.submitted = true;
    if (this.seizureDeclarationForm.valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD SEIZED GOODS\' button to update details Before Saving', 'SEIZURE PRODUCT DETAILS SAVED SUCCESSFUL', () => {
            this.saveSeizureDeclaration();
          });

    }
  }

  saveSeizureDeclaration() {
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

  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
    }
  }

  onChangeSelectedDepartment() {
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

  updateSelectedCounty() {
    this.selectedCounty = this.addNewScheduleForm?.get('county')?.value;
    console.log(`county set to ${this.selectedCounty}`);
    this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
    this.store$.select(selectCountyIdData).subscribe(
        (d) => {
          if (d) {
            console.log(`Select county inside is ${d}`);
            return this.townService.getAll();
          } else {
            return throwError('Invalid request, County id is required');
          }
        },
    );

  }

  updateSelectedTown() {
    this.selectedTown = this.addNewScheduleForm?.get('town')?.value;
    console.log(`town set to ${this.selectedTown}`);
  }

  onChangeSelectedProductSubcategory() {
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onClickSaveWorkPlanScheduled() {
    this.submitted = true;
    if (this.addNewScheduleForm.invalid) {
      return;
    }
    if (this.addNewScheduleForm.valid) {
      this.SpinnerService.show();
      this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msUpdateWorkPlanScheduleDetails(
          this.workPlanInspection.batchDetails.referenceNumber,
          this.workPlanInspection.referenceNumber, this.dataSaveWorkPlan, String(0)).subscribe(
          (data: any) => {
            console.log(data);
            this.workPlanInspection = data;
            this.SpinnerService.hide();
            this.msService.showSuccess('COMPLAINT PLAN SCHEDULED UPDATED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }

}
