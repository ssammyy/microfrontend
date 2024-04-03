import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {BodyComponent} from "./body/body.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {InventoryComponent} from "./inventory/inventory.component";
import {SupplyComponent} from "./supply/supply.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {SaleComponent} from "./sale/sale.component";
import {LoginComponent} from "./login/login.component";



const routes: Routes = [
  {
    // path: '', redirectTo: 'dashBoard', pathMatch: "full"
    path: 'login', component: LoginComponent
  },
  {
    path: 'dashBoard', component: DashboardComponent
  },
  {
    path: 'inventory', component: InventoryComponent
  },
  {
    path: 'sale', component: SaleComponent
  },
  {
    path: 'accounts', component: AccountsComponent
  },
  {
    path: 'supply', component: SupplyComponent
  }

  // Add more routes as needed
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

