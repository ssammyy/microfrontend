import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import swal from "sweetalert2";
import {
  BatchFileFuelSaveDto,
  BSNumberSaveDto, CompliantRemediationDto,
  FuelEntityAssignOfficerDto,
  FuelEntityRapidTestDto,
  FuelInspectionDto,
  LIMSFilesFoundDto, PDFSaveComplianceStatusDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto, SSFSaveComplianceStatusDto
} from "../../../../core/store/data/ms/ms.model";
import {MsService} from "../../../../core/store/data/ms/ms.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
declare global {
  interface Window {
    $:any;
  }
}

@Component({
  selector: 'app-view-fuel-sheduled-details',
  templateUrl: './view-fuel-sheduled-details.component.html',
  styleUrls: ['./view-fuel-sheduled-details.component.css']
})
export class ViewFuelSheduledDetailsComponent implements OnInit {
  active: Number = 0;
  selectedRefNo: string;
  selectedPDFFileName: string;
  fuelInspection: FuelInspectionDto;
  currDiv!: string;
  currDivLabel!: string;
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
  dataSaveAssignOfficer: FuelEntityAssignOfficerDto;
  dataSaveRapidTest: FuelEntityRapidTestDto;
  dataSaveSampleCollect: SampleCollectionDto;
  dataSaveSampleCollectItems: SampleCollectionItemsDto;
  dataSaveSampleSubmit: SampleSubmissionDto;
  dataSaveSampleSubmitParam: SampleSubmissionItemsDto;
  dataSaveSampleSubmitBSNumber: BSNumberSaveDto;
  dataPDFSaveComplianceStatus: PDFSaveComplianceStatusDto;
  dataSSFSaveComplianceStatus: SSFSaveComplianceStatusDto;
  dataSaveScheduleRemediation: CompliantRemediationDto;
  dataSaveNotCompliantInvoice: CompliantRemediationDto;

  attachments: any[];
  comments: any[];
  consignmentItems: any[];
  paymentFees: any[];
  configurations: any[];
  demandNotes: any[];
  checkLists: any[];
  supervisorTasks: any[]
  supervisorCharge: boolean = false
  inspectionOfficer: boolean = false

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
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      id: {
        title: '#',
        type: 'string',
        filter: false
      },
      productBrandName: {
        title: 'PRODUCT BRAND NAME',
        type: 'string'
      },
      batchNo: {
        title: 'BATCH NO',
        type: 'string'
      },
      batchSize: {
        title: 'BATCH SIZE',
        type: 'string'
      },
      sampleSize: {
        title: 'SAMPLE SIZE',
        type: 'string'
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
      perPage: 20
    }
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
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      id: {
        title: '#',
        type: 'string',
        filter: false
      },
      parameters: {
        title: 'PARAMETERS',
        type: 'string'
      },
      laboratoryName: {
        title: 'LABORATORY NAME',
        type: 'string'
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
      perPage: 20
    }
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
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      param: {
        title: 'PARAM',
        type: 'string',
        filter: false
      },
      result: {
        title: 'RESULT',
        type: 'string',
        filter: false
      },
      method: {
        title: 'METHOD',
        type: 'string',
        filter: false
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
      perPage: 20
    }
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
        {name: 'saveRecord', title: '<i class="btn btn-sm btn-primary">Save PDF</i>'}
      ],
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      fileName: {
        title: 'FILE NAME',
        type: 'string',
        filter: false
      },
      fileSavedStatus: {
        title: 'FILE SAVED STATUS',
        type: 'boolean',
        filter: false
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
      perPage: 20
    }
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
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      pdfName: {
        title: 'PDF NAME',
        type: 'string',
        filter: false
      },
      complianceRemarks: {
        title: 'COMPLIANCE REMARKS',
        type: 'string',
        filter: false
      },
      complianceStatus: {
        title: 'COMPLIANCE STATUS',
        type: 'boolean',
        filter: false
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
      perPage: 20
    }
  };


  constructor(
      private msService: MsService,
              // private dialog: MatDialog,
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
              private activatedRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.selectedRefNo = rs.get('referenceNumber');
          this.loadData(this.selectedRefNo)
        }
    );

    this.assignOfficerForm = this.formBuilder.group({
      assignedUserID: ['', Validators.required],
      remarks: null,
    });

    this.rapidTestForm = this.formBuilder.group({
      rapidTestStatus: ['', Validators.required],
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
      batchNo: ['', Validators.required],
      batchSize: ['', Validators.required],
      sampleSize: ['', Validators.required],
    });

    this.sampleSubmitForm = this.formBuilder.group({
      nameProduct: ['',Validators.required],
      packaging: ['',Validators.required],
      labellingIdentification: ['',Validators.required],
      fileRefNumber: ['',Validators.required],
      referencesStandards: ['',Validators.required],
      sizeTestSample: ['',Validators.required],
      sizeRefSample: ['',Validators.required],
      condition: ['',Validators.required],
      sampleReferences: ['',Validators.required],
      sendersName: ['',Validators.required],
      designation: ['',Validators.required],
      address: ['',Validators.required],
      sendersDate: ['',Validators.required],
      receiversName: ['',Validators.required],
      testChargesKsh: ['',Validators.required],
      receiptLpoNumber: ['',Validators.required],
      invoiceNumber: ['',Validators.required],
      disposal: ['',Validators.required],
      remarks: ['',Validators.required],
      sampleCollectionNumber: ['',Validators.required],
    });


    this.sampleSubmitParamsForm = this.formBuilder.group({
      parameters: ['', Validators.required],
      laboratoryName: ['', Validators.required]
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

  }

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formRapidTestForm(): any {
    return this.rapidTestForm.controls;
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

  private loadData(referenceNumber: string): any {
    this.SpinnerService.show()
    // let params = {'personal': this.personalTasks}
    this.fuelInspection = this.msService.fuelInspectionDetailsExamples()
    // this.totalCount = this.loadedData.fuelInspectionDto.length;
    // this.dataSet.load(this.loadedData.fuelInspectionDto);
    this.SpinnerService.hide();
    // this.msService.msFuelInspectionList(referenceNumber,String(page),String(records)).subscribe(
    //     (data) => {
    //       this.loadedData = data;
    //       this.totalCount = this.loadedData.length;
    //       this.dataSet.load(this.loadedData);
    //       this.SpinnerService.hide();
    //       console.log(data);
    //     },
    //     error => {
    //       this.SpinnerService.hide();
    //       console.log(error)
    //       this.msService.showError("AN ERROR OCCURRED")
    //     }
    // );

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
    const arrHead = ['scheduleRemediationInvoicePaid', 'assignOfficer', 'rapidTest', 'addBsNumber', 'ssfAddComplianceStatus', 'scheduleRemediation', 'notCompliantInvoice'];
    const arrHeadSave = ['SCHEDULE REMEDIATION DATE INVOICE PAID','SELECT OFFICER TO ASSIGN', 'RAPID TEST RESULTS', 'ADD BS NUMBER', 'ADD SSF LAB RESULTS COMPLIANCE STATUS', 'SCHEDULE REMEDIATION DATE', 'ADD REMEDIATION INVOICE DETAILS'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }
    this.currDiv = divVal;
  }

  onClickSaveAssignOfficerBatch(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
      this.msService.msFuelInspectionScheduledAssignOfficer(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataSaveAssignOfficer).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("OFFICER ASSIGNED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveRapidTestResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveRapidTest = {...this.dataSaveRapidTest, ...this.rapidTestForm.value};
      this.msService.msFuelInspectionScheduledRapidTest(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataSaveRapidTest).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("RAPID TEST RESULTS SAVED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveSampleCollected(valid: boolean,valid2: boolean) {
    if (valid&&valid2) {
      this.SpinnerService.show();
      this.dataSaveSampleCollect = {...this.dataSaveSampleCollect, ...this.sampleCollectForm.value};
      this.dataSaveSampleCollectItems = {...this.dataSaveSampleCollectItems, ...this.sampleCollectItemsForm.value};

      let sampleCollectionItemsDto : SampleCollectionItemsDto[] = [];
      sampleCollectionItemsDto.push(this.dataSaveSampleCollectItems)
      this.dataSaveSampleCollect.productsList = sampleCollectionItemsDto

      this.msService.msFuelInspectionScheduledAddSampleCollection(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber, this.dataSaveSampleCollect).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("SAMPLE COLLECTION SAVED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveSampleSubmitted(valid: boolean,valid2: boolean) {
    if (valid&&valid2) {
      this.SpinnerService.show();
      this.dataSaveSampleSubmit = {...this.dataSaveSampleSubmit, ...this.sampleSubmitForm.value};
      this.dataSaveSampleSubmitParam = {...this.dataSaveSampleSubmitParam, ...this.sampleSubmitParamsForm.value};

      let sampleSubmissionParamsDto : SampleSubmissionItemsDto[] = [];
      sampleSubmissionParamsDto.push(this.dataSaveSampleSubmitParam)
      this.dataSaveSampleSubmit.parametersList = sampleSubmissionParamsDto

      this.msService.msFuelInspectionScheduledAddSampleSubmission(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber, this.dataSaveSampleSubmit).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("SAMPLE SUBMISSION SAVED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveBSNumber(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveSampleSubmitBSNumber = {...this.dataSaveSampleSubmitBSNumber, ...this.sampleSubmitBSNumberForm.value};
      this.msService.msFuelInspectionScheduledAddSampleSubmissionBSNumber(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataSaveSampleSubmitBSNumber).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("BS NUMBER ADDED SUCCESSFULLYLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
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
      this.msService.msFuelInspectionScheduledSavePDFLIMS(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataPDFSaveComplianceStatus).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("PDF LIMS SAVED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveSSFLabResultsComplianceStatus(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSSFSaveComplianceStatus = {...this.dataSSFSaveComplianceStatus, ...this.ssfSaveComplianceStatusForm.value};
      this.dataSSFSaveComplianceStatus.ssfID = this.fuelInspection.sampleLabResults.ssfResultsList.sffId;
      this.dataSSFSaveComplianceStatus.bsNumber = this.fuelInspection.sampleLabResults.ssfResultsList.bsNumber;
      // this.dataPDFSaveComplianceStatus.PDFFileName = this.selectedPDFFileName;
      this.msService.msFuelInspectionScheduledSaveSSFComplianceStatus(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataSSFSaveComplianceStatus).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("LAB RESULTS COMPLIANCE STATUS SAVED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveScheduleRemediation(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveScheduleRemediation = {...this.dataSaveScheduleRemediation, ...this.scheduleRemediationForm.value};
      this.msService.msFuelInspectionScheduledRemediation(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataSaveScheduleRemediation).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("REMEDIATION SCHEDULE SAVED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  onClickSaveNotCompliantInvoice(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveNotCompliantInvoice = {...this.dataSaveNotCompliantInvoice, ...this.notCompliantInvoiceForm.value};
      this.msService.msFuelInspectionNotCompliantRemediationInvoice(this.fuelInspection.batchDetails.referenceNumber, this.fuelInspection.referenceNumber,this.dataSaveNotCompliantInvoice).subscribe(
          (data: any) => {
            this.fuelInspection = data
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("REMEDIATION INVOICE GENERATED SUCCESSFULLY")
          },
          error => {
            this.SpinnerService.hide();
            console.log(error)
            this.msService.showError("AN ERROR OCCURRED")
          }
      );
    }
  }

  goBack() {
    console.log("TEST 101"+this.fuelInspection.batchDetails.referenceNumber)
    this.router.navigate([`/epra`,this.fuelInspection.batchDetails.referenceNumber]);
  }

  viewLIMSPDFRecord(data: LIMSFilesFoundDto) {
    console.log("TEST 101 REF NO VIEW: "+data.fileName)
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  saveLIMSPDFRecord(data: LIMSFilesFoundDto) {
    console.log("TEST 101 REF NO SAVE: "+data.fileName)
    this.selectedPDFFileName = data.fileName;
    this.currDivLabel = `ADD COMPLIANCE STATUS FOR PDF # ${this.selectedPDFFileName}`;
    this.currDiv ='pdfSaveCompliance';

    window.$('#myModal1').modal('show');
    // $('#myModal1').modal('show');
    // this.openModalAddDetails('assignOfficer')
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
  }

  public onCustomLIMSPDFAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewLIMSPDFRecord(event.data);
        break;
      case 'saveRecord':
        this.saveLIMSPDFRecord(event.data);
        break;
    }
  }

}
