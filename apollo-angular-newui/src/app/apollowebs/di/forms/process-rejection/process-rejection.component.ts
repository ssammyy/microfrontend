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
        let description = ''
        if (this.form.value.approvalStatus !== '1') {
            result = 'reject'
            description = 'A rejection will send the consignment to back to inspection officer for review'
        }
        if (this.data.dataMap.compliantStatus !== 1) {
            // Check non compliance
            if (this.form.value.approvalStatus === '1') {
                let icon: SweetAlertIcon = 'warning'
                this.diService.showConfirmation(`Are you sure you want ${result} non-compliance request on this consignment? \nThis will send a rejection message for all items in the consignment and the action is not reversible`, (res) => {
                    if (res) {
                        this.sendRequest()
                    } else {
                        this.loading = false;
                    }
                }, icon)
            } else {
                this.diService.showConfirmation(`Are you sure you want ${result} compliance on this consignment?`, (res) => {
                    if (res) {
                        this.sendRequest()
                    } else {
                        this.loading = false;
                    }
                })
            }
        } else {
            this.diService.showConfirmation(`Are you sure you want to ${result} compliance on this consignment? ${description}`, (res) => {
                if (res) {
                    this.sendRequest()
                } else {
                    this.loading = false;
                }
            })
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