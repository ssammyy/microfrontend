import {NgModule} from '@angular/core';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule} from '@angular/router';
import {CommonModule, DatePipe} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';


import {MatNativeDateModule, MatOptionModule} from '@angular/material/core';
// @ts-ignore
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatStepperModule} from '@angular/material/stepper';
import {MatIconModule} from '@angular/material/icon';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatDialogModule} from '@angular/material/dialog';
import {MatRadioModule} from '@angular/material/radio';
import {MatTableModule} from '@angular/material/table';


import {AppComponent} from './app.component';

import {SidebarModule} from './sidebar/sidebar.module';
import {FooterModule} from './shared/footer/footer.module';
import {NavbarModule} from './shared/navbar/navbar.module';
import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {AuthLayoutComponent} from './layouts/auth/auth-layout.component';
// import {routes} from './app.routing';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {DashboardComponent} from './apollowebs/dashboard/dashboard.component';
import {InvoiceComponent} from './apollowebs/quality-assurance/invoice/invoice.component';
import {AppRoutingModule} from './app.routing';
import {MdModule} from './md/md.module';
import {FmarkComponent} from './apollowebs/quality-assurance/fmark/fmark.component';
import {DmarkComponent} from './apollowebs/quality-assurance/dmark/dmark.component';
import {SmarkComponent} from './apollowebs/quality-assurance/smark/smark.component';
import {FmarkallappsComponent} from './apollowebs/quality-assurance/fmarkallapps/fmarkallapps.component';
import {St10FormComponent} from './apollowebs/quality-assurance/st10-form/st10-form.component';
import {NgxPaginationModule} from 'ngx-pagination';
import {CoreModule} from './core/core.module';
import {ToastrModule} from 'ngx-toastr';
import {RegistrationComponent} from './views/registration.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {ResetCredentialsComponent} from './views/registration/reset-credentials.component';
import {LoginComponent} from './views/registration/login.component';
import {PermitReportComponent} from './apollowebs/permit-report/permit-report.component';
import {NewSmarkPermitComponent} from './apollowebs/quality-assurance/new-smark-permit/new-smark-permit.component';
import {NewDmarkPermitComponent} from './apollowebs/quality-assurance/new-dmark-permit/new-dmark-permit.component';
import {
    DmarkApplicationsAllComponent
} from './apollowebs/quality-assurance/dmark-applications-all/dmark-applications-all.component';
import {InvoiceDetailsComponent} from './apollowebs/quality-assurance/invoice-details/invoice-details.component';
import {CompaniesList} from './apollowebs/company/companies.list';
import {CompanyComponent} from './apollowebs/company/company.component';
import {BranchComponent} from './apollowebs/company/branch/branch.component';
import {BranchList} from './apollowebs/company/branch/branch.list';
import {UserComponent} from './apollowebs/company/branch/users/user.component';
import {UserList} from './apollowebs/company/branch/users/user.list';
import {UserProfileComponent} from './apollowebs/company/branch/users/user-profile.component';
import {
    SmarkApplicationsAllComponent
} from './apollowebs/quality-assurance/smark-applications-all/smark-applications-all.component';
import {UserProfileMainComponent} from './apollowebs/userprofilemain/user-profile-main.component';
import {NgxSpinnerModule} from 'ngx-spinner';
import {AddBranchComponent} from './apollowebs/company/branch/add-branch/add-branch.component';
import {OtpComponent} from './views/registration/otp/otp.component';
import {
    InvoiceConsolidateComponent
} from './apollowebs/quality-assurance/invoice-consolidate/invoice-consolidate.component';
import {PdfViewComponent} from './pdf-view/pdf-view.component';
import {PdfViewerModule} from 'ng2-pdf-viewer';
import {BrowserModule} from '@angular/platform-browser';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';
import {TaskManagerComponent} from './apollowebs/task-manager/task-manager.component';
// @ts-ignore
import {FileUploadModule} from '@iplab/ngx-file-upload';
import {AddUserComponent} from './apollowebs/company/branch/add-user/add-user.component';
import {FmarkApplicationComponent} from './apollowebs/quality-assurance/fmark-application/fmark-application.component';
import {LoaderComponent} from './shared/loader/loader.component';
import {NgbNavModule, NgbPaginationModule, NgbRatingModule} from '@ng-bootstrap/ng-bootstrap';
import {
    ImportInspectionComponent
} from './apollowebs/pvoc/manufacturer/manufacturer-applications/import-inspection.component';
import {
    ExceptionsApplicationComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/exceptions-application.component';
import {
    WaiverApplicationComponent
} from './apollowebs/pvoc/manufacturer/export-waiver-application/waiver-application.component';
import {
    GoodsDetailsComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/goods-details/goods-details.component';
import {
    ManufacturerDetailsComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/manufacturer-details/manufacturer-details.component';
import {
    ProductDetailsComponent
} from './apollowebs/pvoc/manufacturer/importation-waiver/product-details/product-details.component';
import {
    ImportationWaiverComponent
} from './apollowebs/pvoc/manufacturer/importation-waiver/importation-waiver.component';
import {
    MainProductionMachineryComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/main-production-machinery/main-production-machinery.component';
import {
    RawMaterialsComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/raw-materials/raw-materials.component';
import {
    WaiverProductComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/waiver-product/waiver-product.component';
import {
    IndustrialSparesComponent
} from './apollowebs/pvoc/manufacturer/exceptions-application/industrial-spares/industrial-spares.component';
import {
    ConsignmentDocumentListComponent
} from './apollowebs/di/consignment-document-list/consignment-document-list.component';
import {
    ViewSingleConsignmentDocumentComponent
} from './apollowebs/di/view-single-consignment-document/view-single-consignment-document.component';
import {
    ItemDetailsListViewComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/item-details-list-view.component';
import {
    OtherVersionDetailsComponent
} from './apollowebs/di/view-single-consignment-document/other-version-details/other-version-details.component';
import {
    ApproveRejectConsignmentComponent
} from './apollowebs/di/view-single-consignment-document/approve-reject-consignment/approve-reject-consignment.component';
import {
    MinistryInspectionHomeComponent
} from './apollowebs/di/ministry-inspection-home/ministry-inspection-home.component';
import {
    MotorVehicleInspectionSingleViewComponent
} from './apollowebs/di/motor-vehicle-inspection-single-view/motor-vehicle-inspection-single-view.component';
import {
    UploadForeignFormComponent
} from './apollowebs/di/consignment-document-list/upload-foreign-form/upload-foreign-form.component';
import {FormsComponent} from './apollowebs/forms/forms.component';
import {FieldErrorDisplayComponent} from './apollowebs/forms/field-error-display/field-error-display.component';
import {
    NwaJustificationFormComponent
} from './apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component';
import {StandardsDevelopmentComponent} from './apollowebs/standards-development/standards-development.component';
import {
    InformationcheckComponent
} from './apollowebs/standards-development/informationcheck/informationcheck.component';
import {UsermanagementComponent} from './apollowebs/usermanagement/usermanagement.component';
import {
    ReviewStandardsComponent
} from './apollowebs/standards-development/systemic-review/request-standard-review/review-standards/review-standards.component';
import {
    CsRequestFormComponent
} from './apollowebs/standards-development/company-standard/company-standard-request/cs-request-form/cs-request-form.component';
import {
    DivisionresponseComponent
} from './apollowebs/standards-development/divisionresponse/divisionresponse.component';
import {
    NationalEnquiryPointComponent
} from './apollowebs/standards-development/national-enquiry-point/national-enquiry-point.component';
import {
    MakeEnquiryComponent
} from './apollowebs/standards-development/national-enquiry-point/make-enquiry/make-enquiry.component';
import {
    InternationalStandardProposalComponent
} from './apollowebs/standards-development/international-standard/international-standard-proposal/international-standard-proposal.component';
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
} from './apollowebs/standards-development/international-standard/international-standard-proposal/int-std-comments/int-std-comments.component';
import {
    CompanyStandardRequestComponent
} from './apollowebs/standards-development/company-standard/company-standard-request/company-standard-request.component';
import {
    RequestStandardReviewComponent
} from './apollowebs/standards-development/systemic-review/request-standard-review/request-standard-review.component';
import {
    SystemicReviewCommentsComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-comments/systemic-review-comments.component';
import {
    IntStdJustificationAppComponent
} from './apollowebs/standards-development/international-standard/int-std-justification-app/int-std-justification-app.component';
import {
    IsProposalFormComponent
} from './apollowebs/standards-development/international-standard/international-standard-proposal/is-proposal-form/is-proposal-form.component';
import {
    SystemicAnalyseCommentsComponent
} from './apollowebs/standards-development/systemic-review/systemic-analyse-comments/systemic-analyse-comments.component';
import {
    UserManagementProfileComponent
} from './apollowebs/usermanagement/user-management-profile/user-management-profile.component';
import {NepNotificationComponent} from './apollowebs/standards-development/nep-notification/nep-notification.component';
import {StandardRequestComponent} from './apollowebs/standards-development/standard-request/standard-request.component';
import {
    RequestStandardFormComponent
} from './apollowebs/standards-development/standard-request/request-standard-form/request-standard-form.component';
import {
    ReviewApplicationsComponent
} from './apollowebs/standards-development/standard-request/review-applications/review-applications.component';
import {
    StandardTaskComponent
} from './apollowebs/standards-development/standard-request/standard-task/standard-task.component';
import {ModalModule} from 'ngb-modal';
import {
    SmarkAllAwardedApplicationsComponent
} from './apollowebs/quality-assurance/smark-all-awarded-applications/smark-all-awarded-applications.component';
import {
    DmarkAllAwardedApplicationsComponent
} from './apollowebs/quality-assurance/dmark-all-awarded-applications/dmark-all-awarded-applications.component';
import {
    FmarkAllAwardedApplicationsComponent
} from './apollowebs/quality-assurance/fmark-all-awarded-applications/fmark-all-awarded-applications.component';
import {QaTaskDetailsComponent} from './apollowebs/quality-assurance/qa-task-details/qa-task-details.component';
import {CompanyViewComponent} from './apollowebs/company/company-view/company-view.component';
import {BranchViewComponent} from './apollowebs/company/branch/branch-view/branch-view.component';
import {QrCodeDetailsComponent} from './apollowebs/quality-assurance/qr-code-details/qr-code-details.component';
import {
    StdTscSecTasksComponentComponent
} from './apollowebs/standards-development/standard-request/std-tsc-sec-tasks-component/std-tsc-sec-tasks-component.component';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {DataTablesModule} from 'angular-datatables';
import {
    ViewDiDeclarationDocumentsComponent
} from './apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-di-declaration-documents.component';
import {
    ViewIdfDocumentDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-idf-document-details/view-idf-document-details.component';
import {
    ViewDeclarationDocumentsItemDetailsListComponent
} from './apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-declaration-documents-item-details-list/view-declaration-documents-item-details-list.component';
import {
    MvInspectionUploadFileReportComponent
} from './apollowebs/di/motor-vehicle-inspection-single-view/mv-inspection-upload-file-report/mv-inspection-upload-file-report.component';
import {
    AttachmentListComponent
} from './apollowebs/di/view-single-consignment-document/attachment-list/attachment-list.component';
import {
    AttachmentDialogComponent
} from './apollowebs/di/view-single-consignment-document/attachment-dialog/attachment-dialog.component';
import {AssignOfficerComponent} from './apollowebs/di/forms/assign-officer/assign-officer.component';
import {AssignPortComponent} from './apollowebs/di/forms/assign-port/assign-port.component';
import {SendCoiComponent} from './apollowebs/di/forms/send-coi/send-coi.component';
import {GenerateLocalCocComponent} from './apollowebs/di/forms/generate-local-coc/generate-local-coc.component';
import {GenerateLocalCorComponent} from './apollowebs/di/forms/generate-local-cor/generate-local-cor.component';
import {CompliantComponent} from './apollowebs/di/forms/compliant/compliant.component';
import {ReAssignOfficerComponent} from './apollowebs/di/forms/re-assign-officer/re-assign-officer.component';
import {BlacklistComponent} from './apollowebs/di/forms/blacklist/blacklist.component';
import {BlacklistApproveComponent} from './apollowebs/di/forms/blacklist-approve/blacklist-approve.component';
import {TargetItemComponent} from './apollowebs/di/forms/target-item/target-item.component';
import {TargetSupervisorComponent} from './apollowebs/di/forms/target-supervisor/target-supervisor.component';
import {
    ManualAssignOfficerComponent
} from './apollowebs/di/forms/manual-assign-officer/manual-assign-officer.component';
import {TargetApproveItemComponent} from './apollowebs/di/forms/target-approve-item/target-approve-item.component';
import {
    SendDemandNoteTokwsComponent
} from './apollowebs/di/forms/send-demand-note-tokws/send-demand-note-tokws.component';
import {
    ViewRemarksHistoryComponent
} from './apollowebs/di/view-single-consignment-document/view-remarks-history/view-remarks-history.component';
import {
    ItemDetailsComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/item-details/item-details.component';
import {ProcessRejectionComponent} from './apollowebs/di/forms/process-rejection/process-rejection.component';
import {MatFormFieldModule} from '@angular/material/form-field';
// tslint:disable-next-line:max-line-length
import {
    ChecklistsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/checklists/checklists.component';
import {
    MinistryInspectionRequestComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/ministry-inspection-request/ministry-inspection-request.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {DiCorComponent} from './apollowebs/di/view-single-consignment-document/di-cor/di-cor.component';
import {
    DiCocItemDetailsComponent
} from './apollowebs/di/view-single-consignment-document/di-coc/di-coc-item-details/di-coc-item-details.component';
import {DiCocComponent} from './apollowebs/di/view-single-consignment-document/di-coc/di-coc.component';
import {ViewTasksComponent} from './apollowebs/di/view-tasks/view-tasks.component';
import {DemandNoteListComponent} from './apollowebs/di/demand-note-list/demand-note-list.component';
import {
    ChecklistDataFormComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/checklist-data-form.component';
import {
    AgrochemInspectionChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/agrochem-inspection-checklist/agrochem-inspection-checklist.component';
import {
    EngineeringInspectionChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/engineering-inspection-checklist/engineering-inspection-checklist.component';
import {
    OtherInspectionChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/other-inspection-checklist/other-inspection-checklist.component';
import {
    VehicleInspectionChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/vehicle-inspection-checklist/vehicle-inspection-checklist.component';
import {
    SsfDetailsFormComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/ssf-details-form/ssf-details-form.component';
import {
    ApproveRejectItemComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/approve-reject-item/approve-reject-item.component';
import {ViewDemandNoteComponent} from './apollowebs/di/demand-note-list/view-demand-note/view-demand-note.component';
import {
    ItemSelectionComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/item-selection/item-selection.component';
import {
    ViewInspectionDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/view-inspection-details.component';
import {
    ItemChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/agrochem-inspection-checklist/item-checklist/item-checklist.component';
import {
    EngineeringItemChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/engineering-inspection-checklist/engineering-item-checklist/engineering-item-checklist.component';
import {
    OtherItemChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/other-inspection-checklist/other-item-checklist/other-item-checklist.component';
import {
    VehicleItemChecklistComponent
} from './apollowebs/di/view-single-consignment-document/checklist-data-form/vehicle-inspection-checklist/vehicle-item-checklist/vehicle-item-checklist.component';
import {
    EngineeringDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/engineering-details/engineering-details.component';
import {
    AgrochemDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/agrochem-details/agrochem-details.component';
import {
    OtherDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/other-details/other-details.component';
import {
    VehicleDetailsComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/vehicle-details/vehicle-details.component';
import {
    ScfDetailsFormComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/scf-details-form/scf-details-form.component';
import {
    ComplianceUpdateFormComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/compliance-update-form/compliance-update-form.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatMultilineTabDirective} from './apollowebs/di/mat-multiline-tab.directive';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {InspectionDashboardComponent} from './apollowebs/di/inspection-dashboard/inspection-dashboard.component';
import {
    LabResultsComponent
} from './apollowebs/di/view-single-consignment-document/item-details-list-view/lab-results/lab-results.component';
import {
    StdTcTasksComponent
} from './apollowebs/standards-development/standard-request/std-tc-tasks/std-tc-tasks.component';
import {
    ComStdDraftComponent
} from './apollowebs/standards-development/company-standard/com-std-draft/com-std-draft.component';
import {
    ComStdUploadComponent
} from './apollowebs/standards-development/company-standard/com-std-upload/com-std-upload.component';
import {
    ComStdConfirmComponent
} from './apollowebs/standards-development/company-standard/com-std-confirm/com-std-confirm.component';
import {NepSuccessComponent} from './apollowebs/standards-development/nep-success/nep-success.component';
import {
    ManagernotificationsComponent
} from './apollowebs/standards-development/managernotifications/managernotifications.component';

import {AllpermitsComponent} from './apollowebs/quality-assurance/allpermits/allpermits.component';
import {EpraListComponent} from './apollowebs/market-surveillance/fuel/epra-list/epra-list.component';
import {EpraBatchListComponent} from './apollowebs/market-surveillance/fuel/epra-batch-list/epra-batch-list.component';
import {
    EpraBatchNewComponent
} from './apollowebs/market-surveillance/fuel/epra-batch-list/epra-batch-new/epra-batch-new.component';
import {
    ViewFuelSheduledDetailsComponent
} from './apollowebs/market-surveillance/fuel/view-fuel-sheduled-details/view-fuel-sheduled-details.component';
import {MatMultilineTabMsDirective} from './apollowebs/market-surveillance/mat-multiline-tab-ms.directive';
import {
    CreateDepartmentComponent
} from './apollowebs/standards-development/standard-request/create-department/create-department.component';
import {
    CreatetechnicalcommitteeComponent
} from './apollowebs/standards-development/standard-request/createtechnicalcommittee/createtechnicalcommittee.component';
import {
    CreateproductComponent
} from './apollowebs/standards-development/standard-request/createproduct/createproduct.component';
import {
    CreateproductSubCategoryComponent
} from './apollowebs/standards-development/standard-request/createproduct-sub-category/createproduct-sub-category.component';
import {NgSelectModule} from '@ng-select/ng-select';
import {
    IntStdUploadStandardComponent
} from './apollowebs/standards-development/international-standard/int-std-upload-standard/int-std-upload-standard.component';
import {
    IntStdGazzetteComponent
} from './apollowebs/standards-development/international-standard/int-std-gazzette/int-std-gazzette.component';
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
// tslint:disable-next-line:max-line-length
import {
    StandardLevyPaidHistoryComponent
} from './apollowebs/standards-levy/standard-levy-paid-history/standard-levy-paid-history.component';
import {
    StandardLevyPenaltyHistoryComponent
} from './apollowebs/standards-levy/standard-levy-penalty-history/standard-levy-penalty-history.component';
import {
    StandardLevyDefaulterHistoryComponent
} from './apollowebs/standards-levy/standard-levy-defaulter-history/standard-levy-defaulter-history.component';
import {
    SpcSecTaskComponent
} from './apollowebs/standards-development/standard-request/spc-sec-task/spc-sec-task.component';
import {
    StdJustificationComponent
} from './apollowebs/standards-development/standard-request/std-justification/std-justification.component';
import {
    PreparePreliminaryDraftComponent
} from './apollowebs/standards-development/committee-module/prepare-preliminary-draft/prepare-preliminary-draft.component';
import {
    ComStdPlTaskComponent
} from './apollowebs/standards-development/company-standard/com-std-pl-task/com-std-pl-task.component';
// tslint:disable-next-line:max-line-length
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
    StandardLevySiteVisitApproveOneComponent
} from './apollowebs/standards-levy/standard-levy-site-visit-approve-one/standard-levy-site-visit-approve-one.component';
import {
    StandardLevySiteVisitApproveTwoComponent
} from './apollowebs/standards-levy/standard-levy-site-visit-approve-two/standard-levy-site-visit-approve-two.component';
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
    ReviewJustificationOfTCComponent
} from './apollowebs/standards-development/formationOfTc/review-justification-of-tc/review-justification-of-tc.component';
import {
    ReviewFeedbackSacComponent
} from './apollowebs/standards-development/formationOfTc/review-feedback-sac/review-feedback-sac.component';
import {
    RequestForFormationOfTCComponent
} from './apollowebs/standards-development/formationOfTc/request-for-formation-of-tc/request-for-formation-of-tc.component';
import {
    StdPublishingComponent
} from './apollowebs/standards-development/publishing/std-publishing/std-publishing.component';
import {
    StdHopTasksComponent
} from './apollowebs/standards-development/publishing/std-hop-tasks/std-hop-tasks.component';
import {EditorTasksComponent} from './apollowebs/standards-development/publishing/editor-tasks/editor-tasks.component';
import {
    StdProofreadComponent
} from './apollowebs/standards-development/publishing/std-proofread/std-proofread.component';
import {
    StdDraughtsmanComponent
} from './apollowebs/standards-development/publishing/std-draughtsman/std-draughtsman.component';
import {
    ApproveDraftStdComponent
} from './apollowebs/standards-development/publishing/approve-draft-std/approve-draft-std.component';
import {HttpClientModule} from '@angular/common/http';
import {
    CurrencyExchangeRatesComponent
} from './apollowebs/di/currency-exchange-rates/currency-exchange-rates.component';
import {MessageDashboardComponent} from './apollowebs/di/other-documents/message-dashboard.component';
import {ViewMessageComponent} from './apollowebs/di/other-documents/view-message/view-message.component';
import {ChartsModule, MDBBootstrapModule, WavesModule} from 'angular-bootstrap-md';
import {
    StdTcWorkplanComponent
} from './apollowebs/standards-development/standard-request/std-tc-workplan/std-tc-workplan.component';
import {TransactionViewComponent} from './apollowebs/di/transaction-view/transaction-view.component';
import {ViewClientsComponent} from './apollowebs/system/clients/view-clients/view-clients.component';
import {ViewPartnersComponent} from './apollowebs/pvoc/partners/view-partners/view-partners.component';
import {AddUpdatePartnerComponent} from './apollowebs/pvoc/partners/add-update-partner/add-update-partner.component';
import {
    ViewPartnerDetailsComponent
} from './apollowebs/pvoc/partners/view-partner-details/view-partner-details.component';
import {AddApiClientComponent} from './apollowebs/system/clients/add-api-client/add-api-client.component';
import {IsmApplicationsComponent} from './apollowebs/di/ism/ism-applications/ism-applications.component';
import {ViewIsmApplicationComponent} from './apollowebs/di/ism/view-ism-application/view-ism-application.component';
import {
    ApproveRejectApplicationComponent
} from './apollowebs/di/ism/approve-reject-application/approve-reject-application.component';
import {
    ViewClientCredentialsComponent
} from './apollowebs/system/clients/view-client-credentials/view-client-credentials.component';
import {
    ViewCorporateCustomersComponent
} from './apollowebs/invoice/corporate/view-corporate-customers/view-corporate-customers.component';
import {
    AddUpdateCorporateCustomerComponent
} from './apollowebs/invoice/corporate/add-update-corporate-customer/add-update-corporate-customer.component';
import {ViewCorporateComponent} from './apollowebs/invoice/corporate/view-corporate/view-corporate.component';
import {ViewBillLimitsComponent} from './apollowebs/invoice/limits/view-bill-limits/view-bill-limits.component';
import {ViewTransactionsComponent} from './apollowebs/invoice/corporate/view-transactions/view-transactions.component';
import {ViewAuctionItemsComponent} from './apollowebs/di/auction/view-auction-items/view-auction-items.component';
import {UploadFileComponent} from './apollowebs/di/auction/upload-file/upload-file.component';
import {AuctionItemDetailsComponent} from './apollowebs/di/auction/auction-item-details/auction-item-details.component';
import {AssignAuctionItemComponent} from './apollowebs/di/auction/assign-auction-item/assign-auction-item.component';
import {
    AproveRejectAuctionItemComponent
} from './apollowebs/di/auction/aprove-reject-auction-item/aprove-reject-auction-item.component';
import {GenerateDemandNoteComponent} from './apollowebs/di/auction/generate-demand-note/generate-demand-note.component';
import {AddAuctionRecordComponent} from './apollowebs/di/auction/add-auction-record/add-auction-record.component';
import {
    AddEditAuctionItemComponent
} from './apollowebs/di/auction/add-edit-auction-item/add-edit-auction-item.component';
import {
    CallsForApplicationComponent
} from './apollowebs/standards-development/membershipToTc/calls-for-application/calls-for-application.component';
// tslint:disable-next-line:max-line-length
import {
    SubmitApplicationComponent
} from './apollowebs/standards-development/membershipToTc/submit-application/submit-application.component';
// tslint:disable-next-line:max-line-length
import {
    ReviewApplicationComponent
} from './apollowebs/standards-development/membershipToTc/review-application/review-application.component';
import {
    ReviewRecommendationComponent
} from './apollowebs/standards-development/membershipToTc/review-recommendation/review-recommendation.component';
import {
    ReviewRecommendationOfSpcComponentComponent
} from './apollowebs/standards-development/membershipToTc/review-recommendation-of-spc-component/review-recommendation-of-spc-component.component';
import {
    UploadTcMemberComponentComponent
} from './apollowebs/standards-development/membershipToTc/upload-tc-member-component/upload-tc-member-component.component';
import {
    ManageCorporateCustomerComponent
} from './apollowebs/invoice/corporate/manage-corporate-customer/manage-corporate-customer.component';
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
    ApproveInductionMembersComponent
} from './apollowebs/standards-development/membershipToTc/approve-induction-members/approve-induction-members.component';
import {
    StdLevyApplicationsComponent
} from './apollowebs/standards-levy/std-levy-applications/std-levy-applications.component';
import {StdLevyTasksComponent} from './apollowebs/standards-levy/std-levy-tasks/std-levy-tasks.component';
import {
    StdLevyPendingTasksComponent
} from './apollowebs/standards-levy/std-levy-pending-tasks/std-levy-pending-tasks.component';
import {
    StdLevyCompleteTasksComponent
} from './apollowebs/standards-levy/std-levy-complete-tasks/std-levy-complete-tasks.component';
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
import {ViewComplaintsComponent} from './apollowebs/pvoc/complaints/view-complaints/view-complaints.component';
import {
    ViewComplaintDetailsComponent
} from './apollowebs/pvoc/complaints/view-complaint-details/view-complaint-details.component';
import {
    ManifestDocumentComponent
} from './apollowebs/di/view-single-consignment-document/manifest-document/manifest-document.component';
import {
    IncompleteIDFDocumentsComponent
} from './apollowebs/di/other-documents/idf-documents/incomplete-idfdocuments.component';
import {
    UploadSacSummaryComponent
} from './apollowebs/standards-development/adoptionOfEaStds/upload-sac-summary/upload-sac-summary.component';
import {
    ViewSacSummaryComponent
} from './apollowebs/standards-development/adoptionOfEaStds/view-sac-summary/view-sac-summary.component';
import {
    ViewSacSummaryApprovedComponent
} from './apollowebs/standards-development/adoptionOfEaStds/view-sac-summary-approved/view-sac-summary-approved.component';
import {AddAttachmentComponent} from './apollowebs/di/auction/add-attachment/add-attachment.component';
import {NwaTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-tasks/nwa-tasks.component';
import {
    GenerateAuctionKraReportComponent
} from './apollowebs/di/auction/generate-auction-kra-report/generate-auction-kra-report.component';
import {
    UpdateComplaintTaskComponent
} from './apollowebs/pvoc/complaints/update-complaint-task/update-complaint-task.component';
import {
    UpdateExemptionTaskComponent
} from './apollowebs/pvoc/exemptions/update-exemption-task/update-exemption-task.component';
import {
    AdminBusinessManagementComponent
} from './apollowebs/admin/admin-business-management/admin-business-management.component';
import {
    StandardLevyClosureComponent
} from './apollowebs/standards-levy/standard-levy-closure/standard-levy-closure.component';
import {
    StandardLevySuspensionComponent
} from './apollowebs/standards-levy/standard-levy-suspension/standard-levy-suspension.component';
import {ComplaintNewComponent} from './apollowebs/market-surveillance/complaint/complaint-new/complaint-new.component';
import {
    ComplaintListComponent
} from './apollowebs/market-surveillance/complaint/complaint-list/complaint-list.component';

import {
    CdGeneralChecklistComponent
} from './apollowebs/di/view-single-consignment-document/cd-general-checklist/cd-general-checklist.component';
import {PaymentsComponent} from './apollowebs/quality-assurance/payments/payments.component';
import {NgxMatSelectSearchModule} from 'ngx-mat-select-search';
import {
    EpraMonthlyBatchListComponent
} from './apollowebs/market-surveillance/fuel/epra-monthly-batch-list/epra-monthly-batch-list.component';
import {MatListModule} from '@angular/material/list';
import {
    WorkPlanBatchListComponent
} from './apollowebs/market-surveillance/workplan/workplan-batch-list/work-plan-batch-list.component';
import {WorkPlanListComponent} from './apollowebs/market-surveillance/workplan/work-plan-list/work-plan-list.component';
import {
    WorkPlanDetailsComponent
} from './apollowebs/market-surveillance/workplan/work-plan-details/work-plan-details.component';
import {AddCfsComponent} from './apollowebs/system/cfs/add-cfs/add-cfs.component';
import {CfsComponent} from './apollowebs/system/cfs/cfs.component';
import {InspectionFeesComponent} from './apollowebs/system/inspection-fees/inspection-fees.component';
import {AddUpdateFeeComponent} from './apollowebs/system/inspection-fees/add-update-fee/add-update-fee.component';
import {
    AddUpdateFeeRangeComponent
} from './apollowebs/system/inspection-fees/add-update-fee-range/add-update-fee-range.component';
import {ViewFeeComponent} from './apollowebs/system/inspection-fees/view-fee/view-fee.component';
import {CustomsOfficeComponent} from './apollowebs/system/customs-office/customs-office.component';
import {LaboratoriesComponent} from './apollowebs/system/laboratories/laboratories.component';
import {AddLaboratoryComponent} from './apollowebs/system/laboratories/add-laboratory/add-laboratory.component';
import {
    AddCustomOfficeComponent
} from './apollowebs/system/customs-office/add-custom-office/add-custom-office.component';
import {AddUpdateLimitComponent} from './apollowebs/invoice/limits/add-update-limit/add-update-limit.component';
import {
    ReviewPreliminaryDraftComponent
} from './apollowebs/standards-development/committee-module/review-preliminary-draft/review-preliminary-draft.component';
import {
    PrepareCommitteeDraftComponent
} from './apollowebs/standards-development/committee-module/prepare-committee-draft/prepare-committee-draft.component';
import {
    ReviewCommitteeDraftComponent
} from './apollowebs/standards-development/committee-module/review-committee-draft/review-committee-draft.component';
import {
    PreparePublicReviewDraftComponent
} from './apollowebs/standards-development/publicReview/prepare-public-review-draft/prepare-public-review-draft.component';
import {
    PublicReviewDraftComponent
} from './apollowebs/standards-development/publicReview/public-review-draft/public-review-draft.component';
import {ForeignCorsComponent} from './apollowebs/pvoc/documents/foreign-cors/foreign-cors.component';
import {ForeignCocsComponent} from './apollowebs/pvoc/documents/foreign-cocs/foreign-cocs.component';
import {ViewCorComponent} from './apollowebs/pvoc/documents/foreign-cors/view-cor/view-cor.component';
import {
    ViewOtherDocumentsComponent
} from './apollowebs/pvoc/documents/foreign-cocs/view-other-documents/view-other-documents.component';
import {CorFragmentComponent} from './apollowebs/di/fragments/cor-fragment/cor-fragment.component';
import {CocFragmentComponent} from './apollowebs/di/fragments/coc-fragment/coc-fragment.component';
import {PvocQueriesComponent} from './apollowebs/pvoc/documents/foreign-cors/pvoc-queries/pvoc-queries.component';
import {
    PvocCocQueriesComponent
} from './apollowebs/pvoc/documents/foreign-cocs/pvoc-coc-queries/pvoc-coc-queries.component';
import {PvocQueryCardComponent} from './apollowebs/di/fragments/pvoc-query-card/pvoc-query-card.component';
import {PvocQueryViewComponent} from './apollowebs/di/fragments/pvoc-query-view/pvoc-query-view.component';
import {
    ApproveCommitteeDraftComponent
} from './apollowebs/standards-development/committee-module/approve-committee-draft/approve-committee-draft.component';
import {
    CommentOnPublicReviewDraftComponent
} from './apollowebs/standards-development/publicReview/comment-on-public-review-draft/comment-on-public-review-draft.component';
import {
    PrepareBallotingDraftComponent
} from './apollowebs/standards-development/balloting/prepare-balloting-draft/prepare-balloting-draft.component';
import {
    VoteOnBallotDraftComponent
} from './apollowebs/standards-development/balloting/vote-on-ballot-draft/vote-on-ballot-draft.component';
import {ComTasksComponent} from './apollowebs/standards-development/company-standard/com-tasks/com-tasks.component';

import {
    ReviewBallotDraftComponent
} from './apollowebs/standards-development/balloting/review-ballot-draft/review-ballot-draft.component';
import {
    PvocNewComplaintComponent
} from './apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-new/pvoc-new-complaint.component';
import {
    ViewWaiverCertificatesComponent
} from './apollowebs/pvoc/manufacturer/view-waiver-certificates/view-waiver-certificates.component';
import {
    ViewExemptionCertificatesComponent
} from './apollowebs/pvoc/manufacturer/view-exemption-certificates/view-exemption-certificates.component';
import {
    IntStdTasksComponent
} from './apollowebs/standards-development/international-standard/int-std-tasks/int-std-tasks.component';
import {
    ComplaintDetailsComponent
} from './apollowebs/market-surveillance/complaint/complaint-details/complaint-details.component';
import {
    ManufacturerComplaintListComponent
} from './apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-list/manufacturer-complaint-list.component';
import {
    ManufacturerComplaintDetailsComponent
} from './apollowebs/pvoc/manufacturer/manufacturer-complaint/complaint-details/manufacturer-complaint-details.component';
import {RfcCocDocumentsComponent} from './apollowebs/pvoc/documents/rfc-coc-documents/rfc-coc-documents.component';
import {RfcCorDocumentsComponent} from './apollowebs/pvoc/documents/rfc-cor-documents/rfc-cor-documents.component';
import {
    ViewRfcCorDocumentsComponent
} from './apollowebs/pvoc/documents/rfc-cor-documents/view-rfc-cor-documents/view-rfc-cor-documents.component';
import {
    ViewRfcCocDocumentsComponent
} from './apollowebs/pvoc/documents/rfc-coc-documents/view-rfc-coc-documents/view-rfc-coc-documents.component';
import {FuelListTeamsComponent} from './apollowebs/market-surveillance/fuel/fuel-list-teams/fuel-list-teams.component';
// tslint:disable-next-line:max-line-length
import {
    FuelListTeamsCountyComponent
} from './apollowebs/market-surveillance/fuel/fuel-list-teams-county/fuel-list-teams-county.component';
import {UpdateWaiverTaskComponent} from './apollowebs/pvoc/waivers/update-waiver-task/update-waiver-task.component';
import {MatBadgeModule} from '@angular/material/badge';
import {
    StdLevyManufacturerPenaltyComponent
} from './apollowebs/standards-levy/std-levy-manufacturer-penalty/std-levy-manufacturer-penalty.component';
import {
    StandardLevyRegisteredFirmsComponent
} from './apollowebs/standards-levy/standard-levy-registered-firms/standard-levy-registered-firms.component';
import {
    StandardLevyAllPaymentsComponent
} from './apollowebs/standards-levy/standard-levy-all-payments/standard-levy-all-payments.component';
import {
    StandardLevyPenaltyReportComponent
} from './apollowebs/standards-levy/standard-levy-penalty-report/standard-levy-penalty-report.component';
import {
    StandardLevyActiveFirmsComponent
} from './apollowebs/standards-levy/standard-levy-active-firms/standard-levy-active-firms.component';
import {
    StandardLevyDormantFirmsComponent
} from './apollowebs/standards-levy/standard-levy-dormant-firms/standard-levy-dormant-firms.component';
import {
    StandardLevyClosedFirmsComponent
} from './apollowebs/standards-levy/standard-levy-closed-firms/standard-levy-closed-firms.component';
import {
    AddLaboratoryRequestComponent
} from './apollowebs/di/view-single-consignment-document/view-inspection-details/ssf-details-form/add-laboratory-request/add-laboratory-request.component';
import {
    ComplaintPlanBatchListComponent
} from './apollowebs/market-surveillance/complainWorkPlan/complaint-plan-batch-list/complaint-plan-batch-list.component';
import {
    ComplaintPlanListComponent
} from './apollowebs/market-surveillance/complainWorkPlan/complaint-plan-list/complaint-plan-list.component';
import {
    ComplaintPlanDetailsComponent
} from './apollowebs/market-surveillance/complainWorkPlan/complaint-plan-details/complaint-plan-details.component';
import {
    StandardLevyRejectedChangesComponent
} from './apollowebs/standards-levy/standard-levy-rejected-changes/standard-levy-rejected-changes.component';
import {CorCertificatesComponent} from './apollowebs/certificates/cor-certificates/cor-certificates.component';
import {CocCertificatesComponent} from './apollowebs/certificates/coc-certificates/coc-certificates.component';
import {CoiCertificatesComponent} from './apollowebs/certificates/coi-certificates/coi-certificates.component';
import {NcrCertificatesComponent} from './apollowebs/certificates/ncr-certificates/ncr-certificates.component';
import {
    GeneralCertificatesComponent
} from './apollowebs/certificates/general-certificates/general-certificates.component';
import {
    ManifestDocumentsComponent
} from './apollowebs/di/other-documents/manifest-documents/manifest-documents.component';
import {
    GenerateReportComponent
} from './apollowebs/di/other-documents/idf-documents/generate-report/generate-report.component';
import {
    GenerateManifestReportComponent
} from './apollowebs/di/other-documents/manifest-documents/generate-manifest-report/generate-manifest-report.component';
import {
    GenerateGeneralReportComponent
} from './apollowebs/certificates/general-certificates/generate-general-report/generate-general-report.component';
import {
    GenerateCorReportComponent
} from './apollowebs/certificates/cor-certificates/generate-cor-report/generate-cor-report.component';
import {
    GenerateCDReportComponent
} from './apollowebs/di/consignment-document-list/generate-cdreport/generate-cdreport.component';
import {
    ApplicationsReceivedComponent
} from './apollowebs/quality-assurance/reports/applications-received/applications-received.component';
import {
    PermitsGrantedComponent
} from './apollowebs/quality-assurance/reports/permits-granted/permits-granted.component';
import {
    PermitsRenewedComponent
} from './apollowebs/quality-assurance/reports/permits-renewed/permits-renewed.component';
import {
    SamplesSubmittedComponent
} from './apollowebs/quality-assurance/reports/samples-submitted/samples-submitted.component';
import {
    PermitsDeferredComponent
} from './apollowebs/quality-assurance/reports/permits-deferred/permits-deferred.component';
import {
    StandardsForReviewComponent
} from './apollowebs/standards-development/systemic-review/standards-for-review/standards-for-review.component';
import {
    SystemicReviewTcSecComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-tc-sec/systemic-review-tc-sec.component';
import {
    SystemicReviewDraughtsManComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-draughts-man/systemic-review-draughts-man.component';
import {
    SystemicReviewEditorComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-editor/systemic-review-editor.component';
import {
    SystemicReviewHopComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-hop/systemic-review-hop.component';
import {
    SystemicReviewSpcSecComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-spc-sec/systemic-review-spc-sec.component';
import {
    SystemicReviewSacSecComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-sac-sec/systemic-review-sac-sec.component';
import {
    SystemicReviewProofReaderComponent
} from './apollowebs/standards-development/systemic-review/systemic-review-proof-reader/systemic-review-proof-reader.component';
import {
    SystemReviewGazetteStandardComponent
} from './apollowebs/standards-development/systemic-review/system-review-gazette-standard/system-review-gazette-standard.component';
import {
    SystemReviewUpdateGazetteComponent
} from './apollowebs/standards-development/systemic-review/system-review-update-gazette/system-review-update-gazette.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {SpinnerComponent} from './shared/loader/spinner/spinner/spinner.component';
import {
    GenerateDnReportComponent
} from './apollowebs/di/transaction-view/generate-dn-report/generate-dn-report.component';
import {
    DeclarationDocumentsComponent
} from './apollowebs/di/other-documents/declaration-documents/declaration-documents.component';
import {NotificationsComponent} from './apollowebs/system/notifications/notifications.component';
import {
    AcknowledgementComponent
} from './apollowebs/market-surveillance/reports/acknowledgement/acknowledgement.component';
import {
    FeedbackTimelineComponent
} from './apollowebs/market-surveillance/reports/feedback-timeline/feedback-timeline.component';
import {
    ReportSubmittedTimelineComponent
} from './apollowebs/market-surveillance/reports/report-submitted-timeline/report-submitted-timeline.component';
import {
    SampleSubmittedTimelineComponent
} from './apollowebs/market-surveillance/reports/sample-submitted-timeline/sample-submitted-timeline.component';
import {MatSortModule} from '@angular/material/sort';
import {MatPaginatorModule} from '@angular/material/paginator';
import {
    IntStdProposalsComponent
} from './apollowebs/standards-development/international-standard/int-std-proposals/int-std-proposals.component';
import {
    IntStdApprovedProposalsComponent
} from './apollowebs/standards-development/international-standard/int-std-approved-proposals/int-std-approved-proposals.component';
import {
    IntStdJustificationComponent
} from './apollowebs/standards-development/international-standard/int-std-justification/int-std-justification.component';
import {
    IntStdEditorComponent
} from './apollowebs/standards-development/international-standard/int-std-editor/int-std-editor.component';
import {
    GenerateDeclarationReportComponent
} from './apollowebs/di/other-documents/declaration-documents/generate-declaration-report/generate-declaration-report.component';
import {MatCardModule} from '@angular/material/card';
// import { PrepareDraftComponent } from './apollowebs/standards-development/committee-module/prepare-draft/prepare-draft.component';
import {
    IntStdCheckRequirementsComponent
} from './apollowebs/standards-development/international-standard/int-std-check-requirements/int-std-check-requirements.component';
import {
    IntStdEditDraftComponent
} from './apollowebs/standards-development/international-standard/int-std-edit-draft/int-std-edit-draft.component';
import {
    IntStdDraughtComponent
} from './apollowebs/standards-development/international-standard/int-std-draught/int-std-draught.component';
import {
    IntStdProofReadComponent
} from './apollowebs/standards-development/international-standard/int-std-proof-read/int-std-proof-read.component';
import {
    IntStdApproveDraftComponent
} from './apollowebs/standards-development/international-standard/int-std-approve-draft/int-std-approve-draft.component';
import {
    IntStdEditedDraftComponent
} from './apollowebs/standards-development/international-standard/int-std-edited-draft/int-std-edited-draft.component';
import {
    IntStdSacApprovalComponent
} from './apollowebs/standards-development/international-standard/int-std-sac-approval/int-std-sac-approval.component';
import {CorporateBillsComponent} from './apollowebs/invoice/corporate-bills/corporate-bills.component';
import {CompanyListComponent} from './apollowebs/company/company-list/company-list.component';
import {
    HofReviewProposalComponent
} from './apollowebs/standards-development/formationOfTc/hof-review-proposal/hof-review-proposal.component';
import {PermitDetailsComponent} from './apollowebs/qualityAssuranceAdmin/permit-details/permit-details.component';
import {SmarkAdminComponent} from './apollowebs/qualityAssuranceAdmin/smark-admin/smark-admin.component';
import {FmarkAdminComponent} from './apollowebs/qualityAssuranceAdmin/fmark-admin/fmark-admin.component';
import {DmarkAdminComponent} from './apollowebs/qualityAssuranceAdmin/dmark-admin/dmark-admin.component';
import {
    BillDetailsComponent
} from './apollowebs/invoice/corporate/view-transactions/bill-details/bill-details.component';
import {
    ViewBillDetailsComponent
} from './apollowebs/invoice/corporate-bills/view-bill-details/view-bill-details.component';
// import { ComStdRequestProcessComponent } from './apollowebs/standards-development/company-standard/com-std-request-process/com-std-request-process.component';
// import { ManageTivetComponent } from './apollowebs/usermanagement/manage-tivet/manage-tivet.component';
// import { InvoiceConsolidateFmarkComponent } from './apollowebs/quality-assurance/invoice-consolidate-fmark/invoice-consolidate-fmark.component';
// import { InvoiceConsolidateDmarkComponent } from './apollowebs/quality-assurance/invoice-consolidate-dmark/invoice-consolidate-dmark.component';
import {
    SmarkRenewalApplicationsComponent
} from './apollowebs/quality-assurance/smark-renewal-applications/smark-renewal-applications.component';
import {
    FmarkRenewalApplicationsComponent
} from './apollowebs/quality-assurance/fmark-renewal-applications/fmark-renewal-applications.component';
import {
    SmarkExpiredApplicationsComponent
} from './apollowebs/quality-assurance/smark-expired-applications/smark-expired-applications.component';
import {
    FmarkExpiredApplicationsComponent
} from './apollowebs/quality-assurance/fmark-expired-applications/fmark-expired-applications.component';
import {
    DmarkExpiredApplicationsComponent
} from './apollowebs/quality-assurance/dmark-expired-applications/dmark-expired-applications.component';
import {
    DmarkRenewalApplicationsComponent
} from './apollowebs/quality-assurance/dmark-renewal-applications/dmark-renewal-applications.component';
import {
    IntStdProposalCommentsComponent
} from './apollowebs/standards-development/international-standard/int-std-proposal-comments/int-std-proposal-comments.component';
import {
    ComStdDraftCommentsComponent
} from './apollowebs/standards-development/company-standard/com-std-draft-comments/com-std-draft-comments.component';
import {
    ComStdRequestProcessComponent
} from './apollowebs/standards-development/company-standard/com-std-request-process/com-std-request-process.component';
import {ManageTivetComponent} from './apollowebs/usermanagement/manage-tivet/manage-tivet.component';
import {
    InvoiceConsolidateFmarkComponent
} from './apollowebs/quality-assurance/invoice-consolidate-fmark/invoice-consolidate-fmark.component';
import {
    InvoiceConsolidateDmarkComponent
} from './apollowebs/quality-assurance/invoice-consolidate-dmark/invoice-consolidate-dmark.component';
import {
    ComplaintMonitoringComponent
} from './apollowebs/market-surveillance/reports/complaint-monitoring/complaint-monitoring.component';
import {
    SchemeMembershipSicComponent
} from './apollowebs/standards-development/schemeMembership/scheme-membership-sic/scheme-membership-sic.component';
import {
    SchemeMembershipReviewComponent
} from './apollowebs/standards-development/schemeMembership/scheme-membership-review/scheme-membership-review.component';
import {
    FieldInspectionSummaryComponent
} from './apollowebs/market-surveillance/reports/field-inspection-summary/field-inspection-summary.component';
import {
    WorkplanMonitoringToolComponent
} from './apollowebs/market-surveillance/reports/workplan-monitoring-tool/workplan-monitoring-tool.component';
import {
    IntStdPublishingComponent
} from './apollowebs/standards-development/international-standard/int-std-publishing/int-std-publishing.component';
import {
    IntStdGazetteComponent
} from './apollowebs/standards-development/international-standard/int-std-gazette/int-std-gazette.component';
import {MsDashboardComponent} from './apollowebs/market-surveillance/ms-dashboard/ms-dashboard.component';
import {
    SchemeMembershipFormComponent
} from './apollowebs/standards-development/schemeMembership/scheme-membership-form/scheme-membership-form.component';
import {RegisterTivetComponent} from './views/registration/register-tivet/register-tivet.component';
import {
    ComStdAppDraftComponent
} from './apollowebs/standards-development/company-standard/company-standard-request/com-std-app-draft/com-std-app-draft.component';
import {
    ComStdPublishingComponent
} from './apollowebs/standards-development/company-standard/com-std-publishing/com-std-publishing.component';
import {
    ComStdEditorComponent
} from './apollowebs/standards-development/company-standard/com-std-editor/com-std-editor.component';
import {
    ComStdDraftCommentComponent
} from './apollowebs/standards-development/company-standard/company-standard-request/com-std-draft-comment/com-std-draft-comment.component';
import {StaDetailsComponent} from './apollowebs/qualityAssuranceAdmin/sta-details/sta-details.component';
import {
    InspectionReportListComponent
} from './apollowebs/qualityAssuranceAdmin/inspection-report-list/inspection-report-list.component';
import {
    ViewInspectionReportComponent
} from './apollowebs/qualityAssuranceAdmin/view-inspection-report/view-inspection-report.component';
import {
    PermitDetailsAdminComponent
} from './apollowebs/qualityAssuranceAdmin/permit-details-admin/permit-details-admin.component';
import {RfcFragmentComponent} from './apollowebs/pvoc/documents/rfc-fragment/rfc-fragment.component';
import { InspectionReport } from './apollowebs/qualityAssuranceAdmin/inspection-report/inspection-report';
import { NspStatusComponent } from './apollowebs/standards-development/reports/nsp-status/nsp-status.component';
import { NonePerformingProjectsComponent } from './apollowebs/standards-development/reports/none-preforming-projects/none-performing-projects.component';
import { DelayedProjectsComponent } from './apollowebs/standards-development/reports/delayed-projects/delayed-projects.component';
import { StandardsApprovalCommitteeComponent } from './apollowebs/standards-development/reports/standards-approval-committee/standards-approval-committee.component';
import { TcMemberApplicationComponent } from './apollowebs/standards-development/reports/tc-member-application/tc-member-application.component';
import { DepartmentalWeeklyReportComponent } from './apollowebs/standards-development/reports/departmental-weekly-report/departmental-weekly-report.component';
import { IndividualWeeklyReportComponent } from './apollowebs/standards-development/reports/individual-weekly-report/individual-weekly-report.component';
import { IntStdListComponent } from './apollowebs/standards-development/international-standard/int-std-list/int-std-list.component';

// import {NgxDocViewerModule} from "ngx-doc-viewer";

@NgModule({
    imports: [
        CommonModule,
        DataTablesModule,
        NgxExtendedPdfViewerModule,
        MatAutocompleteModule,
        FileUploadModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        PdfViewerModule,
        HttpClientModule,
        SidebarModule,
        NavbarModule,
        FooterModule,
        AppRoutingModule,
        CoreModule,
        MatRadioModule,
        ChartsModule,
        NgxSpinnerModule,
        // RichTextEditorModule,
        WavesModule,
        ToastrModule.forRoot({
            timeOut: 10000,
            enableHtml: true,
            newestOnTop: false,
            maxOpened: 1,
            autoDismiss: false,
            positionClass: 'toast-top-right',
            preventDuplicates: true,
        }),
        // NgbModule,
        NgxPaginationModule,
        FontAwesomeModule,
        NgxSpinnerModule,
        AppRoutingModule,
        RouterModule,
        BrowserAnimationsModule,
        DragDropModule,
        MdModule,
        CommonModule,
        BrowserModule,
        ModalModule,
        Ng2SmartTableModule,
        NgbNavModule,
        MatDialogModule,
        MatCheckboxModule,
        MatNativeDateModule,
        MatSelectModule,
        MatIconModule,
        MatStepperModule,
        MatOptionModule,
        MatInputModule,
        MatFormFieldModule,
        MatButtonModule,
        NgbPaginationModule,
        MatTableModule,
        NgMultiSelectDropDownModule.forRoot(),
        NgSelectModule,
        NoopAnimationsModule,
        MatTabsModule,
        MatProgressSpinnerModule,
        MDBBootstrapModule.forRoot(),
        NgxMatSelectSearchModule,
        MatListModule,
        MatBadgeModule,
        NgbRatingModule,
        MatDatepickerModule,
        MatSortModule,
        MatPaginatorModule,
        MatCardModule,

        // NgxDocViewerModule
    ],
    declarations: [
        AppComponent,
        AdminLayoutComponent,
        AuthLayoutComponent,
        ImportInspectionComponent,
        ExceptionsApplicationComponent,
        WaiverApplicationComponent,
        GoodsDetailsComponent,
        ManufacturerDetailsComponent,
        ChecklistsComponent,
        MinistryInspectionRequestComponent,
        DiCorComponent,
        DiCocComponent,
        CurrencyExchangeRatesComponent,
        DiCocItemDetailsComponent,
        MessageDashboardComponent,
        ViewTasksComponent,
        DemandNoteListComponent,
        ChecklistDataFormComponent,
        AgrochemInspectionChecklistComponent,
        EngineeringInspectionChecklistComponent,
        OtherInspectionChecklistComponent,
        VehicleInspectionChecklistComponent,
        SsfDetailsFormComponent,
        ApproveRejectItemComponent,
        ViewDemandNoteComponent,
        ItemSelectionComponent,
        GenerateAuctionKraReportComponent,
        ViewInspectionDetailsComponent,
        ItemChecklistComponent,
        EngineeringItemChecklistComponent,
        OtherItemChecklistComponent,
        VehicleItemChecklistComponent,
        EngineeringDetailsComponent,
        AgrochemDetailsComponent,
        OtherDetailsComponent,
        VehicleDetailsComponent,
        ScfDetailsFormComponent,
        ComplianceUpdateFormComponent,
        MatMultilineTabDirective,
        InspectionDashboardComponent,
        LabResultsComponent,
        LoginComponent,
        ResetCredentialsComponent,
        ViewMessageComponent,
        SignUpComponent,
        RegistrationComponent,
        DashboardComponent,
        InvoiceComponent,
        FmarkComponent,
        DmarkComponent,
        SmarkComponent,
        St10FormComponent,
        FmarkallappsComponent,
        NewDmarkPermitComponent,
        PermitReportComponent,
        NewSmarkPermitComponent,
        DmarkApplicationsAllComponent,
        InvoiceDetailsComponent,
        CompaniesList,
        CompanyComponent,
        BranchComponent,
        BranchList,
        UserComponent,
        UserList,
        UserProfileComponent,
        SmarkApplicationsAllComponent,
        UserProfileMainComponent,
        AddBranchComponent,
        OtpComponent,
        InvoiceConsolidateComponent,
        PdfViewComponent,
        TaskManagerComponent,
        PdfViewComponent,
        AddUserComponent,
        FmarkApplicationComponent,
        LoaderComponent,
        FormsComponent,
        FieldErrorDisplayComponent,
        NwaJustificationFormComponent,
        StandardsDevelopmentComponent,
        InformationcheckComponent,
        UsermanagementComponent,
        InternationalStandardProposalComponent,
        IntStdCommentsComponent,
        IntStdJustificationAppComponent,
        IntStdJustificationListComponent,
        IntStdResponsesListComponent,
        IsProposalFormComponent,
        RequestStandardReviewComponent,
        ReviewStandardsComponent,
        SystemicReviewCommentsComponent,
        SystemicAnalyseCommentsComponent,
        CompanyStandardRequestComponent,
        CsRequestFormComponent,
        ComStdRequestListComponent,
        AllpermitsComponent,
        ComStdJcJustificationComponent,
        InformationcheckComponent,
        DivisionresponseComponent,
        NationalEnquiryPointComponent,
        MakeEnquiryComponent,
        UserManagementProfileComponent,
        NepNotificationComponent,
        StandardRequestComponent,
        RequestStandardFormComponent,
        ReviewApplicationsComponent,
        StdJustificationComponent,
        StandardTaskComponent,
        ProductDetailsComponent,
        ImportationWaiverComponent,
        MainProductionMachineryComponent,
        RawMaterialsComponent,
        WaiverProductComponent,
        IndustrialSparesComponent,
        ConsignmentDocumentListComponent,
        ViewSingleConsignmentDocumentComponent,
        ItemDetailsListViewComponent,
        OtherVersionDetailsComponent,
        ApproveRejectConsignmentComponent,
        MinistryInspectionHomeComponent,
        MotorVehicleInspectionSingleViewComponent,
        UploadForeignFormComponent,
        ViewDiDeclarationDocumentsComponent,
        ViewIdfDocumentDetailsComponent,
        ViewDeclarationDocumentsItemDetailsListComponent,
        MvInspectionUploadFileReportComponent,
        AttachmentListComponent,
        AttachmentDialogComponent,
        AssignOfficerComponent,
        AssignPortComponent,
        SendCoiComponent,
        GenerateLocalCocComponent,
        GenerateLocalCorComponent,
        CompliantComponent,
        ReAssignOfficerComponent,
        BlacklistComponent,
        BlacklistApproveComponent,
        TargetItemComponent,
        TargetSupervisorComponent,
        TargetApproveItemComponent,
        ManualAssignOfficerComponent,
        SendDemandNoteTokwsComponent,
        ViewRemarksHistoryComponent,
        ItemDetailsComponent,
        ProcessRejectionComponent,
        StdTcWorkplanComponent,
        SmarkAllAwardedApplicationsComponent,
        DmarkAllAwardedApplicationsComponent,
        FmarkAllAwardedApplicationsComponent,
        QaTaskDetailsComponent,
        CompanyViewComponent,
        BranchViewComponent,
        QrCodeDetailsComponent,
        StdTscSecTasksComponentComponent,
        StdTcTasksComponent,
        ComStdDraftComponent,
        ComStdUploadComponent,
        ComStdConfirmComponent,
        StdTcTasksComponent,
        NepSuccessComponent,
        SpcSecTaskComponent,
        ManagernotificationsComponent,
        EpraListComponent,
        EpraBatchListComponent,
        EpraBatchNewComponent,
        ViewFuelSheduledDetailsComponent,
        MatMultilineTabMsDirective,
        ComplaintNewComponent,
        ComplaintListComponent,
        ComplaintDetailsComponent,
        ManufacturerComplaintDetailsComponent,
        ManufacturerComplaintListComponent,
        PaymentsComponent,
        IntStdUploadStandardComponent,
        IntStdGazzetteComponent,
        CreateDepartmentComponent,
        CreatetechnicalcommitteeComponent,
        CreateproductComponent,
        CreateproductSubCategoryComponent,
        RoleSwitcherComponent,
        CustomerRegistrationComponent,
        StandardsLevyHomeComponent,
        ComStandardLevyComponent,
        ComPaymentHistoryComponent,
        ComStdLevyFormComponent,
        StandardLevyDashboardComponent,
        StandardLevyPaidComponent,
        StandardLevyPenaltiesComponent,
        StandardLevyDefaulterComponent,
        StandardLevyPaidHistoryComponent,
        StandardLevyPenaltyHistoryComponent,
        StandardLevyDefaulterHistoryComponent,
        TransactionViewComponent,
        ViewClientsComponent,
        ViewPartnersComponent,
        AddUpdatePartnerComponent,
        ViewPartnerDetailsComponent,
        IsmApplicationsComponent,
        ViewIsmApplicationComponent,
        ApproveRejectApplicationComponent,
        ViewClientCredentialsComponent,
        ViewCorporateCustomersComponent,
        AddUpdateCorporateCustomerComponent,
        ViewCorporateComponent,
        ViewBillLimitsComponent,
        ViewTransactionsComponent,
        ViewAuctionItemsComponent,
        UploadFileComponent,
        AuctionItemDetailsComponent,
        AssignAuctionItemComponent,
        AproveRejectAuctionItemComponent,
        GenerateDemandNoteComponent,
        AddAuctionRecordComponent,
        AddEditAuctionItemComponent,
        StandardLevyDefaulterHistoryComponent,
        PreparePreliminaryDraftComponent,
        StandardLevyDefaulterHistoryComponent,
        ComStdPlTaskComponent,
        ComStdDraftViewComponent,
        ComStdListComponent,
        StandardLevySiteVisitComponent,
        StandardLevySiteVisitApproveOneComponent,
        StandardLevySiteVisitApproveTwoComponent,
        StandardLevySiteVisitFeedbackComponent,
        StandardLevyUploadSiteVisitFeedbackComponent,
        StandardLevyManufactureDetailsComponent,
        RequestForFormationOfTCComponent,
        ReviewJustificationOfTCComponent,
        ReviewFeedbackSacComponent,
        StdPublishingComponent,
        StdHopTasksComponent,
        EditorTasksComponent,
        StdProofreadComponent,
        StdDraughtsmanComponent,
        ApproveDraftStdComponent,
        CallsForApplicationComponent,
        SubmitApplicationComponent,
        ReviewApplicationComponent,
        ReviewRecommendationComponent,
        ReviewRecommendationOfSpcComponentComponent,
        UploadTcMemberComponentComponent,
        ApproveDraftStdComponent,
        ManageCorporateCustomerComponent,
        ReviewApplicationsAcceptedComponent,
        ReviewApplicationsRejectedComponent,
        ApproveApplicationComponent,
        ApprovedMembersComponent,
        MembersToCreateCredentialsComponent,
        MembersCreatedCredentialsComponent,
        ApproveInductionComponent,
        ApproveInductionMembersComponent,
        ApproveApplicationComponent,
        StdLevyApplicationsComponent,
        StdLevyTasksComponent,
        StdLevyPendingTasksComponent,
        StdLevyCompleteTasksComponent,
        ViewWaiverApplicationsComponent,
        ViewWaiverDetailsComponent,
        UpdateWaiverTaskComponent,
        ViewExemptionApplicationsComponent,
        ViewExemptionDetailsComponent,
        ViewComplaintsComponent,
        ViewComplaintDetailsComponent,
        ManageCorporateCustomerComponent,
        ManifestDocumentComponent,
        IncompleteIDFDocumentsComponent,
        AddApiClientComponent,
        UploadSacSummaryComponent,
        ViewSacSummaryComponent,
        ViewSacSummaryApprovedComponent,
        AddApiClientComponent,
        AddAttachmentComponent,
        NwaTasksComponent,
        UpdateComplaintTaskComponent,
        UpdateExemptionTaskComponent,
        AdminBusinessManagementComponent,
        AdminBusinessManagementComponent,
        StandardLevyClosureComponent,
        StandardLevySuspensionComponent,
        AdminBusinessManagementComponent,
        CdGeneralChecklistComponent,
        EpraMonthlyBatchListComponent,
        WorkPlanBatchListComponent,
        WorkPlanListComponent,
        WorkPlanDetailsComponent,
        AddCfsComponent,
        CfsComponent,
        InspectionFeesComponent,
        AddUpdateFeeComponent,
        AddUpdateFeeRangeComponent,
        ViewFeeComponent,
        CustomsOfficeComponent,
        LaboratoriesComponent,
        AddLaboratoryComponent,
        AddCustomOfficeComponent,
        AddUpdateLimitComponent,
        ForeignCorsComponent,
        ForeignCocsComponent,
        ViewCorComponent,
        ViewOtherDocumentsComponent,
        CocFragmentComponent,
        CorFragmentComponent,
        PvocQueriesComponent,
        PvocCocQueriesComponent,
        PvocQueryCardComponent,
        PvocQueryViewComponent,
        AddUpdateLimitComponent,
        ReviewPreliminaryDraftComponent,
        PrepareCommitteeDraftComponent,
        ReviewCommitteeDraftComponent,
        PreparePublicReviewDraftComponent,
        PublicReviewDraftComponent,
        ApproveCommitteeDraftComponent,
        CommentOnPublicReviewDraftComponent,
        PrepareBallotingDraftComponent,
        VoteOnBallotDraftComponent,
        ComTasksComponent,
        ReviewBallotDraftComponent,
        ComTasksComponent,
        IntStdTasksComponent,
        ComTasksComponent,
        PvocNewComplaintComponent,
        ViewWaiverCertificatesComponent,
        ViewExemptionCertificatesComponent,
        StdHopTasksComponent,
        SpcSecTaskComponent,
        NwaJustificationFormComponent,
        RfcCocDocumentsComponent,
        RfcCorDocumentsComponent,
        StdLevyManufacturerPenaltyComponent,
        StandardLevyRegisteredFirmsComponent,
        StandardLevyAllPaymentsComponent,
        StandardLevyPenaltyReportComponent,
        StandardLevyActiveFirmsComponent,
        StandardLevyDormantFirmsComponent,
        StandardLevyClosedFirmsComponent,
        ViewRfcCorDocumentsComponent,
        ViewRfcCocDocumentsComponent,
        FuelListTeamsComponent,
        FuelListTeamsCountyComponent,
        StandardLevyClosedFirmsComponent,
        AddLaboratoryRequestComponent,
        StandardLevyRejectedChangesComponent,
        AddLaboratoryRequestComponent,
        ApplicationsReceivedComponent,
        PermitsGrantedComponent,
        PermitsRenewedComponent,
        SamplesSubmittedComponent,
        PermitsDeferredComponent,
        AddLaboratoryRequestComponent,
        ComplaintPlanBatchListComponent,
        ComplaintPlanListComponent,
        ComplaintPlanDetailsComponent,
        CorCertificatesComponent,
        CocCertificatesComponent,
        CoiCertificatesComponent,
        NcrCertificatesComponent,
        GeneralCertificatesComponent,
        ManifestDocumentsComponent,
        GenerateReportComponent,
        GenerateManifestReportComponent,
        GenerateGeneralReportComponent,
        GenerateCorReportComponent,
        GenerateCDReportComponent,
        ComplaintPlanDetailsComponent,
        StandardsForReviewComponent,
        SystemicReviewTcSecComponent,
        SystemicReviewDraughtsManComponent,
        SystemicReviewEditorComponent,
        SystemicReviewHopComponent,
        SystemicReviewSpcSecComponent,
        SystemicReviewSacSecComponent,
        SystemicReviewProofReaderComponent,
        SystemReviewGazetteStandardComponent,
        SystemReviewUpdateGazetteComponent,
        SpinnerComponent,
        NotificationsComponent,
        AcknowledgementComponent,
        FeedbackTimelineComponent,
        ReportSubmittedTimelineComponent,
        SampleSubmittedTimelineComponent,
        GenerateDnReportComponent,
        DeclarationDocumentsComponent,
        IntStdProposalsComponent,
        IntStdApprovedProposalsComponent,
        IntStdJustificationComponent,
        IntStdEditorComponent,
        GenerateDeclarationReportComponent,
        // PrepareDraftComponent,
        IntStdCheckRequirementsComponent,
        IntStdEditDraftComponent,
        IntStdDraughtComponent,
        IntStdProofReadComponent,
        IntStdApproveDraftComponent,
        IntStdEditedDraftComponent,
        IntStdSacApprovalComponent,
        IntStdGazetteComponent,
        MsDashboardComponent,
        ComStdDraftCommentComponent,
        SchemeMembershipFormComponent,
        SchemeMembershipReviewComponent,
        SchemeMembershipSicComponent,
        RegisterTivetComponent,
        ComplaintMonitoringComponent,
        FieldInspectionSummaryComponent,
        WorkplanMonitoringToolComponent,
        ComStdAppDraftComponent,
        IntStdPublishingComponent,
        ComStdPublishingComponent,
        ComStdEditorComponent,
        CorporateBillsComponent,
        CompanyListComponent,
        HofReviewProposalComponent,
        PermitDetailsComponent,
        SmarkAdminComponent,
        FmarkAdminComponent,
        DmarkAdminComponent,

        ComStdRequestProcessComponent,

        ManageTivetComponent,

        InvoiceConsolidateFmarkComponent,

        InvoiceConsolidateDmarkComponent,

        IntStdProposalCommentsComponent,

        ComStdDraftCommentsComponent,

        SmarkRenewalApplicationsComponent,

        FmarkRenewalApplicationsComponent,

        SmarkExpiredApplicationsComponent,

        FmarkExpiredApplicationsComponent,

        DmarkExpiredApplicationsComponent,

        DmarkRenewalApplicationsComponent,

        BillDetailsComponent,
        ViewBillDetailsComponent,
        StaDetailsComponent,
        InspectionReportListComponent,
        ViewInspectionReportComponent,
        PermitDetailsAdminComponent,
        RfcFragmentComponent,
        InspectionReport,
        NspStatusComponent,
        NonePerformingProjectsComponent,
        DelayedProjectsComponent,
        StandardsApprovalCommitteeComponent,
        TcMemberApplicationComponent,
        DepartmentalWeeklyReportComponent,
        IndividualWeeklyReportComponent,
        IntStdListComponent,

    ],
    entryComponents: [LoaderComponent],
    providers: [DatePipe, MatNativeDateModule],
    bootstrap: [AppComponent],
})
export class AppModule {
}
