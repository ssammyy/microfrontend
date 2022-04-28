import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AllBatchInvoiceDetailsDto, PermitInvoiceDto, StgInvoiceBalanceDto} from '../../../core/store/data/qa/qa.model';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import swal from 'sweetalert2';

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[];
}

declare const $: any;

@Component({
    selector: 'app-invoice-details',
    templateUrl: './invoice-details.component.html',
    styleUrls: ['./invoice-details.component.css']
})
export class InvoiceDetailsComponent implements OnInit, AfterViewInit {


    public dataTable: DataTable;
    public batchID!: string;
    public allPermitData: PermitInvoiceDto[];
    public allBatchInvoiceDetails!: AllBatchInvoiceDetailsDto;
    // pdfSources = "https://s23.q4cdn.com/202968100/files/doc_downloads/test.pdf";
    pdfSources: any;
    invoiceBalance: StgInvoiceBalanceDto;
    submitted = false;

    stkPushForm!: FormGroup;

    constructor(
        private route: ActivatedRoute,
        private formBuilder: FormBuilder,
        private store$: Store<any>,
        private qaService: QaService
    ) {
    }

    ngOnInit(): void {
        this.getSelectedBatch();

        this.stkPushForm = this.formBuilder.group({
            entityValueID: [''],
            phoneNumber: ['', Validators.required]
        });
    }

    get formStkPushForm(): any {
        return this.stkPushForm.controls;
    }

    public getSelectedBatch(): void {
        this.route.fragment.subscribe(params => {
            this.batchID = params;
            console.log(this.batchID);
            let formattedArray = [];
            this.qaService.loadInvoiceDetails(this.batchID).subscribe(
                (data: AllBatchInvoiceDetailsDto) => {
                    this.allBatchInvoiceDetails = data;
                    console.log(this.allBatchInvoiceDetails);

                    this.allPermitData = data.allRelatedBatchInvoices;
                    // tslint:disable-next-line:max-line-length
                    formattedArray = data.allRelatedBatchInvoices.map(i => [i.sageInvoiceNumber, i.commodityDescription, i.brandName, i.totalAmount, i.paidStatus]);

                    this.dataTable = {
                        headerRow: ['INVOICE REF NO', 'COMMODITY DESCRIPTION', 'BRAND NAME', 'TOTAL AMOUNT', 'PAID STATUS', 'Actions'],
                        footerRow: ['INVOICE REF NO', 'COMMODITY DESCRIPTION', 'BRAND NAME', 'TOTAL AMOUNT', 'PAID STATUS', 'Actions'],

                        dataRows: formattedArray
                        // dataRows: [['KIMSDM#202106290C9', 'Wine', 'Wine', '191400', 'PAID', '']

                    };
                    // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);

                },
            );

            this.qaService.loadInvoiceDetailsPDF(this.batchID).subscribe(
                (data: any) => {
                    this.pdfSources = data;
                },
            );

            this.qaService.loadInvoiceDetailsBalance(this.batchID).subscribe(
                (data: any) => {
                    this.invoiceBalance = data;
                },
            );
        });

    }

    onRemoveInvoice() {

    }

    reloadPage() {
        location.reload();
    }

    public stkPush(): void {
        this.submitted = true;
        // stop here if form is invalid
        if (this.stkPushForm.invalid) {
            return;
        }
        if (this.submitted) {
            this.stkPushForm.controls['entityValueID'].setValue(this.allBatchInvoiceDetails.batchDetails.batchID);
            console.log(this.stkPushForm.value.phoneNumber);
            console.log(this.stkPushForm.value.entityValueID);
            this.qaService.pushSTKInvoicePermit(this.stkPushForm.value).subscribe(
                (data: any) => {
                    swal.fire({
                        title: 'Check You phone for and enter your MPesa pin to complete the payment',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        } else {
            // this.store$.dispatch(loadResponsesFailure({
            //     error: {
            //         payload: 'REQUIER PHONE NUMBER',
            //         status: 100,
            //         response: '05'
            //     }
            // }));
        }
    }

    ngAfterViewInit = () => {
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
}
