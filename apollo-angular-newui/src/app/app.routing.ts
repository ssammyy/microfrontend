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
    DmarkApplicationsAllComponent,
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
    SmarkApplicationsAllComponent,
} from './apollowebs/quality-assurance/smark-applications-all/smark-applications-all.component';
import {UserProfileMainComponent} from './apollowebs/userprofilemain/user-profile-main.component';
import {AddBranchComponent} from './apollowebs/company/branch/add-branch/add-branch.component';
import {OtpComponent} from './views/registration/otp/otp.component';
import {PdfViewComponent} from './pdf-view/pdf-view.component';
import {TaskManagerComponent} from './apollowebs/task-manager/task-manager.component';
import {AddUserComponent} from './apollowebs/company/branch/add-user/add-user.component';
import {
    IsProposalFormComponent,
} from './apollowebs/standards-development/international-standard/international-standard-proposal/is-proposal-form/is-proposal-form.component';
import {
    ReviewStandardsComponent,
} from './apollowebs/standards-development/systemic-review/request-standard-review/review-standards/review-standards.component';
import {
    CsRequestFormComponent,
} from './apollowebs/standards-development/company-standard/company-standard-request/cs-request-form/cs-request-form.component';
import {
    InformationcheckComponent,
} from './apollowebs/standards-development/informationcheck/informationcheck.component';
import {
    DivisionresponseComponent,
} from './apollowebs/standards-development/divisionresponse/divisionresponse.component';
import {
    MakeEnquiryComponent,
} from './apollowebs/standards-development/national-enquiry-point/make-enquiry/make-enquiry.component';
import {
    ComStdRequestListComponent,
} from './apollowebs/standards-development/company-standard/com-std-request-list/com-std-request-list.component';
import {
    IntStdResponsesListComponent,
} from './apollowebs/standards-development/international-standard/int-std-responses-list/int-std-responses-list.component';
import {
    ComStdJcJustificationComponent,
} from './apollowebs/standards-development/company-standard/com-std-jc-justification/com-std-jc-justification.component';
import {
    IntStdJustificationListComponent,
} from './apollowebs/standards-development/international-standard/int-std-justification-list/int-std-justification-list.component';
import {
    IntStdCommentsComponent,
} from './apollowebs/standards-development/international-standard/international-standard-proposal/int-std-comments/int-std-comments.component';
import {
    SystemicReviewCommentsComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-comments/systemic-review-comments.component';
import {
    IntStdJustificationAppComponent,
} from './apollowebs/standards-development/international-standard/int-std-justification-app/int-std-justification-app.component';
import {
    SystemicAnalyseCommentsComponent,
} from './apollowebs/standards-development/systemic-review/systemic-analyse-comments/systemic-analyse-comments.component';
import {
    ImportInspectionComponent,
} from './apollowebs/pvoc/manufacturer/manufacturer-applications/import-inspection.component';
import {
    ExceptionsApplicationComponent,
} from './apollowebs/pvoc/manufacturer/exceptions-application/exceptions-application.component';
import {
    ImportationWaiverComponent,
} from './apollowebs/pvoc/manufacturer/importation-waiver/importation-waiver.component';
import {
    ConsignmentDocumentListComponent,
} from './apollowebs/di/consignment-document-list/consignment-document-list.component';
import {
    ViewSingleConsignmentDocumentComponent,
} from './apollowebs/di/view-single-consignment-document/view-single-consignment-document.component';
import {
    MinistryInspectionHomeComponent,
} from './apollowebs/di/ministry-inspection-home/ministry-inspection-home.component';
import {
    MotorVehicleInspectionSingleViewComponent,
} from './apollowebs/di/motor-vehicle-inspection-single-view/motor-vehicle-inspection-single-view.component';
import {
    NwaJustificationFormComponent,
} from './apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component';
import {UsermanagementComponent} from './apollowebs/usermanagement/usermanagement.component';
import {
    UserManagementProfileComponent,
} from './apollowebs/usermanagement/user-management-profile/user-management-profile.component';
import {
    RequestStandardFormComponent,
} from './apollowebs/standards-development/standard-request/request-standard-form/request-standard-form.component';
import {StandardRequestComponent} from './apollowebs/standards-development/standard-request/standard-request.component';
import {
    StandardTaskComponent,
} from './apollowebs/standards-development/standard-request/standard-task/standard-task.component';
import {
    SmarkAllAwardedApplicationsComponent,
} from './apollowebs/quality-assurance/smark-all-awarded-applications/smark-all-awarded-applications.component';
import {
    FmarkAllAwardedApplicationsComponent,
} from './apollowebs/quality-assurance/fmark-all-awarded-applications/fmark-all-awarded-applications.component';
import {
    DmarkAllAwardedApplicationsComponent,
} from './apollowebs/quality-assurance/dmark-all-awarded-applications/dmark-all-awarded-applications.component';
import {QaTaskDetailsComponent} from './apollowebs/quality-assurance/qa-task-details/qa-task-details.component';
import {CompanyViewComponent} from './apollowebs/company/company-view/company-view.component';
import {BranchViewComponent} from './apollowebs/company/branch/branch-view/branch-view.component';
import {QrCodeDetailsComponent} from './apollowebs/quality-assurance/qr-code-details/qr-code-details.component';
import {
    ComStdDraftComponent,
} from './apollowebs/standards-development/company-standard/com-std-draft/com-std-draft.component';
import {
    ComStdUploadComponent,
} from './apollowebs/standards-development/company-standard/com-std-upload/com-std-upload.component';
import {
    SpcSecTaskComponent,
} from './apollowebs/standards-development/standard-request/spc-sec-task/spc-sec-task.component';
import {AllpermitsComponent} from './apollowebs/quality-assurance/allpermits/allpermits.component';
import {NepNotificationComponent} from './apollowebs/standards-development/nep-notification/nep-notification.component';
import {
    ManagernotificationsComponent,
} from './apollowebs/standards-development/managernotifications/managernotifications.component';
import {
    CreateDepartmentComponent,
} from './apollowebs/standards-development/standard-request/create-department/create-department.component';
import {
    CreatetechnicalcommitteeComponent,
} from './apollowebs/standards-development/standard-request/createtechnicalcommittee/createtechnicalcommittee.component';
import {
    IntStdUploadStandardComponent,
} from './apollowebs/standards-development/international-standard/int-std-upload-standard/int-std-upload-standard.component';
import {
    IntStdGazzetteComponent,
} from './apollowebs/standards-development/international-standard/int-std-gazzette/int-std-gazzette.component';
import {
    CreateproductComponent,
} from './apollowebs/standards-development/standard-request/createproduct/createproduct.component';
import {
    CreateproductSubCategoryComponent,
} from './apollowebs/standards-development/standard-request/createproduct-sub-category/createproduct-sub-category.component';
import {
    RoleSwitcherComponent,
} from './apollowebs/standards-levy/standards-levy-home/role-switcher/role-switcher.component';
import {
    CustomerRegistrationComponent,
} from './apollowebs/standards-levy/standards-levy-home/customer-registration/customer-registration.component';
import {
    StandardsLevyHomeComponent,
} from './apollowebs/standards-levy/standards-levy-home/standards-levy-home.component';
import {ComStandardLevyComponent} from './apollowebs/standards-levy/com-standard-levy/com-standard-levy.component';
import {
    ComPaymentHistoryComponent,
} from './apollowebs/standards-levy/com-payment-history/com-payment-history.component';
import {ComStdLevyFormComponent} from './apollowebs/standards-levy/com-std-levy-form/com-std-levy-form.component';
import {
    StandardLevyDashboardComponent,
} from './apollowebs/standards-levy/standard-levy-dashboard/standard-levy-dashboard.component';
import {StandardLevyPaidComponent} from './apollowebs/standards-levy/standard-levy-paid/standard-levy-paid.component';
import {
    StandardLevyPenaltiesComponent,
} from './apollowebs/standards-levy/standard-levy-penalties/standard-levy-penalties.component';
import {
    StandardLevyDefaulterComponent,
} from './apollowebs/standards-levy/standard-levy-defaulter/standard-levy-defaulter.component';
import {
    StandardLevyPenaltyHistoryComponent,
} from './apollowebs/standards-levy/standard-levy-penalty-history/standard-levy-penalty-history.component';
import {
    StandardLevyPaidHistoryComponent,
} from './apollowebs/standards-levy/standard-levy-paid-history/standard-levy-paid-history.component';
import {
    StandardLevyDefaulterHistoryComponent,
} from './apollowebs/standards-levy/standard-levy-defaulter-history/standard-levy-defaulter-history.component';
import {
    StdTscSecTasksComponentComponent,
} from './apollowebs/standards-development/standard-request/std-tsc-sec-tasks-component/std-tsc-sec-tasks-component.component';
import {
    StdTcTasksComponent,
} from './apollowebs/standards-development/standard-request/std-tc-tasks/std-tc-tasks.component';
import {
    InvoiceConsolidateComponent,
} from './apollowebs/quality-assurance/invoice-consolidate/invoice-consolidate.component';
import {FmarkApplicationComponent} from './apollowebs/quality-assurance/fmark-application/fmark-application.component';
import {SmarkComponent} from './apollowebs/quality-assurance/smark/smark.component';
import {
    ViewDiDeclarationDocumentsComponent,
} from './apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-di-declaration-documents.component';
import {
    ViewIdfDocumentDetailsComponent,
} from './apollowebs/di/view-single-consignment-document/view-idf-document-details/view-idf-document-details.component';
import {
    ItemDetailsComponent,
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/item-details/item-details.component';
import {ViewTasksComponent} from './apollowebs/di/view-tasks/view-tasks.component';
import {DiCorComponent} from './apollowebs/di/view-single-consignment-document/di-cor/di-cor.component';
import {DiCocComponent} from './apollowebs/di/view-single-consignment-document/di-coc/di-coc.component';
import {
    ViewInspectionDetailsComponent,
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/view-inspection-details.component';
import {
    ChecklistDataFormComponent,
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/checklist-data-form.component';
import {InspectionDashboardComponent} from './apollowebs/di/inspection-dashboard/inspection-dashboard.component';
import {
    LabResultsComponent,
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/lab-results/lab-results.component';
import {
    CurrencyExchangeRatesComponent,
} from './apollowebs/di/currency-exchange-rates/currency-exchange-rates.component';
import {MessageDashboardComponent} from './apollowebs/di/other-documents/message-dashboard.component';
import {TransactionViewComponent} from './apollowebs/di/transaction-view/transaction-view.component';
import {ViewClientsComponent} from './apollowebs/system/clients/view-clients/view-clients.component';
import {ViewPartnersComponent} from './apollowebs/pvoc/partners/view-partners/view-partners.component';
import {
    ViewPartnerDetailsComponent,
} from './apollowebs/pvoc/partners/view-partner-details/view-partner-details.component';
import {IsmApplicationsComponent} from './apollowebs/di/ism/ism-applications/ism-applications.component';
import {ViewIsmApplicationComponent} from './apollowebs/di/ism/view-ism-application/view-ism-application.component';
import {
    ViewCorporateCustomersComponent,
} from './apollowebs/invoice/corporate/view-corporate-customers/view-corporate-customers.component';
import {ViewCorporateComponent} from './apollowebs/invoice/corporate/view-corporate/view-corporate.component';
import {ViewBillLimitsComponent} from './apollowebs/invoice/limits/view-bill-limits/view-bill-limits.component';
import {ViewTransactionsComponent} from './apollowebs/invoice/corporate/view-transactions/view-transactions.component';
import {ViewAuctionItemsComponent} from './apollowebs/di/auction/view-auction-items/view-auction-items.component';
import {AuctionItemDetailsComponent} from './apollowebs/di/auction/auction-item-details/auction-item-details.component';

import {EpraBatchListComponent} from './apollowebs/market-surveillance/fuel/epra-batch-list/epra-batch-list.component';
import {EpraListComponent} from './apollowebs/market-surveillance/fuel/epra-list/epra-list.component';
import {
    ViewFuelSheduledDetailsComponent,
} from './apollowebs/market-surveillance/fuel/view-fuel-sheduled-details/view-fuel-sheduled-details.component';
import {ComplaintNewComponent} from './apollowebs/market-surveillance/complaint/complaint-new/complaint-new.component';
import {
    ComplaintListComponent,
} from './apollowebs/market-surveillance/complaint/complaint-list/complaint-list.component';
import {
    ComplaintDetailsComponent,
} from './apollowebs/market-surveillance/complaint/complaint-details/complaint-details.component';
import {ViewComplaintsComponent} from './apollowebs/pvoc/complaints/view-complaints/view-complaints.component';
import {
    ViewComplaintDetailsComponent,
} from './apollowebs/pvoc/complaints/view-complaint-details/view-complaint-details.component';
import {
    ViewWaiverApplicationsComponent,
} from './apollowebs/pvoc/waivers/view-waiver-applications/view-waiver-applications.component';
import {ViewWaiverDetailsComponent} from './apollowebs/pvoc/waivers/view-waiver-details/view-waiver-details.component';
import {
    ViewExemptionApplicationsComponent,
} from './apollowebs/pvoc/exemptions/view-exemption-applications/view-exemption-applications.component';
import {
    ViewExemptionDetailsComponent,
} from './apollowebs/pvoc/exemptions/view-exemption-details/view-exemption-details.component';
import {
    StdJustificationComponent,
} from './apollowebs/standards-development/standard-request/std-justification/std-justification.component';
import {
    StdTcWorkplanComponent,
} from './apollowebs/standards-development/standard-request/std-tc-workplan/std-tc-workplan.component';
import {
    PreparePreliminaryDraftComponent,
} from './apollowebs/standards-development/committee-module/prepare-preliminary-draft/prepare-preliminary-draft.component';
import {
    ComStdPlTaskComponent,
} from './apollowebs/standards-development/company-standard/com-std-pl-task/com-std-pl-task.component';
import {
    ComStdDraftViewComponent,
} from './apollowebs/standards-development/company-standard/com-std-draft-view/com-std-draft-view.component';
import {
    ComStdListComponent,
} from './apollowebs/standards-development/company-standard/com-std-list/com-std-list.component';
import {
    StandardLevySiteVisitComponent,
} from './apollowebs/standards-levy/standard-levy-site-visit/standard-levy-site-visit.component';
import {
    StandardLevySiteVisitApproveTwoComponent,
} from './apollowebs/standards-levy/standard-levy-site-visit-approve-two/standard-levy-site-visit-approve-two.component';
import {
    StandardLevySiteVisitApproveOneComponent,
} from './apollowebs/standards-levy/standard-levy-site-visit-approve-one/standard-levy-site-visit-approve-one.component';
import {
    StandardLevySiteVisitFeedbackComponent,
} from './apollowebs/standards-levy/standard-levy-site-visit-feedback/standard-levy-site-visit-feedback.component';
import {
    StandardLevyUploadSiteVisitFeedbackComponent,
} from './apollowebs/standards-levy/standard-levy-upload-site-visit-feedback/standard-levy-upload-site-visit-feedback.component';
import {
    StandardLevyManufactureDetailsComponent,
} from './apollowebs/standards-levy/standard-levy-manufacture-details/standard-levy-manufacture-details.component';
import {
    RequestForFormationOfTCComponent,
} from './apollowebs/standards-development/formationOfTc/request-for-formation-of-tc/request-for-formation-of-tc.component';
import {
    ReviewJustificationOfTCComponent,
} from './apollowebs/standards-development/formationOfTc/review-justification-of-tc/review-justification-of-tc.component';
import {
    ReviewFeedbackSacComponent,
} from './apollowebs/standards-development/formationOfTc/review-feedback-sac/review-feedback-sac.component';
import {
    ApproveDraftStdComponent,
} from './apollowebs/standards-development/publishing/approve-draft-std/approve-draft-std.component';
import {
    StdDraughtsmanComponent,
} from './apollowebs/standards-development/publishing/std-draughtsman/std-draughtsman.component';
import {
    StdProofreadComponent,
} from './apollowebs/standards-development/publishing/std-proofread/std-proofread.component';
import {EditorTasksComponent} from './apollowebs/standards-development/publishing/editor-tasks/editor-tasks.component';
import {
    StdHopTasksComponent,
} from './apollowebs/standards-development/publishing/std-hop-tasks/std-hop-tasks.component';
import {
    StdPublishingComponent,
} from './apollowebs/standards-development/publishing/std-publishing/std-publishing.component';
import {
    CallsForApplicationComponent,
} from './apollowebs/standards-development/membershipToTc/calls-for-application/calls-for-application.component';
import {
    ReviewRecommendationComponent,
} from './apollowebs/standards-development/membershipToTc/review-recommendation/review-recommendation.component';
import {
    ReviewRecommendationOfSpcComponentComponent,
} from './apollowebs/standards-development/membershipToTc/review-recommendation-of-spc-component/review-recommendation-of-spc-component.component';
import {
    SubmitApplicationComponent,
} from './apollowebs/standards-development/membershipToTc/submit-application/submit-application.component';
import {
    UploadTcMemberComponentComponent,
} from './apollowebs/standards-development/membershipToTc/upload-tc-member-component/upload-tc-member-component.component';
import {
    ReviewApplicationComponent,
} from './apollowebs/standards-development/membershipToTc/review-application/review-application.component';
import {
    ManifestDocumentComponent,
} from './apollowebs/di/view-single-consignment-document/manifest-document/manifest-document.component';
import {
    IncompleteIDFDocumentsComponent,
} from './apollowebs/di/other-documents/idf-documents/incomplete-idfdocuments.component';
import {
    ReviewApplicationsAcceptedComponent,
} from './apollowebs/standards-development/membershipToTc/review-applications-accepted/review-applications-accepted.component';
import {
    ReviewApplicationsRejectedComponent,
} from './apollowebs/standards-development/membershipToTc/review-applications-rejected/review-applications-rejected.component';
import {
    ApproveApplicationComponent,
} from './apollowebs/standards-development/membershipToTc/approve-application/approve-application.component';
import {
    ApprovedMembersComponent,
} from './apollowebs/standards-development/membershipToTc/approved-members/approved-members.component';
import {
    MembersToCreateCredentialsComponent,
} from './apollowebs/standards-development/membershipToTc/members-to-create-credentials/members-to-create-credentials.component';
import {
    MembersCreatedCredentialsComponent,
} from './apollowebs/standards-development/membershipToTc/members-created-credentials/members-created-credentials.component';
import {
    ApproveInductionComponent,
} from './apollowebs/standards-development/membershipToTc/approve-induction/approve-induction.component';
import {
    StdLevyCompleteTasksComponent,
} from './apollowebs/standards-levy/std-levy-complete-tasks/std-levy-complete-tasks.component';
import {
    StdLevyPendingTasksComponent,
} from './apollowebs/standards-levy/std-levy-pending-tasks/std-levy-pending-tasks.component';
import {
    StdLevyApplicationsComponent,
} from './apollowebs/standards-levy/std-levy-applications/std-levy-applications.component';
import {
    UploadSacSummaryComponent,
} from './apollowebs/standards-development/adoptionOfEaStds/upload-sac-summary/upload-sac-summary.component';
import {
    ViewSacSummaryComponent,
} from './apollowebs/standards-development/adoptionOfEaStds/view-sac-summary/view-sac-summary.component';
import {
    ViewSacSummaryApprovedComponent,
} from './apollowebs/standards-development/adoptionOfEaStds/view-sac-summary-approved/view-sac-summary-approved.component';
import {NwaTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-tasks/nwa-tasks.component';
import {
    AdminBusinessManagementComponent,
} from './apollowebs/admin/admin-business-management/admin-business-management.component';
import {
    StandardLevyClosureComponent,
} from './apollowebs/standards-levy/standard-levy-closure/standard-levy-closure.component';
import {
    StandardLevySuspensionComponent,
} from './apollowebs/standards-levy/standard-levy-suspension/standard-levy-suspension.component';
import {PaymentsComponent} from './apollowebs/quality-assurance/payments/payments.component';
import {
    WorkPlanBatchListComponent,
} from './apollowebs/market-surveillance/workplan/workplan-batch-list/work-plan-batch-list.component';
import {WorkPlanListComponent} from './apollowebs/market-surveillance/workplan/work-plan-list/work-plan-list.component';
import {
    WorkPlanDetailsComponent,
} from './apollowebs/market-surveillance/workplan/work-plan-details/work-plan-details.component';
import {CfsComponent} from './apollowebs/system/cfs/cfs.component';
import {InspectionFeesComponent} from './apollowebs/system/inspection-fees/inspection-fees.component';
import {LaboratoriesComponent} from './apollowebs/system/laboratories/laboratories.component';
import {CustomsOfficeComponent} from './apollowebs/system/customs-office/customs-office.component';
import {
    ReviewPreliminaryDraftComponent,
} from './apollowebs/standards-development/committee-module/review-preliminary-draft/review-preliminary-draft.component';
import {
    PrepareCommitteeDraftComponent,
} from './apollowebs/standards-development/committee-module/prepare-committee-draft/prepare-committee-draft.component';
import {
    ReviewCommitteeDraftComponent,
} from './apollowebs/standards-development/committee-module/review-committee-draft/review-committee-draft.component';
import {
    PreparePublicReviewDraftComponent,
} from './apollowebs/standards-development/publicReview/prepare-public-review-draft/prepare-public-review-draft.component';
import {
    PublicReviewDraftComponent,
} from './apollowebs/standards-development/publicReview/public-review-draft/public-review-draft.component';
import {ForeignCorsComponent} from './apollowebs/pvoc/documents/foreign-cors/foreign-cors.component';
import {ForeignCocsComponent} from './apollowebs/pvoc/documents/foreign-cocs/foreign-cocs.component';
import {ViewCorComponent} from './apollowebs/pvoc/documents/foreign-cors/view-cor/view-cor.component';
import {
    ViewOtherDocumentsComponent,
} from './apollowebs/pvoc/documents/foreign-cocs/view-other-documents/view-other-documents.component';
import {
    ApproveCommitteeDraftComponent,
} from './apollowebs/standards-development/committee-module/approve-committee-draft/approve-committee-draft.component';
import {
    CommentOnPublicReviewDraftComponent,
} from './apollowebs/standards-development/publicReview/comment-on-public-review-draft/comment-on-public-review-draft.component';
import {
    PrepareBallotingDraftComponent,
} from './apollowebs/standards-development/balloting/prepare-balloting-draft/prepare-balloting-draft.component';
import {
    VoteOnBallotDraftComponent,
} from './apollowebs/standards-development/balloting/vote-on-ballot-draft/vote-on-ballot-draft.component';
import {ComTasksComponent} from './apollowebs/standards-development/company-standard/com-tasks/com-tasks.component';
import {
    PvocNewComplaintComponent,
} from './apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-new/pvoc-new-complaint.component';
import {
    ViewWaiverCertificatesComponent,
} from './apollowebs/pvoc/manufacturer/view-waiver-certificates/view-waiver-certificates.component';
import {
    ViewExemptionCertificatesComponent,
} from './apollowebs/pvoc/manufacturer/view-exemption-certificates/view-exemption-certificates.component';
import {
    IntStdTasksComponent,
} from './apollowebs/standards-development/international-standard/int-std-tasks/int-std-tasks.component';
import {
    ReviewBallotDraftComponent,
} from './apollowebs/standards-development/balloting/review-ballot-draft/review-ballot-draft.component';
import {
    ManufacturerComplaintListComponent,
} from './apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-list/manufacturer-complaint-list.component';
import {
    ManufacturerComplaintDetailsComponent,
} from './apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-details/manufacturer-complaint-details.component';
import {RfcCocDocumentsComponent} from './apollowebs/pvoc/documents/rfc-coc-documents/rfc-coc-documents.component';
import {RfcCorDocumentsComponent} from './apollowebs/pvoc/documents/rfc-cor-documents/rfc-cor-documents.component';
import {
    StdLevyManufacturerPenaltyComponent,
} from './apollowebs/standards-levy/std-levy-manufacturer-penalty/std-levy-manufacturer-penalty.component';
import {
    StandardLevyRegisteredFirmsComponent,
} from './apollowebs/standards-levy/standard-levy-registered-firms/standard-levy-registered-firms.component';
// tslint:disable-next-line:max-line-length
import {
    StandardLevyAllPaymentsComponent,
} from './apollowebs/standards-levy/standard-levy-all-payments/standard-levy-all-payments.component';
import {
    StandardLevyPenaltyReportComponent,
} from './apollowebs/standards-levy/standard-levy-penalty-report/standard-levy-penalty-report.component';
// tslint:disable-next-line:max-line-length
import {
    StandardLevyActiveFirmsComponent,
} from './apollowebs/standards-levy/standard-levy-active-firms/standard-levy-active-firms.component';
import {
    StandardLevyDormantFirmsComponent,
} from './apollowebs/standards-levy/standard-levy-dormant-firms/standard-levy-dormant-firms.component';
// tslint:disable-next-line:max-line-length
import {
    StandardLevyClosedFirmsComponent,
} from './apollowebs/standards-levy/standard-levy-closed-firms/standard-levy-closed-firms.component';
import {FuelListTeamsComponent} from './apollowebs/market-surveillance/fuel/fuel-list-teams/fuel-list-teams.component';
import {CorCertificatesComponent} from './apollowebs/certificates/cor-certificates/cor-certificates.component';
import {CocCertificatesComponent} from './apollowebs/certificates/coc-certificates/coc-certificates.component';
import {CoiCertificatesComponent} from './apollowebs/certificates/coi-certificates/coi-certificates.component';
import {NcrCertificatesComponent} from './apollowebs/certificates/ncr-certificates/ncr-certificates.component';
import {
    ManifestDocumentsComponent,
} from './apollowebs/di/other-documents/manifest-documents/manifest-documents.component';
import {
    FuelListTeamsCountyComponent,
} from './apollowebs/market-surveillance/fuel/fuel-list-teams-county/fuel-list-teams-county.component';
import {
    ApplicationsReceivedComponent,
} from './apollowebs/quality-assurance/reports/applications-received/applications-received.component';
import {
    PermitsGrantedComponent,
} from './apollowebs/quality-assurance/reports/permits-granted/permits-granted.component';
import {
    ComplaintPlanBatchListComponent,
} from './apollowebs/market-surveillance/complainWorkPlan/complaint-plan-batch-list/complaint-plan-batch-list.component';
import {
    ComplaintPlanListComponent,
} from './apollowebs/market-surveillance/complainWorkPlan/complaint-plan-list/complaint-plan-list.component';
import {
    ComplaintPlanDetailsComponent,
} from './apollowebs/market-surveillance/complainWorkPlan/complaint-plan-details/complaint-plan-details.component';
import {
    PermitsDeferredComponent,
} from './apollowebs/quality-assurance/reports/permits-deferred/permits-deferred.component';
import {
    PermitsRenewedComponent,
} from './apollowebs/quality-assurance/reports/permits-renewed/permits-renewed.component';
import {
    SamplesSubmittedComponent,
} from './apollowebs/quality-assurance/reports/samples-submitted/samples-submitted.component';
import {
    StandardLevyRejectedChangesComponent,
} from './apollowebs/standards-levy/standard-levy-rejected-changes/standard-levy-rejected-changes.component';
import {
    StandardsForReviewComponent,
} from './apollowebs/standards-development/systemic-review/standards-for-review/standards-for-review.component';
import {
    SystemicReviewTcSecComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-tc-sec/systemic-review-tc-sec.component';
import {
    SystemicReviewSpcSecComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-spc-sec/systemic-review-spc-sec.component';
import {
    SystemicReviewSacSecComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-sac-sec/systemic-review-sac-sec.component';
import {
    SystemicReviewProofReaderComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-proof-reader/systemic-review-proof-reader.component';
import {
    SystemicReviewHopComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-hop/systemic-review-hop.component';
import {
    SystemicReviewEditorComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-editor/systemic-review-editor.component';
import {
    SystemicReviewDraughtsManComponent,
} from './apollowebs/standards-development/systemic-review/systemic-review-draughts-man/systemic-review-draughts-man.component';
import {
    SystemReviewGazetteStandardComponent,
} from './apollowebs/standards-development/systemic-review/system-review-gazette-standard/system-review-gazette-standard.component';
import {
    SystemReviewUpdateGazetteComponent,
} from './apollowebs/standards-development/systemic-review/system-review-update-gazette/system-review-update-gazette.component';
import {NotificationsComponent} from './apollowebs/system/notifications/notifications.component';
import {
    IntStdProposalsComponent,
} from './apollowebs/standards-development/international-standard/int-std-proposals/int-std-proposals.component';
import {
    IntStdApprovedProposalsComponent,
} from './apollowebs/standards-development/international-standard/int-std-approved-proposals/int-std-approved-proposals.component';
import {
    IntStdEditorComponent,
} from './apollowebs/standards-development/international-standard/int-std-editor/int-std-editor.component';
import {
    IntStdCheckRequirementsComponent,
} from './apollowebs/standards-development/international-standard/int-std-check-requirements/int-std-check-requirements.component';
import {
    IntStdEditDraftComponent,
} from './apollowebs/standards-development/international-standard/int-std-edit-draft/int-std-edit-draft.component';
import {
    IntStdDraughtComponent,
} from './apollowebs/standards-development/international-standard/int-std-draught/int-std-draught.component';
import {
    IntStdProofReadComponent,
} from './apollowebs/standards-development/international-standard/int-std-proof-read/int-std-proof-read.component';
import {
    IntStdApproveDraftComponent,
} from './apollowebs/standards-development/international-standard/int-std-approve-draft/int-std-approve-draft.component';
import {
    IntStdEditedDraftComponent,
} from './apollowebs/standards-development/international-standard/int-std-edited-draft/int-std-edited-draft.component';
import {
    IntStdSacApprovalComponent,
} from './apollowebs/standards-development/international-standard/int-std-sac-approval/int-std-sac-approval.component';
import {
    AcknowledgementComponent,
} from './apollowebs/market-surveillance/reports/acknowledgement/acknowledgement.component';
import {
    DeclarationDocumentsComponent,
} from './apollowebs/di/other-documents/declaration-documents/declaration-documents.component';
// import {
//     PrepareDraftComponent
// } from "./apollowebs/standards-development/committee-module/prepare-draft/prepare-draft.component";
import {
    IntStdGazetteComponent
} from "./apollowebs/standards-development/international-standard/int-std-gazette/int-std-gazette.component";
import {
    ComStdDraftCommentComponent
} from "./apollowebs/standards-development/company-standard/company-standard-request/com-std-draft-comment/com-std-draft-comment.component";
import {
    SchemeMembershipFormComponent
} from "./apollowebs/standards-development/schemeMembership/scheme-membership-form/scheme-membership-form.component";
import {
    SchemeMembershipReviewComponent
} from "./apollowebs/standards-development/schemeMembership/scheme-membership-review/scheme-membership-review.component";
import {
    SchemeMembershipSicComponent
} from "./apollowebs/standards-development/schemeMembership/scheme-membership-sic/scheme-membership-sic.component";
import {RegisterTivetComponent} from "./views/registration/register-tivet/register-tivet.component";
import {
    ComplaintMonitoringComponent
} from "./apollowebs/market-surveillance/reports/complaint-monitoring/complaint-monitoring.component";
import {
    FieldInspectionSummaryComponent
} from "./apollowebs/market-surveillance/reports/field-inspection-summary/field-inspection-summary.component";
import {
    WorkplanMonitoringToolComponent
} from "./apollowebs/market-surveillance/reports/workplan-monitoring-tool/workplan-monitoring-tool.component";
import {
    ComStdAppDraftComponent
} from "./apollowebs/standards-development/company-standard/company-standard-request/com-std-app-draft/com-std-app-draft.component";
import {
    IntStdPublishingComponent
} from "./apollowebs/standards-development/international-standard/int-std-publishing/int-std-publishing.component";
import {
    ComStdPublishingComponent
} from "./apollowebs/standards-development/company-standard/com-std-publishing/com-std-publishing.component";
import {
    ComStdEditorComponent
} from "./apollowebs/standards-development/company-standard/com-std-editor/com-std-editor.component";
import {
    SampleSubmittedTimelineComponent
} from './apollowebs/market-surveillance/reports/sample-submitted-timeline/sample-submitted-timeline.component';
import {CorporateBillsComponent} from "./apollowebs/invoice/corporate-bills/corporate-bills.component";
import {
    ViewRfcCocDocumentsComponent
} from "./apollowebs/pvoc/documents/rfc-coc-documents/view-rfc-coc-documents/view-rfc-coc-documents.component";
import {
    ViewRfcCorDocumentsComponent
} from "./apollowebs/pvoc/documents/rfc-cor-documents/view-rfc-cor-documents/view-rfc-cor-documents.component";
import {CompanyListComponent} from './apollowebs/company/company-list/company-list.component';
import {SmarkAdminComponent} from './apollowebs/qualityAssuranceAdmin/smark-admin/smark-admin.component';
import {DmarkAdminComponent} from './apollowebs/qualityAssuranceAdmin/dmark-admin/dmark-admin.component';
import {FmarkAdminComponent} from './apollowebs/qualityAssuranceAdmin/fmark-admin/fmark-admin.component';
import {PermitDetailsComponent} from './apollowebs/qualityAssuranceAdmin/permit-details/permit-details.component';
import {
    ComStdRequestProcessComponent
} from "./apollowebs/standards-development/company-standard/com-std-request-process/com-std-request-process.component";
import {
    HofReviewProposalComponent
} from "./apollowebs/standards-development/formationOfTc/hof-review-proposal/hof-review-proposal.component";
import {ManageTivetComponent} from "./apollowebs/usermanagement/manage-tivet/manage-tivet.component";
import {
    InvoiceConsolidateFmarkComponent
} from "./apollowebs/quality-assurance/invoice-consolidate-fmark/invoice-consolidate-fmark.component";
import {
    InvoiceConsolidateDmarkComponent
} from "./apollowebs/quality-assurance/invoice-consolidate-dmark/invoice-consolidate-dmark.component";
import {
    DmarkExpiredApplicationsComponent
} from './apollowebs/quality-assurance/dmark-expired-applications/dmark-expired-applications.component';
import {
    DmarkRenewalApplicationsComponent
} from './apollowebs/quality-assurance/dmark-renewal-applications/dmark-renewal-applications.component';
import {
    FmarkExpiredApplicationsComponent
} from './apollowebs/quality-assurance/fmark-expired-applications/fmark-expired-applications.component';
import {
    FmarkRenewalApplicationsComponent
} from './apollowebs/quality-assurance/fmark-renewal-applications/fmark-renewal-applications.component';
import {
    SmarkExpiredApplicationsComponent
} from './apollowebs/quality-assurance/smark-expired-applications/smark-expired-applications.component';
import {
    SmarkRenewalApplicationsComponent
} from './apollowebs/quality-assurance/smark-renewal-applications/smark-renewal-applications.component';
import {
    IntStdProposalCommentsComponent
} from "./apollowebs/standards-development/international-standard/int-std-proposal-comments/int-std-proposal-comments.component";
import {
    ComStdDraftCommentsComponent
} from "./apollowebs/standards-development/company-standard/com-std-draft-comments/com-std-draft-comments.component";
import {
    InspectionReportListComponent
} from "./apollowebs/qualityAssuranceAdmin/inspection-report-list/inspection-report-list.component";
import {
    ViewInspectionReportComponent
} from "./apollowebs/qualityAssuranceAdmin/view-inspection-report/view-inspection-report.component";
import {
    PermitDetailsAdminComponent
} from "./apollowebs/qualityAssuranceAdmin/permit-details-admin/permit-details-admin.component";
import {StaDetailsComponent} from "./apollowebs/qualityAssuranceAdmin/sta-details/sta-details.component";
import {InspectionReport} from "./apollowebs/qualityAssuranceAdmin/inspection-report/inspection-report";
import {
    ComStdConfirmComponent
} from "./apollowebs/standards-development/company-standard/com-std-confirm/com-std-confirm.component";
import {
    IntStdListComponent
} from "./apollowebs/standards-development/international-standard/int-std-list/int-std-list.component";
import {
    DelayedProjectsComponent
} from './apollowebs/standards-development/reports/SD REPORTS/delayed-projects/delayed-projects.component';
import {
    NspStatusComponent
} from './apollowebs/standards-development/reports/SD REPORTS/nsp-status/nsp-status.component';
import {
    NonePerformingProjectsComponent
} from './apollowebs/standards-development/reports/SD REPORTS/none-preforming-projects/none-performing-projects.component';
import {
    StandardsApprovalCommitteeComponent
} from './apollowebs/standards-development/reports/SD REPORTS/standards-approval-committee/standards-approval-committee.component';
import {
    TcMemberApplicationComponent
} from './apollowebs/standards-development/reports/SD REPORTS/tc-member-application/tc-member-application.component';
import {
    IndividualWeeklyReportComponent
} from './apollowebs/standards-development/reports/SD REPORTS/individual-weekly-report/individual-weekly-report.component';
import {
    DepartmentalWeeklyReportComponent
} from './apollowebs/standards-development/reports/SD REPORTS/departmental-weekly-report/departmental-weekly-report.component';
import {
    StandardsReceivedComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/standards-received/standards-received.component';
import {
    StandardsEditedComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/standards-edited/standards-edited.component';
import {
    StandardsProofreadComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/standards-proofread/standards-proofread.component';
import {
    StandardsTypesetComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/standards-typeset/standards-typeset.component';
import {
    SpcReportComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/spc-report/spc-report.component';
import {
    WeeklyReportComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/weekly-report/weekly-report.component';
import {
    DraughsmanReportComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/draughsman-report/draughsman-report.component';
import {
    PublishingEnquiriestComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/publishing-enquiriest/publishing-enquiriest.component';
import {
    StandardsPrintedComponent
} from './apollowebs/standards-development/reports/PUBLISHING REPORTS/standards-printed/standards-printed.component';
import {
    InformationRequestRegisterComponent
} from './apollowebs/standards-development/reports/SIRC/information-request-register/information-request-register.component';
import {
    DisseminationPublicationComponent
} from './apollowebs/standards-development/reports/SIRC/dissemination-publication/dissemination-publication.component';
import {
    CatalogueReportComponent
} from './apollowebs/standards-development/reports/SIRC/catalogue-report/catalogue-report.component';
import {
    MembershipSubcriptionSchemeComponent
} from './apollowebs/standards-development/reports/SIRC/membership-subcription-scheme/membership-subcription-scheme.component';
import {
    KenyaStandardRequsitionFormComponent
} from './apollowebs/standards-development/reports/SIRC/kenya-standard-requsition-form/kenya-standard-requsition-form.component';
import {
    SaleForeignStandardRegisterComponent
} from './apollowebs/standards-development/reports/SIRC/sale-foreign-standard-register/sale-foreign-standard-register.component';
import {
    SalesStandardRegisterComponent
} from './apollowebs/standards-development/reports/SIRC/sale-kenya-standard-register/sales-standard-register.component';
import {
    PublicationOrderingRegisterComponent
} from './apollowebs/standards-development/reports/SIRC/publication-ordering-register/publication-ordering-register.component';
import {
    IcsAllocationComponent
} from './apollowebs/standards-development/reports/SIRC/ics-allocation/ics-allocation.component';
import {
    SdPaymentsComponent
} from './apollowebs/standards-development/schemeMembership/sd-payments/sd-payments.component';
import {
    DomesticNotificationComponent
} from './apollowebs/standards-development/reports/WTO REPORTS/domestic-notification/domestic-notification.component';
import {
    EnquariesHandledReportComponent
} from './apollowebs/standards-development/reports/WTO REPORTS/enquaries-handled-report/enquaries-handled-report.component';
import {
    StandardWorkProgrammeBulletinComponent
} from './apollowebs/standards-development/reports/WTO REPORTS/standard-work-programme-bulletin/standard-work-programme-bulletin.component';
import {
    NwaViewJustificationComponent
} from "./apollowebs/standards-development/workshop-agreement/nwa-view-justification/nwa-view-justification.component";
import {
    NwaPreliminaryDraftComponent
} from "./apollowebs/standards-development/workshop-agreement/nwa-preliminary-draft/nwa-preliminary-draft.component";
import {
    NwaViewPreliminaryComponent
} from "./apollowebs/standards-development/workshop-agreement/nwa-view-preliminary/nwa-view-preliminary.component";
import {
    NwaEditPreliminaryDraftComponent
} from "./apollowebs/standards-development/workshop-agreement/nwa-edit-preliminary-draft/nwa-edit-preliminary-draft.component";
import {
    NwaEditingDraftComponent
} from "./apollowebs/standards-development/workshop-agreement/nwa-editing-draft/nwa-editing-draft.component";
import {
    NepViewEnquiriesComponent
} from "./apollowebs/standards-development/national-enquiry-point/nep-view-enquiries/nep-view-enquiries.component";
import {QaSlReportsComponent} from './apollowebs/quality-assurance/reports/qa-sl-reports/qa-sl-reports.component';
import {
    NationalEnquiryPointReferalComponent
} from "./apollowebs/standards-development/national-enquiry-point/national-enquiry-point-referal/national-enquiry-point-referal.component";
import {
    NationalEnquiryPointResponseComponent
} from "./apollowebs/standards-development/national-enquiry-point/national-enquiry-point-response/national-enquiry-point-response.component";
import {
    NationalEnquiryReviewDraftComponent
} from "./apollowebs/standards-development/national-enquiry-point/national-enquiry-review-draft/national-enquiry-review-draft.component";
import {
    NepNotificationViewComponent
} from "./apollowebs/standards-development/national-enquiry-point/nep-notification-view/nep-notification-view.component";
import {
    MgrNepNotificationViewComponent
} from "./apollowebs/standards-development/national-enquiry-point/mgr-nep-notification-view/mgr-nep-notification-view.component";
import {
    NepUploadNotificationComponent
} from "./apollowebs/standards-development/national-enquiry-point/nep-upload-notification/nep-upload-notification.component";
import {
    RequestForStandardComponent
} from "./apollowebs/standards-development/sicStdPublication/request-for-standard/request-for-standard.component";
import {
    EmployerApproveComponent
} from "./apollowebs/standards-development/sicStdPublication/employer-approve/employer-approve.component";
import {
    ReviewStandardPublicationComponent
} from "./apollowebs/standards-development/sicStdPublication/review-standard-publication/review-standard-publication.component";
import {
    ReviewStandardPublicationSicComponent
} from "./apollowebs/standards-development/sicStdPublication/review-standard-publication-sic/review-standard-publication-sic.component";
import {
    SourceForStandardComponent
} from "./apollowebs/standards-development/sicStdPublication/source-for-standard/source-for-standard.component";
import {
    DisseminateStandardComponent
} from "./apollowebs/standards-development/sicStdPublication/disseminate-standard/disseminate-standard.component";
import {
    StandardLevyHistoricalPaymentsComponent
} from "./apollowebs/standards-levy/standard-levy-historical-payments/standard-levy-historical-payments.component";
import {
    NepUploadedNotificationComponent
} from "./apollowebs/standards-development/national-enquiry-point/nep-uploaded-notification/nep-uploaded-notification.component";
import {
    StandardsLevyQaPermitsComponent
} from "./apollowebs/standards-levy/standards-levy-qa-permits/standards-levy-qa-permits.component";
import {
    StandardsLevySitesComponent
} from "./apollowebs/standards-levy/standards-levy-sites/standards-levy-sites.component";
import {
    IntStdApproveChangesComponent
} from "./apollowebs/standards-development/international-standard/int-std-approve-changes/int-std-approve-changes.component";
import {
    TrackRequestComponent
} from "./apollowebs/standards-development/standard-request/track-request/track-request.component";

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
            {
                path: 'registerTivet',
                component: RegisterTivetComponent,
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
        path: 'company/list', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompanyListComponent}],
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
        canActivate: [RouteGuard],
        path: 'invoice/all_invoice_difference', component: AdminLayoutComponent,
        children: [{path: '', component: InvoiceConsolidateDmarkComponent}],
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

            {
                path: 'tivet_management',
                component: ManageTivetComponent,
            },

        ],
    },
    {
        path: 'reports',
        canActivate: [RouteGuard],
        component: AdminLayoutComponent,
        children: [
            {
                path: 'all_applications',
                component: ApplicationsReceivedComponent,
            },
            {
                path: 'permits_granted',
                component: PermitsGrantedComponent,
            },
            {
                path: 'permits_deferred',
                component: PermitsDeferredComponent,
            },
            {
                path: 'permits_renewed',
                component: PermitsRenewedComponent,
            },
            {
                path: 'samples_submitted',
                component: SamplesSubmittedComponent,
            },
            {
                path: 'qa-sl-report',
                component: QaSlReportsComponent,
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
        path: 'invoice/consolidate_invoice_fmark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: InvoiceConsolidateFmarkComponent}],
    },
    {
        path: 'invoice/consolidate_invoice-difference', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: InvoiceConsolidateDmarkComponent}],
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
        path: 'qa_task_list', component: AdminLayoutComponent,
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
        path: 'smarkrenewals-applications', component: AdminLayoutComponent,
        children: [{path: '', component: SmarkRenewalApplicationsComponent}],
    },
    {
        path: 'smarkexpired-applications', component: AdminLayoutComponent,
        children: [{path: '', component: SmarkExpiredApplicationsComponent}],
    },

    {
        path: 'fmarkrenewals-applications', component: AdminLayoutComponent,
        children: [{path: '', component: FmarkRenewalApplicationsComponent}],
    },
    {
        path: 'fmarkexpired-applications', component: AdminLayoutComponent,
        children: [{path: '', component: FmarkExpiredApplicationsComponent}],
    },

    {
        path: 'dmarkrenewals-applications', component: AdminLayoutComponent,
        children: [{path: '', component: DmarkRenewalApplicationsComponent}],
    },
    {
        path: 'dmarkexpired-applications', component: AdminLayoutComponent,
        children: [{path: '', component: DmarkExpiredApplicationsComponent}],
    },


    // quality Assurance Admin

    {
        path: 'smark-admin', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: SmarkAdminComponent},
        ],

    },

    {
        path: 'dmark-admin', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: DmarkAdminComponent},
        ],

    },
    {
        path: 'fmark-admin', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: FmarkAdminComponent},
        ],

    },


    {
        path: 'inspection-report-list', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: InspectionReportListComponent},
        ],

    },
    {
        path: 'new-inspection-report/:id', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: InspectionReport},
        ],

    },
    {
        path: 'inspection-report/:id', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: ViewInspectionReportComponent},
        ],

    },
    {
        path: 'permit-details-admin/:id', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: PermitDetailsAdminComponent},
        ],

    },
    {
        path: 'sta-details', component: AdminLayoutComponent,
        // canActivate: [RouteGuard]

        children: [
            {path: '', component: StaDetailsComponent},
        ],

    },

    {
        path: 'permit-details', component: PermitDetailsComponent,
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
                        path: 'rfc/cor',
                        component: RfcCorDocumentsComponent,
                    },
                    {
                        path: 'rfc/cor/:id',
                        component: ViewRfcCorDocumentsComponent,
                    },
                    {
                        path: 'rfc/coc',
                        component: RfcCocDocumentsComponent,
                    },
                    {
                        path: 'rfc/coc/:id',
                        component: ViewRfcCocDocumentsComponent,
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
                path: 'kentrade/declaration/documents',
                canActivate: [RouteGuard],
                component: DeclarationDocumentsComponent,
            },
            {
                path: 'kentrade/manifest/documents',
                canActivate: [RouteGuard],
                component: ManifestDocumentsComponent,
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
                path: 'bills',
                component: CorporateBillsComponent,
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
                component: InspectionFeesComponent,
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
    {
        path: 'certificates',
        canActivate: [RouteGuard],
        component: AdminLayoutComponent,
        children: [
            {
                path: 'coc',
                canActivate: [RouteGuard],
                component: CocCertificatesComponent,
            },
            {
                path: 'coi',
                canActivate: [RouteGuard],
                component: CoiCertificatesComponent,
            },
            {
                path: 'cor',
                canActivate: [RouteGuard],
                component: CorCertificatesComponent,
            },
            {
                path: 'ncr',
                canActivate: [RouteGuard],
                component: NcrCertificatesComponent,
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
        path: 'viewJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaViewJustificationComponent}],
    },
    {
        path: 'nwaPreparePD', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaPreliminaryDraftComponent}],
    },
    {
        path: 'nwaViewPD', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaViewPreliminaryComponent}],
    },
    {
        path: 'nwaEditPD', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaEditPreliminaryDraftComponent}],
    },
    {
        path: 'nwaWdEditing', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaEditingDraftComponent}],
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
        path: 'isProposals', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: IntStdProposalsComponent}],
    },
    {
        path: 'isPropComments', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: IntStdProposalCommentsComponent}],
    },
    {
        path: 'isProposalComments/:proposalId',
        component: IntStdCommentsComponent,
    },

    // {
    //     path: 'isProposalComments/:proposalID', component: AdminLayoutComponent,
    //     // canActivate: [RouteGuard],
    //     children: [{path: '', component: IntStdCommentsComponent}],
    // },
    {
        path: 'isPrepareJustification', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: IntStdApprovedProposalsComponent}],
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
        path: 'isUploadDraft', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdEditorComponent}],
    },
    {
        path: 'isCheckRequirements', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdCheckRequirementsComponent}],
    },
    {
        path: 'isStdEditDraft', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdEditDraftComponent}],
    },
    {
        path: 'isStdDraughting', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdDraughtComponent}],
    },
    {
        path: 'isStdProofReading', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdProofReadComponent}],
    },
    {
        path: 'isApproveChanges', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdApproveChangesComponent}],
    },
    {
        path: 'isApproveDraftStd', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdApproveDraftComponent}],
    },

    {
        path: 'isApprovedEdits', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdEditedDraftComponent}],
    },
    {
        path: 'isSacApproval', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdSacApprovalComponent}],
    },
    {
        path: 'isStandardGazette', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdGazetteComponent}],
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
    {
        path: 'isStdPublishingTask', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdPublishingComponent}],
    },
    {
        path: 'intStandardLists', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdListComponent}],
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
    {
        path: 'standardsForReview', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsForReviewComponent}],
    },
    {
        path: 'reviewStandardsTc', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewTcSecComponent}],
    },
    {
        path: 'reviewStandardsSPC', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewSpcSecComponent}],
    },
    {
        path: 'reviewStandardsSAC', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewSacSecComponent}],
    },
    {
        path: 'reviewStandardsProofReader', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewProofReaderComponent}],
    },
    {
        path: 'reviewStandardsHOP', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewHopComponent}],
    },
    {
        path: 'reviewStandardsEditor', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewEditorComponent}],
    },
    {
        path: 'reviewStandardsDraughtsMan', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewDraughtsManComponent}],
    },
    {
        path: 'reviewStandardsGazette', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemReviewGazetteStandardComponent}],
    },
    {
        path: 'reviewStandardsGazetteDate', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemReviewUpdateGazetteComponent}],
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
        path: 'comStdRequestProcess', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdRequestProcessComponent}],
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
        component: ComStdListComponent,
    },
    {
        path: 'comStdDraftComment/:comDraftID',
        component: ComStdDraftCommentComponent,
    },
    {
        path: 'comStdDraftComments/:comDraftID', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdDraftCommentsComponent}],
    },

    {
        path: 'comStdApproved/:comDraftID',
        component: ComStdAppDraftComponent,
    },
    {
        path: 'comStdTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComTasksComponent}],
    },
    {
        path: 'comStdEdit', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdEditorComponent}],
    },
    {
        path: 'comStdPublishing', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdPublishingComponent}],
    },
    {
        path: 'companyStandardList', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdConfirmComponent}],
    },


    {
        path: 'nepResponse', component: AdminLayoutComponent,
        children: [{path: '', component: NepViewEnquiriesComponent}],
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
        path: 'divResponse/:enquiryId',
        component: NationalEnquiryPointReferalComponent,
    },
    {
        path: 'sendFeedBack', component: AdminLayoutComponent,
        children: [{path: '', component: NationalEnquiryPointResponseComponent}],
    },
    {
        path: 'prepareReviewDraft', component: AdminLayoutComponent,
        children: [{path: '', component: NationalEnquiryReviewDraftComponent}],
    },
    {
        path: 'viewDraftNotification', component: AdminLayoutComponent,
        children: [{path: '', component: NepNotificationViewComponent}],
    },
    {
        path: 'managerViewNotification', component: AdminLayoutComponent,
        children: [{path: '', component: MgrNepNotificationViewComponent}],
    },
    {
        path: 'uploadNotification', component: AdminLayoutComponent,
        children: [{path: '', component: NepUploadNotificationComponent}],
    },
    {
        path: 'uploadedNotification', component: AdminLayoutComponent,
        children: [{path: '', component: NepUploadedNotificationComponent}],
    },


    // {
    //     path: 'make_enquiry', component: MakeEnquiryComponent,
    // },
    {
        path: 'make_enquiry',
        component: StandardRequestComponent,
        children: [{path: '', component: MakeEnquiryComponent}],
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
        // Track Standard Requests
        path: 'ms-track', component: AdminLayoutComponent,
        children: [{path: '', component: TrackRequestComponent}],
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

    // {
    //     // prepare  Draft
    //     path: 'prepareDraft', component: AdminLayoutComponent,
    //     children: [{path: '', component: PrepareDraftComponent}],
    // },


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
        path: 'hofReviewJustificationOfTC',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: HofReviewProposalComponent}],
    },
    {
        path: 'reviewJustificationOfTC',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewJustificationOfTCComponent}],
    },
    {
        path: 'reviewFeedbackSAC',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewFeedbackSacComponent}],
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
        component: StandardRequestComponent,
        children: [{path: '', component: SubmitApplicationComponent}],

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
    /****************************************************************
     * SCHEME MEMBERSHIP
     ***************************************************************/
    {
        path: 'schemeMemberShipForm',
        component: StandardRequestComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: SchemeMembershipFormComponent}],
    },
    {
        path: 'hodReview',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: SchemeMembershipReviewComponent}],
    },

    {
        path: 'sicReview',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: SchemeMembershipSicComponent}],
    },

    {
        path: 'sdpayments',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: SdPaymentsComponent}],
    },


    /****************************************************************
     * END OF STANDARD DEVELOPMENT - SCHEME MEMBERSHIP
     ***************************************************************/


    /****************************************************************
     * SIC STANDARD PUBLICATION
     ***************************************************************/
    {
        path: 'sicRequestForStandard',
        component: StandardRequestComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: RequestForStandardComponent}],
    },
    {
        path: 'sicEmployerApprove',
        component: StandardRequestComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: EmployerApproveComponent}],
    },

    {
        path: 'sicHodReviewRequest',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewStandardPublicationComponent}],
    },

    {
        path: 'sicOfficerReviewRequest',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: ReviewStandardPublicationSicComponent}],
    },
    {
        path: 'sourceForStandard',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: SourceForStandardComponent}],
    },
    {
        path: 'disseminateStandard',
        component: AdminLayoutComponent,
        // canActivate: [AuthGuard],
        children: [{path: '', component: DisseminateStandardComponent}],
    },


    /****************************************************************
     * END OF STANDARD DEVELOPMENT - SCHEME MEMBERSHIP
     ***************************************************************/




    // Standard development reports
    {
        path: 'nspStatus', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NspStatusComponent}],
    },

    {
        path: 'nonePreformingProject', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NonePerformingProjectsComponent}],
    },

    {
        path: 'delayedProjects', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: DelayedProjectsComponent}],
    },

    {
        path: 'sacReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsApprovalCommitteeComponent}],
    },

    {
        path: 'tcMemberApplication', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: TcMemberApplicationComponent}],
    },
    {
        path: 'individualWeeklyReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IndividualWeeklyReportComponent}],
    },

    {
        path: 'deparmentalWeeklyReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: DepartmentalWeeklyReportComponent}],
    },

    // PUBLISHING REPORTS

    {
        path: 'standardsReceived', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsReceivedComponent}],
    },
    {
        path: 'standardsEdited', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsEditedComponent}],
    },
    {
        path: 'standardsProofread', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsProofreadComponent}],
    },
    {
        path: 'standardsTypeset', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsTypesetComponent}],
    },
    {
        path: 'spcReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SpcReportComponent}],
    },
    {
        path: 'weeklyReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: WeeklyReportComponent}],
    },
    {
        path: 'draughtsmans', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: DraughsmanReportComponent}],
    },
    {
        path: 'publishingEnquiriest', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: PublishingEnquiriestComponent}],
    },
    {
        path: 'standardsPrinted', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardsPrintedComponent}],
    },

    // SIRC REPORTS


    {
        path: 'informationRequestRegister', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: InformationRequestRegisterComponent}],
    },
    {
        path: 'publicationOrderingRegister', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: PublicationOrderingRegisterComponent}],
    },
    {
        path: 'saleKenyastandard', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SalesStandardRegisterComponent}],
    },
    {
        path: 'saleForeignstandard', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SaleForeignStandardRegisterComponent}],
    },
    {
        path: 'standardRequsitionForm', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: KenyaStandardRequsitionFormComponent}],
    },
    {
        path: 'membershipSubscriptionScheme', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: MembershipSubcriptionSchemeComponent}],
    },
    {
        path: 'catalogueReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CatalogueReportComponent}],
    },
    {
        path: 'disseminationReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: DisseminationPublicationComponent}],
    },

    {
        path: 'icsAllocation', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IcsAllocationComponent}],
    },

    // WOT REPORTS
    {
        path: 'domesticNotification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: DomesticNotificationComponent}],
    },
    {
        path: 'enquiriesHandled', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: EnquariesHandledReportComponent}],
    },
    {
        path: 'standardworkProgramme', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardWorkProgrammeBulletinComponent}],
    },


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
    {
        path: 'stdLevyHistoricalPayments', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyHistoricalPaymentsComponent}],
    },
    {
        path: 'stdLevyQaReports', component: AdminLayoutComponent,
        children: [{path: '', component: StandardsLevyQaPermitsComponent}],
    },
    {
        path: 'stdLevySiteVisitReport', component: AdminLayoutComponent,
        children: [{path: '', component: StandardsLevySitesComponent}],
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
                component: FuelListTeamsComponent,
            },
            {
                path: 'details/:batchReferenceNumber/:teamsReferenceNo',
                component: FuelListTeamsCountyComponent,
            },
            {
                path: 'details/:batchReferenceNumber/:teamsReferenceNo/:countyReferenceNo',
                component: EpraListComponent,
            },
            {
                path: 'details/:batchReferenceNumber/:teamsReferenceNo/:countyReferenceNo/:referenceNumber',
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
        path: 'complaintPlan',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: ComplaintPlanBatchListComponent,
                pathMatch: 'full',
            },
            {
                path: ':referenceNumber',
                component: ComplaintPlanListComponent,
            },
            {
                path: 'details/:referenceNumber/:batchReferenceNumber',
                component: ComplaintPlanDetailsComponent,
            },
        ],
    },
    {
        path: 'msReports',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'consumerComplaint',
                component: AcknowledgementComponent,
            },
            {
                path: 'seizeGoods',
                component: ComplaintMonitoringComponent,
            },
            {
                path: 'submittedSamplesSummary',
                component: SampleSubmittedTimelineComponent,
            },
            {
                path: 'fieldInspectionSummary',
                component: FieldInspectionSummaryComponent,
            },
            {
                path: 'workPlanMonitoringTool',
                component: WorkplanMonitoringToolComponent,
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
        path: 'notifications',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: NotificationsComponent,
            },
        ],
    },
    {
        path: 'complaint-new',
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
        path: 'stdLevyManPenalty', component: AdminLayoutComponent,
        children: [{path: '', component: StdLevyManufacturerPenaltyComponent}],
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
    {
        path: 'slRegisteredFirms', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyRegisteredFirmsComponent}],
    },
    {
        path: 'slAllLevyPayments', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyAllPaymentsComponent}],
    },
    {
        path: 'slPenaltyReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyPenaltyReportComponent}],
    },
    {
        path: 'slActiveFirms', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyActiveFirmsComponent}],
    },
    {
        path: 'slDormantFirms', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyDormantFirmsComponent}],
    },
    {
        path: 'slClosedFirms', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyClosedFirmsComponent}],
    },
    {
        path: 'slRejectedEdits', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: StandardLevyRejectedChangesComponent}],
    },


    /****************MS COMPONENTS ENDS HERE**********************************/

];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
