import {Component, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-incomplete-idfdocuments',
    templateUrl: './incomplete-idfdocuments.component.html',
    styleUrls: ['./incomplete-idfdocuments.component.css']
})
export class IncompleteIDFDocumentsComponent implements OnInit {

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
            baseDocRefNo: {
                title: 'IDF NUMBER',
                type: 'string',
                filter: false
            },
            ucrNo: {
                title: 'UCR Number',
                type: 'string',
                filter: false
            },
            officeCode: {
                title: 'Office Code',
                type: 'string'
            },
            officeSubDivisionCode: {
                title: 'Office Code Division',
                type: 'string'
            },
            createdOn: {
                title: 'Received On',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
            varField1: {
                title: 'Status',
                type: 'string',
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
        this.loadConversionRates(null, 0)
        this.loadConversionRates(null, 1)
        this.form = this.fb.group({
            startDate: [null, Validators.required],
            status: [0, Validators.required]
        })
    }

    filterByCurrency(event: any) {
        const startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
        this.loadConversionRates(startDate, parseInt(this.form.status))

    }

    loadConversionRates(startDate: string, status: number) {
        let params = {
            status: status,
        }
        params["date"] = startDate ? startDate : ""
        this.diService.loadIncompleteIdfDocuments(params)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        if (status == 1) {
                            this.dataSet.load(res.data)
                        } else {
                            this.activeDataSet.load(res.data)
                        }
                    } else {
                        this.diService.showError(res.message, null)
                    }
                }
            )
    }

}
