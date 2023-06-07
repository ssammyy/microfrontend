import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-view-waiver-certificates',
    templateUrl: './view-waiver-certificates.component.html',
    styleUrls: ['./view-waiver-certificates.component.css']
})
export class ViewWaiverCertificatesComponent implements OnInit {
    dataSource: LocalDataSource
    settings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                {name: 'download', title: '<i class="btn btn-sm btn-primary download">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        rowClassFunction: (row) => {
            let certStatus = row.status;
            if (certStatus !== '1') {
                return 'hide-download-action';
            }
            return ''
        },
        noDataMessage: 'No waiver applications/certificates found',
        columns: {
            serialNo: {
                title: 'REFERENCE NUMBER',
                type: 'string'
            },
            category: {
                title: 'CATEGORY',
                type: 'string'
            },
            kraPin: {
                title: 'KRA PIN',
                type: 'string'
            },
            applicantName: {
                title: 'APPLICANT NAME',
                type: 'string'
            },
            submittedOn: {
                title: 'APPLICATION DATE',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
            reviewStatus: {
                title: 'REVIEW STATUS',
                type: 'string'
            }
        }
    }
    message: any
    searchStatus: any
    activeStatus: any = 'new'
    previousStatus: any
    defaultPageSize = 20
    allowedStatuses = ['new', 'approved', 'rejected', 'others']

    constructor(private  router: Router, private pvocService: PVOCService, private  activeRoute: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.dataSource = new LocalDataSource()
        this.activeRoute.queryParamMap.subscribe(res => {
            if (res.has("tab")) {
                this.previousStatus = this.activeStatus
                if (res.get("tab") in this.allowedStatuses) {
                    this.activeStatus = res.get("tab")
                }
            }
            this.loadData(0,this.defaultPageSize)
        })
    }

    goBack() {
        this.router.navigate(["/company/applications"])
    }

    loadData(page: number, size: number) {
        this.pvocService.manufacturerWaiver(this.activeStatus,page,size)
            .subscribe(
                res => {
                    if(res.responseCode==="00"){
                        this.dataSource.load(res.data)
                    } else {
                        this.pvocService.showError(res.message, null)
                    }
                },
                error => {
                    console.log(error)
                }
            )
    }

    toggleStatus(status: string): void {
        this.searchStatus = null
        if (status !== this.activeStatus) {
            this.activeStatus = status;
            this.router.navigate([], {
                queryParams: {
                    tab: status
                }
            })
            this.loadData(0, this.defaultPageSize)
        }
    }
}
