import {AfterViewInit, Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {
    AllPermitDetailsDto,
    PermitEntityDetails,
    PlantDetailsDto,
    SectionDto,
    STA1,
    STA3
} from '../../core/store/data/qa/qa.model';
import swal from 'sweetalert2';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ApiEndpointService} from '../../core/services/endpoints/api-endpoint.service';
import {TableData} from "../../md/md-table/md-table.component";
// import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
// import {ModalComponent} from "ngb-modal";

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-dmark',
    templateUrl: './dmark.component.html',
    styleUrls: ['./dmark.component.css']
})
export class DmarkComponent implements OnInit, AfterViewInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    currDiv!: string;
    currDivLabel!: string;
    approveRejectSSCForm!: FormGroup;

    pdfSources: any;

    sta1Form: FormGroup;
    sta3FormA: FormGroup;
    sta3FormB: FormGroup;
    sta3FormC: FormGroup;
    sta3FormD: FormGroup;
    returnUrl: string;
    sta1: STA1;
    sta3: STA3;
    sections: SectionDto[];
    plants: PlantDetailsDto[];
    permitEntityDetails: PermitEntityDetails;
    public dataTable: DataTable;
    public permitID!: string;
    public allPermitDetails!: AllPermitDetailsDto;

    DMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;
    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;

    public tableData1: TableData;
    public tableData2: TableData;
    public tableData12: TableData;


    constructor(
        private route: ActivatedRoute,
        private router: Router,
        // private modalService: ModalComponent,
        private qaService: QaService,
        private formBuilder: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.getSelectedPermit();

        this.dataTable = {
            headerRow: ['documents attached details', 'file name', 'file type', 'document description', 'version number', 'view'],
            footerRow: ['documents attached details', 'file name', 'file type', 'document description', 'version number', 'view'],

            dataRows: [
                ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']
            ]
        };
        this.tableData1 = {
            headerRow: ['Remark Details', 'Action'],
            dataRows: [
                ['Completeness Remarks By QAM/HOD', ''],
                ['Review Remarks By PCM', ''],
                ['Recommendation Review Remarks By QAO', ''],
                ['PSC Members Approval/Rejection Remarks', ''],
                ['PCM Approval/Rejection Remarks', ''],

            ]
        };
        this.tableData2 = {
            headerRow: ['File Name', 'File Type', 'Document Description', 'Version Number', 'Action'],
            dataRows: [
                ['V1__8_QC.pdf', 'application/octet-stream', 'LAB RESULTS PDF', '1', ''],


            ]
        };
        this.tableData12 = {
            headerRow: ['Item', 'Fee'],
            dataRows: [
                ['Inspection Fee', 'Kshs 20,000'],
                ['Permit Fee ', 'Kshs 7500'],
                ['Tax Fee', 'Kshs 20,000'],
                ['Total', '47,500']

            ]
        };

        this.sta1Form = this.formBuilder.group({
            commodityDescription: ['', Validators.required],
            applicantName: ['', Validators.required],
            sectionId: ['', Validators.required],
            permitForeignStatus: ['', Validators.required],
            attachedPlant: ['', Validators.required],
            tradeMark: ['', Validators.required]

        });
        this.sta3FormA = this.formBuilder.group({
            produceOrdersOrStock: ['', Validators.required],
            issueWorkOrderOrEquivalent: ['', Validators.required],
            identifyBatchAsSeparate: ['', Validators.required],
            productsContainersCarryWorksOrder: ['', Validators.required],
            isolatedCaseDoubtfulQuality: ['', Validators.required]

        });

        this.sta3FormB = this.formBuilder.group({
            headQaQualificationsTraining: ['', Validators.required],
            reportingTo: ['', Validators.required],
            separateQcid: ['', Validators.required],
            testsRelevantStandard: ['', Validators.required],
            spoComingMaterials: ['', Validators.required],
            spoProcessOperations: ['', Validators.required],
            spoFinalProducts: ['', Validators.required],
            monitoredQcs: ['', Validators.required],
            qauditChecksCarried: ['', Validators.required],
            informationQcso: ['', Validators.required],

        });

        this.sta3FormC = this.formBuilder.group({
            mainMaterialsPurchasedSpecification: ['', Validators.required],
            adoptedReceiptMaterials: ['', Validators.required],
            storageFacilitiesExist: ['', Validators.required],

        });

        this.sta3FormD = this.formBuilder.group({
            stepsManufacture: ['', Validators.required],
            maintenanceSystem: ['', Validators.required],
            qcsSupplement: ['', Validators.required],
            qmInstructions: ['', Validators.required],
            testEquipmentUsed: ['', Validators.required],
            indicateExternalArrangement: ['', Validators.required],
            levelDefectivesFound: ['', Validators.required],
            levelClaimsComplaints: ['', Validators.required],
            independentTests: ['', Validators.required],
            indicateStageManufacture: ['', Validators.required],

        });


        this.approveRejectSSCForm = this.formBuilder.group({
            approvedRejectedScheme: ['', Validators.required],
            approvedRejectedSchemeRemarks: [''],
            // approvedRemarks: ['', Validators.required],
        });


        this.qaService.loadSectionList().subscribe(
            (data: any) => {
                this.sections = data;
                console.log(data);
            }
        );

        this.qaService.loadPlantList().subscribe(
            (data: any) => {
                this.plants = data;
                console.log(data);
            }
        );




    }

    get formApproveRejectSSC(): any {
        return this.approveRejectSSCForm.controls;
    }

    get formSta1Form(): any {
        return this.sta1Form.controls;
    }

    get formSta3FormA(): any {
        return this.sta3FormA.controls;
    }

    get formSta3FormB(): any {
        return this.sta3FormB.controls;
    }

    get formSta3FormC(): any {
        return this.sta3FormC.controls;
    }

    get formSta3FormD(): any {
        return this.sta3FormD.controls;
    }


    public getSelectedPermit(): void {
        this.route.fragment.subscribe(params => {
            // this.permitID = params;
            // console.log(this.permitID);
            this.qaService.loadPermitDetails(params).subscribe(
                (data: AllPermitDetailsDto) => {
                    this.allPermitDetails = data;
                    // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);
                    this.qaService.viewSTA1Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (data) => {
                            this.sta1 = data;
                            this.sta1Form.patchValue(this.sta1);
                        },
                    );
                    this.qaService.viewSTA3Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (data) => {
                            this.sta3 = data;
                            this.sta3FormA.patchValue(this.sta3);
                            this.sta3FormB.patchValue(this.sta3);
                            this.sta3FormC.patchValue(this.sta3);
                            this.sta3FormD.patchValue(this.sta3);
                        },
                    );
                    if (this.allPermitDetails.permitDetails.permitAwardStatus === true) {
                    this.qaService.loadCertificateDetailsPDF(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (data: any) => {
                            this.pdfSources = data;
                        },
                    );
                    }
                },
            );

        });

    }


    ngAfterViewInit() {
        $('#datatables').DataTable({
            'pagingType': 'full_numbers',
            'lengthMenu': [
                [10, 25, 50, -1],
                [10, 25, 50, 'All']
            ],
            responsive: true,
            language: {
        search: '_INPUT_',
        searchPlaceholder: 'Search records',
      }

    });

    let table: any;
    table = $(`#datatables`).DataTable();

    // Edit record
    table.on('click', '.edit', function (e) {
      let $tr = $(this).closest('tr');
      if ($($tr).hasClass('child')) {
        $tr = $tr.prev('.parent');
      }

      let data: any;
      data = table.row($tr).data();
      alert('You press on Row: ' + data[0] + ' ' + data[1] + ' ' + data[2] + '\'s row.');
      e.preventDefault();
    });

    // Delete a record
    table.on('click', '.remove', function (e) {
      const $tr = $(this).closest('tr');
      table.row($tr).remove().draw();
      e.preventDefault();
    });

        // Like record
        table.on('click', '.like', function (e) {
            alert('You clicked on Like button');
            e.preventDefault();
        });

        $('.card .material-datatables label').addClass('form-group');
    }

    submitApplicationForReview(): void {
        // tslint:disable-next-line:max-line-length
        if (this.allPermitDetails.permitDetails.permitForeignStatus === true && this.allPermitDetails.permitDetails.permitTypeID === ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID) {
            this.qaService.submitPermitForReview(String(this.allPermitDetails.permitDetails.id)).subscribe(
                (data: AllPermitDetailsDto) => {
                    this.allPermitDetails = data;
                    swal.fire({
                        title: 'DMARK SUBMITTED SUCCESSFULLY FOR REVIEW FROM PCM!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });

                    // this.onUpdateReturnToList();
                },
            );
        }
    }

    submitApplicationForReviewHOD(): void {
        // tslint:disable-next-line:max-line-length
        // if (this.allPermitDetails.permitDetails.permitForeignStatus === true && this.allPermitDetails.permitDetails.permitTypeID === ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID) {
        this.qaService.submitPermitForReviewHODQAM(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                swal.fire({
                    title: 'DMARK SUBMITTED SUCCESSFULLY FOR REVIEW HOD/RM!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });

                // this.onUpdateReturnToList();
            },
        );
        // }
    }

    submitApplication(): void {
        this.qaService.submitPermitApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                swal.fire({
                    title: 'DMARK SUBMITTED SUCCESSFULLY PENDING PAYMENT!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                this.router.navigate(['/invoiceDetails'], {fragment: this.allPermitDetails.batchID.toString()});

                // this.onUpdateReturnToList();
            },
        );
    }

    submitApprovalRejectionSSC(): void {
        console.log(this.approveRejectSSCForm.value);
        this.qaService.submitSSCApprovalRejection(String(this.allPermitDetails.permitDetails.id), this.approveRejectSSCForm.value).subscribe(
            (data: PermitEntityDetails) => {
                this.allPermitDetails.permitDetails = data;
                swal.fire({
                    title: 'PERMIT APPLICATION SSC UPDATED SUCCESSFULLY!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                // this.router.navigate(['/invoiceDetails'], {fragment: this.allPermitDetails.batchID.toString()});

                // this.onUpdateReturnToList();
            },
        );
    }

    onClickSaveSTA1(valid: boolean) {
        if (valid) {
            if (this.permitEntityDetails == null) {
                this.qaService.savePermitSTA1('1', this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        console.log(data);
                        swal.fire({
                            title: 'STA1 Form saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
// this.router.navigate(['/users-list']);
                    },
                );
            } else {
                this.qaService.updatePermitSTA1(String(this.permitEntityDetails.id), this.sta1Form.value).subscribe(
                    (data) => {
                        this.sta1 = data;
                        console.log(data);
                        swal.fire({
                            title: 'STA1 Form updated!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
// this.router.navigate(['/users-list']);
                    },
                );
            }
        }
    }

    onClickSaveSTA3A(valid: boolean) {
        if (valid) {
            console.log(this.permitEntityDetails.id.toString());
            this.qaService.savePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormA.value).subscribe(
                (data: any) => {
                    console.log(data);
                    swal.fire({
                        title: 'STA3: Factory Organisation Details saved!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
// this.router.navigate(['/users-list']);
                },
            );
        }
    }

    onClickUpdateSTA3B(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormB.value).subscribe(
                (data: any) => {
                    console.log(data);
                    swal.fire({
                        title: 'STA3: Factory Organisation Details updated!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
// this.router.navigate(['/users-list']);
                },
            );
        }
    }

    onClickUpdateSTA3C(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormC.value).subscribe(
                (data: any) => {
                    console.log(data);
                    swal.fire({
                        title: 'STA3: Quality Control/Inspection Staff Details saved!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
// this.router.navigate(['/users-list']);
                },
            );
        }
    }

    onClickUpdateSTA3D(valid: boolean) {
        if (valid) {
            this.qaService.updatePermitSTA3(this.permitEntityDetails.id.toString(), this.sta3FormD.value).subscribe(
                (data: any) => {
                    console.log(data);
                    swal.fire({
                        title: 'STA3 Form Completed! Proceed to submit application.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    // this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});
                },
            );
        }
    }

    goToPermit() {
        swal.fire({
            title: 'STA3 Form Completed! Proceed to submit application.',
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
        });
        this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});

    }

    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Ensure all required fields are items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

    openModal(divVal: string): void {
        const arrHead = ['approveSSC'];
        const arrHeadSave = ['AGREE/CONSENT TO SCHEME OF SUPERVISION AND CONTROL'];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }

        this.currDiv = divVal;
        // this.modalService.open();
    }

    submitRenewalApplication() {
        this.qaService.submitPermitRenewApplication(String(this.allPermitDetails.permitDetails.id)).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                console.log(AllPermitDetailsDto);
                swal.fire({
                    title: 'DMARK Renewed Successfully! Proceed to submit application.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                // this.router.navigate(['/permitdetails'], {fragment: String(this.AllPermitDetailsDto.permitDetails.id)});
            },
        );
    }

    goToInvoiceGenerated() {
        this.router.navigate(['/invoiceDetails'], {fragment: String(this.allPermitDetails.batchID)});
    }
}
