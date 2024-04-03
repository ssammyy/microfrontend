import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {LearnmoreModule} from "./learnmore/learnmore.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LearnmoreModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
