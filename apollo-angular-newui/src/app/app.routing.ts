import {RouterModule, Routes} from '@angular/router';

import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {DashboardComponent} from './apollowebs/dashboard/dashboard.component';
import {NgModule} from '@angular/core';
import {DmarkComponent} from './apollowebs/quality-assurance/dmark/dmark.component';
import {FmarkallappsComponent} from './apollowebs/quality-assurance/fmarkallapps/fmarkallapps.component';
import {St10FormComponent} from './apollowebs/quality-assurance/st10-form/st10-form.component';
import {RegistrationComponent} from './views/registration.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {ResetCredentialsComponent} from './views/registration/reset-credentials.component';
import {RouteGuard} from './core/route-guard/route.guard';
import {LoginComponent} from './views/registration/login.component';
import {PermitReportComponent} from './apollowebs/permit-report/permit-report.component';
import {NewSmarkPermitComponent} from './apollowebs/quality-assurance/new-smark-permit/new-smark-permit.component';
import {NewDmarkPermitComponent} from './apollowebs/quality-assurance/new-dmark-permit/new-dmark-permit.component';
import {
    DmarkApplicationsAllComponent
} from './apollowebs/quality-assurance/dmark-applications-all/dmark-applications-all.component';
import {InvoiceComponent} from './apollowebs/quality-assurance/invoice/invoice.component';
import {InvoiceDetailsComponent} from './apollowebs/quality-assurance/invoice-details/invoice-details.component';
import {CompaniesList} from './apollowebs/company/companies.list';
import {CompanyComponent} from './apollowebs/company/company.component';
import {BranchComponent} from './apollowebs/company/branch/branch.component';
import {BranchList} from './apollowebs/company/branch/branch.list';
import {UserComponent} from './apollowebs/company/branch/users/user.component';
import {UserList} from './apollowebs/company/branch/users/user.list';
import {
    SmarkApplicationsAllComponent
} from './apollowebs/quality-assurance/smark-applications-all/smark-applications-all.component';
import {UserProfileMainComponent} from './apollowebs/userprofilemain/user-profile-main.component';
import {AddBranchComponent} from './apollowebs/company/branch/add-branch/add-branch.component';
import {OtpComponent} from './views/registration/otp/otp.component';
import {PdfViewComponent} from './pdf-view/pdf-view.component';
import {TaskManagerComponent} from './apollowebs/task-manager/task-manager.component';
import {AddUserComponent} from './apollowebs/company/branch/add-user/add-user.component';
import {
    IsProposalFormComponent
} from './apollowebs/standards-development/international-standard/international-standard-proposal/is-proposal-form/is-proposal-form.component';
import {
    ReviewStandardsComponent
} from './apollowebs/standards-development/systemic-review/request-standard-review/review-standards/review-standards.component';
import {
    CsRequestFormComponent
} from './apollowebs/standards-development/company-standard/company-standard-request/cs-request-form/cs-request-form.component';
import {
    InformationcheckComponent
} from './apollowebs/standards-development/informationcheck/informationcheck.component';
import {
    DivisionresponseComponent
} from './apollowebs/standards-development/divisionresponse/divisionresponse.component';
import {
    MakeEnquiryComponent
} from './apollowebs/standards-development/national-enquiry-point/make-enquiry/make-enquiry.component';
import {
    ComStdRequestListComponent
} from './apollowebs/standards-development/company-standard/com-std-request-list/com-std-request-list.component';
import {
    IntStdResponsesListComponent
} from './apollowebs/standards-development/international-standard/int-std-responses-list/int-std-responses-list.component';
import {
    ComStdJcJustificationComponent
} from './apollowebs/standards-development/company-standard/com-std-jc-justification/com-std-jc-justification.component';
import {
    IntStdJustificationListComponent
} from './apollowebs/standards-development/international-standard/int-std-justification-list/int-std-justification-list.component';
import {
    IntStdCommentsComponent
} from './apollowebs/standards-development/international-standard/int-std-comments/int-std-comments.component';
import {
    SystemicReviewCommentsComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-comments/systemic-review-comments.component';
import {
    IntStdJustificationAppComponent
} from './apollowebs/standards-development/international-standard/int-std-justification-app/int-std-justification-app.component';
import {
    SystemicAnalyseCommentsComponent
} from './apollowebs/standards-development/systemic-review/systemic-analyse-comments/systemic-analyse-comments.component';
import {
    ImportInspectionComponent
} from './apollowebs/pvoc/manufacturer/manufacturer-applications/import-inspection.component';
import {
    ExceptionsApplicationComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/exceptions-application.component';
import {
    ImportationWaiverComponent
} from './apollowebs/pvoc/manufacturer/importation-waiver/importation-waiver.component';
import {
    ConsignmentDocumentListComponent
} from './apollowebs/di/consignment-document-list/consignment-document-list.component';
import {
    ViewSingleConsignmentDocumentComponent
} from './apollowebs/di/view-single-consignment-document/view-single-consignment-document.component';
import {
    MinistryInspectionHomeComponent
} from './apollowebs/di/ministry-inspection-home/ministry-inspection-home.component';
import {
    MotorVehicleInspectionSingleViewComponent
} from './apollowebs/di/motor-vehicle-inspection-single-view/motor-vehicle-inspection-single-view.component';
import {
    NwaJustificationFormComponent
} from './apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component';
import {UsermanagementComponent} from './apollowebs/usermanagement/usermanagement.component';
import {
    UserManagementProfileComponent
} from './apollowebs/usermanagement/user-management-profile/user-management-profile.component';
import {
    RequestStandardFormComponent
} from './apollowebs/standards-development/standard-request/request-standard-form/request-standard-form.component';
import {StandardRequestComponent} from './apollowebs/standards-development/standard-request/standard-request.component';
import {
    StandardTaskComponent
} from './apollowebs/standards-development/standard-request/standard-task/standard-task.component';
import {
    SmarkAllAwardedApplicationsComponent
} from './apollowebs/quality-assurance/smark-all-awarded-applications/smark-all-awarded-applications.component';
import {
    FmarkAllAwardedApplicationsComponent
} from './apollowebs/quality-assurance/fmark-all-awarded-applications/fmark-all-awarded-applications.component';
import {
    DmarkAllAwardedApplicationsComponent
} from './apollowebs/quality-assurance/dmark-all-awarded-applications/dmark-all-awarded-applications.component';
import {QaTaskDetailsComponent} from './apollowebs/quality-assurance/qa-task-details/qa-task-details.component';
import {CompanyViewComponent} from './apollowebs/company/company-view/company-view.component';
import {BranchViewComponent} from './apollowebs/company/branch/branch-view/branch-view.component';
import {QrCodeDetailsComponent} from './apollowebs/quality-assurance/qr-code-details/qr-code-details.component';
import {
    ComStdDraftComponent
} from './apollowebs/standards-development/company-standard/com-std-draft/com-std-draft.component';
import {
    ComStdUploadComponent
} from './apollowebs/standards-development/company-standard/com-std-upload/com-std-upload.component';
import {
    SpcSecTaskComponent
} from './apollowebs/standards-development/standard-request/spc-sec-task/spc-sec-task.component';
import {AllpermitsComponent} from './apollowebs/quality-assurance/allpermits/allpermits.component';
import {NepNotificationComponent} from './apollowebs/standards-development/nep-notification/nep-notification.component';
import {
    ManagernotificationsComponent
} from './apollowebs/standards-development/managernotifications/managernotifications.component';
import {
    CreateDepartmentComponent
} from './apollowebs/standards-development/standard-request/create-department/create-department.component';
import {
    CreatetechnicalcommitteeComponent
} from './apollowebs/standards-development/standard-request/createtechnicalcommittee/createtechnicalcommittee.component';
import {
    IntStdUploadStandardComponent
} from './apollowebs/standards-development/international-standard/int-std-upload-standard/int-std-upload-standard.component';
import {
    IntStdGazzetteComponent
} from './apollowebs/standards-development/international-standard/int-std-gazzette/int-std-gazzette.component';
import {
    CreateproductComponent
} from './apollowebs/standards-development/standard-request/createproduct/createproduct.component';
import {
    CreateproductSubCategoryComponent
} from './apollowebs/standards-development/standard-request/createproduct-sub-category/createproduct-sub-category.component';
import {
    RoleSwitcherComponent
} from './apollowebs/standards-levy/standards-levy-home/role-switcher/role-switcher.component';
import {
    CustomerRegistrationComponent
} from './apollowebs/standards-levy/standards-levy-home/customer-registration/customer-registration.component';
import {
    StandardsLevyHomeComponent
} from './apollowebs/standards-levy/standards-levy-home/standards-levy-home.component';
import {ComStandardLevyComponent} from './apollowebs/standards-levy/com-standard-levy/com-standard-levy.component';
import {
    ComPaymentHistoryComponent
} from './apollowebs/standards-levy/com-payment-history/com-payment-history.component';
import {ComStdLevyFormComponent} from './apollowebs/standards-levy/com-std-levy-form/com-std-levy-form.component';
import {
    StandardLevyDashboardComponent
} from './apollowebs/standards-levy/standard-levy-dashboard/standard-levy-dashboard.component';
import {StandardLevyPaidComponent} from './apollowebs/standards-levy/standard-levy-paid/standard-levy-paid.component';
import {
    StandardLevyPenaltiesComponent
} from './apollowebs/standards-levy/standard-levy-penalties/standard-levy-penalties.component';
import {
    StandardLevyDefaulterComponent
} from './apollowebs/standards-levy/standard-levy-defaulter/standard-levy-defaulter.component';
import {
    StandardLevyPenaltyHistoryComponent
} from './apollowebs/standards-levy/standard-levy-penalty-history/standard-levy-penalty-history.component';
import {
    StandardLevyPaidHistoryComponent
} from './apollowebs/standards-levy/standard-levy-paid-history/standard-levy-paid-history.component';
import {
    StandardLevyDefaulterHistoryComponent
} from './apollowebs/standards-levy/standard-levy-defaulter-history/standard-levy-defaulter-history.component';
import {
    StdTscSecTasksComponentComponent
} from './apollowebs/standards-development/standard-request/std-tsc-sec-tasks-component/std-tsc-sec-tasks-component.component';
import {
    StdTcTasksComponent
} from './apollowebs/standards-development/standard-request/std-tc-tasks/std-tc-tasks.component';
import {
    InvoiceConsolidateComponent
} from './apollowebs/quality-assurance/invoice-consolidate/invoice-consolidate.component';
import {FmarkApplicationComponent} from './apollowebs/quality-assurance/fmark-application/fmark-application.component';
import {SmarkComponent} from './apollowebs/quality-assurance/smark/smark.component';
import {
    ViewDiDeclarationDocumentsComponent
} from './apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-di-declaration-documents.component';
import {
    ViewIdfDocumentDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-idf-document-details/view-idf-document-details.component';
import {
    ItemDetailsComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/item-details/item-details.component';
import {ViewTasksComponent} from './apollowebs/di/view-tasks/view-tasks.component';
import {DiCorComponent} from './apollowebs/di/view-single-consignment-document/di-cor/di-cor.component';
import {DiCocComponent} from './apollowebs/di/view-single-consignment-document/di-coc/di-coc.component';
import {
    ViewInspectionDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/view-inspection-details.component';
import {
    ChecklistDataFormComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/checklist-data-form.component';
import {InspectionDashboardComponent} from './apollowebs/di/inspection-dashboard/inspection-dashboard.component';
import {
    LabResultsComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/lab-results/lab-results.component';
import {
    CurrencyExchangeRatesComponent
} from './apollowebs/di/currency-exchange-rates/currency-exchange-rates.component';
import {MessageDashboardComponent} from './apollowebs/di/message-dashboard/message-dashboard.component';
import {TransactionViewComponent} from './apollowebs/di/transaction-view/transaction-view.component';
import {ViewClientsComponent} from './apollowebs/system/clients/view-clients/view-clients.component';
import {ViewPartnersComponent} from './apollowebs/pvoc/partners/view-partners/view-partners.component';
import {
    ViewPartnerDetailsComponent
} from './apollowebs/pvoc/partners/view-partner-details/view-partner-details.component';
import {IsmApplicationsComponent} from './apollowebs/di/ism/ism-applications/ism-applications.component';
import {ViewIsmApplicationComponent} from './apollowebs/di/ism/view-ism-application/view-ism-application.component';
import {
    ViewCorporateCustomersComponent
} from './apollowebs/invoice/corporate/view-corporate-customers/view-corporate-customers.component';
import {ViewCorporateComponent} from './apollowebs/invoice/corporate/view-corporate/view-corporate.component';
import {ViewBillLimitsComponent} from './apollowebs/invoice/limits/view-bill-limits/view-bill-limits.component';
import {ViewTransactionsComponent} from './apollowebs/invoice/corporate/view-transactions/view-transactions.component';
import {ViewAuctionItemsComponent} from './apollowebs/di/auction/view-auction-items/view-auction-items.component';
import {AuctionItemDetailsComponent} from './apollowebs/di/auction/auction-item-details/auction-item-details.component';

import {EpraBatchListComponent} from './apollowebs/market-surveillance/fuel/epra-batch-list/epra-batch-list.component';
import {EpraListComponent} from './apollowebs/market-surveillance/fuel/epra-list/epra-list.component';
import {
    ViewFuelSheduledDetailsComponent
} from './apollowebs/market-surveillance/fuel/view-fuel-sheduled-details/view-fuel-sheduled-details.component';
import {ComplaintNewComponent} from './apollowebs/market-surveillance/complaint/complaint-new/complaint-new.component';
import {
    ComplaintListComponent
} from './apollowebs/market-surveillance/complaint/complaint-list/complaint-list.component';
import {
    ComplaintDetailsComponent
} from './apollowebs/market-surveillance/complaint/complaint-details/complaint-details.component';
import {ViewComplaintsComponent} from './apollowebs/pvoc/complaints/view-complaints/view-complaints.component';
import {
    ViewComplaintDetailsComponent
} from './apollowebs/pvoc/complaints/view-complaint-details/view-complaint-details.component';
import {
    ViewWaiverApplicationsComponent
} from './apollowebs/pvoc/waivers/view-waiver-applications/view-waiver-applications.component';
import {ViewWaiverDetailsComponent} from './apollowebs/pvoc/waivers/view-waiver-details/view-waiver-details.component';
import {
    ViewExemptionApplicationsComponent
} from './apollowebs/pvoc/exemptions/view-exemption-applications/view-exemption-applications.component';
import {
    ViewExemptionDetailsComponent
} from './apollowebs/pvoc/exemptions/view-exemption-details/view-exemption-details.component';
import {
    StdJustificationComponent
} from './apollowebs/standards-development/standard-request/std-justification/std-justification.component';
import {
    StdTcWorkplanComponent
} from './apollowebs/standards-development/standard-request/std-tc-workplan/std-tc-workplan.component';
import {
    PreparePreliminaryDraftComponent
} from './apollowebs/standards-development/committee-module/prepare-preliminary-draft/prepare-preliminary-draft.component';
import {
    ComStdPlTaskComponent
} from './apollowebs/standards-development/company-standard/com-std-pl-task/com-std-pl-task.component';
import {
    ComStdDraftViewComponent
} from './apollowebs/standards-development/company-standard/com-std-draft-view/com-std-draft-view.component';
import {
    ComStdListComponent
} from './apollowebs/standards-development/company-standard/com-std-list/com-std-list.component';
import {
    StandardLevySiteVisitComponent
} from './apollowebs/standards-levy/standard-levy-site-visit/standard-levy-site-visit.component';
import {
    StandardLevySiteVisitApproveTwoComponent
} from './apollowebs/standards-levy/standard-levy-site-visit-approve-two/standard-levy-site-visit-approve-two.component';
import {
    StandardLevySiteVisitApproveOneComponent
} from './apollowebs/standards-levy/standard-levy-site-visit-approve-one/standard-levy-site-visit-approve-one.component';
import {
    StandardLevySiteVisitFeedbackComponent
} from './apollowebs/standards-levy/standard-levy-site-visit-feedback/standard-levy-site-visit-feedback.component';
import {
    StandardLevyUploadSiteVisitFeedbackComponent
} from './apollowebs/standards-levy/standard-levy-upload-site-visit-feedback/standard-levy-upload-site-visit-feedback.component';
import {
    StandardLevyManufactureDetailsComponent
} from './apollowebs/standards-levy/standard-levy-manufacture-details/standard-levy-manufacture-details.component';
import {
    RequestForFormationOfTCComponent
} from './apollowebs/standards-development/formationOfTc/request-for-formation-of-tc/request-for-formation-of-tc.component';
import {
    ReviewJustificationOfTCComponent
} from './apollowebs/standards-development/formationOfTc/review-justification-of-tc/review-justification-of-tc.component';
import {
    ReviewFeedbackSPCComponent
} from './apollowebs/standards-development/formationOfTc/review-feedback-spc/review-feedback-spc.component';
import {
    ApproveDraftStdComponent
} from './apollowebs/standards-development/publishing/approve-draft-std/approve-draft-std.component';
import {
    StdDraughtsmanComponent
} from './apollowebs/standards-development/publishing/std-draughtsman/std-draughtsman.component';
import {
    StdProofreadComponent
} from './apollowebs/standards-development/publishing/std-proofread/std-proofread.component';
import {EditorTasksComponent} from './apollowebs/standards-development/publishing/editor-tasks/editor-tasks.component';
import {
    StdHopTasksComponent
} from './apollowebs/standards-development/publishing/std-hop-tasks/std-hop-tasks.component';
import {
    StdPublishingComponent
} from './apollowebs/standards-development/publishing/std-publishing/std-publishing.component';
import {
    CallsForApplicationComponent
} from './apollowebs/standards-development/membershipToTc/calls-for-application/calls-for-application.component';
import {
    ReviewRecommendationComponent
} from './apollowebs/standards-development/membershipToTc/review-recommendation/review-recommendation.component';
import {
    ReviewRecommendationOfSpcComponentComponent
} from './apollowebs/standards-development/membershipToTc/review-recommendation-of-spc-component/review-recommendation-of-spc-component.component';
import {
    SubmitApplicationComponent
} from './apollowebs/standards-development/membershipToTc/submit-application/submit-application.component';
import {
    UploadTcMemberComponentComponent
} from './apollowebs/standards-development/membershipToTc/upload-tc-member-component/upload-tc-member-component.component';
import {
    ReviewApplicationComponent
} from './apollowebs/standards-development/membershipToTc/review-application/review-application.component';
import {
    ManifestDocumentComponent
} from './apollowebs/di/view-single-consignment-document/manifest-document/manifest-document.component';
import {
    IncompleteIDFDocumentsComponent
} from './apollowebs/di/message-dashboard/incomplete-idfdocuments/incomplete-idfdocuments.component';
import {
    ReviewApplicationsAcceptedComponent
} from './apollowebs/standards-development/membershipToTc/review-applications-accepted/review-applications-accepted.component';
import {
    ReviewApplicationsRejectedComponent
} from './apollowebs/standards-development/membershipToTc/review-applications-rejected/review-applications-rejected.component';
import {
    ApproveApplicationComponent
} from './apollowebs/standards-development/membershipToTc/approve-application/approve-application.component';
import {
    ApprovedMembersComponent
} from './apollowebs/standards-development/membershipToTc/approved-members/approved-members.component';
import {
    MembersToCreateCredentialsComponent
} from './apollowebs/standards-development/membershipToTc/members-to-create-credentials/members-to-create-credentials.component';
import {
    MembersCreatedCredentialsComponent
} from './apollowebs/standards-development/membershipToTc/members-created-credentials/members-created-credentials.component';
import {
    ApproveInductionComponent
} from './apollowebs/standards-development/membershipToTc/approve-induction/approve-induction.component';
import {
    StdLevyCompleteTasksComponent
} from './apollowebs/standards-levy/std-levy-complete-tasks/std-levy-complete-tasks.component';
import {
    StdLevyPendingTasksComponent
} from './apollowebs/standards-levy/std-levy-pending-tasks/std-levy-pending-tasks.component';
import {
    StdLevyApplicationsComponent
} from './apollowebs/standards-levy/std-levy-applications/std-levy-applications.component';
import {
    UploadSacSummaryComponent
} from './apollowebs/standards-development/adoptionOfEaStds/upload-sac-summary/upload-sac-summary.component';
import {
    ViewSacSummaryComponent
} from './apollowebs/standards-development/adoptionOfEaStds/view-sac-summary/view-sac-summary.component';
import {
    ViewSacSummaryApprovedComponent
} from './apollowebs/standards-development/adoptionOfEaStds/view-sac-summary-approved/view-sac-summary-approved.component';
import {NwaTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-tasks/nwa-tasks.component';
import {
    AdminBusinessManagementComponent
} from './apollowebs/admin/admin-business-management/admin-business-management.component';
import {
    StandardLevyClosureComponent
} from './apollowebs/standards-levy/standard-levy-closure/standard-levy-closure.component';
import {
    StandardLevySuspensionComponent
} from './apollowebs/standards-levy/standard-levy-suspension/standard-levy-suspension.component';
import {PaymentsComponent} from './apollowebs/quality-assurance/payments/payments.component';
import {
    WorkPlanBatchListComponent
} from './apollowebs/market-surveillance/workplan/workplan-batch-list/work-plan-batch-list.component';
import {WorkPlanListComponent} from './apollowebs/market-surveillance/workplan/work-plan-list/work-plan-list.component';
import {
    WorkPlanDetailsComponent
} from './apollowebs/market-surveillance/workplan/work-plan-details/work-plan-details.component';
import {CfsComponent} from "./apollowebs/system/cfs/cfs.component";
import {InspectionFeesComponent} from "./apollowebs/system/inspection-fees/inspection-fees.component";
import {LaboratoriesComponent} from "./apollowebs/system/laboratories/laboratories.component";
import {CustomsOfficeComponent} from "./apollowebs/system/customs-office/customs-office.component";
import {
    ReviewPreliminaryDraftComponent
} from "./apollowebs/standards-development/committee-module/review-preliminary-draft/review-preliminary-draft.component";
import {
    PrepareCommitteeDraftComponent
} from "./apollowebs/standards-development/committee-module/prepare-committee-draft/prepare-committee-draft.component";
import {
    ReviewCommitteeDraftComponent
} from "./apollowebs/standards-development/committee-module/review-committee-draft/review-committee-draft.component";
import {
    PreparePublicReviewDraftComponent
} from "./apollowebs/standards-development/publicReview/prepare-public-review-draft/prepare-public-review-draft.component";
import {
    PublicReviewDraftComponent
} from "./apollowebs/standards-development/publicReview/public-review-draft/public-review-draft.component";
import {ForeignCorsComponent} from "./apollowebs/pvoc/documents/foreign-cors/foreign-cors.component";
import {ForeignCocsComponent} from "./apollowebs/pvoc/documents/foreign-cocs/foreign-cocs.component";
import {ViewCorComponent} from "./apollowebs/pvoc/documents/foreign-cors/view-cor/view-cor.component";
import {
    ViewOtherDocumentsComponent
} from "./apollowebs/pvoc/documents/foreign-cocs/view-other-documents/view-other-documents.component";
import {
    ApproveCommitteeDraftComponent
} from "./apollowebs/standards-development/committee-module/approve-committee-draft/approve-committee-draft.component";
import {
    CommentOnPublicReviewDraftComponent
} from "./apollowebs/standards-development/publicReview/comment-on-public-review-draft/comment-on-public-review-draft.component";
import {
    PrepareBallotingDraftComponent
} from "./apollowebs/standards-development/balloting/prepare-balloting-draft/prepare-balloting-draft.component";
import {
    VoteOnBallotDraftComponent
} from "./apollowebs/standards-development/balloting/vote-on-ballot-draft/vote-on-ballot-draft.component";
import {ComTasksComponent} from "./apollowebs/standards-development/company-standard/com-tasks/com-tasks.component";
import {
    PvocNewComplaintComponent
} from "./apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-new/pvoc-new-complaint.component";
import {
    ViewWaiverCertificatesComponent
} from "./apollowebs/pvoc/manufacturer/view-waiver-certificates/view-waiver-certificates.component";
import {
    ViewExemptionCertificatesComponent
} from "./apollowebs/pvoc/manufacturer/view-exemption-certificates/view-exemption-certificates.component";
import {
    IntStdTasksComponent
} from "./apollowebs/standards-development/international-standard/int-std-tasks/int-std-tasks.component";
import {
    ReviewBallotDraftComponent
} from "./apollowebs/standards-development/balloting/review-ballot-draft/review-ballot-draft.component";
import {
    ManufacturerComplaintListComponent
} from "./apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-list/manufacturer-complaint-list.component";
import {
    ManufacturerComplaintDetailsComponent
} from "./apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-details/manufacturer-complaint-details.component";
import {RfcCocDocumentsComponent} from "./apollowebs/pvoc/documents/rfc-coc-documents/rfc-coc-documents.component";
import {RfcCorDocumentsComponent} from "./apollowebs/pvoc/documents/rfc-cor-documents/rfc-cor-documents.component";

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'dashboard',
        canActivate: [RouteGuard],
        pathMatch: 'prefix',
    },
    // {path: '', redirectTo: 'dashboard', pathMatch: 'full'},

    {
        path: 'register',
        component: RegistrationComponent,
        children: [
            {
                path: '',
                component: SignUpComponent,
            },
            {
                path: 'register',
                component: SignUpComponent,
            },


        ],
        data: {
            title: 'KEBS',
        },
    },
    {
        path: 'login',
        component: RegistrationComponent,

        children: [
            {
                path: '',
                component: LoginComponent,
            },
            {
                path: 'reset',
                component: ResetCredentialsComponent,
            },
            {
                path: 'otp',
                component: OtpComponent,

            },


        ],
        data: {
            title: 'KEBS',
        },
    },
    {
        path: 'qr-code-qa-permit-scan', component: RegistrationComponent,
        children: [{path: '', component: QrCodeDetailsComponent}],
    },
    // {path: '**', component: AdminLayoutComponent},
    {
        path: 'dashboard', component: AdminLayoutComponent,
        canActivate: [RouteGuard]
        ,
        children: [
            {path: '', component: DashboardComponent},
        ],

    },
    {
        path: 'company/companies', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompaniesList}],

    },
    {
        path: 'company', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompanyComponent}],
    },
    {
        path: 'company/view', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompanyViewComponent}],
    },
    {
        path: 'company/branches', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: BranchList}],
    },
    {
        path: 'companies/view/branch', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: BranchViewComponent}],
    },
    {
        path: 'branches', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: BranchList}],

    },
    {
        path: 'branches/add_branch', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: AddBranchComponent}],
    },
    {
        path: 'users/add_users', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: AddUserComponent}],
    },
    {
        path: 'companies/branch', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: BranchComponent}],
    },
    {
        path: 'companies/branches/users', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserList}],
    },
    {
        path: 'company/users', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserList}],
    },
    {
        path: 'companies/branches/user', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserComponent}],
    },


    {
        path: 'permitdetails', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkComponent}],
    },
    {
        canActivate: [RouteGuard],
        path: 'invoice/all_invoice', component: AdminLayoutComponent,
        children: [{path: '', component: InvoiceComponent}],
    },
    {
        path: 'invoiceDetails', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: InvoiceDetailsComponent}],
    },

    {
        path: 'admin',
        canActivate: [RouteGuard],
        component: AdminLayoutComponent,
        children: [
            {
                path: 'user_management',
                component: UsermanagementComponent,
            },
            {
                path: 'business_management',
                component: AdminBusinessManagementComponent,
            },

        ],
    },
    {
        path: 'fmark/fMarkAllApp', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: FmarkallappsComponent}],
    },
    {
        path: 'fmark/all_fmark_awarded', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: FmarkAllAwardedApplicationsComponent}],
    },
    {
        path: 'st10Form', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: St10FormComponent}],
    },
    {
        path: 'permitReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: PermitReportComponent}],
    },
    {
        path: 'smark/newSmarkPermit', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: NewSmarkPermitComponent}],
    },
    {
        path: 'dmark/newDmarkPermit', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: NewDmarkPermitComponent}],
    },
    {
        path: 'userDetails', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserManagementProfileComponent}],
    },
    {
        path: 'dmark/all_dmark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkApplicationsAllComponent}],
    },
    {
        path: 'dmark/all_dmark_awarded', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkAllAwardedApplicationsComponent}],
    },
    {
        path: 'profile', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserProfileMainComponent}],
    },
    {
        path: 'all_my_permits', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: AllpermitsComponent}],
    },
    {
        path: 'smark/all_smark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: SmarkApplicationsAllComponent}],
    },
    {
        path: 'smark/all_smark_awarded', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: SmarkAllAwardedApplicationsComponent}],
    },
    {
        path: 'invoice/consolidate_invoice', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: InvoiceConsolidateComponent}],
    },
    {
        path: 'all_tasks_list', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: TaskManagerComponent}],
    },
    {
        path: 'all_qa_tasks_list', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: QaTaskDetailsComponent}],
    },
    {
        path: 'fmark/application', component: AdminLayoutComponent,
        children: [{path: '', component: FmarkApplicationComponent}],
    },
    {
        path: 'invoice_test', component: PdfViewComponent,
    },

    {
        path: 'smarkpermitdetails', component: AdminLayoutComponent,
        children: [{path: '', component: SmarkComponent}],
    },
    {
        path: 'payments', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: PaymentsComponent}],
    },
    {
        path: 'company',
        component: AdminLayoutComponent,
        children: [
            {
                path: 'applications',
                component: ImportInspectionComponent,
            },
        ],
    },
    {
        path: 'pvoc',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'waiver',
                children: [
                    {
                        path: 'apply',
                        component: ImportationWaiverComponent,
                        pathMatch: 'full',
                    },
                    {
                        path: 'certificates',
                        component: ViewWaiverCertificatesComponent,
                        pathMatch: 'full',
                    },
                    {
                        path: 'applications',
                        component: ViewWaiverApplicationsComponent,
                        pathMatch: 'full',
                    },
                    {
                        path: 'application/details/:id',
                        component: ViewWaiverDetailsComponent,
                    },
                ],
            },
            {
                path: 'exemption',
                children: [
                    {
                        path: 'apply',
                        component: ExceptionsApplicationComponent,
                    },
                    {
                        path: 'certificates',
                        component: ViewExemptionCertificatesComponent,
                    },
                    {
                        path: 'applications',
                        component: ViewExemptionApplicationsComponent,
                    },
                    {
                        path: 'view/:id',
                        component: ViewExemptionDetailsComponent,
                    },
                ],

            },
            {
                path: 'foreign',
                children: [
                    {
                        path: 'cors',
                        component: ForeignCorsComponent,
                    },
                    {
                        path: 'documents/:docType',
                        component: ForeignCocsComponent,
                    },
                    {
                        path: 'documents/:docType/:id',
                        component: ViewOtherDocumentsComponent,
                    },
                    {
                        path: 'document/cor/:id',
                        component: ViewCorComponent,
                    },
                    {
                        path: 'documents/rfc/cor',
                        component: RfcCorDocumentsComponent
                    },
                    {
                        path: 'document/rfc/cor/:id',
                        component: ViewCorComponent,
                    },
                    {
                        path: 'documents/rfc/other',
                        component: RfcCocDocumentsComponent
                    },
                    {
                        path: 'document/rfc/other/:id',
                        component: ViewCorComponent,
                    },
                ],
            },
            {
                path: 'partners',
                children: [
                    {
                        path: '',
                        component: ViewPartnersComponent,
                    },
                    {
                        path: 'view/:id',
                        component: ViewPartnerDetailsComponent,
                    },
                ],
            },
            {
                path: 'complaints',
                children: [
                    {
                        path: '',
                        component: ViewComplaintsComponent,
                    },
                    {
                        path: 'filed',
                        component: ManufacturerComplaintListComponent,
                    },
                    {
                        path: 'filed/:id',
                        component: ManufacturerComplaintDetailsComponent,
                    },
                    {
                        path: 'apply',
                        component: PvocNewComplaintComponent,
                    },
                    {
                        path: ':id',
                        component: ViewComplaintDetailsComponent,
                    },
                ],
            },
        ],
    },
    {
        path: 'tasks',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: ViewTasksComponent,
            },
        ],
    },
    {
        path: 'demand/notes',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: TransactionViewComponent,
            },
        ],
    },
    {
        path: 'di',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: ConsignmentDocumentListComponent,
                pathMatch: 'full',
            },
            {
                path: 'declaration/document/:id',
                component: ViewDiDeclarationDocumentsComponent,
            },
            {
                path: 'manifest/document/:id',
                component: ManifestDocumentComponent,
            },
            {
                path: 'inspection/checklist/:id',
                canActivate: [RouteGuard],
                component: ChecklistDataFormComponent,
            },
            {
                path: 'cor/details/:id',
                canActivate: [RouteGuard],
                component: DiCorComponent,
            },
            {
                path: 'certificate/:docType/details/:id',
                canActivate: [RouteGuard],
                component: DiCocComponent,
            },
            {
                path: 'idf/details/:id',
                canActivate: [RouteGuard],
                component: ViewIdfDocumentDetailsComponent,
            },
            {
                path: 'item/:cdUuid/:id',
                canActivate: [RouteGuard],
                component: ItemDetailsComponent,
            },
            {
                path: 'item/lab-results/:id/:page/:cdUuid',
                canActivate: [RouteGuard],
                component: LabResultsComponent,
            },
            {
                path: ':id',
                canActivate: [RouteGuard],
                component: ViewSingleConsignmentDocumentComponent,
            },
            {
                path: 'version/:id',
                canActivate: [RouteGuard],
                component: ViewSingleConsignmentDocumentComponent,
            },
            {
                path: 'checklist/details/:id',
                canActivate: [RouteGuard],
                component: ViewInspectionDetailsComponent,
            },
            {
                path: 'auction/view',
                canActivate: [RouteGuard],
                component: ViewAuctionItemsComponent,
            },
            {
                path: 'auction/details/:id',
                canActivate: [RouteGuard],
                component: AuctionItemDetailsComponent,
            },
            {
                path: 'kentrade/exchange/messages',
                canActivate: [RouteGuard],
                component: MessageDashboardComponent,
            },
            {
                path: 'kentrade/idf/documents',
                canActivate: [RouteGuard],
                component: IncompleteIDFDocumentsComponent,
            },
            {
                path: 'ism',
                children: [
                    {
                        path: 'requests',
                        canActivate: [RouteGuard],
                        component: IsmApplicationsComponent,
                    },
                    {
                        path: 'request/:id',
                        // canActivate: [RouteGuard],
                        component: ViewIsmApplicationComponent,
                    },
                ],
            },
        ],
    },
    {
        path: 'dashboard',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'inspection',
                canActivate: [RouteGuard],
                component: InspectionDashboardComponent,
            },
        ],
    },
    {
        path: 'transaction',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'demand-notes',
                component: TransactionViewComponent,
            },
            {
                path: 'corporates-customers',
                component: ViewCorporateCustomersComponent,
            },
            {
                path: 'corporate/:id',
                component: ViewCorporateComponent,
            },
            {
                path: 'exchange-rates',
                component: CurrencyExchangeRatesComponent,
            },
            {
                path: 'bill/:cid/:id',
                component: ViewTransactionsComponent,
            },
            {
                path: 'limits',
                component: ViewBillLimitsComponent,
            },
        ],
    },
    {
        path: 'currency',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'rates',
                component: CurrencyExchangeRatesComponent,
            },
        ],
    },
    {
        path: 'system',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'api-clients',
                component: ViewClientsComponent,
            },
            {
                path: 'cfs',
                component: CfsComponent,
            },
            {
                path: 'laboratories',
                component: LaboratoriesComponent,
            },
            {
                path: 'custom/offices',
                component: CustomsOfficeComponent,
            },
            {
                path: 'inspection/fees',
                component: InspectionFeesComponent
            },
        ],
    },
    {
        path: 'ministry',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'inspection',
                canActivate: [RouteGuard],
                component: MinistryInspectionHomeComponent,
            },
            {
                path: 'inspection/:id',
                canActivate: [RouteGuard],
                component: MotorVehicleInspectionSingleViewComponent,
            },
        ],
    },


    // SD Kenya National Workshop Agreement
    {
        path: 'nwaJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaJustificationFormComponent}],
    },
    {
        path: 'nwaTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaTasksComponent}],
    },

    // SD International Standards

    {
        path: 'isTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdTasksComponent}],
    },
    {
        path: 'isProposalForm', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: IsProposalFormComponent}],
    },
    {
        path: 'isProposalComments', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: IntStdCommentsComponent}],
    },
    {
        path: 'isProposalResponses', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdResponsesListComponent}],
    },
    {
        path: 'isJustificationList', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdJustificationListComponent}],
    },
    {
        path: 'isJustificationApp', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdJustificationAppComponent}],
    },
    {
        path: 'isUploadStd', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdUploadStandardComponent}],
    },
    {
        path: 'isUploadNotice', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdGazzetteComponent}],
    },
    // SD SYSTEMIC REVIEW
    {
        path: 'requestStandardReview', component: AdminLayoutComponent,
        children: [{path: '', component: ReviewStandardsComponent}],
    },
    {
        path: 'systemicReviewComments', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewCommentsComponent}],
    },
    {
        path: 'systemicRecommendations', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicAnalyseCommentsComponent}],
    },

    // SD COMPANY STANDARDS
    // {
    //     path: 'comStdRequest', component: AdminLayoutComponent,
    //     children: [{path: '', component: CsRequestFormComponent}]
    // },
    {
        path: 'comStdRequest',
        component: StandardRequestComponent,
        children: [{path: '', component: CsRequestFormComponent}],
    },
    {
        path: 'comStdList', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdRequestListComponent}],
    },

    {
        path: 'comStdJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdJcJustificationComponent}],
    },
    {
        path: 'comPlTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdPlTaskComponent}],
    },
    {
        path: 'comStdDraft', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdDraftComponent}],
    },
    {
        path: 'comStdConfirmation', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdDraftViewComponent}],
    },
    {
        path: 'comStdUpload', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdUploadComponent}],
    },
    {
        path: 'comStdListed',
        component: ComStdListComponent
    },
    // {
    //     path: 'comStdListed', component: AdminLayoutComponent,
    //     children: [{path: '', component: ComStdListComponent}],
    // },
    {
        path: 'comStdTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComTasksComponent}],
    },
    {
        path: 'nep_information_received', component: AdminLayoutComponent,
        children: [{path: '', component: InformationcheckComponent}],
    },
    {
        path: 'nep_division_response', component: AdminLayoutComponent,
        children: [{path: '', component: DivisionresponseComponent}],
    },
    {
        path: 'managernotification', component: AdminLayoutComponent,
        children: [{path: '', component: ManagernotificationsComponent}],
    },
    {
        path: 'nepnotification', component: AdminLayoutComponent,
        children: [{path: '', component: NepNotificationComponent}],
    },
    {
        path: 'make_enquiry', component: MakeEnquiryComponent,
    },


    //  Request For Standards
    {
        path: 'request-standards',
        component: StandardRequestComponent,
        children: [{path: '', component: RequestStandardFormComponent}],
    },
    {
        // Review Standard Requests
        path: 'ms-standards', component: AdminLayoutComponent,
        children: [{path: '', component: StandardTaskComponent}],
    },
    {
        // Prepare New Work Item
        path: 'std-tsc-sec-task', component: AdminLayoutComponent,
        children: [{path: '', component: StdTscSecTasksComponentComponent}],
    },
    {
        // Vote On New Work Item
        path: 'std-tc-task', component: AdminLayoutComponent,
        children: [{path: '', component: StdTcTasksComponent}],
    },
    {
        // Upload Justification
        path: 'upload-justification', component: AdminLayoutComponent,
        children: [{path: '', component: StdJustificationComponent}],
    },

    // Decision On Justification
    {
        path: 'decision-justification', component: AdminLayoutComponent,
        children: [{path: '', component: SpcSecTaskComponent}],
    },
    // Upload WorkPlan
    {
        path: 'upload-workplan', component: AdminLayoutComponent,
        children: [{path: '', component: StdTcWorkplanComponent}],
    },
    {
        path: 'department', component: AdminLayoutComponent,
        children: [{path: '', component: CreateDepartmentComponent}],
    },
    {
        path: 'technicalCommittee', component: AdminLayoutComponent,
        children: [{path: '', component: CreatetechnicalcommitteeComponent}],
    },
    {
        path: 'productCategory', component: AdminLayoutComponent,
        children: [{path: '', component: CreateproductComponent}],
    },
    {
        path: 'productSubCategory', component: AdminLayoutComponent,
        children: [{path: '', component: CreateproductSubCategoryComponent}],
    },

    /****************************************************************
     * STANDARD DEVELOPMENT - COMMITTEE MODULE ROUTES
     ***************************************************************/

    {
        // prepare Preliminary Draft
        path: 'preparePd', component: AdminLayoutComponent,
        children: [{path: '', component: PreparePreliminaryDraftComponent}],
    },
    {
        // review Preliminary Draft
        path: 'reviewPd', component: AdminLayoutComponent,
        children: [{path: '', component: ReviewPreliminaryDraftComponent}],
    },
    {
        // prepare Committee Draft
        path: 'prepareCd', component: AdminLayoutComponent,
        children: [{path: '', component: PrepareCommitteeDraftComponent}],
    },
    {
        // review Committee Draft
        path: 'reviewCd', component: AdminLayoutComponent,
        children: [{path: '', component: ReviewCommitteeDraftComponent}],
    },

    {
        // approve Committee Draft
        path: 'approveCD', component: AdminLayoutComponent,
        children: [{path: '', component: ApproveCommitteeDraftComponent}],
    },


    /****************************************************************
     * END OF STANDARD DEVELOPMENT - COMMITTEE MODULE ROUTES
     ***************************************************************/
    /****************************************************************
     * STANDARD DEVELOPMENT - PUBLIC REVIEW ROUTES
     ***************************************************************/

    {
        // prepare Public Review Draft
        path: 'preparePrd', component: AdminLayoutComponent,
        children: [{path: '', component: PreparePublicReviewDraftComponent}],
    },


    {
        // view Public Review Draft
        path: 'viewPrd', component: AdminLayoutComponent,
        children: [{path: '', component: PublicReviewDraftComponent}],
    },

    {
        // view Public Review Draft
        path: 'commentOnPrd', component: AdminLayoutComponent,
        children: [{path: '', component: CommentOnPublicReviewDraftComponent}],
    },
    /****************************************************************
     * END OF STANDARD DEVELOPMENT - PUBLIC REVIEW MODULE ROUTES
     ***************************************************************/

    /****************************************************************
     * STANDARD DEVELOPMENT - BALLOTING ROUTES
     ***************************************************************/

    {
        // prepare Ballot Draft
        path: 'prepareBallot', component: AdminLayoutComponent,
        children: [{path: '', component: PrepareBallotingDraftComponent}],
    },

    {
        // vote on Ballot Draft
        path: 'voteOnBallot', component: AdminLayoutComponent,
        children: [{path: '', component: VoteOnBallotDraftComponent}],
    },

    {
        // review on Ballot Draft
        path: 'reviewBallotDraft', component: AdminLayoutComponent,
        children: [{path: '', component: ReviewBallotDraftComponent}],
    },

    /****************************************************************
     * END OF STANDARD DEVELOPMENT - PUBLIC REVIEW MODULE ROUTES
     ***************************************************************/




    /****************************************************************
     * PUBLISHING ROUTES
     ***************************************************************/
    {
        path: 'draftStandard',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: StdPublishingComponent}],
    },
    {
        path: 'hopTasks',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: StdHopTasksComponent}],
    },
    {
        path: 'editorTasks',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: EditorTasksComponent}],
    },
    {
        path: 'proofReaderTasks',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: StdProofreadComponent}],
    },
    {
        path: 'draughtsmanTasks',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: StdDraughtsmanComponent}],
    },
    {
        path: 'hopApproval',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ApproveDraftStdComponent}],
    },
    /****************************************************************
     * FORMATION OF TECHNICAL COMMITTEE
     ***************************************************************/
    {
        path: 'requestForFormationOfTC',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: RequestForFormationOfTCComponent}],
    },
    {
        path: 'reviewJustificationOfTC',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewJustificationOfTCComponent}],
    },
    {
        path: 'reviewFeedbackSPC',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewFeedbackSPCComponent}],
    },
    /****************************************************************
     * END OF STANDARD DEVELOPMENT - FORMATION OF TECHNICAL COMMITTEE ROUTES
     ***************************************************************/
    /****************************************************************
     * MEMBERSHIP OF TECHNICAL COMMITTEE
     ***************************************************************/
    {
        path: 'callsForApplication',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: CallsForApplicationComponent}],
    },
    {
        path: 'submitApplication',
        component: SubmitApplicationComponent,
    },
    {
        path: 'reviewApplication',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewApplicationComponent}],
    },
    {
        path: 'reviewRecommendation',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewRecommendationComponent}],
    },
    {
        path: 'reviewRecommendationOfSpc',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewRecommendationOfSpcComponentComponent}],
    },
    {
        path: 'reviewAccepted',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewApplicationsAcceptedComponent}],
    },
    {
        path: 'reviewRejected',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewApplicationsRejectedComponent}],
    },

    {
        path: 'approveApplication',
        component: StandardRequestComponent,
        children: [{path: '', component: ApproveApplicationComponent}],
    },
    {
        path: 'approvedMembers',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ApprovedMembersComponent}],
    },
    {
        path: 'createCredentials',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: MembersToCreateCredentialsComponent}],
    },
    {
        path: 'sendInductionEmail',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: MembersCreatedCredentialsComponent}],
    },
    {
        path: 'getInduction',
        component: StandardRequestComponent,
        children: [{path: '', component: ApproveInductionComponent}],
    },
    {
        path: 'sendNotice',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: MembersCreatedCredentialsComponent}],
    },

    {
        path: 'uploadTcMember',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: UploadTcMemberComponentComponent}],
    },
    /****************************************************************
     * END OF STANDARD DEVELOPMENT - MEMBERSHIP OF TECHNICAL COMMITTEE
     ***************************************************************/

    /****************************************************************
     * ADOPTION OF EA STANDARDS
     ***************************************************************/
    {
        path: 'uploadSacSummary',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: UploadSacSummaryComponent}],
    },
    {
        path: 'viewSacSummary',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ViewSacSummaryComponent}],
    },
    {
        path: 'viewSacSummaryApproved',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ViewSacSummaryApprovedComponent}],
    },

    /****************************************************************
     * END OF STANDARD DEVELOPMENT - ADOPTION OF EA STANDARDS
     ***************************************************************/





    // STANDARDS LEVY
    {
        path: 'roleSwitcher', component: StandardsLevyHomeComponent,
        children: [{path: '', component: RoleSwitcherComponent}],
    },
    {
        path: 'standardsLevy/levyRegistration', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CustomerRegistrationComponent}],
    },
    {
        path: 'comStdLevy', component: AdminLayoutComponent,
        children: [{path: '', component: ComStandardLevyComponent}],
    },
    {
        path: 'comPaymentHistory', component: AdminLayoutComponent,
        children: [{path: '', component: ComPaymentHistoryComponent}],
        // No data
    },
    {
        path: 'comStdLevyForm', component: AdminLayoutComponent,
        children: [{path: '', component: ComStdLevyFormComponent}],
    },
    {
        path: 'stdLevyHome', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyDashboardComponent}],
    },

    // {
    //     path: 'epra', component: AdminLayoutComponent,
    //     canActivate: [RouteGuard],
    //     children: [{path: '', component: EpraBatchListComponent}],
    // },
    // {
    //     path: 'epra/:referenceNumber', component: AdminLayoutComponent,
    //     canActivate: [RouteGuard],
    //     children: [{path: '', component: EpraListComponent}],
    // },
    // {
    //     path: 'epra/fuelInspection/details/:referenceNumber/:batchReferenceNumber', component: AdminLayoutComponent,
    //     canActivate: [RouteGuard],
    //     children: [{path: '', component: ViewFuelSheduledDetailsComponent}],
    // },
    {
        path: 'epra',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: EpraBatchListComponent,
                pathMatch: 'full',
            },
            {
                path: ':referenceNumber',
                component: EpraListComponent,
            },
            {
                path: 'details/:referenceNumber/:batchReferenceNumber',
                component: ViewFuelSheduledDetailsComponent,
            },
        ],
    },
    {
        path: 'workPlan',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: WorkPlanBatchListComponent,
                pathMatch: 'full',
            },
            {
                path: ':referenceNumber',
                component: WorkPlanListComponent,
            },
            {
                path: 'details/:referenceNumber/:batchReferenceNumber',
                component: WorkPlanDetailsComponent,
            },
        ],
    },
    {
        path: 'complaint',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: ComplaintListComponent,
            },
            {
                path: 'details/:referenceNumber',
                component: ComplaintDetailsComponent,
            },
            // ,
            // {
            //     path: 'fuelInspection/details/:referenceNumber/:batchReferenceNumber',
            //     component: ViewFuelSheduledDetailsComponent
            // }
        ],
    },
    {
        path: 'complain',
        component: RegistrationComponent,
        children: [
            {
                path: '',
                component: ComplaintNewComponent,
            },
            // ,
            // {
            //     path: 'complain',
            //     component: ComplaintNewComponent
            // }

        ],
        data: {
            title: 'KEBS',
        },
    },
    {
        path: 'stdLevyPenalties', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPenaltiesComponent}],
    },
    {
        path: 'stdLevyPaid', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPaidComponent}],
    },
    {
        path: 'stdLevyDefaulters', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyDefaulterComponent}],
        // no data
    },
    {
        path: 'stdLevyPaidHistory', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPaidHistoryComponent}],
        // no data
    },
    {
        path: 'stdLevyPenaltiesHistory', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPenaltyHistoryComponent}],
    },
    {
        path: 'stdLevyDefaultersHistory', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyDefaulterHistoryComponent}],
    },
    {
        path: 'viewSiteVisits', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevySiteVisitComponent}],
    },
    {
        path: 'viewSiteVisitsReports', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevySiteVisitApproveOneComponent}],
    },
    {
        path: 'viewSiteVisitsReportTwo', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevySiteVisitApproveTwoComponent}],
    },
    {
        path: 'uploadVisitFeedBack', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyUploadSiteVisitFeedbackComponent}],
    },
    {
        path: 'viewSiteVisitsFeedBack', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevySiteVisitFeedbackComponent}],
    },
    {
        path: 'slManufacturers', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyManufactureDetailsComponent}],
    },

    {
        path: 'slApplications', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StdLevyApplicationsComponent}],
    },
    {
        path: 'slPendingTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StdLevyPendingTasksComponent}],
    },
    {
        path: 'slCompleteTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StdLevyCompleteTasksComponent}],
    },
    {
        path: 'slCompanySuspension', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevySuspensionComponent}],
    },
    {
        path: 'slCompanyClosure', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyClosureComponent}],
    },

    /****************MS COMPONENTS ENDS HERE**********************************/

];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
