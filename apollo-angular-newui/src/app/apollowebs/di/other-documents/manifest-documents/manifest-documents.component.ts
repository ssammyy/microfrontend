import {Component, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {GenerateManifestReportComponent} from "./generate-manifest-report/generate-manifest-report.component";

@Component({
    selector: 'app-manifest-documents',
    templateUrl: './manifest-documents.component.html',
    styleUrls: ['./manifest-documents.component.css']
})
export class ManifestDocumentsComponent implements OnInit {

    page: number = 0
    loadedData = false
    pageSize: number = 20;
    currentPageInternal: number = 0;
    totalCount: number;
    activeTab = 1
    stations = []
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
        noDataMessage: 'No manifest documents',
        columns: {
            manifestNumber: {
                title: 'MANIFEST NUMBER',
                type: 'string',
                filter: false
            },
            carrierAgentBusinessName: {
                title: 'Carrier Agent',
                type: 'string',
                filter: false
            },
            consigneeBusinessName: {
                title: 'Consignee',
                type: 'string'
            },
            consignorBusinessName: {
                title: 'Consignor',
                type: 'string'
            },
            voyageNumber: {
                title: 'Voyage No.',
                type: 'string'
            },
            messageDate: {
                title: 'Message Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
            consFlag: {
                title: 'Cons Flag',
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
        this.loadManifestDocuments(null, "all")
        this.form = this.fb.group({
            keywords: [null, Validators.minLength(5)],
            startDate: [null],
        })
        this.loadStations()
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
            this.loadManifestDocuments(startDate, this.activeStatus)
        } else {
            this.loadManifestDocuments(null, this.activeStatus)
        }

    }

    pageChange(pageIndex) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.page = pageIndex
            if (this.form.value.startDate) {
                const startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
                this.loadManifestDocuments(startDate, this.activeStatus)
            } else {
                this.loadManifestDocuments(null, this.activeStatus)
            }
        }
    }

    findManifestDocument(event: any) {
        if (this.form.value.startDate) {
            const startDate = new DatePipe('en-US').transform(this.form.value.startDate, 'dd-MM-yyyy');
            this.loadManifestDocuments(startDate, this.form.status, this.form.value.keywords)
        } else {
            this.loadManifestDocuments(null, this.form.status, this.form.value.keywords)
        }

    }

    generateReport() {
        this.dialog.open(GenerateManifestReportComponent, {
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
                        this.diService.downloadDocument("/api/v1/download/reports", {}, res)
                    }
                },
                error => {
                }
            )
    }

    loadManifestDocuments(startDate: string, status: string, keywords?: string) {
        this.diService.loadManifestDocuments(status, startDate, keywords)
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
