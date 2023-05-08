import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";

@Component({
    selector: 'app-pvoc-queries',
    templateUrl: './pvoc-queries.component.html',
    styleUrls: ['./pvoc-queries.component.css']
})
export class PvocQueriesComponent implements OnInit {
    form: FormGroup
    message: any
    loading = false
    certNumber: any
    errorMessages: any

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        console.log(this.data)
        this.certNumber = this.data.certNumber
        this.form = this.fb.group({
            partnerId: [this.data.partnerId, Validators.required],
            documentType: [this.data.documentType, Validators.required],
            certNumber: [this.data.certNumber, Validators.required],
            rfcNumber: [this.data.rfcNumber],
            invoiceNumber: [this.data.invoiceNumber],
            ucrNumber: [this.data.ucrNumber],
            kebsQuery: [null, [Validators.required, Validators.minLength(20)]]
        })
    }

    saveRecord() {
        this.pvocService.sendPvocQuery(this.form.value)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.dialogRef.close(true)
                    } else {
                        this.message = res.message
                        this.errorMessages = res.errors
                    }
                },
                error => {
                    this.message = error.message
                }
            )
    }

}
