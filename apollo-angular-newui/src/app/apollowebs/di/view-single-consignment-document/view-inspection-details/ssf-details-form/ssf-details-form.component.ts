import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-ssf-details-form',
    templateUrl: './ssf-details-form.component.html',
    styleUrls: ['./ssf-details-form.component.css']
})
export class SsfDetailsFormComponent implements OnInit {
    message: any
    loading = false
    form: FormGroup

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<any>, private fb: FormBuilder, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            permitNumber: ['',],
            ssfSubmissionDate: ['', Validators.required],
            returnOrDispose: ['', Validators.required],
            conditionOfSample: ['', [Validators.required, Validators.maxLength(100)]],
            description: ['', Validators.maxLength(256)],
        })
    }

    saveSsfRecord() {
        this.loading = true
        this.message = null
        this.diService.saveSSFDetails(this.form.value, this.data.uuid)
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
