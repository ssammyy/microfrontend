import {Component, OnInit} from '@angular/core';
import {StandardDraft} from "../../../../core/store/data/std/request_std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";

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

    constructor(private  standardDevelopmentService: PublishingService,
                private formBuilder: FormBuilder,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
    ) {
    }

    ngOnInit(): void {
        this.prepareDraftStandardFormGroup = this.formBuilder.group({
            title: ['', Validators.required],
            officer: ['', Validators.required],
            version: ['', Validators.required],
            requestedBy: ['', Validators.required],
            uploadedFiles: ['', Validators.required]


        });
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    get formPrepareJustification(): any {
        return this.prepareDraftStandardFormGroup.controls;
    }

    // public onUpload(standardDraft: StandardDraft): void {
    //     console.log(standardDraft);
    //     this.standardDevelopmentService.uploadStdDraft(standardDraft).subscribe(
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
        this.SpinnerService.show();
        this.standardDevelopmentService.uploadStdDraft(this.prepareDraftStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Successfully submitted draft standard`);
             //   this.onClickSaveUploads(response.body.savedRowID)
                this.prepareDraftStandardFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
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
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }
}
