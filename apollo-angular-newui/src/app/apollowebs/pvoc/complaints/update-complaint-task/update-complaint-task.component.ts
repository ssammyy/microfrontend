import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-update-complaint-task',
    templateUrl: './update-complaint-task.component.html',
    styleUrls: ['./update-complaint-task.component.css']
})
export class UpdateComplaintTaskComponent implements OnInit {
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
            name: 'QUERY',
            description: 'Query PVOC Agent',
            section: true
        }
    ]
    complaintStatus: any[]
    form: FormGroup
    errors: any
    loading: boolean = false

    constructor(private pvocService: PVOCService, private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,) {
    }

    ngOnInit(): void {
        // Status
        this.complaintStatus = []
        for (let d of this.sectionOfficerStatus) {
            if (d.section === this.data.pvoc_officer) {
                this.complaintStatus.push(d)
            } else {
                this.complaintStatus.push(d)
            }
        }
        // Form
        this.form = this.fb.group({
            taskId: [this.data.taskId],
            action: ["NA", Validators.required],
            remarks: [null, Validators.required]
        })
    }

    saveRecord() {
        this.loading = true
        this.pvocService.updateComplaintStatus(this.data.recordId, this.data)
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
