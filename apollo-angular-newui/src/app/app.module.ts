import {NgModule} from '@angular/core';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {MatNativeDateModule} from '@angular/material/core';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatSliderModule} from '@angular/material/slider';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatMenuModule} from '@angular/material/menu';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatListModule} from '@angular/material/list';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCardModule} from '@angular/material/card';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatStepperModule} from '@angular/material/stepper';
import {MatTabsModule} from '@angular/material/tabs';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatChipsModule} from '@angular/material/chips';
import {MatIconModule} from '@angular/material/icon';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatDialogModule} from '@angular/material/dialog';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatTableModule} from '@angular/material/table';
import {MatSortModule} from '@angular/material/sort';
import {MatPaginatorModule} from '@angular/material/paginator';

import {AppComponent} from './app.component';

import {SidebarModule} from './sidebar/sidebar.module';
import {FooterModule} from './shared/footer/footer.module';
import {NavbarModule} from './shared/navbar/navbar.module';
import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {AuthLayoutComponent} from './layouts/auth/auth-layout.component';

// import {routes} from './app.routing';
import {DashboardComponent} from './apollowebs/dashboard/dashboard.component';
import {InvoiceComponent} from './apollowebs/quality-assurance/invoice/invoice.component';
import {AppRoutingModule} from './app.routing';
import {MdModule} from './md/md.module';
import {FmarkComponent} from './apollowebs/quality-assurance/fmark/fmark.component';
import {DmarkComponent} from './apollowebs/quality-assurance/dmark/dmark.component';
import {SmarkComponent} from './apollowebs/quality-assurance/smark/smark.component';
import {FmarkallappsComponent} from './apollowebs/quality-assurance/fmarkallapps/fmarkallapps.component';
import {St10FormComponent} from './apollowebs/quality-assurance/st10-form/st10-form.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
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
import {FormsComponent} from './apollowebs/forms/forms.component';
import {FieldErrorDisplayComponent} from './apollowebs/forms/field-error-display/field-error-display.component';
import {NwaJustificationFormComponent} from "./apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component";
import {StandardsDevelopmentComponent} from "./apollowebs/standards-development/standards-development.component";
import {InformationcheckComponent} from './apollowebs/standards-development/informationcheck/informationcheck.component';
import {UsermanagementComponent} from './apollowebs/usermanagement/usermanagement.component';
import {NwaJustificationTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-justification-tasks/nwa-justification-tasks.component';
import {DataTablesModule} from "angular-datatables";
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
//import {ModalModule} from "ngb-modal";
import {ModalModule} from "ngb-modal";
import {SmarkAllAwardedApplicationsComponent} from './apollowebs/quality-assurance/smark-all-awarded-applications/smark-all-awarded-applications.component';
import {DmarkAllAwardedApplicationsComponent} from './apollowebs/quality-assurance/dmark-all-awarded-applications/dmark-all-awarded-applications.component';
import {FmarkAllAwardedApplicationsComponent} from './apollowebs/quality-assurance/fmark-all-awarded-applications/fmark-all-awarded-applications.component';
import {QaTaskDetailsComponent} from './apollowebs/quality-assurance/qa-task-details/qa-task-details.component';
import {CompanyViewComponent} from './apollowebs/company/company-view/company-view.component';
import {BranchViewComponent} from './apollowebs/company/branch/branch-view/branch-view.component';
import {QrCodeDetailsComponent} from './apollowebs/quality-assurance/qr-code-details/qr-code-details.component';
import {StdTscSecTasksComponentComponent} from './apollowebs/standards-development/standard-request/std-tsc-sec-tasks-component/std-tsc-sec-tasks-component.component';
import {NgMultiSelectDropDownModule} from "ng-multiselect-dropdown";
import {StdTcTasksComponent} from './apollowebs/standards-development/standard-request/std-tc-tasks/std-tc-tasks.component';
import {ComStdApproveJustificationComponent} from './apollowebs/standards-development/company-standard/com-std-approve-justification/com-std-approve-justification.component';
import {ComStdDraftComponent} from './apollowebs/standards-development/company-standard/com-std-draft/com-std-draft.component';
import {ComStdUploadComponent} from './apollowebs/standards-development/company-standard/com-std-upload/com-std-upload.component';
import {ComStdConfirmComponent} from './apollowebs/standards-development/company-standard/com-std-confirm/com-std-confirm.component';
import {NepSuccessComponent} from './apollowebs/standards-development/nep-success/nep-success.component';
import {ManagernotificationsComponent} from './apollowebs/standards-development/managernotifications/managernotifications.component';
import {AllpermitsComponent} from "./apollowebs/quality-assurance/allpermits/allpermits.component";
import {NgSelectModule} from "@ng-select/ng-select";
import { IntStdUploadStandardComponent } from './apollowebs/standards-development/international-standard/int-std-upload-standard/int-std-upload-standard.component';
import { IntStdGazzetteComponent } from './apollowebs/standards-development/international-standard/int-std-gazzette/int-std-gazzette.component';


// import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    exports: [
        MatAutocompleteModule,
        MatButtonToggleModule,
        MatCardModule,
        MatChipsModule,
        MatCheckboxModule,
        MatStepperModule,
        MatDialogModule,
        MatExpansionModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatSelectModule,
        MatDatepickerModule,
        MatButtonModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatSortModule,
        MatTableModule,
        MatTabsModule,
        MatToolbarModule,
        MatTooltipModule,
        MatNativeDateModule

    ],
    imports: [
        MdModule,
        CommonModule,
        BrowserModule,
        ModalModule,
        // NgbModule,
        //ModalModule,
        // TODO: Discuss as it seems to kill sending of requests to backend
        // EffectsModule.forRoot([]),
        // EntityDataModule.forRoot(entityConfig),
        ReactiveFormsModule,
        RouterModule,
        CoreModule,
        FormsModule,
        PdfViewerModule,
        NgxExtendedPdfViewerModule,
        FileUploadModule,
        NgxSpinnerModule,
        DataTablesModule,
        NgxPaginationModule,
        NgMultiSelectDropDownModule,
        NgSelectModule,
        NoopAnimationsModule
        // CoreModule
    ],
    declarations: [
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
        AllpermitsComponent,
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
        SmarkAllAwardedApplicationsComponent,
        DmarkAllAwardedApplicationsComponent,
        FmarkAllAwardedApplicationsComponent,
        QaTaskDetailsComponent,
        CompanyViewComponent,
        BranchViewComponent,
        QrCodeDetailsComponent,
        StdTscSecTasksComponentComponent,
        StdTcTasksComponent,
        ComStdApproveJustificationComponent,
        ComStdDraftComponent,
        ComStdUploadComponent,
        ComStdConfirmComponent,
        StdTcTasksComponent,
        NepSuccessComponent,
        ManagernotificationsComponent,
        IntStdUploadStandardComponent,
        IntStdGazzetteComponent

    ]

})
export class MaterialModule {}

@NgModule({
    imports: [
        CommonModule,
        BrowserAnimationsModule,
        FormsModule,
        PdfViewerModule,
        // RouterModule.forRoot(routes, {
        //     useHash: true
        // }),
        HttpClientModule,
        MaterialModule,
        SidebarModule,
        NavbarModule,
        FooterModule,
        RouterModule,
        AppRoutingModule,
        CoreModule,
        ToastrModule.forRoot({
            timeOut: 10000,
            enableHtml: true,
            newestOnTop: false,
            maxOpened: 1,
            autoDismiss: false,
            positionClass: 'toast-top-right',
            preventDuplicates: true,
        }),
        ReactiveFormsModule,
        FormsModule,
        // NgbModule,
        NgxPaginationModule,
        FontAwesomeModule,
        NgxSpinnerModule,
    ],
    declarations: [
        AppComponent,
        AdminLayoutComponent,
        AuthLayoutComponent
    ],
    providers : [
        MatNativeDateModule,
        // {provide: APP_BASE_HREF, useValue: '/migration/'}
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }
