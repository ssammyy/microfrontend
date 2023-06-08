import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";

@Component({
    selector: 'app-update-exemption-task',
    templateUrl: './update-exemption-task.component.html',
    styleUrls: ['./update-exemption-task.component.css']
})
export class UpdateExemptionTaskComponent implements OnInit {
    validityDays=[
        {
            name: "3_MONTHS",
            description: "3 Months"
        },
        {
            name: "6_MONTHS",
            description: "6 Months"
        },
        {
            name: "1_YEAR",
            description: "1 Year"
        }
    ]
    sectionOfficerStatus = [
        {
            name: 'APPROVE',
            description: 'Approve Exemption',
            section: false
        },
        {
            name: 'REJECT',
            description: 'Reject Exemption',
            section: false
        },
        {
            name: 'DEFFERED',
            description: 'Deffer Exemption',
            section: true
        }
    ]
    approvalStatus: any[]
    form: FormGroup
    errors: any
    loading: boolean = false

    constructor(private pvocService: PVOCService, private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,) {
    }

    ngOnInit(): void {
        // Status
        this.approvalStatus = []
        for (let d of this.sectionOfficerStatus) {
            if (d.section == this.data.section) {
                this.approvalStatus.push(d)
            } else {
                this.approvalStatus.push(d)
            }
        }
        // Form
        this.form = this.fb.group({
            status: [null, Validators.required],
            taskId: [this.data.taskId],
            certificateValidity: ["NA",Validators.required],
            remarks: [null, Validators.required]
        })
    }

    saveRecord() {
        this.loading = true
        this.pvocService.updateExemptionStatus(this.data.recordId, this.data)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dialogRef.close(true)
                    } else {
                        this.errors = res.errors
                        this.pvocService.showError(res.message)
                    }
                    this.loading = false
                }
            )

    }

}
