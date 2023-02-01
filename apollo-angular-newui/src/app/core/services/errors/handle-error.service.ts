import {Injectable} from '@angular/core';
import {Actions} from '@ngrx/effects';
import {ToastrService} from 'ngx-toastr';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";

@Injectable({
    providedIn: 'root',
})
export class HandleErrorService {
    loading = false;

    constructor(actions$: Actions,
                private toastrService: ToastrService,
                private SpinnerService: NgxSpinnerService,

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
        } else if (err.error instanceof Blob) {
            // Error handled during download
            err.error.text().then((txt) => {
                const res = JSON.parse(txt);
                errorMessage = res.message
            });
            if (errorMessage) {
                throwError(errorMessage);
            }
        } else {
            // The backend returned an unsuccessful response code.
            // errorMessage = `Error Code: ${err.status},  Message: ${err.error}`;
            errorMessage = `Message: ${err.error}`;
            this.SpinnerService.hide()

            console.log(`Message: ${err.error}`);
            this.toastrService.warning(errorMessage);
        }

    }

    public handleMessaging(message: string, code: number) {
        if (
            message
        ) {
            if (
                message === 'null' ||
                message.trim().length < 1 ||
                message === ''
            ) {
                console.warn('No toastr for empty messages');
            } else {
                if (code === 200) {
                    this.toastrService.info(message);
                } else {
                    this.toastrService.warning(JSON.stringify(message));
                }

            }

        } else {
            console.warn('No toastr for null messages');
        }
    }
}
