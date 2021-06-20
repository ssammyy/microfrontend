import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from './views/registration.component';
import {LoginComponent} from './views/registration/login.component';
import {SignUpComponent} from './views/registration/sign-up.component';
import {DashboardComponent} from "./views/dashboard/dashboard.component";


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
    path: 'dashboard',
    component: DashboardComponent,
    data: {
      title: 'KEBS: Home Page'
    }
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
