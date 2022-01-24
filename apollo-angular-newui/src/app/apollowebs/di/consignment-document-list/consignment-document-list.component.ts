import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {UploadForeignFormComponent} from "./upload-foreign-form/upload-foreign-form.component";
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {selectUserInfo} from "../../../core/store/data/auth";
import {Store} from "@ngrx/store";
import {fromEvent, interval, Observable, Subject} from "rxjs";
import {debounce} from "rxjs/operators";

@Component({
    selector: 'app-consignment-document-list',
    templateUrl: './consignment-document-list.component.html',
    styleUrls: ['./consignment-document-list.component.css']
})
export class ConsignmentDocumentListComponent implements OnInit {
    activeStatus: string = 'my-tasks';
    previousStatus: string = 'my-tasks'
    searchStatus: any
    personalTasks = "false"
    defaultPageSize: number = 20
    currentPage: number = 0
    currentPageInternal: number = 0
    totalCount: number = 0
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
                title: 'Approval Date',
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
    private documentTypeUuid: string
    private documentTypeId: any
    supervisorCharge: boolean=false
    inspectionOfficer: boolean=false
    search: Subject<string>

    constructor(private store$: Store<any>,private dialog: MatDialog, private router: Router, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.documentTypeUuid = null;
        this.loadTypes(() => {
            this.search=new Subject<string>()
            this.search.pipe(
                debounce((keyword)=>interval(500))
            ).subscribe(
                res=>{
                    console.log(res)
                    this.searchDocuments(res)
                }
            )
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize);
        })

        this.store$.select(selectUserInfo)
            .subscribe((u) => {
                this.supervisorCharge = this.diService.hasRole(['DI_OFFICER_CHARGE_READ'], u.roles)
                this.inspectionOfficer = this.diService.hasRole(['DI_INSPECTION_OFFICER_READ'],u.roles)
            });
    }

    onSupervisorChange(event: any) {

        if(this.supervisorCharge) {
            this.personalTasks = event.target.value
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

    private loadData(documentTypeUuid: string, page: number, size: number): any {
        let params = {
            'personal': this.personalTasks
        }
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
        data.subscribe(
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
                documentType: type
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
        this.diService.searchConsignmentDocuments(data)
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
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
        }
    }
}