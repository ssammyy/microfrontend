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
        children: [{path: '', component: CompaniesList}]

    },
    {
        path: 'company', component: AdminLayoutComponent,
        children: [{path: '', component: CompanyComponent}]
    },
    {
        path: 'company/branches', component: AdminLayoutComponent,
        children: [{path: '', component: BranchList}]
    },
    {
        path: 'branches', component: AdminLayoutComponent,
        children: [{path: '', component: BranchList}]

    },
    {
        path: 'branches/add_branch', component: AdminLayoutComponent,
        children: [{path: '', component: AddBranchComponent}]
    },

    {
        path: 'companies/branch', component: AdminLayoutComponent,
        children: [{path: '', component: BranchComponent}]
    },
    {
        path: 'companies/branches/users', component: AdminLayoutComponent,
        children: [{path: '', component: UserList}]
    },
    {
        path: 'company/users', component: AdminLayoutComponent,
        children: [{path: '', component: UserList}]
    },
    {
        path: 'companies/branches/user', component: AdminLayoutComponent,
        children: [{path: '', component: UserComponent}]
    },


    {
        path: 'permitdetails', component: AdminLayoutComponent,
        children: [{path: '', component: DmarkComponent}]
    },
    {
        path: 'invoice', component: AdminLayoutComponent,
        children: [{path: '', component: InvoiceComponent}]
    },
    {
        path: 'invoiceDetails', component: AdminLayoutComponent,
        children: [{path: '', component: InvoiceDetailsComponent}]
    },
    {
        path: 'fmark/fMarkAllApp', component: AdminLayoutComponent,
        children: [{path: '', component: FmarkallappsComponent}]
    },
    {
        path: 'st10Form', component: AdminLayoutComponent,
        children: [{path: '', component: St10FormComponent}]
    },
    {
        path: 'permitReport', component: AdminLayoutComponent,
        children: [{path: '', component: PermitReportComponent}]
    },
    {
        path: 'smark/newSmarkPermit', component: AdminLayoutComponent,
        children: [{path: '', component: NewSmarkPermitComponent}]
    },
    {
        path: 'dmark/newDmarkPermit', component: AdminLayoutComponent,
        children: [{path: '', component: NewDmarkPermitComponent}]
    },
    {
        path: 'dmark/all_dmark', component: AdminLayoutComponent,
        children: [{path: '', component: DmarkApplicationsAllComponent}]
    },
    {
        path: 'profile', component: AdminLayoutComponent,
        children: [{path: '', component: UserProfileMainComponent}]
    },
    {
        path: 'smark/all_smark', component: AdminLayoutComponent,
        children: [{path: '', component: SmarkApplicationsAllComponent}]
    },
    {
        path: 'consolidate_invoice', component: AdminLayoutComponent,
        children: [{path: '', component: InvoiceConsolidateComponent}]
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
