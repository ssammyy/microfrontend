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
    form: FormGroup

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<any>, private fb: FormBuilder, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            permitNumber:['',],
            ssfSubmissionDate: ['', Validators.required],
            brandName: ['', [Validators.required, Validators.maxLength(150)]],
            productDescription: ['', [Validators.required, Validators.maxLength(150)]],
            description: ['',Validators.maxLength(256)],
        })
    }

    saveSsfRecord() {
        this.message = null
        this.diService.saveSSFDetails(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })

                    } else {
                        this.message = res.message
                    }
                }
            )
    }
}
