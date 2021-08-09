import {AfterViewInit, Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {
    AllPermitDetailsDto, FilesListDto,
    PermitEntityDetails, PermitEntityDto,
    PlantDetailsDto,
    SectionDto, SSFPDFListDetailsDto,
    STA1,
    STA3
} from '../../core/store/data/qa/qa.model';
import swal from 'sweetalert2';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ApiEndpointService} from '../../core/services/endpoints/api-endpoint.service';
import {TableData} from '../../md/md-table/md-table.component';
import {FileUploadValidators} from '@iplab/ngx-file-upload';
import {LoadingService} from '../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
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
    labResultsStatus!: string;
    labResultsRemarks!: string;
    approveRejectSSCForm!: FormGroup;
    uploadForm!: FormGroup;
    uploadedFile: File;
    uploadedFiles: FileList;
    upLoadDescription: string;
    loading = false;

    pdfSources: any;
    pdfInvoiceBreakDownSources: any;
    pdfSourcesScheme: any;
    pdfUploadsView: any;

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
    sta3FileList: FilesListDto[];
    ordinaryFilesList: FilesListDto[];
    labResultsDetailsList: SSFPDFListDetailsDto[];
    olderVersionDetailsList: PermitEntityDto[];
    permitEntityDetails: PermitEntityDetails;
    public dataTable: DataTable;
    public permitID!: string;
    allPermitDetails!: AllPermitDetailsDto;

    private filesControl = new FormControl(null, FileUploadValidators.filesLimit(2));

    DMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;
    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;

    public tableData1: TableData;
    public tableData2: TableData;
    public tableData3: TableData;
    public tableData4: TableData;
    public tableData5: TableData;
    public tableData12: TableData;
    blob: Blob;


    constructor(
        private route: ActivatedRoute,
        private router: Router,
        // private modalService: ModalComponent,
        private qaService: QaService,
        private _loading: LoadingService,
        private SpinnerService: NgxSpinnerService,
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
        this.remarksDetails();

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

        this.uploadForm = this.formBuilder.group({
            upLoadDescription: ['', Validators.required],
            uploadedFile: this.filesControl
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

    remarksDetails() {
        const formattedArrayRemarks = [];

        if (this.allPermitDetails?.remarksDetails?.hofQamCompleteness !== null) {
            formattedArrayRemarks.push(['Completeness Remarks By HOD', '', 'hofQamCompletenessRemarks']);
        }
        // if (this.allPermitDetails.remarksDetails.pcmReviewApprovalRemarks !== null) {
        formattedArrayRemarks.push(['Review Remarks By PCM', '', 'reviewRemarksPCMRemarks']);
        // }
        // if (this.allPermitDetails.remarksDetails.pscMemberApprovalRemarks !== null) {
        formattedArrayRemarks.push(['PSC Members Approval/Rejection Remarks', '', 'pscApprovalRejectionRemarks']);
        // }
        // if (this.allPermitDetails.remarksDetails.pcmApprovalRemarks !== null) {
        formattedArrayRemarks.push(['PCM Approval/Rejection Remarks', '', 'pcmApprovalRejectionRemarks']);
        // }

        this.tableData1 = {
            headerRow: ['Remark Details', 'Action'],
            dataRows: formattedArrayRemarks
        };
    }

    get formUploadForm(): any {
        return this.uploadForm.controls;
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
            let formattedArraySta3 = [];
            let formattedArrayOrdinaryFiles = [];
            let formattedArrayLabResultsList = [];
            let formattedArrayOlderVersionList = [];
            const formattedArrayInvoiceDetailsList = [];
            this.qaService.loadPermitDetails(params).subscribe(
                (data: AllPermitDetailsDto) => {
                    this.allPermitDetails = data;
                    this.qaService.viewSTA1Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (dataSta1) => {
                            this.sta1 = dataSta1;
                            this.sta1Form.patchValue(this.sta1);
                        },
                    );
                    this.qaService.viewSTA3Details(String(this.allPermitDetails.permitDetails.id)).subscribe(
                        (dataSta3) => {
                            this.sta3 = dataSta3;
                            this.sta3FormA.patchValue(this.sta3);
                            this.sta3FormB.patchValue(this.sta3);
                            this.sta3FormC.patchValue(this.sta3);
                            this.sta3FormD.patchValue(this.sta3);
                        },
                    );
                    if (this.allPermitDetails.sta3FilesList !== []) {
                        this.sta3FileList = this.allPermitDetails.sta3FilesList;
                        // tslint:disable-next-line:max-line-length
                        formattedArraySta3 = this.sta3FileList.map(i => [i.name, i.fileType, i.documentType, i.versionNumber, i.id, i.document]);
                        this.tableData2 = {
                            headerRow: ['File Name', 'File Type', 'Document Description', 'Version Number', 'Action'],
                            dataRows: formattedArraySta3
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
                    if (this.allPermitDetails.labResultsList !== []) {
                        this.labResultsDetailsList = this.allPermitDetails.labResultsList;
                        // tslint:disable-next-line:max-line-length
                        formattedArrayLabResultsList = this.labResultsDetailsList.map(i => [i.pdfName, i.complianceStatus, i.sffId, i.complianceRemarks, i.pdfSavedId]);
                        this.tableData4 = {
                            headerRow: ['File Name', 'Compliant Status', 'View Remarks', 'View PDF'],
                            dataRows: formattedArrayLabResultsList
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
                        this.tableData12 = {
                            headerRow: ['Item', 'Details/Fee'],
                            dataRows: [
                                ['Invoice Ref No', this.allPermitDetails.invoiceDetails.invoiceRef],
                                ['Description', this.allPermitDetails.invoiceDetails.description],
                                ['Sub Total Before Tax', `KSH ${this.allPermitDetails.invoiceDetails.subTotalBeforeTax}`],
                                ['Tax Amount', `KSH ${this.allPermitDetails.invoiceDetails.taxAmount}`],
                                ['Total Amount', `KSH ${this.allPermitDetails.invoiceDetails.totalAmount}`]

                            ]
                        };
                    }
                },
            );

        });

    }

    openModalRemarks(divVal: string): void {
        const arrHead = ['hofQamCompletenessRemarks', 'reviewRemarksPCMRemarks', 'recommendationRemarks', 'pscApprovalRejectionRemarks', 'pcmApprovalRejectionRemarks'];
        // tslint:disable-next-line:max-line-length
        const arrHeadSave = ['Completeness Remarks', 'PCM Review Remarks', 'Recommendation', 'PSC Remarks', 'PCM Approval/Rejection Remarks'];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }
        this.currDiv = divVal;
    }

    viewPdfFile(pdfId: string, fileName: string, applicationType: string): void {
        this.qaService.loadFileDetailsPDF(pdfId).subscribe(
            (dataPdf: any) => {
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    reloadCurrentRoute() {
        location.reload();
    }

    downloadPdfFile(data: string, fileName: string): void {
        this.blob = new Blob([data], {type: 'application/pdf'});

        // tslint:disable-next-line:prefer-const
        let downloadURL = window.URL.createObjectURL(this.blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = fileName;
        link.click();
    }

    getPdfFile(pdfId: string): any {
        this.qaService.loadFileDetailsPDF(pdfId).subscribe(
            (dataPdf: any) => {
                return dataPdf;
            },
        );
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
                    this.reloadCurrentRoute();
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
                this.reloadCurrentRoute();
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
                this.reloadCurrentRoute();
                swal.fire({
                    title: 'DMARK SUBMITTED SUCCESSFULLY PENDING PAYMENT!',
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

    submitApprovalRejectionSSC(): void {
        console.log(this.approveRejectSSCForm.value);
        this.SpinnerService.show();
        // tslint:disable-next-line:max-line-length
        this.qaService.submitSSCApprovalRejection(String(this.allPermitDetails.permitDetails.id), this.approveRejectSSCForm.value).subscribe(
            (data: PermitEntityDetails) => {
                this.allPermitDetails.permitDetails = data;
                this.reloadCurrentRoute();
                this.SpinnerService.hide();
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

    goToPermitOlderVersion(permitID: string) {
        swal.fire({
            title: 'YOU ARE VIEWING THE OLDER VERSION.',
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
        });
        this.router.navigate(['/permitdetails'], {fragment: permitID});

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
                this.reloadCurrentRoute();
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

    viewLabRemarks(status: string, remarksValue: string) {
        this.currDiv = 'viewLabResultsRemarks';
        this.currDivLabel = 'LAB RESULTS DETAILS';
        switch (status) {
            case 'true':
                this.labResultsStatus = 'COMPLIANT';
                break;
            case 'false':
                this.labResultsStatus = 'NOT-COMPLIANT';
                break;
        }

        this.labResultsRemarks = remarksValue;
    }

    openModalUpload(viewDiv: string) {
        this.currDiv = viewDiv;
        this.currDivLabel = 'Upload PDF Documents Only';
    }

    openModalViewUpload(viewDiv: string) {
        this.currDiv = viewDiv;
        this.currDivLabel = 'VIEWS PDF Documents Only';
    }

    uploadDocument() {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            // tslint:disable-next-line:max-line-length
            this.qaService.uploadFile(String(this.allPermitDetails.permitDetails.id), this.upLoadDescription, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.reloadCurrentRoute();
                    console.log(data);
                    swal.fire({
                        title: 'UPLOADED SUCCESSFULLY',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    // this.router.navigate(['/permitdetails'], {fragment: this.permitEntityDetails.id.toString()});
                },
            );
            // this.router.navigate(['/permitdetails'], {fragment: String(this.sta1.id)});
        }
    }

    goToPayment() {
        this.router.navigate(['/invoice/consolidate_invoice']);
    }

    goToNewApplication() {
        this.router.navigate(['/dmark/newDmarkPermit']);
    }
}
