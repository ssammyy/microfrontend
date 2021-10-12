import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-scf-details-form',
    templateUrl: './scf-details-form.component.html',
    styleUrls: ['./scf-details-form.component.css']
})
export class ScfDetailsFormComponent implements OnInit {
    form: FormGroup
    message: any
    loading=false
    samplingMethods=[
        {
            name: "Random Sampling",
            description: "Random Sampling"
        },
        {
            name: "Systematic sampling",
            description: "Systematic sampling"
        },
        {
            name: "Stratified sampling",
            description: "Stratified sampling"
        },
        {
            name: "Clustered sampling",
            description: "Clustered sampling"
        },
        {
            name: "Convenience sampling",
            description: "Convenience sampling"
        },
        {
            name: "Quota sampling",
            description: "Quota sampling"
        },
        {
            name: "Judgement (or Purposive) Sampling",
            description: "Systematic sampling"
        },
        {
            name: "Snowball sampling",
            description: "Snowball sampling"
        }
    ]

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, private fb: FormBuilder, private diService: DestinationInspectionService, public dialogRef: MatDialogRef<any>) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            manufacturerTraderName: ['', Validators.required],
            manufacturerTraderAddress: ['', Validators.required],
            brandName: ['', Validators.required],
            batchNumber: ['', Validators.required],
            batchSize: ['', Validators.required],
            sampleSize: ['', Validators.required],
            samplingMethod: ['', Validators.required],
            reasonsForCollectingSamples: ['', Validators.required],
            witnessName: ['', Validators.required],
            witnessDesignation: ['', Validators.required],
            remarks: ['', Validators.required],
        })
    }

    saveScfRecord() {
        this.loading=true
        this.message = null
        this.diService.saveSCFDetails(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    this.loading=false
                    if (res.responseCode == "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading=false
                }
            )
    }
}
