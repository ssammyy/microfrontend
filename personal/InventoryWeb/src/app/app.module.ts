import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import {RouterLink, RouterLinkActive} from "@angular/router";
import { SupplyComponentComponent } from './supply-component/supply-component.component';
import { SupplyComponent } from './supply/supply.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { InventoryComponent } from './inventory/inventory.component';
import { SaleComponent } from './sale/sale.component';
import { AccountsComponent } from './accounts/accounts.component';
import { BodyComponent } from './body/body.component';
import { RouterModule} from "@angular/router";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatIconModule} from '@angular/material/icon';
import {AppRoutingModule} from './app-routing.module';
import {NgApexchartsModule} from "ng-apexcharts";
import { TopBarComponent } from './top-bar/top-bar.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import {MatPaginatorModule} from "@angular/material/paginator";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from '@angular/common/http';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { ToastComponent } from './Utils/toast/toast.component';
import { CommonModule } from '@angular/common';
import {ToastModule} from "primeng/toast";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {DividerModule} from "primeng/divider";
import {RadioButtonModule} from "primeng/radiobutton";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {MatTabsModule} from "@angular/material/tabs";




@NgModule({
  declarations: [
    AppComponent,
    SideBarComponent,
    SupplyComponentComponent,
    SupplyComponent,
    LoginComponent,
    DashboardComponent,
    InventoryComponent,
    SaleComponent,
    AccountsComponent,
    BodyComponent,
    TopBarComponent,
    ToastComponent
  ],
    imports: [
        MatTooltipModule,
        BrowserModule,
        RouterLink,
        RouterLinkActive,
        RouterModule,
        HttpClientModule,
        AppRoutingModule,
        MatIconModule,
        BrowserAnimationsModule,
        MatSidenavModule,
        NgApexchartsModule,
        MatPaginatorModule,
        FormsModule,
        MatProgressSpinnerModule,
        CommonModule,
        ToastModule,
        ButtonModule,
        DialogModule,
        DividerModule,
        RadioButtonModule,
        InputTextModule,
        InputNumberModule,
        ProgressSpinnerModule,
        MatTabsModule

    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
