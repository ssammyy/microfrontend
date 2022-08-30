import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";

@Component({
    selector: 'app-view-exemption-certificates',
    templateUrl: './view-exemption-certificates.component.html',
    styleUrls: ['./view-exemption-certificates.component.css']
})
export class ViewExemptionCertificatesComponent implements OnInit {
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
        noDataMessage: 'No exemption applications/certificates found',
        columns: {
            sn: {
                title: 'REFERENCE NUMBER',
                type: 'string'
            },
            createdOn: {
                title: 'DATE GENERATED',
                type: 'string'
            },
            companyPinNo: {
                title: 'COMPANY PIN',
                type: 'string'
            },
            conpanyName: {
                title: 'COMPANY NAME',
                type: 'string'
            },
            reviewStatus: {
                title: 'CERT STATUS',
                type: 'string'
            },
            telephoneNo: {
                title: 'PHONE NUMBER',
                type: 'string'
            },
            certNumber: {
                title: 'CERT NUMBER',
                type: 'string'
            },
            certificateValidity: {
                title: 'CERT VALIDITY',
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
        this.dataSource = new LocalDataSource();
        this.activeRoute.queryParamMap.subscribe(res => {
            if (res.has("tab")) {
                this.previousStatus = this.activeStatus
                if (res.get("tab") in this.allowedStatuses) {
                    this.activeStatus = res.get("tab")
                }
            }
            this.loadData(0, this.defaultPageSize)
        })
    }

    goBack() {
        this.router.navigate(["/company/applications"])
    }

    loadData(page: number, size: number) {
        this.pvocService.manufacturerExemptionHistory(this.activeStatus, page, size)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dataSource.load(res.data)
                            .then(() => {

                            })
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
        console.log(status)
        this.message = null
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
