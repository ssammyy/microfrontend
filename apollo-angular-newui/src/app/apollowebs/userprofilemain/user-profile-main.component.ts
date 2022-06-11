import swal from "sweetalert2";
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
import {AdoptionOfEaStdsService} from "../../core/store/data/std/adoption-of-ea-stds.service";
import {DomSanitizer} from "@angular/platform-browser";

class ImageSnippet {
    pending: boolean = false;
    status: string = 'init';

    constructor(public src: string, public file: File) {
    }
}

declare const $: any;

@Component({
    selector: 'app-userprofilemain',
    templateUrl: './user-profile-main.component.html',
    styleUrls: ['./user-profile-main.component.css']
})


export class UserProfileMainComponent implements OnInit {
    roles: string[];
    user: UserEntityDto;
    title$: Observable<Titles[]>;
    stepOneForm: FormGroup = new FormGroup({});
    public emailActivationFormGroup!: FormGroup;
    public activateEmailFormGroup!: FormGroup;
    emailVerificationStatus: number;
    userId: number;
    email: string;
    loadingText: string;
    isShowSendEmailForm = true;
    isShowConfirmEmailForm = true;
    imageToShow: any;
    isImageLoading: boolean;
    selectedFile: File;
    thumbnail: any;
    private base64textString: Blob;
    public uploadedFilesB: FileList;


    constructor(private store$: Store<any>,
                private service: UserEntityService,
                private titleService: TitlesService,
                private formBuilder: FormBuilder,
                private levyService: LevyService,
                private uploadSignature: AdoptionOfEaStdsService,
                private sanitizer: DomSanitizer,
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


        this.getVerificationStatus();
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.userId = u.id;
            this.email= u.email;
        });
        this.emailActivationFormGroup = this.formBuilder.group({
            userId: [],
            email: []
        });
        this.activateEmailFormGroup = this.formBuilder.group({
            userId: [],
            email: [],
            verificationToken: []
        });

    }

    ngAfterViewInit() {
        this.loaduserinfo()

    }



    loaduserinfo() {
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            // console.log(this.roles);
            return this.roles = u.roles;

        });

        this.store$.select(selectUserInfo).subscribe(
            (l) => {
                // console.log(`The id is ${l.id}`);
                return this.service.getByKey(`${l.id}/`).subscribe(
                    (u) => {
                        // console.log(`The id is ${l.id} vs ${u.id}`);

                        this.stepOneForm.patchValue(u);
                        this.isImageLoading = true;

                        if (u.signature != null) {
                            let objectURL = 'data:image/jpeg;base64,' + u.signature;
                            this.thumbnail = this.sanitizer.bypassSecurityTrustUrl(objectURL);
                            this.isImageLoading = true

                        }

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
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }

    onClickSave(valid: boolean) {
        // if (valid) {
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


        //  }
        // else {
        //     this.store$.dispatch(loadResponsesFailure({
        //         error: {
        //             payload: 'Some required details are missing, kindly recheck',
        //             status: 100,
        //             response: '05'
        //         }
        //     }));
        // }
    }

    public getVerificationStatus(): void{
        this.levyService.getVerificationStatus().subscribe(
            (response)=> {
                this.emailVerificationStatus = response;
                // console.log(this.emailVerificationStatus);
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
                this.isShowConfirmEmailForm = true;
                // this.router.navigateByUrl('/dashboard').then(r => {});
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Validating Email Address`);
                console.log(error.message);
            }
        );
    }
    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'KEBS QAIMSS'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Check your Email for verification token</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

    handleFileSelect(evt) {
        const files = evt.target.files;
        const file = files[0];

        if (files && file) {
            const reader = new FileReader();
            reader.onload = this._handleReaderLoaded.bind(this);
            reader.readAsBinaryString(file);
            this.selectedFile = file;

        }

    }

    _handleReaderLoaded(readerEvt) {
        this.base64textString = readerEvt.target.result;
    }


    onClickSaveUploads() {
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.uploadSignature.uploadService(formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    console.log(data);
                    swal.fire({
                        title: 'Signature Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    }).then((result) => {
                        if (result.value) {
                            window.location.href = "/profile";
                        }

                    });
                })
        }

    }

}
