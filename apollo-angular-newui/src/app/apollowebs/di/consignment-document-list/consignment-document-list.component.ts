import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {UploadForeignFormComponent} from "./upload-foreign-form/upload-foreign-form.component";
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {selectUserInfo} from "../../../core/store/data/auth";
import {Store} from "@ngrx/store";
import {interval, Subject} from "rxjs";
import {debounce} from "rxjs/operators";
import {PVOCService} from "../../../core/store/data/pvoc/pvoc.service";
import {GenerateCDReportComponent} from "./generate-cdreport/generate-cdreport.component";

@Component({
    selector: 'app-consignment-document-list',
    templateUrl: './consignment-document-list.component.html',
    styleUrls: ['./consignment-document-list.component.css']
})
export class ConsignmentDocumentListComponent implements OnInit {
    allowedStatuses = ["my-tasks", "completed", "ongoing", "waiting", "search-result", "not-assigned"]
    activeStatus: string = 'my-tasks';
    previousStatus: string = 'my-tasks'
    searchStatus: any
    personalTasks = "false"
    defaultPageSize: number = 20
    currentPage: number = 0
    currentPageInternal: number = 0
    totalCount: number = 0
    inspectionOfficers = []
    supervisors = []
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
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
            ],
            position: 'right' // left|right
        },
        rowClassFunction: (row) => {
            // console.log(row)
            if (row.data.isNcrDocument) {
                return 'risky';
            } else {
                return ''
            }

        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            id: {
                title: 'ID',
                type: 'string',
                filter: false
            },
            freightStation: {
                title: 'Station',
                type: 'string',
                filter: false
            },
            ucrNumber: {
                title: 'UCR No',
                type: 'string'
            },
            applicationRefNo: {
                title: 'Ref No.',
                type: 'string'
            },
            docType: {
                title: 'Document Type',
                type: 'string',
                hidden: true
            },
            applicationDate: {
                title: 'Application Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
                filter: false
            },
            approveRejectCdDate: {
                title: 'Completion Date',
                type: 'data',
                valuePrepareFunction: (date) => {
                    if (date) {
                        // return new DatePipe('en-GB').transform(date, 'dd/MM/yyyy hh:mm');
                        return date
                    }
                    return ""
                },
                filter: false
            },
            approvalStatus: {
                title: 'CD Status',
                type: 'string',
                filter: false
            },
            applicationStatus: {
                title: 'Application Status',
                type: 'string',
                filter: false
            },
            assignedTo: {
                title: 'Assigned Officer',
                type: 'string'
            },
            cdTypeName: {
                title: 'CD Type',
                type: 'string',
                hidden: true
            },
            cdTypeDescription: {
                title: 'CD Description',
                type: 'string'
            },
            cdTypeCategory: {
                title: 'Document Category',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: LocalDataSource = new LocalDataSource();
    documentTypes: any[];
    message: any
    keywords: any;
    stations = []
    pvocPartners: any[]
    prevRequest: any
    private documentTypeUuid: string
    private documentTypeId: any
    isAdmin: boolean = false
    isReadOnly = false
    supervisorCharge: boolean = false
    inspectionOfficer: boolean = false
    search: Subject<string>

    constructor(private store$: Store<any>, private dialog: MatDialog, private  activeRoute: ActivatedRoute, private router: Router, private diService: DestinationInspectionService, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.documentTypeUuid = null;
        this.activeRoute.paramMap.subscribe(res => {
            console.log(res)
            if (res.has("tab")) {
                this.previousStatus = this.activeStatus
                if (this.allowedStatuses.includes(res.get("tab"))) {
                    this.activeStatus = res.get("tab")
                }
            }
            console.log(this.activeStatus)
            this.loadTypes(() => {
                this.search = new Subject<string>()
                this.search.pipe(
                    debounce((keyword) => interval(500))
                ).subscribe(
                    res => {
                        console.log(res)
                        this.searchDocuments(res)
                    }
                )
                this.loadData(this.documentTypeUuid, 0, this.defaultPageSize);
            })
            this.loadPartners()
            this.loadStations()
            this.store$.select(selectUserInfo)
                .subscribe((u) => {
                    this.supervisorCharge = this.diService.hasRole(['DI_OFFICER_CHARGE_READ'], u.roles)
                    this.inspectionOfficer = this.diService.hasRole(['DI_INSPECTION_OFFICER_READ'], u.roles)
                    this.isAdmin = this.diService.hasRole(['DI_ADMIN'], u.roles)
                    this.isReadOnly = this.diService.hasRole(['DI_DIRECTOR_READ'], u.roles)
                    if (this.isReadOnly) {
                        // Read only cannot view these tabs, override
                        if (['my-tasks', 'waiting'].includes(this.activeStatus)) {
                            this.toggleStatus('ongoing')
                        }
                    }
                    if (!this.inspectionOfficer) {
                        this.loadOfficers("io")
                        this.loadOfficers("supervisors")
                    } else {
                        this.inspectionOfficers = []
                    }
                });
        })
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

    loadOfficers(designation: any) {
        this.diService.listOfficersInMyStation(designation)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        switch (designation) {
                            case 'io':
                                this.inspectionOfficers = res.data
                                break
                            default:
                                this.supervisors = res.data

                        }
                    }
                }
            )

    }

    generateReport() {
        this.dialog.open(GenerateCDReportComponent, {
            data: {
                stations: this.stations,
                users: this.inspectionOfficers,
                supervisors: this.supervisors,
                is_supervisor: this.supervisorCharge,
                is_officer: this.inspectionOfficer || this.isReadOnly,
                is_admin: this.isAdmin,
                documentTypes: this.documentTypes
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

    onSupervisorChange(event: any) {
        if (this.supervisorCharge) {
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
        }
    }

    pageChange(pageIndex?: any) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.currentPage = pageIndex
            this.loadData(this.documentTypeUuid, this.currentPageInternal, this.defaultPageSize)
        }
    }

    private cancelPrevious() {
        if (this.prevRequest) {
            this.prevRequest.unsubscribe();
        }
    }

    private loadData(documentTypeUuid: string, page: number, size: number): any {
        let params = {
            'personal': this.personalTasks
        }
        if (this.activeStatus == 'waiting') {
            params['active'] = 0
        } else {
            params['active'] = 1
        }
        this.cancelPrevious()
        let data = this.diService.listAssignedCd(documentTypeUuid, page, size, params);
        console.log(this.activeStatus)
        // Clear list before loading
        this.dataSet.load([])
        // Switch
        if (this.activeStatus === "completed") {
            data = this.diService.listCompletedCd(documentTypeUuid, page, size)
        } else if (this.activeStatus === "ongoing") {
            data = this.diService.listSectionOngoingCd(documentTypeUuid, page, size)
        } else if (this.activeStatus === "not-assigned") {
            data = this.diService.listManualAssignedCd(documentTypeUuid, page, size)
        }
        this.prevRequest = data.subscribe(
            result => {
                if (result.responseCode === "00") {
                    let listD: any[] = result.data;
                    this.totalCount = result.totalCount
                    this.dataSet.load(listD)
                } else {
                    console.log(result)
                }
            }
        );
    }

    private loadTypes(fn: Function) {
        this.diService.documentTypes()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.documentTypes = res.data;
                    } else {
                        this.documentTypes = []
                    }
                    if (fn) {
                        fn()
                    }
                }
            )
    }

    public onCustomAction(event: any): void {
        switch (event.action) {
            case 'viewRecord':
                this.viewRecord(event.data);
                break;
        }
    }

    viewRecord(data: any) {
        // console.log(data)
        this.router.navigate([`/di`, data.uuid]);
    }

    public onFilterChange(event: any) {
        if (event.target.value != this.documentTypeUuid) {
            this.documentTypeUuid = event.target.value.uuid
            this.documentTypeId = event.target.value.id
            if (this.searchStatus) {
                this.searchDocuments(this.keywords)
            } else {
                this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
            }
        }
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
                        this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
                    }
                }
            )
    }

    searchPhraseChanged() {
        this.search.next(this.keywords)
    }

    searchDocuments(keywords: string) {
        this.cancelPrevious()
        this.dataSet.load([])
        this.previousStatus = this.activeStatus
        this.searchStatus = 'search-result'
        this.activeStatus = this.searchStatus
        console.log(event)
        let data = {
            'documentType': this.documentTypeId,
            'keywords': keywords,
            'category': this.activeStatus
        }
        console.log(data)
        this.prevRequest = this.diService.searchConsignmentDocuments(data)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.message = null
                        this.dataSet.load(res.data)
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    console.log(error)
                    this.message = "Search failed"
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
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
        }
    }
}
