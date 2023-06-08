import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

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
            remarks: ['Motor vehicle checklist'],
            items: [[]]
        })
        this.motorVehicleChecklist.statusChanges
            .subscribe(
                status => {
                    if (this.selectedItems && this.selectedItems.length>0) {
                        let data=this.motorVehicleChecklist.value
                        let v = this.validateItems(data)
                        this.valid.emit(v)
                        // At least one vehicle item selected
                        if (v) {
                            console.log(data)
                            this.vehicleDetails.emit(data)
                        } else {
                            this.vehicleDetails.emit(null)
                        }
                    } else {
                        this.valid.emit(true)
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
