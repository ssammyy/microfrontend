import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-generate-auction-kra-report',
    templateUrl: './generate-auction-kra-report.component.html',
    styleUrls: ['./generate-auction-kra-report.component.css']
})
export class GenerateAuctionKraReportComponent implements OnInit {
    maxDate = new Date().toISOString().split("T")[0]
    minDate = '2020-06-01'
    message: string
    form: FormGroup
    loading: boolean = false

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            startDate: [this.maxDate, [Validators.required]],
            endDate: [this.maxDate, [Validators.required]]
        })
    }

    generateReport() {
        let startDate = new DatePipe("En-US").transform(this.form.value.startDate, "yyyyMMdd")
        let endDate = new DatePipe("En-US").transform(this.form.value.endDate, "yyyyMMdd")
        this.loading = true
        this.diService.downloadDocument("/api/v1/download/auction/report/" + startDate + "/" + endDate)
        this.loading = false
        this.dialogRef.close(true)
    }

}
