import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-ministry-inspection-request',
    templateUrl: './ministry-inspection-request.component.html',
    styleUrls: ['./ministry-inspection-request.component.css']
})
export class MinistryInspectionRequestComponent implements OnInit {

    public form: FormGroup;
    public stations: any;
    message: string

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            remarks: ["", Validators.required],
            stationId: ["", Validators.required],
        })
        this.loadMinistryStations()
    }

    loadMinistryStations() {
        this.diService.loadMinistryStations()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.stations = res.data
                    } else {
                        console.log("Failed to load stations")
                    }
                }
            )

    }

    saveDetails() {
        this.diService.requestMinistryChecklist(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message,()=>{
                            this.dialogRef.close(true)
                        })
                    } else{
                        this.message=res.message
                    }
                }
            )
    }

}
