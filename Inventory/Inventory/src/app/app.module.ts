import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {SideBarComponent} from './side-bar/side-bar.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSidenavModule} from "@angular/material/sidenav";
import {RouterLink} from "@angular/router";
import {MatIconModule} from '@angular/material/icon';
import {RouterModule} from '@angular/router';
import {BodyComponent} from './body/body.component';
import {AppRoutingModule} from './app-routing.module';
import {DashboardComponent} from './dashboard/dashboard.component';
import {InventoryComponent} from './inventory/inventory.component';
import {SaleComponent} from './sale/sale.component';
import {SupplyComponent} from './supply/supply.component';
import {AccountsComponent} from './accounts/accounts.component';
import {CanvasJSAngularChartsModule} from '@canvasjs/angular-charts';
import {LoginComponent} from './login/login.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from '@angular/common/http';
import {NgxSpinnerModule} from 'ngx-spinner';
import {MatPaginatorModule} from "@angular/material/paginator";
import { ChartsModule } from 'ng2-charts';



@NgModule({
  declarations: [
    AppComponent,
    SideBarComponent,
    BodyComponent,
    DashboardComponent,
    InventoryComponent,
    SaleComponent,
    SupplyComponent,
    AccountsComponent,
    LoginComponent
  ],
  imports: [
    HttpClientModule,
    AppRoutingModule,
    RouterModule.forRoot([]), // Add this line
    BrowserModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    RouterLink,
    MatIconModule,
    ChartsModule,
    CanvasJSAngularChartsModule,
    FormsModule,
    NgxSpinnerModule,
    MatPaginatorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
