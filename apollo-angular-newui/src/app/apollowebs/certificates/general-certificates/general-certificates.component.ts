import {Component, Input, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {GenerateGeneralReportComponent} from "./generate-general-report/generate-general-report.component";

@Component({
    selector: 'app-general-certificates',
    templateUrl: './general-certificates.component.html',
    styleUrls: ['./general-certificates.component.css']
})
export class GeneralCertificatesComponent implements OnInit {
    @Input() documentType = "coc"
    dataSet: LocalDataSource = new LocalDataSource();
    message: any
    stations = []
    documentTitle: any
    documentTypeLabel = "COC"
    activeStatus: any = 'all' // all,foreign, local
    keywords: string
    page: number = 0
    loadedData = false
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
        noDataMessage: 'No COC/COI/NCRs found',
        columns: {
            cocNumber: {
                title: 'COC No.',
                type: 'string',
                filter: false
            },
            coiNumber: {
                title: 'COI No.',
                type: 'string',
                filter: false
            },
            ucrNumber: {
                title: 'UCR No.',
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
            idfNumber: {
                title: 'IDF No.',
                type: 'string',
                filter: false
            },
            placeOfInspection: {
                title: 'Inspection Center',
                type: 'string',
                filter: false
            },
            cocIssueDate: {
                title: 'Issued On',
                type: 'string',
            },
            countryOfSupply: {
                title: 'Country of Origin',
                type: 'string',
            },
            portOfDestination: {
                title: 'Destination',
                type: 'string',
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

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private dialog: MatDialog, private diServices: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadedData = true;
        if (this.documentType) {
            switch (this.documentType) {
                case "coi":
                    this.documentTypeLabel = "COI"
                    this.documentTitle = "COI (Certificate of Inspections)"
                    break
                case "ncr":
                    this.documentTypeLabel = "NCR"
                    this.documentTitle = "NCR (Certificate of NonConformity"
                    break
                case "coc":
                    this.documentTypeLabel = "COC"
                    this.documentTitle = "COC (Certificate of Compliance)"
                    break
                default:
                    this.loadedData = false;
                    this.message = "Invalid document type: " + this.documentType
            }
        }
        if (this.loadedData) {
            this.loadData()
        }
        this.loadStations();
    }

    loadStations() {
        this.diServices.listMyCfs()
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

    pageChange(pageIndex) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.page = pageIndex
            this.loadData()
        }
    }

    generateReport() {
        this.dialog.open(GenerateGeneralReportComponent, {
            data: {
                documentType: this.documentType,
                stations: this.stations
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        console.log(res)
                        // Download report
                        this.diServices.downloadDocument("/api/v1/download/reports", {}, res)
                    }
                },
                error => {
                }
            )
    }

    loadData() {
        this.diServices.loadCertificateDocument(this.keywords, this.activeStatus, this.documentType, this.currentPageInternal, this.pageSize)
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
                this.viewCoc(actions.data)
                break
        }
    }

    viewCoc(data: any) {
        this.router.navigate(["/pvoc/foreign/documents", this.documentType, data.id])
    }

}
