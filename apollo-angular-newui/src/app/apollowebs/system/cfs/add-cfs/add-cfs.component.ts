import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-add-cfs',
    templateUrl: './add-cfs.component.html',
    styleUrls: ['./add-cfs.component.css']
})
export class AddCfsComponent implements OnInit {
    form: FormGroup
    regions: any
    message: any
    loading: any = false
    revenueLines: any
    cfsStatuses = [
        {
            name: '1',
            description: 'Active'
        },
        {
            name: '0',
            description: 'Disabled'
        }
    ]

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            cfsCode: [this.data ? this.data.cfsCode : "", [Validators.required]],
            altCfsCode: [this.data ? this.data.altCfsCode : "", [Validators.required]],
            cfsName: [this.data ? this.data.cfsName : "", [Validators.required]],
            revenueLineNumber: [this.data ? this.data.revenueLineNumber : "", [Validators.required]],
            status: [this.data ? this.data.status : "", [Validators.required]],
            description: [this.data ? this.data.description : "", [Validators.required]]
        })
        this.loadRevenueLines()
    }

    loadRevenueLines() {
        this.loading = true
        this.diService.listRevenueLines()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.revenueLines = res.data
                    }
                    this.loading = false
                },
                error => {
                    console.error(error)
                    this.loading = false
                }
            )
    }

    isValid() {
        return this.form.valid
    }

    saveCfs() {
        this.loading = true
        let call = this.diService.saveCfs(this.form.value)
        if (this.data) {
            call = this.diService.updateCfs(this.form.value, this.data.id)
        }
        call.subscribe(
            res => {
                if (res.responseCode === "00") {
                    this.diService.showSuccess(res.message, () => {
                        this.dialogRef.close(true)
                    })
                } else {
                    this.message = res.message
                    this.diService.showError(res.message)
                }
                this.loading = false;
            },
            error => {
                console.error(error)
                this.loading = false
            }
        )

    }

}
