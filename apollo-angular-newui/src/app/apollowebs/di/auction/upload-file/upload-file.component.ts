import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-upload-file',
    templateUrl: './upload-file.component.html',
    styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent implements OnInit {

    selectedFile: File
    message: String
    form: FormGroup
    categories: any
    cfsStation: any
    loading = false

    constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            auctionReportDate: ["", Validators.required],
            cfsCode: ["", Validators.required],
            categoryCode: ["", Validators.required],
            fileType: ["", Validators.required]
        })
        this.loadCategories()
        this.loadMyCfs()
    }

    loadMyCfs() {
        this.diService.listMyCfs()
            .subscribe(res => {
                if (res.responseCode === '00') {
                    this.cfsStation = res.data
                }
            })
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

    saveAuctionDocumentDocument() {
        this.loading = true
        if (!this.selectedFile) {
            this.message = "Please select file"
            return
        }
        this.diService.uploadAuctionGoodsAndVehicles(this.selectedFile, this.form.value.cfsCode, this.form.value.fileType, this.form.value.categoryCode, formatDate(this.form.value.auctionReportDate, 'dd-MM-yyyy', 'en-UD'))
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
