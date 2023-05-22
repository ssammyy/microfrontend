import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {MatDialog} from "@angular/material/dialog";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {UploadForeignFormComponent} from "../../../di/consignment-document-list/upload-foreign-form/upload-foreign-form.component";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-foreign-cors',
    templateUrl: './foreign-cors.component.html',
    styleUrls: ['./foreign-cors.component.css']
})
export class ForeignCorsComponent implements OnInit {

    dataSet: LocalDataSource = new LocalDataSource();
    message: any
    documentTitle: string
    documentCategory: string
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
        noDataMessage: 'No CORs found',
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

    constructor(private router: Router, private dialog: MatDialog, private pvocService: PVOCService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    let docType = res.get("docType");
                    if (docType) {
                        switch (docType.toUpperCase()) {
                            case "COR":
                                this.documentTitle = "COR(Certificate of Road worthiness)"
                                this.documentCategory = "COR"
                                break
                            case "NCR-COR":
                                this.documentTitle = "NCR for COR(Certificate of Road worthiness)"
                                this.documentCategory = "NCR-COR"
                                break
                            default:
                                this.message = "Invalid document type: " + docType.toUpperCase()
                        }
                    } else {
                        this.documentTitle = "COR(Certificate of Road worthiness)"
                        this.documentCategory = "COR"
                    }
                    if (this.documentCategory) {
                        this.loadData()
                        this.loadPartners()
                    }
                })

    }

    searchPhraseChanged() {
        this.loadData()
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

    uploadForeignCoROrCor(event: any, type: string) {
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
        this.pvocService.loadCorDocument(this.keywords, this.documentCategory, this.reviewStatus, this.currentPageInternal, this.pageSize)
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
