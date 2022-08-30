import {Component, OnInit} from '@angular/core';
import {
    AllComplaintsDetailsDto,
    ComplaintAdviceRejectDto,
    ComplaintApproveDto,
    ComplaintApproveRejectAdviceWhereDto,
    ComplaintAssignDto,
    ComplaintClassificationDto,
    ComplaintRejectDto,
    ComplaintsFilesFoundDto,
    MsBroadProductCategory,
    MsDepartment,
    MsDivisionDetails,
    MsProducts,
    MsProductSubcategory,
    MSRemarksDto,
    MsStandardProductCategory,
    WorkPlanEntityDto,
} from '../../../../../core/store/data/ms/ms.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
    County,
    CountyService,
    loadCountyId,
    LoggedInUser,
    selectCountyIdData,
    selectUserInfo,
    Town,
    TownService,
} from '../../../../../core/store';
import {MsService} from '../../../../../core/store/data/ms/ms.service';
import {Store} from '@ngrx/store';
import {NgxSpinnerService} from 'ngx-spinner';
import {ActivatedRoute, Router} from '@angular/router';
import {
    BroadProductCategory,
    ProductCategories,
    Products,
    ProductSubcategory,
    StandardProductCategory,
} from '../../../../../core/store/data/master/master.model';
import {Observable, throwError} from 'rxjs';

declare global {
    interface Window {
        $: any;
    }
}

@Component({
    selector: 'app-manufacturer-complaint-details',
    templateUrl: './manufacturer-complaint-details.component.html',
    styleUrls: ['./manufacturer-complaint-details.component.css'],
})
export class ManufacturerComplaintDetailsComponent implements OnInit {

    active: Number = 0;
    submitted = false;
    selectedRefNo: string;
    county$: Observable<County[]>;
    town$: Observable<Town[]>;
    selectedBatchRefNo: string;
    selectedPDFFileName: string;
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
    classificationForm!: FormGroup;
    addNewScheduleForm!: FormGroup;


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
    dataSaveAcceptance: ComplaintApproveRejectAdviceWhereDto;
    dataSaveAdviceWhere: ComplaintAdviceRejectDto;
    dataSaveComplaintClassification: ComplaintClassificationDto;


    msDepartments: MsDepartment[];
    msDivisions: MsDivisionDetails[];
    standardProductCategory!: StandardProductCategory[];
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
            mandateForOga: null,
            department: null,
            division: null,
            approvedRemarks: ['', Validators.required],
            advisedWhereToRemarks: null,
        });

        this.adviceComplaintForm = this.formBuilder.group({
            advisedWhereToRemarks: ['', Validators.required],
            rejectedRemarks: ['', Validators.required],
            mandateForOga: 1,
        });

        this.classificationForm = this.formBuilder.group({
            productClassification: ['', Validators.required],
            broadProductCategory: ['', Validators.required],
            productCategory: ['', Validators.required],
            myProduct: ['', Validators.required],
            productSubcategory: ['', Validators.required],
            classificationRemarks: ['', Validators.required],
        });

        this.addNewScheduleForm = this.formBuilder.group({
            complaintDepartment: null,
            divisionId: null,
            nameActivity: ['', Validators.required],
            timeActivityDate: ['', Validators.required],
            county: null,
            townMarketCenter: null,
            locationActivityOther: null,
            standardCategory: null,
            broadProductCategory: null,
            productCategory: null,
            product: null,
            productSubCategory: null,
            resourcesRequired: ['', Validators.required],
            budget: ['', Validators.required],
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

    openModalAddDetails(divVal: string): void {
        const arrHead = ['acceptRejectComplaint', 'notKebsMandate', 'assignHOF', 'assignOfficer', 'addClassificationDetails', 'startMSProcess'];
        const arrHeadSave = ['ACCEPT/REJECT COMPLAINT', 'NOT WITHIN KEBS MANDATE', 'ASSIGN HOF', 'ASSIGN IO', 'ADD COMPLAINT PRODUCT CLASSIFICATION DETAILS', 'FILL IN MS-PROCESS DETAILS BELOW'];

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
            this.msService.msComplaintUpdateAssignHOFDetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveAssignOfficer).subscribe(
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

    onClickSaveAssignIO(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.dataSaveAssignOfficer = {...this.dataSaveAssignOfficer, ...this.assignOfficerForm.value};
            // tslint:disable-next-line:max-line-length
            this.msService.msComplaintUpdateAssignIODetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveAssignOfficer).subscribe(
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
        } else {
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
        if (this.addNewScheduleForm.invalid) {
            return;
        }
        if (this.addNewScheduleForm.valid) {
            this.SpinnerService.show();
            this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
            // tslint:disable-next-line:max-line-length
            this.msService.msAddComplaintDetailsToWorkPlanScheduleDetails(this.complaintInspection.complaintsDetails.refNumber, this.dataSaveWorkPlan).subscribe(
                (data: any) => {
                    this.complaintInspection = data;
                    console.log(data);
                    this.classificationForm.reset();
                    this.SpinnerService.hide();
                    this.msService.showSuccess('MS-PROCESS DETAILS, SAVED SUCCESSFULLY');
                }
                // ,
                // error => {
                //   this.SpinnerService.hide();
                //   this.addNewScheduleForm.reset();
                //   console.log(error);
                //   this.msService.showError('AN ERROR OCCURRED');
                // },
            );

        }
    }

    viewWorkPlanCreated() {
        this.router.navigate([`/workPlan/details/`, this.complaintInspection.workPlanRefNumber, this.complaintInspection.workPlanBatchRefNumber]);
    }

}
