import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {ConsignmentStatusComponent} from "../../../core/shared/customs/consignment-status/consignment-status.component";
import {DatePipe} from "@angular/common";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";


@Component({
    selector: 'app-currency-exchange-rates',
    templateUrl: './currency-exchange-rates.component.html',
    styleUrls: ['./currency-exchange-rates.component.css']
})
export class CurrencyExchangeRatesComponent implements OnInit {
    activeTab = 0
    public settings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
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
            currencyCode: {
                title: 'Currency Code',
                type: 'string',
                filter: false
            },
            description: {
                title: 'Description',
                type: 'string',
                filter: false
            },
            exchangeRate: {
                title: 'Exchange Rate',
                type: 'string'
            },
            applicableDate: {
                title: 'Upload Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
            status: {
                title: 'Status',
                type: 'custom',
                renderComponent: ConsignmentStatusComponent
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    dataSet: LocalDataSource = new LocalDataSource();
    activeDataSet: LocalDataSource = new LocalDataSource();
    form: FormGroup

    constructor(private diService: DestinationInspectionService, private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.loadConversionRates(null, null, "OTHER")
        this.form = this.fb.group({
            rangeType: [null, Validators.required],
            startDate: [],
            endDate: []
        })
    }

    filterByCurrency(event: any) {
        const startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
        if (this.form.value.endDate) {
            const endDate = new DatePipe('en-US').transform(this.form.value.endDate, 'dd-MM-yyyy');
            this.loadConversionRates(startDate, endDate, this.form.value.rangeType)
        } else {
            this.loadConversionRates(startDate, null, this.form.value.rangeType)
        }

    }

    uploadRates(event: any) {
        let files = event.target.files
        if (files && files.length > 0) {
            this.diService.uploadConversionRates(files[0], "csv")
                .subscribe(
                    res => {
                        if (res.responseCode == "00") {
                            this.diService.showSuccess(res.message, () => {
                                this.loadConversionRates(null, null, "OTHER")
                            })
                        } else {
                            this.diService.showError(res.message, null)
                        }
                    },
                    err => {
                        console.log(err)
                    }
                )
        }
    }

    loadConversionRates(startDate: string, endDate: string, rangeType: string) {
        let params = {
            rangeType: rangeType,
        }
        switch (rangeType) {
            case "RANGE":
                params["endDate"] = endDate ? endDate : ""
                params["date"] = startDate ? startDate : ""
                break
            case "SINGLE":
                params["date"] = startDate ? startDate : ""
                break
            default:
                params["date"] = startDate ? startDate : ""
        }
        this.diService.loadConversionRates(params)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        if (res.data.today) {
                            this.dataSet.load(res.data.today)
                        }
                        if (res.data.active) {
                            this.activeDataSet.load(res.data.active)
                        }
                    } else {
                        this.diService.showError(res.message, null)
                    }
                }
            )
    }

}
