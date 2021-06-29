import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FilterPipe} from "./filter.pipe";
import {PreventDoubleClickDirective} from './prevent-double-click.directive';


@NgModule({
  declarations: [FilterPipe, PreventDoubleClickDirective],
  exports: [
    FilterPipe,
    PreventDoubleClickDirective
  ],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
