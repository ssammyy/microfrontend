import {RouterModule, Routes} from '@angular/router';

import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {DashboardComponent} from './apollowebs/dashboard/dashboard.component';
import {NgModule} from '@angular/core';
import {DmarkComponent} from './apollowebs/dmark/dmark.component';
import {FmarkallappsComponent} from './apollowebs/fmarkallapps/fmarkallapps.component';
import {St10FormComponent} from './apollowebs/st10-form/st10-form.component';
import {RegistrationComponent} from './views/registration.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {ResetCredentialsComponent} from './views/registration/reset-credentials.component';
import {RouteGuard} from './core/route-guard/route.guard';
import {LoginComponent} from './views/registration/login.component';
import {PermitReportComponent} from './apollowebs/permit-report/permit-report.component';
import {NewSmarkPermitComponent} from './apollowebs/new-smark-permit/new-smark-permit.component';
import {NewDmarkPermitComponent} from './apollowebs/new-dmark-permit/new-dmark-permit.component';
import {DmarkApplicationsAllComponent} from './apollowebs/dmark-applications-all/dmark-applications-all.component';
import {InvoiceComponent} from './apollowebs/invoice/invoice.component';
import {InvoiceDetailsComponent} from './apollowebs/invoice-details/invoice-details.component';
import {CompaniesList} from './apollowebs/company/companies.list';
import {CompanyComponent} from './apollowebs/company/company.component';
import {BranchComponent} from './apollowebs/company/branch/branch.component';
import {BranchList} from './apollowebs/company/branch/branch.list';
import {UserComponent} from './apollowebs/company/branch/users/user.component';
import {UserList} from './apollowebs/company/branch/users/user.list';
import {SmarkApplicationsAllComponent} from './apollowebs/smark-applications-all/smark-applications-all.component';
import {UserProfileMainComponent} from './apollowebs/userprofilemain/user-profile-main.component';
import {AddBranchComponent} from "./apollowebs/company/branch/add-branch/add-branch.component";
import {OtpComponent} from "./views/registration/otp/otp.component";
import {InvoiceConsolidateComponent} from "./apollowebs/invoice-consolidate/invoice-consolidate.component";
import {PdfViewComponent} from "./pdf-view/pdf-view.component";
import {TaskManagerComponent} from "./apollowebs/task-manager/task-manager.component";
import {AddUserComponent} from "./apollowebs/company/branch/add-user/add-user.component";
import {FmarkApplicationComponent} from "./apollowebs/fmark-application/fmark-application.component";

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

const routes: Routes = [
    {
        path: '',
        redirectTo: 'dashboard',
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
        path: 'company/branches', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: BranchList}]
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
        path: 'fmark/fMarkAllApp', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: FmarkallappsComponent}]
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
        path: 'dmark/all_dmark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: DmarkApplicationsAllComponent}]
    },
    {
        path: 'profile', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: UserProfileMainComponent}]
    },
    {
        path: 'smark/all_smark', component: AdminLayoutComponent,
        canActivate: [RouteGuard],

        children: [{path: '', component: SmarkApplicationsAllComponent}]
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
        path: 'fmark/appplication', component: AdminLayoutComponent,
        children: [{path: '', component: FmarkApplicationComponent}]
    },
    {
        path: 'invoice_test', component: PdfViewComponent
    }
];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
