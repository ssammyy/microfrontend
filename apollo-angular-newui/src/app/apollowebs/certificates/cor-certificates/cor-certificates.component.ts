import {Component, Input, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {PVOCService} from "../../../core/store/data/pvoc/pvoc.service";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {GenerateCorReportComponent} from "./generate-cor-report/generate-cor-report.component";

@Component({
    selector: 'app-cor-certificates',
    templateUrl: './cor-certificates.component.html',
    styleUrls: ['./cor-certificates.component.css']
})
export class CorCertificatesComponent implements OnInit {
    @Input()
    documentType: string = "cor"
    documentTitle: string
    documentCategory: string
    dataSet: LocalDataSource = new LocalDataSource();
    message: any
    stations = []
    activeStatus: any = 'all'
    keywords: string
    page: number = 0
    pvocPartners: any
    pageSize: number = 20;
    currentPageInternal: number = 0;
    totalCount: number;
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
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        noDataMessage: 'No COR/NCRs found',
        columns: {
            corNumber: {
                title: 'COR No.',
                type: 'string',
                filter: false
            },
            ucrNumber: {
                title: 'UCR No.',
                type: 'string',
                filter: false
            },
            engineNumber: {
                title: 'Engine No.',
                type: 'string',
            },
            inspectionCenter: {
                title: 'Inspection Center',
                type: 'string',
                filter: false
            },
            corIssueDate: {
                title: 'Issued On',
                type: 'string',
            },
            countryOfSupply: {
                title: 'Country of Origin',
                type: 'string',
            },
            chassisNumber: {
                title: 'Chassis No.',
                type: 'string',
            },
            make: {
                title: 'Make',
                type: 'string',
                filter: false
            },
            documentsType: {
                title: "Type",
                type: 'string',
                valuePrepareFunction: (docType) => {
                    switch (docType) {
                        case "F": {
                            return "Foreign Document"
                        }
                        case "L": {
                            return "Local Document"
                        }
                    }
                    return `Unknown: ${docType}`
                },
            },
            createdOn: {
                title: 'Date Received',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }
                    return "N/A"
                },
            },
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private router: Router, private dialog: MatDialog, private pvocService: PVOCService, private diSevice: DestinationInspectionService) {
    }

    ngOnInit(): void {

        this.loadStations()
        this.loadData()
        this.loadPartners()
    }

    loadStations() {
        this.diSevice.listMyCfs()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.stations = res.data
                    }
                }
            )
    }

    toggleStatus(status) {
        if (status === this.activeStatus) {
            return
        }
        this.activeStatus = status
        this.loadData()
    }

    searchPhraseChanged() {
        this.loadData()
    }

    pageChange(pageIndex) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.page = pageIndex
            this.loadData()
        }
    }

    loadPartners() {
        this.pvocService.loadPartnerNames()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.pvocPartners = res.data
                    }
                }
            )
    }

    generateReport() {
        this.dialog.open(GenerateCorReportComponent, {
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
                        this.diSevice.downloadDocument("/api/v1/download/reports", {}, res)
                    }
                },
                error => {
                }
            )
    }

    loadData() {
        this.diSevice.loadCertificateDocument(this.keywords, this.activeStatus, this.documentCategory, this.currentPageInternal, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dataSet.empty()
                        this.dataSet.load(res.data)
                        this.totalCount = res.totalCount
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.message = error.message
                }
            )
    }

    auctionEvent(actions) {
        switch (actions.action) {
            case 'viewRecord':
                this.viewCor(actions.data)
                break
        }
    }

    viewCor(data: any) {
        this.router.navigate(["/pvoc/foreign/document/cor/", data.recordId])
    }
}
