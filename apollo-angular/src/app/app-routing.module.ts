import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from './views/registration.component';
import {LoginComponent} from './views/registration/login.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {DashboardComponent} from "./views/dashboard/dashboard.component";
import {RouteGuard} from "./core/route-guard/route.guard";
import {AccountList} from "./views/dashboard/account/account.list";


const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'register',
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
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [RouteGuard],
    data: {
      title: 'KEBS: Home Page'
    },
    children: [
      {
        path: 'account',
        component: AccountList,
        canActivate: [RouteGuard]
      },
    ]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
