import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {LearnmoreComponent} from "../../../mf-app/src/app/learnmore/learnmore.component";

import  {loadRemoteModule} from '@angular-architects/module-federation'
const MFE_APP_URI ='http://localhost:4201/remoteEntry.js'
const routes: Routes = [
  {
    path: "" , redirectTo: "/home", pathMatch: "full"
  },
  {
    path: "learn-more",
    loadChildren: ()=>{
      return loadRemoteModule({
        remoteEntry: MFE_APP_URI,
        remoteName: "mfApp",
        exposedModule:"./LearnmoreModule"
      }).then(m=>m.LearnmoreModule).catch(err=> console.log("error trying to embed component"+err))
    }
  },
  {
    path: "home", component: HomeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
