import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FilterPipe} from './filter.pipe';
import {PreventDoubleClickDirective} from './prevent-double-click.directive';
import {ConsignmentStatusComponent} from './customs/consignment-status/consignment-status.component';
import {CurrencyFormatterComponent} from './currency-formatter/currency-formatter.component';
import {CurrencyFormatterPipe} from "./currency-formatter.pipe";
import { DateValidatorDirective } from './date-validator.directive';


@NgModule({
    declarations: [
        FilterPipe,
        PreventDoubleClickDirective,
        ConsignmentStatusComponent,
        CurrencyFormatterComponent,
        CurrencyFormatterPipe,
        DateValidatorDirective
    ],
    exports: [
        FilterPipe,
        CurrencyFormatterPipe,
        PreventDoubleClickDirective,
        CurrencyFormatterComponent,
        ConsignmentStatusComponent
    ],
    imports: [
        CommonModule
    ]
})
export class SharedModule {
}
