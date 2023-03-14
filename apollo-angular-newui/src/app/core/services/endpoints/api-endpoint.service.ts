import {Injectable} from '@angular/core';
import {environment} from 'src/environments/environment';

@Injectable({
    providedIn: 'root',
})
export class ApiEndpointService {

    /**
     * Map of protocols for API endpoints.
     */
    public static PROTOCOL = {
        HTTP: `http://`,
        HTTPS: `https://`,
    };

    /**
     * Map of domains for API endpoints.
     */
    public static DOMAIN = {
        LOCAL_DEV: environment.base_url,
    };

    /**
     * Map of contexts for API endpoints.
     */
    public static CONTEXT = '';

    /**
     * Map of contexts for QA PROPERTIES endpoints.
     */
    public static QA_APPLICATION_MAP_PROPERTIES = {
        DMARK_TYPE_ID: 1,
        SMARK_TYPE_ID: 2,
        FMARK_TYPE_ID: 3,
        DRAFT_ID: 1,
        PAYMENT_PENDING_STATUS: 5,
    };

    /**
     * Map of contexts for ADMIN PROPERTIES endpoints.
     */
    public static ADMIN_APPLICATION_MAP_PROPERTIES = {
        QA_DEPARTMENT_ID: 1,
        DI_DEPARTMENT_ID: 2,
    };


    public static MS_APPLICATION_MAP_PROPERTIES = {
        epraRoles: ['EPRA'],
        msManagerRoles: ['MS_MP_MODIFY', 'MS_MP_READ'],
        msOfficerRoles: ['MS_IO_MODIFY', 'MS_IO_READ'],
    };

    /**
     * Map of contexts for API endpoints.
     */
    public static AUTH_CONTEXT = '/api/v1/login/b';
    public static ANONYMOUS_CONTEXT = '/api/v1/migration/anonymous';
    public static ANONYMOUS_CONTEXT_MS = '/api/v1/migration/ms/anonymous';
    public static ANONYMOUS_CONTEXT_NEP = '/api/v1/migration/anonymous/National_enquiry_point';


    public static USER_CONTEXT = 'user';
    public static MASTERS_CONTEXT = '/api/v1/migration';
    public static COMPANY_CONTEX = `${ApiEndpointService.MASTERS_CONTEXT}/company`;
    public static SYSTEMS_ADMIN_SECURITY = `${ApiEndpointService.MASTERS_CONTEXT}/security`;
    public static SYSTEMS_ADMIN_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/system/admin/masters`;
    public static QA_CONTEXT = '/api/v1/migration/qa';
    public static STL_CONTEXT = '/api/v1/migration/stdLevy';
    public static SL_CONTEXT = '/api/auth';
    public static QA_CONTEXT_APPLY = `${ApiEndpointService.QA_CONTEXT}/permit/apply`;
    public static QA_INTERNAL_USER_CONTEXT = `${ApiEndpointService.QA_CONTEXT}/internal-users`;
    public static QA_CONTEXT_VIEW = `${ApiEndpointService.QA_CONTEXT}/permit/view`;
    public static ADMIN_CONTEXT = 'api/admin/v1';
    public static SD_NWA_CONTEXT = '/api/v1/migration/wa';
    public static SD_NEP_NATIONAL_ENQUIRY = `${ApiEndpointService.MASTERS_CONTEXT}/National_enquiry_point`;
    public static SD_SCHEME_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/Scheme_membership`;
    public static SD_IST_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/international_standard`;
    public static SD_ICT_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/company_standard`;
    public static SD_DR_CONTEXT = `${ApiEndpointService.ANONYMOUS_CONTEXT}/standard/dropdown`;
    public static SD_SR_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/standard_review`;
    public static SD_STD_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/standard`;
    public static SD_PB_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/publishing`;
    public static SD_NEP_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/Domestic_notification`;
    public static REQUEST_STANDARD = `${ApiEndpointService.ANONYMOUS_CONTEXT}/standard/dropdown`;
    public static MS_CONTEXT = '/api/v1/migration/ms';
    public static MS_FUEL_CONTEXT = `${ApiEndpointService.MS_CONTEXT}/fuel`;
    public static MS_COMMON_VIEW_PDF_CONTEXT = `${ApiEndpointService.MS_CONTEXT}/view/attached`;
    public static MS_WORK_PLAN_CONTEXT = `${ApiEndpointService.MS_CONTEXT}/workPlan`;
    public static MS_COMPLAINT_PLAN_CONTEXT = `${ApiEndpointService.MS_CONTEXT}/complaintPlan`;
    public static MS_WORK_PLAN_UPDATE_CONTEXT = `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/update`;
    public static MS_WORK_PLAN_ADD_CONTEXT = `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/add`;
    public static MS_COMPLAINT_CONTEXT = `${ApiEndpointService.MS_CONTEXT}/complaint`;
    public static MS_COMPLAINT_UPDATE_CONTEXT = `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/update`;
    public static MS_COMPLAINT_REPORT_CONTEXT = `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/reports`;
    public static MS_COMMON_CONTEXT = `${ApiEndpointService.MS_CONTEXT}/common`;

    /**
     * Map of API endpoints.
     */
    public static ENDPOINT = {
        LOGOUT: `${ApiEndpointService.USER_CONTEXT}/logout/`,
        REGISTER: `${ApiEndpointService.AUTH_CONTEXT}/signUp`,
        VALIDATE: `${ApiEndpointService.AUTH_CONTEXT}/activate`,
        REQUEST_RESET: `${ApiEndpointService.ANONYMOUS_CONTEXT}/reset`,
        RESET: `${ApiEndpointService.ANONYMOUS_CONTEXT}/reset/`,
        BEER: 'beer/',
        // USER_DETAILS: `${ApiEndpointService.AUTH_CONTEXT}/details`,
        LOGIN_PAGE: `${ApiEndpointService.AUTH_CONTEXT}`,
        VALIDATE_BRS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/validateBrs`,
        HOME_PAGE: `${ApiEndpointService.ANONYMOUS_CONTEXT}`,
        SEND_TOKEN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/sendToken`,
        SEND_TOKEN_FOR_USER: `${ApiEndpointService.ANONYMOUS_CONTEXT}/sendTokenForUser`,
        VALIDATE_TOKEN_FOR_USER: `${ApiEndpointService.MASTERS_CONTEXT}/validateToken`,
        RESET_PASSWORD_VALIDATE_TOKEN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/resetPasswordValidateToken`,
        VALIDATE_TOKEN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/validateToken`,
        REGISTER_COMPANY: `${ApiEndpointService.ANONYMOUS_CONTEXT}/registerCompany`,
        REGISTER_TIVET: `${ApiEndpointService.ANONYMOUS_CONTEXT}/registerTivet`,

        COMPANY_LIST: `${ApiEndpointService.MASTERS_CONTEXT}/company/`,

        USER_DETAILS: `${ApiEndpointService.MASTERS_CONTEXT}/secure/user/details/`,
        USER_NOTIFICATION: `${ApiEndpointService.MASTERS_CONTEXT}/secure/user/notifications/`,
        USER_DETAILS_SIGNATURE: `${ApiEndpointService.MASTERS_CONTEXT}/secure/user/details/signature`,
        LOGOUT_URL: `${ApiEndpointService.MASTERS_CONTEXT}/secure/logout`,
        COMPANY_DETAIL_URL: `${ApiEndpointService.MASTERS_CONTEXT}/secure/companyDetails`,
        BRANCH_LIST: `${ApiEndpointService.MASTERS_CONTEXT}/{companyId}/branches/`,
        REGIONS_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/regions`,
        TITLES_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/titles/`,
        COUNTY_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/county/`,
        TOWN_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/towns`,
        BUSINESS_LINES_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/businessLines`,
        BUSINESS_NATURES_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/businessNatures`,
        COMPANY_DETAILS: `${ApiEndpointService.USER_CONTEXT}/company/details`,
        ADMIN_ROLES: `${ApiEndpointService.ADMIN_CONTEXT}/roles/`,
        ADMIN_ROLES_BY_STATUS: `${ApiEndpointService.ADMIN_CONTEXT}/access/roles`,
        ADMIN_USERS_BY_STATUS: `${ApiEndpointService.ADMIN_CONTEXT}/access/users`,
        ADMIN_ROLES_BY_USER: `${ApiEndpointService.ADMIN_CONTEXT}/access/rbac/users`,
        ADMIN_ROLES_BY_USER_ASSIGN: `${ApiEndpointService.ADMIN_CONTEXT}/access/rbac/users/assign`,
        ADMIN_ROLES_BY_USER_REVOKE: `${ApiEndpointService.ADMIN_CONTEXT}/access/rbac/users/revoke`,
        ADMIN_PERMISSIONS_BY_STATUS: `${ApiEndpointService.ADMIN_CONTEXT}/access/permissions/`,
        ADMIN_ROLES_BY_PERMISSIONS: `${ApiEndpointService.ADMIN_CONTEXT}/access/rbac/roles/`,
        ADMIN_ROLES_BY_PERMISSIONS_ASSIGN: `${ApiEndpointService.ADMIN_CONTEXT}/access/rbac/roles/assign`,
        ADMIN_ROLES_BY_PERMISSIONS_REVOKE: `${ApiEndpointService.ADMIN_CONTEXT}/access/rbac/roles/revoke`,
        ADMIN_AUTHORITIES: `${ApiEndpointService.ADMIN_CONTEXT}/permissions/`,
        ADMIN_ORGANIZATIONS: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/`,
        ADMIN_ACTIVE_ORGANIZATIONS: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/active`,
        ADMIN_APPLICATIONS: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/`,
        MASTER_USSD_CODES_S: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/ussd/code/s`,
        MASTER_USSD_CODES: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/ussd/code/`,
        MASTER_USSD_MENU: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/ussd/menu/`,
        MASTER_USSD_MENU_S: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/ussd/menu/s`,
        MASTER_USSD_WHITE_LIST: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/ussd/whitelist/`,
        MASTER_INTEGRATIONS: `${ApiEndpointService.ADMIN_CONTEXT}/external/`,
        MASTER_INTEGRATIONS_DETAILS: `${ApiEndpointService.ADMIN_CONTEXT}/external/details/`,
        MASTER_INTEGRATIONS_DETAILS_S: `${ApiEndpointService.ADMIN_CONTEXT}/external/details/s`,
        MASTER_INTEGRATIONS_HEADERS: `${ApiEndpointService.ADMIN_CONTEXT}/external/headers/`,
        MASTER_INTEGRATIONS_HEADERS_S: `${ApiEndpointService.ADMIN_CONTEXT}/external/headers/s`,
        MASTER_USSD_WHITE_LIST_S: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/ussd/whitelist/s`,
        ADMIN_APPLICATIONS_BY_ORG: `${ApiEndpointService.ADMIN_CONTEXT}/organizations/applications/byOrg`,
        MASTERS_POSTING_TYPE: `${ApiEndpointService.ADMIN_CONTEXT}/posting/type/`,
        MASTERS_ACCOUNT_MASTER: `${ApiEndpointService.ADMIN_CONTEXT}/account/master/`,
        MASTERS_ACCOUNT_TYPE: `${ApiEndpointService.ADMIN_CONTEXT}/account/type/`,
        MASTERS_ACCOUNTING_SIDE: `${ApiEndpointService.ADMIN_CONTEXT}/accounting/side/`,
        MASTERS_ACCOUNTING_PERIOD: `${ApiEndpointService.ADMIN_CONTEXT}/accounting/period/`,
        ADMIN_USER_DETAILS: `${ApiEndpointService.ADMIN_CONTEXT}/users/`,
        MASTERS: `${ApiEndpointService.MASTERS_CONTEXT}`,
        MASTERS_TITLES: `${ApiEndpointService.MASTERS_CONTEXT}/titles/`,
        MASTERS_COUNTRIES: `${ApiEndpointService.MASTERS_CONTEXT}/countries/`,
        MASTERS_CURRENCIES: `${ApiEndpointService.MASTERS_CONTEXT}/currencies/`,
        // tslint:disable-next-line:max-line-length
        /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::USERS & SYSTEM ADMIN ENDPOINTS::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        // MASTER Endpoints
        LOAD_DESIGNATIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/designations/load`,
        LOAD_DESIGNATIONS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/designations/loads`,
        LOAD_DEPARTMENTS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/departments/load`,
        LOAD_DEPARTMENTS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/departments/loads`,
        LOAD_DIVISIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/divisions/load`,
        LOAD_DIVISIONS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/divisions/loads`,
        LOAD_DIRECTORATE: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/directorate/load`,
        LOAD_DIRECTORATE_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/directorate/loads`,
        LOAD_REGIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/regions/load`,
        LOAD_REGIONS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/regions/loads`,
        LOAD_SUB_REGIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/subRegions/load`,
        LOAD_SECTIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/sections/load`,
        LOAD_SECTIONS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/sections/loads`,
        LOAD_USER_TYPES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/userTypes/load`,

        LOAD_SUB_SECTIONS_L1_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/subsections/l1/loads`,
        LOAD_SUB_SECTIONS_L1: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/subsections/l1/load`,
        LOAD_SUB_SECTIONS_L2: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/subsections/l2/load`,
        LOAD_SUB_SECTIONS_L2_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/subsections/l2/loads`,
        LOAD_FREIGHT_STATIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/freightStations/load`,
        LOAD_FREIGHT_STATIONS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/freightStations/loads`,
        LOAD_COUNTIES: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/counties/load`,
        LOAD_COUNTIES_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/counties/loads`,
        LOAD_TOWNS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/towns/load`,
        LOAD_TOWNS_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/towns/loads`,
        LOAD_BUSINESS_LINES: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/businessLines/load`,
        LOAD_BUSINESS_NATURES: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/businessNatures/load`,
        LOAD_REGION_COUNTY_TOWN: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/regionCountyTown/load`,
        LOAD_REGION_SUB_REGION: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/regionSubRegion/load`,
        LOAD_DIRECTORATE_DESIGNATIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/directorateDesignations/load`,
        LOAD_DIRECTORATE_TO_SUB_SECTIONL2: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/directorateToSubSectionL2/load`,
        LOAD_STANDARD_PRODUCT_CATEGORY: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/standardProductCategory/load`,


        // Users Endpoints
        USER_CREATE_EMPLOYEE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/`,
        USER_UPDATE_EMPLOYEE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/`,
        LOAD_USERS_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/load`,
        USER_SELECTED_DETAILS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/user-details`,
        USER_DETAILS_BY_EMAIL: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/user-email-exist`,
        USER_DETAILS_BY_USERNAME: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/user-username-exist`,
        USER_SEARCH: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/users/search`,
        // Titles Endpoint
        LOAD_TITLE_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/titles/load`,
        TITLE_LIST_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/titles/loads`,
        ADD_TITLE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/titles/`,
        UPDATE_TITLE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/titles/`,
        // Roles Endpoint
        LOAD_ROLES_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/roles/load`,
        ROLES_LIST_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/roles/loads`,
        ADD_ROLES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/roles/`,
        UPDATE_ROLES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/roles/`,
        // Authorities Endpoint
        LOAD_AUTHORITIES_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/authorities/load`,
        AUTHORITIES_LIST_BY_STATUS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/authorities/loads`,
        ADD_AUTHORITIES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/authorities/`,
        UPDATE_AUTHORITIES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/authorities/`,
        // RBAC Endpoint
        FETCH_ACTIVE_ROLE_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/roles/`,
        AUTHORITIES_BY_ROLE_AND_STATUS_LISTING: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/authorities/`,
        REVOKE_AUTHORIZATION_FROM_ROLE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/revoke/`,
        ASSIGN_AUTHORIZATION_TO_ROLE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/assign/`,
        LIST_ACTIVE_RBAC_USERS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/users/`,
        LIST_USER_TYPE: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-type/`,

        LIST_ACTIVE_RBAC_USERS_ROLES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-roles/`,
        LIST_ACTIVE_RBAC_USERS_SECTION: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-section/`,
        LIST_ACTIVE_RBAC_USERS_CFS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-cfs/`,
        REVOKE_ROLE_FROM_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/role/revoke/`,
        ASSIGN_ROLE_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/role/assign/`,
        REVOKE_SECTION_FROM_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/section/revoke/`,
        ASSIGN_SECTION_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/section/assign/`,
        REVOKE_CFS_FROM_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/cfs/revoke/`,
        ASSIGN_CFS_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/cfs/assign/`,
        ASSIGN_USER_TYPE_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/userType/assign/`,
        REVOKE_USER_TYPE_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/userType/revoke/`,

        // tslint:disable-next-line:max-line-length
        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::QA ENDPOINTS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        PERMIT_QR_CODE_SCAN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/permit-qrcode/details`,

        PERMIT_PROCESS_STEP: `${ApiEndpointService.QA_CONTEXT_APPLY}/process-step-add`,

        FIRM_PERMIT_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/firm-list`,
        QA_MPESA_STK_PUSH: `${ApiEndpointService.QA_CONTEXT}/permit/mpesa/stk-push`,
        PERMIT_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/list`,
        CLONE_LIST_SMARK: `${ApiEndpointService.QA_CONTEXT}/permit/smark-clone-list`,
        CLONE_LIST_DMARK: `${ApiEndpointService.QA_CONTEXT}/permit/dmark-clone-list`,

        PERMIT_LIST_AWARDED: `${ApiEndpointService.QA_CONTEXT}/permit/awarded-list`,
        PERMIT_LIST_MIGRATION: `${ApiEndpointService.QA_CONTEXT}/permit/my-permits-loaded`,
        PERMIT_LIST_MIGRATION_DMARK: `${ApiEndpointService.QA_CONTEXT}/permit/my-permits-loaded-dmark`,
        PERMIT_LIST_MIGRATION_FMARK: `${ApiEndpointService.QA_CONTEXT}/permit/my-permits-loaded-fmark`,

        PERMIT_LIST_ALL: `${ApiEndpointService.QA_CONTEXT}/permit/all-my-permits-loaded`,
        PERMIT_COMPLETELY_LIST_AWARDED: `${ApiEndpointService.QA_CONTEXT}/permit/awarded-list-completely`,
        DELETE_PERMIT: `${ApiEndpointService.QA_CONTEXT}/permit/delete`,

        PERMIT_LIST_TO_GENERATE_FMRK: `${ApiEndpointService.QA_CONTEXT}/permit/awarded-list-fmark-generate`,
        PERMIT_LIST_TO_GENERATE_FMARK_ALL_AWARDED: `${ApiEndpointService.QA_CONTEXT}/permit/awarded-list-fmark-generated`,

        PERMIT_VIEW_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/details`,
        PERMIT_SUBMIT_DETAILS_FOR_REVIEW: `${ApiEndpointService.QA_CONTEXT_APPLY}/submit-application-review`,
        PERMIT_SUBMIT_DETAILS_FOR_HOD_QAM_REVIEW: `${ApiEndpointService.QA_CONTEXT_APPLY}/submit-application-qam-hod-review`,
        PERMIT_SUBMIT_APPLICATION: `${ApiEndpointService.QA_CONTEXT_APPLY}/submit-application`,
        PERMIT_SUBMIT_SSC_APPROVAL_REJECTION: `${ApiEndpointService.QA_CONTEXT_APPLY}/submit-application-ssc-approval-rejection`,
        PERMIT_RE_SUBMIT_APPLICATION: `${ApiEndpointService.QA_CONTEXT_APPLY}/re-submit-application`,

        PERMIT_RENEW: `${ApiEndpointService.QA_CONTEXT}/permit/renew/`,
        PLANT_LIST: `${ApiEndpointService.QA_CONTEXT}/branch-list`,
        SECTION_LIST: `${ApiEndpointService.QA_CONTEXT}/sections-list`,
        MY_TASK_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/task-list`,

        PERMIT_REPORTS: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allPermitsWithNoFmarkGenerated`,
        PERMIT_REPORTS_ALL_AWARDED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allPermitsAwarded`,
        PERMIT_REPORTS_ALL_RENEWED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allPermitsRenewed`,
        PERMIT_REPORTS_ALL_SAMPLES_SUBMITTED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allSamplesSubmitted`,
        PERMIT_REPORTS_ALL_DEJECTED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allDejectedPermits`,
        PERMIT_REPORTS_ALL_STATUSES: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allStatuses`,
        PERMIT_REPORTS_ALL_OFFICES: `${ApiEndpointService.QA_CONTEXT}/permit/reports/allOfficers`,
        FILTER_REPORTS: `${ApiEndpointService.QA_CONTEXT}/permit/reports/filter`,
        FILTER_REPORTS_AWARDED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/filterAwarded`,
        FILTER_REPORTS_RENEWED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/filterRenewed`,
        FILTER_REPORTS_DEJECTED: `${ApiEndpointService.QA_CONTEXT}/permit/reports/filterDejected`,


        VIEW_ALL_PAYMENTS: `${ApiEndpointService.QA_CONTEXT}/payments`,


        PERMIT_APPLY_FMARK: `${ApiEndpointService.QA_CONTEXT_APPLY}/fmark`,
        PERMIT_APPLY_STA1: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta1`,
        PERMIT_VIEW_STA1: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta1`,
        PERMIT_UPDATE_STA1: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta1-update`,
        PERMIT_APPLY_STA3: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta3`,
        PERMIT_VIEW_STA3: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta3`,
        PERMIT_UPDATE_STA3: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta3-update`,

        UPLOAD_FILE: `${ApiEndpointService.QA_CONTEXT_APPLY}/ordinary-upload`,
        UPLOAD_FILE_STA3: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta3-update-upload`,
        UPLOAD_FILE_STA10: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10-update-upload`,
        UPDATE_PERMIT_MIGRATED: `${ApiEndpointService.QA_CONTEXT_APPLY}/updateMigratedPermit`,


        INVOICE_CONSOLIDATE_SUBMIT: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-submit`,
        INVOICE_CONSOLIDATE_DIFFERENCE_SUBMIT: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-difference-submit`,
        INVOICE_CONSOLIDATE_ADD: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-add`,
        INVOICE_CONSOLIDATE_REMOVE: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-remove`,
        INVOICE_LIST_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-list`,
        INVOICE_DETAILS_BALANCE: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-balance-details`,
        INVOICE_LIST_NO_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/invoice/list-no-batch-Id`,
        INVOICE_LIST_NO_DIFFERENCE_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/list-no-batch-Id-difference`,
        INVOICE_LIST_NO_DETAILS_PERMIT_TYPE: `${ApiEndpointService.QA_CONTEXT}/permit/invoice/list-no-batch-Id-permit-type`,
        INVOICE_LIST_ALL_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/invoice/list`,
        INVOICE_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-details`,
        INVOICE_DETAILS_PDF: `${ApiEndpointService.QA_CONTEXT}/report/proforma-invoice-with-Item`,


        PERMIT_CERTIFICATE_ISSUED_DETAILS_PDF: `${ApiEndpointService.QA_CONTEXT}/report/permit-certificate`,
        PERMIT_INVOICE_BREAK_DOWN_DETAILS_PDF: `${ApiEndpointService.QA_CONTEXT}/report/braked-down-invoice-with-Item`,
        PERMIT_VIEW_PDF: `${ApiEndpointService.QA_CONTEXT}/view/attached`,
        // INVOICE_DETAILS_PDF: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-pdf-details`,

        PERMIT_APPLY_STA10_FIRM_DETAILS: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/firm_details`,
        PERMIT_VIEW_STA10_FIRM_DETAILS: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/firm-details`,
        PERMIT_VIEW_STA10_DETAILS: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/view-details`,


        PERMIT_APPLY_STA10_PERSONNEL_DETAILS: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/personnel_details`,
        PERMIT_VIEW_STA10_PERSONNEL_DETAILS: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/personnel_details`,
        PERMIT_APPLY_STA10_PRODUCTS_BEING_MANUFACTURED: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/products_being_manufactured`,
        PERMIT_VIEW_STA10_PRODUCTS_BEING_MANUFACTURED: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/products_being_manufactured`,
        PERMIT_APPLY_STA10_RAW_MATERIAL: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/raw_material`,
        PERMIT_VIEW_STA10_RAW_MATERIAL: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/raw_material`,
        PERMIT_APPLY_STA10_MACHINERY_PLANT: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/machinery_plant`,
        PERMIT_VIEW_STA10_MACHINERY_PLANT: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/machinery_plant`,
        PERMIT_APPLY_STA10_MANUFACTURING_PROCESS: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/manufacturing_process`,
        PERMIT_VIEW_STA10_MANUFACTURING_PROCESS: `${ApiEndpointService.QA_CONTEXT_VIEW}/sta10/manufacturing_process`,

        PERMIT_UPDATE_STA10_FIRM_DETAILS: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/firm_details_update`,
        PERMIT_UPDATE_STA10_PERSONNEL_DETAILS: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/personnel_details_update`,
        PERMIT_UPDATE_STA10_PRODUCTS_BEING_MANUFACTURED: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/products_being_manufactured_update`,
        PERMIT_UPDATE_STA10_RAW_MATERIAL: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/raw_material_update`,
        PERMIT_UPDATE_STA10_MACHINERY_PLANT: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/machinery_plant_update`,
        PERMIT_UPDATE_STA10_MANUFACTURING_PROCESS: `${ApiEndpointService.QA_CONTEXT_APPLY}/sta10/manufacturing_process_update`,


        /*::::::::::::::::::::::::::::::SD ENDPOINTS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        // SD Kenya National Workshop Agreement
        NWA_VIEW_STANDARD_REQUEST: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkshopStandards`,
        NWA_PREPARE_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/prepareJustification`,
        NWA_GET_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/getJustification`,
        NWA_VIEW_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkshopJustification`,
        NWA_DEPARTMENTS: `${ApiEndpointService.SD_NWA_CONTEXT}/getKNWDepartments`,
        NWA_KNW_COMMITTEE: `${ApiEndpointService.SD_NWA_CONTEXT}/getKNWCommittee`,
        NWA_KNW_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/knwtasks`,
        NWA_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getUserTasks`,
        NWA_KNW_SECRETARY: `${ApiEndpointService.SD_NWA_CONTEXT}/getKnwSecretary`,
        NWA_DI_DIRECTOR: `${ApiEndpointService.SD_NWA_CONTEXT}/getDirector`,
        NWA_HEAD_OF_PUBLISHING: `${ApiEndpointService.SD_NWA_CONTEXT}/getHeadOfPublishing`,
        NWA_SAC_SECRETARY: `${ApiEndpointService.SD_NWA_CONTEXT}/getSacSecretary`,
        NWA_HEAD_OF_SIC: `${ApiEndpointService.SD_NWA_CONTEXT}/getHeadOfSic`,
        NWA_SPC_SECRETARY: `${ApiEndpointService.SD_NWA_CONTEXT}/getSpcSecretary`,
        NWA_SPC_SEC_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getSpcSecTasks`,
        NWA_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/decisionOnJustification`,
        NWA_DECISION_ON_JUSTIFICATION_KNW: `${ApiEndpointService.SD_NWA_CONTEXT}/decisionOnJustificationKNW`,
        NWA_PREPARE_DISDT_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/prepareDiSdtJustification`,
        NWA_DISDT_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getDiSdtTasks`,
        NWA_DECISION_ON_DISDT_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/decisionOnDiSdtJustification`,
        NWA_PREPARE_PRELIMINARY_DRAFT: `${ApiEndpointService.SD_NWA_CONTEXT}/preparePreliminaryDraft`,
        NWA_EDIT_PRELIMINARY_DRAFT: `${ApiEndpointService.SD_NWA_CONTEXT}/editPreliminaryDraft`,
        NWA_PREPARE_PD: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkshopForPDraft`,
        NWA_GET_EDIT_PD: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkshopForEditing`,
        NWA_GET_PD_FOR_ACTION: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkShopStdDraft`,
        NWA_GET_WD_FOR_EDITING: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkShopStdForEditing`,
        NWA_SUBMIT_DRAFT_FOR_EDITING: `${ApiEndpointService.SD_NWA_CONTEXT}/submitDraftForEditing`,
        NWA_DECISION_ON_STD_DR: `${ApiEndpointService.SD_NWA_CONTEXT}/decisionOnStdDraft`,
        NWA_GET_PD_FOR_EDITING: `${ApiEndpointService.SD_NWA_CONTEXT}/getWorkShopDraftForEditing`,
        NWA_DECISION_ON_PRELIMINARY_DRAFT: `${ApiEndpointService.SD_NWA_CONTEXT}/decisionOnPd`,
        NWA_TC_SEC_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getTCSeCTasks`,
        NWA_HOP_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getHOPTasks`,
        NWA_EDIT_WORKSHOP_DRAFT: `${ApiEndpointService.SD_NWA_CONTEXT}/editWorkshopDraft`,
        NWA_SAC_SEC_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getSacSecTasks`,
        NWA_DECISION_ON_WORKSHOP_DRAFT: `${ApiEndpointService.SD_NWA_CONTEXT}/decisionOnWd`,
        NWA_UPLOAD_STANDARD: `${ApiEndpointService.SD_NWA_CONTEXT}/uploadNwaStandard`,
        NWA_HO_SIC_TASKS: `${ApiEndpointService.SD_NWA_CONTEXT}/getHoSiCTasks`,
        NWA_UPLOAD_GAZETTE_NOTICE: `${ApiEndpointService.SD_NWA_CONTEXT}/uploadGazetteNotice`,
        NWA_UPDATE_GAZETTEMENT_DATE: `${ApiEndpointService.SD_NWA_CONTEXT}/updateGazettementDate`,
        NWA_UPLOAD_DATA: `${ApiEndpointService.SD_NWA_CONTEXT}/file-upload`,
        NWA_UPLOAD_DATA_DI: `${ApiEndpointService.SD_NWA_CONTEXT}/di-file-upload`,
        NWA_UPLOAD_DATA_PD: `${ApiEndpointService.SD_NWA_CONTEXT}/pd-file-upload`,
        NWA_UPLOAD_DATA_WD: `${ApiEndpointService.SD_NWA_CONTEXT}/wd-file-upload`,
        NWA_UPLOAD_DATA_STD: `${ApiEndpointService.SD_NWA_CONTEXT}/std-file-upload`,
        NWA_UPLOAD_DATA_VIEW: `${ApiEndpointService.SD_NWA_CONTEXT}/view/justification`,
        NWA_UPLOAD_DATA_VIEW_DI: `${ApiEndpointService.SD_NWA_CONTEXT}/view/di-justification`,
        NWA_UPLOAD_DATA_VIEW_PD: `${ApiEndpointService.SD_NWA_CONTEXT}/view/preliminaryDraft`,
        NWA_UPLOAD_DATA_VIEW_WD: `${ApiEndpointService.SD_NWA_CONTEXT}/view/workShopDraft`,
        NWA_UPLOAD_DATA_VIEW_STD: `${ApiEndpointService.SD_NWA_CONTEXT}/view/knwStandard`,

        // SD INTERNATIONAL STANDARDS
        IST_GET_STD_STAKE_HOLDERS: `${ApiEndpointService.SD_IST_CONTEXT}/findStandardStakeholders`,
        IST_VIEW_ADOPTION_PROPOSAL_REQUEST: `${ApiEndpointService.SD_IST_CONTEXT}/getIntStandardProposals`,
        IST_PREPARE_ADOPTION_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/prepareAdoptionProposal`,
        IST_UPLOAD_PD: `${ApiEndpointService.SD_IST_CONTEXT}/draft-file-upload`,
        IST_UPLOAD_DOCUMENT: `${ApiEndpointService.SD_IST_CONTEXT}/file-upload`,
        IST_VIEW_IS_PROPOSALS: `${ApiEndpointService.SD_IST_CONTEXT}/getISProposals`,
        IST_VIEW_IS_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/getProposal`,
        IST_VIEW_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/getSessionProposals`,
        IST_VIEW_PROPOSALS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/international_standard/getProposals`,
        IST_VIEW_IS_PROPOSALS_DOC: `${ApiEndpointService.SD_IST_CONTEXT}/view/proposal`,
        IST_SUBMIT_AP_COMMENTS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/international_standard/submitAPComments`,
        IST_SUBMIT_PROP_COMMENTS: `${ApiEndpointService.SD_IST_CONTEXT}/submitProposalComments`,
        IST_TC_SEC_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getTCSECTasks`,
        IST_INT_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getUserTasks`,
        IST_PROPOSAL_COMMENTS: `${ApiEndpointService.SD_IST_CONTEXT}/getAllComments`,
        IST_COM_STD_COMMENTS: `${ApiEndpointService.SD_IST_CONTEXT}/getDraftComments`,
        IST_JUSTIFICATION_COMMENTS: `${ApiEndpointService.SD_IST_CONTEXT}/getUserComments`,
        IST_DECISION_ON_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/decisionOnProposal`,
        IST_APPROVED_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/getApprovedProposals`,
        IST_PREPARE_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/prepareJustification`,
        IST_VIEW_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/getISJustification`,
        IST_APP_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/getJustification`,
        IST_APPR_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/getApprovedJustification`,
        IST_VIEW_APP_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/getApprovedISJustification`,
        IST_UPLOAD_DRAFT_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/submitDraftForEditing`,
        IST_VIEW_DRAFT_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getUploadedDraft`,
        IST_VIEW_PB_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getIsPublishingTasks`,
        IST_VIEW_APPROVED_DRAFT_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getApprovedDraft`,
        IST_EDIT_APPROVED_DRAFT_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/editStandardDraft`,
        IST_VIEW_EDITED_DRAFT_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getEditedDraft`,
        IST_UPLOAD_DRAFTING_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/draughtStandard`,
        IST_VIEW_DRAUGHTED_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getDraughtedDraft`,
        IST_UPLOAD_PROOFREADING_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/proofReadStandard`,
        IST_VIEW_PROOFREAD_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getProofReadDraft`,
        IST_DECISION_PROOFREAD_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/approveProofReadStandard`,
        IST_VIEW_EDITED_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/getApprovedProofReadDraft`,
        IST_DECISION_EDITED_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/approveEditedStandard`,
        IST_VIEW_EDITED_STANDARD_DRAFT: `${ApiEndpointService.SD_IST_CONTEXT}/getApprovedEditedDraft`,
        IST_APPROVE_EDITED_STANDARD_DRAFT: `${ApiEndpointService.SD_IST_CONTEXT}/approveInternationalStandard`,
        IST_VIEW_STANDARD_GAZETTE: `${ApiEndpointService.SD_IST_CONTEXT}/getStandardForGazettement`,
        IST_UPLOAD_STD_GAZETTE: `${ApiEndpointService.SD_IST_CONTEXT}/uploadGazetteNotice`,
        IST_UPLOAD_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/uploadISStandard`,
        IST_UPLOAD_JS_DOCUMENT: `${ApiEndpointService.SD_IST_CONTEXT}/js-file-upload`,
        IST_SPC_SEC_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getSPCSECTasks`,
        IST_VIEW_IS_JUSTIFICATION_DOC: `${ApiEndpointService.SD_IST_CONTEXT}/view/justification`,
        IST_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/decisionOnJustification`,
        IST_SAC_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/justificationDecision`,
        IST_HOP_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/checkRequirements`,
        IST_SAC_SEC_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getSACSECTasks`,
        IST_APPROVE_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/approveStandard`,
        IST_HOP_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getHOPTasks`,
        IS_UPLOAD_STD: `${ApiEndpointService.SD_IST_CONTEXT}/std-file-upload`,
        IS_UPLOAD_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/uploadISStandard`,
        IST_HOS_SIC_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getHoSiCTasks`,
        IST_VIEW_IS_STANDARD_DOC: `${ApiEndpointService.SD_IST_CONTEXT}/view/iStandard`,
        IS_UPLOAD_GAZETTE_NOTICE: `${ApiEndpointService.SD_IST_CONTEXT}/uploadGazetteNotice`,
        IS_UPLOAD_STD_GZT: `${ApiEndpointService.SD_IST_CONTEXT}/gzt-file-upload`,
        IST_VIEW_IS_STANDARD_GZT_DOC: `${ApiEndpointService.SD_IST_CONTEXT}/view/gazettement`,
        IS_UPDATE_GAZETTE_DATE: `${ApiEndpointService.SD_IST_CONTEXT}/updateGazettementDate`,
        IST_SUBMIT_DRAFT_COMMENTS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/international_standard/submitDraftComments`,
        IST_SUBMIT_DRAFT_COMMENT: `${ApiEndpointService.SD_IST_CONTEXT}/submitDraftComment`,
        IST_SUBMIT_STANDARD: `${ApiEndpointService.SD_IST_CONTEXT}/uploadInternationalStandard`,
        IST_VIEW_COM_STANDARDS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/international_standard/getCompanyStandards`,
        IST_VIEW_INT_STANDARDS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/international_standard/getInternationalStandards`,
        IST_VIEW_ALL_STANDARDS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/international_standard/getStandards`,
        ICT_COM_UPLOAD_SD_EDIT: `${ApiEndpointService.SD_ICT_CONTEXT}/viewStandard`,

        // SD COMPANY STANDARDS
        ICT_GET_PRODUCTS: `${ApiEndpointService.SD_ICT_CONTEXT}/getProducts`,
        // ICT_GET_PRODUCTS_LS: `${ApiEndpointService.SD_DR_CONTEXT}/getProducts/${id}`,
        ICT_GET_PRODUCTS_LS: `${ApiEndpointService.SD_DR_CONTEXT}/getProducts/`,
        ICT_GET_DEPARTMENTS: `${ApiEndpointService.SD_DR_CONTEXT}/getDepartments`,
        ICT_GET_USERS: `${ApiEndpointService.SD_ICT_CONTEXT}/getUsers`,
        ICT_GET_DEPARTMENT: `${ApiEndpointService.SD_DR_CONTEXT}/getDepartments`,
        ICT_GET_TC_COMMITTEE: `${ApiEndpointService.SD_DR_CONTEXT}/getTechnicalCommittee`,
        // ICT_GET_PRODUCT_CATEGORIES: `${ApiEndpointService.SD_DR_CONTEXT}/getProductCategories/${id}`,
        ICT_GET_PRODUCT_CATEGORIES: `${ApiEndpointService.SD_DR_CONTEXT}/getProductCategories/`,
        ICT_ADD_STD_REQUEST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/request`,
        ICT_VIEW_COMMITMENT_LETTER: `${ApiEndpointService.SD_ICT_CONTEXT}/view/commitmentLetter`,
        ICT_UPLOAD_COMMITMENT_LETTER: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/commitmentLetter`,
        ICT_HOD_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getHODTasks`,
        ICT_COM_USER_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getUserTasks`,
        ICT_COM_PROPOSAL_COMMENTS: `${ApiEndpointService.SD_ICT_CONTEXT}/getAllComments`,
        ICT_ASSIGN_REQUEST: `${ApiEndpointService.SD_ICT_CONTEXT}/assignRequest`,
        ICT_FORM_JOINT_COMMITTEE: `${ApiEndpointService.SD_ICT_CONTEXT}/formJointCommittee`,
        ICT_PL_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getPlTasks`,
        ICT_COM_REMARKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getComStandardRemarks`,
        ICT_PREPARE_JUSTIFICATION: `${ApiEndpointService.SD_ICT_CONTEXT}/prepareJustification`,
        ICT_UPLOAD_JC: `${ApiEndpointService.SD_ICT_CONTEXT}/file-upload`,
        ICT_SPC_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getSpcSecTasks`,
        ICT_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnJustification`,
        ICT_STANDARDS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/getAllStandards`,
        ICT_UPLOAD_DATA_VIEW_STD: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/view/comStandard`,
        ICT_DECISION_ON_APP_JUSTIFICATION: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnAppJustification`,
        ICT_PREPARE_PRELIMINARY_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/uploadDraft`,
        ICT_UPLOAD_PD: `${ApiEndpointService.SD_ICT_CONTEXT}/draft-file-upload`,
        ICT_JC_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getJcSecTasks`,
        ICT_UPLOAD_DATA_VIEW_PD: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/view/comDraft`,
        ICT_DECISION_ON_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnStdDraft`,
        ICT_COM_DECISION_ON_DRAFT: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/decisionOnComStdDraft`,
        ICT_COM_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getComSecTasks`,
        ICT_PREPARE_COM_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/uploadComStandard`,
        ICT_COM_UPLOAD_SD: `${ApiEndpointService.SD_ICT_CONTEXT}/std-file-upload`,
        ICT_COM_HOP_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getHopTasks`,
        ICT_COM_EDIT_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/editCompanyStandard`,
        ICT_COM_VIEW_STANDARD: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/viewStandard`,



        ICT_COM_STD_REQUEST: `${ApiEndpointService.SD_ICT_CONTEXT}/getCompanyStandardRequest`,
        ICT_COM_STD_REQUEST_PROCESS: `${ApiEndpointService.SD_ICT_CONTEXT}/getCompanyStandardRequestProcess`,
        ICT_COM_STD_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/getUploadedStdDraft`,
        ICT_COM_STD_DRAFT_COMMENT: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/getUploadedStdDraftForComment`,
        ICT_COM_STD_DRAFTS_COMMENT: `${ApiEndpointService.SD_ICT_CONTEXT}/getUploadedSDraftForComment`,
        ICT_COM_STD_COMMENT: `${ApiEndpointService.SD_ICT_CONTEXT}/commentOnDraft`,
        ICT_COM_STD_APPROVED_DRAFT: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/getApprovedStdDraft`,
        ICT_COM_STD_EDITS_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/getStdDraftForEditing`,
        ICT_COM_STD_PB_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getComStdPublishing`,
        ICT_COM_STD_SPB_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getAppStdPublishing`,
        ICT_COM_STD_APB_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getAppStd`,
        ICT_COM_STD_COMMENTS: `${ApiEndpointService.SD_ICT_CONTEXT}/getAllComments`,
        ICT_COM_DRAFT_COMMENTS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/getDraftComments`,
        ICT_COM_DRAFT_COMMENTS_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/getDraftCommentList`,
        ICT_COM_STD_REQUIREMENTS: `${ApiEndpointService.SD_ICT_CONTEXT}/checkRequirements`,
        ICT_COM_STD_EDIT_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/editStandardDraft`,
        ICT_COM_STD_DRAFTING: `${ApiEndpointService.SD_ICT_CONTEXT}/draughtStandard`,
        ICT_COM_STD_PROOF_READ: `${ApiEndpointService.SD_ICT_CONTEXT}/proofReadStandard`,
        ICT_COM_STD_DEC_PROOF_READ: `${ApiEndpointService.SD_ICT_CONTEXT}/approveProofReadStandard`,
        ICT_DECISION_EDITED_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/approveEditedStandard`,
        ICT_APPROVE_COM_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/approveCompanyStandard`,
        ICT_UPLOAD_COM_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/uploadStandardDoc`,
        ICT_REJECT_COM_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/rejectCompanyStandard`,
        ICT_COM_STD_SUBMIT_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/submitDraftForEditing`,
        ICT_COM_STD_DRAFT_DOCUMENT_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/getDraftDocumentList`,
        ICT_SUBMIT_DRAFT_COMMENTS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/submitDraftComments`,
        ICT_SUBMIT_DRAFT_COMMENT: `${ApiEndpointService.SD_ICT_CONTEXT}/submitDraftComment`,
        ICT_CONTACT_DETAILS: `${ApiEndpointService.SD_ICT_CONTEXT}/getCompanyContactDetails`,
        ICT_COMMITTEE_LIST: `${ApiEndpointService.SD_ICT_CONTEXT}/getCommitteeList`,


        // SD SYSTEMIC REVIEW
        SR_GET_REVIEWED_STANDARDS: `${ApiEndpointService.SD_SR_CONTEXT}/reviewedStandards`,
        SR_REVIEW_FORM: `${ApiEndpointService.SD_SR_CONTEXT}/standardReviewForm`,
        SR_GET_REVIEW_FORM: `${ApiEndpointService.SD_SR_CONTEXT}/getReviewForms`,
        SR_REVIEW_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/commentsOnReview`,
        SR_GET_REVIEW_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getReviewTasks`,
        SR_DECISION_ON_RECOMMENDATION: `${ApiEndpointService.SD_SR_CONTEXT}/decisionOnRecommendation`,
        SR_UPLOAD_DOCUMENT: `${ApiEndpointService.SD_SR_CONTEXT}/sr-file-upload`,
        SR_SD_FOR_REVIEW_STD: `${ApiEndpointService.SD_SR_CONTEXT}/getStandardsForReview`,
        SR_INITIATE_STD_REVIEW: `${ApiEndpointService.SD_SR_CONTEXT}/standardReviewForm`,
        SR_GET_PROPOSAL_FOR_COMMENT: `${ApiEndpointService.ANONYMOUS_CONTEXT}/standard_review/getProposals`,
        SR_GET_PROPOSALS_FOR_COMMENT: `${ApiEndpointService.SD_SR_CONTEXT}/getProposal`,
        SR_USER_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getUserTasks`,
        SR_SD_PROPOSAL_FOR_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/getStandardsProposalForComment`,
        SR_SUBMIT_PROPOSAL_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/submitProposalComments`,
        SR_SD_PROPOSAL_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/getStandardsProposalComments`,
        SR_SD_GET_PROPOSAL_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/getProposalsComments`,
        SR_STD_FOR_RECOMMENDATION: `${ApiEndpointService.SD_SR_CONTEXT}/getStandardsForRecommendation`,
        SR_STD_FOR_SPC_ACTION: `${ApiEndpointService.SD_SR_CONTEXT}/getStandardsForSpcAction`,
        SR_SD_PROPOSAL_DECION: `${ApiEndpointService.SD_SR_CONTEXT}/decisionOnStdDraft`,
        SR_SD_PROPOSAL_RECOMMENDATIONS: `${ApiEndpointService.SD_SR_CONTEXT}/makeRecommendationsOnAdoptionProposal`,
        SR_SD_DECISION_ON_RECOMMENDATIONS: `${ApiEndpointService.SD_SR_CONTEXT}/decisionOnRecommendation`,
        SR_SD_PROPOSAL_RECOMMENDATIONS_DECISION_LEVEL_UP: `${ApiEndpointService.SD_SR_CONTEXT}/levelUpDecisionOnRecommendations`,
        SR_SD_UPDATE_GAZETTE: `${ApiEndpointService.SD_SR_CONTEXT}/updateGazette`,
        SR_SD_UPDATE_GAZETTE_DATE: `${ApiEndpointService.SD_SR_CONTEXT}/updateGazettementDate`,
        SR_SD_REVIEW_RECOMMENDATIONS: `${ApiEndpointService.SD_SR_CONTEXT}/reviewRecommendations`,
        SR_SD_SUBMIT_DRAFT_FOR_EDITING: `${ApiEndpointService.SD_SR_CONTEXT}/submitDraftForEditing`,
        SR_SD_CHECK_REQUIREMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/checkRequirements`,
        SR_SD_EDIT_STD_DRAFT: `${ApiEndpointService.SD_SR_CONTEXT}/editStandardDraft`,
        SR_SD_DRAFT_STD: `${ApiEndpointService.SD_SR_CONTEXT}/draftStandard`,
        SR_SD_PROOF_READ_STD: `${ApiEndpointService.SD_SR_CONTEXT}/proofReadStandard`,
        SR_SD_CHECK_STD_DRAFT: `${ApiEndpointService.SD_SR_CONTEXT}/checkStandardDraft`,
        SR_SD_TC_SEC_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getTcSecTasks`,
        SR_SD_DR_MAN_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getDraughtsManTasks`,
        SR_SD_PROOF_READER_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getProofReaderTasks`,
        SR_SD_EDITOR_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getEditorTasks`,
        SR_SD_SAC_SEC_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getSacSecTasks`,
        SR_SD_HOP_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getHopTasks`,
        SR_SD_SPC_SEC_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getSpcSecTasks`,
        SR_SD_GAZETTE_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getHoSicTasks`,
        SR_SD_USER_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/getUserComments`,

        // SD NATIONAL ENQUIRY POINT
        NEP_ENQUIRY_NEP_TASKS: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/nep_officer/tasks`,
        NEP_ENQUIRY_DIVISION_TASKS: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/division/tasks`,
        NEP_MAKE_ENQUIRY: `${ApiEndpointService.ANONYMOUS_CONTEXT_NEP}/notification_request`,
        NEP_UPLOAD_ATTACHMENT: `${ApiEndpointService.ANONYMOUS_CONTEXT_NEP}/nepDocUpload`,
        NEP_VIEW_ATTACHMENT: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/viewRequestUpload`,
        NEP_FETCH_ENQ: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/getNepRequests`,
        NEP_FETCH_REQUESTS: `${ApiEndpointService.ANONYMOUS_CONTEXT_NEP}/getNepDivisionRequests`,
        NEP_FETCH_RESPONSE: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/getNepDivisionResponse`,
        NEP_SEND_RESPONSE: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/decisionOnEnquiryInfo`,
        NEP_SEND_FEED_BACK: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/sendFeedBack`,
        NEP_DIV_SEND_RESPONSE: `${ApiEndpointService.ANONYMOUS_CONTEXT_NEP}/responseOnEnquiryInfo`,
        NEP_INFORMATION_AVAILABLE_YES: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/nep_officer/is_available`,
        NEP_DEPARTMENT_RESPONSE: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/division_response/send_response`,
        NEP_FEEDBACK_EMAIL: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/information_available/send_email`,


        NEP_SEND_DRAFT: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/notificationOfReview`,
        NEP_UPLOAD_DRAFT_ATTACHMENT: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/uploadNepDraftDoc`,
        NEP_GET_DRAFT_NOTIFICATIONS: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/getDraftNotification`,
        NEP_VIEW_DRAFT_UPLOADS: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/uploadNepDraftDoc`,
        NEP_DECISION_ON_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/decisionOnReviewDraft`,
        NEP_MGR_GET_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/getNotificationForApproval`,
        NEP_MGR_DECISION_ON_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/decisionOnNotification`,
        NEP_GET_UPLOAD_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/getDraftNotificationForUpload`,
        NEP_UPLOAD_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/uploadNotification`,
        NEP_UPLOADED_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/getUploadedNotification`,
        NEP_VIEW_DRAFT_NOTIFICATION: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/viewDraftUpload`,



        // SD NEP DOMESTIC NOTIFICATION
        NEP_RETRIEVE_NOTIFICATIONS: `${ApiEndpointService.SD_NEP_CONTEXT}/nep_officer/tasks`,
        NEP_MANAGER_RETRIEVE_NOTIFICATIONS: `${ApiEndpointService.SD_NEP_CONTEXT}/manager/tasks`,
        NEP_ACCEPT_REQUEST_MADE: `${ApiEndpointService.SD_NEP_CONTEXT}/nep_officer/is_accepted`,
        NEP_REJECT_REQUEST_MADE: `${ApiEndpointService.SD_NEP_CONTEXT}/nep_officer/is_accepted`,
        NEP_FINAL_SUBMISSION: `${ApiEndpointService.SD_NEP_CONTEXT}/nep_officer/upload_final`,
        NEP_MANAGER_ACCEPT: `${ApiEndpointService.SD_NEP_CONTEXT}/manager/is_accepted`,
        NEP_UPLOAD_FILE: `${ApiEndpointService.SD_NEP_CONTEXT}/nep_officer/draft_notification`,


        // REQUEST STANDARDS
        REQ_STANDARD: `${ApiEndpointService.REQUEST_STANDARD}/request`,
        REQ_PRODUCTS: `${ApiEndpointService.REQUEST_STANDARD}/getProducts`,
        REQ_PRODUCTS_SUBCATEGORY: `${ApiEndpointService.REQUEST_STANDARD}/getProductCategories`,

        // STANDARDS LEVY
        REG_MANUFACTURE_DETAILS: `${ApiEndpointService.SL_CONTEXT}/kebs/add/manufacture-details/save`,
        STD_LEVY_PENALTY_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getManufacturerPenalty`,
        STD_LEVY_PAID_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getPaidLevies`,
        STD_LEVY_PAYMENT_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getLevyPayments`,
        STD_LEVY_DEFAULTER_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getLevyDefaulters`,
        STD_LEVY_MANUFACTURE_PAYMENT_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getManufacturesLevyPayments`,
        STD_LEVY_MANUFACTURE_PAYMENT_RECEIPT: `${ApiEndpointService.STL_CONTEXT}/getLevyPaymentsReceipt`,
        STD_LEVY_MANUFACTURES_PAYMENT_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getManufacturesLevyPaymentsList`,
        STD_LEVY_PENALTY_DETAIL: `${ApiEndpointService.STL_CONTEXT}/getLevyPenalty`,
        STD_LEVY_MANUFACTURE_PENALTY_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getManufacturesLevyPenalty`,
        STD_LEVY_MANUFACTURE_PAY_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getManufacturesPayments`,
        STD_LEVY_MANUFACTURES_PENALTY_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getManufacturesLevyPenaltyList`,
        STD_LEVY_COMPANY_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getCompanyProfile`,
        STD_LEVY_SL_FORM: `${ApiEndpointService.STL_CONTEXT}/getNotificationFormDetails`,
        STD_LEVY_SL_NT_FORM: `${ApiEndpointService.STL_CONTEXT}/getSlNotificationFormDetails`,
        STD_LEVY_MANUFACTURE_STATUS: `${ApiEndpointService.STL_CONTEXT}/getManufacturerStatus`,
        STD_LEVY_SL10_FORM: `${ApiEndpointService.STL_CONTEXT}/save-sl-notification-form`,
        STD_LEVY_SCHEDULE_SITE_VISIT: `${ApiEndpointService.STL_CONTEXT}/scheduleSiteVisit`,
        STD_LEVY_SCHEDULE_SITE_VISIT_DECISION: `${ApiEndpointService.STL_CONTEXT}/decisionOnSiteVisitSchedule`,
        STD_LEVY_SCHEDULED_SITE_VISITS: `${ApiEndpointService.STL_CONTEXT}/getScheduledVisits`,
        STD_LEVY_ASSIGN_COMPANY_TASK: `${ApiEndpointService.STL_CONTEXT}/assignCompany`,
        STD_LEVY_SAVE_VISIT_REPORT: `${ApiEndpointService.STL_CONTEXT}/reportOnSiteVisit`,
        STD_LEVY_SAVE_VISIT_REPORT_DOCUMENT: `${ApiEndpointService.STL_CONTEXT}/site-report-upload`,
        STD_LEVY_VIEW_VISIT_REPORT_DOCUMENT_LIST: `${ApiEndpointService.STL_CONTEXT}/getVisitDocumentList`,
        STD_LEVY_VIEW_VISIT_REPORT_DOCUMENT: `${ApiEndpointService.STL_CONTEXT}/view/siteVisitReport`,
        STD_LEVY_SCHEDULED_SITE_VISITS_REPORT: `${ApiEndpointService.STL_CONTEXT}/getSiteReport`,
        STD_LEVY_SITE_VISITS_REPORT_APPROVAL_ONE: `${ApiEndpointService.STL_CONTEXT}/decisionOnSiteReport`,
        STD_LEVY_SITE_VISITS_REPORT_LEVEL_TWO: `${ApiEndpointService.STL_CONTEXT}/getSiteReportLevelTwo`,
        STD_LEVY_SITE_VISITS_REPORT_APPROVAL_TWO: `${ApiEndpointService.STL_CONTEXT}/decisionOnSiteReportLevelTwo`,
        STD_LEVY_SAVE_VISIT_REPORT_FEEDBACK: `${ApiEndpointService.STL_CONTEXT}/siteVisitReportFeedback`,
        STD_LEVY_SAVE_VISIT_REPORT_VIEW_FEEDBACK: `${ApiEndpointService.STL_CONTEXT}/getSiteFeedback`,
        STD_LEVY_MANUFACTURE_LIST: `${ApiEndpointService.STL_CONTEXT}/getManufacturerList`,
        STD_LEVY_DIRECTORS_LIST: `${ApiEndpointService.STL_CONTEXT}/getCompanyDirectors`,
        STD_LEVY_MANUFACTURE_TASKS: `${ApiEndpointService.STL_CONTEXT}/getUserTasks`,
        STD_LEVY_VIEW_VISIT_REPORT_FEEDBACK: `${ApiEndpointService.STL_CONTEXT}/viewFeedBack`,
        STD_LEVY_MANUFACTURE_COMPLETE_TASKS: `${ApiEndpointService.STL_CONTEXT}/getCompleteTasks`,
        STD_LEVY_LIST_OF_USERS: `${ApiEndpointService.STL_CONTEXT}/getSlUsers`,
        STD_LEVY_APPROVED_USERS_LEVEL_ONE: `${ApiEndpointService.STL_CONTEXT}/getApproveLevelOne`,
        STD_LEVY_APPROVED_USERS_LEVEL_TWO: `${ApiEndpointService.STL_CONTEXT}/getApproveLevelTwo`,
        STD_LEVY_APPROVED_USERS_LEVEL_THREE: `${ApiEndpointService.STL_CONTEXT}/getApproveLevelThree`,
        STD_LEVY_ASSIGN_USERS_LEVEL_ONE: `${ApiEndpointService.STL_CONTEXT}/getAssignLevelOne`,
        STD_LEVY_ASSIGN_USERS_LEVEL_TWO: `${ApiEndpointService.STL_CONTEXT}/getAssignLevelTwo`,
        STD_LEVY_ASSIGN_USERS_LEVEL_THREE: `${ApiEndpointService.STL_CONTEXT}/getAssignLevelThree`,
        STD_LEVY_MANUFACTURE_USERS: `${ApiEndpointService.STL_CONTEXT}/getSlLoggedIn`,
        STD_LEVY_LIST_OF_USERS_PL: `${ApiEndpointService.STL_CONTEXT}/getPlList`,
        STD_LEVY_LIST_OF_USERS_LV: `${ApiEndpointService.STL_CONTEXT}/getSlLvTwoList`,
        STD_LEVY_MANUFACTURE_ROLES: `${ApiEndpointService.STL_CONTEXT}/getRoleByUserId`,
        STD_LEVY_EDIT_COMPANY: `${ApiEndpointService.STL_CONTEXT}/editCompanyDetails`,
        STD_LEVY_EDIT_COMPANY_LEVEL_ONE: `${ApiEndpointService.STL_CONTEXT}/editCompanyDetailsConfirmLvlOne`,
        STD_LEVY_EDIT_COMPANY_LEVEL_TWO: `${ApiEndpointService.STL_CONTEXT}/editCompanyDetailsConfirmLvlTwo`,
        STD_LEVY_EDITED_COMPANY_DATA: `${ApiEndpointService.STL_CONTEXT}/getCompanyEditedDetails`,
        STD_LEVY_SITE_VISIT_REMARKS: `${ApiEndpointService.STL_CONTEXT}/getSiteVisitRemarks`,
        STD_LEVY_COMPANY_REMARKS: `${ApiEndpointService.STL_CONTEXT}/getComEditRemarks`,
        STD_LEVY_COMPANY_EDIT_COMPANY_DATA: `${ApiEndpointService.STL_CONTEXT}/editCompanyDetailsConfirm`,
        STD_LEVY_NOTIFICATION_FORM_STATUS: `${ApiEndpointService.STL_CONTEXT}/getSLNotificationStatus`,
        STD_LEVY_NOTIFICATION_SL_STATUS: `${ApiEndpointService.STL_CONTEXT}/getSlForm`,
        STD_LEVY_BRANCH_NAME: `${ApiEndpointService.STL_CONTEXT}/getBranchName`,
        STD_LEVY_SUSPEND_OPERATIONS: `${ApiEndpointService.STL_CONTEXT}/suspendCompanyOperations`,
        STD_LEVY_RESUME_OPERATIONS: `${ApiEndpointService.STL_CONTEXT}/resumeCompanyOperations`,
        STD_LEVY_VIEW_SUSPENDED_OPERATIONS: `${ApiEndpointService.STL_CONTEXT}/getCompanySuspensionRequest`,
        STD_LEVY_CLOSE_OPERATIONS: `${ApiEndpointService.STL_CONTEXT}/closeCompanyOperations`,
        STD_LEVY_VIEW_CLOSED_OPERATIONS: `${ApiEndpointService.STL_CONTEXT}/getCompanyClosureRequest`,
        STD_LEVY_UPLOAD_WINDING_UP_REPORT: `${ApiEndpointService.STL_CONTEXT}/uploadWindingUpReport`,
        STD_LEVY_GET_WINDING_UP_REPORT_LIST: `${ApiEndpointService.STL_CONTEXT}/getWindingReportDocumentList`,
        STD_LEVY_VIEW_WINDING_UP_REPORT: `${ApiEndpointService.STL_CONTEXT}/view/windingUpReport`,
        STD_LEVY_APPROVE_SUSPENSION: `${ApiEndpointService.STL_CONTEXT}/confirmCompanySuspension`,
        STD_LEVY_APPROVE_RESUMPTION: `${ApiEndpointService.STL_CONTEXT}/confirmCompanyResumption`,
        STD_LEVY_REJECT_SUSPENSION: `${ApiEndpointService.STL_CONTEXT}/rejectCompanySuspension`,
        STD_LEVY_REJECT_RESUMPTION: `${ApiEndpointService.STL_CONTEXT}/rejectCompanyResumption`,
        STD_LEVY_APPROVE_CLOSURE: `${ApiEndpointService.STL_CONTEXT}/confirmCompanyClosure`,
        STD_LEVY_REJECT_CLOSURE: `${ApiEndpointService.STL_CONTEXT}/rejectCompanyClosure`,
        USER_EMAIL_VERIFICATION_STATUS: `${ApiEndpointService.STL_CONTEXT}/getVerificationStatus`,
        SEND_EMAIL_VERIFICATION: `${ApiEndpointService.STL_CONTEXT}/sendEmailVerificationToken`,
        CONFIRM_EMAIL_VERIFICATION_STATUS: `${ApiEndpointService.STL_CONTEXT}/confirmEmailAddress`,
        STD_LEVY_COMPANY_STATUS: `${ApiEndpointService.STL_CONTEXT}/getOperationStatus`,
        STD_LEVY_E_SLIP: `${ApiEndpointService.STL_CONTEXT}/generatePdf`,
        STD_LEVY_VIEW_E_SLIP: `${ApiEndpointService.STL_CONTEXT}/levyPaymentESlip`,
        STD_LEVY_REG_FIRMS: `${ApiEndpointService.STL_CONTEXT}/getRegisteredFirms`,
        STD_LEVY_ALL_LEVY_PAYMENTS: `${ApiEndpointService.STL_CONTEXT}/getAllLevyPayments`,
        STD_LEVY_PEN_REPORT: `${ApiEndpointService.STL_CONTEXT}/getPenaltyReport`,
        STD_LEVY_ACTIVE_FIRMS: `${ApiEndpointService.STL_CONTEXT}/getActiveFirms`,
        STD_LEVY_DORMANT_FIRMS: `${ApiEndpointService.STL_CONTEXT}/getDormantFirms`,
        STD_LEVY_CLOSED_FIRMS: `${ApiEndpointService.STL_CONTEXT}/getClosedFirms`,
        STD_LEVY_REG_FIRMS_FILTER: `${ApiEndpointService.STL_CONTEXT}/getRegisteredFirmsFilter`,
        STD_LEVY_ALL_LEVY_PAYMENTS_FILTER: `${ApiEndpointService.STL_CONTEXT}/getAllLevyPaymentsFilter`,
        STD_LEVY_PEN_REPORT_FILTER: `${ApiEndpointService.STL_CONTEXT}/getPenaltyReportFilter`,
        STD_LEVY_ACTIVE_FIRMS_FILTER: `${ApiEndpointService.STL_CONTEXT}/getActiveFirmsFilter`,
        STD_LEVY_DORMANT_FIRMS_FILTER: `${ApiEndpointService.STL_CONTEXT}/getDormantFirmsFilter`,
        STD_LEVY_CLOSED_FIRMS_FILTER: `${ApiEndpointService.STL_CONTEXT}/getClosedFirmsFilter`,
        STD_LEVY_REJECTED_FIRM_EDITS: `${ApiEndpointService.STL_CONTEXT}/getRejectedCompanyDetails`,
        STD_LEVY_VIEW_REG_FIRMS_REPORT: `${ApiEndpointService.STL_CONTEXT}/levyRegisteredFirmsReport`,
        STD_LEVY_VIEW_ACTIVE_FIRMS_REPORT: `${ApiEndpointService.STL_CONTEXT}/levyActiveFirmsReport`,
        STD_LEVY_VIEW_DORMANT_FIRMS_REPORT: `${ApiEndpointService.STL_CONTEXT}/levyDormantFirmsReport`,
        STD_LEVY_VIEW_CLOSED_FIRMS_REPORT: `${ApiEndpointService.STL_CONTEXT}/levyClosedFirmsReport`,
        STD_LEVY_VIEW_PENALTY_FIRMS_REPORT: `${ApiEndpointService.STL_CONTEXT}/levyPenaltyReport`,
        STD_LEVY_VIEW_PAYMENT_FIRMS_REPORT: `${ApiEndpointService.STL_CONTEXT}/levyPaymentReport`,
        STD_LEVY_BUSINESS_LINE_LIST: `${ApiEndpointService.STL_CONTEXT}/getBusinessLineList`,
        STD_LEVY_REGION_LIST: `${ApiEndpointService.STL_CONTEXT}/getRegionList`,
        STD_LEVY_HISTORICAL_PAYMENTS: `${ApiEndpointService.STL_CONTEXT}/getLevyHistoricalPayments`,
        STD_LEVY_HISTORICAL_PAYMENTS_FILTER: `${ApiEndpointService.STL_CONTEXT}/getLevyHistoricalPaymentsFilter`,
        STD_LEVY_HISTORICAL_PAYMENTS_STATUS: `${ApiEndpointService.STL_CONTEXT}/getLevyPaymentStatus`,


    };

    public static QA_MANUFACTURE_ENDPOINT = {
        GENERATE_INVOICE_DIFFERENCE: `${ApiEndpointService.QA_CONTEXT}/permit/apply/generate-difference-invoice`,
        RE_GENERATE_INVOICE: `${ApiEndpointService.QA_CONTEXT}/permit/apply/re-generate-invoice`,
        GET_STANDARD_LIST: `${ApiEndpointService.QA_CONTEXT}/standards-list`,
    };

    public static QA_INTERNAL_USER_ENDPOINT = {
        LOAD_ONGOING_LIST: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/view/permits-list-ongoing`,
        LOAD_COMPLETE_LIST: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/view/permits-list-complete`,
        LOAD_ALL_PERMIT_LIST: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/view/permits-list-all`,
        LOAD_MY_TASK_LIST: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/view/permits-list`,
        LOAD_PERMIT_DETAIL: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/view/permit-detail`,
        UPDATE_SECTION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/section`,
        RESUBMIT_APPLICATION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/resubmit-details`,
        UPDATE_BRAND: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/update-brand`,
        UPDATE_OFFICER_DIFFERENCE_STATUS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/difference-status-activate`,
        QAM_COMPLETENESS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/completeness`,
        ASSIGN_OFFICER: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/assign-officer`,
        RECOMMENDATION_APPROVAL: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/approve-reject-recommendation`,
        ADD_STANDARD: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/add-standards`,
        ADD_RECOMMENDATION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/save-recommendation`,
        SCHEDULE_INSPECTION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/schedule-inspection`,
        APPROVE_REJECT_INSPECTION_REPORT: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/approve-reject-inspection-report`,
        APPROVE_REJECT_PERMIT_QAM_HOD: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/qam-approve-reject-permit`,
        APPROVE_REJECT_PERMIT_PSC_MEMBER: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/psc-approve-reject-permit`,
        APPROVE_REJECT_PERMIT_PCM: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/pcm-approve-reject-permit`,

        CHECK_IF_INSPECTION_REPORT_EXISTS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/check_if_inspection_report_exists`,
        GET_FULLY_FILLED_INSPECTION_REPORT: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/getFullyFilledInspectionReport`,

        NEW_INSPECTION_TECHNICAL_REPORT: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/new-technical-report`,
        TECHNICAL_REPORT_DETAILS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/inspection-details`,
        TECHNICAL_REPORT_DETAILS_B: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/inspection-detailsB`,
        PRODUCT_LABELLING: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/product-labelling`,
        STANDARDIZATION_MARK: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/standardization-mark-scheme`,
        OPERATION_PROCESS_CONTROLS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/operationProcessAndControls`,
        HACCP_IMPLEMENTATION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/haccpImplementationDetails`,
        RECOMMENDATION_SAVE: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/recommendation_save`,

        FINAL_INSPECTION_REPORT_SUBMISSION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/inspection-checklist-submit`,
        GET_INSPECTION_REPORT: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/inspection/getInspectionReport`,







        ADD_SSF_DETAILS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/ssf-details`,
        ADD_SSF_COMPLIANCE_DETAILS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/ssf-compliance-status`,
        VIEW_PDF_LAB_RESULT: `${ApiEndpointService.QA_CONTEXT}/view/attached-lab-pdf`,
        SAVE_PDF_LAB_RESULT: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/lab-save-pdf-selected`,
        SAVE_PDF_LAB_RESULT_COMPLIANCE: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/lab-save-compliance-status`,
        UPLOAD_SCHEME_OF_SUPERVISION: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/upload-scheme-supervision`,
        UPLOAD_INSPECTION_REPORT: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/uploadInspectionReport`,
        UPLOAD_ATTACHMENTS: `${ApiEndpointService.QA_INTERNAL_USER_CONTEXT}/apply/permit/upload-docs`,

    };

    public static COMPANY_PROFILE_ENDPOINT = {
        LOAD_BUSINESS_LINES: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/businessLines/load`,
        LOAD_BUSINESS_NATURES: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/businessNatures/load`,
        LOAD_COMPANY_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/company-list/load`,
        LOAD_FIRM_TYPE_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/firm-types/load`,
        UPDATE_COMPANY_TURN_OVER: `${ApiEndpointService.QA_CONTEXT}/company/update-turn-over`,
        UPDATE_COMPANY_TURN_OVER_REQUEST_OFFICER: `${ApiEndpointService.QA_CONTEXT}/company/officer-requested-to-upgrade`,
        GENERATE_INSPECTION_FEES_INVOICE: `${ApiEndpointService.QA_CONTEXT}/company/generate-inspection-fee`,
        UPLOAD_INSPECTION_FEES_INVOICE: `${ApiEndpointService.QA_CONTEXT}/upload/inspection-invoice`,
        VIEW_PDF_INSPECTION_FEES_INVOICE: `${ApiEndpointService.QA_CONTEXT}/view/inspection-invoice`,
        TIVETS_LIST: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/company-list/tivetListing`,
        TIVETS_UPDATE: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/company-list/updateTivet`,
        TIVETS_REJECT: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/company-list/rejectTivet`,

    };

    public static MARKET_SURVEILLANCE_PDF_ENDPOINT = {
        VIEW_PDF_COMPLAINT: `${ApiEndpointService.MS_CONTEXT}/report/complaint`,
        VIEW_PDF_SSF: `${ApiEndpointService.MS_CONTEXT}/report/sample-submission`,
        VIEW_PDF_FIELD_REPORT: `${ApiEndpointService.MS_CONTEXT}/report/ms-field-report`,
    };

    public static MARKET_SURVEILLANCE_FUEL_ENDPOINT = {
        VIEW_PDF_LAB_RESULT: `${ApiEndpointService.MS_CONTEXT}/view/attached-lab-pdf`,
        VIEW_PDF_SAVED: `${ApiEndpointService.MS_CONTEXT}/view/attached`,
        VIEW_PDF_SAMPLE_COLLECTION: `${ApiEndpointService.MS_CONTEXT}/report/sample-collection`,
        VIEW_PDF_REMEDIATION_INVOICE: `${ApiEndpointService.MS_CONTEXT}/report/remediation-invoice`,
        LAB_LIST: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/fetch/laboratory-list`,
        ALL_BATCH_LIST: `${ApiEndpointService.MS_FUEL_CONTEXT}/all-batch-list`,
        ADD_BATCH: `${ApiEndpointService.MS_FUEL_CONTEXT}/add`,
        CLOSE_BATCH: `${ApiEndpointService.MS_FUEL_CONTEXT}/close`,
        UPLOAD_FUEL_FILE: `${ApiEndpointService.MS_CONTEXT}/fuel/file/save`,
        INSPECTION_SCHEDULED_LIST: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/list`,
        INSPECTION_ADD_TEAMS: `${ApiEndpointService.MS_FUEL_CONTEXT}/teams/add`,
        INSPECTION_TEAMS_LIST: `${ApiEndpointService.MS_FUEL_CONTEXT}/teams/list`,
        INSPECTION_TEAM_COUNTY_LIST: `${ApiEndpointService.MS_FUEL_CONTEXT}/teams/county/list`,
        INSPECTION_SCHEDULED_ADD_NEW: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/add`,
        INSPECTION_SCHEDULED_DETAILS: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/details`,
        INSPECTION_SCHEDULED_DETAILS_ASSIGN_OFFICER: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/assign`,
        INSPECTION_SCHEDULED_DETAILS_RAPID_TEST: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/rapid-test`,
        INSPECTION_SCHEDULED_DETAILS_RAPID_TEST_PRODUCTS: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/rapid-test-products`,
        INSPECTION_SCHEDULED_DETAILS_SAMPLE_COLLECT: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/sample-collect`,
        INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/sample-submission`,
        INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION_BS_NUMBER: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/sample-submission-bs-number`,
        INSPECTION_SCHEDULED_DETAILS_LAB_RESULTS_SAVE_PDF: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/lab-results-pdf-save`,
        INSPECTION_SCHEDULED_DETAILS_SSF_COMPLIANCE_STATUS: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/ssf-compliance-status-save`,
        INSPECTION_SCHEDULED_DETAILS_SSF_FINAL_COMPLIANCE_STATUS: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/ssf-compliance-final-status-save`,
        INSPECTION_SCHEDULED_REMEDIATION_DATE: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/fuel-remediation-schedule`,
        INSPECTION_SCHEDULED_REMEDIATION_INVOICE: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/fuel-remediation-invoice`,
        INSPECTION_SCHEDULED_ADD_REMEDIATION: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/fuel-remediation`,
        END_INSPECTION: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/end-inspection`,
        END_SSF_ADDING_INSPECTION: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/end-ssf-adding`,
        END_SSF_ADDING_BS_NUMBER_INSPECTION: `${ApiEndpointService.MS_FUEL_CONTEXT}/inspection/update/end-ssf-adding-bs-number`,
        // COUNTRY_DASHBOARD_DETAILS: `${ApiEndpointService.VERSION_THREE_CONTEXT}/country-dashboard-details`,
        // CLIENT_LIST: `${ApiEndpointService.VERSION_THREE_CONTEXT}/client-list`,
    };

    public static MARKET_SURVEILLANCE_COMMON = {
        MS_OFFICER_LIST: `${ApiEndpointService.MS_COMMON_CONTEXT}/officer-list`,
        MS_DASHBOARD: `${ApiEndpointService.MS_COMMON_CONTEXT}/dashboard`,
        MS_SEARCH_PERMIT_NUMBER: `${ApiEndpointService.MS_COMMON_CONTEXT}/search-permit-number`,
        MS_SEARCH_UCR_NUMBER: `${ApiEndpointService.MS_COMMON_CONTEXT}/search-ucr-number`,
        MS_TOWNS: `${ApiEndpointService.MS_COMMON_CONTEXT}/towns`,
        MS_COUNTIES: `${ApiEndpointService.MS_COMMON_CONTEXT}/counties`,
        MS_REGIONS: `${ApiEndpointService.SYSTEMS_ADMIN_CONTEXT}/regions/load`,
        MS_COUNTRIES: `${ApiEndpointService.MS_COMMON_CONTEXT}/countries`,
        MS_DEPARTMENTS: `${ApiEndpointService.MS_COMMON_CONTEXT}/departments`,
        MS_LABORATORIES: `${ApiEndpointService.MS_COMMON_CONTEXT}/laboratories`,
        MS_STANDARDS: `${ApiEndpointService.MS_COMMON_CONTEXT}/standards`,
        MS_RECOMMENDATION: `${ApiEndpointService.MS_COMMON_CONTEXT}/recommendation-list`,
        MS_DIVISIONS: `${ApiEndpointService.MS_COMMON_CONTEXT}/divisions`,
        MS_STANDARD_PRODUCT_CATEGORY: `${ApiEndpointService.MS_COMMON_CONTEXT}/standardProductCategory`,
        MS_PRODUCT_CATEGORIES: `${ApiEndpointService.MS_COMMON_CONTEXT}/productCategories`,
        MS_BROAD_PRODUCT_CATEGORY: `${ApiEndpointService.MS_COMMON_CONTEXT}/broadProductCategory`,
        MS_PREDEFINED_RESOURCES_REQUIRED: `${ApiEndpointService.MS_COMMON_CONTEXT}/predefinedResourcesRequired`,
        MS_OGA_REQUIRED: `${ApiEndpointService.MS_COMMON_CONTEXT}/ogaList`,
        MS_PRODUCTS: `${ApiEndpointService.MS_COMMON_CONTEXT}/products`,
        MS_PRODUCT_SUB_CATEGORY: `${ApiEndpointService.MS_COMMON_CONTEXT}/productSubcategory`,
        MS_NOTIFICATIONS: `${ApiEndpointService.MS_COMMON_CONTEXT}/notification-list`,
        MS_NOTIFICATIONS_READ: `${ApiEndpointService.MS_COMMON_CONTEXT}/notification-read`,
    };

    public static MARKET_SURVEILLANCE_REPORTS = {
        MS_CONSUMER_COMPLAINT_VIEW: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/consumer-complaint/view-all`,
        MS_CONSUMER_COMPLAINT_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/consumer-complaint/search`,

        MS_SUBMITTED_SAMPLES_SUMMARY_VIEW: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/submitted-samples-summary/view-all`,
        MS_SUBMITTED_SAMPLES_SUMMARY_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/submitted-samples-summary/search`,

        MS_FIELD_INSPECTION_SUMMARY_VIEW: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/field-inspection-summary/view-all`,
        MS_FIELD_INSPECTION_SUMMARY_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/field-inspection-summary/search`,

        MS_WORK_PLAN_MONITORING_TOOL_VIEW: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/work-plan-monitoring-tool/view-all`,
        MS_WORK_PLAN_MONITORING_TOOL_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/work-plan-monitoring-tool/search`,

        MS_SEIZED_GOODS_VIEW: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/seized-goods/view-all`,
        MS_SEIZED_GOODS_VIEW_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/seized-goods/search`,

        MS_COMPLAINT_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/complaint-search`,
        MS_SAMPLE_PRODUCTS_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/sample-products-search`,
        MS_SEIZED_GOODS_SEARCH: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/seized-goods-search`,
        MS_TIMELINE_COMPLAINT: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/timeline/complaint`,
        MS_STATUS_REPORT_SAMPLE_PRODUCTS: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/statusReport/performance-selected-product`,
        MS_SEIZED_GOODS: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/seized-goods`,

        MS_TIMELINE_FEEDBACK: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/timeline/feedback`,
        MS_TIMELINE_REPORT_SUBMITTED: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/timeline/reportSubmitted`,
        MS_TIMELINE_SAMPLE_SUBMITTED: `${ApiEndpointService.MS_COMPLAINT_REPORT_CONTEXT}/timeline/sampleSubmitted`,

    };

    public static MARKET_SURVEILLANCE_COMPLAINT = {
        CREATE_NEW_WORK_PLAN_SCHEDULE: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/add/complaint-work-plan`,
        CREATE_NEW_COMPLAINT: `${ApiEndpointService.ANONYMOUS_CONTEXT}/complaint/new`,
        UPLOAD_COMPLIANT_FILE: `${ApiEndpointService.ANONYMOUS_CONTEXT}/complaint/file/save`,
        ALL_COMPLAINT_LIST: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/list`,
        COMPLETED_COMPLAINT_LIST: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/list-completed`,
        NEW_COMPLAINT_LIST: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/list-new`,
        MY_TASK_COMPLAINT_LIST: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/list-my-task`,
        PENDING_ALLOCATION_TASK_VIEW: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/pending-allocation-view`,
        ALLOCATED_TASK_VIEW: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/allocated-task-view`,
        ALLOCATED_OVER_DUE_TASK_VIEW: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/allocated-task-overDue-view`,
        ONGOING_COMPLAINT_LIST: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/list-on-going`,
        COMPLAINT_DETAILS: `${ApiEndpointService.MS_COMPLAINT_CONTEXT}/details`,
        COMPLAINT_DETAILS_UPDATE_ACCEPTANCE: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/accept`,
        COMPLAINT_DETAILS_UPDATE_REJECTION: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/reject`,
        COMPLAINT_DETAILS_UPDATE_OGA_MANDATE: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/advice-where`,
        COMPLAINT_DETAILS_UPDATE_REJECT_FOR_AMENDMENT: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/reject-for-amendment`,
        COMPLAINT_DETAILS_UPDATE_ASSIGN_HOF: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/assign-hof`,
        COMPLAINT_DETAILS_UPDATE_RE_ASSIGN_REGION: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/re-assign-region`,
        COMPLAINT_DETAILS_UPDATE_ASSIGN_IO: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/assign-io`,
        COMPLAINT_DETAILS_ADD_CLASSIFICATION_DETAILS: `${ApiEndpointService.MS_COMPLAINT_UPDATE_CONTEXT}/add-classification-details`,
        VIEW_PDF_SAVED: `${ApiEndpointService.MS_CONTEXT}/view/attached`,
        CLOSE_BATCH: `${ApiEndpointService.MS_FUEL_CONTEXT}/close`,
    };

    public static MARKET_SURVEILLANCE_WORK_PLAN = {
        REPORTS_PENDING_REVIEW_WP_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/report-pending-review-wp-view`,
        REPORTS_PENDING_REVIEW_WP_CP_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/report-pending-review-wp-cp-view`,
        JUNIOR_TASK_OVER_DUE_WP_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/junior-task-overDue-wp-view`,
        JUNIOR_TASK_OVER_DUE_WP_CP_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/junior-task-overDue-wp-cp-view`,
        ALLOCATED_WP_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/allocated-task-wp-view`,
        ALLOCATED_WP_CP_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/allocated-task-wp-cp-view`,
        ALLOCATED_WP_OVER_DUE_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/allocated-task-overDue-wp-view`,
        ALLOCATED_WP_CP_OVER_DUE_TASK_VIEW: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/allocated-task-overDue-wp-cp-view`,
        ALL_BATCH_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/all-batch-list`,
        OPEN_BATCH_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/all-batch-open`,
        CLOSE_BATCH_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/all-batch-closed`,
        // REASSIGNED_COMPLAINTS: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/reAssignedComplaints`,
        ADD_NEW_BATCH: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/add`,
        CLOSE_BATCH: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/close`,
        ALL_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list`,
        COMPLETED_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-completed`,
        NEW_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-new`,
        MY_TASK_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-my-task`,
        ONGOING_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-on-going`,
        CREATE_NEW_WORK_PLAN_SCHEDULE: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/new`,
        UPDATE_NEW_WORK_PLAN_SCHEDULE: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/update`,
        INSPECTION_SCHEDULED_DETAILS: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/details`,
        INSPECTION_SCHEDULED_APPROVE_DETAILS: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/approval-schedule`,
        INSPECTION_SCHEDULED_CLIENT_APPEALED: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/client-appealed-status`,
        INSPECTION_SCHEDULED_CLIENT_APPEALED_SUCCESSFULLY: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/client-appealed-successfully`,
        INSPECTION_SCHEDULED_APPROVE_HOF_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hof/approval-preliminary-report`,
        INSPECTION_SCHEDULED_APPROVE_HOF_FINAL_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hof/approval-final-preliminary-report`,
        INSPECTION_SCHEDULED_UPDATE_ASSIGN_IO: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hof/assign-io`,
        INSPECTION_SCHEDULED_APPROVE_HOD_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/approval-preliminary-report`,
        INSPECTION_SCHEDULED_UPDATE_ASSIGN_HOF: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/assign-hof`,
        INSPECTION_SCHEDULED_APPROVE_HOD_FINAL_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/approval-final-preliminary-report`,
        INSPECTION_SCHEDULED_APPROVE_DIRECTOR_FINAL_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/director/approval-final-preliminary-report`,
        INSPECTION_SCHEDULED_APPROVE_HOD_FINAL_REMARKS: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/feedBack-notification`,
        INSPECTION_SCHEDULED_ADD_DIRECTOR_FINAL_REMARKS_RECOMMENDATION: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/director/recommendation`,
        INSPECTION_SCHEDULED_HOD_ADD_FINAL_RECOMMENDATION: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/final-recommendation`,
        INSPECTION_SCHEDULED_HOD_END_ADD_FINAL_RECOMMENDATION: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/end-adding-final-recommendation`,
        INSPECTION_SCHEDULED_DIRECTOR_END_ADD_FINAL_REMARKS_RECOMMENDATION: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/director/end-recommendation`,
        INSPECTION_SCHEDULED_START_ONSITE_ACTIVITIES: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/start-onsite-activities`,
        INSPECTION_SCHEDULED_SUBMIT_FOR_APPROVAL: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/submit-for-approval`,
        INSPECTION_SCHEDULED_END_ONSITE_ACTIVITIES: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/end-onsite-activities`,
        INSPECTION_SCHEDULED_END_RECOMMENDATION_DONE: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/end-all-recommendation-done`,
        INSPECTION_SCHEDULED_ADD_DESTRUCTION_NOTIFICATION_UPLOAD: `${ApiEndpointService.MS_CONTEXT}/update/destruction-notice-upload`,
        INSPECTION_SCHEDULED_ADD_DESTRUCTION_REPORT_UPLOAD: `${ApiEndpointService.MS_CONTEXT}/update/destruction-report-upload`,
        UPLOAD_WORK_PLAN_FILE: `${ApiEndpointService.MS_CONTEXT}/work-plan/file/save`,
        UPLOAD_FINAL_REPORT_WORK_PLAN_FILE: `${ApiEndpointService.MS_CONTEXT}/update/upload-final-report`,
        UPLOAD_FINAL_REPORT_HOF_HOD_WORK_PLAN_FILE: `${ApiEndpointService.MS_CONTEXT}/update/upload-final-report-hod-hof-director`,
        UPDATE_FIELD_REPORT_WORK_PLAN_FILE: `${ApiEndpointService.MS_CONTEXT}/work-plan/additional-info-field-report/save`,
        UPDATE_FINAL_COMPLAINT_REMARKS_WORK_PLAN_FILE: `${ApiEndpointService.MS_CONTEXT}/work-plan/final-feed-back/save`,
        INSPECTION_SCHEDULED_ADD_CHARGE_SHEET: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/charge-sheet`,
        INSPECTION_SCHEDULED_ADD_DATA_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/data-report`,
        INSPECTION_SCHEDULED_END_ADDING_DATA_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/end-data-report`,
        INSPECTION_SCHEDULED_ADD_SEIZURE_DECLARATION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/seizure-declaration`,
        INSPECTION_SCHEDULED_END_SEIZURE_DECLARATION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/end-seizure-declaration`,
        INSPECTION_SCHEDULED_ADD_INSPECTION_INVESTIGATION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/inspection-investigation`,
        INSPECTION_SCHEDULED_ADD_SAMPLE_COLLECT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/sample-collect`,
        INSPECTION_SCHEDULED_ADD_SAMPLE_SUBMISSION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/sample-submission`,
        INSPECTION_SCHEDULED_END_SAMPLE_SUBMISSION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/end-sample-submission`,
        INSPECTION_SCHEDULED_ADD_SAMPLE_SUBMISSION_BS_NUMBER: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/sample-submission-bs-number`,
        INSPECTION_SCHEDULED_END_SAMPLE_SUBMISSION_BS_NUMBER: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/end-sample-submission-bs-number`,
        INSPECTION_SCHEDULED_ADD_LAB_RESULTS_PDF_SAVE: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/lab-results-pdf-save`,
        INSPECTION_SCHEDULED_ADD_SSF_COMPLIANCE_STATUS_SAVE: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/ssf-compliance-status-save`,
        INSPECTION_SCHEDULED_SEND_SSF_COMPLIANCE_STATUS_SAVE: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/ssf-send-result-saved`,
        INSPECTION_SCHEDULED_ADD_FINAL_SSF_COMPLIANCE_STATUS_SAVE: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/final-ssf-compliance-status-save`,
        INSPECTION_SCHEDULED_ADD_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/preliminary-report`,
        INSPECTION_SCHEDULED_UPDATE_HOF_HOD_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/preliminary-report-hod-hof-director`,
        INSPECTION_SCHEDULED_ADD_FINAL_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/final-report`,

    };

    public static MARKET_SURVEILLANCE_COMPLAINT_PLAN = {
        ALL_BATCH_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/all-batch-list`,
        OPEN_BATCH_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/all-batch-open`,
        CLOSE_BATCH_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/all-batch-closed`,
        ADD_NEW_BATCH: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/add`,
        CLOSE_BATCH: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/close`,
        ALL_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list`,
        COMPLETED_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-completed`,
        NEW_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-new`,
        MY_TASK_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-my-task`,
        ONGOING_WORK_PLAN_LIST: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/list-on-going`,
        CREATE_NEW_WORK_PLAN_SCHEDULE: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/new`,
        UPDATE_NEW_WORK_PLAN_SCHEDULE: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/update`,
        INSPECTION_SCHEDULED_DETAILS: `${ApiEndpointService.MS_WORK_PLAN_CONTEXT}/inspection/details`,
        INSPECTION_SCHEDULED_APPROVE_DETAILS: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/approval-schedule`,
        INSPECTION_SCHEDULED_CLIENT_APPEALED: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/client-appealed-status`,
        INSPECTION_SCHEDULED_CLIENT_APPEALED_SUCCESSFULLY: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/client-appealed-successfully`,
        INSPECTION_SCHEDULED_APPROVE_HOF_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hof/approval-preliminary-report`,
        INSPECTION_SCHEDULED_APPROVE_HOF_FINAL_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hof/approval-final-preliminary-report`,
        INSPECTION_SCHEDULED_UPDATE_ASSIGN_IO: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hof/assign-io`,
        INSPECTION_SCHEDULED_APPROVE_HOD_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/approval-preliminary-report`,
        INSPECTION_SCHEDULED_UPDATE_ASSIGN_HOF: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/assign-hof`,
        INSPECTION_SCHEDULED_APPROVE_HOD_FINAL_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/approval-final-preliminary-report`,
        INSPECTION_SCHEDULED_APPROVE_HOD_FINAL_REMARKS: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/feedBack-notification`,
        INSPECTION_SCHEDULED_HOD_ADD_FINAL_RECOMMENDATION: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/hod/final-recommendation`,
        INSPECTION_SCHEDULED_START_ONSITE_ACTIVITIES: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/start-onsite-activities`,
        INSPECTION_SCHEDULED_SUBMIT_FOR_APPROVAL: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/submit-for-approval`,
        INSPECTION_SCHEDULED_END_ONSITE_ACTIVITIES: `${ApiEndpointService.MS_WORK_PLAN_UPDATE_CONTEXT}/end-onsite-activities`,
        INSPECTION_SCHEDULED_ADD_DESTRUCTION_NOTIFICATION_UPLOAD: `${ApiEndpointService.MS_CONTEXT}/update/destruction-notice-upload`,
        INSPECTION_SCHEDULED_ADD_DESTRUCTION_REPORT_UPLOAD: `${ApiEndpointService.MS_CONTEXT}/update/destruction-report-upload`,
        UPLOAD_WORK_PLAN_FILE: `${ApiEndpointService.MS_CONTEXT}/work-plan/file/save`,
        INSPECTION_SCHEDULED_ADD_CHARGE_SHEET: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/charge-sheet`,
        INSPECTION_SCHEDULED_ADD_DATA_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/data-report`,
        INSPECTION_SCHEDULED_ADD_SEIZURE_DECLARATION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/seizure-declaration`,
        INSPECTION_SCHEDULED_ADD_INSPECTION_INVESTIGATION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/inspection-investigation`,
        INSPECTION_SCHEDULED_ADD_SAMPLE_COLLECT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/sample-collect`,
        INSPECTION_SCHEDULED_ADD_SAMPLE_SUBMISSION: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/sample-submission`,
        INSPECTION_SCHEDULED_ADD_SAMPLE_SUBMISSION_BS_NUMBER: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/sample-submission-bs-number`,
        INSPECTION_SCHEDULED_ADD_LAB_RESULTS_PDF_SAVE: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/lab-results-pdf-save`,
        INSPECTION_SCHEDULED_ADD_SSF_COMPLIANCE_STATUS_SAVE: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/ssf-compliance-status-save`,
        INSPECTION_SCHEDULED_ADD_PRELIMINARY_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/preliminary-report`,
        INSPECTION_SCHEDULED_ADD_FINAL_REPORT: `${ApiEndpointService.MS_WORK_PLAN_ADD_CONTEXT}/final-report`,

    };

    /**
     * Constructor.
     */
    constructor(
        // private http: HttpClient
    ) {
    }

    /**
     * Constructs an API endpoint.
     *
     * NOTE: In the future this could construct API endpoints using environmental configs provided
     * at build time or at runtime via (for example) query string params...but for now we'll
     * keep this dumb simple.
     */
    public static getEndpoint(endpoint: string): string {

        const protocol: string = environment.https_enabled ? ApiEndpointService.PROTOCOL.HTTPS : ApiEndpointService.PROTOCOL.HTTP;
        const domain: string = ApiEndpointService.DOMAIN.LOCAL_DEV;
        const context: string = ApiEndpointService.CONTEXT;
        return `${protocol}${domain}${context}${endpoint}`;
    }

    /**
     * Determines if the requested URL is an authentication API endpoint.
     * @param  url the url
     * @returns it requires authentication
     * @returns it requires authentication
     */
    public static isAuthEndpoint(url: string = ''): boolean {
        return (
            url.toLowerCase().indexOf(ApiEndpointService.ANONYMOUS_CONTEXT) > -1
            || url.toLowerCase().indexOf(ApiEndpointService.AUTH_CONTEXT) > -1
        );

    }

    /**
     * Determines if the requested URL is an API endpoint.
     * @param url the url
     * @returns this is an apiEndPoint
     */
    public static isApiEndpoint(url: string = ''): boolean {
        return url.toLowerCase().indexOf(ApiEndpointService.CONTEXT) > -1;
    }
}
