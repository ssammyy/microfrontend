import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'filterPipe'
})
export class FilterPipe implements PipeTransform {

  private static checkValue({item, key, value}: { item: any, key: any, value: any }): boolean {
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

    return this.checkComplexType({values: items, filter: filter});
  }

  private checkComplexType({values, filter}: { values: any, filter: any }): any {
    if (!values || !filter || Object.entries(filter).length === 0) {
      return values;
    }
    const result: any[] = [];

    // find in all records
    // @ts-ignore
    values.forEach((row: any) => {
      let match = false;
      // find in all filters in the object of filters
      for (const [key, value] of Object.entries(filter)) {
        match = FilterPipe.checkValue({item: row, key: key, value: value});
        if (!match) {
          return false;
        }else{
          return true;
        }
      }
      if (match) {
        result.push(row); // add row in return
        return true;
      }
    });

    return result;
  }

}
