import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-generate-cdreport',
    templateUrl: './generate-cdreport.component.html',
    styleUrls: ['./generate-cdreport.component.css']
})
export class GenerateCDReportComponent implements OnInit {


    public form: FormGroup;
    message: any;
    loading = false
    compliant = [
        {
            name: '1',
            description: 'Complaint'
        },
        {
            name: '0',
            description: 'Non-Complaint'
        }
    ]
    sampled = [
        {
            name: '1',
            description: 'Sampled'
        },
        {
            name: '0',
            description: 'Not Sampled'
        }
    ]

    docStatus = [
        {
            name: '1',
            description: 'Verified'
        },
        {
            name: '0',
            description: 'Not Verified'
        }
    ]

    docType = [
        {
            name: 'L',
            description: 'Local Document(L)'
        },
        {
            name: 'F',
            description: 'Foreign Document(F)'
        }
    ]

    constructor(public dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            station_id: [null],
            coc_type: [null],
            sampled: [null],
            doc_type: [null],
            verified: [null],
            compliant: [null],
            startDate: [null],
            endDate: [null],
            exporter: [null],
            importer: [null],
            country: [null],
            chassis: [null],
            officer: [null],
            hs_code: [null]
        })
    }

    saveRecord() {
        this.dialogRef.close(this.diService.formatDownloadReport(this.form.value, 'consignment_report'))
    }

}
