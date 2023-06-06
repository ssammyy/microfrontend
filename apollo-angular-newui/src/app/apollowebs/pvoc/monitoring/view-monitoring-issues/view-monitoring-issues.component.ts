import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-view-monitoring-issues',
    templateUrl: './view-monitoring-issues.component.html',
    styleUrls: ['./view-monitoring-issues.component.css']
})
export class ViewMonitoringIssuesComponent implements OnInit {

    monitorId: any
    monitoringDetails: any
    activeTab = 0
    pageSize = 20
    page = 0
    totalCount = 0
    // Seal issues
    public sealSettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                // {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        columns: {
            ucrNumber: {
                title: 'UCR Number',
                type: 'string',
                filter: false
            },
            certNumber: {
                title: 'Cert No.',
                type: 'string',
                filter: false
            },
            certType: {
                title: 'Cert Type',
                type: 'string',
                filter: false
            },
            recordYearMonth: {
                title: 'Record Year and Month',
                type: 'string'
            },
            route: {
                title: 'Route',
                type: 'string'
            },
            riskStatus: {
                title: 'Risk Status',
                type: 'string',
                valuePrepareFunction: (status) => {
                    if (status === 1) {
                        return "YES"
                    }
                    return "NO"
                }
            },
            applicablePenalty: {
                title: 'Applicable Penalty',
                type: 'number'
            },
            createdOn: {
                title: 'Record Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }

                    return ""
                },
            },
            modifiedOn: {
                title: 'Last Update',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }

                    return ""
                }
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    // Timeline Issues
    public timelineSettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                // {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        columns: {
            ucrNumber: {
                title: 'UCR Number',
                type: 'string',
                filter: false
            },
            certNumber: {
                title: 'Cert No.',
                type: 'string',
                filter: false
            },
            certType: {
                title: 'Cert Type',
                type: 'string',
                filter: false
            },
            requestDateOfInspection: {
                title: 'Request of Inspection',
                type: 'string',
                filter: false
            },
            rfcDate: {
                title: 'RFC date',
                type: 'string'
            },
            rfcToIssuanceDays: {
                title: 'RFC to Issue days',
                type: 'number'
            },
            rfcToInspectionDays: {
                title: 'RFC to Inspection days',
                type: 'number'
            },
            paymentToIssuanceDays: {
                title: 'Payment to Issue days',
                type: 'number'
            },
            route: {
                title: 'Route',
                type: 'string'
            },
            riskStatus: {
                title: 'Risk Status',
                type: 'string',
                valuePrepareFunction: (status) => {
                    if (status === 1) {
                        return "YES"
                    }
                    return "NO"
                }
            },
            applicablePenalty: {
                title: 'Applicable Penalty',
                type: 'number'
            },
            createdOn: {
                title: 'Record Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }

                    return ""
                },
            },
            modifiedOn: {
                title: 'Last Update',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }

                    return ""
                }
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private pvocService: PVOCService, private activeRouter: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.activeRouter.paramMap.subscribe(
            res => {
                this.monitorId = res.get("id")
                this.loadData()
            }
        )
    }

    viewTimelineItem(item) {

    }


    goBack() {
        this.router.navigate(["/monitoring/issues"])
    }

    auctionEvent(name: string, data: any) {

    }

    pageChange(type: string, event: any) {

    }

    loadData() {
        this.pvocService.monitoringIssueDetails(this.monitorId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.monitoringDetails = res.data
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError("Failed to load data: " + error.toString())
                }
            )
    }

}
