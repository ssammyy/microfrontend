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
    enable?: boolean;
    collapse?: string;
    children?: ChildrenItems[];
}

export interface ChildrenItems {
    path: string;
    title: string;
    ab: string;
    enable?: boolean;
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
        path: '/',
        title: 'Operations',
        type: 'sub',
        icontype: 'class',
        privilege: ['SL_IS_MANAGER'],
        collapse: 'operations',
        children: [
            {path: 'slCompanySuspension', title: 'Suspend Company', ab: 'SC'},
            {path: 'slCompanyClosure', title: 'Close Company ', ab: 'CC'},
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
                privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
            },
            {
                path: 'exemption/applications',
                title: 'Exemption applications',
                ab: 'EA',
                privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
            },
            {
                path: 'complaints',
                title: 'Complaints',
                ab: 'CC',
                privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
            },
            {
                path: 'partners',
                title: 'Partners',
                ab: 'PP',
                privilege: ['PVOC_ADMIN', 'PVOC_ADMIN_REALD'],
            },
        ],
        collapse: 'pvoc',
        privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN', 'PVOC_ADMIN_REALD'],
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
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN_READ'],
            },
            {
                path: 'auction/view',
                title: 'Auction Goods',
                ab: 'AG',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN_READ'],
            },
            {
                path: 'ism/requests',
                title: 'ISM Requests',
                ab: 'SM',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN_READ'],
            },
            {
                path: 'kentrade/exchange/messages',
                title: 'KENTRADE Monitoring',
                ab: 'KM',
                privilege: ['DI_ADMIN_READ', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
            },
            {
                path: 'kentrade/idf/documents',
                title: 'IDF Documents',
                ab: 'ID',
                privilege: ['DI_ADMIN_READ', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
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
        privilege: ['DI_ADMIN', 'DI_ADMIN_READ', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ'],
        children: [
            {
                path: 'demand-notes',
                title: 'Demand Notes',
                ab: 'DN',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'corporates-customers',
                title: 'Corporate Customers',
                ab: 'CC',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'exchange-rates',
                title: 'Exchange Rates',
                ab: 'ER',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'limits',
                title: 'Billing Types',
                ab: 'BT',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
        ],
        icontype: 'money',
    },
    {
        path: '/system',
        title: 'Configurations',
        type: 'sub',
        collapse: 'api-clients',
        privilege: ['DI_ADMIN', 'DI_ADMIN_READ', 'PVOC_ADMIN_READ', 'PVOC_ADMIN'],
        children: [
            {
                path: 'api-clients',
                title: 'Api Clients',
                ab: 'AC',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'cfs',
                title: 'CFS codes',
                ab: 'CC',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'inspection/fees',
                title: 'Inspection Fees',
                ab: 'IF',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'laboratories',
                title: 'Laboratories',
                ab: 'LB',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'custom/offices',
                title: 'Custom Offices',
                ab: 'CO',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            }
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
    {
        path: '/workPlan',
        title: 'MS WORK-PLAN',
        type: 'link',
        collapse: 'workPlan',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        icontype: 'receipt',
    },

    //Standards Development
    {
        path: '',
        title: 'Request Module',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'requestModule',
        children: [
            {path: 'ms-standards', title: 'Review Standard Requests', ab: 'RSR'},
            {path: 'std-tsc-sec-task', title: 'Prepare New Work Item', ab: 'PNWI'},
            {path: 'std-tc-task', title: 'Vote On New Work Item', ab: 'V'},
            {path: 'upload-justification', title: 'Upload Justification', ab: 'UJ'},
            {path: 'decision-justification', title: 'Decision On Justification', ab: 'DOJ'},
            {path: 'upload-workplan', title: 'Upload Workplan', ab: 'UW'},



        ],
    },

    {
        path: '',
        title: 'Publishing',
        type: 'sub',
        icontype: 'publish',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'publish',
        children: [
            {path: 'draftStandard', title: 'Upload Draft Standard', ab: 'UDS'},
            {path: 'hopTasks', title: 'Review Draft Standards', ab: 'RDS'},
            {path: 'editorTasks', title: 'Edit Draft Standards', ab: 'EDS'},
            {path: 'proofReaderTasks', title: 'Proofread Draft Standards', ab: 'PDS'},
            {path: 'draughtsmanTasks', title: 'Draught Draft Standards', ab: 'DDS'},
            {path: 'hopApproval', title: 'Approve Draft Standards', ab: 'ADS'},



        ],
    },
    {
        path: '',
        title: 'Formation Of A TC',
        type: 'sub',
        icontype: 'group',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'formation',
        children: [
            {path: 'requestForFormationOfTC', title: 'Request For Formation', ab: 'RFF'},
            {path: 'reviewJustificationOfTC', title: 'TC Justification', ab: 'RDS'},
            {path: 'reviewFeedbackSPC', title: 'Feedback Review', ab: 'EDS'},




        ],
    },
    {
        path: '',
        title: 'Membership To A TC',
        type: 'sub',
        icontype: 'groups',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'membership',
        children: [
            {path: 'callsForApplication', title: 'Create Application', ab: 'CA'},
            {path: 'reviewApplication', title: 'Review Applications', ab: 'RA'},
            {path: 'reviewRecommendation', title: 'Review Recommendations', ab: 'RAR'},
            {path: 'reviewRecommendationOfSpc', title: 'SPC Review', ab: 'SR'},
            {path: 'reviewAccepted', title: 'Appointment Letter', ab: 'AL'},
            {path: 'reviewRejected', title: 'Rejected Appointments', ab: 'RA'},
            {path: 'approvedMembers', title: 'Approved Members', ab: 'AM'},
            {path: 'createCredentials', title: 'Credentials Creation', ab: 'CC'},
            {path: 'sendInductionEmail', title: 'Send Induction Email', ab: 'SIE'},
            {path: 'sendNotice', title: 'Send Notice', ab: 'SN'},




        ],
    },
    {
        path: '',
        title: 'Adoption of EAS',
        type: 'sub',
        icontype: 'group',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'adoption',
        children: [
            {path: 'uploadSacSummary', title: 'Upload SAC Summary', ab: 'USS'},
            {path: 'viewSacSummary', title: 'Review SAC Summary', ab: 'RSS'},
            {path: 'viewSacSummaryApproved', title: 'Feedback Review', ab: 'EDS'},

        ],
    },

    {
        path: '',
        title: 'Workshop Agreement',
        type: 'sub',
        icontype: 'handshake',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'nwa',
        children: [
            {path: 'nwaJustification', title: 'Prepare Justification', ab: 'PJ'},
            {path: 'nwaTasks', title: 'Workshop Agreement Tasks', ab: 'NWA'},

        ],
    },
    {
        path: '',
        title: 'National Enquiry Point',
        type: 'sub',
        icontype: 'quiz',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'nep',
        children: [
            {path: 'make_enquiry', title: 'Make Enquiry', ab: 'ME'},
            {path: 'nep_information_received', title: 'Enquiry Manage', ab: 'EM'},
            {path: 'nep_division_response', title: 'Division Response', ab: 'DR'},

        ],
    },

    {
        path: '',
        title: 'Company Standard',
        type: 'sub',
        icontype: 'business',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'cs',
        children: [
            {path: 'comStdRequest', title: 'Company Standard Request', ab: 'C'},
            {path: 'comStdList', title: 'View CSR/Assign PL', ab: 'VC'},
            {path: 'comStdJustification', title: 'View Justification ', ab: 'V'},
            {path: 'comAppJustification', title: 'Approve Justification', ab: 'V'},
            {path: 'comStdJustificationList', title: 'List Justification', ab: 'J'},
            {path: 'comStdAppJustification', title: 'Approve Justification', ab: 'J'},
            {path: 'comPlTasks', title: 'Form Joint Committee', ab: 'PL'},
            {path: 'comPlTasks', title: 'Prepare Company Draft', ab: 'PL'},
            {path: 'comStdDraft', title: 'View Company Draft', ab: 'PPD'},
            {path: 'comStdConfirmation', title: 'Approve Company Draft', ab: 'NWA'},
            {path: 'comStdUpload', title: 'Edit/upload Company Standard', ab: 'NWA'},
            {path: 'comStdListed', title: 'View uploaded standard', ab: 'NWA'},

        ],
    },
    {
        path: '',
        title: 'International Standards',
        type: 'sub',
        icontype: 'quiz',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'is',
        children: [
            {path: 'make_enquiry', title: 'Make Enquiry', ab: 'ME'},


        ],
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
        //window.open('/login','_self').close();
        //window.open('/login')
    }

    onClickGoToProfilePage() {
        this.router.navigate(['/profile']);
    }


}
