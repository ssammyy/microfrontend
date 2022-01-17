import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-re-assign-officer',
    templateUrl: './re-assign-officer.component.html',
    styleUrls: ['./re-assign-officer.component.css']
})
export class ReAssignOfficerComponent implements OnInit {

    public form: FormGroup;
    public officers: any;
    message: any
    loading = false

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadOfficers();
        this.form = this.fb.group({
            officerId: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    // Load data here
    loadOfficers(): void {
        console.log("LIST OFFICERS")
        this.diService.listOfficersForConsignment(this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.officers = res.data
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    console.log(error)
                }
            )
    }

    // Reassign Inspection Officer
    saveRecord() {
        this.loading = true
        let data = this.form.value
        data['reassign'] = true
        this.diService.sendConsignmentDocumentAction(data, this.data.uuid, "reassign-io")
            .subscribe(
                res => {
                    this.loading = false
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(this.form.value)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading = false
                }
            )
    }
}