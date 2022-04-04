import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ApiEndpointService {

    /**
     * Map of protocols for API endpoints.
     */
    public static PROTOCOL = {
        HTTP: `http://`,
        HTTPS: `https://`
    };

    /**
     * Map of domains for API endpoints.
     */
    public static DOMAIN = {
         // LOCAL_DEV: 'localhost:8006'
        // LOCAL_DEV: '12:8006'
        // LOCAL_DEV: '41.72.209.58:8006'
        LOCAL_DEV: `kimsint.kebs.org:8006`
        // LOCAL_DEV: `kims.kebs.org`
        // LOCAL_DEV: '10.10.0.149:8007'
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

    /**
     * Map of contexts for API endpoints.
     */
    public static AUTH_CONTEXT = '/api/v1/login/b';
    public static ANONYMOUS_CONTEXT = '/api/v1/migration/anonymous';
    public static ANONYMOUS_CONTEXT_NEP = '/api/v1/migration/anonymous/National_enquiry_point';


    public static USER_CONTEXT = 'user';
    public static MASTERS_CONTEXT = '/api/v1/migration';
    public static SYSTEMS_ADMIN_SECURITY = `${ApiEndpointService.MASTERS_CONTEXT}/security`;
    public static SYSTEMS_ADMIN_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/system/admin/masters`;
    public static QA_CONTEXT = '/api/v1/migration/qa';
    public static STL_CONTEXT = '/api/v1/migration/stdLevy';
    public static SL_CONTEXT = '/api/auth';
    public static QA_CONTEXT_APPLY = `${ApiEndpointService.QA_CONTEXT}/permit/apply`;
    public static QA_CONTEXT_VIEW = `${ApiEndpointService.QA_CONTEXT}/permit/view`;
    public static ADMIN_CONTEXT = 'api/admin/v1';
    public static SD_NWA_CONTEXT = `${ApiEndpointService.MASTERS_CONTEXT}/nwa`;
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
        VALIDATE_TOKEN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/validateToken`,
        REGISTER_COMPANY: `${ApiEndpointService.ANONYMOUS_CONTEXT}/registerCompany`,
        COMPANY_LIST: `${ApiEndpointService.MASTERS_CONTEXT}/company/`,
        USER_DETAILS: `${ApiEndpointService.MASTERS_CONTEXT}/secure/user/details/`,
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
        LIST_ACTIVE_RBAC_USERS_ROLES: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-roles/`,
        LIST_ACTIVE_RBAC_USERS_SECTION: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-section/`,
        LIST_ACTIVE_RBAC_USERS_CFS: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/fetch/user-cfs/`,
        REVOKE_ROLE_FROM_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/role/revoke/`,
        ASSIGN_ROLE_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/role/assign/`,
        REVOKE_SECTION_FROM_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/section/revoke/`,
        ASSIGN_SECTION_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/section/assign/`,
        REVOKE_CFS_FROM_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/cfs/revoke/`,
        ASSIGN_CFS_TO_USER: `${ApiEndpointService.SYSTEMS_ADMIN_SECURITY}/rbac/cfs/assign/`,

        // tslint:disable-next-line:max-line-length
        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::QA ENDPOINTS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        PERMIT_QR_CODE_SCAN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/permit-qrcode/details`,

        PERMIT_PROCESS_STEP: `${ApiEndpointService.QA_CONTEXT_APPLY}/process-step-add`,

        FIRM_PERMIT_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/firm-list`,
        QA_MPESA_STK_PUSH: `${ApiEndpointService.QA_CONTEXT}/permit/mpesa/stk-push`,
        PERMIT_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/list`,
        PERMIT_LIST_AWARDED: `${ApiEndpointService.QA_CONTEXT}/permit/awarded-list`,
        PERMIT_LIST_MIGRATION: `${ApiEndpointService.QA_CONTEXT}/permit/my-permits-loaded`,
        PERMIT_LIST_ALL: `${ApiEndpointService.QA_CONTEXT}/permit/all-my-permits-loaded`,

        PERMIT_LIST_TO_GENERATE_FMRK: `${ApiEndpointService.QA_CONTEXT}/permit/awarded-list-fmark-generate`,
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


        INVOICE_CONSOLIDATE_SUBMIT: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-submit`,
        INVOICE_CONSOLIDATE_ADD: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-add`,
        INVOICE_CONSOLIDATE_REMOVE: `${ApiEndpointService.QA_CONTEXT_APPLY}/invoice/batch-invoice-remove`,
        INVOICE_LIST_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-list`,
        INVOICE_DETAILS_BALANCE: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-balance-details`,
        INVOICE_LIST_NO_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/invoice/list-no-batch-Id`,
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
        NWA_PREPARE_JUSTIFICATION: `${ApiEndpointService.SD_NWA_CONTEXT}/prepareJustification`,
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
        IST_PREPARE_ADOPTION_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/prepareAdoptionProposal`,
        IST_UPLOAD_DOCUMENT: `${ApiEndpointService.SD_IST_CONTEXT}/file-upload`,
        IST_VIEW_IS_PROPOSALS: `${ApiEndpointService.SD_IST_CONTEXT}/getISProposals`,
        IST_VIEW_IS_PROPOSALS_DOC: `${ApiEndpointService.SD_IST_CONTEXT}/view/proposal`,
        IST_SUBMIT_AP_COMMENTS: `${ApiEndpointService.SD_IST_CONTEXT}/SubmitAPComments`,
        IST_TC_SEC_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getTCSECTasks`,
        IST_DECISION_ON_PROPOSAL: `${ApiEndpointService.SD_IST_CONTEXT}/decisionOnProposal`,
        IST_PREPARE_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/prepareJustification`,
        IST_UPLOAD_JS_DOCUMENT: `${ApiEndpointService.SD_IST_CONTEXT}/js-file-upload`,
        IST_SPC_SEC_TASKS: `${ApiEndpointService.SD_IST_CONTEXT}/getSPCSECTasks`,
        IST_VIEW_IS_JUSTIFICATION_DOC: `${ApiEndpointService.SD_IST_CONTEXT}/view/justification`,
        IST_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_IST_CONTEXT}/decisionOnJustification`,
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


        // SD COMPANY STANDARDS
        ICT_GET_PRODUCTS: `${ApiEndpointService.SD_ICT_CONTEXT}/getProducts`,
        // ICT_GET_PRODUCTS_LS: `${ApiEndpointService.SD_DR_CONTEXT}/getProducts/${id}`,
        ICT_GET_PRODUCTS_LS: `${ApiEndpointService.SD_DR_CONTEXT}/getProducts/`,
        ICT_GET_DEPARTMENTS: `${ApiEndpointService.SD_DR_CONTEXT}/getDepartments`,
        ICT_GET_USERS: `${ApiEndpointService.SD_ICT_CONTEXT}/getUserList`,
        ICT_GET_DEPARTMENT: `${ApiEndpointService.SD_DR_CONTEXT}/getDepartments`,
        ICT_GET_TC_COMMITTEE: `${ApiEndpointService.SD_DR_CONTEXT}/getTechnicalCommittee`,
        // ICT_GET_PRODUCT_CATEGORIES: `${ApiEndpointService.SD_DR_CONTEXT}/getProductCategories/${id}`,
        ICT_GET_PRODUCT_CATEGORIES: `${ApiEndpointService.SD_DR_CONTEXT}/getProductCategories/`,
        ICT_ADD_STD_REQUEST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/company_standard/request`,
        ICT_HOD_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getHODTasks`,
        ICT_ASSIGN_REQUEST: `${ApiEndpointService.SD_ICT_CONTEXT}/assignRequest`,
        ICT_FORM_JOINT_COMMITTEE: `${ApiEndpointService.SD_ICT_CONTEXT}/formJointCommittee`,
        ICT_PL_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getPlTasks`,
        ICT_PREPARE_JUSTIFICATION: `${ApiEndpointService.SD_ICT_CONTEXT}/prepareJustification`,
        ICT_UPLOAD_JC: `${ApiEndpointService.SD_ICT_CONTEXT}/file-upload`,
        ICT_SPC_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getSpcSecTasks`,
        ICT_DECISION_ON_JUSTIFICATION: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnJustification`,
        ICT_SAC_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getSacSecTasks`,
        ICT_UPLOAD_DATA_VIEW_STD: `${ApiEndpointService.SD_ICT_CONTEXT}/view/comStandard`,
        ICT_DECISION_ON_APP_JUSTIFICATION: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnAppJustification`,
        ICT_PREPARE_PRELIMINARY_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/uploadDraft`,
        ICT_UPLOAD_PD: `${ApiEndpointService.SD_ICT_CONTEXT}/draft-file-upload`,
        ICT_JC_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getJcSecTasks`,
        ICT_UPLOAD_DATA_VIEW_PD: `${ApiEndpointService.SD_ICT_CONTEXT}/view/comDraft`,
        ICT_DECISION_ON_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnCompanyStdDraft`,
        ICT_COM_DECISION_ON_DRAFT: `${ApiEndpointService.SD_ICT_CONTEXT}/decisionOnComStdDraft`,
        ICT_COM_SEC_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getComSecTasks`,
        ICT_PREPARE_COM_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/uploadComStandard`,
        ICT_COM_UPLOAD_SD: `${ApiEndpointService.SD_ICT_CONTEXT}/std-file-upload`,
        ICT_COM_HOP_TASKS: `${ApiEndpointService.SD_ICT_CONTEXT}/getHopTasks`,
        ICT_COM_EDIT_STANDARD: `${ApiEndpointService.SD_ICT_CONTEXT}/editCompanyStandard`,
        ICT_COM_UPLOAD_SD_EDIT: `${ApiEndpointService.SD_ICT_CONTEXT}/std-efile-upload`,

        // SD SYSTEMIC REVIEW
        SR_GET_REVIEWED_STANDARDS: `${ApiEndpointService.SD_SR_CONTEXT}/reviewedStandards`,
        SR_REVIEW_FORM: `${ApiEndpointService.SD_SR_CONTEXT}/standardReviewForm`,
        SR_GET_REVIEW_FORM: `${ApiEndpointService.SD_SR_CONTEXT}/getReviewForms`,
        SR_REVIEW_COMMENTS: `${ApiEndpointService.SD_SR_CONTEXT}/commentsOnReview`,
        SR_GET_REVIEW_TASKS: `${ApiEndpointService.SD_SR_CONTEXT}/getReviewTasks`,
        SR_DECISION_ON_RECOMMENDATION: `${ApiEndpointService.SD_SR_CONTEXT}/decisionOnRecommendation`,
        SR_UPLOAD_DOCUMENT: `${ApiEndpointService.SD_SR_CONTEXT}/sr-file-upload`,

        // SD NATIONAL ENQUIRY POINT
        NEP_ENQUIRY_NEP_TASKS: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/nep_officer/tasks`,
        NEP_ENQUIRY_DIVISION_TASKS: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/division/tasks`,
        NEP_MAKE_ENQUIRY: `${ApiEndpointService.ANONYMOUS_CONTEXT_NEP}/notification_request`,
        NEP_INFORMATION_AVAILABLE_YES: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/nep_officer/is_available`,
        NEP_DEPARTMENT_RESPONSE: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/division_response/send_response`,
        NEP_FEEDBACK_EMAIL: `${ApiEndpointService.SD_NEP_NATIONAL_ENQUIRY}/information_available/send_email`,

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
        STD_LEVY_COMPANY_DETAILS: `${ApiEndpointService.STL_CONTEXT}/getCompanyProfile`,
        STD_LEVY_SL_FORM: `${ApiEndpointService.STL_CONTEXT}/getNotificationFormDetails`,
        STD_LEVY_SL_NT_FORM: `${ApiEndpointService.STL_CONTEXT}/getSlNotificationFormDetails`,
        STD_LEVY_MANUFACTURE_STATUS: `${ApiEndpointService.STL_CONTEXT}/getManufacturerStatus`,
        STD_LEVY_SL10_FORM: `${ApiEndpointService.STL_CONTEXT}/save-sl-notification-form`,
        STD_LEVY_SCHEDULE_SITE_VISIT: `${ApiEndpointService.STL_CONTEXT}/scheduleSiteVisit`,
        STD_LEVY_SCHEDULED_SITE_VISITS: `${ApiEndpointService.STL_CONTEXT}/getScheduledVisits`,
        STD_LEVY_ASSIGN_COMPANY_TASK: `${ApiEndpointService.STL_CONTEXT}/assignCompany`,
        STD_LEVY_SAVE_VISIT_REPORT: `${ApiEndpointService.STL_CONTEXT}/reportOnSiteVisit`,
        STD_LEVY_SAVE_VISIT_REPORT_DOCUMENT: `${ApiEndpointService.STL_CONTEXT}/site-report-upload`,
        STD_LEVY_VIEW_VISIT_REPORT_DOCUMENT: `${ApiEndpointService.STL_CONTEXT}/view/siteVisitReport`,
        STD_LEVY_SCHEDULED_SITE_VISITS_REPORT: `${ApiEndpointService.STL_CONTEXT}/getSiteReport`,
        STD_LEVY_SITE_VISITS_REPORT_APPROVAL_ONE: `${ApiEndpointService.STL_CONTEXT}/decisionOnSiteReport`,
        STD_LEVY_SITE_VISITS_REPORT_LEVEL_TWO: `${ApiEndpointService.STL_CONTEXT}/getSiteReportLevelTwo`,
        STD_LEVY_SITE_VISITS_REPORT_APPROVAL_TWO: `${ApiEndpointService.STL_CONTEXT}/decisionOnSiteReportLevelTwo`,
        STD_LEVY_SAVE_VISIT_REPORT_FEEDBACK: `${ApiEndpointService.STL_CONTEXT}/siteVisitReportFeedback`,
        STD_LEVY_SAVE_VISIT_REPORT_VIEW_FEEDBACK: `${ApiEndpointService.STL_CONTEXT}/getSiteFeedback`,
        STD_LEVY_MANUFACTURE_LIST: `${ApiEndpointService.STL_CONTEXT}/getManufacturerList`,
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
        STD_LEVY_COMPANY_EDIT_COMPANY_DATA: `${ApiEndpointService.STL_CONTEXT}/editCompanyDetailsConfirm`,
        STD_LEVY_NOTIFICATION_FORM_STATUS: `${ApiEndpointService.STL_CONTEXT}/getSLNotificationStatus`,
        STD_LEVY_BRANCH_NAME: `${ApiEndpointService.STL_CONTEXT}/getBranchName`,





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
    const protocol: string = ApiEndpointService.PROTOCOL.HTTPS;
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
