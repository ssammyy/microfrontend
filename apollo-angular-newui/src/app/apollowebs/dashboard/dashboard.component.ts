import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {TableData} from '../../md/md-table/md-table.component';

import * as Chartist from 'chartist';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {
    loadBranchId,
    loadCompanyId,
    selectCompanyInfoDtoStateData,
    selectUserInfo,
    UserEntityDto
} from 'src/app/core/store';

declare const $: any;

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {
    // constructor(private navbarTitleService: NavbarTitleService, private notificationService: NotificationService) { }
    public tableData: TableData;
    turnOver = 0;
    branchCount = 0;
    countAwarded = 0;
    countExpired = 0;

    user: UserEntityDto;

    roles: string[];

    @ViewChild('content') content: any;

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
                        easing: Chartist.Svg.Easing.easeOutQuint
                    }
                });
            } else if (data.type === 'point') {
                seq++;
                data.element.animate({
                    opacity: {
                        begin: seq * delays,
                        dur: durations,
                        from: 0,
                        to: 1,
                        easing: 'ease'
                    }
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
                        easing: 'ease'
                    }
                });
            }
        });

        seq2 = 0;
    }


    constructor(private router: Router,
                private store$: Store<any>
    ) {
    }

    public ngOnInit() {
        // Load all PermitList Details
        // this.qaService.loadFirmPermitList(this.)
        this.store$.select(selectCompanyInfoDtoStateData).subscribe(
            (d) => {
                if (d) {
                    console.log(`${d.companyId} and ${d.branchId}`);
                    this.store$.dispatch(loadCompanyId({payload: d.companyId, company: null}));
                    this.store$.dispatch(loadBranchId({payload: d.branchId, branch: null}));
                    this.branchCount = d.branchCount;
                    this.turnOver = d.turnover;
                    this.countAwarded = d.countAwarded;
                    this.countExpired = d.countExpired;
                }
            }
        );
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            return this.roles = u.roles;
        });


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


}
