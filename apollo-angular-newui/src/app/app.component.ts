import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, NavigationStart } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import {select, Store} from '@ngrx/store';
import {HandleErrorService} from './core/services/errors/handle-error.service';
import {selectResponseData} from './core/store/data/response/response.selectors';
import {ApiResponse} from './core/domain/response.model';

@Component({
    selector: 'app-my-app',
    templateUrl: './app.component.html'
})

export class AppComponent implements OnInit {
  private _router: Subscription;

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

    ngOnInit() {
      this._router = this.router.events.filter(event => event instanceof NavigationEnd).subscribe((event: NavigationEnd) => {
        const body = document.getElementsByTagName('body')[0];
        const modalBackdrop = document.getElementsByClassName('modal-backdrop')[0];
        if (body.classList.contains('modal-open')) {
          body.classList.remove('modal-open');
          modalBackdrop.remove();
        }
      });
    }
}
