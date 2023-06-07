import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-approve-reject-application',
    templateUrl: './approve-reject-application.component.html',
    styleUrls: ['./approve-reject-application.component.css']
})
export class ApproveRejectApplicationComponent implements OnInit {
    form: FormGroup
    loading = false
    requestId: any

    constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) private data: any, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            approved: [false, [Validators.required]],
            remarks: [null, [Validators.required]]
        })
    }

    saveRecord() {
        let data = this.form.value
        data['requestId'] = this.data.requestId
        this.diService.approveRejectIsm(data)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.diService.showError(res.message)
                    }
                }
            )
    }

}
