import {Component, OnInit} from '@angular/core';
import {
    Go,
    loadResponsesFailure,
    loadResponsesSuccess,
    selectUserInfo,
    UserEntityDto,
    UserEntityService
} from "../../../core/store";
import {Observable, of} from "rxjs";
import {Titles, TitlesService} from "../../../core/store/data/title";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {catchError} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {Router} from "@angular/router";


@Component({
    selector: 'app-allpermits',
    templateUrl: './allpermits.component.html',
    styleUrls: ['./allpermits.component.css']
})
export class AllpermitsComponent implements OnInit {

    user: UserEntityDto;
    title$: Observable<Titles[]>;
    stepOneForm: FormGroup = new FormGroup({});
    public static permitId: string | number | string[];


    constructor(private store$: Store<any>,
                private service: UserEntityService,
                private titleService: TitlesService,
                private router: Router,
                private formBuilder: FormBuilder) {
        this.title$ = titleService.entities$;
        titleService.getAll().subscribe();
        this.showswal();
    }

    ngOnInit(): void {


        this.stepOneForm = this.formBuilder.group({
                id: [''],
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                userName: ['', Validators.required],
                personalContactNumber: ['', Validators.required],
                email: ['', Validators.required],
                userRegNo: [''],
                enabled: [''],
                accountExpired: [''],
                accountLocked: [''],
                credentialsExpired: [''],
                status: [''],
                registrationDate: [''],
                title: [''],
            },
            {
                // validators: ConfirmedValidator('credentials', 'confirmCredentials')
            });
        this.store$.select(selectUserInfo).subscribe(
            (l) => {
                console.log(`The id is ${l.id}`);
                return this.service.getByKey(`${l.id}/`).subscribe(
                    (u) => {
                        console.log(`The id is ${l.id} vs ${u.id}`);
                        this.stepOneForm.patchValue(u);
                        return this.user = u;
                    }, catchError((err: HttpErrorResponse) => {
                        console.log((err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`);
                        return of(loadResponsesFailure({
                            error: {
                                payload: 'Your request could not be processed at the moment, try again later.',
                                status: err.status,
                                response: '99'
                            }
                        }));
                    })
                );
            }
        );


    }

    onClickSave(valid: boolean) {
        if (valid) {


            this.user = {...this.user, ...this.stepOneForm.value};


            this.service.update(this.user).subscribe(
                (a) => {

                    this.store$.dispatch(
                        loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Profile Successfully Updated.`,
                                status: 200
                            }
                        })
                    );
                    return this.store$.dispatch(Go({
                        payload: null,
                        link: 'profile',
                        redirectUrl: 'profile'
                    }));


                },
                catchError(
                    (err: HttpErrorResponse) => {
                        return of(loadResponsesFailure({
                            error: {
                                payload: err.error,
                                status: err.status,
                                response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                            }
                        }));
                    }));


        } else {
            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Some required details are missing, kindly recheck',
                    status: 100,
                    response: '05'
                }
            }));
        }
    }

    public gotoHome() {
        this.router.navigate(['/home']);  // define your component where you want to go

    }

    public showswal() {
        swal.fire({
            title: 'Enter Permit Number',
            html: '<div class="form-group">' + '<input id="input-field" placeholder="Permit Number" type="text" class="form-control" />' + '</div>',
            showCancelButton: true,
            customClass: {confirmButton: 'btn btn-success', cancelButton: 'btn btn-danger',},
            buttonsStyling: false
        }).then(function (result) {
            if (result.value) {
                AllpermitsComponent.retrieve_permits($("#input-field").val())

                swal.fire({
                    icon: 'success',
                    html: 'Permit id: <strong>' + $('#input-field').val() + ' is being retrieved</strong>',
                    customClass: {confirmButton: 'btn btn-success',},
                    buttonsStyling: false
                }).then((result) => {
                    // AllpermitsComponent.permitId =$("#input-field").val();
                    //  alert( AllpermitsComponent.permitId)
                    //AllpermitsComponent.retrieve_permits()

                      //  alert($('#input-field').val())

                    // AllpermitsComponent.retrieve_permits($('#input-field').val())
                });
            } else {
                swal.fire({
                    title: 'Cancelled',
                    text: 'You Did Not Enter A Permit Number :)',
                    icon: 'error',
                    customClass: {confirmButton: "btn btn-info",},
                    buttonsStyling: false
                }).then((result) => {
                    if (result.value) {
                        window.location.href = "/dashboard";
                    }
                });
            }
        });
    }

    private static retrieve_permits(permitId: string | number | string[]) {
        alert(permitId)


    }


}
