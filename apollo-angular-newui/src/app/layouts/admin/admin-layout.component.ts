import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {NavigationEnd, NavigationStart, Router} from '@angular/router';
import {NavItem, NavItemType} from '../../md/md.module';
import {Subscription} from 'rxjs/Subscription';
import {Location, PopStateEvent} from '@angular/common';
import 'rxjs/add/operator/filter';
import {NavbarComponent} from '../../shared/navbar/navbar.component';
import PerfectScrollbar from 'perfect-scrollbar';
import {LoadingService} from "../../core/services/loader/loadingservice.service";
import {delay} from "rxjs/operators";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

declare const $: any;

@Component({
    selector: 'app-layout',
    templateUrl: './admin-layout.component.html'
})

export class AdminLayoutComponent implements OnInit, AfterViewInit {
    public navItems: NavItem[];
    private _router: Subscription;
    private lastPoppedUrl: string;
    private yScrollStack: number[] = [];
    url: string;
    location: Location;
    loading: boolean = false;


    @ViewChild('sidebar', {static: false}) sidebar: any;
    @ViewChild(NavbarComponent, {static: false}) navbar: NavbarComponent;

    constructor
    (private router: Router, location: Location, private _loading: LoadingService
    ) {
        this.location = location;
    }

    ngOnInit() {
        this.listenToLoading();
        const elemMainPanel = <HTMLElement>document.querySelector('.main-panel');
        const elemSidebar = <HTMLElement>document.querySelector('.sidebar .sidebar-wrapper');
        this.location.subscribe((ev: PopStateEvent) => {
            this.lastPoppedUrl = ev.url;
        });
        this.router.events.subscribe((event: any) => {
            if (event instanceof NavigationStart) {
                // tslint:disable-next-line:triple-equals
                if (event.url != this.lastPoppedUrl) {
                    this.yScrollStack.push(window.scrollY);
                }
            } else if (event instanceof NavigationEnd) {
                // tslint:disable-next-line:triple-equals
                if (event.url == this.lastPoppedUrl) {
                    this.lastPoppedUrl = undefined;
                    window.scrollTo(0, this.yScrollStack.pop());
               } else {
                   window.scrollTo(0, 0);
               }
           }
        });
        this._router = this.router.events.filter(event => event instanceof NavigationEnd).subscribe((event: NavigationEnd) => {
             elemMainPanel.scrollTop = 0;
             elemSidebar.scrollTop = 0;
        });
        const html = document.getElementsByTagName('html')[0];
        if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
            let ps = new PerfectScrollbar(elemMainPanel);
            ps = new PerfectScrollbar(elemSidebar);
            html.classList.add('perfect-scrollbar-on');
        } else {
            html.classList.add('perfect-scrollbar-off');
        }
        this._router = this.router.events.filter(event => event instanceof NavigationEnd).subscribe((event: NavigationEnd) => {
          this.navbar.sidebarClose();
        });

        this.navItems = [
          { type: NavItemType.NavbarLeft, title: 'Dashboard', iconClass: 'fa fa-dashboard' },

          {
            type: NavItemType.NavbarRight,
            title: '',
            iconClass: 'fa fa-bell-o',
            numNotifications: 5,
            dropdownItems: [
              { title: 'Notification 1' },
              { title: 'Notification 2' },
              { title: 'Notification 3' },
              { title: 'Notification 4' },
              { title: 'Another Notification' }
            ]
          },
          {
            type: NavItemType.NavbarRight,
            title: '',
            iconClass: 'fa fa-list',

            dropdownItems: [
              { iconClass: 'pe-7s-mail', title: 'Messages' },
              { iconClass: 'pe-7s-help1', title: 'Help Center' },
              { iconClass: 'pe-7s-tools', title: 'Settings' },
               'separator',
              { iconClass: 'pe-7s-lock', title: 'Lock Screen' },
              { iconClass: 'pe-7s-close-circle', title: 'Log Out' }
            ]
          },
          { type: NavItemType.NavbarLeft, title: 'Search', iconClass: 'fa fa-search' },

          { type: NavItemType.NavbarLeft, title: 'Account' },
          {
            type: NavItemType.NavbarLeft,
            title: 'Dropdown',
            dropdownItems: [
              { title: 'Action' },
              { title: 'Another action' },
              { title: 'Something' },
              { title: 'Another action' },
              { title: 'Something' },
              'separator',
              { title: 'Separated link' },
            ]
          },
          { type: NavItemType.NavbarLeft, title: 'Log out' }
        ];
    }
    ngAfterViewInit() {
        this.runOnRouteChange();
    }
    public isMap() {
        if (this.location.prepareExternalUrl(this.location.path()) === '/maps/fullscreen') {
            return true;
        } else {
            return false;
        }
    }
    runOnRouteChange(): void {
      if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
        const elemSidebar = <HTMLElement>document.querySelector('.sidebar .sidebar-wrapper');
        const elemMainPanel = <HTMLElement>document.querySelector('.main-panel');
        let ps = new PerfectScrollbar(elemMainPanel);
        ps = new PerfectScrollbar(elemSidebar);
        ps.update();
      }
    }
    isMac(): boolean {
        let bool = false;
        if (navigator.platform.toUpperCase().indexOf('MAC') >= 0 || navigator.platform.toUpperCase().indexOf('IPAD') >= 0) {
            bool = true;
        }
        return bool;
    }
    listenToLoading(): void {
        this._loading.loadingSub
            .pipe(delay(0)) // This prevents a ExpressionChangedAfterItHasBeenCheckedError for subsequent requests
            .subscribe((loading) => {
                this.loading = loading;
            });
    }
}
