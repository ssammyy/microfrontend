import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
    Go,
    loadResponsesFailure,
    loadResponsesSuccess,
    selectUserInfo,
    UserEntityDto,
    UserEntityService
} from '../../core/store';
import {Observable, of} from 'rxjs';
import {Titles, TitlesService} from '../../core/store/data/title';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {LevyService} from "../../core/store/data/levy/levy.service";
import {NotificationService} from "../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";


@Component({
    selector: 'app-userprofilemain',
    templateUrl: './user-profile-main.component.html',
    styleUrls: ['./user-profile-main.component.css']
})


export class UserProfileMainComponent implements OnInit {

    user: UserEntityDto;
    title$: Observable<Titles[]>;
    stepOneForm: FormGroup = new FormGroup({});
    public emailActivationFormGroup!: FormGroup;
    public activateEmailFormGroup!: FormGroup;
    emailVerificationStatus:number;
    userId: number;
    email: string;
    loadingText: string;
    isShowSendEmailForm=true;
    isShowConfirmEmailForm=true;


    constructor(private store$: Store<any>,
                private service: UserEntityService,
                private titleService: TitlesService,
                private formBuilder: FormBuilder,
                private levyService: LevyService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
        this.title$ = titleService.entities$;
        titleService.getAll().subscribe();
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

        this.getVerificationStatus();
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.userId = u.id;
            this.email= u.email;
        });
        this.emailActivationFormGroup = this.formBuilder.group({
            userId:[],
            email:[]
        });
        this.activateEmailFormGroup = this.formBuilder.group({
            userId:[],
            email:[],
            verificationToken: []
        });

    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

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
    public getVerificationStatus(): void{
        this.levyService.getVerificationStatus().subscribe(
            (response)=> {
                this.emailVerificationStatus = response;
                console.log(this.emailVerificationStatus);
            },
            (error: HttpErrorResponse)=>{
                console.log(error.message)
                //alert(error.message);
            }
        );
    }
    toggleDisplayEmailForm() {
        this.isShowSendEmailForm = !this.isShowSendEmailForm;
        this.isShowConfirmEmailForm=true;

    }

    sendEmailVerificationToken(): void {
        this.loadingText = "Generating token ...."
        console.log(this.emailActivationFormGroup.value);
        this.SpinnerService.show();
        this.levyService.sendEmailVerificationToken(this.emailActivationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Token has been generated and sent to your email Address`);
                this.isShowConfirmEmailForm=!this.isShowConfirmEmailForm;
                this.isShowSendEmailForm=true;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Generating Token`);
                console.log(error.message);
            }
        );
        this.hideCloseModal();


    }
    @ViewChild('myModal') myModal;

    openModel() {
        this.myModal.nativeElement.className = 'modal fade show';
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideCloseModal() {
        this.closeModal?.nativeElement.click();
    }

    confirmEmailAddress(): void {
        this.loadingText = "Validating Email Address ...."
        console.log(this.activateEmailFormGroup.value);
        this.SpinnerService.show();
        this.levyService.confirmEmailAddress(this.activateEmailFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Email Address Validated`);
                this.isShowSendEmailForm=true;
                this.isShowConfirmEmailForm=true;
                // this.router.navigateByUrl('/dashboard').then(r => {});
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Validating Email Address`);
                console.log(error.message);
            }
        );
    }

}
