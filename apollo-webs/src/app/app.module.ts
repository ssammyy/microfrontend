import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginSignupComponent} from './components/login-signup/login-signup.component';
import {SignupComponent} from './components/login-signup/signup/signup.component';
import {LoginComponent} from './components/login-signup/login/login.component';
import {SignupFormPipePipe} from './shared/pipes/signup-form-pipe.pipe';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
// import {NgxStepperModule} from 'ngx-stepper';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {HomeComponent} from './components/home/home.component';
import {DashboardComponent} from './components/home/dashboard/dashboard.component';
import {ReportsComponent} from './components/home/reports/reports.component';
import {CompanyProfileComponent} from './components/home/company-profile/company-profile.component';
import {NgApexchartsModule} from 'ng-apexcharts';
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
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ToastrModule} from 'ngx-toastr';
import {CreateActivityComponent} from './components/market-surveillance/create-activity/create-activity.component';
import {MsActivitiesComponent} from './components/market-surveillance/ms-activities/ms-activities.component';
import {MsActivitiesDeskTasksComponent} from './components/market-surveillance/ms-activities-desk-tasks/ms-activities-desk-tasks.component';
import {SearchPipe} from './shared/pipes/search-pipe.pipe';
import {MsActivitiesFieldTasksComponent} from './components/market-surveillance/ms-activities-field-tasks/ms-activities-field-tasks.component';
import {NgxSpinnerModule} from 'ngx-spinner';
import {JwtInterceptor} from './shared/helpers/jwt.interceptor';
import {AlertComponent} from './components/alert/alert.component';
import {ErrorInterceptor} from './shared/helpers/error.interceptor';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RegisterComponent} from './components/login-signup/register/register.component';
import {CompleteRegistrationComponent} from './components/login-signup/complite-registration/complete-registration.component';
import {ForgotPasswordComponent} from './components/login-signup/forgot-password/forgot-password.component';
import {AdministratorComponent} from './components/administrator/administrator.component';
import {AdminHomeComponent} from './components/administrator/admin-home/admin-home.component';
import {UsersListComponent} from './components/administrator/users-list/users-list.component';
import {UserDetailsComponent} from './components/administrator/user-details/user-details.component';
import {AddUserComponent} from './components/administrator/add-user/add-user.component';
import {AccessControlComponent} from './components/administrator/access-control/access-control.component';
import {NgxPaginationModule} from 'ngx-pagination';
import { StandardsDevelopmentComponent } from './components/standards-development/standards-development.component';
import { StandardRequestComponent } from './components/standards-development/standard-request/standard-request.component';
import { UserProfileComponent } from './components/home/user-profile/user-profile.component';
import { StandardTaskComponent } from './components/standards-development/standard-task/standard-task.component';
import {StdTscSecTasksComponent} from "./components/standards-development/std-tsc-sec-tasks/std-tsc-sec-tasks.component";
import { StdTscTasksComponent } from './components/standards-development/std-tsc-tasks/std-tsc-tasks.component';
import { RequestStandardFormComponent } from './components/standards-development/standard-request/request-standard-form/request-standard-form.component';
import {FileUploadModule} from "ng2-file-upload";
import { StdTcTasksComponent } from './components/standards-development/std-tc-tasks/std-tc-tasks.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginSignupComponent,
    SignupComponent,
    LoginComponent,
    RegisterComponent,
    SignupFormPipePipe,
    HomeComponent,
    DashboardComponent,
    ReportsComponent,
    CompanyProfileComponent,
    ComplaintsFormPageComponent,
    ComplaintsFormComponent,
    MarketSurveillanceComponent,
    MsHomeComponent,
    ComplaintsPageComponent,
    ComplaintDetailsComponent,
    WorkplansOverviewPageComponent,
    WorkplanActivitiesPageComponent,
    WokplanActivityDetailsPageComponent,
    CreateActivityComponent,
    MsActivitiesComponent,
    MsActivitiesDeskTasksComponent,
    SearchPipe,
    MsActivitiesFieldTasksComponent,
    AlertComponent,
    RegisterComponent,
    CompleteRegistrationComponent,
    ForgotPasswordComponent,
    AdministratorComponent,
    AdminHomeComponent,
    UsersListComponent,
    UserDetailsComponent,
    AddUserComponent,
    AccessControlComponent,
    StandardsDevelopmentComponent,
    StandardRequestComponent,
    UserProfileComponent,
    StandardTaskComponent,
    StdTscSecTasksComponent,
    StdTscTasksComponent,
    RequestStandardFormComponent,
    StdTcTasksComponent,
  ],
  exports: [
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgbModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    FontAwesomeModule,
    NgApexchartsModule,
    NgxSpinnerModule,
    NgxPaginationModule,
    HttpClientModule,
    FileUploadModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: false,
    }),
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
