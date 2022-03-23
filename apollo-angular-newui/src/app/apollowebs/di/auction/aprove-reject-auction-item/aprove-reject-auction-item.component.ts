import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-aprove-reject-auction-item',
    templateUrl: './aprove-reject-auction-item.component.html',
    styleUrls: ['./aprove-reject-auction-item.component.css']
})
export class AproveRejectAuctionItemComponent implements OnInit {

    selectedFile: File
    message: String
    form: FormGroup
    loading = false

    constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            approve: ["", Validators.required],
            witnessDesignation: ["", Validators.required],
            witnessName: ["", Validators.required],
            witnessEmail: ["", [Validators.required, Validators.email]],
            remarks: ["", Validators.required]
        })
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

    saveAuctionStatus() {
        this.loading = true
        this.diService.approveRejectAuctionItem(this.selectedFile, this.form.value, this.data.auctionId)
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
