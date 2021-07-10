import {Injectable} from '@angular/core';
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
  ) {
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
      // errorMessage = `Error Code: ${err.status},  Message: ${err.error}`;
      errorMessage = `Message: ${err.error}`;
    }
    // this.store$.dispatch(
    //   fromResponseActions.loadResponsesFailure({
    //     error: {
    //       payload: err.error,
    //       status: err.status,
    //       response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
    //     }
    //   })
    // );

    this.toastrService.error(errorMessage);
  }

  public handleMessaging(message: string, code: number) {
    if (
      message
    ){
      if (
        message.trim().length < 1 ||
        message === ''
      ){
        console.warn("No toastr for empty messages")
      }else{
        if (code === 200) {
          this.toastrService.info(message);
        } else {
          this.toastrService.error(message);
        }

      }

    }else{
      console.warn("No toastr for null messages")
    }
  }
}
