import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {ApiResponse} from "./core/domain/response.model";
import {Router} from "@angular/router";
import {select, Store} from "@ngrx/store";
import {HandleErrorService} from "./core/services/errors/handle-error.service";
import {selectResponseData} from "./core/store/data/response/response.selectors";

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent implements OnInit {
  title = 'apollo-angular-web';
  /**
   * The possible login error.
   */
  public error$!: Observable<ApiResponse>;

  constructor(
    private router: Router,
    private store$: Store<any>,
    private errorsService: HandleErrorService
  ) {
    this.store$.pipe(select(selectResponseData)).subscribe((errors: ApiResponse) => {
      if (errors) {
        this.errorsService.handleMessaging(JSON.stringify(errors.payload), errors.status);
      }
    });
  }

  ngOnInit(): void {
  }
}
