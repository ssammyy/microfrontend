import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LearnmoreComponent } from './learnmore.component';
import {BrowserModule} from "@angular/platform-browser";
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [
    LearnmoreComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forChild([
      {
        path: '', component: LearnmoreComponent
      }
    ])
  ]
})
export class LearnmoreModule { }
