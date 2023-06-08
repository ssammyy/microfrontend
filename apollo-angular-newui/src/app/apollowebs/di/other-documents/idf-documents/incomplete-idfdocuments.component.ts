import {Component, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {GenerateReportComponent} from "./generate-report/generate-report.component";

@Component({
    selector: 'app-incomplete-idfdocuments',
    templateUrl: './incomplete-idfdocuments.component.html',
    styleUrls: ['./incomplete-idfdocuments.component.css']
})
export class IncompleteIDFDocumentsComponent implements OnInit {
    page: number = 0
    loadedData = false
    pageSize: number = 20;
    currentPageInternal: number = 0;
    totalCount: number;
    stations = null
    activeTab = 1
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
            ucrNo: {
                title: "UCR Number",
                type: "string",
                filter: false
            },
            baseDocRefNo: {
                title: 'IDF NUMBER',
                type: 'string',
                filter: false
            },
            declarantPin: {
                title: 'Decl. PIN',
                type: 'string',
                filter: false
            },
            consigneeBusinessName: {
                title: 'Business Name',
                type: 'string'
            },
            consignorBusinessName: {
                title: 'Exporter Name',
                type: 'string'
            },
            officeCode: {
                title: 'Office Code',
                type: 'string'
            },
            officeSubDivisionCode: {
                title: 'Division Code',
                type: 'string'
            },
            delivTermsSub1: {
                title: 'Office Code Division',
                type: 'string'
            },
            placeOfLoading: {
                title: 'Place of Loading',
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
    activeStatus = 'all'
    form: FormGroup

    constructor(private diService: DestinationInspectionService, private fb: FormBuilder, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.loadIdDocuments(null, "all")
        this.form = this.fb.group({
            startDate: [null, Validators.required],
            keywords: [null, Validators.required]
        })
        this.loadStations()
    }

    searchPhraseChanged() {
        let startDate = null
        if (this.form.value.startDate) {
            startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
        }
        this.loadIdDocuments(startDate, "all", this.form.value.keywords)
    }

    loadStations() {
        this.diService.listMyCfs()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.stations = res.data
                    }
                }
            )
    }

    toggleStatus(status) {
        if (this.activeStatus == status) {
            return;
        }
        this.activeStatus = status
        if (this.form.value.startDate) {
            const startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
            this.loadIdDocuments(startDate, this.activeStatus)
        } else {
            this.loadIdDocuments(null, this.activeStatus)
        }

    }

    pageChange(pageIndex) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.page = pageIndex
            if (this.form.value.startDate) {
                const startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
                this.loadIdDocuments(startDate, this.activeStatus)
            } else {
                this.loadIdDocuments(null, this.activeStatus)
            }
        }
    }

    generateReport() {
        this.dialog.open(GenerateReportComponent, {
            data: {
                stations: this.stations
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        console.log(res)
                        // Download report
                        // Download
                        this.diService.downloadDocument("/api/v1/download/reports", {}, res)
                    }
                },
                error => {
                }
            )
    }

    loadIdDocuments(startDate: string, status: string, keywords?: string) {
        this.diService.loadIdfDocuments(status, this.page, this.pageSize, startDate, keywords)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.dataSet.load(res.data)
                        this.totalCount = res.totalCount
                    } else {
                        this.diService.showError(res.message, null)
                    }
                }
            )
    }

}
