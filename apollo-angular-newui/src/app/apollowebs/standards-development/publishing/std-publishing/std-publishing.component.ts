import {Component, OnInit} from '@angular/core';
import {StandardDraft} from "../../../../core/store/data/std/request_std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import swal from "sweetalert2";
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";

declare const $: any;

@Component({
    selector: 'app-std-publishing',
    templateUrl: './std-publishing.component.html',
    styleUrls: ['./std-publishing.component.css']
})
export class StdPublishingComponent implements OnInit {
    public itemId: string = "1";
    public groupId: string = "draft";
    public type: string = "DraftDocument";
    public stdDraft !: StandardDraft | undefined;
    public prepareDraftStandardFormGroup!: FormGroup;
    public uploadedFiles: FileList;
    fullname = '';

    constructor(private  publishingService: PublishingService,
                private formBuilder: FormBuilder,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private store$: Store<any>,
                private router:Router,
    ) {
    }

    ngOnInit(): void {
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.fullname = u.fullName;
        });
        this.prepareDraftStandardFormGroup = this.formBuilder.group({
            title: ['', Validators.required],
            versionNumber: ['', Validators.required],
            requestorId: ['', Validators.required],
            uploadedFiles: ['', Validators.required]


        });
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    get formPrepareJustification(): any {
        return this.prepareDraftStandardFormGroup.controls;
    }

    // public onUpload(standardDraft: StandardDraft): void {
    //     console.log(standardDraft);
    //     this.publishingService.uploadStdDraft(standardDraft).subscribe(
    //         (response: StandardDraft) => {
    //             console.log(response);
    //             //this.getTCSECTasks();
    //             this.showToasterSuccess("Success", "Successfully submitted draft standard")
    //         },
    //         (error: HttpErrorResponse) => {
    //             alert(error.message);
    //         }
    //     )
    // }
    onUpload(): void {
        if (this.uploadedFiles != null) {
            if (this.uploadedFiles.length > 0) {
                this.SpinnerService.show();
                this.publishingService.uploadStdDraft(this.prepareDraftStandardFormGroup.value).subscribe(
                    (response) => {
                        console.log(response);
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, `Successfully submitted draft standard`);
                        this.onClickSaveUploads(response.body.savedRowID)
                        this.prepareDraftStandardFormGroup.reset();
                    },
                    (error: HttpErrorResponse) => {
                        this.SpinnerService.hide();
                        console.log(error.message);
                    }
                );
            } else {
                this.showToasterError("Error", `Please Upload a Draft Standard.`);
            }
        } else {
            this.showToasterError("Error", `Please Upload a Draft Standard.`);
        }
    }

    onClickSaveUploads(draftStandardID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.publishingService.uploadFileDetails(draftStandardID, formData, "DraftStandard").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Draft Standard Submitted To Head Of Publishing.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.router.navigate(['/draftStandard']);
                },
            );
        }

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
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }
}
