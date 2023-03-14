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

    //Quality Assurance
    {
        path: '/admin',
        title: 'Admin',
        type: 'sub',
        icontype: 'dashboard',
        collapse: 'admin',
        privilege: ['SYSADMIN_VIEW'],
        children: [
            {path: 'user_management', title: 'User Management', ab: 'UM'},
            {path: 'tivet_management', title: 'Tivet Management', ab: 'TM'},
            {path: 'business_management', title: 'Business Management', ab: 'BM'},

        ],
    },

    {
        path: '/company',
        title: 'My Companies',
        type: 'sub',
        icontype: 'business',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'company',
        children: [
            {path: 'companies', title: 'View Companies', ab: 'VC'},
            // {path: 'branches', title: 'View Branches ', ab: 'VB'},
            {path: 'users', title: 'View Users ', ab: 'VU'},
        ],
    },

    {
        path: '/company',
        title: 'Companies',
        type: 'sub',
        icontype: 'business',
        privilege: ['QA_MANAGER_MODIFY', 'QA_OFFICER_MODIFY', 'QA_ASSESSORS_MODIFY'],
        collapse: 'company',
        children: [
            {path: 'list', title: 'View Companies', ab: 'VC'},
            // {path: 'branches', title: 'View Branches ', ab: 'VB'},
            // {path: 'users', title: 'View Users ', ab: 'VU'},
        ],
    },
    // {
    //     path: '/qa_task_list',
    //     title: 'My Tasks',
    //     type: 'link',
    //     icontype: 'task',
    //     privilege: ['QA_MANAGER_MODIFY', 'QA_OFFICER_MODIFY', 'QA_ASSESSORS_MODIFY'],
    // },

    {
        path: '/dmark/all_dmark',
        title: 'Diamond Mark',
        type: 'link',
        icontype: 'verified',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'forms',
        children: [
            // {path: 'newDmarkPermit', title: 'Make Application', ab: 'MA'},
            // {path: 'all_dmark', title: 'All My Applications', ab: 'AMA'},
            // {path: 'all_dmark_awarded', title: 'Awarded Applications', ab: 'AA'},
        ],
    }, {
        path: '/smark/all_smark',
        title: 'Standardization Mark',
        type: 'link',
        icontype: 'class',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'tables',
        children: [
            // {path: 'newSmarkPermit', title: 'Make Application', ab: 'MA'},
            // {path: 'all_smark', title: 'All My Applications', ab: 'AMA'},
            // {path: 'all_smark_awarded', title: 'Awarded Applications', ab: 'AA'},
        ],
    }, {
        path: '/fmark/application',
        title: 'Fortification Mark',
        type: 'link',
        icontype: 'recommended',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'fmark',
        children: [
            // {path: 'application', title: 'Make Application', ab: 'MA'},
            // {path: 'fMarkAllApp', title: 'All My Applications', ab: 'AMA'},
            // {path: 'all_fmark_awarded', title: 'Awarded Applications', ab: 'AA'},
        ],
    }, {
        path: '/invoice',
        title: 'Invoices',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'invoice',
        children: [
            {path: 'consolidate_invoice', title: 'Consolidate All Invoices', ab: 'CAI'},
            // {path: 'consolidate_invoice-difference', title: 'Consolidate All Invoices Difference', ab: 'CAID'},
            // {path: 'consolidate_invoice_fmark', title: 'Consolidate Fmark Invoices', ab: 'CFI',},
            // {path: 'consolidate_invoice_dmark', title: 'Consolidate Dmark Invoices', ab: 'CDI',},

            {path: 'all_invoice', title: 'All Invoices', ab: 'AI'},
        ],
    },
    // {
    //     path: '/all_qa_tasks_list',
    //     title: 'My Tasks',
    //     type: 'link',
    //     icontype: 'task',
    //     privilege: ['PERMIT_APPLICATION'],
    // },
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
        privilege: ['MANUFACTURER_ADMIN', 'PVOC_APPLICATION_PROCESS'],
    },
    // {
    //     path: '',
    //     title: 'QA Reports',
    //     type: 'sub',
    //     icontype: 'receipt',
    //     privilege: ['QA_OFFICER_READ', 'QA_MANAGER_READ', 'QA_PCM_READ', 'QA_PSC_MEMBERS_READ', 'QA_DIRECTOR_READ'],
    //     collapse: 'reports',
    //     children: [
    //         {path: 'reports/all_applications', title: 'All Applications', ab: 'AP'},
    //         {path: 'reports/permits_granted', title: 'Permits Granted', ab: 'PG'},
    //         {path: 'reports/permits_renewed', title: 'Permits Renewed', ab: 'PR'},
    //         {path: 'reports/samples_submitted', title: 'Samples Submitted', ab: 'SS'},
    //         {path: 'reports/permits_deferred', title: 'Permits Deferred', ab: 'PD'},


    //     ],
    // },


    // Quality Assurance Admin
    {
        path: '/smark-admin',
        title: 'Standardization Mark',
        type: 'link',
        icontype: 'class',
        privilege: ['QA_OFFICER_READ', 'QA_MANAGER_READ', 'QA_PCM_READ', 'QA_PSC_MEMBERS_READ', 'QA_DIRECTOR_READ'],


    },

    {
        path: '/fmark-admin',
        title: 'Fortification Mark',
        type: 'link',
        icontype: 'recommended',
        privilege: ['QA_OFFICER_READ', 'QA_MANAGER_READ', 'QA_PCM_READ', 'QA_PSC_MEMBERS_READ', 'QA_DIRECTOR_READ'],


    },

    {
        path: '/dmark-admin',
        title: 'Diamond Mark',
        type: 'link',
        icontype: 'verified',
        privilege: ['QA_OFFICER_READ', 'QA_MANAGER_READ', 'QA_PCM_READ', 'QA_PSC_MEMBERS_READ', 'QA_DIRECTOR_READ'],

    },

    {
        path: '',
        title: 'QA Reports',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['QA_OFFICER_READ', 'QA_MANAGER_READ', 'QA_PCM_READ', 'QA_PSC_MEMBERS_READ', 'QA_DIRECTOR_READ'],
        collapse: 'reports',
        children: [
            {path: 'reports/all_applications', title: 'All Applications', ab: 'AP'},
            {path: 'reports/permits_granted', title: 'Permits Granted', ab: 'PG'},
            {path: 'reports/permits_renewed', title: 'Permits Renewed', ab: 'PR'},
            {path: 'reports/samples_submitted', title: 'Samples Submitted', ab: 'SS'},
            {path: 'reports/permits_deferred', title: 'Permits Deferred', ab: 'PD'},
            {path: 'reports/qa-sl-report', title: 'QA-SL Reports', ab: 'QSR'},


        ],
    },


    //DI

    {
        path: '/pvoc',
        title: 'PVOC',
        type: 'sub',
        children: [
            {
                path: 'waiver/applications',
                title: 'Waiver application',
                ab: 'WA',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
            },
            {
                path: 'exemption/applications',
                title: 'Exemption applications',
                ab: 'EA',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
            },
            {
                path: 'complaints',
                title: 'Complaints',
                ab: 'CC',
                privilege: ['DI_INSPECTION_OFFICER_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
            },
            {
                path: 'foreign/cors',
                title: 'Foreign COR',
                ab: 'CO',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN'],
            },
            {
                path: 'foreign/documents/coc',
                title: 'Foreign COC',
                ab: 'CN',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN'],
            },
            {
                path: 'foreign/documents/coi',
                title: 'Foreign COI',
                ab: 'CI',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN'],
            },
            {
                path: 'foreign/documents/ncr',
                title: 'Foreign NCR',
                ab: 'CN',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN'],
            },
            {
                path: 'foreign/rfc/cor',
                title: 'RFC for COR',
                ab: 'CR',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN'],
            },
            {
                path: 'foreign/rfc/coc',
                title: 'RFC for COC/COI',
                ab: 'CC',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN'],
            },
            {
                path: 'partners',
                title: 'Partners',
                ab: 'PP',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
                // privilege: ['PVOC_ADMIN', 'PVOC_ADMIN_REALD'],
            },
        ],
        collapse: 'pvoc',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_DIRECTOR_READ'],
        // privilege: ['PVOC_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'PVOC_ADMIN', 'PVOC_ADMIN_READ'],
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
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'auction/view',
                title: 'Auction Goods',
                ab: 'AG',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'ism/requests',
                title: 'ISM Requests',
                ab: 'SM',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ'],
            },
            {
                path: 'kentrade/exchange/messages',
                title: 'KENTRADE Monitoring',
                ab: 'KM',
                privilege: ['DI_ADMIN_READ', 'DI_ADMIN'],
            },
            {
                path: 'kentrade/idf/documents',
                title: 'IDF Documents',
                ab: 'ID',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'kentrade/declaration/documents',
                title: 'Declaration Documents',
                ab: 'DD',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'kentrade/manifest/documents',
                title: 'Manifest Documents',
                ab: 'MD',
                privilege: ['DI_ADMIN', 'DI_ADMIN_READ', 'DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
        ],
        collapse: 'import-inspection',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'DI_OFFICER_CHARGE_READ', 'DI_ADMIN', 'DI_ADMIN_READ', 'DI_DIRECTOR_READ'],
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
                path: 'bills',
                title: 'Corporate Bills',
                ab: 'CB',
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
        path: '/certificates',
        title: 'Issued Certificates',
        type: 'sub',
        collapse: 'certificates',
        icontype: 'stars',
        privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
        children: [
            {
                path: 'coc',
                title: 'COC Certificates',
                ab: 'CC',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'coi',
                title: 'COI Certificates',
                ab: 'CI',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'cor',
                title: 'COR Certificates',
                ab: 'CR',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
            {
                path: 'ncr',
                title: 'NCR Certificates',
                ab: 'NR',
                privilege: ['DI_INSPECTION_OFFICER_READ', 'PVOC_OFFICER_CHARGE_READ', 'DI_DIRECTOR_READ'],
            },
        ],
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

    //Standards Levy
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
        path: '',
        title: 'Standards Levy',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['PERMIT_APPLICATION'],
        collapse: 'standardsLevy',
        children: [
            {path: 'standardsLevy/levyRegistration', title: 'View SL Form', ab: 'SL'},
            {path: 'stdLevyPaidHistory', title: 'Payment History', ab: 'PH'},
            {path: 'stdLevyManPenalty', title: 'Penalties', ab: 'PE'},
        ],
    },
    {
        path: '',
        title: 'Standards Levy',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['SL_MANUFACTURE_VIEW'],
        collapse: 'levy',
        children: [
            {path: 'slApplications', title: 'Firms', ab: 'FS'},
            {path: 'slPendingTasks', title: 'Pending Tasks', ab: 'PT'},
            {path: 'slCompleteTasks', title: 'Complete Tasks', ab: 'CT'},
            {path: 'stdLevyPaid', title: 'View Payments', ab: 'VP'},
            {path: 'stdLevyPenalties', title: 'View Penalties', ab: 'VP'},
            {path: 'stdLevyDefaulters', title: 'View Defaulters', ab: 'VD'},

        ],
    },
    {
        path: '',
        title: 'Reports',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['SL_MANUFACTURE_VIEW'],
        collapse: 'reports',
        children: [
            {path: 'slRegisteredFirms', title: 'Registered Firms', ab: 'RF'},
            {path: 'slActiveFirms', title: 'Active Firms', ab: 'AF'},
            {path: 'slDormantFirms', title: 'Dormant Firms', ab: 'DF'},
            {path: 'slClosedFirms', title: 'Closed Firms', ab: 'CF'},
            {path: 'slAllLevyPayments', title: 'Levy Payments', ab: 'LP'},
            {path: 'slPenaltyReport', title: 'Penalties', ab: 'PE'},
            {path: 'stdLevyHistoricalPayments', title: 'Payments Made', ab: 'PM'},
            {path: 'reports/qa-sl-report', title: 'QA-SL Reports', ab: 'QSR'},

        ],
    },

    {
        path: '',
        title: 'Company Edits',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['SL_MANUFACTURE_VIEW'],
        collapse: 'edits',
        children: [
            {path: 'slRejectedEdits', title: 'Profile Changes', ab: 'PC'}

        ],
    },


    // Market Surveillance


    {
        path: '/notifications',
        title: 'Notifications',
        type: 'link',
        collapse: 'notifications',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        icontype: 'business',
    },
    {
        path: '/epra',
        title: 'EPRA',
        type: 'link',
        collapse: 'epra',
        privilege: ['FUEL_ASSISTANT_MANAGER_MODIFY', 'FUEL_HOD_MODIFY', 'MS_MP_MODIFY', 'MS_IOP_MODIFY'],
        icontype: 'receipt',
    },
    {
        path: '/complaint',
        title: 'Complaints',
        type: 'link',
        collapse: 'complaint',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        icontype: 'verified',
    },
    {
        path: '/workPlan',
        title: 'Work-Plan Schedule(s)',
        type: 'link',
        collapse: 'workPlan',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        icontype: 'diamond',
    },
    {
        path: '/complaintPlan',
        title: 'Complaint Schedule(s)',
        type: 'link',
        collapse: 'complaintPlan',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        icontype: 'receipt',
    },
    {
        path: '/msReports',
        title: 'MS Reports',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['MS_IO_READ', 'MS_HOD_READ', 'MS_RM_READ', 'MS_HOF_READ', 'MS_DIRECTOR_READ'],
        collapse: 'reports',
        children: [
            {path: 'consumerComplaint', title: 'Consumer Complaint view', ab: 'CMV'},
            {path: 'seizeGoods', title: 'Seized Goods view', ab: 'SGV'},
            {path: 'submittedSamplesSummary', title: 'Submitted Samples view', ab: 'SSV'},
            // {path: 'complaintMonitoring', title: 'Complaint Monitoring', ab: 'CM'},
            {path: 'fieldInspectionSummary', title: 'Field Inspection Summary', ab: 'FIS'},
            {path: 'workPlanMonitoringTool', title: 'Work-Plan Monitoring Tool', ab: 'WMT'},
            // {path: 'acknowledgement', title: 'Acknowledgement', ab: 'ACK'},
            // {path: 'feedbackTimeline', title: 'Feedback Timeline', ab: 'FT'},
            // {path: 'statusReport', title: 'Status Report', ab: 'SR'},
            // {path: 'sampleSubmitted', title: 'Samples Submitted', ab: 'SS'},
        ],
    },

    // Standards Development
    {
        path: '',
        title: 'Request Module',
        type: 'sub',

        children: [
            {
                path: 'ms-standards',
                title: 'Review Standard Requests',
                ab: 'RSR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'HOF_SD_READ'],
            },
            {
                path: 'std-tsc-sec-task',
                title: 'New Work Item',
                ab: 'NWI',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ'],
            },
            {
                path: 'std-tc-task',
                title: 'Vote On New Work Item',
                ab: 'V',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SD_READ'],
            },
            {
                path: 'upload-justification',
                title: 'Justification',
                ab: 'J',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ'],
            },
            {
                path: 'decision-justification',
                title: 'Decision On Justification',
                ab: 'DOJ',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SPC_SEC_SD_READ'],
            },
            // {path: 'upload-workplan', title: 'Upload Workplan', ab: 'UW'},


        ],
        icontype: 'receipt',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'HOF_SD_READ', 'TC_SEC_SD_READ', 'TC_SD_READ', 'SPC_SEC_SD_READ'],
        collapse: 'requestModule',


    },
    {
        path: '',
        title: 'Committee Module',
        type: 'sub',
        children: [
            {
                path: 'preparePd',
                title: 'Prepare Preliminary Draft',
                ab: 'PRD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ'],
            },
            {
                path: 'reviewPd',
                title: 'Review Preliminary Draft',
                ab: 'RPD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ'],
            },
            {
                path: 'prepareCd',
                title: 'Prepare Committee Draft',
                ab: 'V',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ'],
            },
            {
                path: 'reviewCd',
                title: 'Review Committee Draft',
                ab: 'UJ',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ'],
            },
            {
                path: 'approveCD',
                title: 'Approve Committee Draft',
                ab: 'UW',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ'],
            },


        ],
        icontype: 'group',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ'],
        collapse: 'committeeModule',
    },
    {
        path: '',
        title: 'Public Review Module',
        type: 'sub',
        children: [
            {
                path: 'preparePrd',
                title: 'Prepare Public Review Draft',
                ab: 'PRD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ']
            },
            {
                path: 'commentOnPrd',
                title: 'Review Public Review Draft',
                ab: 'RPD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ'],
            },
            {
                path: 'viewPrd',
                title: 'Review Public Review Draft',
                ab: 'V',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ']
            },


        ],
        icontype: 'public',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ'],
        collapse: 'publicReviewModule',
    },
    {
        path: '',
        title: 'Balloting Module',
        type: 'sub',
        children: [
            {
                path: 'prepareBallot',
                title: 'Prepare Ballot Draft',
                ab: 'PBD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ']
            },
            {
                path: 'voteOnBallot',
                title: 'Vote On Ballot Draft',
                ab: 'V',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ']
            },
            {
                path: 'reviewBallotDraft',
                title: 'Review Ballot Draft',
                ab: 'RBD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ']
            },

        ],
        icontype: 'ballot',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'TC_SD_READ'],
        collapse: 'ballotingModule',
    },
    {
        path: '',
        title: 'Publishing',
        type: 'sub',

        children: [
            {path: 'hopTasks', title: 'Review Draft Standards', ab: 'RDS', privilege: ['HOP_SD_READ']},
            {path: 'editorTasks', title: 'Editor Tasks', ab: 'EDS', privilege: ['HOP_SD_READ', 'EDITOR_SD_READ']},
            {
                path: 'draughtsmanTasks',
                title: 'Draught Draft Standards',
                ab: 'DDS',
                privilege: ['HOP_SD_READ', 'DRAUGHTSMAN_SD_READ']
            },
            {
                path: 'proofReaderTasks',
                title: 'Proofread Draft Standards',
                ab: 'PDS',
                privilege: ['HOP_SD_READ', 'PROOFREADER_SD_READ']
            },
            // {path: 'hopApproval', title: 'Approve Draft Standards', ab: 'ADS'},
        ],
        icontype: 'publish',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOP_SD_READ', 'EDITOR_SD_READ', 'DRAUGHTSMAN_SD_READ', 'PROOFREADER_SD_READ'],
        collapse: 'publish',
    },
    {
        path: '',
        title: 'Formation Of A TC',
        type: 'sub',

        children: [
            {
                path: 'requestForFormationOfTC',
                title: 'Request For Formation',
                ab: 'RFF',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ']
            },
            {
                path: 'hofReviewJustificationOfTC',
                title: 'HOF Review',
                ab: 'HOF',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOF_SD_READ']
            },
            {
                path: 'reviewJustificationOfTC',
                title: 'SPC Review',
                ab: 'SPC',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SPC_SD_READ']
            },
            {
                path: 'reviewFeedbackSAC',
                title: 'SAC Review',
                ab: 'SAC',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SAC_SEC_SD_READ']
            },


        ],
        icontype: 'group',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'HOF_SD_READ', 'SPC_SD_READ', 'SAC_SEC_SD_READ'],
        collapse: 'formation',
    },
    {
        path: '',
        title: 'Membership To A TC',
        type: 'sub',
        icontype: 'groups',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN'],
        collapse: 'membership',
        children: [
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
        children: [
            {
                path: 'nwaJustification',
                title: 'Prepare Justification',
                ab: 'PJ', privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'viewJustification',
                title: 'View Justification',
                ab: 'VW',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SPC_SEC_SD_READ']
            },
            {
                path: 'nwaPreparePD',
                title: 'Preliminary Draft',
                ab: 'PD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'nwaViewPD',
                title: 'View Draft',
                ab: 'VD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'nwaEditPD',
                title: 'Make Changes on Draft',
                ab: 'ED',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'nwaWdEditing',
                title: 'Submit to Publishing',
                ab: 'SB',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'EDITOR_SD_READ']
            },

        ],
        icontype: 'handshake',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_READ', 'SPC_SEC_SD_READ', 'HOP_SD_READ', 'TC_SEC_SD_READ', 'TC_SEC_SD_MODIFY', 'SAC_SEC_SD_READ', 'HO_SIC_SD_READ', 'HOD_SIC_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'WA',
    },

    {
        path: '',
        title: 'National Enquiry Point',
        type: 'sub',
        children: [
            //{path: 'make_enquiry', title: 'Make Enquiry', ab: 'ME'},
            {
                path: 'nepResponse',
                title: 'Manage Enquiries',
                ab: 'EM',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'NEP_OFFICER_SD_READ']
            },
            //{path: 'nep_division_response', title: 'Division Response', ab: 'DR'},
            {path: 'sendFeedBack', title: 'Send Feedback', ab: 'SF'},
            {path: 'prepareReviewDraft', title: 'Prepare Review Draft', ab: 'PR'},
            {path: 'viewDraftNotification', title: 'View Draft', ab: 'VD'},
            {path: 'managerViewNotification', title: 'View Notification', ab: 'VN'},
            {path: 'uploadNotification', title: 'Upload Notification', ab: 'UN'},
            {path: 'uploadedNotification', title: 'Uploaded Notification', ab: 'UN'},

        ],
        icontype: 'quiz',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'NEP_OFFICER_SD_READ'],
        collapse: 'nep',
    },

    {
        path: '',
        title: 'Company Standard',
        type: 'sub',
        children: [
            {
                path: 'comStdList',
                title: 'Company Standard Requests',
                ab: 'CSR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ'],
            },
            {
                path: 'comStdDraft',
                title: 'Uploaded Drafts',
                ab: 'UD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'EDITOR_SD_READ'],
            },
            {
                path: 'comStdEdit',
                title: 'Submit to Publishing',
                ab: 'SP',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'EDITOR_SD_READ'],
            },
            {
                path: 'companyStandardList',
                title: 'Company Standards',
                ab: 'CS',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'PL_SD_READ', 'SPC_SEC_SD_READ', 'JC_SEC_SD_READ', 'COM_SEC_SD_READ', 'HOP_SD_READ', 'SAC_SEC_SD_READ'],
            },
            {
                path: 'comStdListed',
                title: 'Standards',
                ab: 'ST',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'PL_SD_READ', 'SPC_SEC_SD_READ', 'JC_SEC_SD_READ', 'COM_SEC_SD_READ', 'HOP_SD_READ', 'SAC_SEC_SD_READ', 'EDITOR_SD_READ'],
            }

        ],
        icontype: 'business',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'PL_SD_READ', 'SPC_SEC_SD_READ', 'JC_SEC_SD_READ', 'COM_SEC_SD_READ', 'HOP_SD_READ', 'SAC_SEC_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'cs',
    },
    {
        path: '',
        title: 'International Standards',
        type: 'sub',
        children: [
            {
                path: 'isProposalForm',
                title: 'Prepare Proposal',
                ab: 'C',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'isProposals',
                title: 'View Proposals ',
                ab: 'VP',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'isPrepareJustification',
                title: 'Prepare Justification',
                ab: 'PJ',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'isJustificationApp',
                title: 'View Justification',
                ab: 'VJ',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SPC_SEC_SD_READ']
            },
            {
                path: 'isUploadDraft',
                title: 'Upload Draft',
                ab: 'UD',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'EDITOR_SD_READ']
            },
            {
                path: 'isSacApproval',
                title: 'SAC Tasks',
                ab: 'ST',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SAC_SD_READ']
            },
            {
                path: 'isUploadStd',
                title: 'Upload Standard',
                ab: 'US',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOP_SD_READ']
            },
            {
                path: 'intStandardLists',
                title: 'International Standards',
                ab: 'IS',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'PL_SD_READ', 'SPC_SEC_SD_READ', 'JC_SEC_SD_READ', 'COM_SEC_SD_READ', 'HOP_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'EDITOR_SD_READ']
            },


        ],
        icontype: 'business',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_TWO_SD_READ', 'PL_SD_READ', 'SPC_SEC_SD_READ', 'JC_SEC_SD_READ', 'COM_SEC_SD_READ', 'HOP_SD_READ', 'SAC_SEC_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'is',
    },

    {
        path: '',
        title: 'Standards Review',
        type: 'sub',
        children: [
            {
                path: 'standardsForReview',
                title: 'View Standards',
                ab: 'VW',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'systemicReviewComments',
                title: 'Comment on Systemic Review',
                ab: 'CP',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'SPC_SEC_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'HO_SIC_SD_READ', 'STAKEHOLDERS_SD_READ', 'EDITOR_SD_READ'],
            },
            {
                path: 'reviewStandardsTc',
                title: 'Review Comments',
                ab: 'CA',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'KNW_SEC_SD_READ']
            },
            {
                path: 'reviewStandardsSPC',
                title: 'View Recommendations',
                ab: 'VR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SPC_SEC_SD_READ']
            },
            // {path: 'reviewStandardsSAC', title: 'SAC SEC Tasks', ab: 'SAC'},
            // {path: 'reviewStandardsProofReader', title: 'Proof Read', ab: 'PR'},
            // {path: 'reviewStandardsHOP', title: 'HOP Tasks', ab: 'HOP'},
            // {path: 'reviewStandardsEditor', title: 'Editor Tasks', ab: 'ET'},
            // {path: 'reviewStandardsDraughtsMan', title: 'Draughting', ab: 'DT'},
            // {path: 'reviewStandardsGazette', title: 'Gazette Standard', ab: 'GS'},
            // {path: 'reviewStandardsGazetteDate', title: 'Update Gazetted Standard', ab: 'UGS'},
        ],
        icontype: 'quiz',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'SPC_SEC_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'HO_SIC_SD_READ', 'STAKEHOLDERS_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'SR',
    },
    {
        path: '',
        title: 'Scheme Membership',
        type: 'sub',

        children: [
            {
                path: 'hodReview',
                title: 'Review Requests',
                ab: 'RR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_SIC_SD_READ']
            },
            {
                path: 'sicReview',
                title: 'SIC Review',
                ab: 'RR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SIC_OFFICER_SD_READ']
            },
            // {path: 'sdpayments', title: 'Payments', ab: 'P'},


        ],
        icontype: 'group',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_SIC_SD_READ', 'SIC_OFFICER_SD_READ'],
        collapse: 'sm'
    },
    {
        path: '',
        title: 'Standard Acquisition',
        type: 'sub',

        children: [
            {
                path: 'sicHodReviewRequest',
                title: 'Review Requests',
                ab: 'RR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_SIC_SD_READ']
            },
            {
                path: 'sicOfficerReviewRequest',
                title: 'SIC Review',
                ab: 'RR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SIC_OFFICER_SD_READ']
            },
            {
                path: 'sourceForStandard',
                title: 'SIC Source',
                ab: 'RR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SIC_OFFICER_SD_READ']
            },
            {
                path: 'disseminateStandard',
                title: 'SIC Disseminate',
                ab: 'RR',
                privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'SIC_OFFICER_SD_READ']
            },


        ],
        icontype: 'group',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'HOD_SIC_SD_READ', 'SIC_OFFICER_SD_READ'],
        collapse: 'saq'
    },

    //SD reports
    {
        path: '',
        title: 'SD Reports',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'SPC_SEC_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'HO_SIC_SD_READ', 'STAKEHOLDERS_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'reports',
        children: [
            {path: 'nspStatus', title: 'NSP/Status', ab: 'NS'},
            {path: 'nonePreformingProject', title: 'None Preforming Projects ', ab: 'NPP'},
            {path: 'delayedProjects', title: 'Delayed Projects ', ab: 'DP'},
            {path: 'sacReport', title: 'Standards Approval Committee', ab: 'SAC'},
            {path: 'tcMemberApplication', title: 'TC Member Application', ab: 'PD'},
            {path: 'deparmentalWeeklyReport', title: 'Departmental Weekly Report', ab: 'DWK'},
            {path: 'individualWeeklyReport', title: 'Individual Weekly Report ', ab: 'IWK'},


        ],
    },


    //Publishing reports
    {
        path: '',
        title: 'Publishing Reports',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'SPC_SEC_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'HO_SIC_SD_READ', 'STAKEHOLDERS_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'reports',
        children: [
            {path: 'standardsReceived', title: 'Standards Received ', ab: 'SR'},
            {path: 'standardsEdited', title: 'Standards Edited ', ab: 'SE'},
            {path: 'standardsProofread', title: 'Standards Proofread ', ab: 'SP'},
            {path: 'standardsTypeset', title: 'Standards Typeset', ab: 'ST'},
            {path: 'spcReport', title: 'SPC Report', ab: 'SPC '},
            {path: 'weeklyReport', title: 'Weekly Report', ab: 'WK'},
            {path: 'draughtsmans', title: 'Draughtsman’s Report  ', ab: 'WR'},
            {path: 'publishingEnquiriest', title: 'Publishing Enquiriest', ab: 'PE'},
            {path: 'standardsPrinted', title: 'Standards Printed on Demand', ab: 'SPD'},


        ],
    },

    // SIRC Reports
    {
        path: '',
        title: 'SIRC Reports',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'SPC_SEC_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'HO_SIC_SD_READ', 'STAKEHOLDERS_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'reports',
        children: [
            {path: 'informationRequestRegister', title: 'Information Request Register ', ab: 'IRR'},
            {path: 'publicationOrderingRegister', title: 'Publication Ordering Register', ab: 'POR'},
            {path: 'saleKenyastandard', title: 'Kenya Standard Sale Register ', ab: 'KSSR'},
            {path: 'saleForeignstandard', title: 'Foreign Standard Sale Register', ab: 'FSSR'},
            {path: 'standardRequsitionForm', title: 'Kenya Standard Requsition Form', ab: 'KSRF '},
            {path: 'membershipSubscriptionScheme', title: 'Membership Subscription Scheme', ab: 'MSS'},
            {path: 'catalogueReport', title: 'Catalogue Report  ', ab: 'CR'},
            {path: 'disseminationReport', title: 'Dissemination of Publications', ab: 'DP'},
            {path: 'icsAllocation', title: 'ICS Allocation Report', ab: 'IAR'},


        ],
    },

    // SIRC Reports
    {
        path: '',
        title: ' WTO TBT NEP KIMS Reports ',
        type: 'sub',
        icontype: 'receipt',
        privilege: ['STANDARDS_DEVELOPMENT_FULL_ADMIN', 'TC_SEC_SD_READ', 'SPC_SEC_SD_READ', 'SAC_SEC_SD_READ', 'HOP_SD_READ', 'HO_SIC_SD_READ', 'STAKEHOLDERS_SD_READ', 'EDITOR_SD_READ'],
        collapse: 'reports',
        children: [
            {path: 'domesticNotification', title: 'Domestic Notifications ', ab: 'DN'},
            {path: 'enquiriesHandled', title: 'Enquiries Handled Report', ab: 'IHR'},
            {path: 'standardworkProgramme', title: 'Standard Work Programme Bulletin ', ab: 'SWPB'},


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
