import {Pipe, PipeTransform} from '@angular/core';
import {CurrencyPipe} from "@angular/common";

@Pipe({
    name: 'money'
})
export class CurrencyFormatterPipe implements PipeTransform {

    transform(value: unknown, ...args: any[]): unknown {
        let renderedValue = value;
        if (value) {
            try {
                let currency = "KES"
                if (args.length == 1) {
                    currency = args[0]
                }
                let pipe = new CurrencyPipe("en-US")
                renderedValue = pipe.transform(value, currency, 'symbol', '1.2-2');
            } catch (e) {
                console.log(e)
                renderedValue = value;
            }
        }
        return renderedValue
    }

}
