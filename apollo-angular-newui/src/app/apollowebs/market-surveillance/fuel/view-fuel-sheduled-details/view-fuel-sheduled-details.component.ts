import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import swal from 'sweetalert2';
import {
  BatchFileFuelSaveDto,
  BSNumberSaveDto, ComplaintsFilesFoundDto,
  CompliantRemediationDto, FuelBatchDetailsDto,
  FuelEntityAssignOfficerDto,
  FuelEntityRapidTestDto,
  FuelInspectionDto,
  LaboratoryDto,
  LIMSFilesFoundDto,
  MSRemarksDto,
  MSSSFPDFListDetailsDto,
  PDFSaveComplianceStatusDto,
  RapidTestProductsDetailsDto,
  RapidTestProductsDto,
  RemediationDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto,
  SSFSaveComplianceStatusDto,
} from '../../../../core/store/data/ms/ms.model';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {LoggedInUser, selectUserInfo} from '../../../../core/store';
declare global {
  interface Window {
    $: any;
  }
}

@Component({
  selector: 'app-view-fuel-sheduled-details',
  templateUrl: './view-fuel-sheduled-details.component.html',
  styleUrls: ['./view-fuel-sheduled-details.component.css'],
})
export class ViewFuelSheduledDetailsComponent implements OnInit {
  active: Number = 0;
  batchReferenceNumber: string;
  teamsReferenceNo: string;
  countyReferenceNo: string;
  referenceNumber: string;
  selectedPDFFileName: string;
  submitted = false;
  fuelInspection: FuelInspectionDto;
  currDiv!: string;
  currDivLabel!: string;
  assignOfficerForm!: FormGroup;
  remarksSavedForm!: FormGroup;
  rapidTestForm!: FormGroup;
  rapidTestProductForm!: FormGroup;
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
  dataSaveAssignOfficer: FuelEntityAssignOfficerDto;
  dataSaveRapidTest: FuelEntityRapidTestDto;
  dataSaveRapidTestProducts: RapidTestProductsDto;
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
  uploadedFiles: FileList;
  blob: Blob;

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
        {name: 'addSSf', title: '<i class="btn btn-sm btn-primary">ADD SSF</i>'},
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
        {name: 'addBSNumber', title: '<i class="btn btn-sm btn-primary">ADD BS NUMBER</i>'},
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
  public settingsRapidTestProducts = {
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
      productName: {
        title: 'PRODUCT NAME',
        type: 'string',
        filter: false,
      },
      exportMarkerTestStatus: {
        title: 'EXPORT MARKER TEST COMPLIANCE RESULTS',
        type: 'string',
        filter: false,
      },
      domesticKeroseneMarkerTestStatus: {
        title: 'DOMESTIC KEROSENE MARKER COMPLIANCE TEST RESULTS',
        type: 'string',
        filter: false,
      },
      sulphurMarkerTest: {
        title: 'SULPHUR MARKER (%) TEST',
        type: 'string',
        filter: false,
      },
      sulphurMarkerTestStatus: {
        title: 'SULPHUR MARKER COMPLIANCE TEST RESULTS',
        type: 'string',
        filter: false,
      },
      overallComplianceStatus: {
        title: 'OVERALL COMPLIANCE TEST RESULTS',
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
  private addLabParamStatus: boolean;
  private scfParamSelected: number;
  private ssfSelectedID: number;
  private scfParamSelectedName: string;
  private selectedSSFDetails: SampleSubmissionDto;



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

    this.remarksSavedForm = this.formBuilder.group({
      processBy: null,
      remarksDescription: null,
    });

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.batchReferenceNumber = rs.get('batchReferenceNumber');
          this.teamsReferenceNo = rs.get('teamsReferenceNo');
          this.countyReferenceNo = rs.get('countyReferenceNo');
          this.referenceNumber = rs.get('referenceNumber');
          this.loadData(this.batchReferenceNumber, this.teamsReferenceNo, this.countyReferenceNo, this.referenceNumber);
        },
    );

    this.assignOfficerForm = this.formBuilder.group({
      assignedUserID: ['', Validators.required],
      remarks: null,
    });


    this.rapidTestForm = this.formBuilder.group({
      rapidTestStatus: ['', Validators.required],
      rapidTestRemarks: null,
    });

    this.rapidTestProductForm = this.formBuilder.group({
      productName: ['', Validators.required],
      sampleSize: ['', Validators.required],
      sulphurMarkerTest: ['', Validators.required],
      exportMarkerTestStatus: ['', Validators.required],
      domesticKeroseneMarkerTestStatus: ['', Validators.required],
      sulphurMarkerTestStatus: ['', Validators.required],
      overallComplianceStatus: ['', Validators.required],
      batchSize: null,
      batchNumber: null,
      rapidTestRemarks: null,
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
      sampleSize: ['', Validators.required],
      batchNo: null,
      batchSize: null,

    });

    this.sampleSubmitForm = this.formBuilder.group({
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
      sampleCollectionNumber: ['', Validators.required],
      sampleCollectionProduct: ['', Validators.required],
    });


    this.sampleSubmitParamsForm = this.formBuilder.group({
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

    this.scheduleRemediationForm = this.formBuilder.group({
      dateOfRemediation: ['', Validators.required],
      remarks: ['', Validators.required],
    });

    this.notCompliantInvoiceForm = this.formBuilder.group({
      remarks: ['', Validators.required],
      volumeFuelRemediated: ['', Validators.required],
      subsistenceTotalNights: ['', Validators.required],
      subsistenceTotalNightsRate: ['', Validators.required],
      transportAirTicket: ['', Validators.required],
      transportInkm: ['', Validators.required],
    });

    this.remediationForm = this.formBuilder.group({
      productType: ['', Validators.required],
      quantityOfFuel: null,
      contaminatedFuelType: ['', Validators.required],
      applicableKenyaStandard: ['', Validators.required],
      remediationProcedure: ['', Validators.required],
      volumeAdded: ['', Validators.required],
      totalVolume: ['', Validators.required],
    });

  }

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formRapidTestForm(): any {
    return this.rapidTestForm.controls;
  }

  get formRapidTestProductForm(): any {
    return this.rapidTestProductForm.controls;
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

  private loadData(batchReferenceNumber: string, teamsReferenceNo: string, countyReferenceNo: string, referenceNumber: string ): any {
    this.SpinnerService.show();
    this.msService.msFuelInspectionScheduledDetails(batchReferenceNumber, teamsReferenceNo, countyReferenceNo, referenceNumber).subscribe(
        (data) => {
          this.fuelInspection = data;
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
    const arrHead = ['scheduleRemediationInvoicePaid',
      'assignOfficer', 'rapidTest', 'addBsNumber', 'uploadScfFiles', 'uploadReportFiles',
      'ssfAddComplianceStatus', 'scheduleRemediation',
      'addRemediationDetails', 'notCompliantInvoice', 'rapidTestAddProducts'];
    const arrHeadSave = ['SCHEDULE REMEDIATION DATE INVOICE PAID',
      'SELECT OFFICER TO ASSIGN', 'RAPID TEST OVERALL RESULTS', 'ADD BS NUMBER', 'UPLOAD SCF FILE', 'UPLOAD REPORT FILE',
      'ADD SSF LAB RESULTS COMPLIANCE STATUS', 'SCHEDULE REMEDIATION DATE',
      'ADD REMEDIATION INVOICE DETAILS', 'ADD REMEDIATION INVOICE DETAILS TO BE GENERATED', 'ADD PRODUCT RAPID TEST DETAILS'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }
    this.currDiv = divVal;
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


  onClickSaveAssignOfficerBatch(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msFuelInspectionScheduledAssignOfficer(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveAssignOfficer,
      ).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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

  onClickSaveRapidTestResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveRapidTest = {...this.dataSaveRapidTest, ...this.rapidTestForm.value};
      this.msService.msFuelInspectionScheduledRapidTest(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveRapidTest).subscribe(
          (data: any) => {
            this.fuelInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('RAPID TEST RESULTS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveRapidTestProductsResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveRapidTestProducts = {...this.dataSaveRapidTestProducts, ...this.rapidTestProductForm.value};
      this.msService.msFuelInspectionScheduledRapidTestProducts(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveRapidTestProducts).subscribe(
          (data: any) => {
            this.fuelInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('RAPID TEST PRODUCTS RESULTS SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
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

      this.msService.msFuelInspectionScheduledAddSampleCollection(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveSampleCollect).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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

      this.msService.msFuelInspectionScheduledAddSampleSubmission(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveSampleSubmit).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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
    }
  }

  onClickSaveBSNumber(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveSampleSubmitBSNumber = {...this.dataSaveSampleSubmitBSNumber, ...this.sampleSubmitBSNumberForm.value};
      this.msService.msFuelInspectionScheduledAddSampleSubmissionBSNumber(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveSampleSubmitBSNumber).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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
      this.dataPDFSaveComplianceStatus.ssfID = this.fuelInspection.sampleLabResults.ssfResultsList.sffId;
      this.dataPDFSaveComplianceStatus.bsNumber = this.fuelInspection.sampleLabResults.ssfResultsList.bsNumber;
      this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      if (this.fuelInspection.sampleLabResults.savedPDFFiles.length === 0) {
        this.msService.msFuelInspectionScheduledSavePDFLIMS(
            this.batchReferenceNumber,
            this.teamsReferenceNo,
            this.countyReferenceNo,
            this.referenceNumber,
            this.dataPDFSaveComplianceStatus).subscribe(
            (data: any) => {
              this.fuelInspection = data;
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
        for (const savedPdf of this.fuelInspection.sampleLabResults.savedPDFFiles) {
          if (savedPdf.pdfName !== this.selectedPDFFileName) {
            this.msService.msFuelInspectionScheduledSavePDFLIMS(
                this.batchReferenceNumber,
                this.teamsReferenceNo,
                this.countyReferenceNo,
                this.referenceNumber,
                this.dataPDFSaveComplianceStatus).subscribe(
                (data: any) => {
                  this.fuelInspection = data;
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
      this.dataSSFSaveComplianceStatus.ssfID = this.fuelInspection.sampleLabResults.ssfResultsList.sffId;
      this.dataSSFSaveComplianceStatus.bsNumber = this.fuelInspection.sampleLabResults.ssfResultsList.bsNumber;
      // this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      this.msService.msFuelInspectionScheduledSaveSSFComplianceStatus(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSSFSaveComplianceStatus).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveScheduleRemediation).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveNotCompliantInvoice).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
          this.dataSaveRemediation).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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
      this.msService.msFuelInspectionEnd(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
      ).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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

  onClickSaveEndSSFAdding() {
    // if (valid) {
      this.SpinnerService.show();
      // this.dataSaveRemediation = {...this.dataSaveRemediation, ...this.remediationForm.value};
      this.msService.msFuelInspectionEndSampleSubmissionAdding(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
      ).subscribe(
          (data: any) => {
            this.fuelInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FUEL INSPECTION UPDATED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    // }
  }

  onClickSaveEndSSFAddingBsNumber() {
    // if (valid) {
      this.SpinnerService.show();
      // this.dataSaveRemediation = {...this.dataSaveRemediation, ...this.remediationForm.value};
      this.msService.msFuelInspectionEndSSFAddingBsNumber(
          this.batchReferenceNumber,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.referenceNumber,
      ).subscribe(
          (data: any) => {
            this.fuelInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('FUEL INSPECTION UPDATED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    // }
  }

  onClickAddDataSampleCollectItems() {
    const products = this.fuelInspection.rapidTestProducts;
    for (let i = 0; i < products.length; i++) {
      if (products[i].overallComplianceStatus !== true) {
        const scfItems = new SampleCollectionItemsDto();
        scfItems.productBrandName = products[i].productName;
        scfItems.batchNo = products[i].batchNumber;
        scfItems.batchSize = products[i].batchSize;
        scfItems.sampleSize = products[i].sampleSize;
        this.dataSaveSampleCollectItemsList.push(scfItems);
      }
    }


  }

  onClickAddDataSampleSubmitParams() {
    this.dataSaveSampleSubmitParam = this.sampleSubmitParamsForm.value;
    this.dataSaveSampleSubmitParamList.push(this.dataSaveSampleSubmitParam);
    this.sampleSubmitParamsForm?.get('parameters')?.reset();
    this.sampleSubmitParamsForm?.get('laboratoryName')?.reset();
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
  removeDataSampleCollectItems(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveSampleCollectItemsList.splice(index, 1);
    } else {
      this.dataSaveSampleCollectItemsList.splice(index, index);
    }
  }

  goBack() {
    console.log('TEST 101' + this.fuelInspection.batchDetails.referenceNumber);
    this.router.navigate([`/epra/details/`,  this.batchReferenceNumber, this.teamsReferenceNo, this.countyReferenceNo]);
  }

  viewLIMSPDFRecord(data: LIMSFilesFoundDto, bsNumber: string) {
    console.log('TEST 101 REF NO VIEW: ' + data.fileName);
    this.viewLabResultsPdfFile(String(data.fileName), bsNumber, 'application/pdf');
  }

  viewLIMSPDFSaved(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);

    this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
  }

  viewLIMSPDFSavedRemarks(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);
    this.selectedPDFFileName = data.pdfName;
    this.currDivLabel = `COMPLIANCE STATUS AND REMARKS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'viewPdfSaveCompliance';
    this.pdfSaveComplianceStatusForm.patchValue(data);

    window.$('#myModal1').modal('show');
    // this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  viewSavedRemarks(data: MSRemarksDto) {
    this.currDivLabel = `REMARKS FOR ${data.processName}`;
    this.currDiv = 'viewSavedRemarks';
    this.remarksSavedForm.patchValue(data);

    window.$('#myModal1').modal('show');
    // this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  saveLIMSPDFRecord(data: LIMSFilesFoundDto) {
    console.log('TEST 101 REF NO SAVE: ' + data.fileName);
    this.selectedPDFFileName = data.fileName;
    this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv = 'pdfSaveCompliance';

    window.$('#myModal1').modal('show');
    // $('#myModal1').modal('show');
    // this.openModalAddDetails('assignOfficer')
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  saveSSFRecord(data: SampleCollectionItemsDto) {
    this.scfParamSelected = data.id;
    this.scfParamSelectedName = data.productBrandName;
    this.addLabParamStatus = true;
    window.$('#sampleSubmitModal').modal('show');
  }

  public onCustomSCFAction(event: any): void {
    switch (event.action) {
      case 'addSSf':
        this.saveSSFRecord(event.data);
        break;
    }
  }

  viewSSFRecord(data: SampleSubmissionDto) {
    this.sampleSubmitForm.patchValue(data);
    const paramDetails = data.parametersList;
    for (let i = 0; i < paramDetails.length; i++) {
      this.dataSaveSampleSubmitParamList.push(paramDetails[i]);
    }
    this.sampleSubmitForm.disable();
    this.addLabParamStatus = false;
    window.$('#sampleSubmitModal').modal('show');
  }

  addSSFBsNumberRecord(data: SampleSubmissionDto) {
    this.currDivLabel = `ADD BS NUMBER FOR FILE REFERENCE NUMBER # ${data.fileRefNumber}`;
    this.currDiv = 'addBsNumber';
    this.ssfSelectedID = data.id;

    window.$('#myModal1').modal('show');
  }

  onClickCloseSSF() {
    this.sampleSubmitForm.reset();
    this.sampleSubmitForm.enable();
    this.addLabParamStatus = true;
    this.dataSaveSampleSubmitParamList = [];
  }


  public onCustomSSFAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewSSFRecord(event.data);
        break;
      case 'addBSNumber':
        this.addSSFBsNumberRecord(event.data);
        break;
    }
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

  onClickSaveFilesResults(docTypeName: string) {
    if (this.uploadedFiles.length > 0) {
      this.SpinnerService.show();
      const file = this.uploadedFiles;
      const formData = new FormData();
      formData.append('batchReferenceNo', this.batchReferenceNumber);
      formData.append('teamsReferenceNo', this.teamsReferenceNo );
      formData.append('countyReferenceNo', this.countyReferenceNo );
      formData.append('referenceNo', this.referenceNumber );
      formData.append('docTypeName', docTypeName);
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
        // this.uploadedFiles.item(i).slice();
      }
      this.msService.saveFuelFiles(formData).subscribe(
          (data: any) => {
            this.fuelInspection = data;
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

  viewFuelFileSaved(data: ComplaintsFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
  }

}
