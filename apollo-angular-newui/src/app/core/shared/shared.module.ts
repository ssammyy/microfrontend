import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FilterPipe} from './filter.pipe';
import {PreventDoubleClickDirective} from './prevent-double-click.directive';
import { ConsignmentStatusComponent } from './customs/consignment-status/consignment-status.component';


@NgModule({
  declarations: [FilterPipe, PreventDoubleClickDirective, ConsignmentStatusComponent],
  exports: [
    FilterPipe,
    PreventDoubleClickDirective
  ],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
