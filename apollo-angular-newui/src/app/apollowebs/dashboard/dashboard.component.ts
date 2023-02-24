import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {TableData} from '../../md/md-table/md-table.component';

import * as Chartist from 'chartist';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {
    loadBranchId,
    loadCompanyId,
    selectCompanyInfoDtoStateData,
    selectUserInfo,
    UserEntityDto,
} from 'src/app/core/store';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../core/store/data/ms/ms.service';
import {
    ApiResponseModel,
    ComplaintsListDto,
    MsDashBoardALLDto,
    WorkPlanListDto
} from '../../core/store/data/ms/ms.model';
import {LocalDataSource} from 'ng2-smart-table';
import {QaInternalService} from 'src/app/core/store/data/qa/qa-internal.service';

declare const $: any;

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit, AfterViewInit {
    // constructor(private navbarTitleService: NavbarTitleService, private notificationService: NotificationService) { }
    public tableData: TableData;
    turnOver = 0;
    branchCount = 0;
    countAwarded = 0;
    countExpired = 0;
    defaultPageSize = 10;
    defaultPage = 0;
    currentPage = 0;
    currentPageInternal = 0;
    totalCount = 12;
    dataSet: LocalDataSource = new LocalDataSource();
    loadedDataCP: ComplaintsListDto[];
    loadedDataCPWP: WorkPlanListDto[];

    user: UserEntityDto;
    msDashBoardDetails: MsDashBoardALLDto;
    dmarkapiResponse: ApiResponseModel;
    fmarkapiResponse: ApiResponseModel;
    smarkapiResponse: ApiResponseModel;

    roles: string[];

    @ViewChild('content') content: any;
    currDiv!: string;
    currDivLabel!: string;

    public settingsCP = {
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
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View Details</i>'},
            ],
            position: 'right', // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true,
        },
        noDataMessage: 'No data found',
        columns: {
            referenceNumber: {
                title: 'REFERENCE NUMBER',
                type: 'string',
                filter: false,
            },
            transactionDate: {
                title: 'DATE RECEIVED',
                type: 'date',
                filter: false,
            },
            progressStep: {
                title: 'STATUS',
                type: 'string',
                filter: false,
            },
        },
        pager: {
            display: true,
            perPage: 10,
        },
    };
    public settingsCPWP = {
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
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View More</i>'},
            ],
            position: 'right', // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true,
        },
        noDataMessage: 'No data found',
        columns: {
            referenceNumber: {
                title: 'REFERENCE NUMBER',
                type: 'string',
                filter: false,
            },
            timeActivityDate: {
                title: 'ACTIVITY DATE',
                type: 'date',
                filter: false,
            },
            budget: {
                title: 'BUDGET',
                type: 'string',
                filter: false,
            },
            progressStep: {
                title: 'STATUS',
                type: 'string',
                filter: true,
            },
        },
        pager: {
            display: true,
            perPage: 10,
        },
    };

    startAnimationForLineChart(chart: any) {
        let seq: any, delays: any, durations: any;
        seq = 0;
        delays = 80;
        durations = 500;
        chart.on('draw', function (data: any) {

            if (data.type === 'line' || data.type === 'area') {
                data.element.animate({
                    d: {
                        begin: 600,
                        dur: 700,
                        from: data.path.clone().scale(1, 0).translate(0, data.chartRect.height()).stringify(),
                        to: data.path.clone().stringify(),
                        easing: Chartist.Svg.Easing.easeOutQuint,
                    },
                });
            } else if (data.type === 'point') {
                seq++;
                data.element.animate({
                    opacity: {
                        begin: seq * delays,
                        dur: durations,
                        from: 0,
                        to: 1,
                        easing: 'ease',
                    },
                });
            }
        });

        seq = 0;
    }

    startAnimationForBarChart(chart: any) {
        let seq2: any, delays2: any, durations2: any;
        seq2 = 0;
        delays2 = 80;
        durations2 = 500;
        chart.on('draw', function (data: any) {
            if (data.type === 'bar') {
                seq2++;
                data.element.animate({
                    opacity: {
                        begin: seq2 * delays2,
                        dur: durations2,
                        from: 0,
                        to: 1,
                        easing: 'ease',
                    },
                });
            }
        });

        seq2 = 0;
    }


    constructor(private router: Router,
                private store$: Store<any>,
                private SpinnerService: NgxSpinnerService,
                private msService: MsService,
                private QaInternalService: QaInternalService,
    ) {
    }

    public ngOnInit() {
        // Load all PermitList Details
        // this.qaService.loadFirmPermitList(this.)
        this.store$.select(selectCompanyInfoDtoStateData).subscribe(
            (d) => {
                if (d) {
                    //(`${d.companyId} and ${d.branchId}`);
                    this.store$.dispatch(loadCompanyId({payload: d.companyId, company: null}));
                    this.store$.dispatch(loadBranchId({payload: d.branchId, branch: null}));
                    this.branchCount = d.branchCount;
                    this.turnOver = d.turnover;
                    this.countAwarded = d.countAwarded;
                    this.countExpired = d.countExpired;
                }
            },
        );
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            //(u.roles);
            this.roles = u.roles;
            return this.roles = u.roles;
        });

        if (this.roles?.includes('MS_HOD_READ')
            || this.roles?.includes('MS_IO_READ')
            || this.roles?.includes('MS_RM_READ')
            || this.roles?.includes('MS_HOF_READ')
            || this.roles?.includes('MS_DIRECTOR_READ')
        ) {
            this.loadMSData();
        }

        if (this.roles?.includes('QA_OFFICER_READ')
            || this.roles?.includes('QA_MANAGER_READ')
            || this.roles?.includes('QA_PCM_READ')
            || this.roles?.includes('QA_PSC_MEMBERS_READ')
            || this.roles?.includes('QA_DIRECTOR_READ')
        ) {
            this.loadSmarkData();
            this.loadFmarkData();
            this.loadDmarkData();

        }

    }

    private loadMSData(): any {
        this.SpinnerService.show();
        this.msService.msDashBoardAllDetails().subscribe(
            (data) => {
                this.msDashBoardDetails = data;
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                //(error);
                // this.msService.showError('AN ERROR OCCURRED');
            },
        );
    }

    private loadSmarkData(): any {
        this.SpinnerService.show();
        this.QaInternalService.loadMyTasksByPermitType(2).subscribe(
            (data) => {
                this.smarkapiResponse = data;
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                //(error);
                // this.msService.showError('AN ERROR OCCURRED');
            },
        );
    }

    private loadFmarkData(): any {
        this.SpinnerService.show();
        this.QaInternalService.loadMyTasksByPermitType(3).subscribe(
            (data) => {
                this.fmarkapiResponse = data;
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                //(error);
                // this.msService.showError('AN ERROR OCCURRED');
            },
        );
    }

    private loadDmarkData(): any {
        this.SpinnerService.show();
        this.QaInternalService.loadMyTasksByPermitType(1).subscribe(
            (data) => {
                this.dmarkapiResponse = data;
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                //(error);
                // this.msService.showError('AN ERROR OCCURRED');
            },
        );
    }


    ngAfterViewInit() {
        const breakCards = true;
        if (breakCards === true) {
            // We break the cards headers if there is too much stress on them :-)
            $('[data-header-animation="true"]').each(function () {
                const $fix_button = $(this);
                const $card = $(this).parent('.card');
                $card.find('.fix-broken-card').click(function () {
                    const $header = $(this).parent().parent().siblings('.card-header, .card-image');
                    $header.removeClass('hinge').addClass('fadeInDown');

                    $card.attr('data-count', 0);

                    setTimeout(function () {
                        $header.removeClass('fadeInDown animate');
                    }, 480);
                });

                $card.mouseenter(function () {
                    const $this = $(this);
                    const hover_count = parseInt($this.attr('data-count'), 10) + 1 || 0;
                    $this.attr('data-count', hover_count);
                    if (hover_count >= 20) {
                        $(this).children('.card-header, .card-image').addClass('hinge animated');
                    }
                });
            });
        }
    }

    gotoDMarkApplication() {
        this.router.navigate(['/dmark/newDmarkPermit']);

    }

    gotoSMarkApplication() {
        this.router.navigate(['/smark/newSmarkPermit']);

    }

    gotoFMarkApplication() {
        this.router.navigate(['/fmark/application']);

    }

    gotoSL1Application() {
        this.router.navigate(['/standardsLevy/levyRegistration']);

    }

    gotoHome() {
        this.router.navigate(['/dashboard']);
    }

    // qa admin
    gotoDiamondMarkApplication() {
        this.router.navigate(['/dmark-admin']);

    }

    gotoStandardizationMarkApplication() {
        this.router.navigate(['/smark-admin']);

    }

    gotoFortificationMarkApplication() {
        this.router.navigate(['/fmark-admin']);

    }

    gotoAwardedApplication() {
        this.router.navigate(['']);

    }

    gotoExpiredApplication() {
        this.router.navigate(['']);

    }

    gotodefferedApplication() {
        this.router.navigate(['']);

    }


    openModalAddDetails(divVal: string, headerVal: string): void {
        this.currDivLabel = headerVal;
        this.currDiv = divVal;
        this.loadData(this.defaultPage, this.defaultPageSize, this.currDiv);
    }

    public onCustomActionCP(event: any): void {
        switch (event.action) {
            case 'viewRecord':
                this.viewRecordCP(event.data);
                break;
        }
    }

    public onCustomActionCPWP(event: any): void {
        if (this.currDiv === 'allocatedWPTask'
            || this.currDiv === 'allocatedOverDueWPTasks'
            || this.currDiv === 'juniorTaskOverDueWPTask'
            || this.currDiv === 'reportPendingReviewWPTask'
        ) {
            switch (event.action) {
                case 'viewRecord':
                    this.viewRecordWP(event.data);
                    break;
            }
        } else if (
            this.currDiv === 'allocatedWPCPTask'
            || this.currDiv === 'allocatedOverDueWPCPTasks'
            || this.currDiv === 'juniorTaskOverDueWPCPTask'
            || this.currDiv === 'reportPendingReviewWPCPTask') {
            switch (event.action) {
                case 'viewRecord':
                    this.viewRecordWPCP(event.data);
                    break;
            }
        }
    }

    viewRecordCP(data: ComplaintsListDto) {
        this.router.navigate([`/complaint/details/`, data.referenceNumber]);
    }

    viewRecordWPCP(data: WorkPlanListDto) {
        this.router.navigate([`/complaintPlan/details/`, data.referenceNumber, data.batchRefNumber]);
    }

    viewRecordWP(data: WorkPlanListDto) {
        this.router.navigate([`/workPlan/details/`, data.referenceNumber, data.batchRefNumber]);
    }


    pageChange(pageIndex?: any) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1;
            this.currentPage = pageIndex;
            this.loadData(this.currentPageInternal, this.defaultPageSize, this.currDiv);
        }
    }

    private loadData(page: number, records: number, routeTake: string): any {
        this.SpinnerService.show();
        switch (routeTake) {
            case 'allocatedCPTask':
                this.loadAllocatedCPTask(page, records);
                break;
            case 'pendingAllocationCPTask':
                this.loadAllocatedCPTask(page, records);
                break;
            case 'allocatedOverDueCPTasks':
                this.loadAllocatedCPTask(page, records);
                break;
            case 'allocatedWPTask':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'reportPendingReviewWPTask':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'reportPendingReviewWPCPTask':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'juniorTaskOverDueWPTask':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'juniorTaskOverDueWPCPTask':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'allocatedWPCPTask':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'allocatedOverDueWPTasks':
                this.loadAllocatedCPWPTask(page, records);
                break;
            case 'allocatedOverDueWPCPTasks':
                this.loadAllocatedCPWPTask(page, records);
                break;
        }

    }

    private loadAllocatedCPTask(page: number, records: number): any {
        this.msService.loadMSDashBoardTaskListView(String(page), String(records), this.currDiv).subscribe(
            (data) => {
                this.loadedDataCP = data.data;
                this.totalCount = this.loadedDataCP.length;
                this.dataSet.load(this.loadedDataCP);
                window.$('#msAllDashBoardViewData').modal('show');
                this.SpinnerService.hide();
            },
        );
    }

    private loadAllocatedCPWPTask(page: number, records: number): any {
        this.msService.loadMSDashBoardTaskListView(String(page), String(records), this.currDiv).subscribe(
            (data) => {
                this.loadedDataCPWP = data.data;
                this.totalCount = this.loadedDataCPWP.length;
                this.dataSet.load(this.loadedDataCPWP);
                window.$('#msAllDashBoardViewData').modal('show');
                this.SpinnerService.hide();
            },
        );
    }


    gotoRequestModuleStandards() {
        this.router.navigate(['']);


    }
}
