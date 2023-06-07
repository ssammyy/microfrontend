import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-add-attachment',
    templateUrl: './add-attachment.component.html',
    styleUrls: ['./add-attachment.component.css']
})
export class AddAttachmentComponent implements OnInit {

    selectedFile: File
    message: String
    form: FormGroup
    categories: any
    loading = false

    constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            remarks: ["", Validators.required]
        })
        this.loadCategories()
    }

    loadCategories() {
        this.diService.listAuctionCategories()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.categories = res.data
                    }
                }
            )
    }

    uploadAuctionItems(event: any) {
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

    saveAuctionDocumentDocument(event: any) {
        this.loading = true
        if (this.selectedFile) {
            this.diService.uploadAuctionReport(this.selectedFile, this.data.requestId, this.form.value.remarks)
                .subscribe(
                    res => {
                        this.loading = false
                        if (res.responseCode === "00") {
                            this.diService.showSuccess(res.message, () => {
                                this.dialogRef.close(true)
                            })
                        } else {
                            this.diService.showError(res.message)
                        }
                    },
                    error => {
                        this.loading = false
                    }
                )
        } else {
            this.loading = false;
            this.diService.showError("Please select file to upload")
        }


    }

}
