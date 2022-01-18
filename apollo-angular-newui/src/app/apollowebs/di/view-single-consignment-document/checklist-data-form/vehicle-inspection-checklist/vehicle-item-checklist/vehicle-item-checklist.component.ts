import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-vehicle-item-checklist',
    templateUrl: './vehicle-item-checklist.component.html',
    styleUrls: ['./vehicle-item-checklist.component.css']
})
export class VehicleItemChecklistComponent implements OnInit {

    complianceStatus: any[]
    stations: any[]
    categories: any[]
    form: FormGroup
    itemId: any
    message: any

    constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.itemId = this.data.itemId
        this.complianceStatus = this.data.complianceStatus
        this.categories = this.data.categories
        this.stations=this.data.stations
        let formData = this.data.checklist
        this.form = this.fb.group({
            stationId: [formData? formData.stationId: ''],
            makeVehicle: [formData ? formData.makeVehicle : '', [Validators.required, Validators.maxLength(256)]],
            engineNoCapacity: [formData ? formData.engineNoCapacity : '', Validators.maxLength(256)],
            manufacturerDate: [formData ? formData.manufacturerDate : '', Validators.maxLength(256)],
            ksEasApplicable: [formData?formData.ksEasApplicable: '',Validators.maxLength(23)],
            registrationDate: [formData ? formData.registrationDate : '', Validators.maxLength(256)],
            odemetreReading: [formData ? formData.odemetreReading : '', Validators.maxLength(256)],
            driveRhdLhd: [formData ? formData.driveRhdLhd : '', Validators.maxLength(256)],
            transmissionAutoManual: [formData ? formData.transmissionAutoManual : '', Validators.maxLength(256)],
            colour: [formData ? formData.colour : '', Validators.maxLength(256)],
            overallAppearance: [formData ? formData.overallAppearance : '', Validators.maxLength(256)],
            compliant: [formData ? formData.compliant : '', Validators.required],
            category: [formData ? formData.category : '',],
            submitToMinistry: [formData ? formData.submitToMinistry : '', Validators.required],
            remarks: [formData ? formData.remarks : '', Validators.required]
        })
    }

    addCompliance() {
        this.dialogRef.close(this.form.value)
    }
}
