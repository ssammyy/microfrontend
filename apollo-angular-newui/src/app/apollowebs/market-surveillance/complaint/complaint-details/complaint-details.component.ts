import { Component, OnInit } from '@angular/core';
import {
  AllComplaintsDetailsDto,
  BSNumberSaveDto,
  ComplaintAdviceRejectDto,
  ComplaintApproveDto,
  ComplaintApproveRejectAdviceWhereDto,
  ComplaintAssignDto,
  ComplaintClassificationDto,
  ComplaintDetailsDto,
  ComplaintRejectDto,
  ComplaintsFilesFoundDto,
  CompliantRemediationDto,
  FuelEntityAssignOfficerDto,
  FuelEntityRapidTestDto,
  FuelInspectionDto,
  LaboratoryDto,
  LIMSFilesFoundDto,
  MsBroadProductCategory,
  MsDepartment,
  MsDivisionDetails, PredefinedResourcesRequired,
  MsProductCategories,
  MsProducts,
  MsProductSubcategory,
  MSRemarksDto,
  MSSSFPDFListDetailsDto,
  MsStandardProductCategory,
  PDFSaveComplianceStatusDto, RegionReAssignDto,
  RemediationDto,
  SampleCollectionDto,
  SampleCollectionItemsDto,
  SampleSubmissionDto,
  SampleSubmissionItemsDto,
  SSFSaveComplianceStatusDto, WorkPlanEntityDto, WorkPlanListDto,
} from '../../../../core/store/data/ms/ms.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  County,
  CountyService,
  loadCountyId,
  LoggedInUser, Region, RegionService,
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
  ProductSubcategory,
  StandardProductCategory,
} from '../../../../core/store/data/master/master.model';
import {Observable, throwError} from 'rxjs';

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
  submitted = false;
  selectedRefNo: string;
  region$: Observable<Region[]>;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  selectedBatchRefNo: string;
  selectedPDFFileName: string;
  selectedRegion = 0;
  selectedCounty = 0;
  selectedTown = 0;
  complaintInspection: AllComplaintsDetailsDto;
  currDiv!: string;
  currDivLabel!: string;
  selectComplaintStatus!: string;
  selectComplaintRejectionStatus!: string;
  remarksSavedForm!: FormGroup;
  acceptRejectComplaintForm!: FormGroup;
  adviceComplaintForm!: FormGroup;
  assignOfficerForm!: FormGroup;
  reAssignRegionForm!: FormGroup;
  classificationForm!: FormGroup;
  addNewScheduleForm!: FormGroup;
  addResourceRequiredForm!: FormGroup;


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
  dataSaveReAssignRegion: RegionReAssignDto;
  dataSaveAcceptance: ComplaintApproveRejectAdviceWhereDto;
  dataSaveAdviceWhere: ComplaintAdviceRejectDto;
  dataSaveComplaintClassification: ComplaintClassificationDto;



  labList: LaboratoryDto[];
  msDepartments: MsDepartment[];
  msDivisions: MsDivisionDetails[];
  standardProductCategory!: StandardProductCategory[];
  predefinedResourcesRequired!: PredefinedResourcesRequired[];
  productCategories!: ProductCategories[];
  broadProductCategory!: BroadProductCategory[];
  products!: Products[];
  productSubcategory!: ProductSubcategory[];
  standardProductCategorySelected!: number;
  productCategoriesSelected!: number;
  broadProductCategorySelected!: number;
  productsSelected!: number;
  productSubcategorySelected!: number;
  departmentSelected!: number;
  roles: string[];
  userLoggedInID: number;
  userProfile: LoggedInUser;
  dataSaveWorkPlan: WorkPlanEntityDto;
  dataSaveResourcesRequired: PredefinedResourcesRequired;
  dataSaveResourcesRequiredList: PredefinedResourcesRequired[] = [];
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
    },
    pager: {
      display: true,
      perPage: 10,
    },
  };


  constructor(
      private msService: MsService,
      // private dialog: MatDialog,
      private formBuilder: FormBuilder,
      private store$: Store<any>,
      private SpinnerService: NgxSpinnerService,
      private activatedRoute: ActivatedRoute,
      private countyService: CountyService,
      private regionService: RegionService,
      private townService: TownService,
      private router: Router) {
    this.region$ = regionService.entities$;
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

    this.reAssignRegionForm = this.formBuilder.group({
      reassignedRemarks: ['', Validators.required],
      regionID: ['', Validators.required],
      countyID: ['', Validators.required],
      townID: ['', Validators.required],
    });

    this.acceptRejectComplaintForm = this.formBuilder.group({
      approved: ['', Validators.required],
      mandateForOga: null,
      department: null,
      division: null,
      approvedRemarks: ['', Validators.required],
      advisedWhereToRemarks: null,
      amendmentRemarks: null,
    });

    this.adviceComplaintForm = this.formBuilder.group({
      advisedWhereToRemarks: ['', Validators.required],
      rejectedRemarks: ['', Validators.required],
      mandateForOga: 1,
    });

    this.addResourceRequiredForm = this.formBuilder.group({
      resourceName: ['', Validators.required],
    });

    this.classificationForm = this.formBuilder.group({
      productClassificationString: ['', Validators.required],
      broadProductCategoryString: ['', Validators.required],
      productCategoryString: ['', Validators.required],
      myProductString: ['', Validators.required],
      // productSubcategoryString: ['', Validators.required],
      standardTitle: ['', Validators.required],
      standardNumber: ['', Validators.required],
      classificationRemarks: ['', Validators.required],
    });

    this.addNewScheduleForm = this.formBuilder.group({
      complaintDepartment: null,
      divisionId: null,
      nameActivity: ['', Validators.required],
      rationale: ['', Validators.required],
      scopeOfCoverage: ['', Validators.required],
      timeActivityDate: ['', Validators.required],
      timeActivityEndDate: ['', Validators.required],
      county: null,
      townMarketCenter: null,
      locationActivityOther: null,
      standardCategory: null,
      broadProductCategory: null,
      productCategory: null,
      product: null,
      productSubCategory: null,
      resourcesRequired: null,
      budget: null,
      // remarks: ['', Validators.required],
    });


    this.remarksSavedForm = this.formBuilder.group({
      processBy: null,
      remarksStatus: null,
      remarksDescription: null,
    });

  }

  get formAssignOfficerForm(): any {
    return this.assignOfficerForm.controls;
  }

  get formAddResourceRequiredForm(): any {
    return this.addResourceRequiredForm.controls;
  }

  get formReAssignRegionForm(): any {
    return this.reAssignRegionForm.controls;
  }

  get formAcceptRejectForm(): any {
    return this.acceptRejectComplaintForm.controls;
  }

  get formAdviceComplaintForm(): any {
    return this.adviceComplaintForm.controls;
  }
  get formClassificationForm(): any {
    return this.classificationForm.controls;
  }

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
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
          console.log(this.countyService);
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


          // tslint:disable-next-line:max-line-length
          if (this.complaintInspection.complaintsDetails.assignedIOStatus === true && this.complaintInspection.complaintsDetails.classificationDetailsStatus === false) {
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

  }
  updateSelectedRegion() {
    this.selectedRegion = this.reAssignRegionForm?.get('regionID')?.value;
    console.log(`region set to ${this.selectedRegion}`);
  }

  updateSelectedCounty() {
    this.selectedCounty = this.reAssignRegionForm?.get('countyID')?.value;
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
    this.selectedTown = this.reAssignRegionForm?.get('townID')?.value;
    console.log(`town set to ${this.selectedTown}`);
  }

  onClickAddResource() {
    this.dataSaveResourcesRequired = this.addResourceRequiredForm.value;
    console.log(this.dataSaveResourcesRequired);
    this.dataSaveResourcesRequiredList.push(this.dataSaveResourcesRequired);
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

  openModalAddDetails(divVal: string): void {
    const arrHead = ['acceptRejectComplaint', 'notKebsMandate', 'assignHOF', 'assignOfficer', 'addClassificationDetails', 'startMSProcess', 'reassignRegion'];
    const arrHeadSave = ['ACCEPT/DEFER COMPLAINT', 'NOT WITHIN KEBS MANDATE', 'ASSIGN HOF', 'ASSIGN IO', 'ADD COMPLAINT PRODUCT CLASSIFICATION DETAILS', 'FILL IN MS-PROCESS DETAILS BELOW', 'RE-ASSIGN REGION'];

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

  onClickSaveReAssignHof(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveReAssignRegion = {...this.dataSaveReAssignRegion, ...this.reAssignRegionForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msComplaintUpdateReAssignRegionDetails(this.complaintInspection.complaintsDetails.refNumber,  this.dataSaveReAssignRegion).subscribe(
          (data: any) => {
            this.complaintInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('LOCATION DETAILS UPDATED SUCCESSFULLY');
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
      this.msService.msComplaintUpdateAssignIODetails(this.complaintInspection.complaintsDetails.refNumber,  this.dataSaveAssignOfficer).subscribe(
          (data: any) => {
            this.complaintInspection = data;
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('IO ASSIGNED SUCCESSFULLY');
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
    this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ACCEPT/REJECT COMPLAINT\' button to update details', 'COMPLAINT ACCEPT/REJECT SUCCESSFUL', () => {
          this.SaveAcceptRejectFormResults(valid);
        });
  }

  SaveAcceptRejectFormResults(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveAcceptance = {...this.dataSaveAcceptance, ...this.acceptRejectComplaintForm.value};
      switch (this.selectComplaintStatus) {
        case 'Accept':
          const newAcceptDto = new ComplaintApproveDto();
          newAcceptDto.department = this.dataSaveAcceptance.department;
          newAcceptDto.division = this.dataSaveAcceptance.division;
          newAcceptDto.approvedRemarks = this.dataSaveAcceptance.approvedRemarks;
          newAcceptDto.approved = this.dataSaveAcceptance.approved;
          // tslint:disable-next-line:max-line-length
          this.msService.msComplaintUpdateAcceptanceDetails(this.complaintInspection.complaintsDetails.refNumber, newAcceptDto).subscribe(
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
          break;
        case 'Reject':
          switch (this.selectComplaintRejectionStatus) {
            case 'Accept':
              const newAdviceWhereToDto = new ComplaintAdviceRejectDto();
              newAdviceWhereToDto.mandateForOga = this.dataSaveAcceptance.mandateForOga;
              newAdviceWhereToDto.advisedWhereToRemarks = this.dataSaveAcceptance.advisedWhereToRemarks;
              newAdviceWhereToDto.rejectedRemarks = this.dataSaveAcceptance.approvedRemarks;
              // tslint:disable-next-line:max-line-length
              this.msService.msComplaintUpdateMandateOGADetails(this.complaintInspection.complaintsDetails.refNumber, newAdviceWhereToDto).subscribe(
                  (data: any) => {
                    this.complaintInspection = data;
                    console.log(data);
                    this.acceptRejectComplaintForm.reset();
                    this.SpinnerService.hide();
                    this.msService.showSuccess('COMPLAINANT ADVISED WHERE TO TAKE THE COMPLAINT, SAVED SUCCESSFULLY');
                  },
                  error => {
                    this.SpinnerService.hide();
                    this.acceptRejectComplaintForm.reset();
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              break;
            case 'Reject':
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
              break;
            case 'RejectAmendment':
              const newRejectAmendmentDto = new ComplaintAdviceRejectDto();
              newRejectAmendmentDto.amendmentRemarks = this.dataSaveAcceptance.advisedWhereToRemarks;
              newRejectAmendmentDto.rejectedRemarks = this.dataSaveAcceptance.approvedRemarks;
              // tslint:disable-next-line:max-line-length
              this.msService.msComplaintUpdateForAmendmentDetails(this.complaintInspection.complaintsDetails.refNumber, newRejectAmendmentDto).subscribe(
                  (data: any) => {
                    this.complaintInspection = data;
                    console.log(data);
                    this.acceptRejectComplaintForm.reset();
                    this.SpinnerService.hide();
                    this.msService.showSuccess('COMPLAINANT TO AMEND, SENT SUCCESSFULLY');
                  },
                  error => {
                    this.SpinnerService.hide();
                    this.acceptRejectComplaintForm.reset();
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              break;
          }
          break;
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

  onClickSaveClassificationDetails(valid: boolean) {
    this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD CLASSIFICATION DETAILS\' button to update details Before Saving', 'CLASSIFICATION DETAILS ADDED SUCCESSFUL', () => {
          this.saveClassificationDetails(valid);
        });
  }

  saveClassificationDetails(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSaveComplaintClassification = {...this.dataSaveComplaintClassification, ...this.classificationForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msComplaintUpdateSaveClassificationDetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveComplaintClassification).subscribe(
          (data: any) => {
            this.complaintInspection = data;
            console.log(data);
            this.classificationForm.reset();
            this.SpinnerService.hide();
            this.msService.showSuccess('COMPLAINT PRODUCT CLASSIFICATION DETAILS, SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            this.classificationForm.reset();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }


  goBack() {
    this.router.navigate([`/complaint`]);
  }


  viewComplaintFileSaved(data: ComplaintsFilesFoundDto) {
    this.viewPdfFile(String(data.id), data.documentType, data.fileContentType);
  }


  viewSavedRemarks(data: MSRemarksDto) {
    this.currDivLabel = `REMARKS FOR ${data.processName}`;
    this.currDiv = 'viewSavedRemarks';
    this.remarksSavedForm.patchValue(data);

    window.$('#myModal1').modal('show');
    // this.viewPdfFile(String(data.pdfSavedId), data.pdfName, 'application/pdf');
    // this.router.navigate([`/epra/fuelInspection/details/`,data.referenceNumber]);
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

  updateSelectedRejectionStatus() {
    const valueSelected = this.acceptRejectComplaintForm?.get('mandateForOga')?.value;
    if (valueSelected === 1) {
      this.selectComplaintRejectionStatus = 'Accept';
    } else if (valueSelected === 2) {
      this.selectComplaintRejectionStatus = 'RejectAmendment';
    } else if (valueSelected === 0) {
      this.selectComplaintRejectionStatus = 'Reject';
    }
      console.log(`rejectAccept set to ${this.selectComplaintRejectionStatus}`);

  }

  onChangeSelectedDepartment() {
    this.departmentSelected = this.acceptRejectComplaintForm?.get('department')?.value;
  }

  onChangeSelectedProductClassification() {
    this.standardProductCategorySelected = this.classificationForm?.get('productClassification')?.value;
  }

  onChangeSelectedBroadProductCategory() {
    this.broadProductCategorySelected = this.classificationForm?.get('broadProductCategory')?.value;
  }

  onChangeSelectedProductCategory() {
    this.productCategoriesSelected = this.classificationForm?.get('productCategory')?.value;
  }

  onChangeSelectedMyProduct() {
    this.productsSelected = this.classificationForm?.get('myProduct')?.value;
  }

  onChangeSelectedProductSubcategory() {
    this.productSubcategorySelected = this.classificationForm?.get('productSubcategory')?.value;
  }

  onClickSaveWorkPlanScheduled() {
    this.submitted = true;
    if (this.addNewScheduleForm.valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'START MS-PROCESS\' button to update details Before Saving', 'COMPLAINT SCHEDULE DETAILS SAVED SUCCESSFUL', () => {
            this.saveWorkPlanScheduled();
          });
    }
  }

  saveWorkPlanScheduled() {
    if (this.addNewScheduleForm.valid) {
      this.SpinnerService.show();
      this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
      this.dataSaveWorkPlan.resourcesRequired = this.dataSaveResourcesRequiredList;
      // tslint:disable-next-line:max-line-length
      this.msService.msAddComplaintDetailsToWorkPlanScheduleDetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveWorkPlan).subscribe(
          (data: any) => {
            this.complaintInspection = data;
            console.log(data);
            this.classificationForm.reset();
            this.SpinnerService.hide();
            this.msService.showSuccess('MS-PROCESS DETAILS, SAVED SUCCESSFULLY');
          },
          error => {
            this.SpinnerService.hide();
            this.addNewScheduleForm.reset();
            console.log(error);
            // this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }

  viewWorkPlanCreated() {
    // tslint:disable-next-line:max-line-length
    this.router.navigate([`/complaintPlan/details/`, this.complaintInspection.workPlanRefNumber, this.complaintInspection.workPlanBatchRefNumber]);
  }

}
