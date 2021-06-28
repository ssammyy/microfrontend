import {Injectable} from '@angular/core';
import * as fromResponseActions from '../../store';
import {Actions} from "@ngrx/effects";
import {ToastrService} from 'ngx-toastr';
import {Store} from "@ngrx/store";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class HandleErrorService {

  constructor(actions$: Actions,
              private toastrService: ToastrService,
              private store$: Store<any>) {
  }

  /**
   * Handling HTTP Errors using Toaster
   */
  public handleError(err: HttpErrorResponse): void {
    let errorMessage: string;
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = `An error occurred: ${err.error.message}`;
    } else {
      // The backend returned an unsuccessful response code.
      errorMessage = `Error Code: ${err.status},  Message: ${err.message}`;
    }
    this.store$.dispatch(
      fromResponseActions.loadResponsesFailure({
        error: {
          payload: err.error,
          status: err.status,
          response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
        }
      })
    );

    this.toastrService.error(errorMessage);
  }

  public handleMessaging(message: string, code: number) {
    if (code === 200) {
      this.toastrService.info(message);
    } else {
      this.toastrService.error(message);
    }
  }
}
