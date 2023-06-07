import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class StepperDataService {
    public dataChange = new Subject<any>();

    constructor() {
    }

    setData(data: any) {
        this.dataChange.next(data);
    }

}
