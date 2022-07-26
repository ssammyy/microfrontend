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
        this.certNumber = this.data.corNumber
        this.form = this.fb.group({
            partnerId: [this.data.partner],
            documentType: [this.data.cocType],
            certNumber: [this.certNumber],
            rfcNumber: [this.data.rfcNumber],
            invoiceNumber: [this.data.finalInvoiceNumber],
            ucrNumber: [this.data.ucrNumber],
            kebsQuery: [null, [Validators.required]]
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
