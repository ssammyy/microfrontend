import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-generate-manifest-report',
    templateUrl: './generate-manifest-report.component.html',
    styleUrls: ['./generate-manifest-report.component.css']
})
export class GenerateManifestReportComponent implements OnInit {

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
            manifest: [null],
            bill_number: [null],
        })
    }

    saveRecord() {

        this.dialogRef.close(this.diService.formatDownloadReport(this.form.value, 'manifest_report'))
    }

}
