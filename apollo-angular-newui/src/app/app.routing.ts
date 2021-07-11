import {RouterModule, Routes} from '@angular/router';

import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {DashboardComponent} from './apollowebs/dashboard/dashboard.component';
import {NgModule} from '@angular/core';
import {DmarkComponent} from './apollowebs/dmark/dmark.component';
import {FmarkallappsComponent} from './apollowebs/fmarkallapps/fmarkallapps.component';
import {St10FormComponent} from './apollowebs/st10-form/st10-form.component';
import {RegistrationComponent} from "./views/registration.component";
import {SignUpComponent} from "./views/registration/sign-up.component";
import {ResetCredentialsComponent} from "./views/registration/reset-credentials.component";
import {RouteGuard} from "./core/route-guard/route.guard";
import {LoginComponent} from "./views/registration/login.component";
import {PermitReportComponent} from "./apollowebs/permit-report/permit-report.component";
import {NewSmarkPermitComponent} from "./apollowebs/new-smark-permit/new-smark-permit.component";
import {NewDmarkPermitComponent} from "./apollowebs/new-dmark-permit/new-dmark-permit.component";
import {DmarkApplicationsAllComponent} from "./apollowebs/dmark-applications-all/dmark-applications-all.component";
import {InvoiceComponent} from "./apollowebs/invoice/invoice.component";
import {InvoiceDetailsComponent} from "./apollowebs/invoice-details/invoice-details.component";

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
        redirectTo: 'register',
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
            }

        ],
        data: {
            title: 'KEBS'
        }
    },
    {
        path: 'dashboard', component: AdminLayoutComponent, canActivate: [RouteGuard]
        ,
        children: [{path: '', component: DashboardComponent}]
    },

    {
        path: 'dmark', component: AdminLayoutComponent,
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
        path: 'newSmarkPermit', component: AdminLayoutComponent,
        children: [{path: '', component: NewSmarkPermitComponent}]
    },
    {
        path: 'dmark/newDmarkPermit', component: AdminLayoutComponent,
        children: [{path: '', component: NewDmarkPermitComponent}]
    },
    {
        path: 'dmark/all_dmark', component: AdminLayoutComponent,
        children: [{path: '', component: DmarkApplicationsAllComponent}]
    }
];


@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
