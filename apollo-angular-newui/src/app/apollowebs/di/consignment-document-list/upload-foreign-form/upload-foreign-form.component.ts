import {Component, Inject, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-upload-foreign-form',
    templateUrl: './upload-foreign-form.component.html',
    styleUrls: ['./upload-foreign-form.component.css']
})
export class UploadForeignFormComponent implements OnInit {
    selectedFile: File
    message: String
    form: FormGroup
    partners: any
    loading = false

    constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.partners = this.data.partners
        console.log(this.partners)
        this.form = this.fb.group({
            fileType: ["", Validators.required],
            partnerId: [null, Validators.required]
        })
    }

    uploadForeignCoROrCor(event: any) {
        console.log(event.target.files.length)
        if (event.target.files.length > 0) {
            this.selectedFile = event.target.files[0]
        } else {
            this.selectedFile = null
        }
    }

    isValid(): Boolean {
        return this.selectedFile && this.form.valid
    }

    saveForeignDocument() {
        this.loading = true
        this.diService.uploadForeignDocuments(this.selectedFile, this.form.value.fileType, this.form.value.partnerId, this.data.documentType)
            .subscribe(
                res => {
                    this.loading = false
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading = false
                    this.message = error.message
                }
            )

    }
}
