import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {select, Store} from "@ngrx/store";
import {selectGetNames} from "../../core/store";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styles: []
})
export class DashboardComponent implements OnInit {
  fullName$!: Observable<string>;

  constructor(
    private store$: Store<any>
  ) {
  }

  ngOnInit(): void {
    this.fullName$ = this.store$.pipe(select(selectGetNames));
  }

}
