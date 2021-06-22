import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationComponent} from "./views/registration.component";
import {LoginComponent} from "./views/registration/login.component";
import {SignUpComponent} from "./views/registration/sign-up.component";
import {DashboardComponent} from "./views/dashboard/dashboard.component";
import {TableComponent} from "./views/table/table.component";


const routes: Routes = [
  {
    path: '',
    component: RegistrationComponent,
    children: [
      {
        path: '',
        component: LoginComponent
      },
      {
        path: 'register',
        component: SignUpComponent
      },
      {
        path: 'login',
        component: LoginComponent
      }

    ],
    data: {
      title: 'KEBS'
    }
  },
  {
    path: '',
    component: DashboardComponent,
    data: {
      title: 'KEBS: Home Page'
    },
    children: [
      {path: 'table', component: TableComponent}
    ]
  },


  // {path: 'login', component: LoginComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
