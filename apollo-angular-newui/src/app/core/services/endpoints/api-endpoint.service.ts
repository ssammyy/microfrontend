import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiEndpointService {

  /**
   * Map of protocols for API endpoints.
   */
  public static PROTOCOL = {
    HTTP: 'http://',
    HTTPS: 'https://'
  };

  /**
   * Map of domains for API endpoints.
   */
  public static DOMAIN = {
    // LOCAL_DEV: 'localhost:8006'
      // LOCAL_DEV: '12:8006'
      // LOCAL_DEV: '41.72.209.58:8006'
      LOCAL_DEV: 'kimsint.kebs.org:8006'
  };

  /**
   * Map of contexts for API endpoints.
   */
  public static CONTEXT = '';

  /**
   * Map of contexts for API endpoints.
   */
  public static AUTH_CONTEXT = '/api/v1/login';
  public static ANONYMOUS_CONTEXT = '/api/v1/migration/anonymous';
  public static USER_CONTEXT = 'user';
  public static MASTERS_CONTEXT = '/api/v1/migration';
  public static QA_CONTEXT = '/api/v1/migration/qa';
  public static ADMIN_CONTEXT = 'api/admin/v1';

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
    USER_DETAILS: `${ApiEndpointService.AUTH_CONTEXT}/details`,
    LOGIN_PAGE: `${ApiEndpointService.AUTH_CONTEXT}`,
    VALIDATE_BRS: `${ApiEndpointService.ANONYMOUS_CONTEXT}/validateBrs`,
    HOME_PAGE: `${ApiEndpointService.ANONYMOUS_CONTEXT}`,
    SEND_TOKEN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/sendToken`,
    SEND_TOKEN_FOR_USER: `${ApiEndpointService.ANONYMOUS_CONTEXT}/sendTokenForUser`,
    VALIDATE_TOKEN_FOR_USER: `${ApiEndpointService.ANONYMOUS_CONTEXT}/validateTokenFromThePhone`,
    VALIDATE_TOKEN: `${ApiEndpointService.ANONYMOUS_CONTEXT}/validateToken`,
    REGISTER_COMPANY: `${ApiEndpointService.ANONYMOUS_CONTEXT}/registerCompany`,
    COMPANY_LIST: `${ApiEndpointService.MASTERS_CONTEXT}/company/`,
    LOGOUT_URL: `${ApiEndpointService.MASTERS_CONTEXT}/secure/logout`,
    BRANCH_LIST: `${ApiEndpointService.MASTERS_CONTEXT}/{companyId}/branches/`,
    REGIONS_LIST: `${ApiEndpointService.ANONYMOUS_CONTEXT}/regions`,
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
      /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::QA ENDPOINTS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      FIRM_PERMIT_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/firm-list`,
      PERMIT_LIST: `${ApiEndpointService.QA_CONTEXT}/permit/list`,
      PERMIT_VIEW_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/details`,
      PERMIT_SUBMIT_DETAILS_FOR_REVIEW: `${ApiEndpointService.QA_CONTEXT}/permit/apply/submit-application-review`,
      PERMIT_SUBMIT_APPLICATION: `${ApiEndpointService.QA_CONTEXT}/permit/apply/submit-application`,
      PLANT_LIST: `${ApiEndpointService.QA_CONTEXT}/branch-list`,
      SECTION_LIST: `${ApiEndpointService.QA_CONTEXT}/sections-list`,
      PERMIT_APPLY_STA1: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta1`,
      PERMIT_UPDATE_STA1: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta1-update`,
      PERMIT_APPLY_STA3: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta3`,
      PERMIT_UPDATE_STA3: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta3-update`,
      INVOICE_LIST_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-list`,
      INVOICE_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/view/invoice/batch-invoice-details`,
      INVOICE_DETAILS_PDF: `${ApiEndpointService.QA_CONTEXT}/report/proforma-invoice-with-Item`,
      PERMIT_APPLY_STA10_FIRM_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta10/firm_details`,
      PERMIT_APPLY_STA10_PERSONNEL_DETAILS: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta10/personnel_details`,
      PERMIT_APPLY_STA10_PRODUCTS_BEING_MANUFACTURED: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta10/products_being_manufactured`,
      PERMIT_APPLY_STA10_RAW_MATERIAL: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta10/raw_material`,
      PERMIT_APPLY_STA10_MACHINERY_PLANT: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta10/machinery_plant`,
      PERMIT_APPLY_STA10_MANUFACTURING_PROCESS: `${ApiEndpointService.QA_CONTEXT}/permit/apply/sta10/manufacturing_process`,


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
