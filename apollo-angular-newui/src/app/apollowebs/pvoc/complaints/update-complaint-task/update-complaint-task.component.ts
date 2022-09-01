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
            description: 'Approve Complaint',
            section: false
        },
        {
            name: 'RECOMMEND',
            description: 'Recommend Action',
            section: true
        },
        {
            name: 'REJECT',
            description: 'Reject Complaint',
            section: false
        },
        {
            name: 'QUERY',
            description: 'Query PVOC Agent',
            section: true
        }
    ]
    complaintStatus: any[]
    recommendations: any[]
    form: FormGroup
    errors: any
    loading: boolean = false

    constructor(private pvocService: PVOCService, private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,) {
    }

    ngOnInit(): void {
        // Status
        this.complaintStatus = []
        this.data.is_pvoc_officer = false
        for (let d of this.sectionOfficerStatus) {
            if (this.data.is_pvoc_officer) {
                if (d.section) {
                    this.complaintStatus.push(d)
                }
            } else {
                if (!d.section) {
                    this.complaintStatus.push(d)
                }
            }
        }
        // Form
        this.form = this.fb.group({
            taskId: [this.data.taskId],
            action: ["0", this.data.is_pvoc_officer ? [Validators.required] : []],
            status: [null, [Validators.required]],
            remarks: [null, Validators.required]
        })
        // Complaint Recommendations
        this.loadData()
    }

    loadData() {
        this.pvocService.getComplaintRecommendations()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.recommendations = res.data
                    }
                }
            )
    }

    saveRecord() {
        this.loading = true
        let pData = this.form.value
        // Convert recommendation to actionId
        pData.action = parseInt(this.form.value.action)
        this.pvocService.updateComplaintStatus(this.data.complaintId, pData)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.pvocService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })

                    } else {
                        this.errors = res.errors
                        this.pvocService.showError(res.message)
                    }
                    this.loading = false
                }
            )
    }
}
