import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-generate-manifest-report',
    templateUrl: './generate-manifest-report.component.html',
    styleUrls: ['./generate-manifest-report.component.css']
})
export class GenerateManifestReportComponent implements OnInit {

    public form: FormGroup;
    message: any;
    loading = false

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            stationId: [null],
            startDate: [null],
            endDate: [null],
            importer: [null],
            manifest: [null],
            bill_number: [null],
        })
    }

    saveRecord() {
        let filters = {}
        Object.keys(this.form.value).forEach(k => {
            if (this.form.value[k]) {
                if (k == 'startDate') {
                    filters['date__start'] = this.form.value[k]
                } else if (k === 'endDate') {
                    filters['date__end'] = this.form.value[k]
                } else {
                    filters[k] = this.form.value[k]
                }
            }
        })
        let data = {
            filters: filters,
            reportName: 'manifest_report'
        }
        this.dialogRef.close(data)
    }

}
