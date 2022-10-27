import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-generate-report',
    templateUrl: './generate-report.component.html',
    styleUrls: ['./generate-report.component.css']
})
export class GenerateReportComponent implements OnInit {
    public form: FormGroup;
    message: any;
    loading = false

    constructor(public dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            station_id: [null],
            startDate: [null],
            endDate: [null],
            importer: [null],
            exporter: [null],
            clearing_agent: [null],
            hs_code: [null],
            ucr_number: [null]
        })
    }

    saveRecord() {
        this.dialogRef.close(this.diService.formatDownloadReport(this.form.value, 'idf_report'))
    }

}
