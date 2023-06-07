import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-blacklist-approve',
    templateUrl: './blacklist-approve.component.html',
    styleUrls: ['./blacklist-approve.component.css']
})
export class BlacklistApproveComponent implements OnInit {
    loading: boolean = false
    message: any;
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.loading = true
        this.diService.sendConsignmentDocumentAction(this.data, this.data.uuid, "approve-blacklist")
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
                }
            )
    }
}
