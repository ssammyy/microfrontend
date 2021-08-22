import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-attachment-dialog',
    templateUrl: './attachment-dialog.component.html',
    styleUrls: ['./attachment-dialog.component.css']
})
export class AttachmentDialogComponent implements OnInit {
    selectedFile: File
    message: string
    form: FormGroup

    constructor(private dialogRef: MatDialogRef<any>,
                @Inject(MAT_DIALOG_DATA) public data: any,
                private fb: FormBuilder,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            description: ['', Validators.required]
        })
    }

    onFileSelected(event: any) {
        if (event.target.files.length > 0) {
            this.selectedFile = event.target.files[0]
        }
    }

    saveRecord() {
        this.diService.uploadConsignmentDocumentAttachment(this.selectedFile, this.form.value.description, this.data.id)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dialogRef.close(true)
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

    closeDialog() {
        this.dialogRef.close(false)
    }
}
