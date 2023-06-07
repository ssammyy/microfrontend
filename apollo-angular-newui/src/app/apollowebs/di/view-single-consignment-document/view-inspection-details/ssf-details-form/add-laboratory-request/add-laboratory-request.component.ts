import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-add-laboratory-request',
    templateUrl: './add-laboratory-request.component.html',
    styleUrls: ['./add-laboratory-request.component.css']
})
export class AddLaboratoryRequestComponent implements OnInit {
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            laboratoryName: [this.data.request?this.data.request.laboratoryName:'', Validators.required],
            testParameters: [this.data.request?this.data.request.testParameters:'', Validators.required],
            remarks: [this.data.request?this.data.request.remarks:'']
        })
    }

    saveRecord() {
        this.dialogRef.close(this.form.value)
    }

}
