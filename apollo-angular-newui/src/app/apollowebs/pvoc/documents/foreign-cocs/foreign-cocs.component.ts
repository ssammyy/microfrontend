import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {UploadForeignFormComponent} from "../../../di/consignment-document-list/upload-foreign-form/upload-foreign-form.component";

@Component({
    selector: 'app-foreign-cocs',
    templateUrl: './foreign-cocs.component.html',
    styleUrls: ['./foreign-cocs.component.css']
})
export class ForeignCocsComponent implements OnInit {

    dataSet: LocalDataSource = new LocalDataSource();
    message: any
    documentType = "COC"
    documentTypeUrl = "cocs"
    documentTitle: any
    reviewStatus: number = 3
    activeStatus: any = 'under-review'
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

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private dialog: MatDialog, private pvocService: PVOCService, private diSevice: DestinationInspectionService) {
    }

    ngOnInit(): void {

        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    let docType = res.get("docType");
                    if (docType) {
                        switch (docType) {
                            case "coi":
                                this.documentTypeUrl = "cois"
                                this.documentType = docType
                                this.documentTitle = "COI (Certificate of Inspections)"
                                break
                            case "ncr":
                                this.documentTypeUrl = "ncrs"
                                this.documentType = docType
                                this.documentTitle = "NCR (Certificate of NonConformity"
                                break
                            case "coc":
                                this.documentTypeUrl = "cocs"
                                this.documentType = docType
                                this.documentTitle = "COC (Certificate of Compliance)"
                                break
                            default:
                                this.message = "Invalid document type: " + docType
                        }
                    }
                }
            )
        this.loadData()
        this.loadPartners()
    }

    toggleStatus(status) {
        if (status === this.activeStatus) {
            return
        }
        this.activeStatus = status
        switch (status) {
            case "under-review":
                this.reviewStatus = 3
                break
            case "rejected-requests":
                this.reviewStatus = 2
                break
            case "approved-requests":
                this.reviewStatus = 1
                break
            default:
                this.reviewStatus = 0
        }
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

    uploadForeignDocument(event: any, type: string) {
        let ref = this.dialog.open(UploadForeignFormComponent, {
            data: {
                documentType: type,
                partners: this.pvocPartners
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    loadData() {
        this.pvocService.loadCocDocument(this.keywords, this.reviewStatus, this.documentTypeUrl, this.currentPageInternal, this.pageSize)
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
