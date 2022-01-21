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
import {AddBranchComponent} from "./apollowebs/company/branch/add-branch/add-branch.component";
import {OtpComponent} from "./views/registration/otp/otp.component";
import {PdfViewComponent} from "./pdf-view/pdf-view.component";
import {TaskManagerComponent} from "./apollowebs/task-manager/task-manager.component";
import {AddUserComponent} from "./apollowebs/company/branch/add-user/add-user.component";
import {ImportInspectionComponent} from './apollowebs/pvoc/import-inspection/import-inspection.component';
import {ExceptionsApplicationComponent} from './apollowebs/pvoc/exceptions-application/exceptions-application.component';
import {ImportationWaiverComponent} from './apollowebs/pvoc/importation-waiver/importation-waiver.component';
import {ConsignmentDocumentListComponent} from './apollowebs/di/consignment-document-list/consignment-document-list.component';
import {ViewSingleConsignmentDocumentComponent} from './apollowebs/di/view-single-consignment-document/view-single-consignment-document.component';
import {MinistryInspectionHomeComponent} from './apollowebs/di/ministry-inspection-home/ministry-inspection-home.component';
import {MotorVehicleInspectionSingleViewComponent} from './apollowebs/di/motor-vehicle-inspection-single-view/motor-vehicle-inspection-single-view.component';
import {NwaJustificationFormComponent} from './apollowebs/standards-development/workshop-agreement/nwa-justification-form/nwa-justification-form.component';
import {NwaJustificationTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-justification-tasks/nwa-justification-tasks.component';
import {NwaKnwSecTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-knw-sec-tasks/nwa-knw-sec-tasks.component';
import {NwaDiSdtTasksComponent} from './apollowebs/standards-development/workshop-agreement/nwa-di-sdt-tasks/nwa-di-sdt-tasks.component';
import {SacSecTasksComponent} from './apollowebs/standards-development/workshop-agreement/sac-sec-tasks/sac-sec-tasks.component';
import {HoSicTasksComponent} from './apollowebs/standards-development/workshop-agreement/ho-sic-tasks/ho-sic-tasks.component';
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
import {ComStdApproveJustificationComponent} from "./apollowebs/standards-development/company-standard/com-std-approve-justification/com-std-approve-justification.component";
import {ComStdDraftComponent} from "./apollowebs/standards-development/company-standard/com-std-draft/com-std-draft.component";
import {ComStdUploadComponent} from "./apollowebs/standards-development/company-standard/com-std-upload/com-std-upload.component";
import {ComStdConfirmComponent} from "./apollowebs/standards-development/company-standard/com-std-confirm/com-std-confirm.component";
import {SpcSecTaskComponent} from "./apollowebs/standards-development/standard-request/spc-sec-task/spc-sec-task.component";
import {AllpermitsComponent} from "./apollowebs/quality-assurance/allpermits/allpermits.component";
import {NepNotificationComponent} from "./apollowebs/standards-development/nep-notification/nep-notification.component";
import {ManagernotificationsComponent} from "./apollowebs/standards-development/managernotifications/managernotifications.component";
import {CreateDepartmentComponent} from "./apollowebs/standards-development/standard-request/create-department/create-department.component";
import {CreatetechnicalcommitteeComponent} from "./apollowebs/standards-development/standard-request/createtechnicalcommittee/createtechnicalcommittee.component";
import {IntStdUploadStandardComponent} from "./apollowebs/standards-development/international-standard/int-std-upload-standard/int-std-upload-standard.component";
import {IntStdGazzetteComponent} from "./apollowebs/standards-development/international-standard/int-std-gazzette/int-std-gazzette.component";
import {CreateproductComponent} from "./apollowebs/standards-development/standard-request/createproduct/createproduct.component";
import {CreateproductSubCategoryComponent} from "./apollowebs/standards-development/standard-request/createproduct-sub-category/createproduct-sub-category.component";
import {RoleSwitcherComponent} from "./apollowebs/standards-levy/standards-levy-home/role-switcher/role-switcher.component";
import {CustomerRegistrationComponent} from "./apollowebs/standards-levy/standards-levy-home/customer-registration/customer-registration.component";
import {StandardsLevyHomeComponent} from "./apollowebs/standards-levy/standards-levy-home/standards-levy-home.component";
import {ComStandardLevyComponent} from "./apollowebs/standards-levy/com-standard-levy/com-standard-levy.component";
import {ComPaymentHistoryComponent} from "./apollowebs/standards-levy/com-payment-history/com-payment-history.component";
import {ComStdLevyFormComponent} from "./apollowebs/standards-levy/com-std-levy-form/com-std-levy-form.component";
import {StandardLevyDashboardComponent} from "./apollowebs/standards-levy/standard-levy-dashboard/standard-levy-dashboard.component";
import {StandardLevyPaidComponent} from "./apollowebs/standards-levy/standard-levy-paid/standard-levy-paid.component";
import {StandardLevyPenaltiesComponent} from "./apollowebs/standards-levy/standard-levy-penalties/standard-levy-penalties.component";
import {StandardLevyDefaulterComponent} from "./apollowebs/standards-levy/standard-levy-defaulter/standard-levy-defaulter.component";
import {StandardLevyPenaltyHistoryComponent} from "./apollowebs/standards-levy/standard-levy-penalty-history/standard-levy-penalty-history.component";
import {StandardLevyPaidHistoryComponent} from "./apollowebs/standards-levy/standard-levy-paid-history/standard-levy-paid-history.component";
import {StandardLevyDefaulterHistoryComponent} from "./apollowebs/standards-levy/standard-levy-defaulter-history/standard-levy-defaulter-history.component";
import {StdTscSecTasksComponentComponent} from "./apollowebs/standards-development/standard-request/std-tsc-sec-tasks-component/std-tsc-sec-tasks-component.component";
import {StdTcTasksComponent} from "./apollowebs/standards-development/standard-request/std-tc-tasks/std-tc-tasks.component";
import {InvoiceConsolidateComponent} from "./apollowebs/quality-assurance/invoice-consolidate/invoice-consolidate.component";
import {FmarkApplicationComponent} from "./apollowebs/quality-assurance/fmark-application/fmark-application.component";
import {SmarkComponent} from "./apollowebs/quality-assurance/smark/smark.component";
import {ViewDiDeclarationDocumentsComponent} from "./apollowebs/di/view-single-consignment-document/view-di-declaration-documents/view-di-declaration-documents.component";
import {ViewIdfDocumentDetailsComponent} from "./apollowebs/di/view-single-consignment-document/view-idf-document-details/view-idf-document-details.component";
import {ItemDetailsComponent} from "./apollowebs/di/view-single-consignment-document/item-details-list-view/item-details/item-details.component";
import {ViewTasksComponent} from "./apollowebs/di/view-tasks/view-tasks.component";
import {DiCorComponent} from "./apollowebs/di/view-single-consignment-document/di-cor/di-cor.component";
import {DiCocComponent} from "./apollowebs/di/view-single-consignment-document/di-coc/di-coc.component";
import {ViewInspectionDetailsComponent} from "./apollowebs/di/view-single-consignment-document/view-inspection-details/view-inspection-details.component";
import {ChecklistDataFormComponent} from "./apollowebs/di/view-single-consignment-document/checklist-data-form/checklist-data-form.component";
import {InspectionDashboardComponent} from "./apollowebs/di/inspection-dashboard/inspection-dashboard.component";
import {LabResultsComponent} from "./apollowebs/di/view-single-consignment-document/item-details-list-view/lab-results/lab-results.component";
import {CurrencyExchangeRatesComponent} from "./apollowebs/di/currency-exchange-rates/currency-exchange-rates.component";
import {MessageDashboardComponent} from "./apollowebs/di/message-dashboard/message-dashboard.component";
import {TransactionViewComponent} from "./apollowebs/di/transaction-view/transaction-view.component";
import {ViewClientsComponent} from "./apollowebs/system/clients/view-clients/view-clients.component";
import {ViewPartnersComponent} from "./apollowebs/pvoc/partners/view-partners/view-partners.component";
import {ViewPartnerDetailsComponent} from "./apollowebs/pvoc/partners/view-partner-details/view-partner-details.component";
import {IsmApplicationsComponent} from "./apollowebs/di/ism/ism-applications/ism-applications.component";
import {ViewIsmApplicationComponent} from "./apollowebs/di/ism/view-ism-application/view-ism-application.component";
import {ViewCorporateCustomersComponent} from "./apollowebs/invoice/corporate/view-corporate-customers/view-corporate-customers.component";
import {ViewCorporateComponent} from "./apollowebs/invoice/corporate/view-corporate/view-corporate.component";
import {ViewBillLimitsComponent} from "./apollowebs/invoice/limits/view-bill-limits/view-bill-limits.component";
import {ViewTransactionsComponent} from "./apollowebs/invoice/corporate/view-transactions/view-transactions.component";
import {ViewAuctionItemsComponent} from "./apollowebs/di/auction/view-auction-items/view-auction-items.component";
import {AuctionItemDetailsComponent} from "./apollowebs/di/auction/auction-item-details/auction-item-details.component";
import {ViewComplaintsComponent} from "./apollowebs/pvoc/complaints/view-complaints/view-complaints.component";
import {ViewComplaintDetailsComponent} from "./apollowebs/pvoc/complaints/view-complaint-details/view-complaint-details.component";
import {ViewWaiverApplicationsComponent} from "./apollowebs/pvoc/waivers/view-waiver-applications/view-waiver-applications.component";
import {ViewWaiverDetailsComponent} from "./apollowebs/pvoc/waivers/view-waiver-details/view-waiver-details.component";
import {ViewExemptionApplicationsComponent} from "./apollowebs/pvoc/exemptions/view-exemption-applications/view-exemption-applications.component";
import {ViewExemptionDetailsComponent} from "./apollowebs/pvoc/exemptions/view-exemption-details/view-exemption-details.component";

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
    {
        path: 'company',
        component: ImportInspectionComponent,
        children: [
            {
                path: 'applications',
                component: ImportInspectionComponent
            }
        ]
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
                        pathMatch: 'full'
                    },
                    {
                        path: 'applications',
                        component: ViewWaiverApplicationsComponent,
                        pathMatch: 'full'
                    },
                    {
                        path: 'application/details/:id',
                        component: ViewWaiverDetailsComponent
                    }
                ]
            },
            {
                path: 'exemption',
                children: [
                    {
                        path: 'apply',
                        component: ExceptionsApplicationComponent
                    },
                    {
                        path: 'applications',
                        component: ViewExemptionApplicationsComponent
                    },
                    {
                        path: 'view/:id',
                        component: ViewExemptionDetailsComponent
                    }
                ]

            },
            {
                path: 'partners',
                children: [
                    {
                        path: '',
                        component: ViewPartnersComponent
                    },
                    {
                        path: 'view/:id',
                        component: ViewPartnerDetailsComponent
                    }
                ]
            },
            {
                path: "complaints",
                children: [
                    {
                        path: '',
                        component: ViewComplaintsComponent
                    },
                    {
                        path: ':id',
                        component: ViewComplaintDetailsComponent
                    }
                ]
            }
        ]
    },
    {
        path: 'tasks',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: ViewTasksComponent
            }
        ]
    },
    {
        path: 'demand/notes',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: TransactionViewComponent
            }
        ]
    },
    {
        path: 'di',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: '',
                component: ConsignmentDocumentListComponent,
                pathMatch: 'full'
            },
            {
                path: 'declaration/document/:id',
                component: ViewDiDeclarationDocumentsComponent
            },
            {
                path: 'inspection/checklist/:id',
                canActivate: [RouteGuard],
                component: ChecklistDataFormComponent
            },
            {
                path: 'cor/details/:id',
                canActivate: [RouteGuard],
                component: DiCorComponent
            },
            {
                path: 'certificate/:docType/details/:id',
                canActivate: [RouteGuard],
                component: DiCocComponent
            },
            {
                path: 'idf/details/:id',
                canActivate: [RouteGuard],
                component: ViewIdfDocumentDetailsComponent
            },
            {
                path: 'item/:cdUuid/:id',
                canActivate: [RouteGuard],
                component: ItemDetailsComponent
            },
            {
                path: 'item/lab-results/:id/:page/:cdUuid',
                canActivate: [RouteGuard],
                component: LabResultsComponent
            },
            {
                path: ':id',
                canActivate: [RouteGuard],
                component: ViewSingleConsignmentDocumentComponent
            },
            {
                path: 'version/:id',
                canActivate: [RouteGuard],
                component: ViewSingleConsignmentDocumentComponent
            },
            {
                path: 'checklist/details/:id',
                canActivate: [RouteGuard],
                component: ViewInspectionDetailsComponent
            },
            {
                path: 'auction/view',
                canActivate: [RouteGuard],
                component: ViewAuctionItemsComponent
            },
            {
                path: 'auction/details/:id',
                canActivate: [RouteGuard],
                component: AuctionItemDetailsComponent
            },
            {
                path: 'kentrade/exchange/messages',
                canActivate: [RouteGuard],
                component: MessageDashboardComponent
            },
            {
                path: 'ism',
                children: [
                    {
                        path: 'requests',
                        canActivate: [RouteGuard],
                        component: IsmApplicationsComponent
                    },
                    {
                        path: 'request/:id',
                        // canActivate: [RouteGuard],
                        component: ViewIsmApplicationComponent
                    }
                ]
            },
        ]
    },
    {
        path: 'dashboard',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'inspection',
                canActivate: [RouteGuard],
                component: InspectionDashboardComponent
            }
        ]
    },
    {
        path: 'transaction',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'demand-notes',
                component: TransactionViewComponent
            },
            {
                path: 'corporates-customers',
                component: ViewCorporateCustomersComponent
            },
            {
                path: 'corporate/:id',
                component: ViewCorporateComponent
            },
            {
                path: 'exchange-rates',
                component: CurrencyExchangeRatesComponent
            },
            {
                path: 'bill/:id/:cid',
                component: ViewTransactionsComponent
            },
            {
                path: 'limits',
                component: ViewBillLimitsComponent
            }
        ]
    },
    {
        path: 'currency',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'rates',
                component: CurrencyExchangeRatesComponent
            }
        ]
    },
    {
        path: 'system',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'api-clients',
                component: ViewClientsComponent
            }
        ]
    },
    {
        path: 'ministry',
        component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [
            {
                path: 'inspection',
                canActivate: [RouteGuard],
                component: MinistryInspectionHomeComponent
            },
            {
                path: 'inspection/:id',
                canActivate: [RouteGuard],
                component: MotorVehicleInspectionSingleViewComponent
            }
        ]
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
        // canActivate: [RouteGuard],
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
    {
        path: 'isUploadStd', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdUploadStandardComponent}]
    },
    {
        path: 'isUploadNotice', component: AdminLayoutComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: IntStdGazzetteComponent}]
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
    {
        path: 'spc-sec-tc-task', component: AdminLayoutComponent,
        children: [{path: '', component: SpcSecTaskComponent}]
    },
    {
        path: 'department', component: AdminLayoutComponent,
        children: [{path: '', component: CreateDepartmentComponent}]
    },
    {
        path: 'technicalCommittee', component: AdminLayoutComponent,
        children: [{path: '', component: CreatetechnicalcommitteeComponent}]
    },
    {
        path: 'productCategory', component: AdminLayoutComponent,
        children: [{path: '', component: CreateproductComponent}]
    },
    {
        path: 'productSubCategory', component: AdminLayoutComponent,
        children: [{path: '', component: CreateproductSubCategoryComponent}]
    },

    // STANDARDS LEVY
    {
        path: 'roleSwitcher', component: StandardsLevyHomeComponent,
        children: [{path: '', component: RoleSwitcherComponent}]
    },
    {
        path: 'levyRegistration', component: StandardsLevyHomeComponent,
        canActivate: [RouteGuard],
        children: [{path: '', component: CustomerRegistrationComponent}]
    },
    {
        path: 'comStdLevy', component: AdminLayoutComponent,
        children: [{path: '', component: ComStandardLevyComponent}]
    },
    {
        path: 'comPaymentHistory', component: AdminLayoutComponent,
        children: [{path: '', component: ComPaymentHistoryComponent}]
    },
    {
        path: 'comStdLevyForm', component: AdminLayoutComponent,
        children: [{path: '', component: ComStdLevyFormComponent}]
    },
    {
        path: 'stdLevyHome', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyDashboardComponent}]
    },
    {
        path: 'stdLevyPaid', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPaidComponent}]
    },
    {
        path: 'stdLevyPenalties', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPenaltiesComponent}]
    },
    {
        path: 'stdLevyDefaulters', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyDefaulterComponent}]
    },
    {
        path: 'stdLevyPaidHistory', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPaidHistoryComponent}]
    },
    {
        path: 'stdLevyPenaltiesHistory', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyPenaltyHistoryComponent}]
    },
    {
        path: 'stdLevyDefaultersHistory', component: AdminLayoutComponent,
        children: [{path: '', component: StandardLevyDefaulterHistoryComponent}]
    },


];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
