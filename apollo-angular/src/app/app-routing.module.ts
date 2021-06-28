import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from './views/registration.component';
import {LoginComponent} from './views/registration/login.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {DashboardComponent} from "./views/dashboard/dashboard.component";
import {RouteGuard} from "./core/route-guard/route.guard";
import {AccountList} from "./views/dashboard/account/account.list";
import {CompaniesList} from "./views/dashboard/company/companies.list";
import {BranchesList} from "./views/dashboard/company/branch/branches.list";
import {DirectorsList} from "./views/dashboard/company/director/directors.list";
import {UsersList} from "./views/dashboard/company/user/users.list";


const routes: Routes = [
  {
    path: 'migration',
    redirectTo: 'register',
    pathMatch: 'prefix',
  },
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
      {
        path: 'companies',
        component: CompaniesList,
        data: {title: 'Companies'}
      },
      {
        path: 'branches',
        component: BranchesList,
        data: {title: 'Branches'}
      },
      {
        path: 'branches/users',
        component: UsersList,
        data: {title: 'Users'}
      },
      {
        path: 'directors',
        component: DirectorsList,
        data: {title: 'Directors'}
      },
    ]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
