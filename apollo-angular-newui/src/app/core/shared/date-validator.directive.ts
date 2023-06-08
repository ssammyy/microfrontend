import { Directive } from '@angular/core';
import {AbstractControl, ValidatorFn} from '@angular/forms';

@Directive({
  selector: '[appDateValidator]'
})
export class DateValidatorDirective {

  constructor() { }

  static fromToDate(fromDateField: string, toDateField: string, errorName: string = 'fromToDate'): ValidatorFn {
    return (formGroup: AbstractControl): { [key: string]: boolean } | null => {
      const fromDate = formGroup.get(fromDateField).value;
      const toDate = formGroup.get(toDateField).value;
      // Ausing the fromDate and toDate are numbers. In not convert them first after null check
      if ((fromDate !== null && toDate !== null) && fromDate > toDate) {
        return {[errorName]: true};
      }
      return null;
    };
  }

}
