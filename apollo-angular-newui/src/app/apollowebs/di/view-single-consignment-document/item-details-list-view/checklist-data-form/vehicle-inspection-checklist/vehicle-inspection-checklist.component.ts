import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-vehicle-inspection-checklist',
    templateUrl: './vehicle-inspection-checklist.component.html',
    styleUrls: ['./vehicle-inspection-checklist.component.css']
})
export class VehicleInspectionChecklistComponent implements OnInit {
    @Output() private vehicleDetails = new EventEmitter<any>();
    motorVehicleChecklist: FormGroup
    @Input() stations: any[]
    @Input() categories: any[]
    @Input() itemList: any[]
    @Output() valid =new EventEmitter<Boolean>()
    selectedItems: []

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.valid.emit(true)
        this.motorVehicleChecklist = this.fb.group({
            remarks: ['Motor vehicle checklist', Validators.required],
            items: [[]]
        })
        this.motorVehicleChecklist.statusChanges
            .subscribe(
                res => {
                    console.log(res)
                    if (this.motorVehicleChecklist.valid) {
                        let data = this.motorVehicleChecklist.value
                        let v = this.validateItems(data)
                        this.valid.emit(v)
                        if (v) {
                            this.vehicleDetails.emit(data)
                        } else {
                            this.vehicleDetails.emit(null)
                        }
                    } else {
                        this.valid.emit(false)
                        console.log("invalid")
                        this.vehicleDetails.emit(null)
                    }
                }
            )
    }

    itemsSelected(items: any) {
        this.motorVehicleChecklist.patchValue({
            items: items
        })
        this.selectedItems = items
    }

    validateItems(data: any) {
        let validItems=true;
        if (data.items && data.items.length > 0) {
            for (let itm of data.items) {
                if (!itm.checklist) {
                    itm.valid=false
                    console.log("Invalid item: "+itm.id)
                    validItems=false
                }
            }
        }
        return validItems
    }

}
