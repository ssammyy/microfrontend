import { Component, OnInit } from '@angular/core';
import {
  AllComplaintsDetailsDto,
  BSNumberSaveDto, ComplaintAdviceRejectDto, ComplaintApproveDto, ComplaintAssignDto,
  ComplaintDetailsDto, ComplaintRejectDto,
  ComplaintsFilesFoundDto,
  CompliantRemediationDto,
  FuelEntityAssignOfficerDto,
  FuelEntityRapidTestDto,
  FuelInspectionDto,
  LaboratoryDto,
  LIMSFilesFoundDto,
  MsDepartment, MsDivisionDetails,
  MSRemarksDto,
  MSSSFPDFListDetailsDto,
  PDFSaveComplianceStatusDto,
  RemediationDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto,
  SSFSaveComplianceStatusDto,
} from '../../../../core/store/data/ms/ms.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoggedInUser, selectUserInfo} from '../../../../core/store';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {Store} from '@ngrx/store';
import {NgxSpinnerService} from 'ngx-spinner';
import {ActivatedRoute, Router} from '@angular/router';

declare global {
  interface Window {
    $: any;
  }
}

@Component({
  selector: 'app-complaint-details',
  templateUrl: './complaint-details.component.html',
  styleUrls: ['./complaint-details.component.css'],
})
export class ComplaintDetailsComponent implements OnInit {

  active: Number = 0;
  selectedRefNo: string;
  selectedBatchRefNo: string;
  selectedPDFFileName: string;
  complaintInspection: AllComplaintsDetailsDto;
  currDiv!: string;
  currDivLabel!: string;
  selectComplaintStatus!: string;
  remarksSavedForm!: FormGroup;
  acceptRejectComplaintForm!: FormGroup;
  adviceComplaintForm!: FormGroup;

  assignOfficerForm!: FormGroup;
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
  dataSaveAssignOfficer: ComplaintAssignDto;
  dataSaveAcceptance: ComplaintApproveDto;
  dataSaveAdviceWhere: ComplaintAdviceRejectDto;
  dataSaveSampleCollect: SampleCollectionDto;
  dataSaveSampleCollectItems: SampleCollectionItemsDto;
  dataSaveSampleSubmit: SampleSubmissionDto;
  dataSaveSampleSubmitParam: SampleSubmissionItemsDto;
  dataSaveSampleSubmitBSNumber: BSNumberSaveDto;
  dataPDFSaveComplianceStatus: PDFSaveComplianceStatusDto;
  dataSSFSaveComplianceStatus: SSFSaveComplianceStatusDto;
  dataSaveScheduleRemediation: CompliantRemediationDto;
  dataSaveNotCompliantInvoice: CompliantRemediationDto;
  dataSaveRemediation: RemediationDto;

  labList: LaboratoryDto[];
  msDepartments: MsDepartment[];
  msDivisions: MsDivisionDetails[];
  departmentSelected!: number;
  roles: string[];
  userLoggedInID: number;
  userProfile: LoggedInUser;
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
  public settingsComplaintsFiles = {
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
        {name: 'viewPDFRemarks', title: '<i class="btn btn-sm btn-primary">View Remarks</i>'},
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

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.selectedRefNo = rs.get('referenceNumber');
          // this.selectedBatchRefNo = rs.get('batchReferenceNumber');
          this.loadData(this.selectedRefNo);
        },
    );

    this.assignOfficerForm = this.formBuilder.group({
      assignedIo: ['', Validators.required],
      assignedRemarks: null,
    });

    this.acceptRejectComplaintForm = this.formBuilder.group({
      approved: ['', Validators.required],
      department: null,
      division: null,
      approvedRemarks: ['', Validators.required],
    });

    this.adviceComplaintForm = this.formBuilder.group({
      advisedWhereToRemarks: ['', Validators.required],
      rejectedRemarks: ['', Validators.required],
      mandateForOga: 1,
    });

    this.rapidTestForm = this.formBuilder.group({
      rapidTestStatus: ['', Validators.required],
      rapidTestRemarks: null,
    });

    this.remarksSavedForm = this.formBuilder.group({
      processBy: null,
      remarksDescription: null,
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

    this.scheduleRemediationForm = this.formBuilder.group({
      dateOfRemediation: ['', Validators.required],
      remarks: ['', Validators.required],
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

  }

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formAcceptRejectForm(): any {
    return this.acceptRejectComplaintForm.controls;
  }

  get formAdviceComplaintForm(): any {
    return this.adviceComplaintForm.controls;
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

  private loadData(referenceNumber: string): any {
    this.SpinnerService.show();
    // let params = {'personal': this.personalTasks}
    // this.fuelInspection = this.msService.fuelInspectionDetailsExamples()
    // this.totalCount = this.loadedData.fuelInspectionDto.length;
    // this.dataSet.load(this.loadedData.fuelInspectionDto);
    // this.SpinnerService.hide();
    this.msService.msComplaintDetails(referenceNumber).subscribe(
        (data) => {
          this.complaintInspection = data;
          // tslint:disable-next-line:max-line-length
          if (this.complaintInspection.complaintsDetails.approvedStatus === false && this.complaintInspection.complaintsDetails.rejectedStatus === false) {
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
          }
          // this.totalCount = this.loadedData.length;
          // this.dataSet.load(this.loadedData);
          this.SpinnerService.hide();
          console.log(data);
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );



    // let data = this.diService.listAssignedCd(documentTypeUuid, page, size, params);
    // console.log(this.activeStatus)
    // // Clear list before loading
    // this.dataSet.load([])
    // // Switch
    // if (this.activeStatus === "completed") {
    //   data = this.diService.listCompletedCd(documentTypeUuid, page, size)
    // } else if (this.activeStatus === "ongoing") {
    //   data = this.diService.listSectionOngoingCd(documentTypeUuid, page, size)
    // } else if (this.activeStatus === "not-assigned") {
    //   data = this.diService.listManualAssignedCd(documentTypeUuid, page, size)
    // }
    // data.subscribe(
    //     result => {
    //       if (result.responseCode === "00") {
    //         let listD: any[] = result.data;
    //         this.totalCount = result.totalCount
    //         this.dataSet.load(listD)
    //       } else {
    //         console.log(result)
    //       }
    //     }
    // );
  }

  openModalAddDetails(divVal: string): void {
    const arrHead = ['acceptRejectComplaint', 'notKebsMandate','assignHOF'];
    const arrHeadSave = ['ACCEPT/REJECT COMPLAINT', 'NOT WITHIN KEBS MANDATE','ASSIGN HOF'];

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


  onClickSaveAssignHof(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msComplaintUpdateAssignHOFDetails(this.complaintInspection.complaintsDetails.refNumber,  this.dataSaveAssignOfficer).subscribe(
          (data: any) => {
            this.complaintInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('HOF ASSIGNED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  onClickSaveAcceptRejectFormResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAcceptance = {...this.dataSaveAcceptance, ...this.acceptRejectComplaintForm.value};
      if (this.selectComplaintStatus === 'Accept') {
        // tslint:disable-next-line:max-line-length
        this.msService.msComplaintUpdateAcceptanceDetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveAcceptance).subscribe(
            (data: any) => {
              this.complaintInspection = data;
              console.log(data);
              this.acceptRejectComplaintForm.reset();
              this.SpinnerService.hide();
              this.msService.showSuccess('COMPLAINT ACCEPTANCE, SAVED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();
              this.acceptRejectComplaintForm.reset();
              console.log(error);
              this.msService.showError('AN ERROR OCCURRED');
            },
        );
      } else if (this.selectComplaintStatus === 'Reject') {
        const newRejectDto = new ComplaintRejectDto();
        newRejectDto.rejected = this.dataSaveAcceptance.approved;
        newRejectDto.rejectedRemarks = this.dataSaveAcceptance.approvedRemarks;
        this.msService.msComplaintUpdateRejectDetails(this.complaintInspection.complaintsDetails.refNumber, newRejectDto).subscribe(
            (data: any) => {
              this.complaintInspection = data;
              this.acceptRejectComplaintForm.reset();
              console.log(data);
              this.SpinnerService.hide();
              this.msService.showSuccess('COMPLAINT REJECTION, SAVED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();
              this.acceptRejectComplaintForm.reset();
              console.log(error);
              this.msService.showError('AN ERROR OCCURRED');
            },
        );
      }



    }
  }

  onClickSaveAdviceWhereFormResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAdviceWhere = {...this.dataSaveAdviceWhere, ...this.adviceComplaintForm.value};
        // tslint:disable-next-line:max-line-length
        this.msService.msComplaintUpdateMandateOGADetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveAdviceWhere).subscribe(
            (data: any) => {
              this.complaintInspection = data;
              console.log(data);
              this.acceptRejectComplaintForm.reset();
              this.SpinnerService.hide();
              this.msService.showSuccess('COMPLAINANT ADVICED WHERE TO TAKE THE COMPLAINT, SAVED SUCCESSFULLY');
            },
            error => {
              this.SpinnerService.hide();
              this.acceptRejectComplaintForm.reset();
              console.log(error);
              this.msService.showError('AN ERROR OCCURRED');
            },
        );

    }
  }


  onClickSaveSampleCollected(valid: boolean, valid2: boolean) {
    // if (valid && valid2) {
    //   this.SpinnerService.show();
    //   this.dataSaveSampleCollect = {...this.dataSaveSampleCollect, ...this.sampleCollectForm.value};
    //   this.dataSaveSampleCollectItems = {...this.dataSaveSampleCollectItems, ...this.sampleCollectItemsForm.value};
    //
    //   const sampleCollectionItemsDto: SampleCollectionItemsDto[] = [];
    //   sampleCollectionItemsDto.push(this.dataSaveSampleCollectItems);
    //   this.dataSaveSampleCollect.productsList = sampleCollectionItemsDto;
    //
    //   this.msService.msFuelInspectionScheduledAddSampleCollection(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSaveSampleCollect).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('SAMPLE COLLECTION SAVED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSaveSampleSubmitted(valid: boolean, valid2: boolean) {
    // if (valid && valid2) {
    //   this.SpinnerService.show();
    //   this.dataSaveSampleSubmit = {...this.dataSaveSampleSubmit, ...this.sampleSubmitForm.value};
    //   this.dataSaveSampleSubmitParam = {...this.dataSaveSampleSubmitParam, ...this.sampleSubmitParamsForm.value};
    //
    //   const sampleSubmissionParamsDto: SampleSubmissionItemsDto[] = [];
    //   sampleSubmissionParamsDto.push(this.dataSaveSampleSubmitParam);
    //   this.dataSaveSampleSubmit.parametersList = sampleSubmissionParamsDto;
    //
    //   this.msService.msFuelInspectionScheduledAddSampleSubmission(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSaveSampleSubmit).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('SAMPLE SUBMISSION SAVED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSaveBSNumber(valid: boolean) {
    // if (valid) {
    //   this.SpinnerService.show();
    //   this.dataSaveSampleSubmitBSNumber = {...this.dataSaveSampleSubmitBSNumber, ...this.sampleSubmitBSNumberForm.value};
    //   this.msService.msFuelInspectionScheduledAddSampleSubmissionBSNumber(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSaveSampleSubmitBSNumber).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('BS NUMBER ADDED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSavePDFSelected(valid: boolean) {
    // if (valid) {
    //   this.SpinnerService.show();
    //   this.dataPDFSaveComplianceStatus = {...this.dataPDFSaveComplianceStatus, ...this.pdfSaveComplianceStatusForm.value};
    //   this.dataPDFSaveComplianceStatus.ssfID = this.complaintInspection.sampleLabResults.ssfResultsList.sffId;
    //   this.dataPDFSaveComplianceStatus.bsNumber = this.complaintInspection.sampleLabResults.ssfResultsList.bsNumber;
    //   this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
    //   if (this.complaintInspection.sampleLabResults.savedPDFFiles.length === 0) {
    //     this.msService.msFuelInspectionScheduledSavePDFLIMS(this.complaintInspection.batchDetails.referenceNumber,
    //         this.complaintInspection.referenceNumber, this.dataPDFSaveComplianceStatus).subscribe(
    //         (data: any) => {
    //           this.complaintInspection = data;
    //           console.log(data);
    //           this.SpinnerService.hide();
    //           this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY');
    //         },
    //         error => {
    //           this.SpinnerService.hide();
    //           console.log(error);
    //           this.msService.showError('AN ERROR OCCURRED');
    //         }
    //     );
    //   } else {
    //     for (const savedPdf of this.complaintInspection.sampleLabResults.savedPDFFiles) {
    //       if (savedPdf.pdfName !== this.selectedPDFFileName) {
    //         this.msService.msFuelInspectionScheduledSavePDFLIMS(this.complaintInspection.batchDetails.referenceNumber,
    //             this.complaintInspection.referenceNumber, this.dataPDFSaveComplianceStatus).subscribe(
    //             (data: any) => {
    //               this.complaintInspection = data;
    //               console.log(data);
    //               this.SpinnerService.hide();
    //               this.msService.showSuccess('PDF LIMS SAVED SUCCESSFULLY');
    //             },
    //             error => {
    //               this.SpinnerService.hide();
    //               console.log(error);
    //               this.msService.showError('AN ERROR OCCURRED');
    //             }
    //         );
    //       } else {
    //         this.SpinnerService.hide();
    //         this.msService.showError('The Pdf selected With Name ' + this.selectedPDFFileName + ' Already Saved');
    //       }
    //     }
    //   }
    // }
  }

  onClickSaveSSFLabResultsComplianceStatus(valid: boolean) {
    // if (valid) {
    //   this.SpinnerService.show();
    //   this.dataSSFSaveComplianceStatus = {...this.dataSSFSaveComplianceStatus, ...this.ssfSaveComplianceStatusForm.value};
    //   this.dataSSFSaveComplianceStatus.ssfID = this.complaintInspection.sampleLabResults.ssfResultsList.sffId;
    //   this.dataSSFSaveComplianceStatus.bsNumber = this.complaintInspection.sampleLabResults.ssfResultsList.bsNumber;
    //   // this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
    //   this.msService.msFuelInspectionScheduledSaveSSFComplianceStatus(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSSFSaveComplianceStatus).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('LAB RESULTS COMPLIANCE STATUS SAVED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSaveScheduleRemediation(valid: boolean) {
    // if (valid) {
    //   this.SpinnerService.show();
    //   this.dataSaveScheduleRemediation = {...this.dataSaveScheduleRemediation, ...this.scheduleRemediationForm.value};
    //   this.msService.msFuelInspectionScheduledRemediation(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSaveScheduleRemediation).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('REMEDIATION SCHEDULE SAVED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSaveNotCompliantInvoice(valid: boolean) {
    // if (valid) {
    //   this.SpinnerService.show();
    //   this.dataSaveNotCompliantInvoice = {...this.dataSaveNotCompliantInvoice, ...this.notCompliantInvoiceForm.value};
    //   this.msService.msFuelInspectionNotCompliantRemediationInvoice(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSaveNotCompliantInvoice).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('REMEDIATION INVOICE GENERATED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSaveRemediationForm(valid: boolean) {
    // if (valid) {
    //   this.SpinnerService.show();
    //   this.dataSaveRemediation = {...this.dataSaveRemediation, ...this.remediationForm.value};
    //   this.msService.msFuelInspectionRemediation(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber, this.dataSaveRemediation).subscribe(
    //       (data: any) => {
    //         this.complaintInspection = data;
    //         console.log(data);
    //         this.SpinnerService.hide();
    //         this.msService.showSuccess('REMEDIATION DETAILS SAVED SUCCESSFULLY');
    //       },
    //       error => {
    //         this.SpinnerService.hide();
    //         console.log(error);
    //         this.msService.showError('AN ERROR OCCURRED');
    //       }
    //   );
    // }
  }

  onClickSaveEndRemediation() {
    // // if (valid) {
    // this.SpinnerService.show();
    // // this.dataSaveRemediation = {...this.dataSaveRemediation, ...this.remediationForm.value};
    // this.msService.msFuelInspectionEnd(this.complaintInspection.batchDetails.referenceNumber, this.complaintInspection.referenceNumber).subscribe(
    //     (data: any) => {
    //       this.complaintInspection = data;
    //       console.log(data);
    //       this.SpinnerService.hide();
    //       this.msService.showSuccess('FUEL INSPECTION ENDED SUCCESSFULLY');
    //     },
    //     error => {
    //       this.SpinnerService.hide();
    //       console.log(error);
    //       this.msService.showError('AN ERROR OCCURRED');
    //     }
    // );
    // // }
  }

  goBack() {
    this.router.navigate([`/complaint`]);
  }

  viewLIMSPDFRecord(data: LIMSFilesFoundDto, bsNumber: string) {
    console.log('TEST 101 REF NO VIEW: ' + data.fileName);
    this.viewLabResultsPdfFile(String(data.fileName), bsNumber, 'application/pdf');
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  viewLIMSPDFSaved(data: MSSSFPDFListDetailsDto) {
    console.log('TEST 101 REF NO VIEW FILE: ' + data.pdfSavedId);

    this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  viewComplaintFileSaved(data: ComplaintsFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
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
      case 'viewPDFRemarks':
        this.viewSavedRemarks(event.data);
        break;
    }
  }

  public onCustomComplaintFileViewAction(event: any): void {
    switch (event.action) {
      case 'viewPDFRecord':
        this.viewComplaintFileSaved(event.data);
        break;
    }
  }


  updateSelectedStatus() {
    const valueSelected = this.acceptRejectComplaintForm?.get('approved')?.value;
    if (valueSelected === 1) {
      this.selectComplaintStatus = 'Accept';
    } else {
      this.selectComplaintStatus = 'Reject';
    }
      console.log(`rejectAccept set to ${this.selectComplaintStatus}`);

  }

  onChangeSelectedDepartment() {
    this.departmentSelected = this.acceptRejectComplaintForm?.get('department')?.value;
  }
}
