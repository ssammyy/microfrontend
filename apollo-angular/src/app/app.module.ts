import {APP_BASE_HREF} from '@angular/common';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import {RegistrationComponent} from './views/registration.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {LoginComponent} from "./views/registration/login.component";
import {CoreModule} from "./core/core.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {SharedModule} from "./core/shared/shared.module";
import {DashboardComponent} from './views/dashboard/dashboard.component';
import {AccountList} from "./views/dashboard/account/account.list";
import {CompaniesList} from './views/dashboard/company/companies.list';
import {NgxPaginationModule} from "ngx-pagination";
import {BranchesList} from './views/dashboard/company/branch/branches.list';
import {DirectorsList} from './views/dashboard/company/director/directors.list';
import {DataTablesModule} from "angular-datatables";
import {UsersList} from './views/dashboard/company/user/users.list';
import {ResetCredentialsComponent} from './views/registration/reset-credentials.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    SignUpComponent,
    DashboardComponent,
    AccountList,
    CompaniesList,
    BranchesList,
    DirectorsList,
    UsersList,
    ResetCredentialsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    CoreModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    ReactiveFormsModule,
    SharedModule,
    FormsModule,
    NgxPaginationModule,
    DataTablesModule,
  ],
  providers: [
    {provide: APP_BASE_HREF, useValue: '/migration/'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
