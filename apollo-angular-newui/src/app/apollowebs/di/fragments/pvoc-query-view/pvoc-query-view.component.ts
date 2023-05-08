import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-pvoc-query-view',
    templateUrl: './pvoc-query-view.component.html',
    styleUrls: ['./pvoc-query-view.component.css']
})
export class PvocQueryViewComponent implements OnInit {
    queryStatuses = [
        {
            "status": "conclusion",
            "description": "Final Response to query"
        },
        {
            "status": "response",
            "description": "Response to query"
        }
    ]
    form: FormGroup
    message: string
    loading: boolean
    errorMessages: any[]

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) private data: any, private  pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            responseData: [""],
            serialNumber: [this.data.serialNumber],
            queryAnalysis: ["None"],
            conclusion: ["None"],
            responseType: ["response"]
        })
        this.loading = false
    }

    sendResponse() {
        this.pvocService.sendPvocQueryResponse(this.form.value, this.data.queryId)
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
