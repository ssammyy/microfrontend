import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LearnmoreComponent} from "./learnmore/learnmore.component";

const routes: Routes = [
  {
    path : '', redirectTo: "/learn-more", pathMatch: 'full'
  },
  {
    path:'learn-more', component: LearnmoreComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
