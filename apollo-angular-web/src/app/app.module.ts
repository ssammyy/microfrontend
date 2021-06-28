import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ToastrModule} from "ngx-toastr";
import {RegistrationComponent} from "./views/registration.component";
import {LoginComponent} from "./views/registration/login.component";
import {SignUpComponent} from "./views/registration/sign-up.component";
import {DashboardComponent} from "./views/dashboard/dashboard.component";
import {TableComponent} from "./views/table/table.component";
import {SharedModule} from "./core/shared/shared.module";
import {CoreModule} from "./core/core.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {APP_BASE_HREF, CommonModule} from "@angular/common";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DataTablesModule} from "angular-datatables";
import {RouterModule} from "@angular/router";


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    SignUpComponent,
    DashboardComponent,
    TableComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: false,
    }),
    SharedModule,
    CoreModule,
    DataTablesModule.forRoot(),
    RouterModule,
  ],
  providers: [{provide: APP_BASE_HREF, useValue: '/'}],
  bootstrap: [AppComponent]
})
export class AppModule { }
