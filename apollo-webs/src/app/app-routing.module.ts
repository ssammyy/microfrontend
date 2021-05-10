import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginSignupComponent} from './components/login-signup/login-signup.component';
import {LoginComponent} from './components/login-signup/login/login.component';
import {SignupComponent} from './components/login-signup/signup/signup.component';
import {HomeComponent} from './components/home/home.component';
import {DashboardComponent} from './components/home/dashboard/dashboard.component';
import {ReportsComponent} from './components/home/reports/reports.component';
import {CompanyProfileComponent} from './components/home/company-profile/company-profile.component';
import {ComplaintsFormPageComponent} from './components/market-surveillance/complaints-form-page/complaints-form-page.component';
import {ComplaintsFormComponent} from './components/market-surveillance/complaints-form-page/complaints-form/complaints-form.component';
import {MarketSurveillanceComponent} from './components/market-surveillance/market-surveillance.component';
import {MsHomeComponent} from './components/market-surveillance/ms-home/ms-home.component';
import {ComplaintsPageComponent} from './components/market-surveillance/complaints-page/complaints-page.component';
import {ComplaintDetailsComponent} from './components/market-surveillance/complaint-details/complaint-details.component';
import {WorkplansOverviewPageComponent} from './components/market-surveillance/workplans-overview-page/workplans-overview-page.component';
// tslint:disable-next-line:max-line-length
import {WorkplanActivitiesPageComponent} from './components/market-surveillance/workplan-activities-page/workplan-activities-page.component';
import {WokplanActivityDetailsPageComponent} from './components/market-surveillance/wokplan-activity-details-page/wokplan-activity-details-page.component';
import {CreateActivityComponent} from './components/market-surveillance/create-activity/create-activity.component';
import {MsActivitiesComponent} from './components/market-surveillance/ms-activities/ms-activities.component';
import {MsActivitiesDeskTasksComponent} from './components/market-surveillance/ms-activities-desk-tasks/ms-activities-desk-tasks.component';
import {MsActivitiesFieldTasksComponent} from './components/market-surveillance/ms-activities-field-tasks/ms-activities-field-tasks.component';
import {AuthGuard} from './shared/helpers/auth.guard';
import {RegisterComponent} from './components/login-signup/register/register.component';
import {CompleteRegistrationComponent} from './components/login-signup/complite-registration/complete-registration.component';
import {ForgotPasswordComponent} from './components/login-signup/forgot-password/forgot-password.component';
import {AdministratorComponent} from './components/administrator/administrator.component';
import {AdminHomeComponent} from './components/administrator/admin-home/admin-home.component';
import {UsersListComponent} from './components/administrator/users-list/users-list.component';
import {UserDetailsComponent} from './components/administrator/user-details/user-details.component';
import {AddUserComponent} from './components/administrator/add-user/add-user.component';
import {StandardRequestComponent} from "./components/standards-development/standard-request/standard-request.component";
import {UserProfileComponent} from './components/home/user-profile/user-profile.component';
import {StandardTaskComponent} from "./components/standards-development/standard-task/standard-task.component";
import {StdTscSecTasksComponent} from "./components/standards-development/std-tsc-sec-tasks/std-tsc-sec-tasks.component";
import {RequestStandardFormComponent} from "./components/standards-development/standard-request/request-standard-form/request-standard-form.component";
import {StandardsDevelopmentComponent} from "./components/standards-development/standards-development.component";
import {StdTcTasksComponent} from "./components/standards-development/std-tc-tasks/std-tc-tasks.component";
import {CsRequestFormComponent} from "./components/standards-development/company-standard-request/cs-request-form/cs-request-form.component";
import {CompanyStandardRequestComponent} from "./components/standards-development/company-standard-request/company-standard-request.component";
import {ComStdRequestListComponent} from "./components/standards-development/com-std-request-list/com-std-request-list.component";
import {NwaJustificationFormComponent} from "./components/standards-development/nwa-justification-form/nwa-justification-form.component";
import {NwaJustificationTasksComponent} from "./components/standards-development/nwa-justification-tasks/nwa-justification-tasks.component";
import {NwaKnwSecTasksComponent} from "./components/standards-development/nwa-knw-sec-tasks/nwa-knw-sec-tasks.component";
import {NwaDiSdtTasksComponent} from "./components/standards-development/nwa-di-sdt-tasks/nwa-di-sdt-tasks.component";
import {NwaHopTasksComponent} from "./components/standards-development/nwa-hop-tasks/nwa-hop-tasks.component";
import {SacSecTasksComponent} from "./components/standards-development/sac-sec-tasks/sac-sec-tasks.component";
import {NwaHoSicTasksComponent} from "./components/standards-development/nwa-ho-sic-tasks/nwa-ho-sic-tasks.component";

const routes: Routes = [
  // { path: 'login-signup', component: LoginSignupComponent },
  // Customer Login

  // {path: 'home', component: HomeComponent,
  //   children: [
  //     // {path: '', redirectTo: '0', pathMatch: 'full'},
  //     {path: 'dashboard', component: DashboardComponent},
  //     {path: 'reports', component: ReportsComponent}
  //   ]
  // },

  /****************************************************************
   * USER ROUTES ONLY
   ***************************************************************/
  {path: '', redirectTo: 'dashboard', pathMatch: 'full'},

  {
    path: 'login', component: LoginSignupComponent,
    children: [{path: '', component: LoginComponent}]
  },

  // Customer Registration
  {
    path: 'register', component: LoginSignupComponent,
    children: [{path: '', component: RegisterComponent}]
  },

  {
    path: 'signup', component: LoginSignupComponent,
    children: [{path: '', component: SignupComponent}]
  },

  {
    path: 'otpVerification', component: LoginSignupComponent,
    children: [{path: '', component: CompleteRegistrationComponent}]
  },

  {
    path: 'forgot-password', component: LoginSignupComponent,
    children: [{path: '', component: ForgotPasswordComponent}]
  },

  {
    path: 'dashboard', component: HomeComponent, canActivate: [AuthGuard],
    children: [{path: '', component: DashboardComponent}]
  },

  {
    path: 'reports', component: HomeComponent,
    children: [{path: '', component: ReportsComponent}]
  },

  {
    path: 'profile', component: HomeComponent,
    children: [{path: '', component: UserProfileComponent}]
  },


  /****************************************************************
   * END OF USER AND START OF MS ROUTES ONLY
   ***************************************************************/


  {
    path: 'complaints', component: ComplaintsFormPageComponent, canActivate: [AuthGuard],
    children: [{path: '', component: ComplaintsFormComponent}]
  },

  {
    path: 'ms-home',
    component: MarketSurveillanceComponent,
    canActivate: [AuthGuard],
    // data: {roles: [Role.MsHod, Role.MsDirector, Role.MsRm, Role.MsHof, Role.MsIo, Role.Employee]},
    children: [{path: '', component: MsHomeComponent}]
  },

  {
    path: 'complaints-page', component: MarketSurveillanceComponent, canActivate: [AuthGuard],
    // data: {roles: [Role.MsHod, Role.MsDirector, Role.MsRm, Role.MsHof, Role.MsIo]},
    children: [{path: '', component: ComplaintsPageComponent}]
  },

  {
    path: 'complaint-details', component: MarketSurveillanceComponent, canActivate: [AuthGuard],
    children: [{path: '', component: ComplaintDetailsComponent}]
  },

  {
    path: 'create-activity', component: MarketSurveillanceComponent,
    children: [{path: '', component: CreateActivityComponent}]
  },

  {
    path: 'workplans-overview', component: MarketSurveillanceComponent,
    children: [{path: '', component: WorkplansOverviewPageComponent}]
  },

  {
    path: 'workplan-activities/:refNumber', component: MarketSurveillanceComponent,
    children: [{path: '', component: WorkplanActivitiesPageComponent}]
  },

  {
    path: 'workplan-activities/:refNumber/activity-details/:activityId', component: MarketSurveillanceComponent,
    children: [{path: '', component: WokplanActivityDetailsPageComponent}]
  },

  {
    path: 'ms-activities', component: MarketSurveillanceComponent,
    children: [{path: '', component: MsActivitiesComponent}]
  },
  {
    path: 'ms-desk-tasks', component: MarketSurveillanceComponent,
    children: [{path: '', component: MsActivitiesDeskTasksComponent}]
  },

  {
    path: 'ms-field-tasks', component: MarketSurveillanceComponent,
    children: [{path: '', component: MsActivitiesFieldTasksComponent}]
  },

  /****************************************************************
   * MS ROUTES END HERE ONLY AND START OF ADMIN ROUTES
   ***************************************************************/

  {
    path: 'admin-home',
    component: AdministratorComponent,
    canActivate: [AuthGuard],
    // data: {roles: [Role.MsHod, Role.MsDirector, Role.MsRm, Role.MsHof, Role.MsIo, Role.Employee]},
    children: [{path: '', component: AdminHomeComponent}]
  },

  {
    path: 'users-list',
    component: AdministratorComponent,
    canActivate: [AuthGuard],
    // data: {roles: [Role.MsHod, Role.MsDirector, Role.MsRm, Role.MsHof, Role.MsIo, Role.Employee]},
    children: [{path: '', component: UsersListComponent}]
  },

  {
    path: 'user-details',
    component: AdministratorComponent,
    canActivate: [AuthGuard],
    // data: {roles: [Role.MsHod, Role.MsDirector, Role.MsRm, Role.MsHof, Role.MsIo, Role.Employee]},
    children: [{path: '', component: UserDetailsComponent}]
  },

  {
    path: 'user-details/:requestID/:requestNAME',
    component: AdministratorComponent,
    canActivate: [AuthGuard],
    // data: {roles: [Role.MsHod, Role.MsDirector, Role.MsRm, Role.MsHof, Role.MsIo, Role.Employee]},
    children: [{path: '', component: UserDetailsComponent}]
  },

  {
    path: 'add-user', component: AdministratorComponent,
    children: [{path: '', component: AddUserComponent}]
  },


  /****************************************************************
   * STANDARD DEVELOPMENT ROUTES
   ***************************************************************/
  {
    path: 'request-standard', component: StandardRequestComponent,
    children: [{path: '', component: RequestStandardFormComponent}]
  },
  {
    path: 'standard-task', component: StandardsDevelopmentComponent,
    children: [{path: '', component: StandardTaskComponent}]
  },
  {
    path: 'std-tsc-sec-task', component: StandardsDevelopmentComponent,
    children: [{path: '', component: StdTscSecTasksComponent}]
  },
  {
    path: 'std-tc-task', component: StandardsDevelopmentComponent,
    children: [{path: '', component: StdTcTasksComponent}]
  },
  {
    path: 'com-std-request', component: CompanyStandardRequestComponent,
    children: [{path: '', component: CsRequestFormComponent}]
  },
  {
    path: 'com-std-list', component: StandardsDevelopmentComponent,
    children: [{path: '', component: ComStdRequestListComponent}]
  },
  {
    path: 'nwa-prepare-justification', component: StandardsDevelopmentComponent,
    children: [{path: '', component: NwaJustificationFormComponent}]
  },
  {
    path: 'nwa-justification-tasks', component: StandardsDevelopmentComponent,
    children: [{path: '', component: NwaJustificationTasksComponent}]
  },
  {
    path: 'nwa-knw-sec-tasks', component: StandardsDevelopmentComponent,
    children: [{path: '', component: NwaKnwSecTasksComponent}]
  },
  {
    path: 'nwa-di-sdt-tasks', component: StandardsDevelopmentComponent,
    children: [{path: '', component: NwaDiSdtTasksComponent}]
  },
  {
    path: 'nwa-hop-tasks', component: StandardsDevelopmentComponent,
    children: [{path: '', component: NwaHopTasksComponent}]
  },
  {
    path: 'sac-sec-tasks', component: StandardsDevelopmentComponent,
    children: [{path: '', component: SacSecTasksComponent}]
  },
  {
    path: 'ho-sic-tasks', component: StandardsDevelopmentComponent,
    children: [{path: '', component: NwaHoSicTasksComponent}]
  },



  /****************************************************************
   * END OF STANDARD DEVELOPMENT ROUTES
   ***************************************************************/

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
