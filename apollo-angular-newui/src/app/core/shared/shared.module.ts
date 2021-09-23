import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FilterPipe} from './filter.pipe';
import {PreventDoubleClickDirective} from './prevent-double-click.directive';
import {ConsignmentStatusComponent} from './customs/consignment-status/consignment-status.component';
import {CurrencyFormatterComponent} from './currency-formatter/currency-formatter.component';
import { CurrencyFormatterPipe } from './currency-formatter.pipe';


@NgModule({
    declarations: [FilterPipe,
        PreventDoubleClickDirective,
        ConsignmentStatusComponent,
        CurrencyFormatterComponent,
        CurrencyFormatterPipe
    ],
    exports: [
        FilterPipe,
        PreventDoubleClickDirective,
        CurrencyFormatterComponent,
        CurrencyFormatterPipe
    ],
    imports: [
        CommonModule
    ]
})
export class SharedModule {
}
