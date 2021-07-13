import {Component, OnInit} from '@angular/core';
import PerfectScrollbar from 'perfect-scrollbar';
import {Store} from '@ngrx/store';
import {loadLogout, selectUserInfo} from '../core/store';

declare const $: any;

// Metadata
export interface RouteInfo {
    path: string;
    title: string;
    type: string;
    icontype: string;
    collapse?: string;
    children?: ChildrenItems[];
}

export interface ChildrenItems {
    path: string;
    title: string;
    ab: string;
    type?: string;
}

// Menu Items
export const ROUTES: RouteInfo[] = [
    {
        path: '/dashboard',
        title: 'Dashboard',
        type: 'link',
        icontype: 'dashboard'
    },
    {
        path: '/dashboard',
        title: 'My Companies',
        type: 'sub',
        icontype: 'book',
        collapse: 'company',
        children: [
            {path: 'companies', title: 'View Companies', ab: 'VC'},
            {path: 'companies/branches', title: 'View Branches ', ab: 'VB'},
            {path: 'companies/branches/users', title: 'View Users ', ab: 'VU'}
        ]
    },

    {
        path: '/fmark',
        title: 'Fortification Mark',
        type: 'sub',
        icontype: 'book',
        collapse: 'fmark',
        children: [
            {path: 'buttons', title: 'Make Application', ab: 'MA'},
            {path: 'sweet-alert', title: 'My Tasks ', ab: 'MT'},
            {path: 'fMarkAllApp', title: 'All My Applications', ab: 'AMA'},
            {path: 'panels', title: 'Complete Applications', ab: 'CA'}
        ]
    }, {
        path: '/dmark',
        title: 'Diamond Mark',
        type: 'sub',
        icontype: 'wysiwyg',
        collapse: 'forms',
        children: [
            {path: 'newDmarkPermit', title: 'Make Application', ab: 'MA'},
            {path: 'sweet-alert', title: 'My Tasks ', ab: 'MT'},
            {path: 'all_dmark', title: 'All My Applications', ab: 'AMA'},
            {path: 'panels', title: 'Complete Applications', ab: 'CA'}
        ]
    }, {
        path: '/tables',
        title: 'Standardization Mark',
        type: 'sub',
        icontype: 'class',
        collapse: 'tables',
        children: [
            {path: 'buttons', title: 'Make Application', ab: 'MA'},
            {path: 'sweet-alert', title: 'My Tasks ', ab: 'MT'},
            {path: 'grid', title: 'All My Applications', ab: 'AMA'},
            {path: 'panels', title: 'Complete Applications', ab: 'CA'}
        ]
    }, {
        path: '/invoice',
        title: 'Invoices',
        type: 'link',
        icontype: 'content_paste'
    }
];

@Component({
    selector: 'app-sidebar-cmp',
    templateUrl: 'sidebar.component.html',
})

export class SidebarComponent implements OnInit {
    public menuItems: any[];
    ps: any;
    fullname = '';

    constructor(
        private store$: Store<any>
    ) {
    }

    isMobileMenu() {
        if ($(window).width() > 991) {
            return false;
        }
        return true;
    }

    ngOnInit() {
        this.menuItems = ROUTES.filter(menuItem => menuItem);
        if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
            const elemSidebar = <HTMLElement>document.querySelector('.sidebar .sidebar-wrapper');
            this.ps = new PerfectScrollbar(elemSidebar);
        }
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.fullname = u.fullName;
        });
    }

    updatePS(): void {
        if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
            this.ps.update();
        }
    }

    isMac(): boolean {
        let bool = false;
        if (navigator.platform.toUpperCase().indexOf('MAC') >= 0 || navigator.platform.toUpperCase().indexOf('IPAD') >= 0) {
            bool = true;
        }
        return bool;
    }

    onClickLogout() {
        this.store$.dispatch(loadLogout({loginUrl: 'login'}));
    }
}
