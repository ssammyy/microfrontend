import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {ConsignmentStatusComponent} from "../../../core/shared/customs/consignment-status/consignment-status.component";
import {DatePipe} from "@angular/common";


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

    constructor(private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadConversionRates(null)
    }

    filterByCurrency(event: any) {
        const date = new DatePipe('en-US').transform(event.target.value, 'dd-MM-yyyy');
        this.loadConversionRates(date)
    }

    uploadRates(event: any) {
        let files = event.target.files
        if (files && files.length > 0) {
            this.diService.uploadConversionRates(files[0], "csv")
                .subscribe(
                    res => {
                        if (res.responseCode == "00") {
                            this.diService.showSuccess(res.message, () => {
                                this.loadConversionRates(null)
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

    loadConversionRates(date: string) {
        this.diService.loadConversionRates(date ? date : "")
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
