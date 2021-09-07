import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {MatNativeDateModule, MatOptionModule} from '@angular/material/core';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatStepperModule} from '@angular/material/stepper';
import {MatIconModule} from '@angular/material/icon';
import {MatDialogModule} from '@angular/material/dialog';


import {AppComponent} from './app.component';

import {SidebarModule} from './sidebar/sidebar.module';
import {NavbarModule} from './shared/navbar/navbar.module';
import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {AuthLayoutComponent} from './layouts/auth/auth-layout.component';

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
import {DmarkApplicationsAllComponent} from './apollowebs/quality-assurance/dmark-applications-all/dmark-applications-all.component';
import {InvoiceDetailsComponent} from './apollowebs/quality-assurance/invoice-details/invoice-details.component';
import {CompaniesList} from './apollowebs/company/companies.list';
import {CompanyComponent} from './apollowebs/company/company.component';
import {BranchComponent} from './apollowebs/company/branch/branch.component';
import {BranchList} from './apollowebs/company/branch/branch.list';
import {UserComponent} from './apollowebs/company/branch/users/user.component';
import {UserList} from './apollowebs/company/branch/users/user.list';
import {UserProfileComponent} from './apollowebs/company/branch/users/user-profile.component';
import {SmarkApplicationsAllComponent} from './apollowebs/quality-assurance/smark-applications-all/smark-applications-all.component';
import {UserProfileMainComponent} from './apollowebs/userprofilemain/user-profile-main.component';
import {NgxSpinnerModule} from 'ngx-spinner';
import {AddBranchComponent} from './apollowebs/company/branch/add-branch/add-branch.component';
import {OtpComponent} from './views/registration/otp/otp.component';
import {InvoiceConsolidateComponent} from './apollowebs/quality-assurance/invoice-consolidate/invoice-consolidate.component';
import {PdfViewComponent} from './pdf-view/pdf-view.component';
import {PdfViewerModule} from 'ng2-pdf-viewer';
import {BrowserModule} from '@angular/platform-browser';
import {NgxExtendedPdfViewerModule} from 'ngx-extended-pdf-viewer';
import {TaskManagerComponent} from './apollowebs/task-manager/task-manager.component';
import {FileUploadModule} from '@iplab/ngx-file-upload';
import {AddUserComponent} from './apollowebs/company/branch/add-user/add-user.component';
import {FmarkApplicationComponent} from './apollowebs/quality-assurance/fmark-application/fmark-application.component';
import {LoaderComponent} from './shared/loader/loader.component';
import {NgbNavModule, NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';
import {ImportInspectionComponent} from './apollowebs/pvoc/import-inspection/import-inspection.component';
import {ExceptionsApplicationComponent} from './apollowebs/pvoc/exceptions-application/exceptions-application.component';
import {WaiverApplicationComponent} from './apollowebs/pvoc/waiver-application/waiver-application.component';
import {GoodsDetailsComponent} from './apollowebs/pvoc/exceptions-application/goods-details/goods-details.component';
import {WaiverSubmittedDialogComponent} from './apollowebs/pvoc/waivers/waiver-submitted-dialog/waiver-submitted-dialog.component';
import {ManufacturerDetailsComponent} from './apollowebs/pvoc/waivers/manufacturer-details/manufacturer-details.component';
import {ProductDetailsComponent} from './apollowebs/pvoc/waiver-application/product-details/product-details.component';
import {ImportationWaiverComponent} from "./apollowebs/pvoc/importation-waiver/importation-waiver.component";
import {MainProductionMachineryComponent} from "./apollowebs/pvoc/waivers/main-production-machinery/main-production-machinery.component";
import {RawMaterialsComponent} from "./apollowebs/pvoc/waivers/raw-materials/raw-materials.component";
import {WaiverProductComponent} from "./apollowebs/pvoc/waivers/waiver-product/waiver-product.component";
import {IndustrialSparesComponent} from "./apollowebs/pvoc/waivers/industrial-spares/industrial-spares.component";
import {ConsignmentDocumentListComponent} from './apollowebs/di/consignment-document-list/consignment-document-list.component';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {ViewSingleConsignmentDocumentComponent} from './apollowebs/di/view-single-consignment-document/view-single-consignment-document.component';
import {ItemDetailsListViewComponent} from './apollowebs/di/view-single-consignment-document/item-details-list-view/item-details-list-view.component';
import {OtherVersionDetailsComponent} from './apollowebs/di/view-single-consignment-document/other-version-details/other-version-details.component';
import {ApproveRejectConsignmentComponent} from './apollowebs/di/view-single-consignment-document/approve-reject-consignment/approve-reject-consignment.component';
import {MinistryInspectionHomeComponent} from './apollowebs/di/ministry-inspection-home/ministry-inspection-home.component';
import {MotorVehicleInspectionSingleViewComponent} from './apollowebs/di/motor-vehicle-inspection-single-view/motor-vehicle-inspection-single-view.component';
import {UploadForeignFormComponent} from './apollowebs/di/consignment-document-list/upload-foreign-form/upload-foreign-form.component';
import {FormsComponent} from './apollowebs/forms/forms.component';
import {FieldErrorDisplayComponent} from './apollowebs/forms/field-error-display/field-error-display.component';
import {NwaJustificationFormComponent} from "./apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component";
import {StandardsDevelopmentComponent} from "./apollowebs/standards-development/standards-development.component";
import {InformationcheckComponent} from './apollowebs/standards-development/informationcheck/informationcheck.component';
import {UsermanagementComponent} from './apollowebs/usermanagement/usermanagement.component';
import {NwaJustificationTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-justification-tasks/nwa-justification-tasks.component';
import {NwaKnwSecTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-knw-sec-tasks/nwa-knw-sec-tasks.component';
import {NwaDiSdtTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-di-sdt-tasks/nwa-di-sdt-tasks.component';
import {NwaHopTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-hop-tasks/nwa-hop-tasks.component';
import {SacSecTasksComponent} from './apollowebs/standards-development/workshop-agreement/sac-sec-tasks/sac-sec-tasks.component';
import {HoSicTasksComponent} from './apollowebs/standards-development/ho-sic-tasks/ho-sic-tasks.component';
import {ReviewStandardsComponent} from './apollowebs/standards-development/systemic-review/request-standard-review/review-standards/review-standards.component';
import {CsRequestFormComponent} from './apollowebs/standards-development/company-standard/company-standard-request/cs-request-form/cs-request-form.component';
import {DivisionresponseComponent} from "./apollowebs/standards-development/divisionresponse/divisionresponse.component";
import {NationalEnquiryPointComponent} from './apollowebs/standards-development/national-enquiry-point/national-enquiry-point.component';
import {MakeEnquiryComponent} from './apollowebs/standards-development/national-enquiry-point/make-enquiry/make-enquiry.component';
import {InternationalStandardProposalComponent} from "./apollowebs/standards-development/international-standard/international-standard-proposal/international-standard-proposal.component";
import {ComStdRequestListComponent} from "./apollowebs/standards-development/company-standard/com-std-request-list/com-std-request-list.component";
import {IntStdResponsesListComponent} from "./apollowebs/standards-development/international-standard/int-std-responses-list/int-std-responses-list.component";
import {ComStdJcJustificationComponent} from "./apollowebs/standards-development/company-standard/com-std-jc-justification/com-std-jc-justification.component";
import {IntStdJustificationListComponent} from "./apollowebs/standards-development/international-standard/int-std-justification-list/int-std-justification-list.component";
import {IntStdCommentsComponent} from "./apollowebs/standards-development/international-standard/int-std-comments/int-std-comments.component";
import {CompanyStandardRequestComponent} from "./apollowebs/standards-development/company-standard/company-standard-request/company-standard-request.component";
import {ComStdJcJustificationListComponent} from "./apollowebs/standards-development/company-standard/com-std-jc-justification-list/com-std-jc-justification-list.component";
import {RequestStandardReviewComponent} from "./apollowebs/standards-development/systemic-review/request-standard-review/request-standard-review.component";
import {SystemicReviewCommentsComponent} from "./apollowebs/standards-development/systemic-review/systemic-review-comments/systemic-review-comments.component";
import {IntStdJustificationAppComponent} from "./apollowebs/standards-development/international-standard/int-std-justification-app/int-std-justification-app.component";
import {IsProposalFormComponent} from "./apollowebs/standards-development/international-standard/international-standard-proposal/is-proposal-form/is-proposal-form.component";
import {SystemicAnalyseCommentsComponent} from "./apollowebs/standards-development/systemic-review/systemic-analyse-comments/systemic-analyse-comments.component";
import {UserManagementProfileComponent} from './apollowebs/usermanagement/user-management-profile/user-management-profile.component';
import {NepNotificationComponent} from './apollowebs/standards-development/nep-notification/nep-notification.component';
import {StandardRequestComponent} from './apollowebs/standards-development/standard-request/standard-request.component';
import {RequestStandardFormComponent} from './apollowebs/standards-development/standard-request/request-standard-form/request-standard-form.component';
import {ReviewApplicationsComponent} from './apollowebs/standards-development/standard-request/review-applications/review-applications.component';
import {StandardTaskComponent} from './apollowebs/standards-development/standard-request/standard-task/standard-task.component';
import {ModalModule} from "ngb-modal";
import {DataTablesModule} from "angular-datatables";
import {ViewDiDeclarationDocumentsComponent} from "./apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-di-declaration-documents.component";
import {ViewIdfDocumentDetailsComponent} from "./apollowebs/di/view-single-consignment-document/view-idf-document-details/view-idf-document-details.component";
import {ViewDeclarationDocumentsItemDetailsListComponent} from "./apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-declaration-documents-item-details-list/view-declaration-documents-item-details-list.component";
import {MvInspectionUploadFileReportComponent} from "./apollowebs/di/motor-vehicle-inspection-single-view/mv-inspection-upload-file-report/mv-inspection-upload-file-report.component";
import {AttachmentListComponent} from './apollowebs/di/view-single-consignment-document/attachment-list/attachment-list.component';
import {AttachmentDialogComponent} from './apollowebs/di/view-single-consignment-document/attachment-dialog/attachment-dialog.component';
import {AssignOfficerComponent} from "./apollowebs/di/forms/assign-officer/assign-officer.component";
import {AssignPortComponent} from "./apollowebs/di/forms/assign-port/assign-port.component";
import {SendCoiComponent} from "./apollowebs/di/forms/send-coi/send-coi.component";
import {GenerateLocalCocComponent} from "./apollowebs/di/forms/generate-local-coc/generate-local-coc.component";
import {GenerateLocalCorComponent} from "./apollowebs/di/forms/generate-local-cor/generate-local-cor.component";
import {CompliantComponent} from "./apollowebs/di/forms/compliant/compliant.component";
import {ReAssignOfficerComponent} from "./apollowebs/di/forms/re-assign-officer/re-assign-officer.component";
import {BlacklistComponent} from "./apollowebs/di/forms/blacklist/blacklist.component";
import {BlacklistApproveComponent} from "./apollowebs/di/forms/blacklist-approve/blacklist-approve.component";
import {TargetItemComponent} from "./apollowebs/di/forms/target-item/target-item.component";
import {TargetSupervisorComponent} from "./apollowebs/di/forms/target-supervisor/target-supervisor.component";
import {ManualAssignOfficerComponent} from "./apollowebs/di/forms/manual-assign-officer/manual-assign-officer.component";
import {TargetApproveItemComponent} from "./apollowebs/di/forms/target-approve-item/target-approve-item.component";
import {SendDemandNoteTokwsComponent} from "./apollowebs/di/forms/send-demand-note-tokws/send-demand-note-tokws.component";
import {ViewRemarksHistoryComponent} from './apollowebs/di/view-single-consignment-document/view-remarks-history/view-remarks-history.component';
import {ItemDetailsComponent} from './apollowebs/di/view-single-consignment-document/item-details-list-view/item-details/item-details.component';
import {ProcessRejectionComponent} from "./apollowebs/di/forms/process-rejection/process-rejection.component";
import {MatFormFieldModule} from "@angular/material/form-field";
import { ChecklistsComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/checklists/checklists.component';
import { MinistryInspectionRequestComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/ministry-inspection-request/ministry-inspection-request.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {DiCorComponent} from "./apollowebs/di/view-single-consignment-document/di-cor/di-cor.component";
import {DiCocItemDetailsComponent} from "./apollowebs/di/view-single-consignment-document/di-coc/di-coc-item-details/di-coc-item-details.component";
import {DiCocComponent} from "./apollowebs/di/view-single-consignment-document/di-coc/di-coc.component";
import { ViewTasksComponent } from './apollowebs/di/view-tasks/view-tasks.component';
import {MatButtonModule} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import { DemandNoteListComponent } from './apollowebs/di/demand-note-list/demand-note-list.component';
import { ChecklistDataFormComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/checklist-data-form/checklist-data-form.component';
import { AgrochemInspectionChecklistComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/checklist-data-form/agrochem-inspection-checklist/agrochem-inspection-checklist.component';
import { EngineeringInspectionChecklistComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/checklist-data-form/engineering-inspection-checklist/engineering-inspection-checklist.component';
import { OtherInspectionChecklistComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/checklist-data-form/other-inspection-checklist/other-inspection-checklist.component';
import { VehicleInspectionChecklistComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/checklist-data-form/vehicle-inspection-checklist/vehicle-inspection-checklist.component';
import { SsfDetailsFormComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/ssf-details-form/ssf-details-form.component';
import { ApproveRejectItemComponent } from './apollowebs/di/view-single-consignment-document/item-details-list-view/approve-reject-item/approve-reject-item.component';
import { ViewDemandNoteComponent } from './apollowebs/di/demand-note-list/view-demand-note/view-demand-note.component';


@NgModule({
    imports: [
        AppRoutingModule,
        RouterModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        DragDropModule,
        MdModule,
        CommonModule,
        BrowserModule,
        ModalModule,
        RouterModule,
        CoreModule,
        PdfViewerModule,
        NgxExtendedPdfViewerModule,
        FileUploadModule,
        NgxSpinnerModule,
        DataTablesModule,
        NgxPaginationModule,
        Ng2SmartTableModule,
        NgbNavModule,
        MatDialogModule,
        MatCheckboxModule,
        MatSelectModule,
        MatIconModule,
        MatStepperModule,
        MatOptionModule,
        MatInputModule,
        NavbarModule,
        ToastrModule.forRoot({
            timeOut: 10000,
            enableHtml: true,
            newestOnTop: false,
            maxOpened: 1,
            autoDismiss: false,
            positionClass: 'toast-top-right',
            preventDuplicates: true,
        }),
        SidebarModule,
        MatFormFieldModule,
        MatButtonModule,
        NgbPaginationModule,
        MatTableModule,
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        ResetCredentialsComponent,
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
        NwaJustificationTasksComponent,
        NwaKnwSecTasksComponent,
        NwaDiSdtTasksComponent,
        NwaHopTasksComponent,
        SacSecTasksComponent,
        HoSicTasksComponent,
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
        ComStdJcJustificationComponent,
        ComStdJcJustificationListComponent,
        InformationcheckComponent,
        DivisionresponseComponent,
        NationalEnquiryPointComponent,
        MakeEnquiryComponent,
        UserManagementProfileComponent,
        NepNotificationComponent,
        StandardRequestComponent,
        RequestStandardFormComponent,
        ReviewApplicationsComponent,
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
        AdminLayoutComponent,
        AuthLayoutComponent,
        ImportInspectionComponent,
        ExceptionsApplicationComponent,
        WaiverApplicationComponent,
        GoodsDetailsComponent,
        WaiverSubmittedDialogComponent,
        ManufacturerDetailsComponent,
        ChecklistsComponent,
        MinistryInspectionRequestComponent,
        DiCorComponent,
        DiCocComponent,
        DiCocItemDetailsComponent,
        ViewTasksComponent,
        DemandNoteListComponent,
        ChecklistDataFormComponent,
        AgrochemInspectionChecklistComponent,
        EngineeringInspectionChecklistComponent,
        OtherInspectionChecklistComponent,
        VehicleInspectionChecklistComponent,
        SsfDetailsFormComponent,
        ApproveRejectItemComponent,
        ViewDemandNoteComponent
    ],
    providers: [
        MatNativeDateModule,
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}