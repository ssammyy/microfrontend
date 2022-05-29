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
    privilege?: string[];
}

// Menu Items
export const ROUTES: RouteInfo[] = [
    {
        path: '/dashboard',
        title: 'Dashboard',
        type: 'link',
        icontype: 'dashboard',
        privilege: ['USER', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
    },

    {
        path: '/admin',
        title: 'Admin',
        type: 'sub',
        icontype: 'dashboard',
        collapse: 'admin',
        privilege: ['SYSADMIN_VIEW'],
        children: [
            {path: 'user_management', title: 'User Management', ab: 'UM'},
            {path: 'business_management', title: 'Business Management', ab: 'BM'},

        ],
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
            {path: 'users', title: 'View Users ', ab: 'VU'},
        ],
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
            {path: 'all_dmark_awarded', title: 'Awarded Applications', ab: 'AA'},
        ],
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
            {path: 'all_smark_awarded', title: 'Awarded Applications', ab: 'AA'},
        ],
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
            {path: 'all_fmark_awarded', title: 'Awarded Applications', ab: 'AA'},
        ],
    }, {
        path: '/invoice',
        title: 'Invoices',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'invoice',
        children: [
            {path: 'consolidate_invoice', title: 'Consolidate Invoices', ab: 'CI'},
            {path: 'all_invoice', title: 'All Invoices', ab: 'AI'},
        ],
    },
    {
        path: '/all_qa_tasks_list',
        title: 'My Tasks',
        type: 'link',
        icontype: 'task',
        privilege: ['PERMIT_APPLICATION'],
    },
    {
        path: '/payments',
        title: 'Payments',
        type: 'link',
        icontype: 'receipt_long',
        privilege: ['PERMIT_APPLICATION'],
    },
    {
        path: '/company/applications',
        title: 'Company application',
        type: 'link',
        icontype: 'apply',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
    },
    {
        path: '/pvoc',
        title: 'PVOC',
        type: 'sub',
        children: [
            {
                path: 'waiver/applications',
                title: 'Waiver application',
                ab: 'WA',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'exemption/applications',
                title: 'Exemption applications',
                ab: 'EA',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'complaints',
                title: 'Complaints',
                ab: 'CC',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'partners',
                title: 'Partners',
                ab: 'PP',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
        ],
        collapse: 'pvoc',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        icontype: 'diamond',
    },
    {
        path: '/ministry/inspection',
        title: 'Motor Vehicle Inspection',
        type: 'link',
        collapse: 'ministry-inspection',
        privilege: ['MINISTRY_OF_TRANSPORT_READ', 'MINISTRY_OF_TRANSPORT_MODIFY'],
        icontype: 'receipt',
    },
    {
        path: '/di',
        title: 'Import Inspection',
        type: 'sub',
        children: [
            {
                path: '',
                title: 'Import Inspection',
                ab: 'II',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'auction/view',
                title: 'Auction Goods',
                ab: 'AG',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'ism/requests',
                title: 'ISM Requests',
                ab: 'SM',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'kentrade/exchange/messages',
                title: 'KENTRADE Monitoring',
                ab: 'KM',
                privilege: ['SYSADMIN_VIEW', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'kentrade/idf/documents',
                title: 'IDF Documents',
                ab: 'ID',
                privilege: ['SYSADMIN_VIEW', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
        ],
        collapse: 'import-inspection',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        icontype: 'receipt',
    },
    {
        path: '/transaction',
        title: 'Finance',
        type: 'sub',
        collapse: 'transactions',
        privilege: ['SYSADMIN_VIEW', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        children: [
            {
                path: 'demand-notes',
                title: 'Demand Notes',
                ab: 'DN',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'corporates-customers',
                title: 'Corporate Customers',
                ab: 'CC',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'exchange-rates',
                title: 'Exchange Rates',
                ab: 'ER',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'limits',
                title: 'Billing Types',
                ab: 'BT',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
        ],
        icontype: 'money',
    },
    {
        path: '/system',
        title: 'Configurations',
        type: 'sub',
        collapse: 'api-clients',
        privilege: ['SYSADMIN_VIEW', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        children: [
            {path: 'api-clients', title: 'Api Clients', ab: 'AC'},
        ],
        icontype: 'settings',
    },
    {
        path: '/standardsLevy',
        title: 'Standards Levy',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'standardsLevy',
        children: [
            {path: 'levyRegistration', title: 'View SL Form', ab: 'SL'},
            {path: 'stdLevyPaidHistory', title: 'Payment History', ab: 'PH'},
            {path: 'stdLevyPenalties', title: 'Penalties', ab: 'PE'},
        ],
    },
    {
        path: '/epra',
        title: 'EPRA',
        type: 'link',
        collapse: 'epra',
        privilege: ['EPRA', 'MS_MP_MODIFY', 'MS_IOP_MODIFY'],
        icontype: 'receipt',
    },
    {
        path: '/complaint',
        title: 'MS COMPLAINTS',
        type: 'link',
        collapse: 'complaint',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        icontype: 'diamond',
    },
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
    canViewRole(privileges?: string[]): Boolean {
        if (!privileges) {
            return true;
        }
        for (const role of this.roles) {
            for (const p of privileges) {
                if (role === p) {
                    return true;
                }
            }
        }
        return false;
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
        window.open('/login','_self').close();
        window.open('/login')
    }

    onClickGoToProfilePage() {
        this.router.navigate(['/profile']);
    }


}
