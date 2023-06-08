import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-mv-inspection-upload-file-report',
    templateUrl: './mv-inspection-upload-file-report.component.html',
    styleUrls: ['./mv-inspection-upload-file-report.component.css']
})
export class MvInspectionUploadFileReportComponent implements OnInit {
    public form: FormGroup
    public selectedFile: File
    public message: String
    loading = false

    constructor(public dialogRef: MatDialogRef<any>,
                private fb: FormBuilder,
                @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            comment: ['', Validators.required],
            referenceNumber: ['', Validators.required]
        })
    }

    closeDialog() {
        this.dialogRef.close(false)
    }

    onFileSelected(event: any) {
        let files = event.target.files
        if (files.length > 0) {
            this.selectedFile = files[0]
        } else {
            this.selectedFile = null
        }
        console.log(this.selectedFile)
    }

    saveRecord() {
        if (this.selectedFile) {
            this.loading = true
            this.diService.uploadMinistryChecklist(this.selectedFile, this.form.value.comment, this.form.value.referenceNumber, this.data.id)
                .subscribe(
                    res => {
                        this.loading = false
                        if (res.responseCode === "00") {
                            this.diService.showSuccess(res.message, () => {
                                this.dialogRef.close(true)
                            })
                        } else {
                            this.diService.showError(res.message, null)
                        }
                    },
                    error => {
                        this.loading = false
                    }
                )
        } else {
            this.message = "Please select file to upload"
        }

    }
}
