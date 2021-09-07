import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {isNullOrUndefined} from "util";

@Component({
    selector: 'app-checklist-data-form',
    templateUrl: './checklist-data-form.component.html',
    styleUrls: ['./checklist-data-form.component.css']
})
export class ChecklistDataFormComponent implements OnInit {
    generalCheckList: FormGroup
    categories: any[]
    checkListTypes: any[]
    laboratories: []
    message: any
    vehicleDetails: any
    otherDetails: any
    engineeringDetails: any
    agrochemDetails: any
    selectedChecklist: any
    engineeringChecklist: Boolean
    vehicleChecklist: Boolean
    otherChecklist: Boolean
    agrochemChecklist

    constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<any>,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.categories = this.data.configs.categories
        this.checkListTypes = this.data.configs.checkListTypes
        this.laboratories = this.data.configs.laboratories
        this.generalCheckList = this.fb.group({
            category: ['', Validators.required],
            inspection: ['', Validators.required],
            confirmItemType: ['', Validators.required],
            clearingAgent: ['', [Validators.required, Validators.maxLength(256)]],
            overallRemarks: ['', [Validators.required, Validators.maxLength(256)]],
            customsEntryNumber: ['', [Validators.required, Validators.maxLength(256)]],
        })
    }

    setAgrochemChecklist(data: any) {
        this.agrochemDetails = data
    }

    setEngineringChecklist(data: any) {
        this.engineeringDetails = data
    }

    setVehicleChecklist(data: any) {
        this.vehicleDetails = data
    }

    setOtherChecklist(data: any) {
        this.otherDetails = data
    }

    invalidData(): Boolean {
        let result = true;
        if (this.engineeringChecklist) {
            result = isNullOrUndefined(this.engineeringDetails)
        } else if (this.vehicleChecklist) {
            result = isNullOrUndefined(this.vehicleDetails)
        } else if (this.agrochemChecklist) {
            result = isNullOrUndefined(this.agrochemDetails)
        } else if (this.otherChecklist) {
            result = isNullOrUndefined(this.otherDetails)
        }
        console.log("CHK: " + result)
        return result
    }

    checklistChanges(event: any) {
        console.log(event)
        let selections = this.checkListTypes.filter(d => d.id == event.target.value)
        if (selections.length > 0) {
            this.selectedChecklist = selections[0]
            this.engineeringChecklist = this.selectedChecklist.typeName.includes("ENGINEERING")
            this.vehicleChecklist = this.selectedChecklist.typeName.includes("VEHICLE")
            this.otherChecklist = this.selectedChecklist.typeName.includes("OTHER")
            this.agrochemChecklist = this.selectedChecklist.typeName.includes("AGROCHEM")
        }
    }

    saveRecord() {
        this.message = null
        let data = this.generalCheckList.value
        if (this.engineeringChecklist) {
            data["engineering"] = this.engineeringDetails
        } else if (this.vehicleChecklist) {
            data["vehicle"] = this.vehicleDetails
        } else if (this.agrochemChecklist) {
            data["agrochem"] = this.agrochemDetails
        } else if (this.otherChecklist) {
            data["others"] = this.otherDetails
        } else {
            this.message = "Please select at least one checklist"
            return
        }
        this.diService.saveChecklist(this.data.uuid, data)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dialogRef.close(true)
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

}
