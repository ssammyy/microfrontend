import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {
    ApiResponseModel,
    BatchFileFuelSaveDto,
    BSNumberSaveDto,
    ComplaintCustomersDto,
    ComplaintDto,
    ComplaintLocationDto,
    ComplaintDetailsDto,
    CompliantRemediationDto,
    FuelBatchDetailsDto,
    FuelEntityAssignOfficerDto,
    FuelEntityDto,
    FuelEntityRapidTestDto,
    FuelInspectionDto,
    FuelInspectionScheduleListDetailsDto,
    LaboratoryDto,
    LabResultsParamDto,
    LIMSFilesFoundDto,
    MSComplaintSubmittedSuccessful,
    MSSSFComplianceStatusDetailsDto,
    MSSSFLabResultsDto,
    MSSSFPDFListDetailsDto,
    MsUsersDto,
    NewComplaintDto,
    PDFSaveComplianceStatusDto,
    RemediationDto,
    SampleCollectionDto,
    SampleCollectionItemsDto,
    SampleSubmissionDto,
    SampleSubmissionItemsDto,
    SSFSaveComplianceStatusDto,
    AllComplaintsDetailsDto,
    MsDepartment,
    MsDivisionDetails,
    MsProducts,
    MsProductCategories,
    MsBroadProductCategory,
    MsProductSubcategory,
    ComplaintApproveDto,
    ComplaintRejectDto,
    ComplaintAdviceRejectDto,
    ComplaintAssignDto,
    MsStandardProductCategory,
    ComplaintClassificationDto,
    WorkPlanBatchDetailsDto,
    WorkPlanScheduleListDetailsDto,
    WorkPlanEntityDto,
    WorkPlanInspectionDto,
    WorkPlanScheduleApprovalDto,
    CountriesEntityDto,
    ChargeSheetDto,
    CountryListDto,
    DataReportDto,
    SeizureDeclarationDto,
    InspectionInvestigationReportDto,
    PreliminaryReportDto,
    ApprovalDto,
    PreliminaryReportFinal,
    MsRecommendationDto,
    WorkPlanFinalRecommendationDto,
    WorkPlanFeedBackDto,
    FuelScheduleTeamsListDetailsDto,
    TeamsFuelSaveDto,
    FuelScheduleCountyListDetailsDto, RapidTestProductsDto, EndFuelDto, RegionReAssignDto,
} from './ms.model';
import swal from 'sweetalert2';
import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';
import Swal from 'sweetalert2';
import {AllPermitDetailsDto} from '../qa/qa.model';

@Injectable({
    providedIn: 'root',
})
export class MsService {

    constructor(private http: HttpClient) {
    }

    public notAllowed(input: RegExp): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            const forbidden = input.test(control.value);
            return forbidden ? {notAllowed: {value: control.value}} : null;
        };
    }

    public getAllCountriesList() {
        const countryList: {name: string, code: string }[] = [{name: 'Afghanistan', code: 'AF'},
            {name: 'Åland Islands', code: 'AX'},
            {name: 'Albania', code: 'AL'},
            {name: 'Algeria', code: 'DZ'},
            {name: 'American Samoa', code: 'AS'},
            {name: 'AndorrA', code: 'AD'},
            {name: 'Angola', code: 'AO'},
            {name: 'Anguilla', code: 'AI'},
            {name: 'Antarctica', code: 'AQ'},
            {name: 'Antigua and Barbuda', code: 'AG'},
            {name: 'Argentina', code: 'AR'},
            {name: 'Armenia', code: 'AM'},
            {name: 'Aruba', code: 'AW'},
            {name: 'Australia', code: 'AU'},
            {name: 'Austria', code: 'AT'},
            {name: 'Azerbaijan', code: 'AZ'},
            {name: 'Bahamas', code: 'BS'},
            {name: 'Bahrain', code: 'BH'},
            {name: 'Bangladesh', code: 'BD'},
            {name: 'Barbados', code: 'BB'},
            {name: 'Belarus', code: 'BY'},
            {name: 'Belgium', code: 'BE'},
            {name: 'Belize', code: 'BZ'},
            {name: 'Benin', code: 'BJ'},
            {name: 'Bermuda', code: 'BM'},
            {name: 'Bhutan', code: 'BT'},
            {name: 'Bolivia', code: 'BO'},
            {name: 'Bosnia and Herzegovina', code: 'BA'},
            {name: 'Botswana', code: 'BW'},
            {name: 'Bouvet Island', code: 'BV'},
            {name: 'Brazil', code: 'BR'},
            {name: 'British Indian Ocean Territory', code: 'IO'},
            {name: 'Brunei Darussalam', code: 'BN'},
            {name: 'Bulgaria', code: 'BG'},
            {name: 'Burkina Faso', code: 'BF'},
            {name: 'Burundi', code: 'BI'},
            {name: 'Cambodia', code: 'KH'},
            {name: 'Cameroon', code: 'CM'},
            {name: 'Canada', code: 'CA'},
            {name: 'Cape Verde', code: 'CV'},
            {name: 'Cayman Islands', code: 'KY'},
            {name: 'Central African Republic', code: 'CF'},
            {name: 'Chad', code: 'TD'},
            {name: 'Chile', code: 'CL'},
            {name: 'China', code: 'CN'},
            {name: 'Christmas Island', code: 'CX'},
            {name: 'Cocos (Keeling) Islands', code: 'CC'},
            {name: 'Colombia', code: 'CO'},
            {name: 'Comoros', code: 'KM'},
            {name: 'Congo', code: 'CG'},
            {name: 'Congo, The Democratic Republic of the', code: 'CD'},
            {name: 'Cook Islands', code: 'CK'},
            {name: 'Costa Rica', code: 'CR'},
            {name: 'Cote D\'Ivoire', code: 'CI'},
            {name: 'Croatia', code: 'HR'},
            {name: 'Cuba', code: 'CU'},
            {name: 'Cyprus', code: 'CY'},
            {name: 'Czech Republic', code: 'CZ'},
            {name: 'Denmark', code: 'DK'},
            {name: 'Djibouti', code: 'DJ'},
            {name: 'Dominica', code: 'DM'},
            {name: 'Dominican Republic', code: 'DO'},
            {name: 'Ecuador', code: 'EC'},
            {name: 'Egypt', code: 'EG'},
            {name: 'El Salvador', code: 'SV'},
            {name: 'Equatorial Guinea', code: 'GQ'},
            {name: 'Eritrea', code: 'ER'},
            {name: 'Estonia', code: 'EE'},
            {name: 'Ethiopia', code: 'ET'},
            {name: 'Falkland Islands (Malvinas)', code: 'FK'},
            {name: 'Faroe Islands', code: 'FO'},
            {name: 'Fiji', code: 'FJ'},
            {name: 'Finland', code: 'FI'},
            {name: 'France', code: 'FR'},
            {name: 'French Guiana', code: 'GF'},
            {name: 'French Polynesia', code: 'PF'},
            {name: 'French Southern Territories', code: 'TF'},
            {name: 'Gabon', code: 'GA'},
            {name: 'Gambia', code: 'GM'},
            {name: 'Georgia', code: 'GE'},
            {name: 'Germany', code: 'DE'},
            {name: 'Ghana', code: 'GH'},
            {name: 'Gibraltar', code: 'GI'},
            {name: 'Greece', code: 'GR'},
            {name: 'Greenland', code: 'GL'},
            {name: 'Grenada', code: 'GD'},
            {name: 'Guadeloupe', code: 'GP'},
            {name: 'Guam', code: 'GU'},
            {name: 'Guatemala', code: 'GT'},
            {name: 'Guernsey', code: 'GG'},
            {name: 'Guinea', code: 'GN'},
            {name: 'Guinea-Bissau', code: 'GW'},
            {name: 'Guyana', code: 'GY'},
            {name: 'Haiti', code: 'HT'},
            {name: 'Heard Island and Mcdonald Islands', code: 'HM'},
            {name: 'Holy See (Vatican City State)', code: 'VA'},
            {name: 'Honduras', code: 'HN'},
            {name: 'Hong Kong', code: 'HK'},
            {name: 'Hungary', code: 'HU'},
            {name: 'Iceland', code: 'IS'},
            {name: 'India', code: 'IN'},
            {name: 'Indonesia', code: 'ID'},
            {name: 'Iran, Islamic Republic Of', code: 'IR'},
            {name: 'Iraq', code: 'IQ'},
            {name: 'Ireland', code: 'IE'},
            {name: 'Isle of Man', code: 'IM'},
            {name: 'Israel', code: 'IL'},
            {name: 'Italy', code: 'IT'},
            {name: 'Jamaica', code: 'JM'},
            {name: 'Japan', code: 'JP'},
            {name: 'Jersey', code: 'JE'},
            {name: 'Jordan', code: 'JO'},
            {name: 'Kazakhstan', code: 'KZ'},
            {name: 'Kenya', code: 'KE'},
            {name: 'Kiribati', code: 'KI'},
            {name: 'Korea, Democratic People\'S Republic of', code: 'KP'},
            {name: 'Korea, Republic of', code: 'KR'},
            {name: 'Kuwait', code: 'KW'},
            {name: 'Kyrgyzstan', code: 'KG'},
            {name: 'Lao People\'S Democratic Republic', code: 'LA'},
            {name: 'Latvia', code: 'LV'},
            {name: 'Lebanon', code: 'LB'},
            {name: 'Lesotho', code: 'LS'},
            {name: 'Liberia', code: 'LR'},
            {name: 'Libyan Arab Jamahiriya', code: 'LY'},
            {name: 'Liechtenstein', code: 'LI'},
            {name: 'Lithuania', code: 'LT'},
            {name: 'Luxembourg', code: 'LU'},
            {name: 'Macao', code: 'MO'},
            {name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK'},
            {name: 'Madagascar', code: 'MG'},
            {name: 'Malawi', code: 'MW'},
            {name: 'Malaysia', code: 'MY'},
            {name: 'Maldives', code: 'MV'},
            {name: 'Mali', code: 'ML'},
            {name: 'Malta', code: 'MT'},
            {name: 'Marshall Islands', code: 'MH'},
            {name: 'Martinique', code: 'MQ'},
            {name: 'Mauritania', code: 'MR'},
            {name: 'Mauritius', code: 'MU'},
            {name: 'Mayotte', code: 'YT'},
            {name: 'Mexico', code: 'MX'},
            {name: 'Micronesia, Federated States of', code: 'FM'},
            {name: 'Moldova, Republic of', code: 'MD'},
            {name: 'Monaco', code: 'MC'},
            {name: 'Mongolia', code: 'MN'},
            {name: 'Montserrat', code: 'MS'},
            {name: 'Morocco', code: 'MA'},
            {name: 'Mozambique', code: 'MZ'},
            {name: 'Myanmar', code: 'MM'},
            {name: 'Namibia', code: 'NA'},
            {name: 'Nauru', code: 'NR'},
            {name: 'Nepal', code: 'NP'},
            {name: 'Netherlands', code: 'NL'},
            {name: 'Netherlands Antilles', code: 'AN'},
            {name: 'New Caledonia', code: 'NC'},
            {name: 'New Zealand', code: 'NZ'},
            {name: 'Nicaragua', code: 'NI'},
            {name: 'Niger', code: 'NE'},
            {name: 'Nigeria', code: 'NG'},
            {name: 'Niue', code: 'NU'},
            {name: 'Norfolk Island', code: 'NF'},
            {name: 'Northern Mariana Islands', code: 'MP'},
            {name: 'Norway', code: 'NO'},
            {name: 'Oman', code: 'OM'},
            {name: 'Pakistan', code: 'PK'},
            {name: 'Palau', code: 'PW'},
            {name: 'Palestinian Territory, Occupied', code: 'PS'},
            {name: 'Panama', code: 'PA'},
            {name: 'Papua New Guinea', code: 'PG'},
            {name: 'Paraguay', code: 'PY'},
            {name: 'Peru', code: 'PE'},
            {name: 'Philippines', code: 'PH'},
            {name: 'Pitcairn', code: 'PN'},
            {name: 'Poland', code: 'PL'},
            {name: 'Portugal', code: 'PT'},
            {name: 'Puerto Rico', code: 'PR'},
            {name: 'Qatar', code: 'QA'},
            {name: 'Reunion', code: 'RE'},
            {name: 'Romania', code: 'RO'},
            {name: 'Russian Federation', code: 'RU'},
            {name: 'RWANDA', code: 'RW'},
            {name: 'Saint Helena', code: 'SH'},
            {name: 'Saint Kitts and Nevis', code: 'KN'},
            {name: 'Saint Lucia', code: 'LC'},
            {name: 'Saint Pierre and Miquelon', code: 'PM'},
            {name: 'Saint Vincent and the Grenadines', code: 'VC'},
            {name: 'Samoa', code: 'WS'},
            {name: 'San Marino', code: 'SM'},
            {name: 'Sao Tome and Principe', code: 'ST'},
            {name: 'Saudi Arabia', code: 'SA'},
            {name: 'Senegal', code: 'SN'},
            {name: 'Serbia and Montenegro', code: 'CS'},
            {name: 'Seychelles', code: 'SC'},
            {name: 'Sierra Leone', code: 'SL'},
            {name: 'Singapore', code: 'SG'},
            {name: 'Slovakia', code: 'SK'},
            {name: 'Slovenia', code: 'SI'},
            {name: 'Solomon Islands', code: 'SB'},
            {name: 'Somalia', code: 'SO'},
            {name: 'South Africa', code: 'ZA'},
            {name: 'South Georgia and the South Sandwich Islands', code: 'GS'},
            {name: 'Spain', code: 'ES'},
            {name: 'Sri Lanka', code: 'LK'},
            {name: 'Sudan', code: 'SD'},
            {name: 'Suriname', code: 'SR'},
            {name: 'Svalbard and Jan Mayen', code: 'SJ'},
            {name: 'Swaziland', code: 'SZ'},
            {name: 'Sweden', code: 'SE'},
            {name: 'Switzerland', code: 'CH'},
            {name: 'Syrian Arab Republic', code: 'SY'},
            {name: 'Taiwan, Province of China', code: 'TW'},
            {name: 'Tajikistan', code: 'TJ'},
            {name: 'Tanzania, United Republic of', code: 'TZ'},
            {name: 'Thailand', code: 'TH'},
            {name: 'Timor-Leste', code: 'TL'},
            {name: 'Togo', code: 'TG'},
            {name: 'Tokelau', code: 'TK'},
            {name: 'Tonga', code: 'TO'},
            {name: 'Trinidad and Tobago', code: 'TT'},
            {name: 'Tunisia', code: 'TN'},
            {name: 'Turkey', code: 'TR'},
            {name: 'Turkmenistan', code: 'TM'},
            {name: 'Turks and Caicos Islands', code: 'TC'},
            {name: 'Tuvalu', code: 'TV'},
            {name: 'Uganda', code: 'UG'},
            {name: 'Ukraine', code: 'UA'},
            {name: 'United Arab Emirates', code: 'AE'},
            {name: 'United Kingdom', code: 'GB'},
            {name: 'United States', code: 'US'},
            {name: 'United States Minor Outlying Islands', code: 'UM'},
            {name: 'Uruguay', code: 'UY'},
            {name: 'Uzbekistan', code: 'UZ'},
            {name: 'Vanuatu', code: 'VU'},
            {name: 'Venezuela', code: 'VE'},
            {name: 'Viet Nam', code: 'VN'},
            {name: 'Virgin Islands, British', code: 'VG'},
            {name: 'Virgin Islands, U.S.', code: 'VI'},
            {name: 'Wallis and Futuna', code: 'WF'},
            {name: 'Western Sahara', code: 'EH'},
            {name: 'Yemen', code: 'YE'},
            {name: 'Zambia', code: 'ZM'},
            {name: 'Zimbabwe', code: 'ZW'},
        ];
        return countryList;
    }

   public fuelBatchDetailsListExamples(): FuelBatchDetailsDto[] {
        const test101 = new FuelBatchDetailsDto();
        test101.id = 123;
        test101.region = 'test';
        test101.county = 'test';
        test101.town = 'test';
        test101.referenceNumber = 'test';
        test101.batchFileYear = 'test';
        test101.remarks = 'test';
        test101.batchClosed = true;

        const test: FuelBatchDetailsDto[] = [];
        test.push(test101);

        return test;
    }

   public fuelInspectionDetailsExamples(): FuelInspectionDto {
       const currentDate = new Date();
       const test101BatchDetails = new FuelBatchDetailsDto();
       test101BatchDetails.id = 123;
       test101BatchDetails.region = 'test';
       test101BatchDetails.county = 'test';
       test101BatchDetails.town = 'test';
       test101BatchDetails.referenceNumber = 'test';
       test101BatchDetails.batchFileYear = 'test';
       test101BatchDetails.remarks = 'test';
       test101BatchDetails.batchClosed = true;

       const test101MSUserDetails = new MsUsersDto();
       test101MSUserDetails.id = 123;
       test101MSUserDetails.firstName = 'TESTUSER';
       test101MSUserDetails.lastName = 'TESTUSER';
       test101MSUserDetails.userName = 'TESTUSER';
       test101MSUserDetails.email = 'TESTUSER';
       test101MSUserDetails.status = true;

       const testUserList: MsUsersDto[] = [];
       testUserList.push(test101MSUserDetails);

       const test101SampleCollectionItemsDto = new SampleCollectionItemsDto();
       test101SampleCollectionItemsDto.id = 125;
       test101SampleCollectionItemsDto.productBrandName = 'test101SampleCollectionItemsDto';
       test101SampleCollectionItemsDto.batchNo = '123';
       test101SampleCollectionItemsDto.batchSize = '6776';
       test101SampleCollectionItemsDto.sampleSize = '756';

       const testSampleCollectionItemsDto: SampleCollectionItemsDto[] = [];
       testSampleCollectionItemsDto.push(test101SampleCollectionItemsDto);

       const test101MSSampleCollectDetails = new SampleCollectionDto();
       test101MSSampleCollectDetails.nameManufacturerTrader = 'testSampleCollect';
       test101MSSampleCollectDetails.addressManufacturerTrader = 'testSampleCollect';
       test101MSSampleCollectDetails.samplingMethod = 'testSampleCollect';
       test101MSSampleCollectDetails.reasonsCollectingSamples = 'testSampleCollect';
       test101MSSampleCollectDetails.anyRemarks = 'testSampleCollect';
       test101MSSampleCollectDetails.designationOfficerCollectingSample = 'testSampleCollect';
       test101MSSampleCollectDetails.nameOfficerCollectingSample = 'testSampleCollect';
       test101MSSampleCollectDetails.dateOfficerCollectingSample = currentDate;
       test101MSSampleCollectDetails.nameWitness = 'testSampleCollect';
       test101MSSampleCollectDetails.designationWitness = 'testSampleCollect';
       test101MSSampleCollectDetails.dateWitness = currentDate;
       test101MSSampleCollectDetails.productsList = testSampleCollectionItemsDto;

       const test101SampleSubmissionItemsDto = new SampleSubmissionItemsDto();
       test101SampleSubmissionItemsDto.parameters = 'test198';
       test101SampleSubmissionItemsDto.laboratoryName = 'test198';

       const testSampleSubmissionItemsDto: SampleSubmissionItemsDto[] = [];
       testSampleSubmissionItemsDto.push(test101SampleSubmissionItemsDto);

       const test101SampleSubmissionDto = new SampleSubmissionDto();
       test101SampleSubmissionDto.nameProduct = 'stringTest';
       test101SampleSubmissionDto.packaging = 'stringTest';
       test101SampleSubmissionDto.labellingIdentification = 'stringTest';
       test101SampleSubmissionDto.fileRefNumber = 'stringTest';
       test101SampleSubmissionDto.referencesStandards = 'stringTest';
       test101SampleSubmissionDto.condition = 'stringTest';
       test101SampleSubmissionDto.sampleReferences = 'stringTest';
       test101SampleSubmissionDto.sendersName = 'stringTest';
       test101SampleSubmissionDto.designation = 'stringTest';
       test101SampleSubmissionDto.address = 'stringTest';
       test101SampleSubmissionDto.sendersDate = currentDate;
       test101SampleSubmissionDto.receiversName = 'stringTest';
       test101SampleSubmissionDto.testChargesKsh = 4567;
       test101SampleSubmissionDto.receiptLpoNumber = 'stringTest';
       test101SampleSubmissionDto.invoiceNumber = 'stringTest';
       test101SampleSubmissionDto.disposal = 'stringTest';
       test101SampleSubmissionDto.remarks = 'stringTest';
       test101SampleSubmissionDto.sampleCollectionNumber = 75877;
       test101SampleSubmissionDto.bsNumber = 'stringTest';
       test101SampleSubmissionDto.parametersList = testSampleSubmissionItemsDto;

       const test101LabResultsParamDto = new LabResultsParamDto();
       test101LabResultsParamDto.param = 'labParam';
       test101LabResultsParamDto.result = 'labParam';
       test101LabResultsParamDto.method = 'labParam';

       const testLabResultsParamDto: LabResultsParamDto[] = [];
       testLabResultsParamDto.push(test101LabResultsParamDto);

       const limsFilesFoundDto = new LIMSFilesFoundDto();
       limsFilesFoundDto.fileSavedStatus = true;
       limsFilesFoundDto.fileName = 'labParam';

       const limsFilesFoundDtoList: LIMSFilesFoundDto[] = [];
       limsFilesFoundDtoList.push(limsFilesFoundDto);

       const msSSFPDFListDetailsDto = new MSSSFPDFListDetailsDto();
       msSSFPDFListDetailsDto.pdfSavedId = 5265;
       msSSFPDFListDetailsDto.pdfName = 'steat';
       msSSFPDFListDetailsDto.sffId = 3434;
       msSSFPDFListDetailsDto.complianceRemarks = 'steat';
       msSSFPDFListDetailsDto.complianceStatus = true;

       const msSSFPDFListDetailsDtoList: MSSSFPDFListDetailsDto[] = [];
       msSSFPDFListDetailsDtoList.push(msSSFPDFListDetailsDto);

       const msssfComplianceStatusDetailsDto = new MSSSFComplianceStatusDetailsDto();
        msssfComplianceStatusDetailsDto.sffId = 5265;
       msssfComplianceStatusDetailsDto.bsNumber = 'steat';
       msssfComplianceStatusDetailsDto.complianceRemarks = '3434';
       msssfComplianceStatusDetailsDto.complianceStatus = true;

       const test101LabResultsDto = new MSSSFLabResultsDto();
       test101LabResultsDto.ssfResultsList = msssfComplianceStatusDetailsDto;
       test101LabResultsDto.savedPDFFiles = msSSFPDFListDetailsDtoList;
       test101LabResultsDto.limsPDFFiles = limsFilesFoundDtoList;
       test101LabResultsDto.parametersListTested = testLabResultsParamDto;




        const test101 = new FuelInspectionDto();
       test101.id = 123;
       test101.referenceNumber = 'test101';
       test101.company = 'testDetails';
       test101.petroleumProduct = 'testDetails';
       test101.physicalLocation = 'testDetails';
       test101.inspectionDateFrom = currentDate;
       test101.inspectionDateTo = currentDate;
       test101.processStage = 'test Stage';
       test101.batchDetails = test101BatchDetails;
       test101.officersList = testUserList;
       test101.officersAssigned = test101MSUserDetails;
       test101.sampleCollected = test101MSSampleCollectDetails;


        return test101;
    }
   public fuelInspectionListExamples(): FuelInspectionScheduleListDetailsDto {
       const currentDate = new Date();
       const test101BatchDetails = new FuelBatchDetailsDto();
       test101BatchDetails.id = 123;
       test101BatchDetails.region = 'test';
       test101BatchDetails.county = 'test';
       test101BatchDetails.town = 'test';
       test101BatchDetails.referenceNumber = 'test';
       test101BatchDetails.batchFileYear = 'test';
       test101BatchDetails.remarks = 'test';
       test101BatchDetails.batchClosed = true;

        const test101 = new FuelInspectionDto();
       test101.id = 123;
       test101.referenceNumber = 'test101';
       test101.company = 'testDetails';
       test101.petroleumProduct = 'testDetails';
       test101.physicalLocation = 'testDetails';
       test101.inspectionDateFrom = currentDate;
       test101.inspectionDateTo = currentDate;
       test101.batchDetails = test101BatchDetails;
       test101.processStage = 'test Stage';

        const test: FuelInspectionDto[] = [];
        test.push(test101);

       const test102 = new FuelInspectionScheduleListDetailsDto();
       test102.fuelInspectionDto = test;
       test102.fuelBatchDetailsDto = test101BatchDetails;


        return test102;
    }

    // Check if role is in required privileges
    public hasRole(privileges: string[], roles: any[]): boolean {
        for (const role of roles) {
            for (const p of privileges) {
                if (role === p) {
                    return true;
                }
            }
        }
        return false;
    }

    public reloadCurrentRoute() {
        location.reload();
    }


    showSuccessWith2Message(title: string, text: string, cancelMessage: string, successMessage: string, fn?: Function) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger',
            },
            buttonsStyling: false,
        });

        swalWithBootstrapButtons.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                if (fn) {
                    const results = fn();
                    if (results === true) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            successMessage,
                            'success',
                        );
                    } else if (results === false) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            'AN ERROR OCCURRED',
                            'error',
                        );
                    }
                }

            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    cancelMessage,
                    'error',
                );
            }
        });
    }

    showSuccess(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }

   public showError(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'error',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }
    // tslint:disable-next-line:max-line-length
    /*******************************************************************START OF MARKET SURVEILLANCE*****************************************************************************/

    // tslint:disable-next-line:max-line-length
    public loadMSWorkPlanBatchList(page: string, records: string, routeTake: string): Observable<WorkPlanBatchDetailsDto[]> {
        let url = null;
        switch (routeTake) {
            case 'all-workPlan-batch':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.ALL_BATCH_LIST);
                break;
            case 'open-workPlan-batch':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.OPEN_BATCH_LIST);
                break;
            case 'close-workPlan-batch':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.CLOSE_BATCH_LIST);
                break;
        }
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<WorkPlanBatchDetailsDto[]>(url, {params}).pipe(
            map(function (response: WorkPlanBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public addNewMSWorkPlanBatch(page: string, records: string): Observable<WorkPlanBatchDetailsDto[]> {
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.ADD_NEW_BATCH);
        return this.http.post<WorkPlanBatchDetailsDto[]>(url, null, {params}).pipe(
            map(function (response: WorkPlanBatchDetailsDto[]) {
                return response;
            })
            , catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.error} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public loadMSWorkPlanList(page: string, records: string, referenceNo: string, routeTake: string, complaintStatus: string): Observable<WorkPlanScheduleListDetailsDto> {
        // console.log(data);
        let url = null;
        switch (routeTake) {
            case 'my-tasks':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.MY_TASK_WORK_PLAN_LIST);
                break;
            case 'on-going':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.ONGOING_WORK_PLAN_LIST);
                break;
            case 'all-list':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.ALL_WORK_PLAN_LIST);
                break;
            case 'new-workPlan':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.NEW_WORK_PLAN_LIST);
                break;
            case 'completed':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.COMPLETED_WORK_PLAN_LIST);
                break;
        }

        const params = new HttpParams()
            .set('complaintStatus', complaintStatus)
            .set('batchReferenceNo', referenceNo)
            .set('page', page)
            .set('records', records);
        return this.http.get<WorkPlanScheduleListDetailsDto>(url, {params}).pipe(
            map(function (response: WorkPlanScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public closeMSWorkPlanBatch(referenceNo: string): Observable<WorkPlanBatchDetailsDto[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.CLOSE_BATCH);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanBatchDetailsDto[]>(url, null, {params}).pipe(
            map(function (response: WorkPlanBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msAddWorkPlanScheduleDetails(referenceNo: string, data: WorkPlanEntityDto): Observable<WorkPlanScheduleListDetailsDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.CREATE_NEW_WORK_PLAN_SCHEDULE);
        const params = new HttpParams()
            .set('batchReferenceNo', referenceNo);
        return this.http.post<WorkPlanScheduleListDetailsDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msUpdateWorkPlanScheduleDetails(batchReferenceNo: string, referenceNo: string,  data: WorkPlanEntityDto): Observable<WorkPlanScheduleListDetailsDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.UPDATE_NEW_WORK_PLAN_SCHEDULE);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanScheduleListDetailsDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msWorkPlanScheduleDetails(batchReferenceNo: string, referenceNo: string): Observable<WorkPlanInspectionDto> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_DETAILS);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.get<WorkPlanInspectionDto>(url, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleSaveChargeSheet(batchReferenceNo: string, referenceNo: string, data: ChargeSheetDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_CHARGE_SHEET);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleSaveDataReport(batchReferenceNo: string, referenceNo: string, data: DataReportDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_DATA_REPORT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleSaveSeizureDeclaration(batchReferenceNo: string, referenceNo: string, data: SeizureDeclarationDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_SEIZURE_DECLARATION,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleSaveInvestInspectReport(batchReferenceNo: string, referenceNo: string, data: InspectionInvestigationReportDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_INSPECTION_INVESTIGATION,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleSavePreliminaryReport(batchReferenceNo: string, referenceNo: string, data: PreliminaryReportDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_PRELIMINARY_REPORT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsApprove(batchReferenceNo: string, referenceNo: string, data: WorkPlanScheduleApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_APPROVE_DETAILS);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsApprovePreliminaryHOF(batchReferenceNo: string, referenceNo: string, data: ApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_APPROVE_HOF_PRELIMINARY_REPORT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsClientAppealed(batchReferenceNo: string, referenceNo: string, data: ApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_CLIENT_APPEALED);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsClientAppealedSuccessfully(batchReferenceNo: string, referenceNo: string, data: ApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_CLIENT_APPEALED_SUCCESSFULLY);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


     // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsApproveFinalPreliminaryHOF(batchReferenceNo: string, referenceNo: string, data: ApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_APPROVE_HOF_FINAL_PRELIMINARY_REPORT,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

     // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsApproveFinalPreliminaryHOD(batchReferenceNo: string, referenceNo: string, data: ApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_APPROVE_HOD_FINAL_PRELIMINARY_REPORT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

  // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsFinalRemarksHOD(batchReferenceNo: string, referenceNo: string, data: WorkPlanFeedBackDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_APPROVE_HOD_FINAL_REMARKS);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsApprovePreliminaryHOD(batchReferenceNo: string, referenceNo: string, data: ApprovalDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_APPROVE_HOD_PRELIMINARY_REPORT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


     // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsFinalRecommendationHOD(batchReferenceNo: string, referenceNo: string, data: WorkPlanFinalRecommendationDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_HOD_ADD_FINAL_RECOMMENDATION);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsFinalReport(batchReferenceNo: string, referenceNo: string, data: PreliminaryReportFinal): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_FINAL_REPORT,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsSubmitForApproval(batchReferenceNo: string, referenceNo: string): Observable<WorkPlanInspectionDto> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_SUBMIT_FOR_APPROVAL,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, null, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

     // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsStartOnsiteActivities(batchReferenceNo: string, referenceNo: string): Observable<WorkPlanInspectionDto> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_START_ONSITE_ACTIVITIES,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.get<WorkPlanInspectionDto>(url, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

     // tslint:disable-next-line:max-line-length
    public msWorkPlanScheduleDetailsEndOnsiteActivities(batchReferenceNo: string, referenceNo: string): Observable<WorkPlanInspectionDto> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_END_ONSITE_ACTIVITIES,
        );
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.get<WorkPlanInspectionDto>(url, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveWorkPlanFiles(data: FormData): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.UPLOAD_WORK_PLAN_FILE,
        );
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveWorkPlanDestructionReportFiles(data: FormData): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_DESTRUCTION_REPORT_UPLOAD,
        );
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveWorkPlanDestructionNotificationFiles(data: FormData): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_DESTRUCTION_NOTIFICATION_UPLOAD,
        );
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanInspectionScheduledAddSampleCollection(batchReferenceNo: string, referenceNo: string, data: SampleCollectionDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_SAMPLE_COLLECT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanInspectionScheduledAddSampleSubmission(batchReferenceNo: string, referenceNo: string, data: SampleSubmissionDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_SAMPLE_SUBMISSION);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msWorkPlanInspectionScheduledAddSampleSubmissionBSNumber
    (batchReferenceNo: string,
     referenceNo: string, data: BSNumberSaveDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_SAMPLE_SUBMISSION_BS_NUMBER);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanInspectionScheduledSaveSSFComplianceStatus(batchReferenceNo: string, referenceNo: string, data: SSFSaveComplianceStatusDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_SSF_COMPLIANCE_STATUS_SAVE);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanInspectionScheduledSavePDFLIMS(batchReferenceNo: string, referenceNo: string, data: PDFSaveComplianceStatusDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_ADD_LAB_RESULTS_PDF_SAVE);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public MsRecommendationListDetails(): Observable<MsRecommendationDto[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_RECOMMENDATION);
        return this.http.get<MsRecommendationDto[]>(url).pipe(
            map(function (response: MsRecommendationDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanInspectionScheduledUpdateAssignHOFDetails(batchReferenceNo: string, referenceNo: string, data: ComplaintAssignDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_UPDATE_ASSIGN_HOF);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msWorkPlanInspectionScheduledUpdateAssignIODetails(batchReferenceNo: string, referenceNo: string, data: ComplaintAssignDto): Observable<WorkPlanInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.INSPECTION_SCHEDULED_UPDATE_ASSIGN_IO);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<WorkPlanInspectionDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }









    // tslint:disable-next-line:max-line-length
    /*******************************************************************START OF COMPLAINT*****************************************************************************/


    public msDepartmentListDetails(): Observable<MsDepartment[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_DEPARTMENTS);
        return this.http.get<MsDepartment[]>(url).pipe(
            map(function (response: MsDepartment[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msCountriesListDetails(): Observable<CountriesEntityDto[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_COUNTRIES);
        return this.http.get<CountriesEntityDto[]>(url).pipe(
            map(function (response: CountriesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msDivisionListDetails(): Observable<MsDivisionDetails[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_DIVISIONS);
        return this.http.get<MsDivisionDetails[]>(url).pipe(
            map(function (response: MsDivisionDetails[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductListDetails(): Observable<MsProducts[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_PRODUCTS);
        return this.http.get<MsProducts[]>(url).pipe(
            map(function (response: MsProducts[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductCategoryListDetails(): Observable<MsProductCategories[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_PRODUCT_CATEGORIES);
        return this.http.get<MsProductCategories[]>(url).pipe(
            map(function (response: MsProductCategories[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductBroadCategoryListDetails(): Observable<MsBroadProductCategory[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_BROAD_PRODUCT_CATEGORY);
        return this.http.get<MsBroadProductCategory[]>(url).pipe(
            map(function (response: MsBroadProductCategory[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductSubCategoryListDetails(): Observable<MsProductSubcategory[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_PRODUCT_SUB_CATEGORY);
        return this.http.get<MsProductSubcategory[]>(url).pipe(
            map(function (response: MsProductSubcategory[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductStandardCategoryListDetails(): Observable<MsStandardProductCategory[]> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_STANDARD_PRODUCT_CATEGORY);
        return this.http.get<MsStandardProductCategory[]>(url).pipe(
            map(function (response: MsStandardProductCategory[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public createNewComplaint(newComplaintDto: NewComplaintDto): Observable<MSComplaintSubmittedSuccessful> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.CREATE_NEW_COMPLAINT);
        return this.http.post<MSComplaintSubmittedSuccessful>(url, newComplaintDto).pipe(
            map(function (response: MSComplaintSubmittedSuccessful) {
                return response;
            })
            , catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.error} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msAddComplaintDetailsToWorkPlanScheduleDetails(referenceNo: string, data: WorkPlanEntityDto): Observable<WorkPlanScheduleListDetailsDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.CREATE_NEW_WORK_PLAN_SCHEDULE);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.post<WorkPlanScheduleListDetailsDto>(url, data, {params}).pipe(
            map(function (response: WorkPlanScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public saveComplaintFiles(data: FormData): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.UPLOAD_COMPLIANT_FILE);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveComplaintWithNoFiles(data: FormData): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.CREATE_NEW_COMPLAINT);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadMSComplaintList(page: string, records: string, routeTake: string): Observable<ApiResponseModel> {
        // console.log(data);
        let url = null;
        switch (routeTake) {
            case 'my-tasks':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.MY_TASK_COMPLAINT_LIST);
                break;
            case 'on-going':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.ONGOING_COMPLAINT_LIST);
                break;
            case 'new-complaint':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.NEW_COMPLAINT_LIST);
                break;
            case 'completed':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLETED_COMPLAINT_LIST);
                break;
        }

        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<ApiResponseModel>(url, {params}).pipe(
            map(function (response: ApiResponseModel) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public msComplaintDetails(referenceNo: string): Observable<AllComplaintsDetailsDto> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.get<AllComplaintsDetailsDto>(url, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateAcceptanceDetails(referenceNo: string, data: ComplaintApproveDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_ACCEPTANCE);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateRejectDetails(referenceNo: string, data: ComplaintRejectDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_REJECTION);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateMandateOGADetails(referenceNo: string, data: ComplaintAdviceRejectDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_OGA_MANDATE);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateForAmendmentDetails(referenceNo: string, data: ComplaintAdviceRejectDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_REJECT_FOR_AMENDMENT);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateAssignHOFDetails(referenceNo: string, data: ComplaintAssignDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_ASSIGN_HOF);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateReAssignRegionDetails(referenceNo: string, data: RegionReAssignDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_RE_ASSIGN_REGION);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateAssignIODetails(referenceNo: string, data: ComplaintAssignDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_ASSIGN_IO);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateSaveClassificationDetails(
        referenceNo: string,
        data: ComplaintClassificationDto,
    ): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_ADD_CLASSIFICATION_DETAILS);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    /*******************************************************************START OF FUEL SURVEILLANCE*****************************************************************************/

    public loadMSFuelBatchList(page: string, records: string): Observable<FuelBatchDetailsDto[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.ALL_BATCH_LIST);
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelBatchDetailsDto[]>(url, {params}).pipe(
            map(function (response: FuelBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadMSLabList(): Observable<LaboratoryDto[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.LAB_LIST);
        return this.http.get<LaboratoryDto[]>(url).pipe(
            map(function (response: LaboratoryDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadFileDetailsPDF(fileID: string): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MS_COMMON_VIEW_PDF_CONTEXT);
        const params = new HttpParams()
            .set('fileID', fileID);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadFileDetailsLabResultsPDF(fileName: string, bsNumber: string): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_LAB_RESULT);
        const params = new HttpParams()
            .set('fileName', fileName)
            .set('bsNumber', bsNumber);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadRemediationInvoiceDetailsPDF(fuelInspectionId: string): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_REMEDIATION_INVOICE);
        const params = new HttpParams()
            .set('fuelInspectionId', fuelInspectionId);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadSampleCollectionDetailsPDF(sampleCollectionID: string): Observable<any> {
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_SAMPLE_COLLECTION);
        const params = new HttpParams()
            .set('sampleCollectionID', sampleCollectionID);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }




    public addNewMSFuelBatch(data: BatchFileFuelSaveDto): Observable<FuelInspectionScheduleListDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.ADD_BATCH);
        return this.http.post<FuelInspectionScheduleListDetailsDto>(url, data).pipe(
            map(function (response: FuelInspectionScheduleListDetailsDto) {
                return response;
            })
            , catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.error} )`);
                return throwError(fault);
            }),
        );
    }

    public closeMSFuelBatch(referenceNo: string): Observable<FuelBatchDetailsDto[]> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.CLOSE_BATCH);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<FuelBatchDetailsDto[]>(url, null, {params}).pipe(
            map(function (response: FuelBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionTeamsList(batchReferenceNo: string, page: string, records: string): Observable<FuelScheduleTeamsListDetailsDto> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_TEAMS_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelScheduleTeamsListDetailsDto>(url, {params}).pipe(
            map(function (response: FuelScheduleTeamsListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionTeamCountyList(batchReferenceNo: string, teamsReferenceNo: string, page: string, records: string): Observable<FuelScheduleCountyListDetailsDto> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_TEAM_COUNTY_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelScheduleCountyListDetailsDto>(url, {params}).pipe(
            map(function (response: FuelScheduleCountyListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionList(batchReferenceNo: string, teamsReferenceNo: string, countyReferenceNo: string, page: string, records: string): Observable<FuelInspectionScheduleListDetailsDto> {
        // console.log(data);

        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo)
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelInspectionScheduleListDetailsDto>(url, {params}).pipe(
            map(function (response: FuelInspectionScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionAddSchedule(batchReferenceNo: string, teamsReferenceNo: string, countyReferenceNo: string, data: FuelEntityDto): Observable<FuelInspectionScheduleListDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_ADD_NEW);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionScheduleListDetailsDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledDetails(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
    ): Observable<FuelInspectionDto> {
        // console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.get<FuelInspectionDto>(url, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveFuelFiles(data: FormData): Observable<FuelInspectionDto> {
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.UPLOAD_FUEL_FILE,
        );
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<FuelInspectionDto>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledAssignOfficer(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string ,
        data: FuelEntityAssignOfficerDto,
    ): Observable<FuelInspectionDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_ASSIGN_OFFICER);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledRapidTest(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: FuelEntityRapidTestDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_RAPID_TEST);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledRapidTestProducts(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: RapidTestProductsDto): Observable<FuelInspectionDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_RAPID_TEST_PRODUCTS);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledAddSampleCollection(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: SampleCollectionDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_COLLECT);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledAddSampleSubmission(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: SampleSubmissionDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledAddTeamsAndCounty(batchReferenceNo: string, data: TeamsFuelSaveDto): Observable<FuelScheduleTeamsListDetailsDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_ADD_TEAMS);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo);
        return this.http.post<FuelScheduleTeamsListDetailsDto>(url, data, {params}).pipe(
            map(function (response: FuelScheduleTeamsListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledAddSampleSubmissionBSNumber(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: BSNumberSaveDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION_BS_NUMBER);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledSavePDFLIMS(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: PDFSaveComplianceStatusDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_LAB_RESULTS_SAVE_PDF);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledSaveSSFComplianceStatus(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: SSFSaveComplianceStatusDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SSF_COMPLIANCE_STATUS);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledSaveSSFFinalComplianceStatus(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: SSFSaveComplianceStatusDto): Observable<FuelInspectionDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SSF_FINAL_COMPLIANCE_STATUS);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledRemediation(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: CompliantRemediationDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_REMEDIATION_DATE);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionNotCompliantRemediationInvoice(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: CompliantRemediationDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_REMEDIATION_INVOICE);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionRemediation(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: RemediationDto): Observable<FuelInspectionDto> {
        console.log(data);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_ADD_REMEDIATION);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionEnd(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
        data: EndFuelDto,
    ): Observable<FuelInspectionDto> {
        // console.log(da/ta);
         // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.END_INSPECTION);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionEndSampleSubmissionAdding(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
    ): Observable<FuelInspectionDto> {
        // console.log(da/ta);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.END_SSF_ADDING_INSPECTION);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, null, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionEndSSFAddingBsNumber(
        batchReferenceNumber: string,
        teamsReferenceNo: string,
        countyReferenceNo: string,
        referenceNumber: string,
    ): Observable<FuelInspectionDto> {
        // console.log(da/ta);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.END_SSF_ADDING_BS_NUMBER_INSPECTION);
        const params = new HttpParams()
            .set('referenceNo', referenceNumber)
            .set('batchReferenceNo', batchReferenceNumber)
            .set('teamsReferenceNo', teamsReferenceNo)
            .set('countyReferenceNo', countyReferenceNo);
        return this.http.post<FuelInspectionDto>(url, null, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

}

export class CustomeDateValidators {
    static fromToDate(fromDateField: string, toDateField: string, errorName: string = 'fromToDate'): ValidatorFn {
        return (formGroup: AbstractControl): { [key: string]: boolean } | null => {
            const fromDate = formGroup.get(fromDateField).value;
            const toDate = formGroup.get(toDateField).value;
            // Ausing the fromDate and toDate are numbers. In not convert them first after null check
            if ((fromDate !== null && toDate !== null) && fromDate > toDate) {
                return {[errorName]: true};
            }
            return null;
        };
    }
}
