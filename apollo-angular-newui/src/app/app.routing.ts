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
import {DmarkApplicationsAllComponent} from './apollowebs/quality-assurance/dmark-applications-all/dmark-applications-all.component';
import {InvoiceComponent} from './apollowebs/quality-assurance/invoice/invoice.component';
import {InvoiceDetailsComponent} from './apollowebs/quality-assurance/invoice-details/invoice-details.component';
import {CompaniesList} from './apollowebs/company/companies.list';
import {CompanyComponent} from './apollowebs/company/company.component';
import {BranchComponent} from './apollowebs/company/branch/branch.component';
import {BranchList} from './apollowebs/company/branch/branch.list';
import {UserComponent} from './apollowebs/company/branch/users/user.component';
import {UserList} from './apollowebs/company/branch/users/user.list';
import {SmarkApplicationsAllComponent} from './apollowebs/quality-assurance/smark-applications-all/smark-applications-all.component';
import {UserProfileMainComponent} from './apollowebs/userprofilemain/user-profile-main.component';
import {AddBranchComponent} from './apollowebs/company/branch/add-branch/add-branch.component';
import {OtpComponent} from './views/registration/otp/otp.component';
import {InvoiceConsolidateComponent} from './apollowebs/quality-assurance/invoice-consolidate/invoice-consolidate.component';
import {PdfViewComponent} from './pdf-view/pdf-view.component';
import {TaskManagerComponent} from './apollowebs/task-manager/task-manager.component';
import {AddUserComponent} from './apollowebs/company/branch/add-user/add-user.component';
import {FmarkApplicationComponent} from './apollowebs/quality-assurance/fmark-application/fmark-application.component';
import {SmarkComponent} from './apollowebs/quality-assurance/smark/smark.component';
import {NwaJustificationFormComponent} from './apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component';
import {NwaJustificationTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-justification-tasks/nwa-justification-tasks.component';
import {NwaKnwSecTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-knw-sec-tasks/nwa-knw-sec-tasks.component';
import {NwaDiSdtTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-di-sdt-tasks/nwa-di-sdt-tasks.component';
import {SacSecTasksComponent} from './apollowebs/standards-development/workshop-agreement/sac-sec-tasks/sac-sec-tasks.component';
import {HoSicTasksComponent} from './apollowebs/standards-development/ho-sic-tasks/ho-sic-tasks.component';
import {NwaHopTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-hop-tasks/nwa-hop-tasks.component';
import {IsProposalFormComponent} from './apollowebs/standards-development/international-standard/international-standard-proposal/is-proposal-form/is-proposal-form.component';
import {ReviewStandardsComponent} from './apollowebs/standards-development/systemic-review/request-standard-review/review-standards/review-standards.component';
import {CsRequestFormComponent} from './apollowebs/standards-development/company-standard/company-standard-request/cs-request-form/cs-request-form.component';
import {InformationcheckComponent} from './apollowebs/standards-development/informationcheck/informationcheck.component';
import {DivisionresponseComponent} from './apollowebs/standards-development/divisionresponse/divisionresponse.component';
import {MakeEnquiryComponent} from './apollowebs/standards-development/national-enquiry-point/make-enquiry/make-enquiry.component';
import {ComStdRequestListComponent} from './apollowebs/standards-development/company-standard/com-std-request-list/com-std-request-list.component';
import {IntStdResponsesListComponent} from './apollowebs/standards-development/international-standard/int-std-responses-list/int-std-responses-list.component';
import {ComStdJcJustificationComponent} from './apollowebs/standards-development/company-standard/com-std-jc-justification/com-std-jc-justification.component';
import {IntStdJustificationListComponent} from './apollowebs/standards-development/international-standard/int-std-justification-list/int-std-justification-list.component';
import {IntStdCommentsComponent} from './apollowebs/standards-development/international-standard/int-std-comments/int-std-comments.component';
import {ComStdJcJustificationListComponent} from './apollowebs/standards-development/company-standard/com-std-jc-justification-list/com-std-jc-justification-list.component';
import {SystemicReviewCommentsComponent} from './apollowebs/standards-development/systemic-review/systemic-review-comments/systemic-review-comments.component';
import {IntStdJustificationAppComponent} from './apollowebs/standards-development/international-standard/int-std-justification-app/int-std-justification-app.component';
import {SystemicAnalyseCommentsComponent} from './apollowebs/standards-development/systemic-review/systemic-analyse-comments/systemic-analyse-comments.component';
import {UsermanagementComponent} from './apollowebs/usermanagement/usermanagement.component';
import {UserManagementProfileComponent} from './apollowebs/usermanagement/user-management-profile/user-management-profile.component';
import {RequestStandardFormComponent} from './apollowebs/standards-development/standard-request/request-standard-form/request-standard-form.component';
import {StandardRequestComponent} from './apollowebs/standards-development/standard-request/standard-request.component';
import {StandardTaskComponent} from './apollowebs/standards-development/standard-request/standard-task/standard-task.component';
import {SmarkAllAwardedApplicationsComponent} from './apollowebs/quality-assurance/smark-all-awarded-applications/smark-all-awarded-applications.component';
import {FmarkAllAwardedApplicationsComponent} from './apollowebs/quality-assurance/fmark-all-awarded-applications/fmark-all-awarded-applications.component';
import {DmarkAllAwardedApplicationsComponent} from './apollowebs/quality-assurance/dmark-all-awarded-applications/dmark-all-awarded-applications.component';
import {QaTaskDetailsComponent} from './apollowebs/quality-assurance/qa-task-details/qa-task-details.component';
import {CompanyViewComponent} from './apollowebs/company/company-view/company-view.component';
import {BranchViewComponent} from './apollowebs/company/branch/branch-view/branch-view.component';
import {QrCodeDetailsComponent} from './apollowebs/quality-assurance/qr-code-details/qr-code-details.component';
import {StdTscSecTasksComponentComponent} from './apollowebs/standards-development/standard-request/std-tsc-sec-tasks-component/std-tsc-sec-tasks-component.component';
import {StdTcTasksComponent} from './apollowebs/standards-development/standard-request/std-tc-tasks/std-tc-tasks.component';
import {ComStdApproveJustificationComponent} from "./apollowebs/standards-development/company-standard/com-std-approve-justification/com-std-approve-justification.component";
import {ComStdDraftComponent} from "./apollowebs/standards-development/company-standard/com-std-draft/com-std-draft.component";
import {ComStdUploadComponent} from "./apollowebs/standards-development/company-standard/com-std-upload/com-std-upload.component";
import {ComStdConfirmComponent} from "./apollowebs/standards-development/company-standard/com-std-confirm/com-std-confirm.component";
import {StdJustificationComponent} from "./apollowebs/standards-development/standard-request/std-justification/std-justification.component";
import {SpcSecTaskComponent} from "./apollowebs/standards-development/standard-request/spc-sec-task/spc-sec-task.component";
import {StdTcWorkplanComponent} from "./apollowebs/standards-development/standard-request/std-tc-workplan/std-tc-workplan.component";
import {AllpermitsComponent} from "./apollowebs/quality-assurance/allpermits/allpermits.component";
import {NepNotificationComponent} from "./apollowebs/standards-development/nep-notification/nep-notification.component";
import {ManagernotificationsComponent} from "./apollowebs/standards-development/managernotifications/managernotifications.component";
import {EpraBatchListComponent} from "./apollowebs/market-surveillance/fuel/epra-batch-list/epra-batch-list.component";
import {EpraListComponent} from "./apollowebs/market-surveillance/fuel/epra-list/epra-list.component";

// export const AppRoutes: Routes = [
//     {
//         path: '',
//         redirectTo: 'dashboard',
//         pathMatch: 'full',
//     }, {
//         path: '',
//         component: AdminLayoutComponent,
//         children: [
//             {
//                 path: '',
//                 loadChildren: './dashboard/dashboard.module#DashboardModule'
//             }, {
//                 path: 'components',
//                 loadChildren: './components/components.module#ComponentsModule'
//             }, {
//                 path: 'forms',
//                 loadChildren: './forms/forms.module#Forms'
//             }, {
//                 path: 'tables',
//                 loadChildren: './tables/tables.module#TablesModule'
//             }, {
//                 path: 'maps',
//                 loadChildren: './maps/maps.module#MapsModule'
//             }, {
//                 path: 'widgets',
//                 loadChildren: './widgets/widgets.module#WidgetsModule'
//             }, {
//                 path: 'charts',
//                 loadChildren: './charts/charts.module#ChartsModule'
//             }, {
//                 path: 'calendar',
//                 loadChildren: './calendar/calendar.module#CalendarModule'
//             }, {
//                 path: '',
//                 loadChildren: './userpage/user.module#UserModule'
//             }, {
//                 path: '',
//                 loadChildren: './timeline/timeline.module#TimelineModule'
//             }
//         ]
//     }, {
//         path: '',
//         component: AuthLayoutComponent,
//         children: [{
//             path: 'pages',
//             loadChildren: './pages/pages.module#PagesModule'
//         }]
//     }
// ];

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
                component: SignUpComponent
            },
            {
                path: 'register',
                component: SignUpComponent
            }

        ],
        data: {
            title: 'KEBS'
        }
    },
    {
        path: 'login',
        component: RegistrationComponent,

        children: [
            {
                path: '',
                component: LoginComponent
            },
            {
                path: 'reset',
                component: ResetCredentialsComponent
            },
            {
                path: 'otp',
                component: OtpComponent

            }

        ],
        data: {
            title: 'KEBS'
        }
    },
    {
        path: 'qr-code-qa-permit-scan', component: RegistrationComponent,
        children: [{path: '', component: QrCodeDetailsComponent}]
    },
    // {path: '**', component: AdminLayoutComponent},
    {
        path: 'dashboard', component: AdminLayoutComponent,
        canActivate: [RouteGuard]
        ,
        children: [
            {path: '', component: DashboardComponent},


        ]

    },
    {
        path: 'company/companies', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompaniesList}]

    },
    {
        path: 'company', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompanyComponent}]
    },
    {
        path: 'company/view', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CompanyViewComponent}]
    },
    {
        path: 'company/branches', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: BranchList}]
    },
    {
        path: 'companies/view/branch', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: BranchViewComponent}]
    },
    {
        path: 'branches', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: BranchList}]

    },
    {
        path: 'branches/add_branch', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: AddBranchComponent}]
    },
    {
        path: 'users/add_users', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: AddUserComponent}]
    },
    {
        path: 'companies/branch', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: BranchComponent}]
    },
    {
        path: 'companies/branches/users', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserList}]
    },
    {
        path: 'company/users', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserList}]
    },
    {
        path: 'companies/branches/user', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserComponent}]
    },


    {
        path: 'permitdetails', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkComponent}]
    },
    {
        canActivate: [RouteGuard],
        path: 'invoice/all_invoice', component: AdminLayoutComponent,
        children: [{path: '', component: InvoiceComponent}]
    },
    {
        path: 'invoiceDetails', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: InvoiceDetailsComponent}]
    },
    {
        path: 'user_management', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UsermanagementComponent}]
    },
    {
        path: 'fmark/fMarkAllApp', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: FmarkallappsComponent}]
    },
    {
        path: 'fmark/all_fmark_awarded', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: FmarkAllAwardedApplicationsComponent}]
    },
    {
        path: 'st10Form', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: St10FormComponent}]
    },
    {
        path: 'permitReport', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: PermitReportComponent}]
    },
    {
        path: 'smark/newSmarkPermit', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: NewSmarkPermitComponent}]
    },
    {
        path: 'dmark/newDmarkPermit', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: NewDmarkPermitComponent}]
    },
    {
        path: 'userDetails', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserManagementProfileComponent}]
    },
    {
        path: 'dmark/all_dmark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkApplicationsAllComponent}]
    },
    {
        path: 'dmark/all_dmark_awarded', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkAllAwardedApplicationsComponent}]
    },
    {
        path: 'profile', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserProfileMainComponent}]
    },
    {
        path: 'all_my_permits', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: AllpermitsComponent}]
    },
    {
        path: 'smark/all_smark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: SmarkApplicationsAllComponent}]
    },
    {
        path: 'smark/all_smark_awarded', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: SmarkAllAwardedApplicationsComponent}]
    },
    {
        path: 'invoice/consolidate_invoice', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: InvoiceConsolidateComponent}]
    },
    {
        path: 'all_tasks_list', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: TaskManagerComponent}]
    },
    {
        path: 'all_qa_tasks_list', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: QaTaskDetailsComponent}]
    },
    {
        path: 'fmark/application', component: AdminLayoutComponent,
        children: [{path: '', component: FmarkApplicationComponent}]
    },
    {
        path: 'invoice_test', component: PdfViewComponent
    },

    {
        path: 'smarkpermitdetails', component: AdminLayoutComponent,
        children: [{path: '', component: SmarkComponent}]
    },

    // SD Kenya National Workshop Agreement
    {
        path: 'nwaJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaJustificationFormComponent}]
    },
    {
        path: 'nwaJustificationTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaJustificationTasksComponent}]
    },
    {
        path: 'nwaKnwSecTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaKnwSecTasksComponent}]
    },
    {
        path: 'nwaDirStTasks', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: NwaDiSdtTasksComponent}]
    },
    {
        path: 'nwaHopTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: NwaHopTasksComponent}]
    },
    {
        path: 'nwaSacSecTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SacSecTasksComponent}]
    },
    {
        path: 'nwaHoSicTasks', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: HoSicTasksComponent}]
    },

    // SD International Standards
    {
        path: 'isProposalForm', component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{path: '', component: IsProposalFormComponent}]
    },
    {
        path: 'isProposalComments', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdCommentsComponent}]
    },
    {
        path: 'isProposalResponses', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdResponsesListComponent}]
    },
    {
        path: 'isJustificationList', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdJustificationListComponent}]
    },
    {
        path: 'isJustificationApp', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdJustificationAppComponent}]
    },
    // SD SYSTEMIC REVIEW
    {
        path: 'requestStandardReview', component: AdminLayoutComponent,
        children: [{path: '', component: ReviewStandardsComponent}]
    },
    {
        path: 'systemicReviewComments', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicReviewCommentsComponent}]
    },
    {
        path: 'systemicRecommendations', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: SystemicAnalyseCommentsComponent}]
    },

    // SD COMPANY STANDARDS
    // {
    //     path: 'comStdRequest', component: AdminLayoutComponent,
    //     children: [{path: '', component: CsRequestFormComponent}]
    // },
    {
        path: 'comStdRequest',
        component: StandardRequestComponent,
        children: [{path: '', component: CsRequestFormComponent}]
    },
    {
        path: 'comStdList', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdRequestListComponent}]
    },
    {
        path: 'comStdJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdJcJustificationComponent}]
    },
    {
        path: 'comAppJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdApproveJustificationComponent}]
    },
    {
        path: 'comStdJustificationList', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdJcJustificationListComponent}]
    },
    {
        path: 'comStdAppJustification', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdApproveJustificationComponent}]
    },
    {
        path: 'comStdDraft', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdDraftComponent}]
    },
    {
        path: 'comStdConfirmation', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdUploadComponent}]
    },
    {
        path: 'comStdUpload', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: ComStdConfirmComponent}]
    },

    {
        path: 'nep_information_received', component: AdminLayoutComponent,
        children: [{path: '', component: InformationcheckComponent}]
    },
    {
        path: 'nep_division_response', component: AdminLayoutComponent,
        children: [{path: '', component: DivisionresponseComponent}]
    },
    {
        path: 'managernotification', component: AdminLayoutComponent,
        children: [{path: '', component: ManagernotificationsComponent}]
    },
    {
        path: 'nepnotification', component: AdminLayoutComponent,
        children: [{path: '', component: NepNotificationComponent}]
    },
    {
        path: 'make_enquiry', component: MakeEnquiryComponent
    },


    //  Request For Standards
    {
        path: 'request-standards',
        component: StandardRequestComponent,
        children: [{path: '', component: RequestStandardFormComponent}]
    },
    {
        path: 'ms-standards', component: AdminLayoutComponent,
        children: [{path: '', component: StandardTaskComponent}]
    },
    {
        path: 'std-tsc-sec-task', component: AdminLayoutComponent,
        children: [{path: '', component: StdTscSecTasksComponentComponent}]
    },
    {
        path: 'std-tc-task', component: AdminLayoutComponent,
        children: [{path: '', component: StdTcTasksComponent}]
    },

    /****************MS COMPONENTS START FROM HERE**********************************/
    {
        path: 'epra',
        component: AdminLayoutComponent,
        // canActivate: [RouteGuard],
        children: [{
                path: '',
                component: EpraBatchListComponent},
            {
                path: ':referenceNumber',
                component: EpraListComponent
            },
        ]
    },
    // {
    //     path: 'batchDetails:referenceNumber', component: AdminLayoutComponent,
    //     // canActivate: [RouteGuard],
    //     children: [{path: '', component: EpraListComponent}]
    // },

    /****************MS COMPONENTS ENDS HERE**********************************/

];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
