import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-approve-reject-consignment',
    templateUrl: './approve-reject-consignment.component.html',
    styleUrls: ['./approve-reject-consignment.component.css']
})
export class ApproveRejectConsignmentComponent implements OnInit {
    public statuses: any[]
    public form: FormGroup
    message: String;
    complianceStatus: number
    loading: boolean = false

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder,
                @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        console.log(this.data)
        this.complianceStatus = this.data.complianceStatus
        this.statuses = []
        this.data.configurations.CDStatusTypes.forEach(item => {
            if (this.complianceStatus === 1) {
                switch (item.category) {
                    case 'QUERY':
                        break
                    case 'ONHOLD':
                        break
                    case "REJECT":
                        break
                    default:
                        this.statuses.push(item)
                }
            } else if (this.data.documentType === "F") {
                this.statuses.push(item)
            } else {
                if (item.category !== "APPROVE") {
                    this.statuses.push(item)
                }
            }
        })
        this.form = this.fb.group({
            cdStatusTypeId: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    getStatus(statusId: any): any {
        let status = {}
        for (let d of this.statuses) {
            if (d.id == statusId) {
                status = d
                break
            }
        }
        return status
    }


    processResult(res: any) {
        console.log(res)
        this.loading = false
        if (res.responseCode === "00") {
            this.dialogRef.close(true)
        } else {
            this.message = res.message
        }
    }

    processError(err: any) {
        this.loading = false
        console.log(err)
    }

    saveRecord() {
        this.loading = true
        let status = this.getStatus(this.form.value.cdStatusTypeId)
        //console.log(status)
        if (status && status.category === "REJECT") {
            this.diService.showConfirmation(`Are you sure you want to reject consignment with UCR number ${this.data.ucr}`, (result) => {
                //console.log(result)
                if (result) {
                    this.diService.approveReject(this.form.value, this.data.uuid)
                        .subscribe(
                            (res) => {
                                this.processResult(res)
                            },
                            this.processError
                        )
                } else {
                    this.loading = false
                }
            })
        } else {
            this.diService.approveReject(this.form.value, this.data.uuid)
                .subscribe(
                    (res) => {
                        this.processResult(res)
                    },
                    this.processError
                )
        }
    }
}
