import {Component, OnInit} from '@angular/core';
import PerfectScrollbar from 'perfect-scrollbar';
import {Store} from '@ngrx/store';
import {loadLogout, selectUserInfo} from '../core/store';
import {Router} from '@angular/router';

declare const $: any;

// Metadata
export interface RouteInfo {
    path: string;
    title: string;
    type: string;
    icontype: string;
    privilege: string[];
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
        icontype: 'dashboard',
        privilege: ['USER','DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ']
    },

    {
        path: '/user_management',
        title: 'Admin',
        type: 'link',
        icontype: 'dashboard',
        privilege: ['SYSADMIN_VIEW']
    },

    {
        path: '/company',
        title: 'My Companies',
        type: 'sub',
        icontype: 'business',
        privilege: ['MODIFY_COMPANY'],
        collapse: 'company',
        children: [
            {path: 'companies', title: 'View Companies', ab: 'VC'},
            // {path: 'branches', title: 'View Branches ', ab: 'VB'},
            {path: 'users', title: 'View Users ', ab: 'VU'}
        ]
    },

    {
        path: '/dmark',
        title: 'Diamond Mark',
        type: 'sub',
        icontype: 'verified',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'forms',
        children: [
            {path: 'newDmarkPermit', title: 'Make Application', ab: 'MA'},
            {path: 'all_dmark', title: 'All My Applications', ab: 'AMA'},
            {path: 'all_dmark_awarded', title: 'Awarded Applications', ab: 'AA'}
        ]
    }, {
        path: '/smark',
        title: 'Standardization Mark',
        type: 'sub',
        icontype: 'class',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'tables',
        children: [
            {path: 'newSmarkPermit', title: 'Make Application', ab: 'MA'},
            {path: 'all_smark', title: 'All My Applications', ab: 'AMA'},
            {path: 'all_smark_awarded', title: 'Awarded Applications', ab: 'AA'}
        ]
    }, {
        path: '/fmark',
        title: 'Fortification Mark',
        type: 'sub',
        icontype: 'recommended',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'fmark',
        children: [
            {path: 'application', title: 'Make Application', ab: 'MA'},
            {path: 'fMarkAllApp', title: 'All My Applications', ab: 'AMA'},
            {path: 'all_fmark_awarded', title: 'Awarded Applications', ab: 'AA'}
        ]
    }, {
        path: '/invoice',
        title: 'Invoices',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'invoice',
        children: [
            {path: 'consolidate_invoice', title: 'Consolidate Invoices', ab: 'CI'},
            {path: 'all_invoice', title: 'All Invoices', ab: 'AI'}
        ]
    },
    {
        path: '/all_qa_tasks_list',
        title: 'My Tasks',
        type: 'link',
        icontype: 'task',
        privilege: ['PERMIT_APPLICATION'],
    },
    {
        path: '/pvoc',
        title: 'PVOC',
        type: 'link',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        icontype: 'receipt'
    },
    {
        path: '/ministry/inspection',
        title: 'Motor Vehicle Inspection',
        type: 'link',
        privilege: ['MINISTRY_OF_TRANSPORT_READ','MINISTRY_OF_TRANSPORT_MODIFY'],
        icontype: 'receipt'
    },
    {
        path: '/di',
        title: 'Import Inspection',
        type: 'link',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        icontype: 'receipt'
    },

    {
        path: '/tasks',
        title: 'Import Inspection Tasks',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        type: 'link',
        icontype: 'receipt'
    }
];

@Component({
    selector: 'app-sidebar-cmp',
    templateUrl: 'sidebar.component.html',
})

export class SidebarComponent implements OnInit {
    public menuItems: any[];
    roles: string[];
    ps: any;
    fullname = '';

    constructor(
        private store$: Store<any>, private router: Router
    ) {
    }

    isMobileMenu() {
        return $(window).width() <= 991;

    }

    ngOnInit() {
        this.menuItems = ROUTES.filter(menuItem => menuItem);

        if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
            const elemSidebar = <HTMLElement>document.querySelector('.sidebar .sidebar-wrapper');
            this.ps = new PerfectScrollbar(elemSidebar);
        }
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            return this.fullname = u.fullName;
        });
    }

    // Check if role is in required privileges
    canViewRole(privileges: string[]): Boolean {
        for(let role of this.roles) {
            for (let p of privileges) {
                if (role == p) {
                    return true
                }
            }
        }
        return false
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

    onClickGoToProfilePage() {
        this.router.navigate(['/profile']);
    }
}
