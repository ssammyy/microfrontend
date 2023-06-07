import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-attachment-dialog',
    templateUrl: './attachment-dialog.component.html',
    styleUrls: ['./attachment-dialog.component.css']
})
export class AttachmentDialogComponent implements OnInit {
    selectedFile: File
    message: string
    form: FormGroup
    loading: boolean;

    constructor(private dialogRef: MatDialogRef<any>,
                @Inject(MAT_DIALOG_DATA) public data: any,
                private fb: FormBuilder,
                private toastrService: ToastrService,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            description: ['', Validators.required]
        });
    }

    onFileSelected(event: any) {
        if (event.target.files.length > 0) {
            this.selectedFile = event.target.files[0]
        }
    }

    saveRecord() {
        this.loading =  true;
        this.diService.uploadConsignmentDocumentAttachment(this.selectedFile, this.form.value.description, this.data.id)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.toastrService.success('Record Saved Successfully', 'Success');
                        this.dialogRef.close(true);
                    } else {
                        this.loading=false
                        this.message = res.message;
                    }
                },
                error => {
                    this.loading=false
                }
            );
    }

    closeDialog() {
        this.dialogRef.close(false);
    }
}
