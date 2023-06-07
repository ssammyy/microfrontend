import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {SweetAlertIcon} from "sweetalert2";

@Component({
    selector: 'app-process-rejection',
    templateUrl: './process-rejection.component.html',
    styleUrls: ['./process-rejection.component.css']
})
export class ProcessRejectionComponent implements OnInit {
    message: any
    title: any
    loading = false
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        console.log(this.data)
        this.title = this.data.title
        this.form = this.fb.group({
            approvalStatus: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.loading = true
        let result = 'approve'
        if (this.form.value.approvalStatus !== '1') {
            result = 'reject'
        }
        // Validate compliance needs confirmation
        if (this.data.dataMap.hasOwnProperty('compliantStatus')) {
            // Check non compliance
            if (this.form.value.approvalStatus === '1' && this.data.dataMap.compliantStatus === 0) {
                let icon: SweetAlertIcon = 'warning'
                this.diService.showConfirmation(`Are you sure you want ${result} non-compliance request on this consignment? \nThis will send a rejection message for all items in the consignment and the action is not reversible`, (res) => {
                    if (res) {
                        this.sendRequest()
                    } else {
                        this.loading = false;
                    }
                }, icon)
            } else if (this.form.value.approvalStatus === '0') {
                this.diService.showConfirmation(`Are you sure you want reject non-compliance on this consignment? This will send the consignment back to the supervisor for review`, (res) => {
                    if (res) {
                        this.sendRequest()
                    } else {
                        this.loading = false;
                    }
                })
            } else {
                this.sendRequest()
            }
        } else {
            this.sendRequest()
        }
    }

    sendRequest() {
        this.diService.sendConsignmentDocumentAction(this.form.value, this.data.cdUuid, "process/approve-reject/" + this.data.taskId)
            .subscribe(
                res => {
                    this.loading = false
                    if (res.responseCode == "00") {
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