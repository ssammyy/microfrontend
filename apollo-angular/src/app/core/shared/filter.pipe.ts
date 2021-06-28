import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'filterPipe'
})
export class FilterPipe implements PipeTransform {

  // @ts-ignore
  private static checkValue(item, key, value): boolean {
    if (!item || !key || !value || value === '') {
      return true;
    } else {
      return (
        item[key]
          .toString()
          .toLowerCase()
          .indexOf(value.toString().toLowerCase()) !== -1
      );
    }

  }

  transform(items: any[], filter?: any): any {
    // clean undefined object properties
    Object.keys(filter).forEach(key => !filter[key] && delete filter[key]);

    return this.checkComplexType(items, filter);
  }

  // @ts-ignore
  private checkComplexType(values, filter): any {
    if (!values || !filter || Object.entries(filter).length === 0) {
      return values;
    }
    // @ts-ignore
    const result = [];

    // find in all records
    // @ts-ignore
    values.forEach(row => {
      let match = false;
      // find in all filters in the object of filters
      for (const [key, value] of Object.entries(filter)) {
        match = FilterPipe.checkValue(row, key, value);
        if (!match) {
          return false;
        }
      }
      if (match) {
        result.push(row); // add row in return
      }
    });

    // @ts-ignore
    return result;
  }
}
