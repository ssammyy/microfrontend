import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-generate-general-report',
    templateUrl: './generate-general-report.component.html',
    styleUrls: ['./generate-general-report.component.css']
})
export class GenerateGeneralReportComponent implements OnInit {

    public form: FormGroup;
    message: any;
    loading = false

    constructor(public dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            stationId: [null],
            doc_type: [null],
            startDate: [null],
            endDate: [null],
            importer: [null],
            country: [null],
            hs_code: [null],
            description: [null]
        })
    }

    saveRecord() {
        this.dialogRef.close(this.diService.formatDownloadReport(this.form.value, 'coc_report'))
    }

}
